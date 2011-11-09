package com.teama.pds;

import junit.framework.TestCase;

/**
 * Author:      Grant Kurtz
 */
public class KitchenTest extends TestCase {

	// instance vars
	private Time t;
	private TimeReader tr;
	private Kitchen k;
	private ResourceList<Order> new_orders;
	private ResourceList<FoodItem> food_to_cook;
	private PeekResourceList<Oven> ovens;
	private Chef c;
	private Oven oven;
	private PizzaMenu menu;

	public void setUp() throws Exception {
		super.setUp();
		t = Time.createTime();
		t.setCurTime(0);
		tr = new TimeReader(t);
		History h = new History();
		ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
		menu = config.getMenu();

		// Chefs and Ovens
		PeekResourceList<Chef> chefs = new PeekResourceList<Chef>();
		c = new Chef(tr);
		chefs.addResource(c);
		food_to_cook = new ResourceList<FoodItem>();
		ovens = new PeekResourceList<Oven>();
		oven = new Oven(40, tr);
		ovens.addResource(oven);

		// The actual Kitchen
		try{
			k = Kitchen.createKitchen(tr, food_to_cook, ovens, chefs);
		}
		catch(AlreadyInstantiatedException e){
			k = Kitchen.getKitchen();
		}
		k.setLogger(h);
		k.addChef(c);
		new_orders = new ResourceList<Order>();
	}

	public void testAssignOrder() {

		// test initial Kitchen Setup
		assertEquals("The Kitchen expects two Chefs", 2, k.getChefCount());
		assertEquals("The Kitchen expects one Oven", 1, k.getOvenCount());

		// create and add an order
		Order o = new Order(tr, menu);
		CookedItem ci = new CookedItem("Pizza Logs", 2.50, 15, 4, 60, tr);
		o.addItem(ci);
		new_orders.addResource(o);
		food_to_cook.addResource(ci);

		// process a single second
		t.incrementTime();
		k.execute();

		// alright, see where our Kitchen is after a second
		assertEquals("There should be no food left to prepare.", 0,
				food_to_cook.getCount());
		assertNull("The Order should not be available yet.",
				new_orders.grabAvailable());
		assertEquals("The food should be finished preparing at TC 16.", 16,
				ci.getAvailTime());
		assertFalse("The Chef should not be ready.", c.isAvailable());
		assertEquals("The chef should be available at TC 16.", 16,
				c.getAvailTime());

		// make 14 seconds pass
		t.addSeconds(14);
		k.execute();

		// make sure our Kitchen did nothing
		assertEquals("We should be at TC 15.", 15, t.getCurTime());
		assertNull("The Order should not be available yet.",
				new_orders.grabAvailable());
		assertEquals("The food should be finished preparing at TC 16.", 16,
				ci.getAvailTime());
		assertFalse("The Chef should not be ready.", c.isAvailable());
		assertEquals("The chef should be available at TC 16.", 16,
				c.getAvailTime());
		assertTrue("The oven should be available.", oven.isAvailable());
		assertNotNull("We should get back an oven.", ovens.grabAvailable());
		assertTrue("The oven should have space for our item.",
				oven.hasSpaceFor(ci));

		// process a single second
		t.addSeconds(1);
		k.execute();

		// Our chef should now be ready, the oven should have 4 space consumed,
		// and the order should still not be ready
		assertTrue("The chef should now be available.", c.isAvailable());
		assertEquals("The oven should have 36 available space", 36,
				oven.getTotalFreeSpace());
		assertTrue("The Oven should still be available.", ovens.anyAvailable());
		assertNull("No orders should be available yet.",
				new_orders.grabAvailable());
		assertEquals("The food should be done cooking at 76.", 76,
				ci.getAvailTime());

		// process 59 more seconds
		t.addSeconds(59);
		k.execute();

		// the oven should still have the food in it
		assertEquals("The oven should have one food item in it.", 1,
				oven.getFoodCount());
		assertNull("No orders should be ready.", new_orders.grabAvailable());

		// process one more second
		t.incrementTime();
		k.execute();

		// make sure the food item is totally removed from the kitchen, and that
		// the order is now available
		assertEquals("The Oven should have no items in it.", 0,
				oven.getFoodCount());
		assertEquals("The original order should now be available.", o,
				new_orders.grabAvailable());
	}

	public void testNormalOrder() {
		Order o = new Order(tr, menu);
		CookedItem cm = menu.getCookedItem("Medium Pizza");
		CookedItem cmm = menu.getCookedItem("Medium Pizza");
		CookedItem cl = menu.getCookedItem("Large Pizza");
		CookedItem cpl = menu.getCookedItem("Pizza Logs");
		System.out.println(cm.toString());
		o.addItem(cm);
		o.addItem(cmm);
		o.addItem(cl);
		o.addItem(cpl);

		new_orders.addResource(o);
		food_to_cook.addResource(cm);
//        food_to_cook.addResource(cmm);
//        food_to_cook.addResource(cl);
//        food_to_cook.addResource(cpl);


		t.incrementTime();
		k.execute();
		System.out.println(food_to_cook.getCount());

		assertEquals(0, c.getAvailTime());
		//assertNull(cmm.getAvailTime());
		//assertNull(cl.getAvailTime());
		assertEquals(0, cpl.getAvailTime());


	}
}
