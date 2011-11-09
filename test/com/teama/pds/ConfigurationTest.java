package com.teama.pds;

import junit.framework.TestCase;

import java.util.HashMap;


/**
 * User: Jason Greaves
 */
public class ConfigurationTest extends TestCase {

    /**
     * This is a quick test I made to make sure Configurations were passed to
	 * the menu correctly.
     */
    public void testConfig() {
		Time time = Time.createTime();
		TimeReader tr = new TimeReader(time);
        ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
        PizzaMenu menu = config.getMenu();
        ResourceList<Truck> trucks = config.getTrucks();
        ResourceList<Chef> cooks = config.getChefs();
        HashMap<String, Integer> l = config.getLocationMap();
        assertEquals("The name of the food item should be Salad", "Small Pizza",
				menu.getItemName("Small Pizza"));
        assertEquals("The item, a Salad, should cost $5.00", 5.00,
				menu.getItemCost("Salad"));
        assertEquals("The item, a Medium Pizza, should take up 2 spaces.",
                2, menu.getItemSpace("Medium Pizza"));
        assertEquals("The item, a Small Pizza, should have a 13 minute cook" +
				" time. (Given in seconds)",
                13*60, menu.getItemCook("Small Pizza"));
        assertEquals("The config file has 1 cooks", 1, cooks.getCount());
        int i = l.get("RIT");
        assertEquals("RIT is 18 miles away from the shop (Given in seconds)",
				18*60, i);
        assertEquals("The truck has 3 drivers in the Config file.", 3,
				trucks.getCount());

    }

    public void testConfigA(){
        Time t = Time.createTime();
        TimeReader tr = new TimeReader(t);
        ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
		assertEquals("Should be 24 oven spaces", 24,
				config.getOvens().grabAvailable().getOvenSpace());
        assertEquals("Should be 3 trucks", 3, config.getTrucks().getCount());
        assertEquals("Should be 2 trucks", 2, config.getChefs().getCount());
    }

    public void testConfigB(){
        Time t = Time.createTime();
        TimeReader tr = new TimeReader(t);
	}

    public void testConfigC(){
        Time t = Time.createTime();
        TimeReader tr = new TimeReader(t);
        ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
	}

    public void testConfigD(){
        Time t = Time.createTime();
        TimeReader tr = new TimeReader(t);
        ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
	}

    public void testMissingOrMisspelledConfig(){
		Time t = Time.createTime();
		TimeReader tr = new TimeReader(t);
		ConfigManager cfgm = new ConfigManager(tr);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
	}

		public void testJSONWriting(){
		TimeReader tr = new TimeReader(Time.createTime());
		ConfigManager configManager = new ConfigManager(tr);

		//Create Truck List
		ResourceList<Truck> truckResourceList = new ResourceList<Truck>();
		truckResourceList.addResource(new Truck("Bob", tr));
		truckResourceList.addResource(new Truck("Steve", tr));
		truckResourceList.addResource(new Truck("Joe", tr));

		PeekResourceList<Chef> chefPeekResourceList = new PeekResourceList<Chef>();
		chefPeekResourceList.addResource(new Chef(tr));
		chefPeekResourceList.addResource(new Chef(tr));
		chefPeekResourceList.addResource(new Chef(tr));

		PeekResourceList<Oven> ovenPeekResourceList = new PeekResourceList<Oven>();
		ovenPeekResourceList.addResource(new Oven(24, tr));

		HashMap<String, Integer> location_map = new HashMap<String, Integer>();
		location_map.put("RIT", 18);
		location_map.put("University of Rochester", 12);
		location_map.put("St. John Fisher", 21);
		location_map.put("Roberts Wesleyan College", 25);
		location_map.put("Monroe Community College", 18);

		PizzaMenu menu = new PizzaMenu();
		menu.addMenuItem("Salad", new FoodItem("Salad", 8.00, 5, tr));
		menu.addCookedMenuItem("Pizza Logs", new CookedItem("Pizza Logs", 6.00, 0, 1, 10, tr));
		menu.addCookedMenuItem("Small Pizza", new PizzaItem("Small Pizza", 1, tr, new CookedItem("Small Pizza", 8.00, 8, 1, 13, tr)));
		menu.addCookedMenuItem("Medium Pizza", new PizzaItem("Medium Pizza", 1, tr, new CookedItem("Medium Pizza", 11.00, 10, 2, 15, tr)));
		menu.addCookedMenuItem("Large Pizza", new PizzaItem("Large Pizza", 1, tr, new CookedItem("Large Pizza", 16.00, 15, 4, 20, tr)));

		StoreConfiguration storeConfiguration = new StoreConfiguration(tr);
		storeConfiguration.setChef_list(chefPeekResourceList);
		storeConfiguration.setOvens(ovenPeekResourceList);
		storeConfiguration.setTruck_list(truckResourceList);
		storeConfiguration.setLocationMap(location_map);
		storeConfiguration.setMenu(menu);

		storeConfiguration.build();
		configManager.setStoreConfiguration(storeConfiguration);
		configManager.writeConfig("conf/configTestA.json", configManager.getStoreConfiguration());
	}

	public void testJSONReading(){
		TimeReader tr = new TimeReader(Time.createTime());
		ConfigManager configManager = new ConfigManager(tr);

		StoreConfiguration sc = null;
		sc = configManager.readConfig("conf/configTestA.json");
		sc.rebuild(tr);
		System.out.println(sc.getTrucks().getCount());

		PizzaMenu menu = sc.getMenu();
		System.out.println(menu.getMenu());
		//System.out.println(menu.getCookedItem("Pizza Logs"));
	}
}
