package com.teama.pds;

import com.teama.pds.AddressBook;
import com.teama.pds.Customer;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;

/**
 * User: Jasong
 */
public class AddressBookTest extends TestCase {

	private AddressBook a_b;
	private Customer a;


	@Before
	public void setUp() throws Exception {
		a_b = new AddressBook();
		a = new Customer();
		Customer b = new Customer();
		Customer c = new Customer();
		a.setName("Jeff");
		a.setLocation("RIT");
		a.setPhoneNumber("7175556666");
		b.setName("Mark");
		b.setLocation("University of Rochester");
		b.setPhoneNumber("2153336666");
		c.setName("Mary");
		c.setLocation("St. John Fisher");
		c.setPhoneNumber("1234567890");
		a_b.addCustomer(c);
	}

	public void testAddNewCustomer() {
		a_b.addCustomer(a);
		assertEquals("The customer data for Customer 'a' should be added " +
				"without problem", a, a_b.getRecord("7175556666"));
	}

	public void testFindCustomer() {
		assertTrue("Customer c should have been added in the set up",
				a_b.doesRecordExist("1234567890"));
		a_b.addCustomer(a);
		assertTrue("Customer a has been added recently.",
				a_b.doesRecordExist("7175556666"));
	}

	public void testChangeStoreCustomerName() {
		a_b.getRecord("1234567890").setName("Mary Birch");
		assertEquals("Customer c's name should be changed or modified without" +
				" problem", "Mary Birch",
				a_b.getRecord("1234567890").getName());
	}

	public void testAddListOfCustomers(){

        try {
        ArrayList<Customer> custs = new ArrayList<Customer>();

		// create some test data
		Customer d = new Customer();
		d.setName("Drake");
		d.setPhoneNumber("123");
		Customer e = new Customer();
		e.setName("Eric");
		e.setPhoneNumber("456");
		Customer f = new Customer();
		f.setName("Frank");
		f.setPhoneNumber("789");
        custs.add(d);
		custs.add(e);
		custs.add(f);
        // add them
		a_b.addMultipleAddresses(custs);

		// make sure they were added
		assertEquals("Customer d should exist.", d,
				a_b.getRecord(d.getPhoneNumber()));
		assertEquals("Customer e should exist.", e,
				a_b.getRecord(e.getPhoneNumber()));
		assertEquals("Customer f should exist.", f,
				a_b.getRecord(f.getPhoneNumber()));
        } catch (Exception ex){
            System.err.println("incorrect test data");
        }



	}
}
