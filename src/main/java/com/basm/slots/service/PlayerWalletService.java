package com.basm.slots.service;

import com.basm.slots.config.SlotsProperties;
import com.basm.slots.model.*;
import com.basm.slots.repository.PlayerWalletRepository;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import com.basm.slots.repository.SlotWinningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("PlayerWalletService")
public class PlayerWalletService {

    @Autowired
    private PlayerWalletRepository playerWalletRepository;

    @Autowired
    private SlotWinningRepository slotWinningRepository;

    @Autowired
    private SlotsProperties slotsProperties;

    public boolean hasEnoughFundsInEscrowWalletToPlay(final PlayerWallet playerWallet) {
        return playerWallet.getBalance() > slotsProperties.getAmountToSpin();
    }

    private PlayerWallet findPlayerWalletForPublicKey(final String publicKey) {
        PlayerWallet wallet = playerWalletRepository.findByPublicKey(publicKey);
        if(wallet == null) {
            throw new RuntimeException("No player found for publicKey " + publicKey + ". To register your public key simply send EQD from your wallet to the game wallet.");
        }
        return wallet;
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
        PlayerWallet playerWallet = findPlayerWalletForPublicKey(publicKey);
        double amountToAddToPlayerWallet = amount - slotsProperties.getAmountToSpin();
        playerWallet.setBalance(playerWallet.getBalance() + amountToAddToPlayerWallet);
        SlotWinning winning = SlotWinning.createNewWinning(amount, playerWallet);
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

    public PlayerWallet update(PlayerWallet playerWallet) {
        return playerWalletRepository.save(playerWallet);
    }
}
