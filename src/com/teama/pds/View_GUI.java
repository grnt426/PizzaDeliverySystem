package com.teama.pds;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Author: Matthew Frey,
 * <p/>
 * This class will be the Graphical User Interface for our Model.
 * Should be interchangeable with CLI or other Interfaces.
 * It will hold only basic framing information along with the
 * navigation pane. Other views will be sideloaded into the application.
 */
class View_GUI implements ActionListener {

	/**
	 * The controller in our MVC pattern
	 */
	private Controller controller;

	/**
	 * The swappable panel in our GUI
	 */
	private JPanel subPanel;

	/**
	 * Visibility of the frame
	 */
	public boolean visible = false;

	/**
	 * Main frame in which everything is contained
	 */
	private JFrame mainFrame;

	/**
	 * The OrdersPanel object ~contains order listings for view/edit
	 */
	private OrdersPanel ordersPanel;

	/**
	 * The HistoryPanel JPanel ~contains simulation history
	 */
	private HistoryPanel historyPanel;

	/**
	 * The Management JPanel ~contains config management
	 */
	private ManagementPanel managementPanel;

	/**
	 * The Simulation JPanel ~displays simulation status information
	 */
	private SimulationPanel simulationPanel;

	/**
	 * The Customer JPanel ~displays all customers in the system
	 */
	private CustomerPanel customerPanel;

	/**
	 * the default JPanel shown upon logging in
	 */
	private JPanel welcomePanel;

	/**
	 * boolean value to tell if refresher is currently running
	 */
	private boolean refresherRunning = false;

	/**
	 * The timer that will keep our panel updated
	 */
	private Timer timer;

	/**
	 * The action our timer creates
	 */
	private ActionListener updater;

	/**
	 * The buttons for our navigation scheme
	 */
	private JButton ordersButton, historyButton, managementButton, customersButton, simulationButton;

	/**
	 * fonts
	 */
	Font offNavNodeButtonFont = new Font("Verdana", Font.PLAIN, 11);
	Font onNavNodeButtonFont = new Font("Verdana", Font.BOLD, 11);

	/**
	 * Timecode label!
	 */
	JLabel timeCodeLabel;

	/**
	 * Constructor for the main GUI of PDS.
	 */
	public View_GUI(Controller controller) {
		//set up the seaglass look and feel
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
			System.err.println("Failed to load Seaglass Look and Feel");

		}
		//construct our various subPanels
		//ordersPanel = new OrdersPanel();
		controller.assignViewGUI(this);
		this.controller = controller;

		//start the gui
		initGui();

	}

	/**
	 * The main builder method. This will construct the entire GUI.
	 */
	private void initGui() {
		//Whip up our various variables so we can start cooking with them
		mainFrame = new JFrame("A-Team PDSys");
		BackgroundPanel navPanel = new BackgroundPanel("img/nav-bg.jpg");
		subPanel = new JPanel(); //this contains the hot-swapped panel
		JLabel navSpacer = new JLabel(" ");
		timeCodeLabel = new JLabel();
		ordersButton = new JButton("Orders");
		historyButton = new JButton("Console");
		simulationButton = new JButton("Simulation");
		managementButton = new JButton("Management");
		customersButton = new JButton(("Customers"));
		JButton logoutButton = new JButton("Logout");
		ordersPanel = new OrdersPanel(controller);
		historyPanel = new HistoryPanel(controller);
		managementPanel = new ManagementPanel(controller);
		simulationPanel = new SimulationPanel(controller);
		customerPanel = new CustomerPanel(controller);
		welcomePanel = new JPanel();
		JLabel bannerLabel = new JLabel(new ImageIcon("img/banner.png"));
        ComponentMover cmvr = new ComponentMover(mainFrame, ordersPanel.getToolBar());
        ComponentMover cmvr2 = new ComponentMover(mainFrame, historyPanel.getToolBar());
        ComponentMover cmvr3 = new ComponentMover(mainFrame, customerPanel.getToolBar());
        ComponentMover cmvr4 = new ComponentMover(mainFrame, managementPanel.getToolBar());

		//configure our timer refresher
		updater = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListings();
			}
		};

		//get screen size
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenDims.width - 200;
		int height = screenDims.height - 200;

		//Configure our various components
		//--mainFrame
		mainFrame.setLayout(new BorderLayout());
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.abandonShip();
			}
		});

		//--navPanel
		navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
		navPanel.setPreferredSize(new Dimension(width / 10, height));
		navPanel.setBackground(new Color(154, 176, 198));
		navPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Color.BLACK));

		//--subPanel
		subPanel.setSize(width - width / 10, height);
		subPanel.setBackground(new Color(154, 176, 198));
		subPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));

		//--welcomePanel
		welcomePanel.setLayout(new BorderLayout());
		//welcomePanel.add(authLabel, BorderLayout.NORTH);
		welcomePanel.add(bannerLabel, BorderLayout.CENTER);

		//--navlabel
		navSpacer.setForeground(Color.WHITE);

		//--timeCodeLabel
		timeCodeLabel.setText(" TimeCode: " + controller.getTime().getCurTime());
		timeCodeLabel.setFont(onNavNodeButtonFont);
		timeCodeLabel.setForeground(Color.WHITE);
		timeCodeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeCodeLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		//make our buttons the same width
		ordersButton.setMaximumSize(new Dimension(width / 9, 35));
		historyButton.setMaximumSize(new Dimension(width / 9, 35));
		simulationButton.setMaximumSize(new Dimension(width / 9, 35));
		managementButton.setMaximumSize(new Dimension(width / 9, 35));
		logoutButton.setMaximumSize(new Dimension(width / 9, 35));
		customersButton.setMaximumSize(new Dimension(width / 9, 35));

		//set our button's fonts
		ordersButton.setFont(offNavNodeButtonFont);
		historyButton.setFont(offNavNodeButtonFont);
		simulationButton.setFont(offNavNodeButtonFont);
		managementButton.setFont(offNavNodeButtonFont);
		logoutButton.setFont(offNavNodeButtonFont);
		customersButton.setFont(offNavNodeButtonFont);

		//give the buttons actionListeners
		ordersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPanelTo("ORDERS");
			}
		});
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPanelTo("HISTORY");
			}
		});
		managementButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPanelTo("MANAGEMENT");
			}
		});
		simulationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPanelTo("SIMULATION");
			}
		});
		customersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				swapPanelTo("CUSTOMERS");
			}
		});
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.swapMode();
				controller.logout();
				swapPanelTo("WELCOME");
			}
		});

		//add the buttons to the navPanel
		navPanel.add(new JLabel(" "));
		navPanel.add(ordersButton);
		navPanel.add(historyButton);
		navPanel.add(simulationButton);
		navPanel.add(customersButton);
		navPanel.add(managementButton);
		navPanel.add(logoutButton);
		navPanel.add(new JLabel(" "));
		navPanel.add(timeCodeLabel);

		//drop the panels into mainFrame
		mainFrame.add(navPanel, BorderLayout.WEST);
		mainFrame.add(subPanel, BorderLayout.CENTER);

		//add the various panels into the subPanel0.
		subPanel.add(ordersPanel);
		subPanel.add(historyPanel);
		subPanel.add(managementPanel);
		subPanel.add(simulationPanel);
		subPanel.add(customerPanel);
		subPanel.add(welcomePanel);
		ordersPanel.setVisible(false); //GO AWAY.

		//make the mainFrame visible, etc
		mainFrame.setSize(new Dimension(width, height));
		mainFrame.setMinimumSize(new Dimension(600, 400));
		mainFrame.setLocation((screenDims.width - width) / 2,
				(screenDims.height - height) / 2);
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		mainFrame.setResizable(false);
		//make it un-maximizable
		mainFrame.addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
					mainFrame.setExtendedState(JFrame.NORMAL);
				}
			}
		});
		startRefresher();

	}

	private void updateListings() {
		timeCodeLabel.setText("<html><u>TimeCode</u><br><center>" + controller.getTime().getCurTime() + "</center></html>");
	}

	/**
	 * Start the refresh timer
	 */
	public void startRefresher() {
		if (!refresherRunning) {
			timer = new Timer(50, updater);
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
	 * Hides all but the specified panel.
	 *
	 * @param navnode A string of the specified panel to make available
	 */
	private void swapPanelTo(String navnode) {

		ordersPanel.setVisible(false);
		historyPanel.setVisible(false);
		managementPanel.setVisible(false);
		simulationPanel.setVisible(false);
		customerPanel.setVisible(false);
		welcomePanel.setVisible(false);
		//customersPanel.setVisible(false);
		stopAllRefreshers();
		resetAllButtonFonts();
		if (navnode.equals("ORDERS")) {
			ordersPanel.setVisible(true);
			ordersButton.setFont(onNavNodeButtonFont);
			ordersPanel.startRefresher();
		} else if (navnode.equals("HISTORY")) {
			historyPanel.setVisible(true);
			historyButton.setFont(onNavNodeButtonFont);
			historyPanel.startRefresher();
		} else if (navnode.equals("MANAGEMENT")) {
			managementPanel.setVisible(true);
			managementButton.setFont(onNavNodeButtonFont);
			managementPanel.startRefresher();
		} else if (navnode.equals("SIMULATION")) {
			simulationPanel.setVisible(true);
			simulationButton.setFont(onNavNodeButtonFont);
			//simulationPanel.startRefresher();
		} else if (navnode.equals("WELCOME")) {
			welcomePanel.setVisible(true);
		} else if (navnode.equals("CUSTOMERS")) {
			customerPanel.setVisible(true);
			customersButton.setFont(onNavNodeButtonFont);
		}
	}

	public void SetController(Controller c) {
		this.controller = c;
	}

	/**
	 * Hides/Shows the authView
	 */
	public void toggleVisibility() {
		visible = !visible;
		mainFrame.setVisible(visible);
	}

	/**
	 * disposes the View_GUI
	 */
	public void dispose() {
		stopAllRefreshers();
		stopRefresher();
		mainFrame.dispose();
	}

	public void setAuthOptions() {
		if (controller.getToken().getAuthLevel() == User.AuthLevel.MANAGER) {
			managementButton.setVisible(true);
		} else {
			managementButton.setVisible(false);
		}
	}

	/**
	 * Stop refreshing Timer on all panels
	 */
	public void stopAllRefreshers() {
		ordersPanel.stopRefresher();
		historyPanel.stopRefresher();
		//managementPanel.stopRefresher();
		//simulationPanel.stopRefresher();
		customerPanel.stopRefresher();
	}

	/**
	 * Resets all nav button fonts (for navigation)
	 */
	public void resetAllButtonFonts() {
		ordersButton.setFont(offNavNodeButtonFont);
		historyButton.setFont(offNavNodeButtonFont);
		managementButton.setFont(offNavNodeButtonFont);
		simulationButton.setFont(offNavNodeButtonFont);
		customersButton.setFont(offNavNodeButtonFont);
	}

	public void actionPerformed(ActionEvent e) {
		String msg = e.getActionCommand();
		if (msg.equals("Add_Customer")) {
			customerPanel.updateCustomerData();
		}
	}

	public void updateCustomerPanel() {
		if (customerPanel != null)
			customerPanel.updateCustomerData();
	}
}
