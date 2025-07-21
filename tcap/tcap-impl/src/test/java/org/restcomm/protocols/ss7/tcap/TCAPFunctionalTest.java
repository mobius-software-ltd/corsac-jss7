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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.TCListener;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCEndIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCNoticeIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUniIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCUserAbortIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.Server;
import org.restcomm.protocols.ss7.tcap.listeners.TestEvent;

import com.mobius.software.common.dal.timers.TaskCallback;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPFunctionalTest extends SccpHarness {
	public static final long WAIT_TIME = 500;
	public static final long[] _ACN_ = new long[] { 0, 4, 0, 0, 1, 0, 19, 2 };
	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;
	private TCAPListenerWrapper tcapListenerWrapper;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		this.tcapStack1 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider2, 8, workerPool);

		this.tcapListenerWrapper = new TCAPListenerWrapper();
		this.tcapStack1.getProvider().addTCListener(tcapListenerWrapper);

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setDoNotSendProtocolVersion(false);
		this.tcapStack2.setDoNotSendProtocolVersion(false);
		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(5000);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack2.setDialogIdleTimeout(5000);

		// create test classes
		this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		this.tcapStack1.stop();
		this.tcapStack2.stop();

		client.dialog = null;
		server.dialog = null;

		client.observerdEvents = null;
		server.observerdEvents = null;

		super.tearDown();
	}

	@Test
	public void simpleTCWithDialogTest() throws Exception {
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		// this.saveTrafficInFile();

		client.startClientDialog();
		assertNotNull(client.dialog.getLocalAddress());
		assertNull(client.dialog.getRemoteDialogId());

		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		assertNotNull(server.dialog.getLocalAddress());
		assertNotNull(server.dialog.getRemoteDialogId());

		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.sendEnd(TerminationType.Basic);
		assertNotNull(client.dialog.getLocalAddress());
		assertNotNull(client.dialog.getRemoteDialogId());

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void uniMsgTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Uni, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Uni, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		serverExpectedEvents.add(te);

		client.startUniDialog();
		client.sendUni();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	private class TCAPListenerWrapper implements TCListener {
		@Override
		public void onTCUni(TCUniIndication ind) {
		}

		@Override
		public void onTCBegin(TCBeginIndication ind, TaskCallback<Exception> callback) {
		}

		@Override
		public void onTCContinue(TCContinueIndication ind, TaskCallback<Exception> callback) {
			assertEquals(ind.getComponents().size(), 2);
			ReturnResultLast rrl = (ReturnResultLast) ind.getComponents().get(0);
			Invoke inv = (Invoke) ind.getComponents().get(1);

			// operationCode is not sent via ReturnResultLast because it does not contain a
			// Parameter
			// so operationCode is taken from a sent Invoke
			assertEquals((long) rrl.getInvokeId(), 0);
			assertEquals((long) rrl.getOperationCode().getLocalOperationCode(), 12);

			// second Invoke has its own operationCode and it has linkedId to the second
			// sent Invoke
			assertEquals((long) inv.getInvokeId(), 0);
			assertEquals((long) inv.getOperationCode().getLocalOperationCode(), 14);
			assertEquals((long) inv.getLinkedId(), 1);

			// we should see operationCode of the second sent Invoke
			assertEquals((long) inv.getLinkedOperationCode().getLocalOperationCode(), 13);
		}

		@Override
		public void onTCEnd(TCEndIndication ind, TaskCallback<Exception> callback) {
		}

		@Override
		public void onTCUserAbort(TCUserAbortIndication ind) {
		}

		@Override
		public void onTCPAbort(TCPAbortIndication ind) {
		}

		@Override
		public void onTCNotice(TCNoticeIndication ind) {
		}

		@Override
		public void onDialogReleased(Dialog dialog) {
		}

		@Override
		public void onInvokeTimeout(Dialog dialog, int invokeId, InvokeClass invokeClass) {
		}

		@Override
		public void onDialogTimeout(Dialog dialog) {
		}
	}
}