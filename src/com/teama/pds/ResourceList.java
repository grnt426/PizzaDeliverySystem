package com.teama.pds;

import java.util.PriorityQueue;

/**
 * Author:      Grant Kurtz
 * Maintains a list of objects extending Resource, with a PriorityQueue as the
 * backing data structure. The difference is this class only returns elements
 * assuming that the resource itself is "available", whatever those conditions
 * may be.
 */
public class ResourceList<T extends Resource> {

	/**
	 * The backing data structure that this class operates on top of.
	 */
	final PriorityQueue<T> resources;

	/**
	 * Default Constructor
	 * <p/>
	 * Just initializes an empty PriorityQueue for the backing data structure
	 * of this class.
	 */
	public ResourceList() {
		resources = new PriorityQueue<T>();
	}

	public ResourceList(PriorityQueue<T> tmp) {
		resources = new PriorityQueue<T>();
		resources.addAll(tmp);
	}

	/**
	 * @param r The resource to add to the priority queue
	 */
	public void addResource(T r) {
		resources.add(r);
	}

	/**
	 * @return The next most available Resource, otherwise null.
	 */
	public T grabAvailable() {
		return getCount() != 0 && resources.peek().isAvailable()
				? resources.poll() : null;
	}

	/**
	 * @return True if the first resource in the queue is available, otherwise
	 *         false if there are no elements in the queue or the first
	 *         resource isn't available.
	 */
	public boolean anyAvailable() {
		return getCount() != 0 && resources.peek().isAvailable();
	}

	/**
	 * This method is necessary for cases where you have nested ResourceLists
	 * and it is necessary for each resource in the upper-level list to re-ask
	 * its top-most child if it is available.  This is a must as the resources
	 * of the bottom-most resource may change availability while the top-most
	 * will not reflect this change (as it may be second in the queue and
	 * therefore never polled for available resources, and then subsequently
	 * the children are never re-polled for availability).
	 */
	public void checkAllInternalResources() {
		for (T t : resources)
			t.isAvailable();
	}

	/**
	 * @return The current number of resources in this list, regardless of
	 *         availability.
	 */
	public int getCount() {
		return resources.size();
	}

	/**
	 * A convenience function for dumping all the elements of the received list
	 * into this class' instance.
	 *
	 * @param items The list to union.
	 */
	public void addResources(ResourceList<T> items) {
		resources.addAll(items.getResourceQueue());
	}

	/**
	 * @return The underlying data structure this class operates on top of.
	 */
	public PriorityQueue<T> getResourceQueue() {
		return resources;
	}

	public boolean containsResource(T resource) {
		return resources.contains(resource);
	}

	public void removeResource(T resource) {
		resources.remove(resource);
	}

	/**
	 * Just removes all resource objects from this ResourceList
	 */
	public void clear() {
		for (T t : resources) {
			resources.remove(t);
		}
	}
}
