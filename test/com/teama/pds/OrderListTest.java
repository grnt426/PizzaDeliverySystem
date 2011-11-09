package com.teama.pds;

import junit.framework.TestCase;

import java.util.List;

/**
 * Author:      Grant Kurtz
 */
public class OrderListTest extends TestCase {

	private Customer customerOne;
	private Customer customerTwo;
	TimeReader time;
	PizzaMenu pm;

	public void setUp() throws Exception {

		Time time = Time.createTime();
		this.time = new TimeReader(time);
		pm = new PizzaMenu();

		// let's get some test customers
		customerOne = new Customer();
		customerOne.setPhoneNumber("1234567890");
		customerOne.setName("Mr. Herp Jr. III & Mrs. Derp");
		customerTwo = new Customer();
		customerTwo.setPhoneNumber("0987654321");
		customerTwo.setName("Professor Doom, Squire");
		customerTwo.setLocation("University of Rochester");
	}

	public void testSortByCost() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order1.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order_list.addOrder(order2);
		order_list.sortByCost();
		List<Order> sorted_orders = order_list.getOrders();
		assertEquals("The first element should be the Order with the least" +
				" cost of 15.00", order2, sorted_orders.get(0));
	}

	public void testSortByCustomerID() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.setCustomer(customerOne);
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.setCustomer(customerTwo);
		order_list.addOrder(order2);
		order_list.sortByCustomerID();
		List<Order> sorted_orders = order_list.getOrders();
		assertEquals("The first element should be the Order with the lowest" +
				" phone number.", order2, sorted_orders.get(0));
	}

	public void testReverseList() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order1.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order_list.addOrder(order2);
		order_list.sortByCost();
		order_list.reverseList();
		List<Order> sorted_orders = order_list.getOrders();
		assertEquals("The first element should be the Order with the highest" +
				" cost of 45.00", order1, sorted_orders.get(0));
	}

	public void testGetTotalTendered() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order1.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order1);
		assertEquals("The total Tendered should be 45.00", 45.00,
				order_list.getTotalTendered());
	}

	public void testTotalTenderedWithCanceledItem() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order1.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order1);
		order1.cancelOrder();
		assertEquals("The total Tendered should be 0.0", 0.0,
				order_list.getTotalTendered());
	}

	public void testTotalTenderedWithMixedOrders() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order1.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order2.addItem(new FoodItem("sdfdf", 30.0, 0, time));
		order_list.addOrder(order2);
		order2.cancelOrder();
		assertEquals("The total Tendered should be 45.0", 45.0,
				order_list.getTotalTendered());
	}

	public void testGetAverageOrderCost() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.addItem(new FoodItem("sdfsdf", 10.0, 0, time));
		order_list.addOrder(order2);
		assertEquals("The average should be 12.5", 12.5,
				order_list.getAverageOrderCost());
	}

	public void testGetAverageOrderCostWithCanceled() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.addItem(new FoodItem("dfsdf", 15.0, 0, time));
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.addItem(new FoodItem("sdfsdf", 10.0, 0, time));
		order_list.addOrder(order2);
		order2.cancelOrder();
		assertEquals("The average should be 15.0", 15.0,
				order_list.getAverageOrderCost());
	}

	public void testGetAverageWithNoElements() throws Exception {
		OrderList order_list = new OrderList();
		assertEquals("The average should be 0.0", 0.0,
				order_list.getAverageOrderCost());
	}

	public void testGetTotalNonCanceledOrders() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order_list.addOrder(order2);
		order2.cancelOrder();
		assertEquals("There should be only 1 non-canceled order.", 1,
				order_list.getTotalNonCanceledOrders());
	}

	public void testGetOrdersFromCustomer() throws Exception {
		OrderList order_list = new OrderList();
		Order order1 = new Order(time, pm);
		order1.setCustomer(customerOne);
		order_list.addOrder(order1);
		Order order2 = new Order(time, pm);
		order2.setCustomer(customerTwo);
		order_list.addOrder(order2);
		List<Order> custs_orders = order_list.getOrdersFromCustomer(
				customerOne);
		assertEquals("There should only be one element.", 1,
				custs_orders.size());
		assertEquals("The only order returned should be of the one the" +
				"customer made.", customerOne,
				custs_orders.get(0).getCustomer());
	}
}
