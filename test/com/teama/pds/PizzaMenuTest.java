package com.teama.pds;

import junit.framework.TestCase;

/**
* Author: Jason Greaves
*/
public class PizzaMenuTest extends TestCase{
    private Time t;
    private TimeReader time;
    private StoreConfiguration config;
    private PizzaMenu menu;

    public void setUp() throws Exception {
        t = Time.createTime();
        time = new TimeReader(t);
        ConfigManager cfgm = new ConfigManager(time);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
        menu = config.getMenu();
    }


    public void testAddMenuItem() throws Exception {
        PizzaMenu m = new PizzaMenu();
        FoodItem f = new FoodItem("Salad", 5.00, 5, time);
        m.addMenuItem("#Salad", f);
        assertEquals("Salad", m.getItemName("Salad"));

    }


    public void testAddCookedMenuItem() throws Exception {
        PizzaMenu m = new PizzaMenu();
        CookedItem f = new CookedItem("Pizza Logs", 6.00, 0, 1, 10,  time);
        m.addMenuItem("#Pizza Logs", f);
        assertEquals("Pizza Logs", m.getItemName("Pizza Logs"));
    }


    public void testRemoveMenuItem() throws Exception {
        PizzaMenu m = new PizzaMenu();
        CookedItem f = new CookedItem("Pizza Logs", 6.00, 0, 1, 10,  time);
        m.addMenuItem("#Pizza Logs", f);
        m.removeMenuItem("#Pizza Logs");
        assertEquals(null, m.getItemName("Pizza Logs"));
    }



    public void testGetCookedItem() throws Exception {
        assertTrue("The item found in the menu should be a CookedItem",
                CookedItem.class.isInstance(menu.getCookedItem("Pizza Logs")));

    }


    public void testGetUncookedItem() throws Exception {
        assertTrue("The item found in the menu should be a FoodItem",
                FoodItem.class.isInstance(menu.getUncookedItem("Salad")));
    }

    public void testGetters(){
        PizzaMenu m = new PizzaMenu();
        CookedItem f = new CookedItem("Pizza Logs", 6.00, 0, 1, 10, time);
        m.addCookedMenuItem("#Pizza Logs", f);
        assertEquals("Item name should return Pizza Logs", "Pizza Logs",
                m.getCookedItem("Pizza Logs").getName());
        assertEquals("Item cost should return 6.00", 6.00,
                m.getCookedItem("Pizza Logs").getCost());
        assertEquals("Item prep time should return 0", 0,
                m.getCookedItem("Pizza Logs").getPrepTime());
        assertEquals("Item oven space should return 1", 1,
                m.getCookedItem("Pizza Logs").getSpace());
        assertEquals("Item cook time should return 10", 10,
                m.getCookedItem("Pizza Logs").getCookTime());
    }
}
