package com.teama.pds;

import com.teama.pds.FoodItem;
import com.teama.pds.Time;
import com.teama.pds.TimeReader;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Jason Greaves
 */
public class FoodItemTest extends TestCase {

	private TimeReader time;
	private FoodItem pre;
	private FoodItem f;

	@Before
	public void setUp() throws Exception {
		Time t = Time.createTime();
		time = new TimeReader(t);
		pre = new FoodItem("Salad", 5.00, 5, time);
	}

	//First three tests use a food item with nothing initialized
	@Test
	public void testSetName() throws Exception {
		f = new FoodItem(time);
		f.setName("Small Pizza");
		assertEquals("Small Pizza", f.getName());
	}

	@Test
	public void testSetCost() throws Exception {
		f = new FoodItem(time);
		f.setCost(8.00);
		assertEquals(8.00, f.getCost());
	}

	@Test
	public void testSetPrepTime() throws Exception {
		f = new FoodItem(time);
		f.setPrepTime(13);
		assertEquals(13, f.getPrepTime());
	}

	//Next 3 tests get info from an already configured FoodItem
	@Test
	public void testGetName() throws Exception {
		assertEquals("Salad", pre.getName());

	}

	@Test
	public void testGetCost() throws Exception {
		assertEquals(5.00, pre.getCost());

	}

	@Test
	public void testGetPrepTime() throws Exception {
		assertEquals(5, pre.getPrepTime());

	}
}
