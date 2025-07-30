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

import java.util.Arrays;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndRequest;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Simple example demonstrates how to use TCAP Stack
 *
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class ClientTest implements TCListener {
	// encoded Application Context Name
	public static final List<Long> _ACN_ = Arrays.asList(new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L });

	private TCAPProvider tcapProvider;
	private Dialog clientDialog;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	ClientTest() throws NamingException {

		InitialContext ctx = new InitialContext();
		try {
			String providerJndiName = "java:/restcomm/ss7/tcap";
			this.tcapProvider = ((TCAPProvider) ctx.lookup(providerJndiName));
		} finally {
			ctx.close();
		}

		this.tcapProvider.addTCListener(this);
	}

	public void sendInvoke() throws TCAPException, TCAPSendException {
		SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		clientDialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress, 0);

		// create some INVOKE
		OperationCode oc = TcapFactory.createLocalOperationCode(12);
		// no parameter
		this.clientDialog.sendData(null, null, null, null, oc, null, true, false);
		ApplicationContextName acn = this.tcapProvider.getDialogPrimitiveFactory().createApplicationContextName(_ACN_);
		// UI is optional!
		TCBeginRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createBegin(this.clientDialog);
		tcbr.setApplicationContextName(acn);
		this.clientDialog.send(tcbr, dummyCallback);
	}

	@Override
	public void onDialogReleased(Dialog dialog) {
	}

	@Override
	public void onInvokeTimeout(Dialog dialog, int invokeId, InvokeClass invokeClass) {
	}

	@Override
	public void onDialogTimeout(Dialog dialog) {
		dialog.keepAlive();
	}

	@Override
	public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
	}

	@Override
	public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
		// send end
		TCEndRequest end = this.tcapProvider.getDialogPrimitiveFactory().createEnd(ind.getDialog());
		end.setTermination(TerminationType.Basic);

		ind.getDialog().send(end, dummyCallback);
	}

	@Override
	public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
		// should not happen, in this scenario, we send data.
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		// not going to happen
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		// not going to happen
	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		// not going to happen
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		// not going to happen
	}

	public static void main(String[] args) {
		try {
			ClientTest c = new ClientTest();
			c.sendInvoke();
		} catch (NamingException | TCAPException | TCAPSendException e) {
			e.printStackTrace();
		}
	}
}
