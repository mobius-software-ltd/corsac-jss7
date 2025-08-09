package org.restcomm.protocols.ss7.sccp.impl.events;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public abstract class TestEventHarness<T> {
	public static final int DEFAULT_EVENT_TIMEOUT = 10 * 1000;
	protected int eventTimeout = DEFAULT_EVENT_TIMEOUT;

	protected Queue<TestEvent<T>> observerdEvents = new ConcurrentLinkedQueue<>();
	protected Map<T, Queue<TestEvent<T>>> receivedEventsByType = new ConcurrentHashMap<>();

	protected Map<T, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<T, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	private Semaphore getSemaphoreOrCreate(T eventType, boolean isSent) {
		Map<T, Semaphore> semaphores = isSent ? sentSemaphores : receivedSemaphores;
		return semaphores.computeIfAbsent(eventType, k -> new Semaphore(0));
	}

	private void handleAwait(T eventType, boolean isSent) {
		Semaphore semaphore = getSemaphoreOrCreate(eventType, isSent);

		try {
			boolean isAcquired = semaphore.tryAcquire(eventTimeout, TimeUnit.MILLISECONDS);
			assertTrue("Event for type " + eventType + " is not acquired in " + eventTimeout + " milliseconds",
					isAcquired);
		} catch (AssertionError e) {
			String error = e.getMessage() + "\n\n" + TestEventUtils.outputEvents(observerdEvents);
			throw new AssertionError(error);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void handleEvent(TestEvent<T> testEvent, Map<T, Semaphore> semaphores) {
		this.observerdEvents.add(testEvent);
		T eventType = testEvent.getEventType();

		if (!testEvent.isSent()) {
			Queue<TestEvent<T>> events = receivedEventsByType.computeIfAbsent(eventType,
					k -> new ConcurrentLinkedQueue<>());
			events.add(testEvent);
		}

		Semaphore semaphore = getSemaphoreOrCreate(eventType, testEvent.isSent());
		semaphore.release();
	}

	public void handleReceived(T eventType, Object eventSource) {
		TestEvent<T> receivedEvent = TestEvent.createReceivedEvent(eventType, eventSource, observerdEvents.size());
		this.handleEvent(receivedEvent, this.receivedSemaphores);
	}

	public void handleSent(T eventType, Object eventSource) {
		TestEvent<T> sentEvent = TestEvent.createSentEvent(eventType, eventSource, observerdEvents.size());
		this.handleEvent(sentEvent, this.sentSemaphores);
	}

	public void awaitReceived(@SuppressWarnings("unchecked") T... eventTypes) {
		for (T eventType : eventTypes)
			handleAwait(eventType, false);
	}

	public void awaitSent(@SuppressWarnings("unchecked") T... eventTypes) {
		for (T eventType : eventTypes)
			handleAwait(eventType, true);
	}

	public TestEvent<T> getNextEvent(T eventType) {
		Queue<TestEvent<T>> events = receivedEventsByType.get(eventType);
		if (events == null)
			return null;

		return events.poll();
	}

	public TestEvent<T> getNextEventWithSkip(T eventType, int skip) {
		for (int i = 0; i < skip; i++)
			this.getNextEvent(eventType);

		return getNextEvent(eventType);
	}

	public Collection<TestEvent<T>> getEvents() {
		return observerdEvents;
	}

	public void updateEventTimeout(int newTimeout) {
		eventTimeout = newTimeout;
	}

	public void clear() {
		eventTimeout = DEFAULT_EVENT_TIMEOUT;
		observerdEvents.clear();

		receivedEventsByType.clear();

		sentSemaphores.clear();
		receivedSemaphores.clear();
	}
}