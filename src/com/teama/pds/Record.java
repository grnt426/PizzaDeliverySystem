package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * <p/>
 * An Entity class for storing an event that happened in the simulation.
 */
public class Record {

	/**
	 * Used for standardizing the icons that are printed alongside messages.
	 */
	public enum Status {
		TRANSITION,
		WORKING,
		SLOWED,
		ERROR
	}

	/**
	 * The event message that describes what happened.
	 */
	private final String msg;

	/**
	 * Whether or not what happened is normal, a delayed operation (ie. finding
	 * out the oven is full and can't put the pizza in for cooking), or an
	 * error.
	 */
	private final Status status;

	/**
	 * The time that the event was recorded at.
	 */
	private final int time_stamp;

	/**
	 * Default Constructor
	 *
	 * @param msg		The event message that describes what happened
	 * @param status	 The type of event that happened.
	 * @param time_stamp The time of the event
	 */
	public Record(String msg, Status status, int time_stamp) {
		this.msg = msg;
		this.status = status;
		this.time_stamp = time_stamp;
	}

	/**
	 * @return The string that describes what happened
	 */
	String getMsg() {
		return msg;
	}

	/**
	 * @return The type of event that occurred.
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @return The time of the event
	 */
	public int getTimeStamp() {
		return time_stamp;
	}

	/**
	 * @return converts the event status code into a standardized icon.
	 */
	String getIcon() {
		String icon;
		switch (status) {
			case TRANSITION:
				icon = "->";
				break;
			case SLOWED:
				icon = "~!";
				break;
			case WORKING:
				icon = "~~";
				break;
			case ERROR:
				icon = "!!";
				break;
			default:
				icon = "";
		}
		return icon;
	}

	/**
	 * @return A human readable version of what happened.
	 */
	public String toString() {
		return "( " + getIcon() + " ) " + getMsg() + " [at TC:" + getTimeStamp() + "]";
	}
}
