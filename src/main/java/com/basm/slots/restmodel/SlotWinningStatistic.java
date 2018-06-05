package com.basm.slots.restmodel;

public class SlotWinningStatistic {

	private long count;
	
	private double price;

	private double fee;

	public SlotWinningStatistic(final long count, final double price, final double fee) {
		this.count = count;
		this.price = price;
		this.fee = fee;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public double getFee() {
		return fee;
	}
}
