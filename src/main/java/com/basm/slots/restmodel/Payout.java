package com.basm.slots.restmodel;

public class Payout {

    private String publicKey;

    private double amount;

    public Payout(final String publicKey, final double amount){
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
