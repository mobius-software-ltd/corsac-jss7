package org.restcomm.protocols.ss7.sccp.impl.events;

import java.util.LinkedList;
import java.util.Queue;

public class TestEventFactory<T> {
	private Queue<TestEvent<T>> events = new LinkedList<TestEvent<T>>();
	private int sequence = 0;

	public static <T> TestEventFactory<T> create() {
		return new TestEventFactory<T>();
	}

	public TestEvent<T> addReceived(T type) {
		TestEvent<T> event = TestEvent.createReceivedEvent(type, null, sequence++);
		events.add(event);

		return event;
	}

	public TestEvent<T> addSent(T type) {
		TestEvent<T> event = TestEvent.createSentEvent(type, null, sequence++);
		events.add(event);

		return event;
	}

	public Queue<TestEvent<T>> getEvents() {
		return events;
	}
}
