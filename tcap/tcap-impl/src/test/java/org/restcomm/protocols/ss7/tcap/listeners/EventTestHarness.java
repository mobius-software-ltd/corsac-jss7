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

package org.restcomm.protocols.ss7.tcap.listeners;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCAPStack;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Super class for event based tests. Has capabilities for testing if events are
 * received and if so, if they were received in proper order.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class EventTestHarness implements TCListener {
	private static final long EVENT_TIMEOUT = 10000;
	public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });

	private Logger logger = null;
	protected String listenerName = this.toString();

	public Dialog dialog;
	public TCAPStack stack;
	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	protected TCAPProvider tcapProvider;
	protected ParameterFactory parameterFactory;

	protected ApplicationContextName acn;
	protected UserInformation ui;

	public PAbortCauseType pAbortCauseType;

	public Queue<TestEvent> observerdEvents = new ConcurrentLinkedQueue<TestEvent>();
	protected AtomicInteger sequence = new AtomicInteger(0);

	protected Map<EventType, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<EventType, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public EventTestHarness(final TCAPStack stack, final ParameterFactory parameterFactory,
			final SccpAddress thisAddress, final SccpAddress remoteAddress, Logger logger) {
		this.stack = stack;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);
		this.parameterFactory = parameterFactory;
		this.logger = logger;
	}

	public void startClientDialog() throws TCAPException {
		if (dialog != null)
			throw new IllegalStateException("Dialog exists...");

		dialog = this.tcapProvider.getNewDialog(thisAddress, remoteAddress, 0);
	}

	public void startClientDialog(SccpAddress localAddress, SccpAddress remoteAddress) throws TCAPException {
		if (dialog != null)
			throw new IllegalStateException("Dialog exists...");
		dialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress, 0);
	}

	public void startUniDialog() throws TCAPException {
		if (dialog != null)
			throw new IllegalStateException("Dialog exists...");
		dialog = this.tcapProvider.getNewUnstructuredDialog(thisAddress, remoteAddress, 0);
	}

	public void sendBegin() throws TCAPException, TCAPSendException {
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		// UI is optional!
		TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
		tcbr.setApplicationContextName(acn);

		this.handleSent(EventType.Begin, tcbr);
		this.dialog.send(tcbr, dummyCallback);
	}

	public void sendContinue() throws TCAPSendException, TCAPException {
		TCContinueRequest con = this.tcapProvider.getDialogPrimitiveFactory().createContinue(dialog);
		if (acn != null) {
			con.setApplicationContextName(acn);
			acn = null;
		}
		if (ui != null) {
			con.setUserInformation(ui);
			ui = null;
		}

		this.handleSent(EventType.Continue, con);
		dialog.send(con, dummyCallback);
	}

	public void sendEnd(TerminationType terminationType) throws TCAPSendException {
		TCEndRequest end = this.tcapProvider.getDialogPrimitiveFactory().createEnd(dialog);
		end.setTermination(terminationType);
		if (acn != null) {
			end.setApplicationContextName(acn);
			acn = null;
		}
		if (ui != null) {
			end.setUserInformation(ui);
			ui = null;
		}

		this.handleSent(EventType.End, end);
		dialog.send(end, dummyCallback);
	}

	public void sendAbort(ApplicationContextName acn, UserInformation ui, DialogServiceUserType type)
			throws TCAPSendException {
		TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
		if (acn != null)
			abort.setApplicationContextName(acn);
		if (ui != null)
			abort.setUserInformation(ui);
		abort.setDialogServiceUserType(type);

		this.handleSent(EventType.UAbort, abort);
		this.dialog.send(abort, dummyCallback);
	}

	public void sendUni() throws TCAPException, TCAPSendException {
		// create some INVOKE
		OperationCode oc = TcapFactory.createLocalOperationCode(12);
		// no parameter
		this.dialog.sendData(null, null, InvokeClass.Class4, null, oc, null, true, false);

		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
		tcur.setApplicationContextName(acn);

		this.handleSent(EventType.Uni, tcur);
		this.dialog.send(tcur, dummyCallback);
	}

	private void handleAwait(EventType eventType, Map<EventType, Semaphore> semaphores) {
		Semaphore semaphore = semaphores.getOrDefault(eventType, new Semaphore(0));
		semaphores.putIfAbsent(eventType, semaphore);

		try {
			boolean isAcquired = semaphore.tryAcquire(EVENT_TIMEOUT, TimeUnit.MILLISECONDS);
			assertTrue("Event for type " + eventType + " is not acquired in " + EVENT_TIMEOUT + " milliseconds",
					isAcquired);
		} catch (InterruptedException e) {
			logger.error("Interrupted exception occured while waiting for event for type " + eventType + ": " + e);
			return;
		}
	}

	public void awaitReceived(EventType eventType) {
		handleAwait(eventType, this.receivedSemaphores);
	}

	public void awaitSent(EventType eventType) {
		handleAwait(eventType, this.sentSemaphores);
	}

	private void handleEvent(TestEvent testEvent, Map<EventType, Semaphore> semaphores) {
		this.observerdEvents.add(testEvent);

		EventType eventType = testEvent.getEventType();
		if (semaphores.containsKey(eventType))
			semaphores.get(eventType).release();
		else
			semaphores.put(eventType, new Semaphore(1));
	}

	protected void handleReceived(EventType eventType, Object eventSource) {
		TestEvent receivedEvent = TestEvent.createReceivedEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(receivedEvent, this.receivedSemaphores);
	}

	protected void handleSent(EventType eventType, Object eventSource) {
		TestEvent sentEvent = TestEvent.createSentEvent(eventType, eventSource, sequence.getAndIncrement());
		this.handleEvent(sentEvent, this.sentSemaphores);
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		this.handleReceived(EventType.Uni, ind);
		this.dialog = ind.getDialog();
	}

	@Override
	public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
		this.handleReceived(EventType.Begin, ind);
		this.dialog = ind.getDialog();

		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
		this.handleReceived(EventType.Continue, ind);

		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
		this.handleReceived(EventType.End, ind);
	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		this.handleReceived(EventType.UAbort, ind);
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		this.handleReceived(EventType.PAbort, ind);

		pAbortCauseType = ind.getPAbortCause();
	}

	@Override
	public void onDialogReleased(Dialog dialog) {
		this.handleReceived(EventType.DialogRelease, dialog);
	}

	@Override
	public void onInvokeTimeout(Dialog dialog, int invokeId, InvokeClass invokeClass) {
		this.handleReceived(EventType.InvokeTimeout, null);
	}

	@Override
	public void onDialogTimeout(Dialog dialog) {
		this.handleReceived(EventType.DialogTimeout, dialog);
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		this.handleReceived(EventType.Notice, ind);
	}

	public void compareEvents(List<TestEvent> expectedEvents) {
		doCompareEvents(new ArrayList<>(this.observerdEvents), expectedEvents);
	}

	public static void doCompareEvents(List<TestEvent> observerdEvents, List<TestEvent> expectedEvents) {
		if (expectedEvents.size() != observerdEvents.size())
			fail("Size of received events: " + observerdEvents.size() + ", does not equal expected events: "
					+ expectedEvents.size() + "\n" + doStringCompare(expectedEvents, observerdEvents));

		for (int i = 0; i < observerdEvents.size(); i++)
			assertEquals(observerdEvents.get(i), expectedEvents.get(i));
	}

	protected static String doStringCompare(List<TestEvent> lst1, List<TestEvent> lst2) {
		StringBuilder sb = new StringBuilder();
		int size1 = lst1.size();
		int size2 = lst2.size();
		int count = size1;
		if (count < size2)
			count = size2;

		for (int index = 0; count > index; index++) {
			String s1 = size1 > index ? lst1.get(index).toString() : "NOP";
			String s2 = size2 > index ? lst2.get(index).toString() : "NOP";
			sb.append(s1).append(" - ").append(s2).append("\n");
		}
		return sb.toString();
	}

	public static void waitFor(long v) {
		try {
			Thread.sleep(v);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
