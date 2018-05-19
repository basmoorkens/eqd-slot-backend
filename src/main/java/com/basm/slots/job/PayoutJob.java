package com.basm.slots.job;

import com.basm.slots.model.OutgoingPlayerWalletTransaction;
import com.basm.slots.model.PlayerWalletTransaction;
import com.basm.slots.repository.OutgoingPlayerWalletTransactionRepository;
import com.basm.slots.service.StellarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PayoutJob {

    private final Logger log = LoggerFactory.getLogger(PayoutJob.class);

    @Autowired
    private OutgoingPlayerWalletTransactionRepository playerWalletTransactionRepository;

    @Autowired
    private StellarService stellarService;

    @Scheduled(cron = "${payoutjob.cron.pay.new.tx}")
    public void payOutstandingBalances() {
    log.info("Running payment job");
    long startTime = System.currentTimeMillis();
    List<OutgoingPlayerWalletTransaction> openTransactions = playerWalletTransactionRepository.findUnprocessedOutgoingTransactions(new PageRequest(0,20));
    log.info("Found " + openTransactions.size() + " open outgoing transactions");

    for(OutgoingPlayerWalletTransaction tx : openTransactions) {
        processOutgoingTransaction(tx);
    }
    log.info("Paid out " + openTransactions.size() + " open transactions in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private void processOutgoingTransaction(OutgoingPlayerWalletTransaction tx) {
        tx.markProcessing();
        tx = playerWalletTransactionRepository.save(tx);
        try {
            String resultHash = stellarService.processOpenPaymentTransaction(tx);
            tx.setBlockchainHash(resultHash);
            tx.markProcessed();
            playerWalletTransactionRepository.save(tx);
            log.info("Processed payment " + tx.getId() + " - " + tx.getPublicKey() + " - " + tx.getAmount());
        } catch (Exception e) {
            tx.markFailed("Failed to process TX, see the logs for more details");
            playerWalletTransactionRepository.save(tx);
            log.error("Failed processing payment " + tx.getId() + " - " + tx.getPublicKey() + " - " + tx.getAmount());
        }
    }
}
