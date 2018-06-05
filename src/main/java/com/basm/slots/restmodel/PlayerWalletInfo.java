package com.basm.slots.restmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerWalletInfo implements Serializable {

	private static final long serialVersionUID = -6863193447663447551L;

	@JsonProperty("amount")
    private double amount;

    @JsonProperty("publicKey")
    private String publicKey;

    private List<PlayerWalletInfoWinning> lastWinnings;

    public PlayerWalletInfo(final String publicKey, final double amount) {
        this.amount = amount;
        this.publicKey = publicKey;
    }

    public void addResult(PlayerWalletInfoWinning result ) {
        if(this.lastWinnings == null ) {
            lastWinnings = new ArrayList<>();
        }
        lastWinnings.add(result);
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

    public List<PlayerWalletInfoWinning> getLastWinnings() {
        return lastWinnings;
    }

    public void setLastWinnings(List<PlayerWalletInfoWinning> lastWinnings) {
        this.lastWinnings = lastWinnings;
    }
}
