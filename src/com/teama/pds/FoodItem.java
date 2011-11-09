package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * <p/>
 * The base information necessary to represent a single piece of food.
 */
public class FoodItem extends Resource {

	/**
	 * The name of the food that this instance represents.
	 */
	private String name;

	/**
	 * The base cost of this food.
	 */
	private double cost;

	/**
	 * The total time needed to prepare this food.
	 */
	private int prep_time;

	/**
	 * The time that this food item started preparing.
	 */
	private int started_preparing;

	/**
	 * The time that this food item finished preparing.
	 */
	private int ended_preparing;

	/**
	 * Constructor
	 *
	 * @param name	  Name of the food
	 * @param cost	  Cost of the food
	 * @param prep_time The time to prepare this food
	 * @param time	  The instance that holds the current time of the
	 *                  simulation.
	 */
	public FoodItem(String name, double cost, int prep_time, TimeReader time) {
		super(time);
		this.name = name;
		this.cost = cost;
		this.prep_time = prep_time;
		started_preparing = 0;
		ended_preparing = 0;
	}

	/**
	 * Used by the CookedItem class
	 *
	 * @param time The time object that accurately reports information.
	 */
	public FoodItem(TimeReader time) {
		super(time);
	}

	/**
	 * @return A deep-copied clone of this class.  Necessary for the menu class
	 */
	@SuppressWarnings({"CloneDoesntCallSuperClone",
			"CloneDoesntDeclareCloneNotSupportedException"})
	public FoodItem clone() {
		return new FoodItem(getName(), getCost(), getPrepTime(),
				this.time);
	}

	/**
	 * @return True if the current time is equal to the time this will
	 *         be finished.
	 */
	public boolean isAvailable() {
		return getAvailTime() <= time.getCurrentTime();
	}

	/**
	 * @param o the FoodItem to be compared against for availability
	 *          priority
	 * @return a negative value if this FoodItem will be available sooner,
	 *         a positive value if this will be available after the passed
	 *         in FoodItem, and 0 if they are equal
	 */
	public int compareTo(Resource o) {
		FoodItem food = (FoodItem) o;
		if (getAvailTime() == 0)
			return this.getPrepTime() - food.getPrepTime();
		return getAvailTime() - food.getAvailTime();
	}

	/**
	 * Sets the availability of this food into the future.
	 */
	public void prepareFood() {
		started_preparing = time.getCurrentTime();
		setAvailTime(prep_time + time.getCurrentTime());
	}

	/**
	 * Just sets the time stamp of when this food item finished preparing to the
	 * current time stamp. Used for statistics gathering.
	 */
	public void finishPreparation() {
		ended_preparing = time.getCurrentTime();
	}

	/**
	 * @return The total time this food item spent in preparation.
	 */
	public int getTotalTimeInPreparation() {
		return ended_preparing - started_preparing;
	}

	public int getStartPreparationTime() {
		return started_preparing;
	}

	public int getEndPreparationTime() {
		return ended_preparing;
	}

	/**
	 * Sets the name of the food, used for modifying food names and food items
	 * initialized with no parameters.
	 *
	 * @param food_name name or new name of the food item
	 */
	public void setName(String food_name) {
		this.name = food_name;

	}

	/**
	 * Sets the cost of the food, for modifying cost or item initialized
	 * with no parameters
	 *
	 * @param food_cost cost or new cost of the food item
	 */
	public void setCost(double food_cost) {
		this.cost = food_cost;
	}

	/**
	 * Sets the prep time of the food. For modifying prep time or item
	 * initialized with no parameters
	 *
	 * @param food_prep time or new time to prepare the food item.
	 */
	public void setPrepTime(int food_prep) {
		this.prep_time = food_prep;
	}

	/**
	 * @return Food item's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return Food item's base cost
	 */
	public double getCost() {
		return this.cost;
	}

	/**
	 * @return Food item's prep time
	 */
	public int getPrepTime() {
		return this.prep_time;
	}

	/**
	 * @return Just the name of this food item.
	 */
	public String toString() {
		return getName();
	}


}
