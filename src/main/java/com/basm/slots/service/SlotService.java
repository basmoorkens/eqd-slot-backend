package com.basm.slots.service;

import com.basm.slots.job.IncomingFundsJob;
import com.basm.slots.model.*;
import com.basm.slots.repository.StatefulConfigurationRepository;
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

    @Autowired
    private StatefulConfigurationRepository statefulConfigurationRepository;

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
            SlotResult result = null;
            int counter = 1;
            do {
                try {
                    result = getNextSlotResult(stellarService.getAvailableAmountInSlotsWallet(), playerWallet);
                } catch (IOException e2) {
                    if (counter <= 5) { //retry 5 times if horizon is being a bitch again...
                        log.warn("IOException when fetching wallet for playing slots, retrying " + counter + " time");
                    } else {
                        log.error("IOexception fetching wallet balance", e2);
                        throw new RuntimeException("Stellar network currently unavailable.");
                    }
                }
            } while (result == null);
            SlotWinning winning = playerWalletService.registerPlayerWinnings(playerWallet.getPublicKey(), result.getAmount());
            winning.setSlotFollowNumber(result.getFollowNumber());
            return winning;
        } catch (Exception e) {
            log.error("An unexpected exception occurred whilst spinning", e);
            throw new RuntimeException("An unexpected exception occurred, if this keeps happening contact the our support.");
        }
    }

    protected SlotResult getNextSlotResult(double inGameWallet, PlayerWallet playerWallet) {
        double finalAmount = calculateTrueWalletAmount(inGameWallet);
        if(finalAmount > slotResultFactory.getResultX10().getAmount() && playerWallet.isFirstTimer()) {
            playerWallet.setFirstTimer(false);
            playerWalletService.update(playerWallet);
            return  slotResultFactory.getResultX10();
        }
        StatefulConfiguration config = statefulConfigurationRepository.findByName("production");
        if(config.isBigPayout()) {
            if (finalAmount >= slotResultFactory.getResultX500().getAmount()) {
                config.setBigPayout(false);
                statefulConfigurationRepository.save(config);
                log.info("Paying out big win");
                return slotResultFactory.getResultX500();
            }
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
        return slotResultFactory.getResultX01();
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
