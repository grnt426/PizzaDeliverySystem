package com.teama.pds;

import com.teama.pds.Customer;
import junit.framework.TestCase;

/**
 * User: Jason Greaves
 */
public class CustomerTest extends TestCase {

	public void testCustomerLocationInvalid() {
		Customer c = new Customer();
        try {
		c.setLocation("Over there");
        } catch (Exception e){

        }
		assertNull(c.getLocation());
	}

	public void testCustomerLocationValid() {
		Customer c = new Customer();
        try {
		c.setLocation("RIT");
        } catch (Exception e){

        }
		assertEquals("The location of the customer should be RIT.", "RIT",
				c.getLocation());
	}

	public void testPrintPhoneNumber() {
		Customer c = new Customer();
        try {
		c.setPhoneNumber("1234567890");
        } catch (Exception e){
            
        }
		assertEquals("The formatted phone number of the customer should be" +
				"(123) 456-7890", "(123) 456-7890", c.printPhoneNumber());
	}
}
