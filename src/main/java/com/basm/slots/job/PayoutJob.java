package com.basm.slots.job;

import com.basm.slots.model.OutgoingPlayerWalletStellarTransaction;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import com.basm.slots.service.PlayerWalletService;
import com.basm.slots.service.StellarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PayoutJob {

    private final Logger log = LoggerFactory.getLogger(PayoutJob.class);

    @Autowired
    private OutgoingPlayerWalletTransactionRepository playerWalletTransactionRepository;

    @Autowired
    private PlayerWalletService playerWalletService;

    @Autowired
    private StellarService stellarService;

    @Scheduled(cron = "${payoutjob.cron.pay.new.tx}")
    public void payOutstandingBalances() {
    log.info("Running payment job");
    long startTime = System.currentTimeMillis();
    PlayerWallet gameWallet = playerWalletService.getGameWallet();
    List<OutgoingPlayerWalletStellarTransaction> openTransactions = playerWalletTransactionRepository.findUnprocessedOutgoingTransactions(new PageRequest(0,20));
    log.info("Found " + openTransactions.size() + " open outgoing transactions");

    for(OutgoingPlayerWalletStellarTransaction tx : openTransactions) {
        processOutgoingTransaction(tx, gameWallet);
    }
    log.info("Paid out " + openTransactions.size() + " open transactions in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Transactional
    public PlayerWallet processOutgoingTransaction(OutgoingPlayerWalletStellarTransaction tx, PlayerWallet gameWallet) {
        tx.markProcessing();
        tx = playerWalletTransactionRepository.save(tx);
        try {
            String resultHash = stellarService.processOpenPaymentTransaction(tx);
            tx.setBlockchainHash(resultHash);
            tx.markProcessed();
            playerWalletTransactionRepository.save(tx);
            log.info("Processed payment " + tx.getId() + " - " + tx.getPublicKey() + " - " + tx.getAmount());
            gameWallet.setBalance(gameWallet.getBalance() - tx.getAmount());
            return playerWalletService.update(gameWallet);
        } catch (Exception e) {
            tx.markFailed("Failed to process TX, see the logs for more details");
            playerWalletTransactionRepository.save(tx);
            log.error("Failed processing payment " + tx.getId() + " - " + tx.getPublicKey() + " - " + tx.getAmount(), e);
            return gameWallet;
        }
    }
}
