package org.restcomm.protocols.ss7.tcap.listeners.events;

import java.util.LinkedList;
import java.util.Queue;

public class TestEventFactory {
	private Queue<TestEvent> events = new LinkedList<TestEvent>();
	private int sequence = 0;

	public static TestEventFactory create() {
		return new TestEventFactory();
	}

	public TestEvent addReceived(EventType type) {
		TestEvent event = new TestEvent(type, false, 0, null, sequence++);
		events.add(event);

		return event;
	}

	public TestEvent addSent(EventType type) {
		TestEvent event = new TestEvent(type, true, 0, null, sequence++);
		events.add(event);

		return event;
	}

	public Queue<TestEvent> getEvents() {
		return events;
	}
}
