package com.basm.slots.restmodel;

import java.io.Serializable;

public class PlayerWalletInfoWinning implements Serializable {

	private static final long serialVersionUID = 7206404462965331183L;

	private String wonTime;

    private Double amount;

    public PlayerWalletInfoWinning(String wonTime, Double amount) {
        this.amount = amount;
        this.wonTime = wonTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getWonTime() {
        return wonTime;
    }

    public void setWonTime(String wonTime) {
        this.wonTime = wonTime;
    }
}
