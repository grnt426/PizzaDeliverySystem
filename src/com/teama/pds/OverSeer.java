package com.teama.pds;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      Grant Kurtz
 * Contains all ResourceLists and PeekResourceLists used by the simulation.
 */
class OverSeer {

	/**
	 * The Orders that still need processing/shipping.
	 */
	private final ResourceList<Order> newOrders;

	/**
	 * The food that needs to be prepared/cooked.
	 */
	private final ResourceList<FoodItem> food;

	/**
	 * All the Ovens currently in use by the Kitchen.
	 */
	private final PeekResourceList<Oven> ovens;

	/**
	 * All the chefs the Kitchen has available for use.
	 */
	private final PeekResourceList<Chef> chefs;

	/**
	 * The ResourceList used for sending Trucks to their delivery location.
	 */
	private final ResourceList<Truck> outbound_trucks;

	/**
	 * The ResourceList used for sending Trucks back to the pizza shop.
	 */
	private final ResourceList<Truck> inboundTrucks;

	/**
	 * The ResourceList used for Trucks that are delivering their orders to
	 * their customers.
	 */
	private final ResourceList<Truck> delivery_trucks;

	/**
	 * Default Constructor.
	 * <p/>
	 * Brings in all Root level ResourceLists.  Sub-ResourceLists (such as
	 * accessing the FoodItems inside the Oven) should be queried from the
	 * Oven's ResourceList.
	 *
	 * @param newOrders	   The Orders that need to be processed by the
	 *                        Kitchen or are waiting to be delivered.
	 * @param food			All FoodItems that need to be processed by the
	 *                        Kitchen.
	 * @param ovens		   All Ovens that the Kitchen has access to.
	 * @param chefs		   All Chefs the Kitchen has access to.
	 * @param outbound_trucks The ResourceList used for sending Trucks to
	 *                        their delivery location.
	 * @param inbound_trucks  The ResourceList used for sending Trucks back to
	 *                        the pizza shop.
	 * @param delivery_trucks The ResourceList used for Trucks that are
	 *                        delivering their orders to their customers.
	 */
	public OverSeer(ResourceList<Order> newOrders, ResourceList<FoodItem> food,
					PeekResourceList<Oven> ovens, PeekResourceList<Chef> chefs,
					ResourceList<Truck> outbound_trucks,
					ResourceList<Truck> inbound_trucks,
					ResourceList<Truck> delivery_trucks) {
		this.newOrders = newOrders;
		this.food = food;
		this.ovens = ovens;
		this.chefs = chefs;
		this.outbound_trucks = outbound_trucks;
		this.inboundTrucks = inbound_trucks;
		this.delivery_trucks = delivery_trucks;
	}

	/**
	 * The Orders that still need processing/shipping.
	 *
	 * @return The Orders that still need processing/shipping.
	 */
	public ResourceList<Order> getNewOrders() {
		return newOrders;
	}

	/**
	 * The food that needs to be prepared/cooked.
	 *
	 * @return The food that needs to be prepared/cooked.
	 */
	public ResourceList<FoodItem> getFood() {
		return food;
	}

	/**
	 * All the Ovens currently in use by the Kitchen.
	 *
	 * @return All the Ovens currently in use by the Kitchen.
	 */
	public PeekResourceList<Oven> getOvens() {
		return ovens;
	}

	/**
	 * All the chefs the Kitchen has available for use.
	 *
	 * @return All the chefs the Kitchen has available for use.
	 */
	public PeekResourceList<Chef> getChefs() {
		return chefs;
	}

	/**
	 * A convenience function for grabbing all Chef's that are preparing
	 * FoodItems that belong to the provided Order.
	 *
	 * @param o The Order to check if a Chef is working on a FoodItem that
	 *          belongs to the Order.
	 * @return True if the Chef is working on a part of that Order, otherwise
	 * false.
	 */
	public List<Chef> getChefsWorkingOnOrder(Order o) {
		List<Chef> chefs = new ArrayList<Chef>();

		for (Chef chef : getChefs().getResourceQueue()) {
			if (chef.isWorkingOnOrder(o))
				chefs.add(chef);
		}

		return chefs;
	}

	/**
	 * A convenience function for grabbing all the ovens that are cooking items
	 * CookedItems that belong to the provided Order.
	 *
	 * @param order The Order to check if any Ovens are cooking CookedItems
	 *              that belong it.
	 * @return True if the Oven is cooking CookedItems that are part of the
	 * order, otherwise false.
	 */
	public List<Oven> getOvensWorkingOnOrder(Order order) {
		List<Oven> ovens = new ArrayList<Oven>();
		for (Oven oven : getOvens().getResourceQueue()) {
			if (oven.isCookingFoodFromOrder(order))
				ovens.add(oven);
		}
		return ovens;
	}

	/**
	 * A convenience function for finding Trucks that are driving with the
	 * provided Order.
	 *
	 * @param order The Order to find a Truck that is carrying it.
	 * @return The Truck that is driving with that Order, otherwise null
	 * if no Truck is outbound with that Order.
	 */
	public Truck getOutboundTruckDeliveringOrder(Order order) {
		for (Truck truck : outbound_trucks.getResourceQueue())
			if (truck.containsOrder(order))
				return truck;
		return null;
	}

	/**
	 * The ResourceList used for sending Trucks to their delivery location.
	 *
	 * @return The ResourceList used for sending Trucks to their delivery
	 * location.
	 */
	public ResourceList<Truck> getOutboundTrucks() {
		return outbound_trucks;
	}

	/**
	 * The ResourceList used for sending Trucks back to the pizza shop.
	 *
	 * @return The ResourceList used for sending Trucks back to the pizza shop.
	 */
	public ResourceList<Truck> getInboundTrucks() {
		return inboundTrucks;
	}

	/**
	 * A convenience function for funding a Truck that is currently trying to
	 * deliver the provided Order to the Customer.
	 *
	 * @param order The Order to find a Truck that is delivering to a Customer.
	 * @return The Truck that is delivering that Order, otherwise null if
	 * no Truck is making that delivery.
	 */
	public Truck getTruckDeliveringOrderToCustomer(Order order) {
		for (Truck truck : delivery_trucks.getResourceQueue())
			if (truck.containsOrder(order))
				return truck;
		return null;
	}

	/**
	 * The ResourceList used for Trucks that are delivering their orders to
	 * their customers.
	 *
	 * @return The ResourceList used for Trucks that are delivering their orders to
	 * their customers.
	 */
	public ResourceList<Truck> getDeliveryTrucks() {
		return delivery_trucks;
	}
}
