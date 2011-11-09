package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * A base entity class that is the backbone of the producer/consumer model of
 * this whole project.  This class is used heavily by the ResourceList class.
 */
public abstract class Resource implements Comparable<Resource> {

	/**
	 * When this resource will be available. Note, this may not be the only
	 * conditions that determine a resource's availability, but it should be an
	 * accurate measurement.
	 */
	private int avail_time;

	public void setTimeReader(TimeReader time) {
		this.time = time;
	}

	/**
	 * The time object that will report back accurate time information on the
	 * simulation.
	 */
	TimeReader time;

	/**
	 * The logger that is used for keeping track of all events this resource
	 * processes.
	 */
	private History logger;

	/**
	 * Default Constructor
	 *
	 * @param time The time object that will report back accurate time
	 *             information of the simulation.
	 */
	Resource(TimeReader time) {
		this.time = time;
		this.avail_time = 0;
	}

	/**
	 * Non-Default Constructor
	 * Used for Unit Testing, or giving the resource some pre-determined time
	 * availability.  This will have more use once a Scheduler is created.
	 *
	 * @param avail_time The preset time this resource will be available.
	 * @param time	   The time object that will report accurate time
	 *                   information of the simulation.
	 */
	public Resource(int avail_time, TimeReader time) {
		this.time = time;
		this.avail_time = avail_time;
	}

	/**
	 * @param future_time When this resource is going to be available.
	 */
	public void setAvailTime(int future_time) {
		avail_time = future_time;
	}

	/**
	 * @return The time this resource will be available at.
	 */
	public int getAvailTime() {
		return avail_time;
	}

	/**
	 * @return true if this resource can be used for a period of time,
	 * otherwise if this resource can not be used false should be
	 * returned. Note, this method does not necessarily have to solely
	 * depend on the avail_time value, but it should not deviate too
	 * far from it.
	 */
	public abstract boolean isAvailable();

	/**
	 * @param logger The logger that will be used for logging all events this
	 *               resource may act out.
	 */
	public void setLogger(History logger) {
		this.logger = logger;
	}

	/**
	 * @return The object this resource is using to report all its
	 * events
	 */
	History getLogger() {
		return logger;
	}
}
