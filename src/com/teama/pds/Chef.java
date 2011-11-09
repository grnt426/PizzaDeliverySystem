package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * The Controller that handles preparing food, and where needed, will move
 * food that is prepared to the oven for cooking, if need be.  Note, this class
 * MUST be polled for availability every second, otherwise the food may not
 * reach it to the oven when it is supposed to.
 */
public class Chef extends Resource {


	/**
	 * boolean to tell view if preparations are complete.
	 */
	private boolean prep_confirm = false;

	/**
	 * boolean to tell view if an item was put in the oven.
	 */
	private boolean item_put_in_oven = false;

	/**
	 * The single food thing this chef is currently working on.
	 */
	private FoodItem food;

	/**
	 * item transfered to the oven for the view
	 */
	private CookedItem finished_food;

	/**
	 * This chef has direct access to all the ovens in the kitchen.
	 */
	private PeekResourceList<Oven> ovens;

	/**
	 * Default Constructor
	 * <p/>
	 * Chef's need access ovens in order to move items that need cooking to the
	 * oven(s).
	 *
	 * @param time The class that keeps time.
	 */
	public Chef(TimeReader time) {
		super(time);
	}

	/**
	 * @return The food that the chef is currently working on.
	 */
	FoodItem getFood() {
		return food;
	}

	/**
	 * Gives the chef a food from an order to create.
	 *
	 * @param food The food to be created by the
	 *             chef.
	 * @throws AlreadyCookingOrderException This is thrown if the chef
	 *                                      already had a food item and is
	 *                                      still working on it.
	 */
	public void setFood(FoodItem food) throws AlreadyCookingOrderException {
		if (this.food == null) {
			this.food = food;

			// For food of 0 preparation time, we actually need to do things
			// a little differently.
			if (food.getPrepTime() == 0) {
				food.prepareFood();
				food.finishPreparation();
				String msg = this + " is moving " + food + " directly to the" +
						" Oven.";
				Record r = new Record(msg, Record.Status.TRANSITION,
						time.getCurrentTime());
				getLogger().addEvent(r);
				checkMoveToOven();
			} else {

				// just start making the food immediately
				food.prepareFood();
				setAvailTime(food.getAvailTime());
				String msg = this + " is working on " + food + " and will" +
						" be done at TC " + getAvailTime();
				Record r = new Record(msg, Record.Status.TRANSITION,
						time.getCurrentTime());
				getLogger().addEvent(r);
			}
		} else
			throw new AlreadyCookingOrderException("This cook is " +
					"already working on another order; he only has two hands!");
	}

	/**
	 * In order to reduce the likelihood that the caller of this class will
	 * forget to move the FoodItem to the Oven, this method will handle that
	 * entire process by making sure the Chef takes the responsibility of
	 * placing the food in the oven before making himself available for a new
	 * food item.
	 *
	 * @return True if the chef has no food he is currently working on,
	 *         otherwise false.
	 */
	public boolean isAvailable() {

		// first, check if the food is done being made
		if (food != null && food.isAvailable()) {

			// this check is necessary in case we can't add the food item to the
			// oven, which will cause this to be called several times until the
			// chef can add the food to the oven.
			if (food.getAvailTime() == time.getCurrentTime()) {
				String msg = this + " finished working on " + food;
				Record r = new Record(msg, Record.Status.TRANSITION,
						time.getCurrentTime());
				getLogger().addEvent(r);
				prep_confirm = false;
			}

			// since it is complete, it may need to go to the oven, otherwise
			// we are done making this food
			if (food instanceof CookedItem)
				checkMoveToOven();
			else {
				food.finishPreparation();
				food = null;
			}
		}
		return food == null;
	}

	private void checkMoveToOven() {
		if (food instanceof CookedItem) {

			// Add the cookable food to the oven
			CookedItem ci = (CookedItem) food;
			Oven oven = ovens.grabAvailable();
			if (oven != null && oven.hasSpaceFor(ci)) {
				try {
					item_put_in_oven = true;
					finished_food = ci;
					oven.addItem(ci);
					ci.finishPreparation();
				} catch (NotEnoughSpaceException e) {
					e.printStackTrace();
					System.exit(1);
				}
				food = null;
			} else {
				String msg = this + " is waiting on " +
						((CookedItem) food).getSpace() + " space in the" +
						"ovens for " + food;
				Record r = new Record(msg, Record.Status.SLOWED,
						time.getCurrentTime());
				getLogger().addEvent(r);
			}
		}
	}

	/**
	 * If this chef is not working on any food, then he has higher priority
	 * than other chefs.  Otherwise, sort the chefs based on the time
	 * availability of their food.
	 *
	 * @param o The chef to compare against.
	 * @return Integer.MIN_VALUE is returned if currently not working on
	 *         any food.  Otherwise, the value from comparing the two foods
	 *         the chefs are working on is returned instead.
	 */
	public int compareTo(Resource o) {
		Chef chef = (Chef) o;
		return (this.food == null ?
				Integer.MAX_VALUE :
				this.food.compareTo(chef.getFood()));
	}

	public boolean isWorkingOnOrder(Order o) {
		return o.containsFoodItem(food);
	}

	/**
	 * @param ovens The ovens this chef has access to.
	 */
	public void setOvens(PeekResourceList<Oven> ovens) {
		this.ovens = ovens;
	}

	/**
	 * @return Chef's don't have names, their mother's didn't love them.
	 */
	public String toString() {
		return "A Chef";
	}

	public FoodItem cancelPreparation() {
		FoodItem canceled_food = food;
		food = null;
		return canceled_food;
	}

	/**
	 * set preparation confirmation for view
	 */
	public void setPrepConfirm() {
		prep_confirm = true;

	}

	/**
	 * @return if cook is preparing a food item
	 */
	public boolean getPrepConfirm() {
		return prep_confirm;
	}

	/**
	 * @return if cook put an item in the oven
	 */
	public boolean getIfItemIsInOven() {
		return item_put_in_oven;
	}

	/**
	 * set item in oven boolean for the view
	 */
	public void setItemInOven() {
		item_put_in_oven = false;
	}

	public CookedItem getFoodSent() {
		return finished_food;
	}
}

