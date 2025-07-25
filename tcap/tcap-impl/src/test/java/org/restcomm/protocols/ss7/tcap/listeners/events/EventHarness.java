package org.restcomm.protocols.ss7.tcap.listeners.events;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EventHarness {
	public static final int DEFAULT_EVENT_TIMEOUT = 10 * 1000;
	private int eventTimeout = DEFAULT_EVENT_TIMEOUT;

	private AtomicInteger sequence = new AtomicInteger(0);

	private Queue<TestEvent> observerdEvents = new ConcurrentLinkedQueue<TestEvent>();
	private Map<EventType, Queue<TestEvent>> receivedEventsByType = new ConcurrentHashMap<>();

	protected Map<EventType, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<EventType, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	private synchronized Semaphore getSemaphoreOrCreate(EventType event, boolean isSent) {
		Map<EventType, Semaphore> semaphores = null;
		if (isSent)
			semaphores = sentSemaphores;
		else
			semaphores = receivedSemaphores;

		Semaphore newSemaphore = new Semaphore(0);

		Semaphore result = semaphores.getOrDefault(event, newSemaphore);
		semaphores.putIfAbsent(event, newSemaphore);

		return result;
	}

	private void handleAwait(EventType eventType, boolean isSent) {
		Semaphore semaphore = getSemaphoreOrCreate(eventType, isSent);

		try {
			boolean isAcquired = semaphore.tryAcquire(eventTimeout, TimeUnit.MILLISECONDS);
			assertTrue("Event for type " + eventType + " is not acquired in " + eventTimeout + " milliseconds",
					isAcquired);
		} catch (AssertionError e) {
			String error = e.getMessage() + "\n\nReceived events:\n" + observerdEvents;
			throw new AssertionError(error);
		} catch (InterruptedException e) {
			return;
		}
	}

	private void handleEvent(TestEvent testEvent, Map<EventType, Semaphore> semaphores) {
		EventType eventType = testEvent.getEventType();

		this.observerdEvents.add(testEvent);

		if (!testEvent.isSent()) {
			this.receivedEventsByType.putIfAbsent(eventType, new ConcurrentLinkedQueue<>());
			this.receivedEventsByType.get(eventType).add(testEvent);
		}

		Semaphore semaphore = getSemaphoreOrCreate(eventType, testEvent.isSent());
		semaphore.release();
	}

	protected void handleReceived(EventType eventType, Object eventSource) {
		TestEvent receivedEvent = TestEvent.createReceivedEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(receivedEvent, this.receivedSemaphores);
	}

	protected void handleSent(EventType eventType, Object eventSource) {
		TestEvent sentEvent = TestEvent.createSentEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(sentEvent, this.sentSemaphores);
	}

	public void awaitReceived(EventType... eventTypes) {
		for (EventType eventType : eventTypes)
			handleAwait(eventType, false);
	}

	public void awaitSent(EventType... eventTypes) {
		for (EventType eventType : eventTypes)
			handleAwait(eventType, true);
	}

	public TestEvent getNextEvent(EventType eventType) {
		Queue<TestEvent> events = receivedEventsByType.get(eventType);
		if (events == null)
			return null;

		return events.poll();
	}

	public TestEvent getNextEventWithSkip(EventType eventType, int skip) {
		for (int i = 0; i < skip; i++)
			this.getNextEvent(eventType);

		return getNextEvent(eventType);
	}

	public Collection<TestEvent> getEvents() {
		return observerdEvents;
	}

	public void updateEventTimeout(int newTimeout) {
		eventTimeout = newTimeout;
	}

	public void clear() {
		eventTimeout = DEFAULT_EVENT_TIMEOUT;
		sequence.set(0);
		observerdEvents.clear();

		receivedEventsByType.clear();

		sentSemaphores.clear();
		receivedSemaphores.clear();
	}
}
