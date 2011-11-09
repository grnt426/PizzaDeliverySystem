package com.teama.pds;

import com.teama.pds.CookedItem;
import com.teama.pds.FoodItem;
import com.teama.pds.Time;
import com.teama.pds.TimeReader;
import junit.framework.TestCase;

/**
 *
 * User: Jason Greaves
 *
 */
public class CookedItemTest extends TestCase {
    private Time t;
    private TimeReader time;
    private CookedItem c;
    public void setUp() throws Exception {
        t = Time.createTime();
        time =  new TimeReader(t);
    }

    public void testGetPrice(){
        c = new CookedItem("Small Pizza", 8.00, 8, 1, 13, time);
        assertEquals(8.00, c.getCost());
    }

    public void testGetName(){
        c = new CookedItem("Large Pizza", 16.00, 15, 4, 20, time);
        assertEquals("Large Pizza", c.getName());
    }

    public void testGetPrepTime(){
        c = new CookedItem("Pizza Logs", 6.00, 0, 1, 10, time);
        assertEquals(0, c.getPrepTime());
    }

    public void testGetCookTime(){
        c = new CookedItem("Medium Pizza", 11.00, 10, 2, 15, time);
        assertEquals(15, c.getCookTime());
    }

	public void testCookFood(){
		c = new CookedItem("Medium Pizza", 11.00, 10, 2, 15, time);
		c.cookFood();
		assertEquals("The availability of the Medium Pizza should be 15.", 15,
				c.getAvailTime());
		t.addSeconds(10);
		c.cookFood();
		assertEquals("The availability fo the Medium Pizza should be 25.", 25,
				c.getAvailTime());
	}

	public void testClone(){
		c = new CookedItem("Medium Pizza", 11.00, 10, 2, 15, time);
		CookedItem cc = c.clone();
		assertEquals("The name of the cloned pizza should be the same.",
				c.getName(), cc.getName());
		assertEquals("The cost of the cloned pizza should be the same.",
				c.getCost(), cc.getCost());
		assertEquals("The preparation time of the cloned pizza should be the " +
				"same. ", c.getPrepTime(), cc.getPrepTime());
		assertEquals("The space of the cloned pizza should be the same.",
				c.getSpace(), cc.getSpace());
		assertEquals("The cook time of the cloned pizza should be the same.",
				c.getCookTime(), cc.getCookTime());
		assertEquals("The time reader object of the cloned pizza should be" +
				"the same", c.getTimeReader(), cc.getTimeReader());
	}

	public void testToString(){
		c = new CookedItem("Medium Pizza", 11.00, 10, 2, 15, time);
		assertEquals("The string representation of this object should be " +
				"the item name.", c.toString(), c.getName());
	}
}
