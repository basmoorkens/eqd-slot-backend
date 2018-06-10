package com.basm.slots.job;

import com.basm.slots.model.IncomingPlayerWalletStellarTransaction;
import com.basm.slots.model.PlayerWallet;
import com.basm.slots.model.StatefulConfiguration;
import com.basm.slots.repository.IncomingPlayerWalletTransactionRepository;
import com.basm.slots.repository.StatefulConfigurationRepository;
import com.basm.slots.service.PlayerWalletService;
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
    private PlayerWalletService playerWalletService;

    @Scheduled(cron = "${incomingfundsjob.cron.scan.horizon.for.new.incoming.tx}")
    public void scanForIncomingTransactions() {
    log.info("Started scanning HORIZON for incoming transactions");
    long startTime = System.currentTimeMillis();
    StatefulConfiguration config = statefulConfigurationRepository.findByName("production");
    List<IncomingPlayerWalletStellarTransaction> foundTransactions = new ArrayList<>();
    try {
         foundTransactions = stellarService.scanForUnprocessedIncomingPayments(config);
        for(IncomingPlayerWalletStellarTransaction tx : foundTransactions) {
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
    public void processNewIncomingTransactions() {
        log.info("Started processing NEW incoming transactions in database");
        long startTime = System.currentTimeMillis();
        PlayerWallet gameWallet = playerWalletService.getGameWallet();
        List<IncomingPlayerWalletStellarTransaction> incomingPlayerWalletStellarTransactions = incomingPlayerWalletTransactionRepository.findUnprocessedIncomingTransactions(new PageRequest(0,200));
        for(IncomingPlayerWalletStellarTransaction txIn : incomingPlayerWalletStellarTransactions) {
            gameWallet = processIncomingTransaction(txIn, gameWallet);
        }
        log.info("Processed " + incomingPlayerWalletStellarTransactions.size() + " incoming transactions in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    @Transactional
    public PlayerWallet processIncomingTransaction(IncomingPlayerWalletStellarTransaction txIn, PlayerWallet gameWallet) {
        try {
            txIn.markProcessing();
            PlayerWallet wallet = playerWalletService.findByPublicKey(txIn.getPublicKey());
            processPlayerWalletPart(txIn, wallet);
            txIn.markProcessed();
            incomingPlayerWalletTransactionRepository.save(txIn);
            return processGameWalletPart(txIn, gameWallet);
        } catch (Exception e) {
            log.error("Error processing incoming TX " + txIn.getId(), e);
            txIn.markFailed("Failed to process TX, see the logs for more details");
            incomingPlayerWalletTransactionRepository.save(txIn);
            return gameWallet;
        }
    }

    private PlayerWallet processGameWalletPart(IncomingPlayerWalletStellarTransaction txIn, PlayerWallet gameWallet) {
        gameWallet.setBalance(gameWallet.getBalance() + txIn.getAmount());
        return playerWalletService.update(gameWallet);
    }

    private void processPlayerWalletPart(IncomingPlayerWalletStellarTransaction txIn, PlayerWallet wallet) {
        if(wallet == null) {
            wallet = PlayerWallet.buildNew(txIn.getPublicKey(), txIn.getAmount());
        } else {
            wallet.setBalance(wallet.getBalance() + txIn.getAmount());
        }
        playerWalletService.update(wallet);
    }

    @Scheduled(cron = "${incomingfundsjob.cron.skip.duplicate.tx}")
    public void skipDuplicateIncomingTransactions() {
        log.info("Checking for duplicate incoming transactions");
        long startTime = System.currentTimeMillis();
        List<IncomingPlayerWalletStellarTransaction> incomingPlayerWalletStellarTransactions = incomingPlayerWalletTransactionRepository.getDuplicateIncomingTransactions(new PageRequest(0, 100));
        for(IncomingPlayerWalletStellarTransaction duplicateTx : incomingPlayerWalletStellarTransactions) {
            duplicateTx.markProcessing();
            duplicateTx.markAsSkipped("Skipped processing as an incoming TX with this blockchainhash was already processed");
        }
        incomingPlayerWalletTransactionRepository.saveAll(incomingPlayerWalletStellarTransactions);
        log.info("Set " + incomingPlayerWalletStellarTransactions.size() + " incoming transactions to SKIPPED in " + (System.currentTimeMillis() - startTime) + " ms");
    }
}
