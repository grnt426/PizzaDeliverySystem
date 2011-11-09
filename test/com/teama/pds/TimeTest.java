package com.teama.pds;

import com.teama.pds.Time;
import junit.framework.TestCase;

/**
 * Author:      Grant Kurtz
 *
 * Note, the below methods can not be run all at once, as Time can only be
 * created once, so they have to be run individually.
 */
public class TimeTest extends TestCase {

	public void setUp() throws Exception {
		super.setUp();

		// we need to do the below incase in other tests someone else had
		// initialized Time and used it.
		Time time = Time.createTime();
		time.setCurTime(0);
	}

	public void testGetCurTime() throws Exception {
		Time time = Time.createTime();
		assertEquals("The current time should be 0.", 0, time.getCurTime());
		time.setCurTime(0);
	}

	public void testCreateTime() throws Exception {
		Time time = Time.createTime();
		assertNotNull("The time object should not be null.", time);
		time.setCurTime(0);
	}

	public void testCreateTimeTwice() throws Exception {
		Time time = Time.createTime();
		Time time2 = Time.createTime();
		assertSame("Time can not be created twice.", time, time2);
		time.setCurTime(0);
	}

	public void testGetCurTimeAfterIncrement() throws Exception {
		Time time = Time.createTime();
		time.incrementTime();
		assertEquals("The current time should be 1 after incrementing.", 1,
				time.getCurTime());
		time.setCurTime(0);
	}

	public void testGetCurTimeAfterTwoIncrements() throws Exception {
		Time time = Time.createTime();
		time.incrementTime();
		time.incrementTime();
		assertEquals("The current time should be 2 after incrementing twice",
				2, time.getCurTime());
		time.setCurTime(0);
	}
}
