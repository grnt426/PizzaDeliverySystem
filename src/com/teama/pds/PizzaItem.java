package com.teama.pds;

import java.util.ArrayList;

/**
 * Author:      Grant Kurtz, Jason Greaves
 * <p/>
 * Stores all information necessary to represent a pizza.
 */
public class PizzaItem extends CookedItem {

	/**
	 * Size of the pizza
	 */
	private Size size;
	/**
	 * List of Toppings
	 */
	private final ArrayList<Topping> toppings;
	/**
	 * cost of the topping
	 */
	private double topping_cost;
	/**
	 * amount of toppings on the pizza
	 */
	private double topping_amount;

	/**
	 * Pizza sizes
	 */
	public enum Size {
		SMALL,
		MEDIUM,
		LARGE
	}

	/**
	 * Valid toppings for the pizza
	 */
	public enum Topping {
		CHEESE,
		PEPPERONI,
		SAUSAGE,
		ONIONS,
		PEPPERS,
		MUSHROOMS
	}

	/**
	 * Creates the topping to go on the pizza. Pepperoni is an automatic topping
	 * and is free.
	 *
	 * @param pizza_size	 Size of the pizza
	 * @param topping_amount Amount of toppings to put on the pizza
	 * @param time		   The time class that will accurately report the
	 *                       current simulation time.
	 * @param item		   the CookedItem instance to get information from.
	 */
	public PizzaItem(String pizza_size, double topping_amount, TimeReader time,
					 CookedItem item) {

		// TODO base this value from the size
		super(item.getName(), item.getCost(), item.getPrepTime(),
				item.getSpace(), item.getCookTime(), time);
		if (pizza_size.equalsIgnoreCase("Small") || pizza_size.equalsIgnoreCase("Small Pizza")) {
			this.size = Size.SMALL;
		} else if (pizza_size.equalsIgnoreCase("Medium") ||
				pizza_size.equalsIgnoreCase("Medium Pizza")) {
			this.size = Size.MEDIUM;
		} else if (pizza_size.equalsIgnoreCase("Large") ||
				pizza_size.equalsIgnoreCase("Large Pizza")) {
			this.size = Size.LARGE;
		} else {
			System.out.println("Invalid pizza size.");
		}

		// Toppings can only be set on whole or half a pizza
		if (topping_amount != 0.5) {
			if (topping_amount != 1.0) {
				System.out.println("Invalid amount of toppings, you can have whole or half the pizza filled with toppings. Toppings reset to one.");
				topping_amount = 1.0;
			}
		}
		this.topping_amount = topping_amount;
		toppings = new ArrayList<Topping>();
	}

	/**
	 * @return A deep-copied clone of this instance of PizzaItem
	 */
	public PizzaItem clone() {
		super.clone();
		CookedItem ci = new CookedItem(getName(), getCost(), getPrepTime(),
				getSpace(), getCookTime(), this.time);
		return new PizzaItem(getSize().name(), this.topping_amount,
				this.time, ci);
	}

	/**
	 * @return The size of the pizza
	 */
	Size getSize() {
		return size;
	}

	/**
	 * @param size The size of the pizza
	 */
	public void setSize(Size size) {
		this.size = size;
	}

	/**
	 * @param topping_amount amount of toppings on the pizza. Can only be .5
	 *                       or 1.0. If anything else given, tells the user
	 *                       and auto sets the topping amount to 1.0.
	 */
	public void setToppingAmount(double topping_amount) {
		if (topping_amount != 0.5 && topping_amount != 1.0) {
			System.out.println("Invalid amount of toppings, you can have whole or half the pizza filled with toppings. Toppings reset to one.");
			topping_amount = 1.0;
		}
		this.topping_amount = topping_amount;
	}

	/**
	 * @param topping The topping to go on the pizza
	 */
	public void addTopping(Topping topping) {
		toppings.add(topping);
		setToppingCost();
		super.setCost(super.getCost() + topping_cost);
	}

	/**
	 * @param topping The topping to be removed from pizza
	 */
	public void removeTopping(Topping topping) {
		if (toppings.size() == 0) {
			System.out.println("There are no toppings on your pizza.");
		} else {
			toppings.remove(topping);
			setToppingCost();
		}

	}

	/**
	 * Calculates the cost of toppings for each size of pizza depending if there
	 * is more than one topping on the pizza or a pizza with a topping other
	 * than pepperoni is ordered.
	 *
	 * @return cost of the toppings on the pizza
	 */
	public double getToppingCost() {
//		if (this.size == Size.LARGE) {
//			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
//				topping_cost = 0.00;
//			} else {
//				topping_cost = (2.00 * this.topping_amount);
//			}
//		} else if (this.size == Size.MEDIUM) {
//			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
//				topping_cost = 0.00;
//			} else {
//				topping_cost = (1.50 * this.topping_amount);
//			}
//		} else if (this.size == Size.SMALL) {
//			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
//				topping_cost = 0.00;
//			} else {
//				topping_cost = (1.00 * this.topping_amount);
//			}
//		}
		return topping_cost;
	}

	public ArrayList<Topping> getToppings() {
		return toppings;
	}

	void setToppingCost() {
		if (this.size == Size.LARGE) {
			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
				topping_cost = 0.00;
			} else {
				topping_cost = (2.00 * this.topping_amount);
			}
		} else if (this.size == Size.MEDIUM) {
			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
				topping_cost = 0.00;
			} else {
				topping_cost = (1.50 * this.topping_amount);
			}
		} else if (this.size == Size.SMALL) {
			if ((toppings.size() == 2 && toppings.contains(Topping.PEPPERONI)) || toppings.size() <= 1) {
				topping_cost = 0.00;
			} else {
				topping_cost = (1.00 * this.topping_amount);
			}
		}
	}

	/**
	 * Converts the backing ArrayList into its String version for humans
	 * Robots, however, do not need this, as they are clearly superior to the fleshy meatbags that built them.
	 */
	public void printToppings() {
		System.out.println(toppings.toString());
	}

	/**
	 * @return A human readable string version of this order intance.
	 */
	public String toString() {
		String toReturn;
		String written_toppings = toppings.toString();
		written_toppings = written_toppings.substring(1,
				written_toppings.length() - 1);
		toReturn = getName();
		if (written_toppings.length() > 0) {
			toReturn = (toReturn + " with " + written_toppings);
		}
		return toReturn;
	}
}

