package com.basm.slots.restmodel;

import java.io.Serializable;

public class PlayerWalletInfo implements Serializable {

    private double amount;

    private String publicKey;

    public PlayerWalletInfo(final String publicKey, final double amount) {
        this.amount = amount;
        this.publicKey = publicKey;
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
}
