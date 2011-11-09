package com.teama.pds;

import java.util.HashMap;

/**
 * User: Ryan Brown
 * Handles receiving, putting trucks into delivery mode, and then returning
 * trucks to the pizza shop.
 */
public class Location extends ActionNode {

	/**
	 * Maintains the singleton requirement.
	 */
	private static Location location;

	/**
	 * Holds the list of destinations and their return times to the shop from
	 * them.
	 */
	private final HashMap<String, Integer> locations;

	/**
	 * All vehicles currently going towards their destinations.
	 */
	private final ResourceList<Truck> inbound_vehicles;

	/**
	 * All vehicles returning to the pizza shop, or are currently sitting at the
	 * pizza shop.
	 */
	private final ResourceList<Truck> outbound_vehicles;

	/**
	 * Vehicles that are currently out delivering orders.
	 */
	private final ResourceList<Truck> delivering_vehicles;

	/**
	 * The object that will have all the activities of Location dumped to.
	 */
	private History logger;

	/**
	 * Default Constructor
	 *
	 * @param time	 The object that will accurately report the current time
	 * @param locs	 Holds the list of destinations and their return times
	 *                 to the shop from them.
	 * @param inbound  All vehicles currently going towards their destinations.
	 * @param outbound All vehicles returning to the pizza shop, or are
	 *                 currently sitting at the pizza shop.
	 */
	private Location(TimeReader time, HashMap<String, Integer> locs,
					 ResourceList<Truck> inbound, ResourceList<Truck> outbound) {
		super(time);
		inbound_vehicles = inbound;
		outbound_vehicles = outbound;
		delivering_vehicles = new ResourceList<Truck>();
		locations = locs;
	}

	/**
	 * Maintains the singleton relationship.
	 *
	 * @param tr	  the time object for reporting accurate time information.
	 * @param h	   the list of destinations and their return times to the
	 *                shop.
	 * @param del_out The trucks taht are currently going to lcoations.
	 * @param del_in  The trucks that are currently going back to the shop.
	 * @throws AlreadyInstantiatedException This is thrown if this method has
	 *                                      already been called.
	 * @return a newly created location, or the previously created one.
	 */
	public static Location createLocation(TimeReader tr,
										  HashMap<String, Integer> h,
										  ResourceList<Truck> del_out,
										  ResourceList<Truck> del_in)
			throws AlreadyInstantiatedException {
		if (location != null)
			throw new AlreadyInstantiatedException("The Location class has " +
					"already been instantiated.");
		else
			return (location = new Location(tr, h, del_out, del_in));
	}

	/**
	 * Note: One MUST call createLocation() at least once before calling this
	 * method.
	 *
	 * @throws AlreadyInstantiatedException This is thrown if the
	 *                                      createLocation() method was not
	 *                                      called.
	 * @return The already instantiated Location instance.
	 */
	public static Location getLocation() throws AlreadyInstantiatedException {
		if (location == null)
			throw new AlreadyInstantiatedException("The Location class has " +
					"not been instantiated.");
		else
			return location;
	}

	/**
	 * Processes all trucks that are arriving, processes trucks doing delivery,
	 * and sends trucks back home that are finished.
	 */
	public void execute() {

		// Grab all trucks that arrived at their destination
		while (inbound_vehicles.anyAvailable()) {
			Truck t = inbound_vehicles.grabAvailable();
			t.startDelivering();
			delivering_vehicles.addResource(t);
			t.setOnDelivery();
			Record r = new Record(t + " arrived at " + t.getDestination() +
					" and will finish delivering the orders at TC " +
					t.getAvailTime(), Record.Status.TRANSITION,
					time.getCurrentTime());
			logger.addEvent(r);
		}

		// grab all trucks that finished delivering orders
		while (delivering_vehicles.anyAvailable()) {
			Truck t = delivering_vehicles.grabAvailable();

			// mark all orders as delivered
			t.markAllOrdersDelivered();

			// Remove all the orders in the truck and send it on its way
			t.setAvailTime(t.getETAToLocation() + time.getCurrentTime());
			t.removeOrders();
			outbound_vehicles.addResource(t);
			t.setReturning();
			Record r = new Record(t + " finished delivering the orders and " +
					"will return to the pizza shop at TC " + t.getAvailTime(),
					Record.Status.TRANSITION, time.getCurrentTime());

			logger.addEvent(r);
		}
	}

	/**
	 * @param destination The destination to check for
	 * @return the time to return to the pizza shop from that
	 * destination
	 */
	public Integer getTravelTime(String destination) {
		return locations.get(destination);
	}

	/**
	 * @return All the destinations and their return time to the shop.
	 */
	public HashMap<String, Integer> getHashmap() {
		return locations;
	}

	/**
	 * @param t The truck that is on its way to the location.
	 * @deprecated This isn't actually needed, just for unit testing.
	 */
	public void addInbound(Truck t) {
		int travel_time = locations.get(t.getDestination());
		t.setAvailTime(time.getCurrentTime() + travel_time);
		inbound_vehicles.addResource(t);
	}

	/**
	 * @param t The truck to return to the pizza shop.
	 * @deprecated This isn't actually used, only for unit testing.
	 */
	public void addOutbound(Truck t) {
		outbound_vehicles.addResource(t);
	}

	/**
	 * @return The trucks that are currently performing deliveries to customers
	 */
	public ResourceList<Truck> getDeliveringVehicles() {
		return delivering_vehicles;
	}

	/**
	 * @return The trucks that are returning to or are currently at the pizza
	 * shop
	 */
	public ResourceList<Truck> getOutboundVehicles() {
		return outbound_vehicles;
	}

	/**
	 * @return The trucks that are currently driving to their locations.
	 */
	public ResourceList<Truck> getInboundVehicles() {
		return inbound_vehicles;
	}

	/**
	 * @return The number of trucks that are driving towards their locations.
	 */
	public int getNumInboundTrucks() {
		return inbound_vehicles.getCount();
	}

	/**
	 * @return The number of vehicles currently returning to the pizza shop,
	 * or are at the pizza shop.
	 */
	public int getNumOutboundTrucks() {
		return outbound_vehicles.getCount();
	}

	/**
	 * Clears all elements from all in/out-boxes.
	 */
	public void clear() {
		outbound_vehicles.clear();
		delivering_vehicles.clear();
		inbound_vehicles.clear();
	}

	/**
	 * @return The number of vehicles currently returning to the pizza
	 * shop, or are at the pizza shop.
	 */
	public int getNumDeliveringTrucks() {
		return delivering_vehicles.getCount();
	}

	/**
	 * @param history the object for reporting all events from this class.
	 */
	public void setLogger(History history) {
		this.logger = history;
	}
}
