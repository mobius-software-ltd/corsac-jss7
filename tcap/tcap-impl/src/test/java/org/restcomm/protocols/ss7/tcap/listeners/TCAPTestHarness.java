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

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventHarness;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.DialogImpl;
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
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Super class for event based tests. Has capabilities for testing if events are
 * received and if so, if they were received in proper order.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public abstract class TCAPTestHarness extends TestEventHarness<EventType> implements TCListener {
	public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });

	protected Logger logger = LogManager.getLogger(TCAPTestHarness.class);
	protected String listenerName = this.toString();

	public Dialog dialog;
	public TCAPStack stack;
	protected SccpAddress thisAddress;
	protected SccpAddress remoteAddress;

	public TCAPProvider tcapProvider;
	protected ParameterFactory parameterFactory;

	protected ApplicationContextName acn;
	protected UserInformation ui;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception ex) {
			logger.error("Stub callback received error, " + ex.getMessage(), ex);
		}
	};

	public TCAPTestHarness(TCAPStack stack, ParameterFactory paramFactory, SccpAddress thisAddress,
			SccpAddress remoteAddress, Logger logger) {
		this.stack = stack;

		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;

		this.tcapProvider = this.stack.getProvider();
		this.tcapProvider.addTCListener(this);

		this.parameterFactory = paramFactory;
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

		super.handleSent(EventType.Begin, tcbr);
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

		super.handleSent(EventType.Continue, con);
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

		super.handleSent(EventType.End, end);
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

		super.handleSent(EventType.UAbort, abort);
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

		super.handleSent(EventType.Uni, tcur);
		this.dialog.send(tcur, dummyCallback);
	}

	public DialogImpl getCurrentDialog() {
		return (DialogImpl) this.dialog;
	}

	@Override
	protected void handleReceived(EventType eventType, Object eventSource) {
		super.handleReceived(eventType, eventSource);

		if (logger.isDebugEnabled())
			logger.debug("Received " + eventType);
	}

	@Override
	protected void handleSent(EventType eventType, Object eventSource) {
		super.handleSent(eventType, eventSource);

		if (logger.isDebugEnabled())
			logger.debug("Sent " + eventType);
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		this.dialog = ind.getDialog();
		super.handleReceived(EventType.Uni, ind);
	}

	@Override
	public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
		this.dialog = ind.getDialog();

		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();

		super.handleReceived(EventType.Begin, ind);
	}

	@Override
	public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
		super.handleReceived(EventType.Continue, ind);

		if (ind.getApplicationContextName() != null)
			this.acn = ind.getApplicationContextName();

		if (ind.getUserInformation() != null)
			this.ui = ind.getUserInformation();
	}

	@Override
	public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
		super.handleReceived(EventType.End, ind);
	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		super.handleReceived(EventType.UAbort, ind);
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		super.handleReceived(EventType.PAbort, ind);
	}

	@Override
	public void onDialogReleased(Dialog dialog) {
		super.handleReceived(EventType.DialogRelease, dialog);
	}

	@Override
	public void onInvokeTimeout(Dialog dialog, int invokeId, InvokeClass invokeClass) {
		super.handleReceived(EventType.InvokeTimeout, null);
	}

	@Override
	public void onDialogTimeout(Dialog dialog) {
		super.handleReceived(EventType.DialogTimeout, dialog);
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		super.handleReceived(EventType.Notice, ind);
	}
}
