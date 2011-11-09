package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * This entity holds relevant information for items that need to be cooked in
 * an oven.
 */
public class CookedItem extends FoodItem {

	/**
	 * The space the item takes in the oven
	 */
	private final int space;

	/**
	 * The time it takes to cook an item
	 */
	private final int cook_time;

	private int started_cooking;

	private int ended_cooking;

	/**
	 * Default Constructor
	 *
	 * @param name	  The name of the item.
	 * @param cost	  The total cost of this food.
	 * @param prep_time The total time necessary to prepare the food.
	 * @param space	 The total space this food requires in an oven.
	 * @param cook_time The total time necessary to cook this food.
	 * @param time	  The TimeReader object for reporting accurate time
	 *                  information.
	 */
	public CookedItem(String name, double cost, int prep_time, int space,
					  int cook_time, TimeReader time) {
		super(name, cost, prep_time, time);
		this.space = space;
		this.cook_time = cook_time;
	}

	/**
	 * Non-kosher Constructor
	 *
	 * @param space	 The space this food item requires in the oven.
	 * @param cook_time The total time necessary to cook this item.
	 * @param time	  The time object for reporting accurate time
	 *                  information
	 * @deprecated DO NOT use this, this class does not initialize all necessary
	 *             data members, and should only be used for testing.
	 */
	public CookedItem(int space, int cook_time, TimeReader time) {
		super(time);
		this.space = space;
		this.cook_time = cook_time;
	}

	/**
	 * @return A cloned version of this instance.  This is necessary for
	 * filling new orders with separate food items for manipulation
	 * later.
	 */
	public CookedItem clone() {
		super.clone();
		return new CookedItem(getName(), getCost(), getPrepTime(),
				getSpace(), getCookTime(), this.time);
	}

	/**
	 * gets the amount of space the item takes in the oven
	 *
	 * @return space item takes in the oven
	 */
	public int getSpace() {
		return space;
	}

	/**
	 * gets the amount of time it takes to cook the item
	 *
	 * @return time it takes to cook in the oven
	 */
	public int getCookTime() {
		return cook_time;
	}

	/**
	 * Sets the availability of this food item in the future by the time
	 * necessary to cook.
	 */
	public void cookFood() {
		setAvailTime(cook_time + time.getCurrentTime());
		started_cooking = time.getCurrentTime();
	}

	public void finishCooking() {
		ended_cooking = time.getCurrentTime();
	}

	/**
	 * @return Just the name of this food item.
	 */
	public String toString() {
		return getName();
	}

	public TimeReader getTimeReader() {
		return time;
	}

	public int getTotalRealCookTime() {
		return ended_cooking - started_cooking;
	}

	public int getStartCookTime() {
		return started_cooking;
	}

	public int getEndCookTime() {
		return ended_cooking;
	}
}
