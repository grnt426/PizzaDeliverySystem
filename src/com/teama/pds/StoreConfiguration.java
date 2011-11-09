package com.teama.pds;

import java.util.*;


/**
 * User: Ryan Brown
 * <p/>
 * Sets the configuration from a .properties file to use for our pds.
 */
public class StoreConfiguration {

	/**
	 * creates a menu that will take in food items from the configuration file.
	 */
	private Vector<Truck> truck_vector;
	private Vector<Chef> chef_vector;
	private Vector<Oven> oven_vector;
	private HashMap<String, PizzaItem> pizza_hashmap;
	private HashMap<String, CookedItem> cooked_hashmap;
	private HashMap<String, FoodItem> uncooked_hashmap;
	private transient PizzaMenu menu;
	private HashMap<String, Integer> locationMap;
	private transient ResourceList<Truck> truck_list = new ResourceList<Truck>();
	private transient PeekResourceList<Chef> chef_list;
	private transient PeekResourceList<Oven> ovens;
	private transient TimeReader time;

	public StoreConfiguration(PizzaMenu menu, Location location,
							  PeekResourceList<Chef> chef_list, PeekResourceList<Oven> ovens) {
		this.menu = menu;
		this.locationMap = location.getHashmap();
		this.chef_list = chef_list;
		this.ovens = ovens;
		PriorityQueue<Truck> tmp = new PriorityQueue<Truck>();
		//put all the trucks in a single list
		tmp.addAll(location.getInboundVehicles().getResourceQueue());
		tmp.addAll(location.getOutboundVehicles().getResourceQueue());
		tmp.addAll(location.getDeliveringVehicles().getResourceQueue());
		truck_list = new ResourceList<Truck>(tmp);
	}

	public StoreConfiguration(TimeReader timeReader) {
		this.time = timeReader;
	}

	public void build() {
		truck_vector = new Vector<Truck>();
		chef_vector = new Vector<Chef>();
		oven_vector = new Vector<Oven>();
		pizza_hashmap = new HashMap<String, PizzaItem>();
		cooked_hashmap = new HashMap<String, CookedItem>();
		uncooked_hashmap = new HashMap<String, FoodItem>();

		Iterator<Truck> i = truck_list.getResourceQueue().iterator();
		while (i.hasNext()) {
			truck_vector.add(i.next());
		}

		oven_vector = new Vector<Oven>();
		Iterator<Oven> o = ovens.getResourceQueue().iterator();
		while (o.hasNext()) {
			oven_vector.add(o.next());
		}

		chef_vector = new Vector<Chef>();
		Iterator<Chef> c = chef_list.getResourceQueue().iterator();
		while (c.hasNext()) {
			chef_vector.add(c.next());
		}

		HashMap<String, FoodItem> hm = menu.getMenu();

		for (Map.Entry<String, FoodItem> f : hm.entrySet()) {
			if (PizzaItem.class.isInstance(f.getValue())) {
				pizza_hashmap.put(f.getKey(), (PizzaItem) f.getValue());
			}
			if (CookedItem.class.isInstance(f.getValue())) {
				cooked_hashmap.put(f.getKey(), (CookedItem) f.getValue());
			} else {
				uncooked_hashmap.put(f.getKey(), f.getValue());
			}
		}
	}

	public void rebuild(TimeReader tr) {
		truck_list = new ResourceList<Truck>();
		chef_list = new PeekResourceList<Chef>();
		ovens = new PeekResourceList<Oven>();
		time = null;
		menu = new PizzaMenu();
		for (Truck t : truck_vector) {
			t.setTimeReader(tr);
			truck_list.addResource(t);
		}
		for (Oven o : oven_vector) {
			o.setTimeReader(tr);
			ovens.addResource(o);
		}
		for (Chef c : chef_vector) {
			c.setTimeReader(tr);
			chef_list.addResource(c);
		}
		for (Map.Entry<String, FoodItem> f : uncooked_hashmap.entrySet()) {
			FoodItem foodItem = f.getValue();
			foodItem.setTimeReader(tr);
			menu.addMenuItem(f.getKey(), foodItem);
		}
		for (Map.Entry<String, CookedItem> f : cooked_hashmap.entrySet()) {
			CookedItem foodItem = f.getValue();
			foodItem.setTimeReader(tr);
			menu.addCookedMenuItem(f.getKey(), foodItem);
		}
		for (Map.Entry<String, PizzaItem> f : pizza_hashmap.entrySet()) {
			PizzaItem foodItem = f.getValue();
			foodItem.setTimeReader(tr);
			menu.addCookedMenuItem(f.getKey(), foodItem);
		}
	}

	public void setMenu(PizzaMenu menu) {
		this.menu = menu;
	}

	public void setLocationMap(HashMap<String, Integer> locationMap) {
		this.locationMap = locationMap;
	}

	public void setTruck_list(ResourceList<Truck> truck_list) {
		this.truck_list = truck_list;
	}

	public void setChef_list(PeekResourceList<Chef> chef_list) {
		this.chef_list = chef_list;
	}

	public void setOvens(PeekResourceList<Oven> ovens) {
		this.ovens = ovens;
	}

	/**
	 * gets the menu.
	 *
	 * @return menu
	 */
	public PizzaMenu getMenu() {
		return menu;
	}

	public ResourceList<Truck> getTrucks() {
		return truck_list;
	}

	public PeekResourceList<Chef> getChefs() {
		return chef_list;
	}

	public PeekResourceList<Oven> getOvens() {
		return ovens;
	}

	public HashMap<String, Integer> getLocationMap() {
		return locationMap;
	}

}
