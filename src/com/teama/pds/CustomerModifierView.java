package com.teama.pds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Auto Generated Form by NetBeans
 *
 * @author Matthew Frey
 */
public class CustomerModifierView extends javax.swing.JFrame {

	// Variables declaration - do not modify
	private JButton cancelButton;
	private JLabel locationLabel;
	private JComboBox locationField;
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JButton okayButton;
	private JLabel phoneLabel;
	private JTextField phoneTextField;

	private Controller controller;
	private Customer customer;
	private String title = "Modify Customer";
	private boolean createMode;
	// End of variables declaration

	/**
	 * Creation constructor
	 */
	public CustomerModifierView(Controller controller) {
		this.controller = controller;
		title = "Create Customer";
		createMode = true;
		initComponents();
	}

	/**
	 * Edit constructor
	 */
	public CustomerModifierView(Controller controller, Customer customer) {
		this.controller = controller;
		title = "Modify customer";
		createMode = false;
		initComponents();
		this.customer = customer;
		loadFieldsFromCustomer(customer);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 */
	@SuppressWarnings("unchecked")
	private void initComponents() {

		okayButton = new JButton();
		cancelButton = new JButton();
		nameLabel = new JLabel();
		phoneLabel = new JLabel();
		locationLabel = new JLabel();
		nameTextField = new JTextField();
		phoneTextField = new JTextField();
		locationField = new JComboBox();
		for (String location : new Customer().getLocationList()) {
			locationField.addItem(location);
		}

		setTitle(title);
		setResizable(false);

		okayButton.setText("Okay");
		okayButton.setMaximumSize(new java.awt.Dimension(75, 32));
		okayButton.setMinimumSize(new java.awt.Dimension(75, 32));
		okayButton.setPreferredSize(new java.awt.Dimension(75, 32));
		okayButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okayButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.setMaximumSize(new java.awt.Dimension(75, 32));
		cancelButton.setMinimumSize(new java.awt.Dimension(75, 32));
		cancelButton.setPreferredSize(new java.awt.Dimension(75, 32));
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		nameLabel.setText("Name:");

		phoneLabel.setText("Phone:");

		locationLabel.setText("Location:");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(nameLabel)
										.addComponent(phoneLabel)
										.addComponent(locationLabel))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
												.addGap(10, 10, 10)
												.addComponent(locationField, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
										.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
												.addGap(10, 10, 10)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
														.addComponent(phoneTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))))
								.addContainerGap())
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap(254, Short.MAX_VALUE)
								.addComponent(cancelButton)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(okayButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(nameLabel)
										.addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(phoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(phoneLabel))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(locationLabel)
										.addComponent(locationField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(okayButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(cancelButton))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		//handle enter key behavior for submit
		KeyListener enterSubmitter = new KeyListener() {
			public void keyTyped(KeyEvent e) {
				//nothing
			}

			public void keyPressed(KeyEvent e) {
				//nothing
			}

			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == 10) {
					submit();
				}
			}
		};
		nameTextField.addKeyListener(enterSubmitter);
		phoneTextField.addKeyListener(enterSubmitter);
		locationField.addKeyListener(enterSubmitter);

		//set dimensions and get dialog centered on screen
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		int width = 408;
		int height = 126;
		this.setLocation((screenDims.width - width) / 2,
				(screenDims.height - height) / 2);

		//override maximize
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
					setExtendedState(JFrame.NORMAL);
				}
			}
		});

		pack();
		setVisible(true);
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void okayButtonActionPerformed(java.awt.event.ActionEvent evt) {
		submit();
	}

	private void submit() {
		boolean success;
		String name, number, locale;

		name = nameTextField.getText();
		number = phoneTextField.getText();
		locale = (String) locationField.getSelectedItem();
		success = checkPhoneNumber(number);
		if(success){
			if (createMode) {
				controller.submitNewCustomer(name, formatPhoneNumber(number),
						locale);
				dispose();
			}
			else {
				controller.deleteCustomer(customer);
				customer.setName(name);
				try {
					customer.setPhoneNumber(formatPhoneNumber(number));
					customer.setLocation(locale);
					controller.addCustomer(customer);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "Incorrect data" +
							" entered");
				}
				finally{
					dispose();
				}
			}
			controller.updateCustomerPanelData();
		}
		else {
			JOptionPane.showMessageDialog(this, "Incorrect phone number" +
					" entered");
		}
	}

	private void loadFieldsFromCustomer(Customer c) {
		nameTextField.setText(c.getName());
		phoneTextField.setText(c.getPhoneNumber());
		locationField.setSelectedItem(c.getLocation());
	}

	private boolean checkPhoneNumber(String number) {
		if (number.length() != 10) {
			String formattedNumber = formatPhoneNumber(number);
			if (formattedNumber.length() != 10) {
				return false;
			}
		}
		return true;
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
}
