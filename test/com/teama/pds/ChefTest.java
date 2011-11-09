package com.teama.pds;

import com.teama.pds.*;
import junit.framework.TestCase;
import org.junit.Before;

/**
* Created by IntelliJ IDEA.
* User: Jasong
* Date: 10/12/11
* Time: 9:47 PM
* To change this template use File | Settings | File Templates.
*/
public class ChefTest extends TestCase{
	private TimeReader tr;
    private Chef c;
    private History h;
	PeekResourceList<Oven> ovens;
    @Before


    public void testChefSetUp(){
        FoodItem f = new FoodItem("Salad", 5.00, 5, tr);
        try {
            c.setFood(f);
        } catch (AlreadyCookingOrderException e) {

        }
        assertEquals("Chef should have 5 minutes of setup time.", 5, c.getAvailTime());

    }

    public void testUnavailableChef() throws Exception{
        FoodItem f = new FoodItem("Salad", 5.00, 5, tr);
        CookedItem ci = new CookedItem("Small Pizza", 8.00, 8, 1, 13, tr);
        try {
            c.setFood(f);
            c.setFood(ci);
            throw new Exception("An AlreadyCookingOrderException should have " +
                    "been thrown.");
        } catch (AlreadyCookingOrderException e) {

        }
        assertFalse("Chef should be unavailable.", c.isAvailable());
    }

	public void testFoodOfZeroPrepTime() throws Exception {
		FoodItem f = new FoodItem("SDFDSF", 0, 0, tr);
		c.setFood(f);
		assertTrue("The Chef should still be available.", c.isAvailable());
	}

	public void testFoodOfZeroPrepTimeToOven() throws Exception {
		CookedItem ci = new CookedItem("dfsdf", 0, 0, 5, 15, tr);
		c.setFood(ci);
		assertTrue("The Chef should still be available.", c.isAvailable());
		assertEquals("The oven should have 35 free space left", 35,
				ovens.grabAvailable().getTotalFreeSpace());
	}

    public void setUp() throws Exception {
		Time t = Time.createTime();
        tr = new TimeReader(t);
        c = new Chef(tr);
        h = new History();
		c.setLogger(h);
		ovens = new PeekResourceList<Oven>();
		Oven o = new Oven(40, tr);
		o.setLogger(h);
		ovens.addResource(o);
		c.setOvens(ovens);
    }
}
