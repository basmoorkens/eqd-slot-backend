package com.basm.slots.service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.*;
import com.basm.slots.repository.PlayerWalletRepository;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import com.basm.slots.repository.SlotWinningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("PlayerWalletService")
public class PlayerWalletService {

    private final Logger log = LoggerFactory.getLogger(PlayerWalletService.class);

    @Autowired
    private PlayerWalletRepository playerWalletRepository;

    @Autowired
    private SlotWinningRepository slotWinningRepository;

    @Autowired
    private SlotsProperties slotsProperties;

    @Autowired
    private OutgoingPlayerWalletTransactionRepository outgoingPlayerWalletTransactionRepository;

    public boolean hasEnoughFundsInEscrowWalletToPlay(final PlayerWallet playerWallet) {
        return playerWallet.getBalance() >= slotsProperties.getAmountToSpin();
    }

    public PlayerWallet findPlayerWalletByPublicKey(final String publicKey) {
        PlayerWallet playerWallet = playerWalletRepository.findByPublicKey(publicKey);
        if(playerWallet == null ) {
            throw new RuntimeException("The player with publickey " + publicKey + " is not known in the slots application. Please read the instructions on how to create an account.");
        }
        return playerWallet;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SlotWinning registerPlayerWinnings(final String publicKey, final double amount) {
        PlayerWallet playerWallet = findPlayerWalletByPublicKey(publicKey);
        double amountToAddToPlayerWallet = amount - slotsProperties.getAmountToSpin();
        playerWallet.setBalance(playerWallet.getBalance() + amountToAddToPlayerWallet);
        SlotWinning winning = SlotWinning.createNewWinning(amount, playerWallet, slotsProperties.getAmountToSpin());
        slotWinningRepository.save(winning);
        playerWalletRepository.save(playerWallet);
        return winning;
    }

    public double getOpenPlayersBalanceToPay() {
        return playerWalletRepository.getAllOpenPlayerWalletBalances();
    }

    public List<SlotWinning> findLast10WinningsForPlayerWallet(final PlayerWallet playerWallet) {
        return slotWinningRepository.findLast10WinningsForWallet(playerWallet, (new PageRequest(0,10)));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OutgoingPlayerWalletStellarTransaction createPayout(final String publicKey, final double amount) {
        PlayerWallet playerWallet = findPlayerWalletByPublicKey(publicKey);
        if(playerWallet.getBalance() < amount || amount < 0) {
            throw new RuntimeException("You don't have sufficient funds in your wallet to withdraw " + amount);
        }
        OutgoingPlayerWalletStellarTransaction transaction = OutgoingPlayerWalletStellarTransaction.buildOutgoing(amount, publicKey);
        outgoingPlayerWalletTransactionRepository.save(transaction);
        playerWallet.setBalance(playerWallet.getBalance()-amount);
        playerWalletRepository.save(playerWallet);
        log.info("Created payout for " + publicKey + " - " +amount);
        return transaction;
    }

    public PlayerWallet update(PlayerWallet playerWallet) {
        return playerWalletRepository.save(playerWallet);
    }
    
    public List<PlayerWallet> findPlayerWalletsWithBalanceGreaterThen0() {
    	return playerWalletRepository.findWalletsWithFunds();
    }

    public PlayerWallet getGameWallet() {
        return playerWalletRepository.findByPublicKey(slotsProperties.getEscrowWalletPublicKey());
    }

    public PlayerWallet findByPublicKey(final String publicKey) {
        return playerWalletRepository.findByPublicKey(publicKey);
    }
}
