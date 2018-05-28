package com.basm.slots.restmodel;

import java.util.ArrayList;
import java.util.List;

public class AdminStats {

	private double amountInWallet;

	private double actualAmountInWallet;

	private List<PlayerWalletInfo> playerWallets;
	
	private boolean bigPayoutActive;

	private int totalSpins;

    private List<SlotWinningStatistic> statsSinceLastBigWin;

    private double resultsSinceLastBigWin;

	public boolean isBigPayoutActive() {
		return bigPayoutActive;
	}

	public void setBigPayoutActive(boolean bigPayoutActive) {
		this.bigPayoutActive = bigPayoutActive;
	}

	public void addPlayerWalletInfo(PlayerWalletInfo pwi) { 
		if(playerWallets == null) { 
			playerWallets = new ArrayList<>();
		}
		playerWallets.add(pwi);
	}
	
	public double getAmountInWallet() {
		return amountInWallet;
	}

	public void setAmountInWallet(double amountInWallet) {
		this.amountInWallet = amountInWallet;
	}

	public List<PlayerWalletInfo> getPlayerWallets() {
		return playerWallets;
	}

	public void setPlayerWallets(List<PlayerWalletInfo> playerWallets) {
		this.playerWallets = playerWallets;
	}

	public int getTotalSpins() {
		return totalSpins;
	}

	public void setTotalSpins(int totalSpins) {
		this.totalSpins = totalSpins;
	}

	public void setActualAmountInWallet(double amountToSet) {
	    this.actualAmountInWallet = amountToSet;
    }

    public double getActualAmountInWallet() {
        return actualAmountInWallet;
    }

    public List<SlotWinningStatistic> getStatsSinceLastBigWin() {
        return statsSinceLastBigWin;
    }

    public void setStatsSinceLastBigWin(List<SlotWinningStatistic> statsSinceLastBigWin) {
        this.statsSinceLastBigWin = statsSinceLastBigWin;
    }


    public double getResultsSinceLastBigWin() {
        return resultsSinceLastBigWin;
    }

    public void setResultsSinceLastBigWin(double resultsSinceLastBigWin) {
        this.resultsSinceLastBigWin = resultsSinceLastBigWin;
    }

}
