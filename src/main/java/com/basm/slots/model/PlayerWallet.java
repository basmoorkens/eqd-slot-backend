package com.basm.slots.model;

import javax.persistence.*;

@Entity
public class PlayerWallet {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Version
    private int version;

    @Column(unique = true)
    private String publicKey;

    private double balance;

    private boolean firstTimer;

    private boolean gameWallet;

    public static PlayerWallet buildNew(final String publicKey, final double balance) {
        PlayerWallet wallet = new PlayerWallet();
        wallet.setPublicKey(publicKey);
        wallet.setBalance(balance);
        wallet.setVersion(0);
        wallet.setFirstTimer(true);
        wallet.setGameWallet(false);
        return wallet;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    public boolean isFirstTimer() {
        return firstTimer;
    }

    public void setFirstTimer(boolean firstTimer) {
        this.firstTimer = firstTimer;
    }

    public boolean isGameWallet() {
        return gameWallet;
    }

    public void setGameWallet(boolean gameWallet) {
        this.gameWallet = gameWallet;
    }
}
