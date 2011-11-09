package com.teama.pds;

/**
 * Author: Matthew Frey
 * <p/>
 * A token of authentication singleton
 * Note: This is just a hack for now, Jason.
 * Please feel free to rip it apart.
 */
public class AuthTokenSingleton {

	private static AuthTokenSingleton auth_t_singleton = null;
	private final String username;
	private final char[] password;
	private User.AuthLevel auth_level;

	private AuthTokenSingleton(String username, char[] password, User.AuthLevel auth_level) {
		this.username = username;
		this.password = password;
		this.auth_level = auth_level;
	}


	public static AuthTokenSingleton getSingletonObject(String username,
														char[] password, User.AuthLevel auth_level) {
		if (auth_t_singleton == null) {
			auth_t_singleton = new AuthTokenSingleton(username, password, auth_level);
		}
		return auth_t_singleton;
	}


	public void logout() {
		auth_t_singleton = null;
	}

	//getters

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password.toString();
	}

	public User.AuthLevel getAuthLevel() {
		return auth_level;
	}


}
