package com.teama.pds;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Matthew Frey, Jason
 *         This panel displays the restaurant simulation
 */
public class SimulationPanel extends JPanel {


	private Controller controller;
	private ActionListener updater;
	private JPanel prep_panel;
	private JPanel oven_panel;
	private JPanel delivery_panel;
	private JPanel display_panel;
	private Timer timer;
	private boolean refresherRunning = false;
	private JList prep_list;
	private JList oven_list;
	private JList delivery_list;
	private OrderList orders;
	private ArrayList<Order> order_list;
	private ArrayList<FoodItem> items;
	private String order_id;
	private String item_name;
	private Vector<String> prep_label_list;
	private Vector<String> oven_label_list;
	private Vector<String> delivery_label_list;
	private JScrollPane prep_scroll;
	private JScrollPane oven_scroll;
	private JScrollPane deliver_scroll;
	private JMenuBar menuBar;


	/**
	 * Construct the SimulationPanel
	 */
	public SimulationPanel(Controller controller) {
		this.controller = controller;
		buildPanel();

	}

	private void buildPanel() {
		//initialize variables
		orders = controller.getOrderData();
		order_list = new ArrayList<Order>();
		items = new ArrayList<FoodItem>();
		prep_label_list = new Vector<String>();
		oven_label_list = new Vector<String>();
		delivery_label_list = new Vector<String>();
		menuBar = new JMenuBar();

		//get screen size
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenDims.width - 200;
		int height = screenDims.height - 200;


		updater = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListings();
			}
		};


		//3 panels in simulation
		int panel_width = (int) width / 3;
		prep_panel = new JPanel();
		oven_panel = new JPanel();
		delivery_panel = new JPanel();

		//preparation panel scroll
		Border lowered_bevel = BorderFactory.createLoweredBevelBorder();
		prep_scroll = new JScrollPane(prep_panel);
		prep_scroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Preparation"), lowered_bevel));

		//oven panel scroll
		oven_scroll = new JScrollPane(oven_panel);
		oven_scroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Oven"), lowered_bevel));

		//delivery panel scroll
		deliver_scroll = new JScrollPane(delivery_panel);
		deliver_scroll.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Delivery"), lowered_bevel));

		//set up scroll sizes
		prep_scroll.setPreferredSize(new Dimension(panel_width - 65, height - 37));
		prep_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		oven_scroll.setPreferredSize(new Dimension(panel_width - 65, height - 37));
		oven_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		deliver_scroll.setPreferredSize(new Dimension(panel_width - 65, height - 37));
		deliver_scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//panel containing the 3 scrolls
		display_panel = new JPanel();
		display_panel.setLayout(new GridLayout(1, 3));

		//Set layout
		this.setLayout(new BorderLayout());

		//place everything into this panel
		display_panel.add(prep_scroll);
		display_panel.add(oven_scroll);
		display_panel.add(deliver_scroll);
		this.add(display_panel, BorderLayout.CENTER);

		//hide everything
		this.setVisible(false);
		//refresher should start when created.
		startRefresher();
	}

	/**
	 * the method constantly updates the panels with new information about the status
	 * of preparation, cooking and delivery.
	 */
	public void updateListings() {
		//Save all the scroll bar positions
		int h1, y1, h2, y2, h3, y3;
		h1 = prep_scroll.getHorizontalScrollBar().getValue();
		y1 = prep_scroll.getVerticalScrollBar().getValue();
		h2 = oven_scroll.getHorizontalScrollBar().getValue();
		y2 = oven_scroll.getVerticalScrollBar().getValue();
		h3 = deliver_scroll.getHorizontalScrollBar().getValue();
		y3 = deliver_scroll.getVerticalScrollBar().getValue();

		DefaultListModel prep_list_model = new DefaultListModel();
		DefaultListModel oven_list_model = new DefaultListModel();
		DefaultListModel delivery_list_model = new DefaultListModel();
		FoodItem item = null;
		CookedItem cooked_item = null;

		for (Chef chef : controller.getChefs()) {
			if (chef.getFood() != null) {
				if (chef.getFood() instanceof CookedItem) {
					cooked_item = (CookedItem) chef.getFood();
				} else {
					item = chef.getFood();
				}

				if (chef.getPrepConfirm() == false) {
					chef.setPrepConfirm();
					String label = new String(chef.getFood().getName() + " will be finished" +
							" at " + chef.getFood().getAvailTime());
					prep_label_list.add(label);
				}
			}
			if (chef.getIfItemIsInOven()) {
				cooked_item = chef.getFoodSent();
				chef.setItemInOven();
				String label2 = new String(cooked_item.getName() + " will finish cooking at " +
						cooked_item.getAvailTime());
				oven_label_list.add(label2);
			}
		}
		for (int i = 0; i < prep_label_list.size(); i++) {
			prep_list_model.add(i, prep_label_list.get(i));
		}
		for (int i = 0; i < oven_label_list.size(); i++) {
			oven_list_model.add(i, oven_label_list.get(i));
		}
		prep_list = new JList(prep_list_model);
		prep_scroll.setViewportView(prep_list);
		oven_list = new JList(oven_list_model);
		oven_scroll.setViewportView(oven_list);

		for (Truck truck : controller.getTrucks()) {
			if (truck.isOnLocation()) {
				for (Order o : truck.getDriverOrders()) {
					String label3 = new String("Driver " + truck.getDriverName() + " will arrive at " +
							o.getLocation() + " at " + truck.getAvailTime());
					delivery_label_list.add(label3);
					truck.setOnLocation();
				}
			}
			if (truck.isOnDelivery()) {
				String label4 = new String("Driver " + truck.getDriverName() + " has arrived at " +
						truck.getDestination() + " and is out delivering.");
				delivery_label_list.add(label4);
				truck.setOnDelivery();
			}
			if (truck.isReturning()) {
				String label5 = new String("Driver " + truck.getDriverName() + " has finished delivering and will" +
						" return back to the shop at " + truck.getAvailTime());
				delivery_label_list.add(label5);
				truck.setReturning();
			}
		}
		for (int i = 0; i < delivery_label_list.size(); i++) {
			delivery_list_model.add(i, delivery_label_list.get(i));
		}
		delivery_list = new JList(delivery_list_model);
		deliver_scroll.setViewportView(delivery_list);

		//set the scrollbars back where they were:
		prep_scroll.getHorizontalScrollBar().setValue(h1);
		prep_scroll.getVerticalScrollBar().setValue(y1);
		oven_scroll.getHorizontalScrollBar().setValue(h2);
		oven_scroll.getVerticalScrollBar().setValue(y2);
		deliver_scroll.getHorizontalScrollBar().setValue(h3);
		deliver_scroll.getVerticalScrollBar().setValue(y3);
	}


	/**
	 * Start the refresh timer
	 */
	public void startRefresher() {
		if (!refresherRunning) {
			timer = new Timer(150, updater);
			timer.start();
			refresherRunning = true;
		}
	}

	/**
	 * Stop the refresh timer
	 */
	public void stopRefresher() {
		if (refresherRunning) {
			timer.stop();
			refresherRunning = false;
		}
	}
}
