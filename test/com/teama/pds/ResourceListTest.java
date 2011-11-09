package com.teama.pds;

import com.teama.pds.FoodItem;
import com.teama.pds.ResourceList;
import com.teama.pds.Time;
import com.teama.pds.TimeReader;
import junit.framework.TestCase;

/**
 * Author:      Grant Kurtz
 */
public class ResourceListTest extends TestCase {

	private ResourceList<FoodItem> resources;
	private TimeReader tr;

	public void testGetCountWhenEmpty(){
		assertEquals("There should be no items in the list.", 0,
				resources.getCount());
	}

	public void testGetCountWithOne(){
		resources.addResource(new FoodItem("", 0, 0, tr));
		assertEquals("There should one item in the list.", 1,
				resources.getCount());
	}

	public void testGetCountWithTwo(){
		resources.addResource(new FoodItem("", 0, 0, tr));
		resources.addResource(new FoodItem("", 0, 0, tr));
		assertEquals("There should be two items in the list.", 2,
				resources.getCount());
	}

	public void testGrabAvailableResourceWhenEmpty() throws Exception {
		assertEquals("There should be nothing returned from the list.", null,
				resources.grabAvailable());
	}

	public void testGrabAvailableResourceWithOne() throws Exception{
		FoodItem f = new FoodItem("", 0, 0, tr);
		resources.addResource(f);

		assertEquals("We should get back a FoodItem, ", f,
				resources.grabAvailable());
	}

	public void testAnyAvailableWhenEmpty() throws Exception {
		assertFalse("There should be no available Resources When empty.",
				resources.anyAvailable());
	}

	public void testAnyAvailableWhenOneTotalAndNotAvail() throws Exception {
		FoodItem fi = new FoodItem("", 100, 100, tr);
		fi.prepareFood();
		resources.addResource(fi);
		assertFalse("The only item in the list should not be available",
				resources.anyAvailable());
	}

	public void testOrderingOfElements(){
		FoodItem fi = new FoodItem("", 0, 400, tr);
		FoodItem fi2 = new FoodItem("", 0, 100, tr);
		resources = new ResourceList<FoodItem>();
		resources.addResource(fi);
		resources.addResource(fi2);

		assertEquals("The First element returned is incorrect.", fi2,
				resources.grabAvailable());
		assertEquals("The second element returned is incorrect.", fi,
				resources.grabAvailable());
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void setUp() throws Exception {
		super.setUp();
		resources = new ResourceList<FoodItem>();
		Time time = Time.createTime();
		tr = new TimeReader(time);
	}
}
