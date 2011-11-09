package com.teama.pds;

/**
 * Author:      Grant Kurtz
 */
public class Oven extends Resource {

	/**
	 * The maximum space this Oven has for items.
	 */
	private final int OVEN_SPACE;

	/**
	 * The current amount of space (of the maximal amount) being used by items.
	 */
	private int used_oven_space;

	/**
	 * Holds all the items that this oven is currently "cooking".
	 */
	private final ResourceList<CookedItem> resources;

	/**
	 * Default Constructor
	 *
	 * @param oven_space The total space this oven has for storage of food
	 *                   to be cooked.
	 * @param time	   The instance that holds the current time of the
	 *                   simulation.
	 */
	public Oven(int oven_space, TimeReader time) {
		super(time);
		this.OVEN_SPACE = oven_space;
		resources = new ResourceList<CookedItem>();
	}

	/**
	 * @return The maximum total space this oven has available.
	 */
	public int getOvenSpace() {
		return OVEN_SPACE;
	}

	/**
	 * A convenience function for determining if the oven has enough space for
	 * the next item.
	 *
	 * @param food The item to check for enough space.
	 * @return True if there is enough space, otherwise false.
	 */
	public boolean hasSpaceFor(CookedItem food) {
		return getTotalFreeSpace() >= food.getSpace();
	}

	/**
	 * Ensures the oven will have enough space for the item, and then increments
	 * the total used space of the oven.
	 *
	 * @param food The item to add to the oven.
	 * @throws NotEnoughSpaceException this is thrown if the space being asked
	 *                                 for the item is greater than the
	 *                                 available space of the oven.
	 */
	public void addItem(CookedItem food) throws NotEnoughSpaceException {
		if (!hasSpaceFor(food)) {
			throw new NotEnoughSpaceException("( * ) There is not enough space in the" +
					"oven for the " + food + " of size " + food.getSpace() +
					".");
		}
		used_oven_space += food.getSpace();
		resources.addResource(food);
		food.cookFood();
		String msg = food + " was added to the oven and will be finished" +
				" cooking at TC " + food.getAvailTime();
		Record r = new Record(msg, Record.Status.TRANSITION,
				time.getCurrentTime());
		getLogger().addEvent(r);
	}

	/**
	 * Removes the first item that finished cooking, and returns it.  The space
	 * the item consumed will also be freed.
	 *
	 * @return A cooked item if the resource was successfully removed,
	 *         otherwise null
	 */
	public CookedItem removeItem() {
		CookedItem ci = resources.grabAvailable();
		if (ci != null) {
			used_oven_space -= ci.getSpace();
		}
		return ci;
	}

	/**
	 * @return The space left for more items to occupy
	 */
	public int getTotalFreeSpace() {
		return OVEN_SPACE - used_oven_space;
	}

	/**
	 * Ovens will be sorted so as to allow for ovens with the most space to be
	 * at the front.
	 *
	 * @param o The oven to check against.
	 * @return A negative value if this oven has more space, a positive
	 *         value if this oven has less space, and 0 if the ovens are
	 *         equal in free space.
	 */
	public int compareTo(Resource o) {
		return ((Oven) o).getTotalFreeSpace() - getTotalFreeSpace();
	}

	/**
	 * @return To be considered available the oven must have a positive
	 *         amount of space.
	 */
	public boolean isAvailable() {

		// alright, some food might have finished during this second, so go
		// ahead and make sure none are finished
		CookedItem ci = resources.grabAvailable();
		while (ci != null) {
			String msg = "Finished cooking " + ci;
			ci.finishCooking();
			used_oven_space -= ci.getSpace();
			Record r = new Record(msg, Record.Status.TRANSITION,
					time.getCurrentTime());
			getLogger().addEvent(r);
			ci = resources.grabAvailable();
		}
		return getTotalFreeSpace() > 0;
	}


	public int getFoodCount() {
		return resources.getCount();
	}

	public boolean isCookingFoodFromOrder(Order order) {
		for (CookedItem cooking_item : resources.getResourceQueue()) {
			if (order.containsFoodItem(cooking_item))
				return true;
		}
		return false;
	}

	public void stopCookingFoodFromOrder(Order order) {
		for (CookedItem cooking_item : resources.getResourceQueue()) {
			if (order.containsFoodItem(cooking_item)) {
				resources.removeResource(cooking_item);
				used_oven_space -= cooking_item.getSpace();
			}
		}
	}
}
