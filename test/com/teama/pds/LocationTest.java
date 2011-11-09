package com.teama.pds;

import junit.framework.TestCase;

import java.util.HashMap;

/**
 * User: Ryan Brown
 */
public class LocationTest extends TestCase {
	private TimeReader tr;
	private Time t;
	private Location loc;
    private PizzaMenu m;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		t = Time.createTime();
		tr = new TimeReader(t);
		ResourceList<Truck> in = new ResourceList<Truck>();
		ResourceList<Truck> out = new ResourceList<Truck>();
		HashMap<String, Integer> l = new HashMap<String, Integer>();
		l.put("RIT", 240);
		try{
			loc = Location.createLocation(tr, l, in, out);
		}
		catch(AlreadyInstantiatedException e){
			loc = Location.getLocation();
		}
        ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
        m = config.getMenu();
		History h = new History();
		loc.setLogger(h);
	}

	public void testExecute() throws Exception {
		t.setCurTime(0);
		Truck v = new Truck("Steve", tr);
		Order o = new Order(tr, m);
		o.addItem(new FoodItem("salad", 2.50, 300, tr));
		o.addItem(new CookedItem("Pizza", 5.00, 300, 4, 300, tr));
		o.setLocation("RIT");
		v.addOrderForDelivery(o);
		assertEquals("Expected truck to have one order", 1, v.getNumOrders());
		loc.addInbound(v);
		t.setCurTime(200);
		assertEquals("One inbound truck", 1, loc.getInboundVehicles().getCount());
		assertEquals("Expected avail_time to be 240", 240, v.getAvailTime());
		assertEquals("Expected time to be 200", 200, tr.getCurrentTime());
		assertEquals("Truck unavailable", false, v.isAvailable());
		loc.execute();
		assertEquals("One inbound truck", 1, loc.getInboundVehicles().getCount());
		t.addSeconds(40);
		loc.execute();
		assertEquals("One delivering truck", 1, loc.getDeliveringVehicles().getCount());
		t.addSeconds(120);
		loc.execute();
		assertEquals("No inbound trucks", 0, loc.getInboundVehicles().getCount());
		assertEquals("No delivering trucks", 0, loc.getDeliveringVehicles().getCount());
		assertEquals("One outbound truck", 1, loc.getOutboundVehicles().getCount());
	}
}
