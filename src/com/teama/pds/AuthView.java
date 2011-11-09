package com.teama.pds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Matthew Frey,
 *         <p/>
 *         This will be a short-lived frame for handling authentication.
 */
public class AuthView {

	private Controller controller;
	private boolean failMode = false;
	//Some instance Variables for access by methods
	private JTextField usernameTextField;
	private JPasswordField passwordField;
	private JFrame authFrame;
	public boolean visible = false;

	/**
	 * The constructor for the AuthView
	 */
	public AuthView(Controller controller) {
		//set the look and feel to our imported library "seaglass"
		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
			System.err.println("Failed to load Seaglass Look and Feel");
			System.err.println(e);
			failMode = true;
		}

		//initialize the frame
		initFrame();
		controller.assignAuthView(this);
		this.controller = controller;
	}

	public void SetController(Controller con) {
		this.controller = con;
	}

	/**
	 * Initializes the auth window
	 */
	private void initFrame() {
		//initialize our individual components
		authFrame = new JFrame();
		usernameTextField = new JTextField(10);
		passwordField = new JPasswordField(10);

		JLabel usernameLabel = new JLabel("Username: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JButton submitButton = new JButton("Submit");
		JButton cancelButton = new JButton("Cancel");
		JPanel mainPanel = new BackgroundPanel("img/auth-bg.jpg");
		JPanel buttonPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel usernamePanel = new JPanel();
		JPanel passwordPanel = new JPanel();
		Font labelFont = new Font("Verdana", Font.BOLD, 14);
		Font buttonFont = new Font("Verdana", Font.PLAIN, 12);

		//configure our actionlistener for submitting auth details
		ActionListener submitAuth = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttemptAuth();
			}
		};

		KeyListener submitAuthWithEnter = new KeyListener() {
			public void keyTyped(KeyEvent e) {
				//nothing
			}

			public void keyPressed(KeyEvent e) {
				//nothing
			}

			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == 10) {
					AttemptAuth();
				}
			}
		};

		//configure the components-------------------------------------

		//usernameLabel
		usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		usernameLabel.setForeground(Color.WHITE);
		usernameLabel.setFont(labelFont);

		//passwordLabel
		passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
		passwordLabel.setForeground(Color.WHITE);
		passwordLabel.setFont(labelFont);

		//usernameTextField
		usernameTextField.setToolTipText("Enter your username");
		usernameTextField.setColumns(20);
		usernameTextField.addKeyListener(submitAuthWithEnter);

		//passwordTextField
		passwordField.setToolTipText("Enter your password");
		passwordField.setColumns(20);
		passwordField.addKeyListener(submitAuthWithEnter);

		//submitButton
		submitButton.setFont(buttonFont);
		submitButton.addActionListener(submitAuth);
		submitButton.addKeyListener(submitAuthWithEnter);


		//cancelButton
		cancelButton.setFont(buttonFont);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.abandonShip();
			}
		});

		//usernamePanel
		usernamePanel.setLayout(new FlowLayout());
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameTextField);

		//passwordPanel
		passwordPanel.setLayout(new FlowLayout());
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);

		//buttonPanel
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(submitButton);
		buttonPanel.add(cancelButton);

		//centerPanel
		centerPanel.add(usernamePanel, BorderLayout.NORTH);
		centerPanel.add(passwordPanel, BorderLayout.SOUTH);

		//mainPanel
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setSize(500, 200);
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		//authFrame
		authFrame.setBackground(new Color(100, 2, 100));
		authFrame.add(mainPanel);
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		authFrame.setTitle("Login to A-Team PDS");
		int width = 450;
		int height = 150;
		authFrame.setSize(new Dimension(width, height));
		authFrame.setLocation((screenDims.width - width) / 2,
				(screenDims.height - height) / 2);
		authFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		authFrame.setMaximumSize(new Dimension(450, 150));
		authFrame.setResizable(false);
		//remove maximize functionality (because Windows likes to make my life harder)
		authFrame.addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				if (e.getNewState() == JFrame.MAXIMIZED_BOTH) {
					authFrame.setExtendedState(JFrame.NORMAL);
				}
			}
		});
		authFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.abandonShip();
			}
		});

		//end component configuration----------------------------------

		//configure fail mode readability
		if (failMode) {
			usernameLabel.setForeground(Color.BLACK);
			passwordLabel.setForeground(Color.BLACK);
		}

		//fire it up
		//authFrame.setVisible(true);
	}

	/**
	 * Authenticates the user credentials
	 * If successful, moves on to View_GUI
	 */
	private void AttemptAuth() {
		String username = usernameTextField.getText();
		char[] password = passwordField.getPassword();
		if (controller.login(username, password)) {
			//the model should now have an authtoken
			correctAuth();
			startWithAuth();
		} else {
			failedAuth();
		}
	}

	/**
	 * Starts up the full system with an authentication level
	 */
	private void startWithAuth() {
		controller.swapMode();
	}

	/**
	 * colors text fields red to let users know they are incorrect
	 */
	private void failedAuth() {
		usernameTextField.setText("");
		usernameTextField.setBackground(Color.PINK);
		passwordField.setText("");
		passwordField.setBackground(Color.PINK);
		usernameTextField.requestFocus();

		//dev assistance
		//TODO remove before release
		usernameTextField.setText("manager");
		passwordField.setText("manage123");
	}

	/**
	 * resets authView to normal so our return isn't greeted
	 * with a persistent set of login credentials
	 */
	private void correctAuth() {
		usernameTextField.setText("");
		usernameTextField.setBackground(Color.WHITE);
		passwordField.setText("");
		passwordField.setBackground(Color.WHITE);
		usernameTextField.requestFocus();
	}

	/**
	 * Hides/Shows the authView
	 */
	public void toggleVisibility() {
		visible ^= true;
		authFrame.setVisible(visible);
	}

	/**
	 * disposes the authview
	 */
	public void dispose() {
		authFrame.dispose();
	}
}
