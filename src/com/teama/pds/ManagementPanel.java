package com.teama.pds;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Matthew Frey
 * This panel displays the config management UI
 */
public class ManagementPanel extends JPanel implements ActionListener {

    private Controller controller;
    private JToolBar toolBar;
    private Timer timer;
    private ActionListener updater;
    private boolean refresherRunning = false;
    private JScrollPane infoScrollPane;
    private JList user_list;
	private NumberFormat money;
	private JPanel sales_stats;
	private JLabel total_tendered, total_orders, total_canceled_orders,
			avg_cost_per_order, avg_time_for_order, avg_creation_time,
			avg_delivery_time;

	// temporary selection data
	private User cur_select_user;
	private JPanel cur_select_users_panel;
	private JComboBox sortBySelector;
	private String cur_sort_selection;
	private JComboBox sortDirectionSelector;
	private String cur_sort_direction;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JTextField searchField;
	private JPanel allUserInfo;
	private String search_data = "";

	/**
     * Construct the ManagementPanel
     */
    public ManagementPanel(Controller controller){
        this.controller = controller;
		money = new DecimalFormat("0.00");
		buildPanel();
    }

    public void buildPanel(){
        // get screen size
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenDims.width - 200;
		int height = screenDims.height - 200;

        toolBar = new JToolBar(JToolBar.HORIZONTAL);
		addButton = new JButton(new ImageIcon("img/icon/add.png"));
		addButton.setToolTipText("Add a user");
		editButton = new JButton(new ImageIcon("img/icon/edit.png"));
		editButton.setToolTipText("Edit selected user");
		deleteButton = new JButton(new ImageIcon("img/icon/remove.png"));
		deleteButton.setToolTipText("Delete selected user");
		searchField = new JTextField();
		searchField.setColumns(20);
		searchField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				doPartialSearch(searchField.getText());
			}
		});
		allUserInfo = new JPanel();
		allUserInfo.setLayout(new BoxLayout(allUserInfo,
				BoxLayout.Y_AXIS));
        infoScrollPane = new JScrollPane(allUserInfo);

        updater = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                refresher();
			}
		};

		//configure buttons
		//--set action listeners
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addUser();
			}
		});
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editSelectedUser();
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cur_select_user != null) {
					deleteSelectedUser();
				}
			}
		});

		// --Sort Control
		sortBySelector = new JComboBox();
		sortBySelector.setMaximumSize(new Dimension(150, 50));
		cur_sort_selection = "Name";
		sortBySelector.addItem(cur_sort_selection);
		sortBySelector.addItem("Auth Level");
		sortBySelector.addItem("Username");
		sortBySelector.addActionListener(this);

		sortDirectionSelector = new JComboBox();
		sortDirectionSelector.setMaximumSize(new Dimension(150, 50));
		cur_sort_direction = "Ascending";
		sortDirectionSelector.addItem(cur_sort_direction);
		sortDirectionSelector.addItem("Descending");
		sortDirectionSelector.addActionListener(this);

		//		auxScrollPane Data
		sales_stats = new JPanel(new GridLayout(10, 2));
		sales_stats = new JPanel(new GridLayout(10, 2));
		sales_stats.add(new JLabel("Total Tendered: "));
		sales_stats.add(total_tendered = new JLabel());
		sales_stats.add(new JLabel("Total Orders: "));
		sales_stats.add(total_orders = new JLabel());
		sales_stats.add(new JLabel("Total Canceled Orders: "));
		sales_stats.add(total_canceled_orders = new JLabel());
		sales_stats.add(new JLabel("Avg Cost Per Order: "));
		sales_stats.add(avg_cost_per_order = new JLabel());
		sales_stats.add(new JLabel("Average Times: "));
		sales_stats.add(new JLabel(""));
		sales_stats.add(new JLabel("Average Order Time: "));
		sales_stats.add(avg_time_for_order = new JLabel());
		sales_stats.add(new JLabel("Average Creation Time: "));
		sales_stats.add(avg_creation_time = new JLabel());
		sales_stats.add(new JLabel("Average Delivery Time: "));
		sales_stats.add(avg_delivery_time = new JLabel());
		refreshSalesStats();
		JScrollPane auxScrollPane = new JScrollPane(sales_stats);
        auxScrollPane.setBackground(new Color(214, 234, 249));
        infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        //		auxScrollPane
        auxScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        auxScrollPane.setBorder(BorderFactory.createTitledBorder("Sales" +
				" Statistics:"));

        //splitPane
        JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, infoScrollPane, auxScrollPane);
        splitPane.setLeftComponent(infoScrollPane);
		splitPane.setPreferredSize(new Dimension(width - width / 10, height - 64));
		splitPane.setResizeWeight(1);
		splitPane.setDividerSize(5);
		splitPane.setDividerLocation((width - width/10) - width/4);
        splitPane.repaint();
        splitPane.revalidate();
        //splitPane.setBorder(BorderFactory.createTitledBorder("Orders"));

        //		toolBar
		toolBar.setPreferredSize(new Dimension(width - width / 10, 32));
		toolBar.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, Color.BLACK));
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

		JPanel toolbar_options = new JPanel(new BorderLayout());
		JPanel toolbar_buttons = new JPanel();
		toolbar_buttons.setLayout(new BoxLayout(toolbar_buttons,
				BoxLayout.X_AXIS));
		toolbar_buttons.add(addButton);
		toolbar_buttons.add(editButton);
		toolbar_buttons.add(deleteButton);
		toolbar_buttons.add(searchField);
		toolbar_options.add(toolbar_buttons, BorderLayout.WEST);
		JPanel toolbar_sort = new JPanel();
		toolbar_sort.setLayout(new BoxLayout(toolbar_sort, BoxLayout.X_AXIS));
		toolbar_sort.add(new JLabel("Sort Options"));
		toolbar_sort.add(sortBySelector);
		toolbar_sort.add(sortDirectionSelector);
		toolbar_options.add(toolbar_sort, BorderLayout.EAST);
		toolBar.add(toolbar_options);
		refreshUserData();


		//		OrdersPanel
		this.setLayout(new BorderLayout());

        // place everything into this panel
        this.add(toolBar, BorderLayout.PAGE_START);
        this.add(splitPane, BorderLayout.CENTER);

        // hide everything
        this.setVisible(false);


    }

	private void deleteSelectedUser() {
		if (controller.areYouSure("delete the selected User")) {
			User selectedUser = getSelectedUser();

			// get the selected customer
			if (controller.getUsers().contains(selectedUser)) {
				controller.deleteUser(selectedUser);
				refreshUserData();
            } else {
                //nothing
            }
        } else {
            //do nothing
        }

	}

	private void editSelectedUser() {
		if(getSelectedUser() != null)
			controller.launchUserModifyGUI(getSelectedUser());
	}

	private User getSelectedUser() {
		return cur_select_user;
	}

	private void addUser() {
		controller.launchUserModifyGUI();
	}

	private void doPartialSearch(String search_text) {
		search_data = search_text;
		refreshUserData();
	}

	private void refresher() {
		refreshUserData();
		refreshSalesStats();
	}

	private void refreshUserData() {

		// clear out existing components
		allUserInfo.removeAll();

		// grab the user-defined sorted list
		ArrayList<User> users = grabSortedUsers();

		for(final User user : users){

			final JPanel userPanel = new JPanel(new BorderLayout());

			// set this panel's properties
			userPanel.setMaximumSize(new Dimension(9400, 100));
			userPanel.setAlignmentX(0);
			userPanel.setBorder(BorderFactory.createTitledBorder(
					user.getNameOfUser()));

			// build the click-handler
			MouseListener l = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					clickedUser(user, userPanel);
				}
			};
			userPanel.addMouseListener(l);

			// customer information breakdown
			JPanel userInfo = new JPanel(new GridLayout(3, 2));
			userPanel.add(userInfo);
			userInfo.add(new JLabel("Username: "));
			userInfo.add(new JLabel(user.getNameOfUser()));
			userInfo.add(new JLabel("Authentication Level: "));
			userInfo.add(new JLabel(user.getAuthLevel().name()));
			userPanel.add(userInfo, BorderLayout.WEST);

			allUserInfo.add(userPanel);
		}
	}

	private ArrayList<User> grabSortedUsers() {
		ArrayList<User> sorted_users;
		if(search_data.length() == 0){
			sorted_users = controller.getUsers();
		}
		else{
			sorted_users = controller.getMatchingUsers(search_data);
		}

		Collections.sort(sorted_users, new Comparator<User>() {
			public int compare(User o1, User o2) {
				if(cur_sort_selection.equals("Name")){
					return o1.getNameOfUser().compareTo(o2.getNameOfUser());
				}
				else if(cur_sort_selection.equals("Username")){
					return o1.getUsername().compareTo(o2.getUsername());
				}
				else if(cur_sort_selection.equals("Auth Level")){
					if(o1.getAuthLevel() == o2.getAuthLevel())
						return 0;
					else if(o1.getAuthLevel() == User.AuthLevel.MANAGER
							&& o2.getAuthLevel() == User.AuthLevel.CASHIER)
						return 1;
					return -1;
				}

				//TODO: better handle this
				System.err.println("Invalid sort selected in ManagementPanel");
				return 0;
			}
		});

		if(cur_sort_direction.equals("Descending"))
			Collections.reverse(sorted_users);
		return sorted_users;
	}

	private void refreshSalesStats(){

		// grab a historical account of all orders
		OrderList order_history = controller.getOrderData();

		// update data
		total_tendered.setText("$" + money.format(
				order_history.getTotalTendered()));
		total_orders.setText("" + order_history.getTotalOrders());
		total_canceled_orders.setText("" +
				(order_history.getTotalOrders()
				- order_history.getTotalNonCanceledOrders()));
		avg_cost_per_order.setText("$" + money.format(
				order_history.getAverageOrderCost()));
		avg_time_for_order.setText(order_history.getAverageTotalTimeSpent()
				+ "s");
		avg_creation_time.setText(order_history.getAverageTotalCreationTime()
				+ "s");
		avg_delivery_time.setText(order_history.getAverageTotalDeliveryTime()
				+ "s");

		// refresh the container
		sales_stats.revalidate();
		sales_stats.repaint();
	}
	
	private void clickedUser(User user, JPanel user_panel) {
		if (cur_select_users_panel != null)
			cur_select_users_panel.setBackground(Color.WHITE);
		user_panel.setBackground(new Color(154, 176, 198));
		cur_select_users_panel = user_panel;
		cur_select_user = user;
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

	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		String msg = e.getActionCommand();

		if (object.equals(sortBySelector)) {
			String sort_by_select = (String) sortBySelector.getSelectedItem();
			if (!sort_by_select.equals(cur_sort_selection)) {
				cur_sort_selection = sort_by_select;
				refreshUserData();
			}
		} else if (object.equals(sortDirectionSelector)) {
			String dir_select = (String)
					sortDirectionSelector.getSelectedItem();
			if (!dir_select.equals(cur_sort_direction)) {
				reverseSortDirection();
			}
		}
	}

	private void reverseSortDirection() {
		cur_sort_direction = cur_sort_direction.equals("Ascending") ?
				"Descending" : "Ascending";
		refreshUserData();
	}
    
    public JToolBar getToolBar() {
        return toolBar;
    }
}
