package com.basm.slots.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class PlayerWalletTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private double amount;

    private Date createdDateTime;

    private Date processingDateTime;

    private Date processedDateTime;

    private String publicKey;

    private String statusReason;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String blockchainHash;

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public static IncomingPlayerWalletTransaction buildIncoming(final double amount, final String publicKey, final String blockchainHash) {
        IncomingPlayerWalletTransaction playerWalletTransaction = new IncomingPlayerWalletTransaction();
        fillPlayerWalletTransactionCommonProperties(playerWalletTransaction,amount,publicKey);
        playerWalletTransaction.setBlockchainHash(blockchainHash);
        return playerWalletTransaction;
    }

    private static void fillPlayerWalletTransactionCommonProperties(PlayerWalletTransaction playerWalletTransaction, final double amount, final String publicKey) {
        playerWalletTransaction.setAmount(amount);
        playerWalletTransaction.setCreatedDateTime(new Date());
        playerWalletTransaction.setTransactionStatus(TransactionStatus.NEW);
        playerWalletTransaction.setPublicKey(publicKey);
        playerWalletTransaction.setStatusReason("Created new Transaction");
    }

    public static OutgoingPlayerWalletTransaction buildOutgoing(final double amount, final String publicKey) {
        OutgoingPlayerWalletTransaction playerWalletTransaction = new OutgoingPlayerWalletTransaction();
        fillPlayerWalletTransactionCommonProperties(playerWalletTransaction,amount,publicKey);
        return playerWalletTransaction;
    }

    public void markAsSkipped(final String reason) {
        this.setTransactionStatus(TransactionStatus.SKIPPED);
        this.setProcessedDateTime(new Date());
        this.setStatusReason(reason);
    }

    public void markProcessing() {
        this.setTransactionStatus(TransactionStatus.PROCESSING);
        this.setProcessingDateTime(new Date());
        this.setStatusReason("Started processing transaction");
    }

    public void markProcessed() {
        this.setTransactionStatus(TransactionStatus.DONE);
        this.setProcessedDateTime(new Date());
        this.setStatusReason("Transaction processed successfully");
    }

    public void markFailed(String reason) {
        this.setTransactionStatus(TransactionStatus.FAILED);
        this.setProcessedDateTime(new Date());
        this.setStatusReason(reason);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getProcessedDateTime() {
        return processedDateTime;
    }

    public void setProcessedDateTime(Date processedDateTime) {
        this.processedDateTime = processedDateTime;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Date getProcessingDateTime() {
        return processingDateTime;
    }

    public void setProcessingDateTime(Date processingDateTime) {
        this.processingDateTime = processingDateTime;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
