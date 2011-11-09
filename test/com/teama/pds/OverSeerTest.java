package com.teama.pds;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * Author:      Grant Kurtz
 */
public class OverSeerTest extends TestCase {

	Time time;

	TimeReader time_reader;

	public void setUp() throws Exception {

		time = Time.createTime();
		time_reader = new TimeReader(time);

		// Create all the ResourceLists and ActionNodes
		ResourceList<FoodItem> foods = new ResourceList<FoodItem>();
		PeekResourceList<Oven> ovens = new PeekResourceList<Oven>();
		PeekResourceList<Chef> chefs = new PeekResourceList<Chef>();
		Kitchen.createKitchen(time_reader,  foods, ovens, chefs);
		HashMap<String, Integer> dest = new HashMap<String, Integer>();
		ResourceList<Truck> avail_trucks = new ResourceList<Truck>();
		ResourceList<Truck> outbound = new ResourceList<Truck>();
		ResourceList<Order> orders = new ResourceList<Order>();
		Delivery.createDelivery(time_reader,  dest, avail_trucks, outbound,
				orders);
		Location l = Location.createLocation(time_reader, dest, outbound,
				avail_trucks);

		// create the actual class for testing
		OverSeer over_seer = new OverSeer(orders, foods, ovens, chefs, outbound,
				avail_trucks, l.getDeliveringVehicles());
	}
}
