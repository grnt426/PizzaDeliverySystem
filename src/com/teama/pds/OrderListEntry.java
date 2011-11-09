package com.teama.pds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

/**
 * This Class is the panel that is used by
 * OrderListCellRenderer to create an entry
 * for the list of orders in OrdersPanel
 * @author Matthew Frey
 */
public class OrderListEntry extends javax.swing.JPanel {

    //some variables
    /**
     * The ordersPanel that spawned this entry
     */
    private OrdersPanel ordersPanel;

    /** Construct with correct values **/
    public OrderListEntry(Order order, OrdersPanel ordersPanel){
        this.order = order;
        this.ordersPanel = ordersPanel;
        initComponents();
        setAllFields();
        setProgress();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        locationLabel = new javax.swing.JLabel();
        costLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        phoneLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        setBorder(BorderFactory.createMatteBorder(1,0,1,0,Color.BLACK));
        setMaximumSize(new Dimension(9001, 87)); //parent component restricts width

        locationLabel.setText("Order ID: XXX");

        costLabel.setText("Cost: $00.00");

        nameLabel.setText("Customer: John Doe");

        phoneLabel.setText("Phone: (555) 555-5555");

        //progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(locationLabel)
                            .addComponent(costLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 270, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nameLabel)
                            .addComponent(phoneLabel))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationLabel)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(costLabel)
                    .addComponent(phoneLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //add the actionlistener for clicking it
        MouseListener l = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                sendClickEventToOrdersPanel();
            }
        };
        this.addMouseListener(l);
        this.setVisible(true);
    }

    // Variables declaration
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel costLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JProgressBar progressBar;
    private Order order;
    // End of variables declaration

    public void setAllFields(){
        setCostLabel(formatMoney(order.getTotalCost()));
        setNameLabel(order.getCustomer().getName());
        setPhoneLabel(order.getCustomer().getPhoneNumber());
        setLocationLabel(order.getLocation());
    }

    private String formatMoney(double money){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(getLocale());
        return currencyFormatter.format(money);
    }

    public void setCostLabel(String cost) {
        this.costLabel.setText("Cost: " + cost);
    }

    public void setNameLabel(String name) {
        this.nameLabel.setText("Customer: " + name);
    }

    public void setPhoneLabel(String phone) {
        this.phoneLabel.setText("Phone: " + phone);
    }

    public void setLocationLabel(String location) {
        this.locationLabel.setText("Location: " + location);
    }

    public void setProgress(){
        this.progressBar.setMinimum(0);
        this.progressBar.setMaximum(order.getItems().getCount());
        updateProgressBar();
    }

    private void sendClickEventToOrdersPanel(){
        ordersPanel.orderClicked(this);
    }

    public Order getOrder() {
        return order;
    }

    /**
     * number is derived as follows:
     * max value of progress bar = 3 x fooditem count
     * for each food item, the value of the progress bar
     * is +1 for oven
     *    +2 for on delivery
     *    +3 for completed and delivered
     */
    public void updateProgressBar(){
        int completeItemScore = 0;
        for (FoodItem f : order.getFoodItemsList()){
            if (f.getEndPreparationTime() != 0){
                completeItemScore ++;
            }
        }

        this.progressBar.setValue(completeItemScore);

        if (order.completed()){
            progressBar.setMinimum(0);
            progressBar.setMaximum(1);
            progressBar.setValue(1);
        }
    }

    /**
     * Update the fields
     */

    public void updateInfo(){
        setAllFields();
        updateProgressBar();
    }
}