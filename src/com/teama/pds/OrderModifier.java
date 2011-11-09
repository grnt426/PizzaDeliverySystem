package com.teama.pds;

import java.util.List;

/**
 * Author:      Grant Kurtz
 */
public class OrderModifier {

	/**
	 * The instance object that holds all the ResourceLists.
	 */
	private OverSeer over_seer;


	private TimeReader time;

	/**
	 * Default Constructor
	 * <p/>
	 * Just initializes the OverSeer for use in getting all available
	 * ResourceLists as provided by the Model.
	 *
	 * @param over_seer The instance object that holds all the available
	 *                  ResourceLists.
	 * @param time	  The instance object that can report the accurate
	 *                  current time of the simulation.
	 */
	public OrderModifier(OverSeer over_seer, TimeReader time) {
		this.over_seer = over_seer;
		this.time = time;
	}

	/**
	 * Will completely cancel the order by removing any FoodItems being cooked
	 * or prepared, remove the Order from the waiting to be delivered queue,
	 * removing the order from the Truck that is performing the driving to the
	 * Location or delivering to the customer.
	 *
	 * @param order The order to be canceled.
	 */
	public void cancelOrder(Order order) {

		// To avoid a LOT of potential work, we are going to see where
		// the Order is and try to cancel only the parts we need to.
		ResourceList<Order> deliverables = over_seer.getNewOrders();
		if (deliverables.containsResource(order)) {

			// no point in doing all that looping and checks if the order
			// already cooked everything and is just waiting to be
			// delivered.
			if (!order.isAvailable()) {

				// first, check if any Chefs are preparing any FoodItems
				// contained in this Order.
				List<Chef> chefs = over_seer.getChefsWorkingOnOrder(order);
				for (Chef chef : chefs) {
					chef.cancelPreparation();
				}

				// Next, check for any Ovens that may be cooking CookedItems
				// contained in this Order.
				List<Oven> ovens = over_seer.getOvensWorkingOnOrder(order);
				for (Oven oven : ovens) {
					oven.stopCookingFoodFromOrder(order);
				}
			}

			// Remove all FoodItems from the food waiting to be processed
			ResourceList<FoodItem> foods = over_seer.getFood();
			for (FoodItem food : order.getFoodItems().getResourceQueue()) {
				foods.removeResource(food);
			}

			deliverables.removeResource(order);
		} else {

			// Since the order wasn't waiting to be delivered we need to find
			// the truck that is delivering it and cancel the order.
			Truck delivering_truck =
					over_seer.getOutboundTruckDeliveringOrder(order);
			if (delivering_truck != null) {
				delivering_truck.removeOrder(order);

				// Send the truck back to the pizza shop if he no longer has
				// orders to deliver.
				if (delivering_truck.getNumOrders() == 0) {
					int return_time = delivering_truck.getETAToLocation()
							- (delivering_truck.getAvailTime()
							- time.getCurrentTime());
					delivering_truck.setETAToLocation(return_time);
					over_seer.getOutboundTrucks().removeResource(
							delivering_truck);
					over_seer.getInboundTrucks().addResource(
							delivering_truck);
				}
			} else {

				// Alright, now we need to check if it is already at the
				// Location but has *yet* to deliver the order.
				delivering_truck =
						over_seer.getTruckDeliveringOrderToCustomer(order);
				if (delivering_truck != null) {
					delivering_truck.removeOrder(order);

					// Send the truck back to the shop if it no longer needs
					// to give any customers their orders.
					if (delivering_truck.getNumOrders() == 0) {
						over_seer.getDeliveryTrucks().removeResource(
								delivering_truck);
						delivering_truck.setETAToLocation(
								delivering_truck.getETAToLocation());
						over_seer.getInboundTrucks().addResource(
								delivering_truck);
					}
				}
			}
		}
		order.cancelOrder();
	}


}
