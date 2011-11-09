package com.teama.pds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Author:      Grant Kurtz
 */
public class UserModifierView extends JFrame{

	// Variables declaration - do not modify
	private JButton cancelButton;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JLabel nameLabel;
	private JTextField nameTextField;
	private JButton okayButton;
	private JLabel usernameLabel;
	private JTextField usernameTextField;

	private Controller controller;
	private Customer customer;
	private String title = "Modify User";
	private boolean createMode;
	private JCheckBox isManagement;
	private JLabel isManagementLabel;
	// End of variables declaration

	public UserModifierView(Controller controller){
		this.controller = controller;
		title = "Create User";
		createMode = true;
		initComponents();
	}

	public UserModifierView(Controller controller, User selectedUser) {
		this.controller = controller;
		title = "Modify User";
		createMode = false;
		initComponents();
		loadFieldsFromUser(selectedUser);
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
		usernameLabel = new JLabel();
		passwordLabel = new JLabel();
		nameTextField = new JTextField();
		usernameTextField = new JTextField();
		passwordField = new JPasswordField();
		isManagementLabel = new JLabel();
		isManagement = new JCheckBox();

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

		usernameLabel.setText("Username:");

		passwordLabel.setText("Password:");

		isManagementLabel.setText("Management? ");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(nameLabel)
										.addComponent(usernameLabel)
										.addComponent(passwordLabel)
										.addComponent(isManagementLabel))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
												.addGap(10, 10, 10)
												.addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
										.addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
												.addGap(10, 10, 10)
												.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
														.addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
														.addComponent(isManagement, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))))
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
										.addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(usernameLabel))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(passwordLabel)
										.addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(isManagementLabel)
										.addComponent(isManagement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
		usernameTextField.addKeyListener(enterSubmitter);
		passwordField.addKeyListener(enterSubmitter);

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
		boolean exists;
		User.AuthLevel auth_level;
		String name, username, password;

		name = nameTextField.getText();
		username = usernameTextField.getText();
		password = String.valueOf(passwordField.getPassword());
		auth_level = isManagement.isSelected() ? User.AuthLevel.MANAGER :
				User.AuthLevel.CASHIER;
		if (createMode) {
			if(!checkAlreadyExisting(username)) {
				controller.submitNewUser(name, username, password, auth_level);
				dispose();
			}
			else{
				JOptionPane.showMessageDialog(this, "User already exists!");
			}
		}
		else {
			String cur_user =
					controller.getCurrentlyLoggedInUser().getUsername();
			if(!cur_user.equals(username)){
				controller.deleteUser(username);
				controller.submitNewUser(name, username, password,
						auth_level);
				dispose();
			}
			else {
				JOptionPane.showMessageDialog(this, "Can not modify" +
						" a user that is currently logged in!");
			}
		}
		controller.updateCustomerPanelData();
	}

	private boolean checkAlreadyExisting(String username) {
		for(User user : controller.getUsers()){
			if(user.getUsername().equals(username))
				return true;
		}
		return false;
	}

	private void loadFieldsFromUser(User u) {
		nameTextField.setText(u.getNameOfUser());
		usernameTextField.setText(u.getUsername());
		passwordField.setText(u.getPassword().toString());
		isManagement.setSelected(u.getAuthLevel() == User.AuthLevel.MANAGER);
	}
}
