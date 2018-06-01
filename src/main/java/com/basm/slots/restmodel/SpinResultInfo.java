package com.basm.slots.restmodel;

import com.basm.slots.model.SlotWinning;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;

public class SpinResultInfo {

    @JsonProperty("balance")
    private double balance;

    @JsonProperty("winning")
    private double winning;

    @JsonProperty("spinDateTime")
    private String spinDateTime;

    @JsonProperty("spinFollowNumber")
    private double spinFollowNumber;

    public SpinResultInfo(SlotWinning winning) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.winning = winning.getAmount();
        this.spinDateTime = sdf.format(winning.getCreatedDate());
        this.balance = winning.getPlayerWallet().getBalance();
        this.spinFollowNumber = winning.getSlotFollowNumber();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getWinning() {
        return winning;
    }

    public void setWinning(double winning) {
        this.winning = winning;
    }

    public String getSpinDateTime() {
        return spinDateTime;
    }

    public void setSpinDateTime(String spinDateTime) {
        this.spinDateTime = spinDateTime;
    }

    public double getSpinFollowNumber() {
        return spinFollowNumber;
    }
}
