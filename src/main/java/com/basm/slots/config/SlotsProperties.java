package com.basm.slots.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource("classpath:application.properties")
public class SlotsProperties {

    private String escrowWalletPrivateKey;

    private String escrowWalletPublicKey;

    private String slotToken;

    private String slotTokenIssuer;

    private double amountToSpin;

    public String getEscrowWalletPrivateKey() {
        return escrowWalletPrivateKey;
    }

    public void setEscrowWalletPrivateKey(String escrowWalletPrivateKey) {
        this.escrowWalletPrivateKey = escrowWalletPrivateKey;
    }

    public String getEscrowWalletPublicKey() {
        return escrowWalletPublicKey;
    }

    public void setEscrowWalletPublicKey(String escrowWalletPublicKey) {
        this.escrowWalletPublicKey = escrowWalletPublicKey;
    }

    public String getSlotToken() {
        return slotToken;
    }

    public void setSlotToken(String slotToken) {
        this.slotToken = slotToken;
    }

    public double getAmountToSpin() {
        return amountToSpin;
    }

    public void setAmountToSpin(double amountToSpin) {
        this.amountToSpin = amountToSpin;
    }

    public String getSlotTokenIssuer() {
        return slotTokenIssuer;
    }

    public void setSlotTokenIssuer(String slotTokenIssuer) {
        this.slotTokenIssuer = slotTokenIssuer;
    }
}
