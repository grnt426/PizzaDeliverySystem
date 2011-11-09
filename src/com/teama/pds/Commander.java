package com.teama.pds;

import java.util.LinkedList;

/**
 * Author:      Grant Kurtz
 * Maintains the requirement of calling each ActionNode in sequence,
 * guaranteeing that the ActionNode is only executed once, in FIFO order.
 */
public class Commander {

	/**
	 * The (FIFO) ordered list of actions to perform
	 */
	private final LinkedList<ActionNode> actions;

	/**
	 * Maintains the singleton requirement of this class.
	 */
	private static Commander commander;

	/**
	 * Default Constructor
	 * <p/>
	 * Just creates the needed LinkedList so that actions can be added to our
	 * command list
	 */
	private Commander() {
		actions = new LinkedList<ActionNode>();
	}

	/**
	 * This method may only be called exactly once.
	 *
	 * @return returns a newly created singleton Commander Object, otherwise
	 *         will returned an already previously created Commander Object.
	 * @throws AlreadyInstantiatedException This is thrown if this method has
	 *                                      already been called once before.
	 */
	public static Commander createCommander()
			throws AlreadyInstantiatedException {
		if (commander != null)
			throw new AlreadyInstantiatedException("The Commander class has " +
					"already been instantiated.");
		else
			return (commander = new Commander());
	}

	/**
	 * Note: You MUST call createCommander before you can call this method.
	 *
	 * @throws NotInstantiatedException This is thrown if createCommander was
	 *                                  not called.
	 * @return The already instantiated Commander instance.
	 */
	public Commander getCommander() throws NotInstantiatedException {
		if (commander == null)
			throw new NotInstantiatedException("The Commander class has not " +
					"been instantiated.");
		else
			return commander;
	}

	/**
	 * The passed action is placed at the end of our command list for
	 * processing.
	 *
	 * @param action the new action to add to the end of our command list
	 */
	public void addAction(ActionNode action) {
		actions.add(action);
	}

	/**
	 * Causes all the current actions to perform their respective roles once,
	 * in (FIFO) order.
	 */
	public void run() {
		for (ActionNode an : actions)
			an.execute();
	}
}
