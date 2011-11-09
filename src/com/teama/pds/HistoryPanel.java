package com.teama.pds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author Matthew Frey
 *         This panel accesses the restaurant's history
 */
public class HistoryPanel extends JPanel {
	//TODO~ Documentation
	private JToolBar toolBar;
	private Controller controller;
	private Vector<Record> historyList;
	private JScrollPane infoScrollPane;
	private JPanel auxPane;
	private JList infoList;
	private ActionListener updater;
	private Timer timer;
	private JTable table;
	private JLabel timeCodeLabel;
	private JButton toggleUpdateButton;

	/**
	 * boolean value of refresher run status
	 * true - running
	 * false - stopped
	 */
	private boolean refresherRunning = false;

	/**
	 * Constructs the HistoryPanel
	 *
	 * @param controller The controller in MVC
	 */
	public HistoryPanel(Controller controller) {
		this.controller = controller;
		//historyList = controller.getRecordArray();
		buildPanel();
	}

	private void buildPanel() {
		//get screen size
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenDims.width - 200;
		int height = screenDims.height - 200;

		// instantiate components
		toolBar = new JToolBar(JToolBar.HORIZONTAL);
		JButton searchButton = new JButton(new ImageIcon("img/icon/search.png"));
		JButton helpButton = new JButton(new ImageIcon("img/icon/help.png"));
		toggleUpdateButton = new JButton("Toggle Auto-Refresh");
		timeCodeLabel = new JLabel();
		infoList = new JList();
		infoScrollPane = new JScrollPane();
		auxPane = new JPanel();
		historyList = new Vector<Record>();
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoScrollPane, auxPane);
		updater = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListings();
			}
		};

		//configure components
		//--searchButton
		searchButton.setToolTipText("Search");
		searchButton.setEnabled(false);

		//--helpButton
		helpButton.setToolTipText("Help");
		helpButton.setEnabled(false);

		//--toggleUpdateButton
		toggleUpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (refresherRunning) stopRefresher();
				if (!refresherRunning) startRefresher();
			}
		});

		//--auxPane
		auxPane.setLayout(new BorderLayout());
		auxPane.add(new JLabel("If necessary, auxiliary information will fall here"), BorderLayout.NORTH);

		//--infoScrollPane
		//infoScrollPane.add(infoList);
		infoScrollPane.setViewportView(infoList);
		infoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		infoScrollPane.setPreferredSize(new Dimension(width - width / 10 - 9, height - 30));
        infoScrollPane.setBorder(BorderFactory.createTitledBorder(""));
		updateListings();

		//--splitPane
		splitPane.setPreferredSize(new Dimension(width - width / 10, height - 37));
		splitPane.setResizeWeight(1);
		splitPane.setDividerSize(5);
		splitPane.setDividerLocation((width - width / 10) - width / 4);

		//--toolBar
		toolBar.setPreferredSize(new Dimension(width - width / 10, 32));
		toolBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		toolBar.setFloatable(false);
		toolBar.add(searchButton);
		toolBar.add(helpButton);
		//toolBar.add(timeCodeLabel);
		toolBar.setRollover(true);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

		//--HistoryPanel
		this.setLayout(new BorderLayout());

		//place everything into this panel
		//this.add(toolBar, BorderLayout.PAGE_START);
		this.add(infoScrollPane, BorderLayout.CENTER);

		//hide everything
		this.setVisible(false);
	}

	/**
	 * Any updates that need to happen every second get tossed in here
	 */
	public void updateListings() {
		//get the position of the scrollbar
		int sB = infoScrollPane.getVerticalScrollBar().getValue();

		//make a new listModel and fill it with history
		DefaultListModel listModel = new DefaultListModel();
		//update the historyList
		historyList = controller.getHistoryArray();
		int i = 0;
		for (Record r : historyList) {
			listModel.add(i, r.toString());
		}

		//before changing anything, save the user's selection
		int selectedIndex = infoList.getSelectedIndex();

		//update
		infoList = new JList(listModel);
		infoScrollPane.setViewportView(infoList);

		// not sure if needed
		infoList.validate();
		infoList.repaint();
		infoScrollPane.validate();
		infoScrollPane.repaint();
		this.validate();
		this.repaint();

		//fix selection
		infoList.setSelectedIndex(selectedIndex);

		//update timecode
		//timeCodeLabel.setText("Timecode: " + controller.getTime().getCurTime());

		//reset scrollbar position
		infoScrollPane.getVerticalScrollBar().setValue(sB);
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

	/**
	 * Fill out the auxiliary panel details
	 */
	public void fillOutAux(int sel_index) {
		Record selectedRecord = historyList.get(sel_index);

	}

    public JToolBar getToolBar() {
        return toolBar;
    }
}