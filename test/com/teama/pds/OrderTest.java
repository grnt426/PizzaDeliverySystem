package com.teama.pds;

import junit.framework.TestCase;
import org.junit.Before;

/**
 * User: Jason Greaves
 */
public class OrderTest extends TestCase {
	private Time t;
	private TimeReader time;
	private StoreConfiguration config;
	private PizzaMenu m;

	@Before
	public void setUp() throws Exception {
		t = Time.createTime();
		time = new TimeReader(t);
		ConfigManager cfgm = new ConfigManager(time);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
		m = config.getMenu();
	}

	public void testAddingAnItem() {
		Order o = new Order(time, m);
		o.addItem(m.getCookedItem("Small Pizza"));
		o.addItem(m.getCookedItem("Medium Pizza"));
		assertEquals("2 orders should have been added to the order to total" +
				" a cost of $19.00.",
				19.00, o.getTotalCost());

	}

	public void testAddingAnItemWithHalfToppings() {
		Order o = new Order(time, m);
		CookedItem c = m.getCookedItem("Large Pizza");
		PizzaItem p = new PizzaItem(c.getName(), 0.5, time, c);
		p.addTopping(PizzaItem.Topping.SAUSAGE);
		o.addItem(p);
		assertEquals("Half toppings for a large pizza is $1.00. A large pizza" +
				" is 16.00. Add them and get $17.00", 17.00, o.getTotalCost());
	}

	public void testCookedAndUncookedItems() {
		Order o = new Order(time, m);
		o.addItem(m.getCookedItem("Small Pizza"));
		o.addItem(m.getUncookedItem("Salad"));
		assertEquals("These should equal 13.00.", 13.00, o.getTotalCost());
	}

	public void testGetHighest(){
		Order o = new Order(time, m);
		CookedItem c = m.getCookedItem("Large Pizza");
		PizzaItem p = new PizzaItem(c.getName(), .5, time, c);
		CookedItem pl = m.getCookedItem("Pizza Logs");

		// add the items
		o.addItem(p);
		o.addItem(pl);

		// set the two items cooking
		p.prepareFood();
		pl.prepareFood();

		// the highest should be correctly determined.
		assertEquals("The time available should be the highest of the two, " +
				"the Pizza.", o.getAvailTime(), p.getAvailTime());
		pl.cookFood();
		assertEquals("The time available should be the highest of the two, " +
				"the Pizza.", o.getAvailTime(), p.getAvailTime());
		pl.setAvailTime(1500);
		assertEquals("The time available should be the highest of the two, " +
				"the Pizza Logs.", o.getAvailTime(), p.getAvailTime());
		p.cookFood();
		assertEquals("The time available should be the highest of the two, " +
				"the Pizza", o.getAvailTime(), p.getAvailTime());
		p.setAvailTime(1501);
		assertEquals("The time available should be the highest of the two, " +
				"the Pizza.", o.getAvailTime(), p.getAvailTime());
	}
}
