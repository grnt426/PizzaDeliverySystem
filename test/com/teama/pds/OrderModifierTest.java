package com.teama.pds;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * Author:      Grant Kurtz
 */
public class OrderModifierTest extends TestCase {

	Time time;
	TimeReader time_reader;
	PizzaMenu pm;
	OrderModifier order_modifier;
	ResourceList<FoodItem> foods;
	PeekResourceList<Oven> ovens;
	PeekResourceList<Chef> chefs;
	HashMap<String, Integer> dest;
	ResourceList<Truck> avail_trucks;
	ResourceList<Truck> outbound;
	ResourceList<Order> orders;
	ResourceList<Truck> delivering;
	Kitchen kitchen;
	Location location;
	History history;

	public void setUp() throws Exception {
		time = Time.createTime();
		time_reader = new TimeReader(time);
		pm = new PizzaMenu();
		history = new History();

		// Create all the ResourceLists and ActionNodes
		foods = new ResourceList<FoodItem>();
		ovens = new PeekResourceList<Oven>();
		chefs = new PeekResourceList<Chef>();
		try{
			kitchen = Kitchen.createKitchen(time_reader,  foods, ovens, chefs);
		}
		catch(AlreadyInstantiatedException e){
			kitchen = Kitchen.getKitchen();
			ovens = kitchen.getOvens();
			ovens.clear();
		}
		kitchen.setLogger(history);
		dest = new HashMap<String, Integer>();
		avail_trucks = new ResourceList<Truck>();
		outbound = new ResourceList<Truck>();
		orders = new ResourceList<Order>();
		Delivery delivery;
		try{
			 delivery = Delivery.createDelivery(time_reader, dest, avail_trucks, outbound,
					orders);
		}
		catch(AlreadyInstantiatedException e){
			delivery = Delivery.getDelivery();
			outbound = delivery.getOutboundTrucks();
			outbound.clear();
			avail_trucks = delivery.getInboundTrucks();
			avail_trucks.clear();
		}
		try{
			location = Location.createLocation(time_reader, dest, outbound,
					avail_trucks);
		}
		catch(AlreadyInstantiatedException e){
			location = Location.getLocation();
		}
		location.setLogger(history);
		delivering = location.getDeliveringVehicles();

		// create the actual class for testing
		OverSeer over_seer = new OverSeer(orders, foods, ovens, chefs, outbound,
				avail_trucks, location.getDeliveringVehicles());
		order_modifier = new OrderModifier(over_seer, time_reader);

		// create some filler data
		dest.put("Test", 15);
	}

	public void testCancelOrderWithNonExistentOrder() throws Exception {
		Order o = new Order(time_reader, pm);
		o.addItem(new FoodItem("FDFD", 0.0, 50, time_reader));
		order_modifier.cancelOrder(o);
	}

	public void testCancelOrderJustAdded() throws Exception {
		Order o = new Order(time_reader,  pm);
		FoodItem fi = new FoodItem("DSFDSF", 0, 50, time_reader);
		o.addItem(fi);
		foods.addResource(fi);
		orders.addResource(o);
		order_modifier.cancelOrder(o);
		assertEquals("There should be no food left.", 0, foods.getCount());
		assertEquals("There should be no orders left.", 0, orders.getCount());
	}

	public void testCancelOrderWithOthersPresent() throws Exception {
		Order o = new Order(time_reader,  pm);
		FoodItem fi = new FoodItem("DSFDSF", 0, 50, time_reader);
		o.addItem(fi);
		foods.addResource(fi);
		orders.addResource(o);

		// create a second order
		Order o2 = new Order(time_reader,  pm);
		FoodItem fi2 = new FoodItem("DSFDSF", 0, 50, time_reader);
		o2.addItem(fi2);
		foods.addResource(fi2);
		orders.addResource(o2);

		order_modifier.cancelOrder(o);
		assertEquals("There should be one food left.", 1, foods.getCount());
		assertEquals("There should be one order left.", 1, orders.getCount());
	}

	public void testCancelOrderBeingPrepared() throws Exception {

		// clean up from other calls
		foods.clear();
		orders.clear();

		Order o = new Order(time_reader,  pm);
		FoodItem fi = new FoodItem("DSFDSF", 0, 50, time_reader);
		o.addItem(fi);
		foods.addResource(fi);
		orders.addResource(o);
		Chef c = new Chef(time_reader);
		c.setLogger(history);
		chefs.addResource(c);
		kitchen.execute();
		assertFalse("There should be no available chefs.",
				chefs.anyAvailable());
		order_modifier.cancelOrder(o);
		assertTrue("There should now be an available chef.",
				chefs.anyAvailable());
	}

	public void testCancelOrderBeingCooked() throws Exception {

		// clean up from other calls
		chefs.clear();

		// setup our test case
		Order o = new Order(time_reader,  pm);
		CookedItem fi = new CookedItem("sdfsdf", 0, 0, 5, 15, time_reader);
		o.addItem(fi);
		foods.addResource(fi);
		orders.addResource(o);
		Chef c = new Chef(time_reader);
		c.setLogger(history);
		Oven oven = new Oven(10, time_reader);
		oven.setLogger(history);
		ovens.addResource(oven);
		kitchen.addChef(c);
		kitchen.execute();
		time.incrementTime();
		kitchen.execute();
		assertEquals("The Oven should now have 5 free space left.", 5,
				oven.getTotalFreeSpace());
		order_modifier.cancelOrder(o);
		assertEquals("The Oven should now have 10 free space left.", 10,
				oven.getTotalFreeSpace());
	}

	public void testCancelOrderWaitingForDelivery() throws Exception{
		Order o = new Order(time_reader, pm);
		orders.addResource(o);
		assertTrue("There should be an available order", orders.anyAvailable());
		order_modifier.cancelOrder(o);
		assertEquals("There should be no orders left.", 0, orders.getCount());
	}

	public void testCancelOrderOutbound() throws Exception{
		Order o = new Order(time_reader, pm);
		Truck t = new Truck("dfsdf", time_reader);
		outbound.addResource(t);
		t.addOrderForDelivery(o);
		t.setETAToLocation(50);
		time.addSeconds(35);
		order_modifier.cancelOrder(o);
		assertFalse("The Truck should have not have that order.",
				t.containsOrder(o));
		assertEquals("The Truck should have 35 seconds until availability.",
				35, t.getETAToLocation());
		assertFalse("The Truck should no longer be in the outbound queue.",
				outbound.containsResource(t));
		assertTrue("The Truck should be in the inbound queue.",
				avail_trucks.containsResource(t));
		assertFalse("There should be no available trucks.",
				avail_trucks.anyAvailable());
	}

	public void testCancelOrderBeingDelivered() throws Exception{
		Order o = new Order(time_reader, pm);
		Truck t = new Truck("dfsdf", time_reader);
		outbound.addResource(t);
		t.addOrderForDelivery(o);
		t.setETAToLocation(50);
		time.addSeconds(51);
		location.execute();
		order_modifier.cancelOrder(o);
		assertFalse("The Truck should have not have that order.",
				t.containsOrder(o));
		assertEquals("The Truck should have 50 seconds until availability.",
				50, t.getETAToLocation());
		assertFalse("The Truck should no longer be in the delivering queue.",
				delivering.containsResource(t));
		assertTrue("The Truck should be in the inbound queue.",
				avail_trucks.containsResource(t));
		assertFalse("There should be no available trucks.",
				avail_trucks.anyAvailable());
	}

	public void testCancelOrderAlreadyDelivered() throws Exception{
		Order o = new Order(time_reader, pm);
		Truck t = new Truck("dfsdf", time_reader);
		outbound.addResource(t);
		t.addOrderForDelivery(o);
		t.setETAToLocation(50);
		time.addSeconds(51);
		location.execute();
		time.addSeconds(121);
		location.execute();
		assertFalse("The Truck should have not have that order.",
				t.containsOrder(o));
		order_modifier.cancelOrder(o);
		assertEquals("The Truck should have 50 seconds until availability.",
				50, t.getETAToLocation());
		assertTrue("The Truck should be in the inbound queue.",
				avail_trucks.containsResource(t));
		assertFalse("There should be no available trucks.",
				avail_trucks.anyAvailable());
	}
}
