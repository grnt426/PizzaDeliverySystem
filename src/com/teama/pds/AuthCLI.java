package com.teama.pds;

import java.io.Console;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Command line Interface for the authentication
 */
public class AuthCLI {

	private Controller c;
	private Console console;
	private Scanner scan;

	public AuthCLI(Controller c) {
		scan = new Scanner(System.in);
		this.c = c;
		auth();

	}

	/**
	 * Runs check for authentication
	 */
	private void auth() {
		boolean yes = false;
		while (!yes) {
			System.out.println("Type in your Username and password. Type exit in either" +
					" to close the application.");
			System.out.print("Username: ");
			String username = scan.nextLine();
			if (username.toLowerCase().equals("exit")) {
				confirmExit();
			}
			char[] password = null;
			if (console != null) {
				System.out.print("Password: ");
				password = console.readPassword();
			} else {
				//System.out.println("No Console.");
				System.out.print("Password: ");
				password = scan.nextLine().toCharArray();
			}
			String pass_check = new String(password);
			if (pass_check.toLowerCase().equals("exit")) {
				confirmExit();
			}
			if (c.login(username, password)) {
				c.runCLI();
				yes = true;
			} else {
				System.out.println("Incorrect credentials. Please try again.");
			}
		}

	}

	/**
	 * Used to ask user if they really want to exit the program. If yes, program exits.
	 */
	private void confirmExit() {
		String answer = null;
		boolean ask = true;
		while (ask) {
			System.out.println("Are you sure you want to Exit? Type yes or no. ");
			answer = scan.nextLine();
			if (answer.toLowerCase().equals("yes")) {
				System.exit(0);
			} else if (answer.toLowerCase().equals("no")) {
				ask = false;
				auth();
			} else {
				System.out.println("I didn't understand that, let me ask again.");
			}
		}

	}
}
