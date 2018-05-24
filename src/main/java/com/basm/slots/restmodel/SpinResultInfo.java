package com.basm.slots.restmodel;

import com.basm.slots.model.SlotWinning;

import java.text.SimpleDateFormat;

public class SpinResultInfo {

    private double balance;

    private double winning;

    private String spinDateTime;

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
