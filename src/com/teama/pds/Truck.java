package com.teama.pds;

import java.util.ArrayList;

/**
 * Author:      Grant Kurtz
 * The Entity class that acts to "transport" orders from the store to the
 * customer's location.
 */
public class Truck extends Resource {

	/**
	 * The name of the driver.
	 */
	private String driver_name;

	private int eta_to_location;

	/**
	 * boolean to help tell view that the truck is on it's way to
	 * the delivery location
	 */
	private boolean on_location = false;

	/**
	 * boolean to help tell view that the truck is delivering
	 */
	private boolean on_delivery = false;

	/**
	 * boolean to help tell view that the truck is returning to the store.
	 */
	private boolean on_return = false;

	/**
	 * The list of orders this truck has in it for delivery.
	 */
	private final ArrayList<Order> orders;

	/**
	 * Default Constructor
	 *
	 * @param driver_name The name of the person driving this truck
	 * @param time		The time object used for reporting accurate time
	 *                    information of the simulation.
	 */
	public Truck(String driver_name, TimeReader time) {
		super(time);
		this.driver_name = driver_name;
		orders = new ArrayList<Order>();
	}

	/**
	 * @return The name of the person driving this truck
	 */
	String getDriverName() {
		return driver_name;
	}

	/**
	 * @param driver_name The new name of the person driving this truck
	 */
	public void setDriverName(String driver_name) {
		this.driver_name = driver_name;
	}

	/**
	 * NOTE: The current implementation of the program does not check where all
	 * the orders in a truck are going, only the first order, and for that
	 * matter only really expects the truck to have a single order.  Be careful
	 * when calling this function.
	 *
	 * @param order The order to add to this truck for delivery.
	 */
	public void addOrderForDelivery(Order order) {
		orders.add(order);
	}

	public void setETAToLocation(int eta) {
		eta_to_location = eta;
		setAvailTime(time.getCurrentTime() + eta);
	}

	public int getETAToLocation() {
		return eta_to_location;
	}

	/**
	 * Sets the vehicle's availability in the future by two minutes for every
	 * order in this truck.
	 */
	public void startDelivering() {
		setAvailTime(time.getCurrentTime() + (120 * orders.size()));

	}

	/**
	 * @param order_id The order to remove from this truck
	 * @return The order that was removed, otherwise null.
	 */
	public Order removeOrder(String order_id) {
		if (order_id.equals("")) {
			Order o = orders.get(0);
			orders.clear();
			return o;
		}
		for (Order o : orders) {
			if (o.getOrderId().equals(order_id)) {
				orders.remove(o);
				return o;
			}
		}
		return null;
	}

	/**
	 * @return True if this truck is ready to deliver an order, otherwise
	 * false.
	 */
	public boolean isAvailable() {

		// The tuck must do its own logging for when it arrives back at the
		// pizza shop
		if (orders.size() == 0 && getAvailTime() != 0 &&
				getAvailTime() == time.getCurrentTime()) {
			eta_to_location = 0;
			Record r = new Record(this + " arrived back at the pizza shop.",
					Record.Status.TRANSITION, time.getCurrentTime());
			getLogger().addEvent(r);
		}
		return time.getCurrentTime() >= getAvailTime();
	}

	/**
	 * Orders trucks based on arrival time.
	 *
	 * @param r The truck to compare against.
	 * @return A negative integer if this truck will arrive sooner, 0 if they
	 *         will arrive at the same time, and a positive integer if this
	 *         truck will arrive later then the passed truck.
	 */
	public int compareTo(Resource r) {
		Truck t = (Truck) r;
		return getAvailTime() - t.getAvailTime();
	}

	/**
	 * @return The number of orders that are in the truck waiting for delivery.
	 */
	public int getNumOrders() {
		return orders.size();
	}

	/**
	 * Grabs an order from the truck and determines its destination.
	 * Since all orders in a truck go to the same place, it doesn't matter
	 * which one you grab.
	 *
	 * @return The end location of the first Order in the list.
	 */
	public String getDestination() {
		return orders.get(0).getLocation();
	}

	/**
	 * @return A string representing waht this truck driver is doing.
	 */
	public String toString() {
		String msg = "Truck Driver " + getDriverName();
		if (getAvailTime() < time.getCurrentTime())
			msg += ", will arrive at TC " + getAvailTime();
		return msg;
	}

	public boolean containsOrder(Order order) {
		return orders.contains(order);
	}

	public void removeOrder(Order order) {
		orders.remove(order);
	}

	public void removeOrders() {
		orders.clear();
	}

	public void markAllOrdersDelivered() {
		for (Order order : orders) {
			order.markOrderDroppedOff();
			order.markOrderFinished();
		}
	}

	/**
	 * @return Array list of orders driver is carrying
	 */
	public ArrayList<Order> getDriverOrders() {
		return orders;
	}

	/**
	 * @return if the truck is currently on it's way to destination
	 */
	public boolean isOnLocation() {
		return on_location;
	}

	/**
	 * set boolean if truck is on location or not
	 */
	public void setOnLocation() {
		if (on_location) {
			on_location = false;
		} else {
			on_location = true;
		}

	}

	/**
	 * @return if the truck is currently delivering
	 */
	public boolean isOnDelivery() {
		return on_delivery;
	}

	/**
	 * set boolean if truck is delivering or not.
	 */
	public void setOnDelivery() {
		if (on_delivery) {
			on_delivery = false;
		} else {
			on_delivery = true;
		}

	}

	/**
	 * @return if truck is returning to the store
	 */
	public boolean isReturning() {
		return on_return;
	}

	/**
	 * set boolean if truck is returning to the store or not
	 */
	public void setReturning() {
		if (on_return) {
			on_return = false;
		} else {
			on_return = true;
		}

	}
}
