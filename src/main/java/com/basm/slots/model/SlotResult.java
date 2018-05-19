package com.basm.slots.model;


import java.io.Serializable;

public class SlotResult implements Serializable {

	private double followNumber;

	private double amount;

	private int randomness;

	protected SlotResult(int amount, int randomness, int followNumber) {
		this.amount = amount;
		this.randomness = randomness;
		this.followNumber = followNumber;
	}
	
	public double getAmount() {
		return amount;
	}

	public int getRandomness() {
		return randomness;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof  SlotResult) {
			return this.amount == ((SlotResult) obj).getAmount();
		}
		return false;
	}

	public double getFollowNumber() {
		return followNumber;
	}

	public void setFollowNumber(double followNumber) {
		this.followNumber = followNumber;
	}
}
