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

package org.restcomm.protocols.ss7.tcapAnsi;

import java.util.Arrays;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SccpAddressImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCListener;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCQueryRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseRequest;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;

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
	public static final Long[] _ACN_ = new Long[] { 0L, 4L, 0L, 0L, 1L, 0L, 19L, 2L };
	private TCAPProvider tcapProvider;
	private Dialog clientDialog;

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

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	public void sendInvoke() throws TCAPException, TCAPSendException {
		SccpAddress localAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		SccpAddress remoteAddress = new SccpAddressImpl(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		clientDialog = this.tcapProvider.getNewDialog(localAddress, remoteAddress, 0);
		ComponentPrimitiveFactory cpFactory = this.tcapProvider.getComponentPrimitiveFactory();

		// create some INVOKE
		Invoke invoke = cpFactory.createTCInvokeRequestNotLast();
		invoke.setInvokeId(this.clientDialog.getNewInvokeId());
		OperationCode oc = TcapFactory.createNationalOperationCode(12);
		invoke.setOperationCode(oc);
		// no parameter

		this.clientDialog.sendComponent(invoke);

		ApplicationContext acn = this.tcapProvider.getDialogPrimitiveFactory()
				.createApplicationContext(Arrays.asList(_ACN_));
		// UI is optional!
		TCQueryRequest tcbr = this.tcapProvider.getDialogPrimitiveFactory().createQuery(this.clientDialog, true);
		tcbr.setApplicationContext(acn);
		this.clientDialog.send(tcbr, dummyCallback);
	}

	@Override
	public void onDialogReleased(Dialog d) {
	}

	@Override
	public void onInvokeTimeout(Invoke tcInvokeRequest) {
	}

	@Override
	public void onDialogTimeout(Dialog d) {
		d.keepAlive();
	}

	@Override
	public void onTCQuery(TCQueryIndication ind) {
	}

	@Override
	public void onTCConversation(TCConversationIndication ind) {
		// send end
		TCResponseRequest end = this.tcapProvider.getDialogPrimitiveFactory().createResponse(ind.getDialog());
//        end.setTermination(TerminationType.Basic);

		ind.getDialog().send(end, dummyCallback);
	}

	@Override
	public void onTCResponse(TCResponseIndication ind) {
		// should not happen, in this scenario, we send data.
	}

	@Override
	public void onTCUni(TCUniIndication ind) {
		// not going to happen
	}

	@Override
	public void onTCPAbort(TCPAbortIndication ind) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTCUserAbort(TCUserAbortIndication ind) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTCNotice(TCNoticeIndication ind) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {

		try {
			ClientTest c = new ClientTest();
			c.sendInvoke();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TCAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TCAPSendException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
