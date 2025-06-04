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

package org.restcomm.protocols.ss7.tcap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });
	protected List<TestEvent> observerdEvents = new ArrayList<TestEvent>();

	protected String listenerName = this.toString();

	protected Dialog dialog;
	protected TCAPStack stack;
	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	protected TCAPProvider tcapProvider;
	protected ParameterFactory parameterFactory;
	protected int sequence = 0;

	protected ApplicationContextName acn;
	protected UserInformation ui;

	protected PAbortCauseType pAbortCauseType;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	/**
	 * @param stack
	 * @param thisAddress
	 * @param remoteAddress
	 */
	public EventTestHarness(final TCAPStack stack, final ParameterFactory parameterFactory,
			final SccpAddress thisAddress, final SccpAddress remoteAddress) {
		this.stack = stack;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);
		this.parameterFactory = parameterFactory;
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
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]send BEGIN");
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		// UI is optional!
		TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.dialog);
		tcbr.setApplicationContextName(acn);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.Begin, tcbr, sequence++));
		this.dialog.send(tcbr, dummyCallback);
	}

	public void sendContinue() throws TCAPSendException, TCAPException {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]send CONTINUE");
		// send continue
		TCContinueRequest con = this.tcapProvider.getDialogPrimitiveFactory().createContinue(dialog);
		if (acn != null) {
			con.setApplicationContextName(acn);
			acn = null;
		}
		if (ui != null) {
			con.setUserInformation(ui);
			ui = null;
		}
		dialog.send(con, dummyCallback);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.Continue, con, sequence++));

	}

	public void sendEnd(TerminationType terminationType) throws TCAPSendException {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]send END");
		// send end
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
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.End, end, sequence++));
		dialog.send(end, dummyCallback);

	}

	public void sendAbort(ApplicationContextName acn, UserInformation ui, DialogServiceUserType type)
			throws TCAPSendException {

		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]send ABORT");
		TCUserAbortRequest abort = this.tcapProvider.getDialogPrimitiveFactory().createUAbort(dialog);
		if (acn != null)
			abort.setApplicationContextName(acn);
		if (ui != null)
			abort.setUserInformation(ui);
		abort.setDialogServiceUserType(type);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.UAbort, abort, sequence++));
		this.dialog.send(abort, dummyCallback);

	}

	public void sendUni() throws TCAPException, TCAPSendException {
		// create some INVOKE
		OperationCode oc = TcapFactory.createLocalOperationCode(12);
		// no parameter
		this.dialog.sendData(null, null, InvokeClass.Class4, null, oc, null, true, false);

		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]send UNI");
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		TCUniRequest tcur = this.tcapProvider.getDialogPrimitiveFactory().createUni(this.dialog);
		tcur.setApplicationContextName(acn);
		this.observerdEvents.add(TestEvent.createSentEvent(EventType.Uni, tcur, sequence++));
		this.dialog.send(tcur, dummyCallback);
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onUni");
		TestEvent te = TestEvent.createReceivedEvent(EventType.Uni, ind, sequence++);
		this.observerdEvents.add(te);
		this.dialog = ind.getDialog();
	}

	@Override
	public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onBegin");
		TestEvent te = TestEvent.createReceivedEvent(EventType.Begin, ind, sequence++);
		this.observerdEvents.add(te);
		this.dialog = ind.getDialog();

		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onContinue");
		TestEvent te = TestEvent.createReceivedEvent(EventType.Continue, ind, sequence++);
		this.observerdEvents.add(te);
		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onEnd");
		TestEvent te = TestEvent.createReceivedEvent(EventType.End, ind, sequence++);
		this.observerdEvents.add(te);

	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onUAbort");
		TestEvent te = TestEvent.createReceivedEvent(EventType.UAbort, ind, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onPAbort");
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, ind, sequence++);
		this.observerdEvents.add(te);

		pAbortCauseType = ind.getPAbortCause();
	}

	@Override
	public void onDialogReleased(Dialog dialog) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onDialogReleased");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogRelease, dialog, sequence++);
		this.observerdEvents.add(te);
	}

	@Override
	public void onInvokeTimeout(Dialog dialog, int invokeId, InvokeClass invokeClass) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onInvokeTimeout");
		TestEvent te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, sequence++);
		this.observerdEvents.add(te);

	}

	@Override
	public void onDialogTimeout(Dialog dialog) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onDialogTimeout");
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, dialog, sequence++);
		this.observerdEvents.add(te);

	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		System.err.println(listenerName + " T[" + System.currentTimeMillis() + "]onNotice");
		TestEvent te = TestEvent.createReceivedEvent(EventType.Notice, ind, sequence++);
		this.observerdEvents.add(te);
	}

	public List<TestEvent> getObserverdEvents() {
		return observerdEvents;
	}

	public void compareEvents(List<TestEvent> expectedEvents) {
		doCompareEvents(this.observerdEvents, expectedEvents);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
