package com.teama.pds;

/**
 * Author:      Grant Kurtz
 */
public class Fryer extends Resource {

	public Fryer(TimeReader time) {
		super(time);
	}

	public boolean isAvailable() {
		return false;
	}

	public int compareTo(Resource o) {
		return 0;
	}
}
