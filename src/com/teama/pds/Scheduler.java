package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * Manages all ResourceLists in the program, and will alter the availability
 * of Resources so as to schedule all events for maximum turn-around time of
 * Orders.
 */
public class Scheduler {


	/**
	 * The instance object that will hold all the resource lists.
	 */
	private final OverSeer over_seer;

	/**
	 * Default Constructor.
	 * <p/>
	 * Takes in the OverSeer for use in reading the ResourceLists made available
	 * by the Model.
	 */
	public Scheduler(OverSeer overseer) {
		this.over_seer = overseer;
	}
}
