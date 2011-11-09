package com.teama.pds;

/**
 * Author:      Grant Kurtz
 * This is a special class that is used in cases where the stored resources
 * never need to be removed from the queue, but you need dynamic updating of
 * the queue each time the queue is accessed.  This allows for queues that are
 * constantly self-re-organizing but only return references.
 */
public class PeekResourceList<T extends Resource> extends ResourceList<T> {

	/**
	 * In order to get the effect as described by the class documentation above,
	 * we will do a little hack to get the internal PriorityQueue class to
	 * forcibly re-order itself, thereby guaranteeing the returned element
	 * is of the highest priority.
	 *
	 * @return A reference to the most-available object, otherwise null.
	 */
	public T grabAvailable() {
		if (getCount() != 0) {
			resources.add(resources.poll());
			return resources.peek().isAvailable() ? resources.peek() : null;
		}
		return null;
	}
}
