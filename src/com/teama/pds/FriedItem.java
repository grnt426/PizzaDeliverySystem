package com.teama.pds;

/**
 * Author:      Grant Kurtz
 */
public class FriedItem extends FoodItem {

	private final int fry_time;

	private final int space;

	FriedItem(String name, double cost, int prep_time, int fry_time,
			  int space, TimeReader time) {
		super(name, cost, prep_time, time);
		this.fry_time = fry_time;
		this.space = space;
	}

	public FriedItem clone() {
		super.clone();
		return new FriedItem(getName(), getCost(), getPrepTime(),
				getSpace(), getFryTime(), this.time);
	}

	int getFryTime() {
		return fry_time;
	}

	public void fryFood() {
		setAvailTime(time.getCurrentTime() + getFryTime());
	}

	int getSpace() {
		return space;
	}

	public String toString() {
		return getName();
	}
}
