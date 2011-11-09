package com.teama.pds;

import com.teama.pds.Record;
import junit.framework.TestCase;

/**
 * Author:      Grant Kurtz
 */
public class RecordTest extends TestCase {

	public void testToString() throws Exception {
		Record r = new Record("This is a test", Record.Status.TRANSITION,
				15);
		String expected = "( -> ) This is a test";
		assertEquals("The to string method should return a properly " +
				"formatted message.", expected, r.toString());
	}
}
