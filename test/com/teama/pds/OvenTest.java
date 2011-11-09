package com.teama.pds;

import com.teama.pds.*;
import junit.framework.TestCase;

/**
 * Author:      Grant Kurtz
 */
public class OvenTest extends TestCase {

	private TimeReader tr;
	private Time t;
	private History h;

	public void testGetOvenSpaceWhenZero() throws Exception {
		Oven oven = new Oven(0, tr);
		oven.setLogger(h);
		assertEquals("The oven should not have a max size.", 0,
				oven.getOvenSpace());
	}

	public void testGetOvenSpace() throws Exception {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		assertEquals("The oven should be size 10", 10, oven.getOvenSpace());
	}

	public void testHasSpaceFor() throws Exception {
		Oven oven = new Oven(10, tr);
		assertTrue(oven.hasSpaceFor(new CookedItem(5,5, tr)));
	}

	public void testAddItem() throws Exception {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		try{
			oven.addItem(new CookedItem(5,5, tr));
		}
		catch (NotEnoughSpaceException nese){
			throw new Exception("There should have been enough space in the " +
					"oven.");
		}
	}

	public void testAddItemLargerThanOven() throws Exception {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		try{
			oven.addItem(new CookedItem(11,5, tr));
			throw new Exception("A NotEnoughSpaceException should have been " +
					"thrown.");
		}
		catch(NotEnoughSpaceException nese){
			// normal operation is to reach here
		}
	}

	public void testFillOvenPerfectly() throws Exception {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		try{
			oven.addItem(new CookedItem(9,5, tr));
			oven.addItem(new CookedItem(1,5, tr));
		}
		catch(NotEnoughSpaceException nese){
			throw new Exception("There should have been one space left for a " +
					"single space item.");
		}
	}

	public void testAddItemWhenOneSpaceLeft() throws Exception {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		try{
			oven.addItem(new CookedItem(9,5, tr));
			oven.addItem(new CookedItem(2,5, tr));
			throw new Exception("A NotEnoughSpaceException should have been" +
					"thrown.");
		}
		catch (NotEnoughSpaceException nese){
			// normal operation is to reach here
		}
	}

	public void testRemoveItemWhenEmpty() {
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		oven.removeItem();
	}

	public void testCookAnItem() throws Exception{
		t.setCurTime(0);
		CookedItem ci = new CookedItem("test", 0.0, 5, 4, 100, tr);
		Oven oven = new Oven(10, tr);
		oven.setLogger(h);
		oven.addItem(ci);
		t.addSeconds(50);
		ci = new CookedItem("test", 0.0, 5, 3, 150, tr);
		oven.addItem(ci);
		ci = new CookedItem("test", 0.0, 5, 3, 51, tr);
		oven.addItem(ci);
		t.addSeconds(49);

		// alright, nothing should be available yet
		oven.isAvailable();
		assertEquals("There should be 0 free space left.", 0,
				oven.getTotalFreeSpace());
		t.incrementTime();
		oven.isAvailable();
		assertEquals("There should be 4 space left.", 4,
				oven.getTotalFreeSpace());
		t.incrementTime();
		oven.isAvailable();
		assertEquals("There should be 7 space left.", 7,
				oven.getTotalFreeSpace());
		t.addSeconds(99);
		oven.isAvailable();
		assertEquals("There should be 10 space left.", 10,
				oven.getTotalFreeSpace());
		t.setCurTime(0);
	}

	public void setUp() throws Exception {
		super.setUp();
		t = Time.createTime();
		tr = new TimeReader(t);
		h = new History();
	}
}
