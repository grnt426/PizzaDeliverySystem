package com.teama.pds;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author Matthew Frey, Grant Kurtz
 *         <p/>
 *         This panel accesses the restaurant's history
 */
public class OrdersPanel extends JPanel {

	/**
	 * The Toolbar, provides options for the InfoPanel
	 */
	private JToolBar toolBar;

	/**
	 * The timer for our updating
	 */
	private Timer timer;

	/**
	 * The action our timer creates
	 */
	private ActionListener updater;

	/**
	 * boolean value of refresher run status
	 * true - running
	 * false - stopped
	 */
	private boolean refresherRunning = false;

	/**
	 * The controller in our MVC pattern
	 */
	private Controller controller;

	/**
	 * JList (sortable) of orders and order contents (auxJList)
	 */
	private JList auxJList;

	/**
	 * The boxlayout of order entries
	 */
	private JPanel orderListPanel;

	/**
	 * The currently selected order
	 */
	private Order selectedOrder;

	/**
	 * the currently selected selectedOrderListEntry
	 */
	private OrderListEntry selectedOrderListEntry;

	/**
	 * scroll pane for orders
	 */
	private JScrollPane infoScrollPane;

	private JComboBox sortBySelector;
	private JComboBox sortDirectionSelector;

	// currently set temporary values
	private String cur_sort_selection;
	private String cur_sort_direction;

	/**
	 * Default Constructor.
	 * <p/>
	 * Constructs the HistoryPanel
	 *
	 * @param controller The MVC Controller
	 */
	public OrdersPanel(Controller controller) {
		this.controller = controller;
		//addOrder();
		buildPanel();
	}

	private void buildPanel() {

		// init stuff
		sortBySelector = new JComboBox();
		sortDirectionSelector = new JComboBox();
		cur_sort_selection = "Name";
		sortBySelector.addItem(cur_sort_selection);
		sortBySelector.addItem("Phone");
		cur_sort_direction = "Ascending";
		sortDirectionSelector.addItem(cur_sort_direction);
		sortDirectionSelector.addItem("Descending");

		// get screen size
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenDims.width - 200;
		int height = screenDims.height - 200;

		// instantiate components
		toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
		JButton searchButton = new JButton(
				new ImageIcon("img/icon/search.png"));
		JButton helpButton = new JButton(new ImageIcon("img/icon/help.png"));
		JButton addButton = new JButton(new ImageIcon("img/icon/add.png"));
		JButton removeButton = new JButton(
				new ImageIcon("img/icon/remove.png"));
		JButton delayButton = new JButton(new ImageIcon("img/icon/delay.png"));
		infoScrollPane = new JScrollPane();
		JScrollPane auxScrollPane = new JScrollPane();
		orderListPanel = new JPanel();
		updater = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresher();
			}
		};
		JButton tempRefreshButton = new JButton("refresh(temp)");
		JButton editButton = new JButton(new ImageIcon("img/icon/edit.png"));
		auxJList = new JList();
		selectedOrder = null;
//        hideFinishedOrdersCheckBox = new JCheckBox();
//        hideFinishedOrdersLabel = new JLabel("Hide finished orders: ");

		// configure components

		//      auxJList

		//      auxScrollPane
		auxScrollPane.setBackground(new Color(214, 234, 249));

		//      orderListPanel
		orderListPanel.setLayout(new BoxLayout(orderListPanel, BoxLayout.Y_AXIS));
		orderListPanel.setVisible(true);
		//orderListPanel.setBackground(new Color(214,234,249));

		//		searchButton
		searchButton.setToolTipText("Search orders");

		//		helpButton
		helpButton.setToolTipText("Help");

		//		addButton
		addButton.setToolTipText("Add a new order");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newOrder();
			}
		});

		//      editButton
		editButton.setToolTipText("Edit the selected order");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelectedOrder();
			}
		});

		//		removeButton
		removeButton.setToolTipText("Remove the selected order");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelOrder();
			}
		});

		//      delayButton
		delayButton.setToolTipText("Delay the selected order");
		delayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		//      tempRefreshButton
		tempRefreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		//		Sort selector
		sortBySelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_sort_selection = (String) sortBySelector.getSelectedItem();
				updateListings();
			}
		});

		sortDirectionSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cur_sort_direction = (String)
						sortDirectionSelector.getSelectedItem();
				updateListings();
			}
		});

		//		infoScrollPane
		infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		//		auxScrollPane
		auxScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		auxScrollPane.setBorder(BorderFactory.createTitledBorder("Selected Order Contents:"));

		//		splitPane
		JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, infoScrollPane, auxScrollPane);
		splitPane.setLeftComponent(infoScrollPane);
		splitPane.setPreferredSize(new Dimension(width - width / 10, height - 64));
		splitPane.setResizeWeight(1);
		splitPane.setDividerSize(5);
		splitPane.setDividerLocation((width - width / 10) - width / 4);
		splitPane.repaint();
		splitPane.revalidate();
		//splitPane.setBorder(BorderFactory.createTitledBorder("Orders"));

		//		toolBar
		toolBar.setPreferredSize(new Dimension(width - width / 10, 32));
		toolBar.setBorder(BorderFactory.createMatteBorder(
				0, 0, 1, 0, Color.BLACK));
		toolBar.setFloatable(false);
		toolBar.add(addButton);
		toolBar.add(editButton);
		toolBar.add(removeButton);
		toolBar.add(searchButton);
		JPanel toolbar_options = new JPanel();
		toolbar_options.setLayout(new BorderLayout());
		toolBar.add(toolbar_options);

		JPanel toolbar_buttons = new JPanel();
		toolbar_buttons.setLayout(new BoxLayout(toolbar_buttons,
				BoxLayout.X_AXIS));
		toolbar_options.add(toolbar_buttons, BorderLayout.WEST);
		toolbar_buttons.add(addButton);
		toolbar_buttons.add(editButton);
		toolbar_buttons.add(removeButton);
		toolbar_buttons.add(searchButton);
		//toolBar.add(helpButton);
		toolBar.add(delayButton);
		toolbar_buttons.add(delayButton);
		toolBar.setRollover(true);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

		//		toolBar Sort stuff
		JPanel toolbar_sort_options = new JPanel();
		toolbar_sort_options.setLayout(new BoxLayout(toolbar_sort_options,
				BoxLayout.X_AXIS));
		toolbar_sort_options.add(new JLabel("Sort: "));
		toolbar_sort_options.add(sortBySelector);
		toolbar_sort_options.add(sortDirectionSelector);
		toolbar_options.add(toolbar_sort_options, BorderLayout.EAST);

		//		OrdersPanel
		this.setLayout(new BorderLayout());

		//add things into the scrollPanes in the SplitPane
		//infoScrollPane.add(orderListPanel);
		infoScrollPane.setViewportView(orderListPanel);
		auxScrollPane.add(auxJList);
		auxScrollPane.setViewportView(auxJList);

		// place everything into this panel
		this.add(toolBar, BorderLayout.PAGE_START);
		this.add(splitPane, BorderLayout.CENTER);

		// hide everything
		this.setVisible(false);

	}

	/**
	 * Any updates that need to happen every second get tossed in here
	 */
	public void updateListings() {
		//get scrollbar position
		int sB = infoScrollPane.getVerticalScrollBar().getValue();

		//first empty the orderListPanel
		orderListPanel.removeAll();

        //add new orderListEntry panels
		ArrayList<Order> sorted_orders = getSortedOrders();

        for (Order o : sorted_orders){
            orderListPanel.add(new OrderListEntry(o, this));
        }
        this.repaint();
        this.revalidate();

        updateAuxList();

        //reset scrollbar position
        infoScrollPane.getVerticalScrollBar().setValue(sB);
    }

	private ArrayList<Order> getSortedOrders() {
		List<Order> sorted_orders =
				controller.getOrderData().getOrders();
		Collections.sort(sorted_orders, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				if (cur_sort_selection.equals("Name")) {
					return o1.getCustomer().getName().compareTo(
							o2.getCustomer().getName());
				} else if (cur_sort_selection.equals("Phone")) {
					return o1.getCustomer().getPhoneNumber().compareTo(
							o2.getCustomer().getPhoneNumber());
				}

				System.err.println("Invalid comparison for OrdersPanel");
				return 0;
			}
		});
		if(cur_sort_direction.equals("Descending"))
			Collections.reverse(sorted_orders);
		return (ArrayList<Order>) sorted_orders;
	}

	/**
	 * refreshes the list of fooditems in the selected order
	 */
	private void updateAuxList() {
		if (selectedOrder != null) {
			//get the food items from the selected order
			PriorityQueue<FoodItem> foodItemPQ = selectedOrder.getItems().getResourceQueue();

			//convert the priority queue to an array of FoodItem
			Iterator<FoodItem> iterator = foodItemPQ.iterator();
			Object[] foodItemArray = foodItemPQ.toArray();

			//for (FoodItem f : foodItems)
			auxJList.setListData(foodItemArray);
		}
	}

	/**
	 * Start the refresh timer
	 */
	public void startRefresher() {
		if (!refresherRunning) {
			timer = new Timer(1000, updater);
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

	/**
	 * Launches the order editing gui for the selected order
	 */
	public void editSelectedOrder() {
		if (selectedOrder != null) {
			controller.launchModifyOrderGUI(selectedOrder, this);
		}
	}

	/**
	 * Returns the selected Order
	 *
	 * @return selected order
	 */
	public Order getSelectedOrder() {
		return selectedOrder;
	}

	/**
	 * prompts the user to enter a value
	 * to delay the order by//TODO
	 */
	private void delayOrder() {
		int delayTime = -1;
		//Order selectedOrder = getSelectedOrder();
		if (selectedOrder != null) {
			while (delayTime < 0) {
				try {
					String input = (String) JOptionPane.showInputDialog(
							new JPanel(),
							"How many time units do you wish to delay the order by?",
							"Delay Order",
							JOptionPane.PLAIN_MESSAGE
					);
					delayTime = Integer.parseInt(input);

				} catch (NumberFormatException nfe) {
					delayTime = -1;
				}
			}
		}
	}

	/**
	 * Launches the modify order GUI from controller, containing no information
	 */
	private void newOrder() {
		controller.launchModifyOrderGUI(this);

		//make sure to have that GUI call updateListings() here when finished
	}

	/**
	 * This method handles what goes down
	 * when the user selects an order. Ole!
	 *
	 * @param ole the selected OrderListEntry
	 */
	public void orderClicked(OrderListEntry ole) {
		selectedOrder = ole.getOrder();
		selectedOrderListEntry = ole;

		//update coloring
		for (OrderListEntry e : getAllOrderListEntryPanels()) {
			e.setBackground(Color.WHITE);
		}
		ole.setBackground(new Color(154, 176, 198));

		//propagate the aux info
		updateAuxList();

	}

    /**
     * deletes the selected order
     */
    private void cancelOrder(){
        if (selectedOrder != null){
            if (controller.areYouSure("delete this order")){
                orderListPanel.remove(selectedOrderListEntry); //works
                auxJList.removeAll();
                auxJList.revalidate();
                controller.cancelOrder(getSelectedOrder());
                controller.getOrderData().getOrders().remove(getSelectedOrder());
                selectedOrder = null;
                selectedOrderListEntry = null;
                updateListings();
                repaint();
                revalidate();
                orderListPanel.repaint();
                orderListPanel.revalidate();
                updateListings();
            }
        }
    }

	/**
	 * returns an array list of all orderListEntries in the panel
	 *
	 * @return <code>ArrayList</code> of all OrderListEntries
	 */
	private ArrayList<OrderListEntry> getAllOrderListEntryPanels() {
		Component[] components = orderListPanel.getComponents();
		//convert the array of components to array of OrderListEntry
		ArrayList<OrderListEntry> arrayList = new ArrayList<OrderListEntry>();

		for (Component c : components) {
			arrayList.add((OrderListEntry) c);
		}

		return arrayList;
	}

    private void refresher(){
        for (OrderListEntry ole : getAllOrderListEntryPanels()){
            ole.updateInfo();
        }
    }

    public JToolBar getToolBar(){
        return toolBar;
    }
}
