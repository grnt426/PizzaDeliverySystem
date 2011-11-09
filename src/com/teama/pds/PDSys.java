package com.teama.pds;

/**
 * Author: Matthew Frey
 * <p/>
 * This is the main class for starting the system
 */
public class PDSys {

	public static void main(String[] args) {
		//AuthView a = new AuthView();
		Model m = new Model();
		View_CLI v = new View_CLI();
		Controller c = new Controller(m, v);
		AuthCLI a = new AuthCLI(c);
	}


}
