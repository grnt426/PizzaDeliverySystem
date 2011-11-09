package com.teama.pds;

import java.util.HashMap;

/**
 * Author:      Grant Kurtz
 * The Delivery controller keeps the fleet of vehicles that move all orders from
 * the store to the colleges.  It assigns orders to vehicles for delivery and
 * holds the orders until a vehicle returns for pick-up.
 */
public class Delivery extends ActionNode {

	/**
	 * Maintains an instance of itself to avoid double-initialization of this
	 * class
	 */
	private static Delivery delivery;

	/**
	 * The fleet of vehicles either currently waiting to deliver orders, or are
	 * returning from the drop-off location and will be arriving soon.  This
	 * difference is denoted by their availability.
	 */
	private final ResourceList<Truck> inbound_vehicles;

	/**
	 * The orders that need to be delivered, once they are available.
	 */
	private final ResourceList<Order> deliverables;

	/**
	 * The trucks that have left the pizza shop with a single order, and are
	 * driving to their drop-off location.
	 */
	private final ResourceList<Truck> outbound_vehicles;

	/**
	 * A list of possible destinations and their distances by drive time.
	 */
	private final HashMap<String, Integer> destinations;

	/**
	 * The logger for reporting all changes.
	 */
	private History logger;

	/**
	 * The available resources that can move orders from the store to their
	 * final destination
	 *
	 * @param time		 The object that will accurately report time
	 *                     information
	 * @param dest		 The HashMap of destination locations
	 * @param avail_trucks The inbox that will hold all trucks currently
	 *                     waiting or returning to the pizza shop
	 * @param outbound	 The outbox of trucks that are out for delivery.
	 * @param orders	   The inbox of all orders that will need to be
	 *                     delivered, once available.
	 */
	private Delivery(TimeReader time, HashMap<String, Integer> dest,
					 ResourceList<Truck> avail_trucks,
					 ResourceList<Truck> outbound, ResourceList<Order> orders) {
		super(time);
		inbound_vehicles = avail_trucks;
		deliverables = orders;
		outbound_vehicles = outbound;
		destinations = dest;
	}

	/**
	 * Keeps the requirement of only allowing one Delivery station to be
	 * operated at any given point in time.  This method may only be called
	 * exactly once.
	 *
	 * @param time		 The object that will accurately report time
	 *                     information
	 * @param dest		 The HashMap of destination locations
	 * @param avail_trucks The inbox that will hold all trucks currently
	 *                     waiting or returning to the pizza shop
	 * @param outbound	 The trucks that are leaving the Pizza Shop and going
	 *                     to their destination to deliver pizzas.
	 * @param orders	   The orders that are waiting to be delivered
	 *                     and completed.
	 * @return an already initialized instance of this class, or creates
	 *         a new one if this is the first time this class is being
	 *         created.
	 * @throws AlreadyInstantiatedException This is thrown if this method has
	 *                                      already been called.
	 */
	public static Delivery createDelivery(TimeReader time,
										  HashMap<String, Integer> dest,
										  ResourceList<Truck> avail_trucks,
										  ResourceList<Truck> outbound,
										  ResourceList<Order> orders)
			throws AlreadyInstantiatedException {
		if (delivery != null)
			throw new AlreadyInstantiatedException("The delivery class is " +
					"already instantiated.");
		else
			return (delivery = new Delivery(time, dest, avail_trucks, outbound,
					orders));
	}

	/**
	 * Note: One must call createDelivery() at least once before one can call
	 * this method.
	 *
	 * @throws NotInstantiatedException This is thrown if createDelivery() was
	 *                                  never called.
	 * @return The already instantiated Delivery instance.
	 */
	public static Delivery getDelivery() throws NotInstantiatedException {
		if (delivery == null)
			throw new NotInstantiatedException("The Delivery class was" +
					" not instantiated.");
		else
			return delivery;
	}

	/**
	 * Will check for orders that are available, and if so put them on a truck
	 * and send the truck to the order's destination.
	 */
	public void execute() {
		Truck truck;
		Order order;

		// first, check if any inbound vehicles arrived just now for logging
		// purposes
		inbound_vehicles.checkAllInternalResources();

		// Our Orders need to mark themselves as "finished" as soon as the last
		// FoodItem is done preparing/cooking.
		deliverables.checkAllInternalResources();

		// Check for both an available order AND an available truck
		while (inbound_vehicles.anyAvailable() && deliverables.anyAvailable()) {
			truck = inbound_vehicles.grabAvailable();
			order = deliverables.grabAvailable();
			order.markOrderPickedUp();
			truck.addOrderForDelivery(order);
			truck.setETAToLocation(destinations.get(order.getLocation()));
			outbound_vehicles.addResource(truck);
			truck.setOnLocation();
			Record r = new Record(truck + " will deliver order #" + order.getCustomer().getName() +
					" and will arrive at TC " + truck.getAvailTime(),
					Record.Status.TRANSITION, time.getCurrentTime());
			logger.addEvent(r);
		}
	}

	/**
	 * @param t The truck to add that can deliver orders.
	 */
	public void addTruck(Truck t) {
		inbound_vehicles.addResource(t);
	}

	/**
	 * @param order The order to add to our list of orders to deliver
	 *              once the order is available.
	 */
	public void addOrder(Order order) {
		deliverables.addResource(order);
	}

	/**
	 * @return The number of orders that still need to be delivered.
	 */
	public int getNumOrders() {
		return deliverables.getCount();
	}

	/**
	 * @return The number of vehicles that are driving to their location.
	 */
	public int getNumOutboundTrucks() {
		return outbound_vehicles.getCount();
	}

	/**
	 * Empties all in/out-boxes.
	 */
	public void clear() {
		outbound_vehicles.clear();
		inbound_vehicles.clear();
		deliverables.clear();
	}

	/**
	 * @return The number of vehicles either arriving or waiting at the
	 * pizza shop.
	 */
	public int getNumInboundTrucks() {
		return inbound_vehicles.getCount();
	}

	/**
	 * @return The number of trucks that are currently returning from
	 * delivering all the orders.
	 */
	public int getNumberArrivingTrucks() {
		int c = 0;
		for (Truck t : inbound_vehicles.getResourceQueue()) {
			if (!t.isAvailable() && t.getNumOrders() == 0)
				c++;
		}
		return c;
	}

	/**
	 * @param history The logger that this class should use for recording all
	 *                events.
	 */
	public void setLogger(History history) {
		this.logger = history;

		// give all the trucks the logger as well
		for (Truck t : inbound_vehicles.getResourceQueue())
			t.setLogger(logger);
	}

	public ResourceList<Truck> getOutboundTrucks() {
		return outbound_vehicles;
	}

	public ResourceList<Truck> getInboundTrucks() {
		return inbound_vehicles;
	}
}
