package com.basm.slots.restmodel;

import java.io.Serializable;
import java.util.Date;

public class PlayerWalletInfoResultLine implements Serializable {

    private String wonTime;

    private String status;

    private Double amount;

    private String blockchainHash;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBlockchainHash() {
        return blockchainHash;
    }

    public void setBlockchainHash(String blockchainHash) {
        this.blockchainHash = blockchainHash;
    }

    public String getWonTime() {
        return wonTime;
    }

    public void setWonTime(String wonTime) {
        this.wonTime = wonTime;
    }
}
