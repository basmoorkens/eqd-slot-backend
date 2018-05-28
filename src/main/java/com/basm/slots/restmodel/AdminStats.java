package com.basm.slots.restmodel;

import java.util.ArrayList;
import java.util.List;

public class AdminStats {

	private double amountInWallet;
	
	private List<PlayerWalletInfo> playerWallets;
	
	private boolean bigPayoutActive;

	
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
	
	
	
}
