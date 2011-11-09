package com.teama.pds;

import java.util.Arrays;

/**
 * Author: Jason Greaves
 * <p/>
 * The User class is for separate users and stores their credentials like their username,
 * password, Full name, and authentication level
 */
public class User {

	private String username;
	//private String password;
	private char[] password;
	private String name_of_user;
	private AuthLevel auth_level;

	public enum AuthLevel {
		MANAGER,
		CASHIER
	}

	public User(String username, String password, String name_of_user, AuthLevel auth_level) {
		this.username = username;
		this.password = password.toCharArray();
		this.name_of_user = name_of_user;
		this.auth_level = auth_level;
	}

	/**
	 * @return username of the User
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return User's password
	 */
	public char[] getPassword() {
		return password;
	}

	/**
	 * @return User's full name
	 */
	public String getNameOfUser() {
		return name_of_user;
	}

	/**
	 * @return User's authentication Level.
	 */
	public AuthLevel getAuthLevel() {
		return auth_level;
	}

	/**
	 * checks to see if username and password given match the user's
	 *
	 * @param inName username given
	 * @param inPass password given
	 * @return if credentials given were correct return true. If not false.
	 */
	public boolean checkAuth(String inName, char[] inPass) {
		if (username.equals(inName) && Arrays.equals(inPass, password)) {
			return true;
		} else {
			return false;
		}

	}
}
