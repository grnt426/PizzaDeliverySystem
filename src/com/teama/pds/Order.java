package com.teama.pds;

import java.util.ArrayList;

/**
 * Authors:      Grant Kurtz, Svyatoslav Sivov
 * <p/>
 * An Entity class that maintains the orders as placed by a customer
 */
public class Order extends Resource {

	private static int order_count;
	private String order_id;
	private final int order_number;

	/**
	 * All the items that belong to this order.
	 */
	private final PeekResourceList<FoodItem> items;

	/**
	 * The destination of this order.
	 */
	private String location;

	/**
	 * The class instance that will accurately report the time of the
	 * simulation.
	 */
	private final TimeReader time;
	private final int avail_time;
	private final PizzaMenu menu;

	/**
	 * The customer this order is assigned to.
	 */
	private Customer customer;

	/**
	 * The time code at which this Order will no longer be "delayed" for
	 * availability.
	 */
	private int delay_expires;

	private boolean order_canceled;
	private int order_started;
	private int order_finished;
	private int order_picked_up;
	private int order_dropped_off;
	private ArrayList<FoodItem> fooditems_list;

	/**
	 * Default Constructor
	 * <p/>
	 * Initializes everything to sensible default values, including setting the
	 * location of this class to 'RIT' by default.
	 *
	 * @param time The time object that will accurately report the time of the
	 *             simulation.
	 * @param menu The menu that can be used for cloning FoodItems for later
	 *             manipulation by the simulation.
	 */
	public Order(TimeReader time, PizzaMenu menu) {
		super(time);
		this.time = time;
		order_number = order_count;
		order_count++;
		this.avail_time = 0;
		this.menu = menu;
		items = new PeekResourceList<FoodItem>();
		location = "NEVER SET";
		delay_expires = 0;
		order_canceled = false;
		order_started = -1;
		order_finished = -1;
		order_dropped_off = -1;
		order_picked_up = -1;
		fooditems_list = new ArrayList<FoodItem>();
	}

	/**
	 * @param number The ID of this order, for external use.
	 */
	public void setId(String number) {
		this.order_id = number;
	}

	/**
	 * This Order can only be available for operations if all its FoodItems are
	 * also available, and there is no delay on this Order. Will also check
	 * the last FoodItem to see if it finished.  If so, then this Order will
	 * mark itself as "finished".
	 *
	 * @return True if this order's FoodItems are all available and
	 * there the delay already expired, otherwise false.
	 */
	public boolean isAvailable() {
		int availTime = this.getAvailTime();

		// If the last FoodItem just became available, then this order also
		// just finished being made.
		if (availTime == time.getCurrentTime())
			markOrderFinished();

		// go ahead and check if this order is actually available, however.
		return time.getCurrentTime() >= Math.max(availTime,
				this.getDelayExpires());
	}

	/**
	 * @return The availability of the last FoodItem.
	 */
	public int getAvailTime() {
		return getHighest();
	}

	/**
	 * @return The number of orders that have been created since program start.
	 */
	public static int getTotalOrders() {
		return order_count;
	}

	/**
	 * @param item Adds another FoodItem to the order.
	 */
	public void addItem(FoodItem item) {
		fooditems_list.add(item);
		items.addResource(item);
	}

	/**
	 * Orders food based on availability time, descending.
	 *
	 * @param r The FoodItem object to compare against.
	 * @return Negative if this food item will finish sooner, 0 if the two
	 * items will finish times will be the same, otherwise a
	 * positive number.
	 */
	public int compareTo(Resource r) {
		Order o = (Order) r;
		return getHighest() - o.getHighest();
	}

	/**
	 * @return Computes the most time needed for all orders are available.
	 */
	private int getHighest() {
		int highest = 0;
		for (FoodItem food : items.getResourceQueue()) {
			highest = food.getAvailTime() > highest ? food.getAvailTime() : highest;
		}
		return highest;
	}

	/**
	 * @param loc The final destination of the order
	 */
	public void setLocation(String loc) {
		location = loc;
	}

	/**
	 * @return The final destination of the order
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @return All the food that this order requires
	 */
	public PeekResourceList<FoodItem> getItems() {
		return items;
	}

	/**
	 * @return An external order number, based on phone_number
	 */
	public String getOrderId() {
		return order_id;
	}

	/**
	 * @return The total number of orders created since program start
	 */
	public int getOrderCount() {
		return order_count;
	}

	public boolean containsFoodItem(FoodItem fi) {
		return items.containsResource(fi);
	}

	/**
	 * Prints out a human readable version of the order that was placed by the
	 * customer.
	 */
	public void print() {
		int i = 1;
		System.out.println("\n+++++++++++++++++++++++++++++++++++++");
		if (isOrderCanceled()) {
			System.out.println("\t\t### Canceled ###");
		} else if (getOrderStartTime() == -1) {
			System.out.println("\t\t### Delayed ###");
		} else if (getOrderStartTime() != -1 && getOrderFinishTime() == -1) {
			System.out.println("\t\t### Creating ###");
		} else if (getOrderFinishTime() != -1 && getOrderPickedUpTime() == -1) {
			System.out.println("\t\t### Waiting ###");
		} else if (getOrderPickedUpTime() != -1 && getOrderDropOffTime() == -1) {
			System.out.println("\t\t### In Transit ###");
		} else if (getOrderFinishTime() != -1) {
			System.out.println("\t\t### Delivered ###");
		}
		System.out.println("ID:\t\t\t\t\t" + this.getOrderId());
		System.out.println("Customer Name:\t\t" + customer.getName());
		System.out.println("Customer Number:\t" + customer.printPhoneNumber());
		System.out.println("Goes to:\t\t\t" + this.getLocation());
		System.out.println("\nContains:");
		for (FoodItem fi : items.getResourceQueue()) {
			System.out.print("\t" + i + ". " + fi);
			System.out.printf("\n\t\t\t\t$%.2f\n", fi.getCost());
			i++;
		}
		System.out.println("\t\t\t\t------");
		System.out.print("Total Cost:\t");
		//System.out.print("$");
		System.out.printf("\t$" + "%.2f", this.getTotalCost());
		System.out.println("\n+++++++++++++++++++++++++++++++++++++");

	}

	/**
	 * @return The total cost of the Order, as currently placed by the user.
	 */
	public double getTotalCost() {
		double totalCost = 0;
		for (FoodItem fi : items.getResourceQueue()) {
			totalCost += fi.getCost();
		}
		return totalCost;
	}

	/**
	 * @return The Customer who made the order.
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer The Customer that made the order
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return Just the order ID of this order (the phone_number)
	 */
	public String toString() {
		return "" + getOrderId();
	}

	public PeekResourceList<FoodItem> getFoodItems() {
		return items;
	}

	/**
	 * Adds a delay to when this Order can be marked for availability.
	 * Note: All values added are the absolute value.
	 *
	 * @param delay_time The amount to adjust the availability of this order.
	 */
	public void addDelay(int delay_time) {
		this.delay_expires = Math.abs(delay_time) + time.getCurrentTime();
	}

	/**
	 * The time code at which this order will be available (or when its delay
	 * expired).
	 *
	 * @return The time at which the delay expires.
	 */
	public int getDelayExpires() {
		return delay_expires;
	}

	public void cancelOrder() {
		order_canceled = true;
	}

	public boolean isOrderCanceled() {
		return order_canceled;
	}

	public void markOrderStarted() {
		order_started = time.getCurrentTime();
	}

	public void markOrderFinished() {
		order_finished = time.getCurrentTime();
	}

	public void markOrderPickedUp() {
		order_picked_up = time.getCurrentTime();
	}

	public void markOrderDroppedOff() {
		order_dropped_off = time.getCurrentTime();
	}

	public int getOrderStartTime() {
		return order_started;
	}

	public int getOrderPickedUpTime() {
		return order_picked_up;
	}

	public int getOrderDropOffTime() {
		return order_dropped_off;
	}

	public int getOrderFinishTime() {
		return order_finished;
	}

	public int getTotalOrderCreationTime() {
		return order_finished == -1 ? 0 : order_finished - order_started;
	}

	public int getTotalDeliveryTime() {
		return order_dropped_off == -1 ? 0 : order_dropped_off - order_picked_up;
	}

	public int getTotalTimeSpentOnOrder() {
		return getTotalOrderCreationTime() + getTotalDeliveryTime()
				+ (getOrderPickedUpTime() - getOrderFinishTime());
	}

	/**
	 * use to determine if order is complete
	 *
	 * @return boolean value ~order complete?
	 */
	public boolean completed() {
		return time.getCurrentTime() > order_finished && order_finished != -1;
	}

	public ArrayList<FoodItem> getFoodItemsList() {
		return fooditems_list;
	}
}
