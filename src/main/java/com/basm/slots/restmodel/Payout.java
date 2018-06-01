package com.basm.slots.restmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Payout {

    @JsonProperty("publicKey")
    private String publicKey;

    @JsonProperty("amount")
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
