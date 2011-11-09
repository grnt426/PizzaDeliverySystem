package com.teama.pds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Author:      Grant Kurtz
 * Is used as a logger for all events performed by the system.
 */
public class History {

	/**
	 * All events performed by the system, grouped into partitions of seconds.
	 */
	private final HashMap<Integer, ArrayList<Record>> history;

	/**
	 * The time code of the last event that was logged.
	 */
	private int last_logged_second;

	/**
	 * Default Constructor TODO: improve this description
	 * Just initializes the HashMap for use
	 */
	public History() {
		history = new HashMap<Integer, ArrayList<Record>>();
		last_logged_second = 0;
	}

	/**
	 * Adds a record to the logger and groups it along with other events in the
	 * same second.
	 * TODO: catch nulls
	 *
	 * @param record The event to add
	 */
	public void addEvent(Record record) {

		// Add a record to the history partition
		if (history.containsKey(record.getTimeStamp()))
			history.get(record.getTimeStamp()).add(record);

			// create a new record array
		else {
			ArrayList<Record> new_records = new ArrayList<Record>();
			new_records.add(record);
			history.put(record.getTimeStamp(), new_records);
		}

		// update as needed
		if (record.getTimeStamp() > last_logged_second) {
			last_logged_second = record.getTimeStamp();
		}
	}

	/**
	 * @return true if there are no records, otherwise false.
	 */
	public boolean isEmpty() {
		return history.isEmpty();
	}

	/**
	 * @return The number of seconds that have events.
	 */
	int getTotalLoggedCount() {
		return history.size();
	}

	/**
	 * @return The total number of events throughout the whole program.
	 */
	public int getTotalEventCount() {
		int count = 0;
		for (ArrayList<Record> records : history.values()) {
			count += records.size();
		}
		return count;
	}

	/**
	 * @param time_code The time code, denoted in seconds, to do the check
	 *                  at.
	 * @return true if, at that point in time, an event was logged,
	 * otherwise false.
	 */
	public boolean hasEventAt(int time_code) {
		return history.containsKey(time_code);
	}

	/**
	 * @param time_code The time code, in seconds, to get all events at.
	 * @return A list of all events that occurred at the specified
	 * time.
	 */
	public ArrayList<Record> getRecordsAt(int time_code) {
		ArrayList<Record> recordArrayList = history.get(time_code);
		return recordArrayList == null ? new ArrayList<Record>() : recordArrayList;
	}

	/**
	 * @return The last time, in seconds, that any events were recorded at.
	 */
	public int getLastRecordedEventTime() {
		return last_logged_second;
	}

	/**
	 * @return A list of events that were recorded at the most recent second.
	 */
	public ArrayList<Record> getLastRecordedEvents() {
		return getRecordsAt(last_logged_second);
	}

	/**
	 * @return Gets the absolute last element added into this class.
	 * TODO:fix chance of out of bounds&null
	 */
	public Record getLastRecordedEvent() {
		ArrayList<Record> records = getLastRecordedEvents();
		return records.get(records.size() - 1);
	}

	/**
	 * @return The whole history of the program.
	 */
	public String printEntireHistory() {
		return printEventRange(0, last_logged_second);
	}

	/**
	 * Prints just a given range of all events during a program's execution,
	 * inclusively.
	 * TODO: String Builder! also, is this text output(header) necessary?
	 *
	 * @param start The start of the time range to print events
	 * @param end   The end of the time range to print events
	 * @return The string representing all the logged events that happened.
	 */
	String printEventRange(int start, int end) {
		String msg;
		msg = "=========================================\n";
		msg += "\t\t\t\tHistory\n";
		msg += "=========================================\n";

		// let the user know there are no events
		if (getTotalLoggedCount() == 0)
			return msg + "\tNo Recorded Events Available.";

		// otherwise just print them all out
		for (int i = start; i <= end; i++) {
			ArrayList<Record> records = history.get(i);
			if (records == null)
				continue;
			msg += "\t" + records.get(0).getTimeStamp() + "\n";
			for (Record r : records) {
				msg += "\t\t" + r + "\n";
			}
			msg += "\n";
		}
		msg = msg + "\n" + "Current time: ";
		return msg;
	}

	/**
	 * Prints all events from the given start time to the latest event.
	 *
	 * @param start The start of the range of events to print from
	 * @return A string representing all events that happened in the range
	 */
	public String printEventSince(int start) {
		return printEventRange(start, last_logged_second);
	}

	/**
	 * @return An Array List of Records
	 */
	public Vector<Record> getRecordArray() {
		Vector<Record> results = new Vector<Record>();
		for (int i = 0; i <= last_logged_second; i++) {
			ArrayList<Record> records = history.get(i);
			if (records != null) {
				results.addAll(records);
			}
		}
		return results;
	}

	/**
	 * @return The entire history of all events as a string.
	 */
	public String toString() {
		return printEntireHistory();
	}
}
