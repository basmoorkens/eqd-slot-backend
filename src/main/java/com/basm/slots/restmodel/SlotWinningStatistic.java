package com.basm.slots.restmodel;

public class SlotWinningStatistic {

	private long count;
	
	private double price;

	public SlotWinningStatistic(final long count, final double price) {
		this.count = count;
		this.price = price;
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
	
}
