package com.basm.slots.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class SlotWinning {


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    private Date createdDate;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "playerWalletId")
    private PlayerWallet playerWallet;

    @Transient
    private double slotFollowNumber;

    public static SlotWinning createNewWinning(double amount, final PlayerWallet playerWallet) {
        SlotWinning winning = new SlotWinning();
        winning.setAmount(amount);
        winning.setCreatedDate(new Date());
        winning.setPlayerWallet(playerWallet);
        return winning;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PlayerWallet getPlayerWallet() {
        return playerWallet;
    }

    public void setPlayerWallet(PlayerWallet playerWallet) {
        this.playerWallet = playerWallet;
    }

    public double getSlotFollowNumber() {
        return slotFollowNumber;
    }

    public void setSlotFollowNumber(double slotFollowNumber) {
        this.slotFollowNumber = slotFollowNumber;
    }
}
