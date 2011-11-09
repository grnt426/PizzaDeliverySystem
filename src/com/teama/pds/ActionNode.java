package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * <p/>
 * The module that will act as our Producer-Consumer; consuming input from one
 * or more input queues, and dumping finalized information into output queues,
 * of which future ActionNode's may consume from.
 */
public abstract class ActionNode {

	/**
	 * Holds the time, so that all actions can be time-stamped along the way
	 */
	final TimeReader time;

	/**
	 * Default Constructor
	 *
	 * @param time The time object that will maintain an accurate account of
	 *             the time
	 */
	ActionNode(TimeReader time) {
		this.time = time;
	}

	/**
	 * This method will be called to have this ActionNode perform an action.
	 * This method should never be called more than once, unless all other
	 * action nodes have been called in sequence first (and after). However,
	 * this method can have a loop to consume all resources from its input
	 * queues as needed.
	 */
	public abstract void execute();
}