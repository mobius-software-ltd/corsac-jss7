/*
 * Mobius Software LTD
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.tcap.listeners.events;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TestEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private EventType eventType;
	private boolean sent;
	private long timestamp;
	private Object event;
	private int sequence;

	public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence) {
		TestEvent te = new TestEvent(eventType, false, System.currentTimeMillis(), eventSource, sequence);
		return te;
	}

	public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence) {
		TestEvent te = new TestEvent(eventType, true, System.currentTimeMillis(), eventSource, sequence);
		return te;
	}

	public static TestEvent createReceivedEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
		TestEvent te = new TestEvent(eventType, false, stamp, eventSource, sequence);
		return te;
	}

	public static TestEvent createSentEvent(EventType eventType, Object eventSource, int sequence, long stamp) {
		TestEvent te = new TestEvent(eventType, true, stamp, eventSource, sequence);
		return te;
	}

	public TestEvent(EventType eventType, boolean sent, long timestamp, Object event, int sequence) {
		this.eventType = eventType;
		this.sent = sent;
		this.timestamp = timestamp;
		this.event = event;
		this.sequence = sequence;
	}

	public EventType getEventType() {
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

		TestEvent other = (TestEvent) obj;

		return eventType == other.eventType && sent == other.sent && sequence == other.sequence;
	}

	@Override
	public String toString() {
		return "TestEvent [eventType=" + eventType + ", sent=" + sent + ", timestamp=" + timestamp + ", sequence="
				+ sequence + ", source=" + event + "]";
	}
}
