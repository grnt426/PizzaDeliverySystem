package com.teama.pds;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author: Jason Greaves
 * <p/>
 * Creates a menu that takes in FoodItems and CookedItems that are gettable.
 */
public class PizzaMenu {

	/**
	 * Hash map of menu key and food item that represents the key
	 */
	private final HashMap<String, FoodItem> menu;

	/**
	 * list of keys for when Printing Menu to console
	 */
	private final ArrayList<String> keys = new ArrayList<String>();

	/**
	 * Constructor
	 * <p/>
	 * Creates a hash map for the menu
	 */
	public PizzaMenu() {
		menu = new HashMap<String, FoodItem>();
	}

	/**
	 * Adds a noncooked item to the menu
	 *
	 * @param key  noncooked item's key (keys should start with a #)
	 * @param item food item to be stored in the menu
	 */
	public void addMenuItem(String key, FoodItem item) {
		menu.put(key, item);
		keys.add(key);
	}

	/**
	 * Adds a cooked item to the menu
	 *
	 * @param key  cooked item's key (keys should start with a #)
	 * @param item food item to be stored in the menu
	 */
	public void addCookedMenuItem(String key, CookedItem item) {
		menu.put(key, item);
		keys.add(key);
	}

	/**
	 * remove an item from the menu
	 *
	 * @param key key of the item to be removed
	 */
	public void removeMenuItem(String key) {
		menu.remove(key);
		keys.remove(key);
	}

	/**
	 * Prints out the menu.
	 */
	void printMenu() {
		ArrayList<String> products = new ArrayList<String>();
		for (String key : keys) {
			FoodItem item = menu.get(key);
			String product = item.getName() + ": $" + item.getCost();
			products.add(product);
		}
		System.out.println(products.toString());
	}

	/**
	 * Gets the name of an item in the menu
	 *
	 * @param item The name of the item. Does NOT start with a #.
	 * @return name	 The stripped Name of the item
	 */
	public String getItemName(String item) {
		String key = "#" + item;
		if (!(keys.contains(key))) {
			printMenu();
			return null;
		}
		return menu.get(key).getName();
	}

	/**
	 * Gets the cost of the item
	 *
	 * @param item Name of the item. Does NOT start with a #.
	 * @return cost	 The associated cost of the item
	 */
	public double getItemCost(String item) {
		String key = "#" + item;
		if (!(keys.contains(key))) {
			printMenu();
			return 0.0;
		}
		return menu.get(key).getCost();

	}

	/**
	 * Gets the time it takes to prepare an item
	 *
	 * @param item Name of the item. Does NOT start with a #.
	 * @return Prep	 The associated prep time of the item
	 */
	public int getItemPrep(String item) {
		String key = "#" + item;
		if (!(keys.contains(key))) {
			printMenu();
			return 0;
		}
		return menu.get(key).getPrepTime();
	}

	/**
	 * Makes sure item is a cookable then gets the amount of space it takes in
	 * the oven
	 *
	 * @param item The name of the item. Does NOT start with a #.
	 * @return 0 for an uncookable item. Otherwise it returns the
	 *         amount of space the item takes in the oven
	 */
	public int getItemSpace(String item) {
		String key = "#" + item;
		if ((!(keys.contains(key))) ||
				!CookedItem.class.isInstance(menu.get(key))) {
			if (!(keys.contains(key))) {
				printMenu();
			}
			return 0;
		}
		CookedItem ci = (CookedItem) menu.get(key);

		return ci.getSpace();

	}

	/**
	 * Makes sure item is a cookable item then gets the amount of time it takes
	 * in the oven
	 *
	 * @param item The name of the item. Does NOT start with a #.
	 * @return 0 for an uncookable item. Otherwise it returns the amount of
	 *         time the item takes in the oven.
	 */
	public int getItemCook(String item) {
		String key = "#" + item;
		if ((!(keys.contains(key))) ||
				!CookedItem.class.isInstance(menu.get(key))) {
			if (!(keys.contains(key))) {
				printMenu();
			}
			return 0;
		}
		CookedItem ci = (CookedItem) menu.get(key);
		return ci.getCookTime();

	}

	public CookedItem getCookedItem(String item) {
		CookedItem food = null;
		if ((!(keys.contains(item)))) {
			//printMenu();
			System.out.println("Attempted getting cooked item for nonexistant food!");
		} else {
			if (!CookedItem.class.isInstance(menu.get(item))) {
				System.out.println("That's not cookable.");
			} else {
				food = (CookedItem) menu.get(item).clone();
			}
		}
		return food;
	}

	public FoodItem getUncookedItem(String item) {
		FoodItem food = null;
		if ((!(keys.contains(item)))) {
			printMenu();
		} else {
			if (!CookedItem.class.isInstance(menu.get(item))) {
				food = menu.get(item).clone();
			} else {
				System.out.println("That's not cookable.");
			}
		}
		return food;
	}


	public HashMap<String, FoodItem> getMenu() {
		return menu;
	}
}
