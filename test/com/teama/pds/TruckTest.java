package com.teama.pds;

import com.teama.pds.*;
import junit.framework.TestCase;
import org.junit.Before;

/**
* Created by IntelliJ IDEA.
* User: Jasong
* Date: 10/13/11
* Time: 11:52 AM
* To change this template use File | Settings | File Templates.
*/
public class TruckTest extends TestCase{
    private Time t;
    private TimeReader tr;
    private Truck tu;
    private PizzaMenu m;
    private CookedItem c;
    private FoodItem f;

    public void testAddingOrder(){
        Order o = new Order(tr, m);
        tu.addOrderForDelivery(o);
        assertEquals("There should be one order added", 1, tu.getNumOrders());
    }

    public void testMultipleOrdersAdded(){
        Order o = new Order(tr, m);
        Order oi = new Order(tr, m);
        tu.addOrderForDelivery(o);
        tu.addOrderForDelivery(oi);
        assertEquals("There should be 2 orders added", 2, tu.getNumOrders());
    }

    public void testRemoveOrders(){
        Order o = new Order(tr, m);
        tu.addOrderForDelivery(o);
        tu.removeOrder("");
        assertEquals("There should be no orders on the Truck", 0, tu.getNumOrders());
    }

    public void testStartDelivery(){
        Order o = new Order(tr, m);
        o.addItem(m.getUncookedItem("Salad"));
        o.addItem(m.getCookedItem("Small Pizza"));
        tu.addOrderForDelivery(o);
        tu.startDelivering();
        assertEquals("Truck should have 2 min. available time for the single order.",
                120, tu.getAvailTime());
    }


    @Before
    public void setUp() throws Exception {
        t = Time.createTime();
        tr = new TimeReader(t);
        tu = new Truck("Marty",tr);
        f = new FoodItem("Salad", 5.00, 5, tr);
        c = new CookedItem("Small Pizza", 8.00, 8,13,1,tr);
        m = new PizzaMenu();
        m.addCookedMenuItem("#Small Pizza", c);
        m.addMenuItem("#Salad", f);

    }
}
