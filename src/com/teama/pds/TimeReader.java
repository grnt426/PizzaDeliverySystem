package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * This Entity class is just used as a separation of privileges, so as to avoid
 * a chef accidentally altering the time without given the rest of the
 * simulation a chance to update. Just like in the Time class, all values
 * given to or returned from this class are reported in seconds.
 */
public class TimeReader {

	/**
	 * The actual class that maintains the time of the simulation.
	 */
	private final Time time;

	/**
	 * @param time The Time object that maintains the real time of the
	 *             simulation
	 */
	public TimeReader(Time time) {
		this.time = time;
	}

	/**
	 * @return The current time of the simulation, in seconds.
	 */
	public int getCurrentTime() {
		return time.getCurTime();
	}
}
