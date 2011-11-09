package com.teama.pds;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * User: Ryan Brown
 */
public class DeliveryTest extends TestCase {
	private Delivery d;
	private Location loc;
	private Time t;
	private TimeReader tr;
	private PizzaMenu m;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		t = Time.createTime();
		tr = new TimeReader(t);
		ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
		m = config.getMenu();
		HashMap<String, Integer> h = new HashMap<String, Integer>();
		h.put("RIT", 240);
		ResourceList<Truck> del_in = new ResourceList<Truck>();
		ResourceList<Truck> del_out = new ResourceList<Truck>();
		ResourceList<Order> del_orders = new ResourceList<Order>();
		try{
			d = Delivery.createDelivery(new TimeReader(t), h, del_in, del_out,
				del_orders);
		}
		catch(AlreadyInstantiatedException e){
			d = Delivery.getDelivery();
		}
		try{
			loc = Location.createLocation(tr, h, del_out, del_in);
		}
		catch(AlreadyInstantiatedException e){
			loc = Location.getLocation();
		}
	}

	@Override
	public void tearDown() throws Exception {
		d.clear();
		loc.clear();
		super.tearDown();
	}

	public void testCreateDelivery() throws Exception {
		try{
			Delivery.createDelivery(new TimeReader(t),
					new HashMap<String, Integer>(), new ResourceList<Truck>(),
					new ResourceList<Truck>(), new ResourceList<Order>());
			throw new Exception("An AlreadyInstantiatedException was expected" +
					" to be thrown, but was not.");
		}
		catch(AlreadyInstantiatedException e){
			// normal operation
		}
	}

	public void testAddTruck() throws Exception {
		assertEquals("There should be no trucks in the Delivery", 0,
				d.getNumInboundTrucks());
		d.addTruck(new Truck("Steve", tr));
		assertEquals("There should be one truck in the Delivery", 1,
				d.getNumInboundTrucks());
		d.addTruck(new Truck("Joe", tr));
		assertEquals("There should be two trucks in the Delivery", 2,
				d.getNumInboundTrucks());

	}

	public void testAddOrder() throws Exception {
		d.addOrder(new Order(tr, m));
	}

	public void testAssignOrder() throws Exception {
		History h = new History();
		d.setLogger(h);
		loc.setLogger(h);
		d.clear();
		d.addTruck(new Truck("Steve", tr));
		assertEquals("Expected one truck to be in Delivery", 1,
				d.getNumInboundTrucks());
		Order o = new Order(tr, m);
		o.setLocation("RIT");
		d.addOrder(o);
		assertEquals("Expected one order to be in Delivery", 1,
				d.getNumOrders());
		d.execute();
		assertEquals("Expected no orders to be in Delivery", 0,
				d.getNumOrders());
		assertEquals("Expected no trucks in Delivery", 0,
				d.getNumInboundTrucks());
		assertEquals("Expected one truck to be in outbound queue", 1,
				d.getNumOutboundTrucks());
		//Now we see if location works properly
		t.addSeconds(200);
		loc.execute();
		assertEquals("One truck outbound from delivery", 1,
				d.getNumOutboundTrucks());
		assertEquals("One truck inbound to location", 1,
				loc.getNumInboundTrucks());
		t.addSeconds(40);
		loc.execute();
		assertEquals("No trucks inbound to location", 0,
				loc.getNumInboundTrucks());
		assertEquals("One truck delivering to location", 1,
				loc.getNumDeliveringTrucks());
		assertEquals("No trucks outbound from location", 0,
				loc.getNumOutboundTrucks());
		t.addSeconds(120);
		loc.execute();
		assertEquals("No trucks delivering to location", 0,
				loc.getNumDeliveringTrucks());
		assertEquals("One truck outbound from location", 1,
				loc.getNumOutboundTrucks());
		assertEquals("One truck inbound to delivery", 1,
				d.getNumInboundTrucks());
/*		o = new Order(tr);
		o.setLocation("RIT");
		d.addOrder(o);
		d.execute();
		assertEquals("One truck inbound to delivery", 1, d.getNumInboundTrucks());*/
	}
}
