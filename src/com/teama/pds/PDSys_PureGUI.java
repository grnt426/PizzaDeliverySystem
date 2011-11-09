package com.teama.pds;

/**
 * @author Matthew Frey
 *         This class will be the main class for running the program
 *         with absolutely no CLI. I needed to make this class so I
 *         can work on the GUI while you guys use the CLI to provide
 *         backend work. This class will probably be renamed to just
 *         PDSys eventually, and the old will be deleted.
 */
public class PDSys_PureGUI {
	public static void main(String[] args) {
		Model m = new Model();
		Controller c = new Controller(m);
		AuthView av = new AuthView(c);
		View_GUI vgui = new View_GUI(c);
		c.runGUI();
	}
}
