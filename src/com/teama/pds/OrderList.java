package com.teama.pds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author:      Grant Kurtz
 *
 * A General purpose Entity class for storing an arbitrary number of Orders,
 * with some convenience functions for computing useful statistics and sorting.
 */
public class OrderList {

	/**
	 * The backing data structure that this class will use for storing Orders.
	 */
	private final List<Order> orders;

	/**
	 * Default Constructor.
	 * Just Initializes an empty ArrayList for use in storing Orders.
	 */
	public OrderList(){
		orders = new ArrayList<Order>();
	}

	/**
	 * Adds a single order to this OrderList.
	 * @param order	The Order to be added.
	 */
	public void addOrder(Order order){
		orders.add(order);
	}

	/**
	 * Adds a number of Orders to this OrderList at one time.
	 * @param orders	The List of Orders to be added.
	 */
	public void addOrders(List<Order> orders){
		orders.addAll(orders);
	}

	/**
	 * In-place sorts the orders List by descending Order cost.
	 */
	public void sortByCost(){
		Collections.sort(orders, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return (int) (o1.getTotalCost() - o2.getTotalCost());
			}
		});
	}

	/**
	 * In-place sorts the orders List by descending customer ID (currently
	 * phone number).
	 */
	public void sortByCustomerID(){
		Collections.sort(orders, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return o1.getCustomer().getPhoneNumber().compareTo(
						o2.getCustomer().getPhoneNumber());
			}
		});
	}

	/**
	 * In-place reversal of this List.
	 */
	public void reverseList(){
		Collections.reverse(orders);
	}

	/**
	 * Computes the total cost of all Orders that were never canceled.
	 * @return	The cost of all non-canceled orders.
	 */
	public double getTotalTendered(){
		double total_tendered = 0.0;
		for(Order order : orders){
			if(!order.isOrderCanceled()){
				total_tendered += order.getTotalCost();
			}
		}
		return total_tendered;
	}

	/**
	 * The average cost of non-canceled Orders.
	 * @return	The average cost of non-canceled Orders.
	 */
	public double getAverageOrderCost(){
		return getTotalNonCanceledOrders() == 0 ? 0
				: getTotalTendered() / getTotalNonCanceledOrders();
	}

	public double getAverageTotalTimeSpent(){
		int total_time_total = 0;
		for(Order order : orders) {
			if (order.completed()) {
				total_time_total += order.getTotalTimeSpentOnOrder();
			}
		}

		return getTotalNonCanceledCompletedOrders() == 0 ? 0
				: total_time_total / getTotalNonCanceledCompletedOrders();
	}

	private int getTotalNonCanceledCompletedOrders() {
		int total_completed_orders = 0;
		for(Order order : orders){
			if(order.completed()){
				total_completed_orders++;
			}
		}
		return total_completed_orders;
	}

	public double getAverageTotalCreationTime(){
		int total_time_total = 0;
		for(Order order : orders)
			total_time_total += order.getTotalOrderCreationTime();

		return getTotalNonCanceledOrders() == 0 ? 0
				: total_time_total / getTotalNonCanceledOrders();
	}

	public double getAverageTotalDeliveryTime(){
		int total_time_total = 0;
		for(Order order : orders)
			total_time_total += order.getTotalDeliveryTime();

		return getTotalNonCanceledOrders() == 0 ? 0
				: total_time_total / getTotalNonCanceledOrders();
	}

	/**
	 * The total number of orders processed by this List.
	 * @return
	 */
	public int getTotalOrders(){
		return orders.size();
	}

	/**
	 * The total number of Orders that weren't canceled, or have yet to be
	 * canceled.
	 * @return		The number of non-canceled Orders stored in this List.
	 */
	public int getTotalNonCanceledOrders(){
		int total = 0;
		for(Order order : orders){
			if(!order.isOrderCanceled()){
				total++;
			}
		}
		return total;
	}

	/**
	 * Gathers a List of all Orders as made by the given Customer.
	 * @param customer	The Customer to check for Orders placed by.
	 * @return			A List of Orders that the Customer placed.
	 */
	public List<Order> getOrdersFromCustomer(Customer customer){
		List<Order> matched_orders = new ArrayList<Order>();
		for(Order order : orders){
			if(order.getCustomer() == customer){
				matched_orders.add(order);
			}
		}
		return matched_orders;
	}

	/**
	 * The List structure this class instance is backed by, containing all the
	 * Orders that were passed to this class.
	 * @return	The List of all Orders contained in this OrderList instance.
	 */
	public List<Order> getOrders() {
		return orders;
	}
}
