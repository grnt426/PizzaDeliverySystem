package com.teama.pds;

import com.teama.pds.*;
import junit.framework.TestCase;
import org.junit.Before;

/**
 * User: Jason Greaves
 */
public class PizzaItemTest extends TestCase {
    private CookedItem c;
    private PizzaItem d;
    private Time t;
    private TimeReader time;
    @Before
    public void setUp() throws Exception {
        t = Time.createTime() ;
        time = new TimeReader(t);
        c = new CookedItem("Small Pizza", 8.00, 5, 1, 10, time);
        d = new PizzaItem(c.getName(), 1.0, time, c);
    }

    public void testPepperoniCost(){
        assertEquals("A Pizza that has no toppings but pepperoni should have a cost of 0.0 for toppings.",
                0.0, d.getToppingCost());
    }

    public void testAddingTopping(){
        d.addTopping(PizzaItem.Topping.SAUSAGE);
        assertEquals("A Pizza with a topping added that's not pepperoni is $1.00",
                1.00, d.getToppingCost());
    }

    public void testAddThenRemove(){
        d.addTopping(PizzaItem.Topping.SAUSAGE);
        d.removeTopping(PizzaItem.Topping.SAUSAGE);
        assertEquals("No toppings on a pizza means no money in the cost for toppings.",
                0.0, d.getToppingCost());
    }

    public void testNoToppingsToRemove(){
        d.printToppings();
        d.removeTopping(PizzaItem.Topping.PEPPERONI);
        d.removeTopping(PizzaItem.Topping.SAUSAGE);
    }

    public void testMultipleItems(){
        d = new PizzaItem("Medium", 1.0, time, c);
        d.addTopping(PizzaItem.Topping.SAUSAGE);
        d.addTopping(PizzaItem.Topping.PEPPERS);
        assertEquals("A medium pizza's topping cost is $1.50.", 1.50, d.getToppingCost());
    }

     public void testDisplay(){
        d = new PizzaItem("Small Pizza", 1.0, time, c);
        assertEquals("Small Pizza with PEPPERONI, CHEESE", d.toString());
    }
}
