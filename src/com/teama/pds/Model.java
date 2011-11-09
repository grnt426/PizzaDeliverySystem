package com.teama.pds;
//this comment is a commit test

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * Author:      Grant Kurtz, Matthew Frey, Svyatoslav Sivov
 * <p/>
 * The Model controller maintains the entire state of the simulation. This model
 * is part of the MVC structure of this program.
 */
class Model {

	/**
	 * Maintains all Time based activities.
	 */
	private final Time time;

	/**
	 * Handles executing all ActionNodes.
	 */
	private Commander scu;

	/**
	 * Orders that have been created and need to be finished, or delivered.
	 */
	private final ResourceList<Order> waiting_orders;

	/**
	 * Food that still needs to be created before orders are available for
	 * delivery.
	 */
	private final ResourceList<FoodItem> food_to_make;

	/**
	 * Maintains the list of all addresses and customer information.
	 */
	private final AddressBook addresses;

	/**
	 * A Scheduler for keeping the simulation running.
	 */
	private Timer timer;

	/**
	 * True indicates the simulation is running, false means otherwise.
	 */
	private boolean simulation_status = false;

	/**
	 * The number of simulation seconds that should be processed for each real
	 * second.
	 */
	private double TICK_RATE_IN_SECONDS;

	/**
	 * Used for cloning food items for instance data.
	 */
	private final PizzaMenu menu;

	/**
	 * The logger through which all events are logged.
	 */
	private final History history;

	/**
	 * The authentication for determining access
	 */
	private static AuthTokenSingleton auth_token;

	/**
	 * Maintains information on all ResourceLists and PeekResourceLists as
	 * employed by the simulation.
	 */
	private final OverSeer over_seer;

	/**
	 * A wrapper class for OverSeer to modify the lists.
	 */
	private final OrderModifier order_modifier;


	private final ArrayList<User> user_list;

	private final OrderList order_history;

	private ConfigManager cfgm;

	private PeekResourceList<Chef> chef_list;

	private ArrayList<FoodItem> items;

	private ArrayList<Chef> chefs;

	private ArrayList<Oven> ovens_list;

	private ArrayList<Truck> trucks;

	/**
	 * Used to store orders that were read in-batch at program start-up.  Can
	 * also be used to insert orders with a delay while the simulation is
	 * running.
	 */
	private final ResourceList<Order> delayed_orders;

	/**
	 * Default Constructor
	 * <p/>
	 * Initializes the entire base portion of the program which will do the
	 * majority of the heavy lifting of the simulation.
	 */

	public Model() {

		// instantiate the lists
		waiting_orders = new ResourceList<Order>();
		food_to_make = new ResourceList<FoodItem>();
		delayed_orders = new ResourceList<Order>();
		user_list = new ArrayList<User>();
		items = new ArrayList<FoodItem>();
		chefs = new ArrayList<Chef>();
		ovens_list = new ArrayList<Oven>();
		trucks = new ArrayList<Truck>();

		// instantiate some needed classes
		addresses = new AddressBook();
		order_history = new OrderList();

		// instantiate some defaults
		TICK_RATE_IN_SECONDS = 1.0;

		// this is going to maintain all time-based activities
		time = Time.createTime();
		TimeReader time_reader = new TimeReader(time);
		history = new History();

		//Used to test if Authentication would go through, feel free to delete.
		User cashier = new User("cashier", "cash123", "Joshie",
				User.AuthLevel.CASHIER);
		User manager = new User("manager", "manage123", "Jeffy",
				User.AuthLevel.MANAGER);
		user_list.add(cashier);
		user_list.add(manager);

		// set up configuration
		cfgm = new ConfigManager(time_reader);
		cfgm.readConfig("conf/config.json");
		StoreConfiguration config = cfgm.getStoreConfiguration();
		menu = config.getMenu();

		// create and configure the commander
		try {
			scu = Commander.createCommander();
		} catch (AlreadyInstantiatedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Get the resource lists from the config scripts
		chef_list = config.getChefs();
		ResourceList<Truck> truck_list = config.getTrucks();
		PeekResourceList<Oven> ovens = config.getOvens();

		Iterator<Chef> c = chef_list.getResourceQueue().iterator();
		while (c.hasNext()) {
			chefs.add(c.next());
		}
//        Iterator<Oven> o = ovens.getResourceQueue().iterator();
//        while(o.hasNext()){
//            ovens_list.add(o.next());
//        }
		Iterator<Truck> t = truck_list.getResourceQueue().iterator();
		while (t.hasNext()) {
			trucks.add(t.next());
		}


		// create and configure the kitchen
		Kitchen kitchen = null;
		try {
			kitchen = Kitchen.createKitchen(time_reader, food_to_make,
					ovens, chef_list);
		} catch (AlreadyInstantiatedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		kitchen.setLogger(history);
		scu.addAction(kitchen);

		// create and configure Delivery
		HashMap<String, Integer> h = config.getLocationMap();
		ResourceList<Truck> del_out = new ResourceList<Truck>();
		Delivery delivery = null;
		try {
			delivery = Delivery.createDelivery(time_reader, h, truck_list,
					del_out, waiting_orders);
		} catch (AlreadyInstantiatedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		delivery.setLogger(history);
		scu.addAction(delivery);

		// create and configure Location
		Location location = null;
		try {
			location = Location.createLocation(time_reader, h, del_out,
					truck_list);
		} catch (AlreadyInstantiatedException e) {
			e.printStackTrace();
			System.exit(1);
		}
		location.setLogger(history);
		scu.addAction(location);

		// now that we have created all the ActionNodes and their respective
		// ResourceLists, create the OverSeer and OrderModifier objects
		over_seer = new OverSeer(waiting_orders, food_to_make, ovens,
				chef_list, del_out, truck_list,
				location.getDeliveringVehicles());
		order_modifier = new OrderModifier(over_seer, time_reader);

		//default the authentication to null
		auth_token = null;

		// default the simulation to running
		simulation_status = true;
		startSimulation();

		/*add a new temp order ~matt
				PizzaMenu pizzaMenu = new PizzaMenu();
				pizzaMenu.addMenuItem("#Pizza Logs", new FoodItem("Pizza Logs",20.0, 1000, time_reader));
				addOrder(new Order(time_reader, pizzaMenu));
				*/
	}

	/**
	 * Adds an new (completed) order to the queue for creation.
	 *
	 * @param order The completed Order to add
	 */
	public void addOrder(Order order) {
		Record r = new Record("Added Order #" + order.getCustomer().getName(),
				Record.Status.TRANSITION, time.getCurTime());
		history.addEvent(r);
		order.markOrderStarted();
		order_history.addOrder(order);
		waiting_orders.addResource(order);
		food_to_make.addResources(order.getItems());
	}

	/**
	 * Starts the whole simulation.  Note, once this is called, orders will
	 * immediately start to be processed, resources consumed, and finished
	 * resources produced from/into their respective queues.
	 */
	public void startSimulation() {
		simulation_status = true;

		// create the actionListener that will hold our commands
		ActionListener runTask = new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// increment the time
				time.incrementTime();

				// Grab any delayed orders that need to be inserted.
				while (delayed_orders.anyAvailable())
					addOrder(delayed_orders.grabAvailable());

				// process everything
				scu.run();
			}
		};
		// configure timer with converted time rate, timer starts
		timer = new Timer((int) (TICK_RATE_IN_SECONDS * 1000), runTask);
		timer.start();
	}

	/**
	 * Halts the simulation, and forces the timer to discontinue looping.
	 */
	public void endSimulation() {
		simulation_status = false;
		timer.stop();
	}

	/**
	 * @return True if the simulation is running, otherwise false
	 */
	public boolean getSimulationStatus() {
		return simulation_status;
	}

	/**
	 * @return returns the current tick-rate of the internal simulation time
	 *         counter, in seconds.
	 */
	public double getTickRate() {
		return TICK_RATE_IN_SECONDS;
	}

	/**
	 * @return Returns a list of all orders that still need to be delivered.
	 */
	public ResourceList<Order> getOrders() {
		return waiting_orders;
	}

	/**
	 * @return Returns a list of all orders ever.
	 */
	public OrderList getAllOrders() {
		return order_history;
	}

	/**
	 * @param TICK_RATE_IN_SECONDS The new rate of the internal counter for the
	 *                             simulation speed.
	 */
	void setTickRate(double TICK_RATE_IN_SECONDS) {
		this.TICK_RATE_IN_SECONDS = TICK_RATE_IN_SECONDS;
	}

	/**
	 * @return The time object that can be used to manipulate the time of the
	 * simulation.
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * @return Returns an object that can be used for cloning FoodItems for use
	 * in the simulation.
	 */
	public PizzaMenu getMenu() {
		return menu;
	}

	/**
	 * @return Returns the history of the simulation so the controller can add to it.
	 */
	public History getHistory() {
		return history;
	}

	/**
	 * @return Returns the Address Book of the simulation so the controller can add to it.
	 */
	public AddressBook getAddresses() {
		return addresses;
	}

	public void cancelOrder(Order o) {
		endSimulation();
		try {
			Thread.sleep((long) (TICK_RATE_IN_SECONDS * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		order_modifier.cancelOrder(o);

		Record r = new Record("Canceled Order #" + o.getCustomer().getName(),
				Record.Status.WORKING, time.getCurTime());
		history.addEvent(r);

		startSimulation();
	}

	/**
	 * Sets the AuthToken.
	 *
	 * @param username   User's username to log in
	 * @param password   User's password to log in
	 * @param auth_level The authentication level of the user (Can be Cashier or Manager now)
	 */
	public void setAuthToken(String username, char[] password,
							 User.AuthLevel auth_level) {
		auth_token = AuthTokenSingleton.getSingletonObject(username, password, auth_level);
	}

	/**
	 * gets the list of users
	 *
	 * @return
	 */
	public ArrayList<User> getUserList() {
		return user_list;
	}

	/**
	 * @return the authentication token
	 */
	public AuthTokenSingleton getToken() {
		return auth_token;
	}

	/**
	 * logs out by killing authToken
	 */
	public void logout() {
		auth_token.logout();
	}

	public Vector<Record> getHistoryArray() {
		return history.getRecordArray();
	}

	public OrderList getOrderHistory() {
		return order_history;
	}

	public OverSeer getOverSeer() {
		return over_seer;
	}

	public ConfigManager getConfigManager() {
		return cfgm;
	}

	/**
	 * @return array list of chefs
	 */
	public ArrayList<Chef> getChefs() {
		return chefs;
	}

	/**
	 * @return array list of trucks
	 */
	public ArrayList<Truck> getTrucks() {
		return trucks;
	}

	public void deleteUser(User selectedUser) {
		user_list.remove(selectedUser);
	}

	public void deleteUser(String username) {
		for(User user : user_list){
			if(user.getUsername().equals(username)) {
				user_list.remove(user);
				return;
			}
		}
	}

	public void addUser(User user) {
		user_list.add(user);
	}

	public User getLoggedInUser(){
		for(User user : user_list){
			if(user.getUsername().equals(auth_token.getUsername()))
				return user;
		}
		return null;
	}

    public void addItemToMake(FoodItem f){
        food_to_make.addResource(f);
    }
}

