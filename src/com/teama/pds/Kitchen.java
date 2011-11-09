package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * <p/>
 * Handles delegating unprepared food items to available chefs, as well as
 * ensuring the ovens and chefs are keeping an eye on their food to keep
 * everything moving.
 */
public class Kitchen extends ActionNode {

	/**
	 * The list of active chefs that are currently working in the kitchen.
	 */
	private final PeekResourceList<Chef> chefs;

	/**
	 * The list of all ovens available for cooking food.
	 */
	private PeekResourceList<Oven> ovens;

	/**
	 * The food items that need to be processed
	 */
	private final ResourceList<FoodItem> food_to_make;

	/**
	 * An instance of oneself, to avoid double-initialization.
	 */
	private static Kitchen kitchen;

	/**
	 * The logger instance for reporting changes to.
	 */
	private History logger;

	/**
	 * Default Private Constructor; Don't allow outside initialization.
	 * <p/>
	 * Create all the facilities necessary for this kitchen to operate, as well
	 * as accepting references to lists for keeping track of new orders.
	 *
	 * @param time  The instance class that maintains the current time of the
	 *              simulation
	 * @param foods The resource list that holds all the food that needs to be
	 *              prepared/cooked.
	 * @param ovens The ovens this Kitchen has available for use.
	 * @param chefs The chefs this Kitchen has available for cooking food.
	 */
	private Kitchen(TimeReader time, ResourceList<FoodItem> foods,
					PeekResourceList<Oven> ovens,
					PeekResourceList<Chef> chefs) {
		super(time);
		this.ovens = ovens;
		this.chefs = chefs;
		food_to_make = foods;

		// give all the chefs the ovens the Kitchen has
		for (Chef c : chefs.getResourceQueue())
			c.setOvens(ovens);
	}

	/**
	 * Maintains the requirement of only one kitchen allowed in existence during
	 * the execution of the program, also only permits this method to be called
	 * exactly once.
	 *
	 * @param time  The instance class that maintains the current time
	 *              of the simulation.
	 * @param foods The reference to a shared list where food that needs
	 *              to be prepared/cooked from new orders.
	 * @param ovens The list of ovens that are available to this Kitchen.
	 * @param chefs The list of chefs that can work in this Kitchen.
	 * @return A singleton Kitchen for use with operating the oven,
	 *         chefs, etc.
	 * @throws AlreadyInstantiatedException This is thrown if this method has
	 *                                      already been called.
	 */
	public static Kitchen createKitchen(TimeReader time,
										ResourceList<FoodItem> foods,
										PeekResourceList<Oven> ovens,
										PeekResourceList<Chef> chefs)
			throws AlreadyInstantiatedException {
		if (kitchen != null)
			throw new AlreadyInstantiatedException("The Kitchen class has " +
					"already been instantiated.");
		else
			return (kitchen = new Kitchen(time, foods, ovens, chefs));
	}

	/**
	 * Note: One MUST call createKitchen() at least once before calling this
	 * method.
	 *
	 * @throws NotInstantiatedException This is thrown if the
	 *                                  createKitchen() method was never
	 *                                  called
	 * @return The instantiated Kitchen class
	 */
	public static Kitchen getKitchen() throws NotInstantiatedException {
		if (kitchen == null)
			throw new NotInstantiatedException("The Kitchen class has not" +
					" been instantiated yet.");
		else
			return kitchen;
	}

	/**
	 * Handles all logic for giving the chefs orders to process.  For now, just
	 * gives a chef an order, and then "finishes" the order the next time this
	 * is called.
	 */
	public void execute() {

		// Some ovens may finish and therefore be available (freeing space)
		// causing the priority queue to need to be re-sorted.
		ovens.checkAllInternalResources();

		// Some chef's might finish even if there are no orders to process,
		// so we need to have all chef's check on their food, as some may need
		// to put their food in the oven.
		chefs.checkAllInternalResources();

		// check inbox for new orders
		while (food_to_make.anyAvailable() && chefs.anyAvailable()) {
			FoodItem fi = food_to_make.grabAvailable();

			// alright, give the chef some food to prepare
			Chef avail_chef = chefs.grabAvailable();
			try {
				avail_chef.setFood(fi);
			} catch (AlreadyCookingOrderException e) {
				Record r = new Record(avail_chef + " is still working on another " +
						"order", Record.Status.SLOWED, time.getCurrentTime());
				logger.addEvent(r);
			}
		}
	}

	/**
	 * Automatically gives the new chef access to all the ovens this kitchen
	 * current has assigned to it.
	 *
	 * @param c The new chef to start working in the kitchen.
	 */
	public void addChef(Chef c) {
		c.setOvens(ovens);
		chefs.addResource(c);
	}

	/**
	 * @param ovens A list of ovens that this kitchen can use for cooking food.
	 */
	public void setOvens(PeekResourceList<Oven> ovens) {
		this.ovens = ovens;
		for (Chef c : chefs.getResourceQueue())
			c.setOvens(this.ovens);
	}

	/**
	 * @return The number of chefs that are able to work and in this kitchen.
	 */
	public int getChefCount() {
		return chefs.getCount();
	}

	/**
	 * @return The number of ovens that can be used in the kitchen.
	 */
	public int getOvenCount() {
		return ovens.getCount();
	}

	/**
	 * @param logger The object for logging all activities to.
	 */
	public void setLogger(History logger) {
		this.logger = logger;

		// let our ovens and chefs have the logger, too
		for (Chef c : chefs.getResourceQueue())
			c.setLogger(logger);
		for (Oven o : ovens.getResourceQueue())
			o.setLogger(logger);
	}

	public PeekResourceList<Oven> getOvens() {
		return ovens;
	}
}
