package com.teama.pds;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.*;

/**
 * @author Matthew Frey
 */
public class OrderModifierView extends javax.swing.JFrame {
    // Variables declaration
    private javax.swing.JButton addItemButton;
    private javax.swing.JButton addItemExtrasButton;
    private javax.swing.JLabel addItemLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel extrasLabel;
    private javax.swing.JPanel foodItemsPanel;
    private javax.swing.JScrollPane foodItemsScrollPane;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel innerPanel;
    private javax.swing.JComboBox itemComboBox;
    private javax.swing.JComboBox itemExtrasComboBox;
    private javax.swing.JList itemJList;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JComboBox locationComboBox;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okayButton;
    private javax.swing.JLabel orderProgressLabel;
    private javax.swing.JLabel orderStatusLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField phoneTextField;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JLabel tipLabel;
    private javax.swing.JToolBar toolbar;
    private javax.swing.JLabel totalCostLabel;
    private javax.swing.JTextField totalCostTextField;
    private javax.swing.JButton undoButton;
    private Controller controller;
    private OrdersPanel ordersPanel;
    private Order order;
    private String title;
    private boolean hasTimer = false;
    private Timer progressTimer;
    private ActionListener progressUpdater;
    private boolean creation;
    private double orderCost;
    private FoodItem selectedFoodItem;
    private String[] toppingStrings;
    private DefaultComboBoxModel itemExtrasComboBoxModel;
    private DefaultListModel itemListModel;
    private ArrayList<FoodItem> foodItems;
    private Customer customer;
    private JPopupMenu popupMenu;

    // End of variables declaration

    /**
     * Constructor for creating a new order
     *
     * @param controller  The controller in MVC
     * @param ordersPanel The parent panel that spawned this instance
     */
    public OrderModifierView(Controller controller, OrdersPanel ordersPanel) {
        this.controller = controller;
        this.ordersPanel = ordersPanel;
        this.title = "Create a new order";
        creation = true;
        initComponents();
    }

    /**
     * Constructor for editing an Order
     *
     * @param controller  The Controller in MVC
     * @param order       An Order to be modified
     * @param ordersPanel the parent panel that spawned this instance
     */
    public OrderModifierView(Controller controller, Order order, OrdersPanel ordersPanel) {
        this.controller = controller;
        this.ordersPanel = ordersPanel;
        this.title = "Modify an order";
        creation = false;
        this.order = order;
        initComponents();
        loadFieldsFromOrder(order);
        fieldDisableCheck();
    }


    private void initComponents() {

        okayButton = new javax.swing.JButton();
        toolbar = new javax.swing.JToolBar();
        tipLabel = new javax.swing.JLabel();
        innerPanel = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        foodItemsPanel = new javax.swing.JPanel();
        foodItemsScrollPane = new javax.swing.JScrollPane();
        itemJList = new javax.swing.JList();
        formPanel = new javax.swing.JPanel();
        phoneLabel = new javax.swing.JLabel();
        phoneTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        totalCostLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox();
        totalCostTextField = new javax.swing.JTextField();
        undoButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        orderProgressLabel = new javax.swing.JLabel();
        orderStatusLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        addItemLabel = new javax.swing.JLabel();
        itemComboBox = new javax.swing.JComboBox();
        addItemButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        itemExtrasComboBox = new javax.swing.JComboBox();
        extrasLabel = new javax.swing.JLabel();
        addItemExtrasButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();
        itemExtrasComboBoxModel = new DefaultComboBoxModel();
        itemListModel = new DefaultListModel();
        foodItems = new ArrayList<FoodItem>();
        popupMenu = new JPopupMenu();

        setTitle(title);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        JMenuItem deleteOrderMenuItem = new JMenuItem("Remove Item");
        deleteOrderMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedFoodItem();
            }
        });
        popupMenu.add(deleteOrderMenuItem);


        okayButton.setText("Okay");
        okayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okayButtonActionPerformed(evt);
            }
        });

        toolbar.setBackground(new java.awt.Color(154, 176, 198));
        toolbar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)));
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setFocusable(false);

        tipLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tipLabel.setText("Press the Enter key in the phone field to autocomplete." +
                " Right click a food item to remove it.");
        tipLabel.setFocusable(false);
        toolbar.add(tipLabel);

        innerPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        splitPane.setDividerLocation(300);


        javax.swing.GroupLayout foodItemsPanelLayout = new javax.swing.GroupLayout(foodItemsPanel);
        foodItemsPanel.setLayout(foodItemsPanelLayout);
        foodItemsPanelLayout.setHorizontalGroup(
                foodItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(foodItemsScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
        );
        foodItemsPanelLayout.setVerticalGroup(
                foodItemsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(foodItemsScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
        );

        splitPane.setRightComponent(foodItemsPanel);

        phoneLabel.setText("Phone");

        phoneTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        nameLabel.setText("Name:");

        nameTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);

        locationLabel.setText("Location: ");

        totalCostLabel.setText("Total Cost:");

        //configure the locationComboBox
        ArrayList<String> locations = controller.getLocations();
        String[] locStrings = new String[locations.size() + 1];
        int i = 1;
        locStrings[0] = "";
        for (String s : locations) {
            locStrings[i] = s;
            i++;
        }
        locationComboBox = new JComboBox(locStrings);

        //configure the foodItemComboBox
        Object[] foodItemObjectArray = controller.getMenu().getMenu().keySet().toArray();
        String[] foods = new String[foodItemObjectArray.length + 1];
        i = 1;
        foods[0] = "";
        for (Object o : foodItemObjectArray) {
            foods[i] = o.toString();
            i++;
        }
        itemComboBox = new JComboBox(foods);
        itemComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemBoxSelectionChanged();
            }
        });

        //configure the extras combo box
        itemExtrasComboBox = new JComboBox(itemExtrasComboBoxModel);
        itemExtrasComboBoxModel.addElement("");

        //configure the list of items, itemJList
        itemJList = new JList(itemListModel);
        itemJList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                popupClick(e);
            }

            public void mouseReleased(MouseEvent e) {
                popupClick(e);
            }
        });

        foodItemsScrollPane.setViewportView(itemJList);

        totalCostTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        totalCostTextField.setText("$0.00");

        undoButton.setText("Undo changes");
        undoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                undoButtonActionPerformed(evt);
            }
        });

        orderProgressLabel.setText("Order Progress:");

        orderStatusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        orderStatusLabel.setText("<STATUS>");

        addItemLabel.setText("Add Item:");

        addItemButton.setText("+");
        addItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemButtonActionPerformed(evt);
            }
        });

        extrasLabel.setText("Extras:");

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                                        .addComponent(progressBar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(addItemLabel)
                                                        .addComponent(extrasLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(itemExtrasComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(itemComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 177, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(addItemButton, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(addItemExtrasButton, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(formPanelLayout.createSequentialGroup()
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(phoneLabel)
                                                        .addComponent(nameLabel)
                                                        .addComponent(locationLabel)
                                                        .addComponent(totalCostLabel))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(totalCostTextField)
                                                        .addComponent(locationComboBox)
                                                        .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(phoneTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, formPanelLayout.createSequentialGroup()
                                                .addComponent(orderProgressLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                                                .addComponent(orderStatusLabel))
                                        .addComponent(undoButton))
                                .addContainerGap())
        );
        formPanelLayout.setVerticalGroup(
                formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(formPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(phoneLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(locationLabel)
                                        .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(totalCostLabel)
                                        .addComponent(totalCostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(5, 5, 5)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(orderProgressLabel)
                                        .addComponent(orderStatusLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addItemLabel)
                                        .addComponent(itemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addItemButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(itemExtrasComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(extrasLabel)
                                        .addComponent(addItemExtrasButton))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(undoButton)
                                .addContainerGap(18, Short.MAX_VALUE))
        );

        splitPane.setLeftComponent(formPanel);

        javax.swing.GroupLayout innerPanelLayout = new javax.swing.GroupLayout(innerPanel);
        innerPanel.setLayout(innerPanelLayout);
        innerPanelLayout.setHorizontalGroup(
                innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(innerPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 567, Short.MAX_VALUE)
                                .addContainerGap())
        );
        innerPanelLayout.setVerticalGroup(
                innerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(innerPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                                .addContainerGap())
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(418, Short.MAX_VALUE)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(okayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addComponent(toolbar, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                        .addComponent(innerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(innerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(okayButton)
                                        .addComponent(cancelButton))
                                .addContainerGap())
        );

        pack();
        //NON-GENERATED CODE FALLS BELOW THIS LINE --------------------------------

        //make enter key handle phone search
        KeyListener submitWithEnter = new KeyListener() {
            public void keyTyped(KeyEvent e) {
                //nothing
            }

            public void keyPressed(KeyEvent e) {
                //nothing
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == 10) {
                    Customer lookedUp = lookupNumber();
                    if (lookedUp != null) {
                        customer = lookedUp;
                    }
                }
            }
        };
        phoneTextField.addKeyListener(submitWithEnter);

        //set dimensions and get dialog centered on screen
        Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 591;
        int height = 417;
        this.setLocation((screenDims.width - width) / 2,
                (screenDims.height - height) / 2);

        //set close action
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        //override maximize
        addWindowStateListener(new WindowAdapter() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
                    setExtendedState(JFrame.NORMAL);
                }
            }
        });

        //other tweaks
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        if (creation) {
            orderStatusLabel.setText("Not Started");
        }
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        totalCostTextField.setEditable(false);
        totalCostTextField.setFocusable(false);
        totalCostTextField.setHorizontalAlignment(JTextField.RIGHT);

        addItemExtrasButton.setVisible(false);
        addItemExtrasButton.setEnabled(false);
        //NON-GENERATED CODE FALLS ABOVE THIS LINE --------------------------------
    }

    private void removeSelectedFoodItem() {
        itemJList.remove(itemJList.getComponent(itemJList.getSelectedIndex()));
        itemListModel.removeElementAt(itemJList.getSelectedIndex());
    }

    private void popupClick(MouseEvent e) {
        if (e.isPopupTrigger()) {
            itemJList.setSelectedIndex(itemJList.locationToIndex(e.getPoint()));
            popupMenu.show(itemJList, e.getX(), e.getY());
        }
    }

    private String formatMoney(double money){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(getLocale());
        return currencyFormatter.format(money);
    }

    /**
     * Looks up customer number. If not found, red. If found, propagates.
     */
    private Customer lookupNumber() {
        String searchNumber = phoneTextField.getText();
        boolean found = false;
        Customer foundCust = null;
        for (Customer c : controller.getAllCustomers()) {
            if (c.getPhoneNumber().equals(searchNumber)) {
                found = true;
                foundCust = c;
            }
        }
        if (found) {
            nameTextField.setText(foundCust.getName());
            locationComboBox.setSelectedItem(foundCust.getLocation());
            phoneTextField.setBackground(Color.WHITE);
            return foundCust;

        } else {
            phoneTextField.setBackground(Color.PINK);
            return null;
        }
    }

    private void okayButtonActionPerformed(ActionEvent evt) {
        //make sure we are creating an order (order == null)
        if (creation) {
            if (customer == null) { //if we haven't looked up a customer
                boolean correctFields = false;
                Order newOrder = new Order(new TimeReader(controller.getTime()), controller.getMenu());
                Customer newCustomer = new Customer();

                try {
                    newCustomer.setName(nameTextField.getText());
                    newCustomer.setLocation(locationComboBox.getSelectedItem().toString());
                    newCustomer.setPhoneNumber(formatPhoneNumber(phoneTextField.getText()));
                    newOrder.setCustomer(newCustomer);
                    newOrder.setLocation(newCustomer.getLocation());

                    // add food items to the order
                    for (FoodItem f : foodItems) {
                        newOrder.addItem(f);
                    }

                    //all looks good if no exception thrown, so:
                    correctFields = true;
                } catch (Exception e) {
                    correctFields = false;
                }
                //check if name is empty
                if (nameTextField.getText().equals("")) correctFields = false;

                //check if no food added to order
                if (foodItems.size() < 1) {
                    correctFields = false;
                }

                if (correctFields) {
                    controller.addOrder(newOrder);
                    controller.addCustomer(newCustomer);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect fields");
                }
            } else { //we have looked up a customer
                boolean correctFields = false;
                Order newOrder = new Order(new TimeReader(controller.getTime()), controller.getMenu());

                try {
                    customer.setName(nameTextField.getText());
                    customer.setLocation(locationComboBox.getSelectedItem().toString());
                    customer.setPhoneNumber(formatPhoneNumber(phoneTextField.getText()));
                    newOrder.setCustomer(customer);
                    newOrder.setLocation(customer.getLocation());

                    // add food items to the order
                    for (FoodItem f : foodItems) {
                        newOrder.addItem(f);
                    }

                    //all looks good if no exception thrown, so:
                    correctFields = true;
                } catch (Exception e) {
                    correctFields = false;
                }
                //check if name is empty
                if (nameTextField.getText().equals("")) correctFields = false;

                //check if no food added to order
                if (foodItems.size() < 1) {
                    correctFields = false;
                }

                if (correctFields) {
                    controller.addOrder(newOrder);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect fields");
                }
            }
        } else if (!creation) {
            //modify order
            //details should be pulled already, so we can just send them back now
            boolean correctFields = false;
            Customer customer = order.getCustomer();

            try {
                customer.setName(nameTextField.getText());
                customer.setLocation(locationComboBox.getSelectedItem().toString());
                customer.setPhoneNumber(formatPhoneNumber(phoneTextField.getText()));
                order.setCustomer(customer);
                order.setLocation(customer.getLocation());

                // replace the list?


                //all looks good if no exception  thrown, so:
                correctFields = true;
            } catch (Exception e) {
                e.printStackTrace();
                correctFields = false;
            }
            //check if name is empty
            if (nameTextField.getText().equals("")) correctFields = false;

            //check if no food added to order
            if (foodItems.size() < 1) {
                correctFields = false;
            }

            if (correctFields) {
                //no need to add things into the system again, they're already there
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect fields");
            }
        }
        ordersPanel.updateListings();
    }

    private void undoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //if no order, clear all fields
        if (creation) {
            phoneTextField.setText("");
            nameTextField.setText("");
            locationComboBox.setSelectedItem("Choose one:");
            totalCostTextField.setText("");
            foodItems.clear();
            itemJList.removeAll();
            itemListModel.removeAllElements();

        } else {
            //if there is an order, revert to it's original attributes
            phoneTextField.setText(order.getCustomer().getPhoneNumber());
            nameTextField.setText(order.getCustomer().getName());
            locationComboBox.setSelectedItem(order.getLocation());
            double amount = order.getTotalCost();
            totalCostTextField.setText(formatMoney(amount));
        }
    }

    private void addItemButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //get the selected food item
        String type = itemComboBox.getSelectedItem().toString();
        String modifier = itemExtrasComboBox.getSelectedItem().toString();
        PizzaItem pizza;

        if (!type.equals("")) {
            if (!modifier.equals("")) {
                //this is pizza, adding pizzaitem

                pizza = new PizzaItem(
                        type.split(" ")[0],                         //size
                        1.0,                                        //topping multiplier
                        new TimeReader(controller.getTime()),       //timeReader
                        controller.getMenu().getCookedItem(type));  //cookedItem
                if (!modifier.equalsIgnoreCase("plain")) {
                    pizza.addTopping(PizzaItem.Topping.valueOf(modifier.toUpperCase()));
                }
                pizza.setToppingCost();
                foodItems.add(pizza);
                itemListModel.addElement(pizza.toString());
                //if we aren't creating, we can just add it directly!
                if (!creation){
                    order.addItem(pizza);
                    controller.addItemToMake(pizza);
                }
            } else {
                //adding a non-pizza, cookeditem or fooditem
                //unfortunately, this has to be static so it seems; fix that
                if (type.contains("Salad")) {
                    FoodItem item = controller.getMenu().getUncookedItem("Salad");
                    foodItems.add(item);
                    itemListModel.addElement(item.toString());
                    //if we aren't creating, we can just add it directly!
                    if (!creation){
                        order.addItem(item);
                        controller.addItemToMake(item);
                    }
                } else if (type.equalsIgnoreCase("Pizza Logs")) {
                    FoodItem item = controller.getMenu().getCookedItem("Pizza Logs");
                    foodItems.add(item);
                    itemListModel.addElement(item.toString());
                    //if we aren't creating, we can just add it directly!
                    if (!creation){
                        order.addItem(item);
                        controller.addItemToMake(item);
                    }
                }

            }
        } else {
            //this is nothing
        }
        updateCostTotal();
    }

    /**
     * updates the total cost field with the correct cost
     */
    private void updateCostTotal() {
        double cost = 0.0;
        for (FoodItem f : foodItems) {
            cost += f.getCost();
        }
        totalCostTextField.setText(formatMoney(cost));
    }

    /**
     * takes care of items that cannot be added mid simulation
     */
    private void postProcessItems(){

    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        //CANCEL BUTTON
        if (progressTimer != null && progressTimer.isRunning()) {
            progressTimer.stop();
        }
        this.dispose();
    }

    /**
     * pulls in the fields from the order passed to the constructor
     */
    private void loadFieldsFromOrder(final Order o) {
        //double check we have an order to edit
        if (order != null) {
            this.nameTextField.setText(o.getCustomer().getName());
            this.locationComboBox.setSelectedItem(o.getCustomer().getLocation());
            this.phoneTextField.setText(o.getCustomer().getPhoneNumber());
            this.totalCostTextField.setText(formatMoney(o.getTotalCost()));

            //handle the progress bar
            this.progressBar.setMinimum(o.getOrderStartTime());
            this.progressBar.setMaximum(o.getOrderFinishTime());
            //if the order is complete, fill progressBar
            if (o.completed()) {
                progressBar.setMinimum(0);
                progressBar.setMaximum(1);
                progressBar.setValue(1);
            }

            //spawn a Timer to keep progress bar and status label up to date
            hasTimer = true;
            progressUpdater = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    progressBar.setValue(controller.getTime().getCurTime());
                    if (o.completed()) {
                        orderStatusLabel.setText("Completed");
                    } else {
                        orderStatusLabel.setText("In Progress");
                    }
                    //try if not updating: jProgressBar1.repaint();
                }
            };
            progressTimer = new Timer(50, progressUpdater);
            progressTimer.start();

            //from here, load our order list
            foodItems = order.getFoodItemsList();
            for (FoodItem f : foodItems) {
                itemListModel.addElement(f.toString());
            }
            updateCostTotal();

        }
    }

    /**
     * Disable all fields (in case the order is finished)
     */
    private void fieldDisableCheck() {
        if (order != null) {
            if (order.completed()) {
                nameTextField.setEditable(false);
                locationComboBox.setEditable(false);
                phoneTextField.setEditable(false);
                totalCostTextField.setEditable(false);
                undoButton.setEnabled(false);
                okayButton.setEnabled(false);
                itemJList.setEnabled(false);
                itemComboBox.setEnabled(false);
                itemExtrasComboBox.setEnabled(false);
                addItemButton.setEnabled(false);
                addItemExtrasButton.setEnabled(false);
            }
        }
    }

    private String formatPhoneNumber(String number) {
        int length = number.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(number.charAt(i))) {
                number = number.substring(0, i) + number.substring(i + 1,
                        number.length());
                i--;
                length--;
            }
        }
        return number;
    }

    /**
     * selection changed on itemBox, update extras
     * contains special cases for pizzaitems
     */
    private void itemBoxSelectionChanged() {
        //update the Extras comboBox
        PizzaItem pizzaItem;
        ArrayList<PizzaItem.Topping> toppings;
        itemExtrasComboBox.removeAll();
        itemExtrasComboBoxModel.removeAllElements();

        selectedFoodItem = controller.getMenu().getMenu().get(itemComboBox.getSelectedItem().toString());
        if (itemComboBox.getSelectedItem().toString().equals("Large Pizza") ||
                itemComboBox.getSelectedItem().toString().equals("Medium Pizza") ||
                itemComboBox.getSelectedItem().toString().equals("Small Pizza")
                ) {
            //we have a pizza!
            pizzaItem = (PizzaItem) selectedFoodItem;
            toppings = pizzaItem.getToppings();

            //fill our combobox
            itemExtrasComboBoxModel.addElement("PLAIN");
            for (PizzaItem.Topping top : toppings) {
                itemExtrasComboBoxModel.addElement(top.toString());
            }

        } else {
            //not a pizza, wipe :C
            itemExtrasComboBox.removeAllItems();
            //itemExtrasComboBox.setSelectedIndex(-1);
            itemExtrasComboBoxModel.addElement("");
            itemExtrasComboBox.setSelectedIndex(0);
        }

    }
}
