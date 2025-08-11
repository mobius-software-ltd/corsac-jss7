package org.restcomm.protocols.ss7.sccp.impl.events;

import java.util.Objects;

public class TestEvent<T> {
	private final T eventType;
	private final boolean sent;
	private final Object event;
	private int sequence;

	public static <T> TestEvent<T> createReceivedEvent(T eventType, Object eventSource) {
		TestEvent<T> te = new TestEvent<>(eventType, false, eventSource, Integer.MIN_VALUE);
		return te;
	}

	public static <T> TestEvent<T> createSentEvent(T eventType, Object eventSource) {
		TestEvent<T> te = new TestEvent<>(eventType, true, eventSource, Integer.MIN_VALUE);
		return te;
	}

	public static <T> TestEvent<T> createReceivedEvent(T eventType, Object eventSource, int sequence) {
		TestEvent<T> te = new TestEvent<>(eventType, false, eventSource, sequence);
		return te;
	}

	public static <T> TestEvent<T> createSentEvent(T eventType, Object eventSource, int sequence) {
		TestEvent<T> te = new TestEvent<>(eventType, true, eventSource, sequence);
		return te;
	}

	public TestEvent(T eventType, boolean sent, Object event, int sequence) {
		this.eventType = eventType;
		this.sent = sent;
		this.event = event;
		this.sequence = sequence;
	}

	public T getEventType() {
		return eventType;
	}

	public boolean isSent() {
		return sent;
	}

	public Object getEvent() {
		return event;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventType, sequence, sent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		TestEvent<?> other = (TestEvent<?>) obj;

		return eventType == other.eventType && sent == other.sent && sequence == other.sequence;
	}

	public String getSimpleString() {
		return "[type=" + eventType + ", sent=" + sent + ", seq=" + sequence + "]";
	}

	@Override
	public String toString() {
		return "TestEvent [eventType=" + eventType + ", sent=" + sent + ", sequence=" + sequence + ", source=" + event
				+ "]";
	}
}
