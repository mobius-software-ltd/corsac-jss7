package org.restcomm.protocols.ss7.tcap.utils;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.restcomm.protocols.ss7.tcap.listeners.events.TestEvent;

public class EventTestUtils {
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

	public static String compareEvents(TestEvent ev1, TestEvent ev2) {
		StringBuilder sb = new StringBuilder();

		sb.append(ev1 != null ? ev1.toString() : "NOP");
		sb.append(" - ");
		sb.append(ev2 != null ? ev2.toString() : "NOP");

		if (!ev1.equals(ev2))
			sb.append(" [MISMATCH]");

		return sb.toString();
	}

	public static String compareEvents(Collection<TestEvent> evList1, Collection<TestEvent> evList2) {
		StringBuilder sb = new StringBuilder();
		sb.append("Compared events:");
		sb.append("\n");

		Iterator<TestEvent> iterator1 = evList1.iterator();
		Iterator<TestEvent> iterator2 = evList2.iterator();

		while (iterator1.hasNext() || iterator2.hasNext()) {
			TestEvent ev1 = iterator1.hasNext() ? iterator1.next() : null;
			TestEvent ev2 = iterator2.hasNext() ? iterator2.next() : null;

			sb.append(compareEvents(ev1, ev2));
		}

		return sb.toString();
	}

	public static void assertEvents(Collection<TestEvent> expectedEvents, Collection<TestEvent> actualEvents) {
		Queue<TestEvent> actual = new LinkedList<TestEvent>(actualEvents);
		Queue<TestEvent> expected = new LinkedList<TestEvent>(expectedEvents);

		try {
			assertEquals(expected, actual);
		} catch (AssertionError err) {
			throw new AssertionError(compareEvents(expected, actual));
		}
	}
}
