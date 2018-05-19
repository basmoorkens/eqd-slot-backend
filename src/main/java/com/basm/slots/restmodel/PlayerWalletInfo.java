package com.basm.slots.restmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerWalletInfo implements Serializable {

    private double amount;

    private String publicKey;

    private List<PlayerWalletInfoResultLine> lastResults;

    public PlayerWalletInfo(final String publicKey, final double amount) {
        this.amount = amount;
        this.publicKey = publicKey;
    }

    public void addResult(PlayerWalletInfoResultLine result ) {
        if(this.lastResults == null ) {
            lastResults = new ArrayList<>();
        }
        lastResults.add(result);
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<PlayerWalletInfoResultLine> getLastResults() {
        return lastResults;
    }

    public void setLastResults(List<PlayerWalletInfoResultLine> lastResults) {
        this.lastResults = lastResults;
    }
}
