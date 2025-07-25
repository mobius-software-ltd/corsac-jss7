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

package org.restcomm.protocols.ss7.tcapAnsi.listeners;

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
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPStack;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortRequest;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.comp.OperationCodeImpl;

import com.mobius.software.common.dal.timers.TaskCallback;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.Unpooled;

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

	public Queue<TestEvent> observerdEvents = new ConcurrentLinkedQueue<TestEvent>();
	protected AtomicInteger sequence = new AtomicInteger(0);

	protected Map<EventType, Semaphore> sentSemaphores = new ConcurrentHashMap<>();
	protected Map<EventType, Semaphore> receivedSemaphores = new ConcurrentHashMap<>();

	private Logger logger = null;

	public Dialog dialog;
	public TCAPStack stack;
	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	protected TCAPProvider tcapProvider;
	protected ParameterFactory parameterFactory;

	protected ApplicationContext acn;
	protected UserInformation ui;

	public PAbortCause pAbortCauseType;
	public RejectProblem rejectProblem;

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
		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
		// UI is optional!
		TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.dialog, true);
		tcbr.setApplicationContext(acn);
		this.dialog.send(tcbr, dummyCallback);

		this.handleSent(EventType.Begin, tcbr);
	}

	public void sendContinue(boolean addingInv) throws TCAPSendException, TCAPException {
		// send end
		TCConversationRequest con = this.tcapProvider.getDialogPrimitiveFactory().createConversation(dialog, true);
		if (acn != null) {
			con.setApplicationContext(acn);
			acn = null;
		}
		if (ui != null) {
			con.setUserInformation(ui);
			ui = null;
		}

		if (addingInv && acn == null && ui == null) {
			// no dialog patch - we are adding Invoke primitive
			Invoke inv = TcapFactory.createComponentInvokeNotLast();
			inv.setInvokeId(this.dialog.getNewInvokeId());
			OperationCode oc = TcapFactory.createNationalOperationCode(10);
			inv.setOperationCode(oc);
			ASNOctetString innerString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }), null, null,
					null, false);
			inv.setSeqParameter(innerString);

			dialog.sendComponent(inv);
		}

		dialog.send(con, dummyCallback);
		this.handleSent(EventType.Continue, con);

	}

	public void sendEnd(boolean addingInv) throws TCAPSendException, TCAPException {
		// send end
		TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(dialog);
		// end.setTermination(terminationType);
		if (acn != null) {
			end.setApplicationContext(acn);
			acn = null;
		}
		if (ui != null) {
			end.setUserInformation(ui);
			ui = null;
		}

		if (addingInv && acn == null && ui == null) {
			// no dialog patch - we are adding Invoke primitive
			Invoke inv = TcapFactory.createComponentInvokeNotLast();
			inv.setInvokeId(this.dialog.getNewInvokeId());
			OperationCode oc = new OperationCodeImpl();
			inv.setOperationCode(oc);
			ASNOctetString innerString = new ASNOctetString(Unpooled.wrappedBuffer(new byte[] { 3, 4, 5 }), null, null,
					null, false);
			inv.setSeqParameter(innerString);

			dialog.sendComponent(inv);
		}

		this.handleSent(EventType.End, end);
		dialog.send(end, dummyCallback);

	}

	public void sendAbort(ApplicationContext acn, UserInformationImpl ui) throws TCAPSendException {
		TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
		if (acn != null)
			abort.setApplicationContext(acn);
		if (ui != null)
			abort.setUserAbortInformation(ui);
		// abort.setDialogServiceUserType(type);
		this.handleSent(EventType.UAbort, abort);
		this.dialog.send(abort, dummyCallback);

	}

	public void sendUni() throws TCAPException, TCAPSendException {
		ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

		// create some INVOKE
		Invoke invoke = cpFactory.createTCInvokeRequestNotLast(InvokeClass.Class4);
		invoke.setInvokeId(this.dialog.getNewInvokeId());
		OperationCode oc = TcapFactory.createNationalOperationCode(12);
		invoke.setOperationCode(oc);
		// no parameter
		this.dialog.sendComponent(invoke);

		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContext(_ACN_);
		TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
		tcur.setApplicationContext(acn);
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
	public void onTCQuery(TCQueryIndication ind) {
		this.handleReceived(EventType.Begin, ind);
		this.dialog = ind.getDialog();

		if (ind.getApplicationContext() != null)
			this.acn = ind.getApplicationContext();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCConversation(TCConversationIndication ind) {
		this.handleReceived(EventType.Continue, ind);
		if (ind.getApplicationContext() != null) {
			// this.acn = ind.getApplicationContextName();
		}

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCResponse(TCResponseIndication ind) {
		this.handleReceived(EventType.End, ind);

		ComponentPortion compp = ind.getComponents();
		if (compp != null && compp.getComponents() != null && compp.getComponents().size() > 0)
			if (compp.getComponents().get(0).getType() == ComponentType.Reject) {
				Reject rej = (Reject) compp.getComponents().get(0);
				this.rejectProblem = null;
				try {
					this.rejectProblem = rej.getProblem();
				} catch (ParseException ex) {

				}
			}

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
	public void onDialogReleased(Dialog d) {
		this.handleReceived(EventType.DialogRelease, d);
	}

	@Override
	public void onInvokeTimeout(Invoke tcInvokeRequest) {
		this.handleReceived(EventType.InvokeTimeout, tcInvokeRequest);
	}

	@Override
	public void onDialogTimeout(Dialog d) {
		this.handleReceived(EventType.DialogTimeout, d);
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		this.handleReceived(EventType.Notice, ind);
	}

	public void compareEvents(List<TestEvent> expectedEvents) {
		List<TestEvent> actualEvents = new ArrayList<TestEvent>(observerdEvents);
		int expectedSize = expectedEvents.size();
		int actualSize = actualEvents.size();

		try {
			assertEquals("Size of received events: " + actualSize + ", does not equal expected events: " + expectedSize,
					expectedSize, actualSize);

			for (int index = 0; index < expectedSize; index++) {
				TestEvent expected = expectedEvents.get(index);
				TestEvent actual = actualEvents.get(index);

				assertEquals(expected, actual);
			}
		} catch (AssertionError err) {
			StringBuilder sb = new StringBuilder();
			sb.append(err.getMessage()).append("\n");
			sb.append("Received events:").append("\n");
			sb.append(doStringCompare(expectedEvents, actualEvents));

			fail(sb.toString());
		}
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
