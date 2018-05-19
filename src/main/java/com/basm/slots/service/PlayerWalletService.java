package com.basm.slots.service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.PlayerWalletTransaction;
import com.basm.slots.repository.PlayerWalletRepository;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionScoped;

@Service("PlayerWalletService")
public class PlayerWalletService {

    @Autowired
    private PlayerWalletRepository playerWalletRepository;

    @Autowired
    private OutgoingPlayerWalletTransactionRepository playerWalletTransactionRepository;

    @Autowired
    private SlotsProperties slotsProperties;

    public boolean hasEnoughFundsInEscrowWalletToPlay(final String publicKey) {
        PlayerWallet playerWallet = findPlayerWalletForPublicKey(publicKey);
        if(playerWallet == null ) {
            throw new RuntimeException("Publickey is not known in the slots application");
        }
        return playerWallet.getBalance() > slotsProperties.getAmountToSpin();
    }

    private PlayerWallet findPlayerWalletForPublicKey(final String publicKey) {
        PlayerWallet wallet = playerWalletRepository.findByPublicKey(publicKey);
        if(wallet == null) {
            throw new RuntimeException("No player found for publicKey " + publicKey + ". To register your public key simply send EQD from your wallet to the game wallet.");
        }
        return wallet;
    }

    public double getFundsForPublicKey(final String publicKey) {
        PlayerWallet playerWallet = findPlayerWalletForPublicKey(publicKey);
        return playerWallet.getBalance();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerPlayerWinnings(final String publicKey, final double amount) {
        PlayerWallet playerWallet = findPlayerWalletForPublicKey(publicKey);
        playerWallet.setBalance(playerWallet.getBalance()-slotsProperties.getAmountToSpin());
        OutgoingPlayerWalletTransaction transaction = PlayerWalletTransaction.buildOutgoing(amount, publicKey);
        playerWalletTransactionRepository.save(transaction);
        playerWalletRepository.save(playerWallet);
    }

    public Double getOpenPlayersBalanceToPay() {
        return playerWalletTransactionRepository.findUnprocessedOutgoingTransactionsAmountToPay();
    }
}
