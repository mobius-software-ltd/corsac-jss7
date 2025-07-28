package org.restcomm.protocols.ss7.sccp.impl.events;

import java.util.Objects;

public class TestEvent<T> {
	private final T eventType;
	private final boolean sent;
	private final long timestamp;
	private final Object event;
	private final int sequence;

	public static <T> TestEvent<T> createReceivedEvent(T eventType, Object eventSource, int sequence) {
		TestEvent<T> te = new TestEvent<>(eventType, false, System.currentTimeMillis(), eventSource, sequence);
		return te;
	}

	public static <T> TestEvent<T> createSentEvent(T eventType, Object eventSource, int sequence) {
		TestEvent<T> te = new TestEvent<>(eventType, true, System.currentTimeMillis(), eventSource, sequence);
		return te;
	}

	public static <T> TestEvent<T> createReceivedEvent(T eventType, Object eventSource, int sequence, long stamp) {
		TestEvent<T> te = new TestEvent<>(eventType, false, stamp, eventSource, sequence);
		return te;
	}

	public static <T> TestEvent<T> createSentEvent(T eventType, Object eventSource, int sequence, long stamp) {
		TestEvent<T> te = new TestEvent<>(eventType, true, stamp, eventSource, sequence);
		return te;
	}

	public TestEvent(T eventType, boolean sent, long timestamp, Object event, int sequence) {
		this.eventType = eventType;
		this.sent = sent;
		this.timestamp = timestamp;
		this.event = event;
		this.sequence = sequence;
	}

	public T getEventType() {
		return eventType;
	}

	public boolean isSent() {
		return sent;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public Object getEvent() {
		return event;
	}

	public int getSequence() {
		return sequence;
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
		return "TestEvent [eventType=" + eventType + ", sent=" + sent + ", timestamp=" + timestamp + ", sequence="
				+ sequence + ", source=" + event + "]";
	}
}
