package com.basm.slots.job;

import com.basm.slots.model.IncomingPlayerWalletTransaction;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.IncomingPlayerWalletTransactionRepository;
import com.basm.slots.repository.PlayerWalletRepository;
import com.basm.slots.repository.StatefulConfigurationRepository;
import com.basm.slots.service.StellarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class IncomingFundsJob {

    private final Logger log = LoggerFactory.getLogger(IncomingFundsJob.class);

    @Autowired
    private StellarService stellarService;

    @Autowired
    private StatefulConfigurationRepository statefulConfigurationRepository;

    @Autowired
    private IncomingPlayerWalletTransactionRepository incomingPlayerWalletTransactionRepository;

    @Autowired
    private PlayerWalletRepository playerWalletRepository;

    @Scheduled(cron = "${incomingfundsjob.cron.scan.horizon.for.new.incoming.tx}")
    public void scanForIncomingTransactions() {
    log.info("Started scanning HORIZON for incoming transactions");
    long startTime = System.currentTimeMillis();
    StatefulConfiguration config = statefulConfigurationRepository.findByName("production");
    List<IncomingPlayerWalletTransaction> foundTransactions = new ArrayList<>();
    try {
         foundTransactions = stellarService.scanForUnprocessedIncomingPayments(config);
        for(IncomingPlayerWalletTransaction tx : foundTransactions) {
            tx = incomingPlayerWalletTransactionRepository.save(tx);
            log.info("Inserted incoming transaction with ID[" + tx.getId() + "] with TX hash " + tx.getBlockchainHash());
        }
        statefulConfigurationRepository.save(config);
    } catch (IOException e) {
        log.error("Error while scanning for transactions", e);
    }
    log.info("Inserted " + foundTransactions.size() + " incoming transactions in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Scheduled(cron = "${incomingfundsjob.cron.process.new.incoming.tx}")
    @Transactional
    public void processNewIncomingTransactions() {
        log.info("Started processing NEW incoming transactions in database");
        long startTime = System.currentTimeMillis();
        List<IncomingPlayerWalletTransaction> incomingPlayerWalletTransactions = incomingPlayerWalletTransactionRepository.findUnprocessedIncomingTransactions(new PageRequest(0,200));
        for(IncomingPlayerWalletTransaction txIn : incomingPlayerWalletTransactions) {
            processIncomingTransaction(txIn);
        }
        log.info("Processed " + incomingPlayerWalletTransactions.size() + " incoming transactions in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    private void processIncomingTransaction(IncomingPlayerWalletTransaction txIn) {
        try {
            txIn.markProcessing();
            PlayerWallet wallet = playerWalletRepository.findByPublicKey(txIn.getPublicKey());
            if(wallet == null) {
                wallet = PlayerWallet.buildNew(txIn.getPublicKey(), txIn.getAmount());
            } else {
                wallet.setBalance(wallet.getBalance() + txIn.getAmount());
            }
            playerWalletRepository.save(wallet);
            txIn.markProcessed();
            incomingPlayerWalletTransactionRepository.save(txIn);
        } catch (Exception e) {
            log.error("Error processing incoming TX " + txIn.getId(), e);
            txIn.markFailed("Failed to process TX, see the logs for more details");
            incomingPlayerWalletTransactionRepository.save(txIn);
        }
    }

    @Scheduled(cron = "${incomingfundsjob.cron.skip.duplicate.tx}")
    public void skipDuplicateIncomingTransactions() {
        log.info("Checking for duplicate incoming transactions");
        long startTime = System.currentTimeMillis();
        List<IncomingPlayerWalletTransaction> incomingPlayerWalletTransactions = incomingPlayerWalletTransactionRepository.getDuplicateIncomingTransactions(new PageRequest(0, 100));
        for(IncomingPlayerWalletTransaction duplicateTx : incomingPlayerWalletTransactions) {
            duplicateTx.markProcessing();
            duplicateTx.markAsSkipped("Skipped processing as an incoming TX with this blockchainhash was already processed");
        }
        incomingPlayerWalletTransactionRepository.saveAll(incomingPlayerWalletTransactions);
        log.info("Set " + incomingPlayerWalletTransactions.size() + " incoming transactions to SKIPPED in " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
