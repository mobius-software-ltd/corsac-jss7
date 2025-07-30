package org.restcomm.protocols.ss7.sccp.impl.events;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TestEventUtils {
	private static final long DEFAULT_TOLERANCE = 500;
	private static long lastStamp = Long.MIN_VALUE;

	public static void updateStamp() {
		lastStamp = System.currentTimeMillis();
	}

	public static void assertPassed(long min) {
		assertPassed(min - DEFAULT_TOLERANCE, min + DEFAULT_TOLERANCE);
	}

	public static void assertPassed(long min, long max) {
		long currStamp = System.currentTimeMillis();
		long diff = currStamp - lastStamp;

		if (diff < min || diff > max) {
			String message = "Timestamp check with failed, expected from " + min + " to " + max + " ms passed, but got "
					+ diff;

			throw new AssertionError(message);
		}
	}

	public static <T> String outputEvents(Collection<TestEvent<T>> events) {
		StringBuilder sb = new StringBuilder();
		sb.append("Received events:");
		sb.append("\n");

		for (TestEvent<?> event : events) {
			sb.append(event.getSimpleString());
			sb.append("\n");
		}

		return sb.toString();
	}

	public static <T> String compareEvents(TestEvent<T> ev1, TestEvent<T> ev2) {
		StringBuilder sb = new StringBuilder();

		sb.append(ev1 != null ? ev1.getSimpleString() : "NOP");
		sb.append(" - ");
		sb.append(ev2 != null ? ev2.getSimpleString() : "NOP");

		if (ev1 == null || ev2 == null || !ev1.equals(ev2))
			sb.append(" [MISMATCH]");

		return sb.toString();
	}

	public static <T> String compareEvents(Collection<TestEvent<T>> evList1, Collection<TestEvent<T>> evList2) {
		StringBuilder sb = new StringBuilder();
		sb.append("Compared events:");
		sb.append("\n");

		Iterator<TestEvent<T>> iterator1 = evList1.iterator();
		Iterator<TestEvent<T>> iterator2 = evList2.iterator();

		while (iterator1.hasNext() || iterator2.hasNext()) {
			TestEvent<T> ev1 = iterator1.hasNext() ? iterator1.next() : null;
			TestEvent<T> ev2 = iterator2.hasNext() ? iterator2.next() : null;

			sb.append(compareEvents(ev1, ev2));
			sb.append("\n");
		}

		return sb.toString();
	}

	public static <T> void assertEvents(Collection<TestEvent<T>> expectedEvents,
			Collection<TestEvent<T>> actualEvents) {
		Queue<TestEvent<T>> actual = new LinkedList<TestEvent<T>>(actualEvents);
		Queue<TestEvent<T>> expected = new LinkedList<TestEvent<T>>(expectedEvents);

		try {
			assertEquals(expected, actual);
		} catch (AssertionError err) {
			throw new AssertionError(compareEvents(expected, actual));
		}
	}
}
