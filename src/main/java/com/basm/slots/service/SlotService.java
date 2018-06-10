package com.basm.slots.service;

import com.basm.slots.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Random;

@Service
public class SlotService {

    private final Logger log = LoggerFactory.getLogger(SlotService.class);

    @Autowired
    private StellarService stellarService;

    @Autowired
    private PlayerWalletService playerWalletService;

    private Random randomGenerator = new Random();

    private SlotResultFactory slotResultFactory = new SlotResultFactory();

	public SlotWinning playSlots(final String publicKey)  {
	    if(publicKey == null || publicKey.isEmpty()) {
	        throw new RuntimeException("Can not play without providing a public key");
        }
        PlayerWallet playerWallet = playerWalletService.findPlayerWalletByPublicKey(publicKey);
        if(playerWalletService.hasEnoughFundsInEscrowWalletToPlay(playerWallet)) {
            return doPlaySlots(playerWallet);
        } else {
	        throw new RuntimeException("Not enough funds to play");
        }
	}

    /**
     * Fetches the next slot result. Create a DB record for a new outgoing transaction for the player.
     * Deduct the cost to play from the player wallet. Deduct the winnings from the game wallet and save all the state.
     * @param playerWallet The playerwallet to credit
     * @return             The slotresult to return to the frontend.
     */
	@Transactional(propagation = Propagation.REQUIRED)
    public SlotWinning doPlaySlots(PlayerWallet playerWallet)  {
        try {
            PlayerWallet gameWallet = playerWalletService.getGameWallet();
            SlotResult result = getNextSlotResult(gameWallet, playerWallet);
            SlotWinning winning = playerWalletService.registerPlayerWinnings(playerWallet.getPublicKey(), result.getAmount());
            winning.setSlotFollowNumber(result.getFollowNumber());
            return winning;
        } catch (Exception e) {
            log.error("An unexpected exception occurred whilst spinning", e);
            throw new RuntimeException("An unexpected exception occurred, if this keeps happening contact our support.");
        }
    }

    protected SlotResult getNextSlotResult(PlayerWallet gameWallet, PlayerWallet playerWallet) {
        double finalAmount = calculateTrueWalletAmount(gameWallet.getBalance());
        if(finalAmount > slotResultFactory.getResultX10().getAmount() && playerWallet.isFirstTimer()) {
            playerWallet.setFirstTimer(false);
            playerWalletService.update(playerWallet);
            return  slotResultFactory.getResultX10();
        }
        return normalSpin(finalAmount);
	}

	protected SlotResult normalSpin(double finalAmount) {
        for(SlotResult result : slotResultFactory.getSlotResultsReversed()) {
            if(finalAmount >= result.getAmount()) {
                if(randomGenerator.nextInt(result.getRandomness())< 1) {
                    return result;
                }
            }
        }
        return slotResultFactory.getResultX0();
    }

    private double calculateTrueWalletAmount(double inGameWallet) {
        double finalAmount = inGameWallet;
        Double outStandingBalance = playerWalletService.getOpenPlayersBalanceToPay();
        if(outStandingBalance != null) {
            finalAmount -= outStandingBalance;
        }
        log.info("Final balance in game wallet: " + finalAmount);
        return finalAmount;
    }

}
