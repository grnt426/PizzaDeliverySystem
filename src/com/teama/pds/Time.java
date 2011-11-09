package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * An Singleton Entity class that maintains the current time for the entire
 * simulation.  This class can always be relied upon to return the most accurate
 * time. All values given to or returned are treated as seconds.
 */
public class Time {

	/**
	 * The int that actually stores the current time of the simulation
	 */
	private int cur_time;

	/**
	 * Used to maintain our Singleton requirement
	 */
	private static Time time;

	/**
	 * Default Constructor
	 */
	private Time() {
		cur_time = 0;
	}

	/**
	 * Used to guard from double-initialization.
	 *
	 * @return A newly created Time instance, or a previously generated one.
	 */
	public static Time createTime() {
		if (time != null)
			return time;
		else
			return (time = new Time());
	}

	/**
	 * Increments the time of the simulation by a single second.
	 */
	public void incrementTime() {
		cur_time++;
	}

	/**
	 * @return The current time of the simulation, in seconds.
	 */
	public int getCurTime() {
		return cur_time;
	}

	/**
	 * WARNING: This method is INCREDIBLY dangerous to call.  Using this under
	 * ANY situation outside of explicit unit testing may result in HIGHLY
	 * UNPREDICTABLE program behavior.
	 *
	 * @param cur_time forcibly set the time to a different value.
	 */
	public void setCurTime(int cur_time) {
		this.cur_time = cur_time;
	}

	/**
	 * WARNING: This method is rather DANGEROUS to call.  Using this under any
	 * situation outside of explicit unit testing may result in HIGHLY
	 * UNPREDICTABLE program behavior.
	 * This method will add an absolute value of seconds to the current time of
	 * the simulation.
	 *
	 * @param sec The number of seconds to add to the current time.
	 */
	public void addSeconds(int sec) {
		cur_time += Math.abs(sec);
	}
}
