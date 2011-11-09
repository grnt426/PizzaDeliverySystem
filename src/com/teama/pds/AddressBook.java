package com.teama.pds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author:      Grant Kurtz
 * Stores all customer information in a convenient wrapper class.
 */
public class AddressBook {

	/**
	 * The primary backing data structure for this class.
	 */
	private final HashMap<String, Customer> records;

	/**
	 * Default Constructor
	 * Just initializes the backing data structure.
	 */
	public AddressBook() {
		records = new HashMap<String, Customer>();
	}

	/**
	 * Adds a single customer to our list of known records.
	 *
	 * @param customer the customer to add
	 */
	public void addCustomer(Customer customer) {
		records.put(customer.getPhoneNumber(), customer);
	}

	/**
	 * Removes a single customer from the list of known records
	 *
	 * @param customer the customer to remove
	 * @return boolean success
	 */
	public boolean deleteCustomer(Customer customer) {
		if (records.remove(customer.getPhoneNumber()) != null) {
			return true;
		} else {
			System.err.println("Tried removing a non-existant customer");
			return false;
		}
	}

	/**
	 * @param phone_number The phone number to look for
	 * @return true if a customer is associated with this phone number,
	 *         otherwise false.
	 */
	public boolean doesRecordExist(String phone_number) {
		return records.containsKey(phone_number);
	}

	/**
	 * @param phone_number The phone number of the customer to retrieve.
	 * @return The customer's record, iff it exists, otherwise null
	 */
	public Customer getRecord(String phone_number) {
		return records.get(phone_number);
	}

	/**
	 * A convenience function for adding a lot of customers at one time.
	 *
	 * @param customers The list of customers to add
	 */
	public void addMultipleAddresses(List<Customer> customers) {
		for (Customer c : customers)
			records.put(c.getPhoneNumber(), c);
	}

	/**
	 * Given a string, this function will find all records that match the first
	 * portion of the search to an equal portion of each customer's phone
	 * number.
	 *
	 * @param partial_search The string to find partial matches for
	 * @return All partially matching records.
	 */
	public HashMap<String, Customer> getPartialMatches(String partial_search) {
		HashMap<String, Customer> matched_customers =
				new HashMap<String, Customer>();
		for (String phone_number : records.keySet()) {
			if (phone_number.substring(0,
					partial_search.length()).equals(partial_search)) {
				matched_customers.put(phone_number, records.get(phone_number));
			} else if (records.get(phone_number).getName().substring(0,
					partial_search.length()).equals(partial_search)) {
				matched_customers.put(phone_number, records.get(phone_number));
			}
		}
		return matched_customers;
	}

	/**
	 * @return A string that contains all the customers that are stored in this
	 *         address book in a convenient Human Readable form.
	 */
	public String toString() {
		String output = "========================================\n";
		output += "\t\t\tAddress Book\n";
		output += "========================================\n";
		if (records.isEmpty())
			return (output + "\tNo Customer Records Found");
		for (Customer c : records.values()) {
			output += "\t" + c.getPhoneNumber() + "\n\t\t" + c.getName() +
					"\n\t\t" + c.getLocation() + "\n";
		}
		return output;
	}

	/**
	 * Returns an ArrayList of all Customers in the address book
	 *
	 * @return An ArrayList of all Customers in the address book
	 */
	public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> result = new ArrayList<Customer>();
		for (Customer c : records.values()) {
			result.add(c);
		}
		return result;
	}


}
