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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCContinueIndication;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.Server;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.events.TestEvent;
import org.restcomm.protocols.ss7.tcap.listeners.events.TestEventFactory;
import org.restcomm.protocols.ss7.tcap.utils.EventTestUtils;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class TCAPFunctionalTest extends SccpHarness {
	private static final long INVOKE_TIMEOUT = 0;
	private static final long DIALOG_TIMEOUT = Integer.MAX_VALUE;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("TCAPFunctionalTest", super.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPFunctionalTest", super.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setDoNotSendProtocolVersion(false);
		tcapStack2.setDoNotSendProtocolVersion(false);

		// default invoke timeouts
		tcapStack1.setInvokeTimeout(INVOKE_TIMEOUT);
		tcapStack2.setInvokeTimeout(INVOKE_TIMEOUT);

		// default dialog timeouts
		tcapStack1.setDialogIdleTimeout(DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(DIALOG_TIMEOUT);

		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (tcapStack1 != null) {
			tcapStack1.stop();
			tcapStack1 = null;
		}

		if (tcapStack2 != null) {
			tcapStack2.stop();
			tcapStack2 = null;
		}

		super.tearDown();
	}

	/**
	 * Simple TC with dialog
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * TC-END
	 * </pre>
	 */
	@Test
	public void simpleTCWithDialogTest() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		Dialog clientDialog = client.getCurrentDialog();
		assertNotNull(clientDialog.getLocalAddress());
		assertNull(clientDialog.getRemoteDialogId());

		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();
		Dialog serverDialog = server.getCurrentDialog();

		assertNotNull(serverDialog.getLocalAddress());
		assertNotNull(serverDialog.getRemoteDialogId());

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		{
			TestEvent event = client.getNextEvent(EventType.Continue);
			TCContinueIndication ind = (TCContinueIndication) event.getEvent();

			assertEquals(2, ind.getComponents().size());
			ReturnResultLast rrl = (ReturnResultLast) ind.getComponents().get(0);
			Invoke inv = (Invoke) ind.getComponents().get(1);

			// operationCode is not sent via ReturnResultLast because it does not contain a
			// Parameter
			// so operationCode is taken from a sent Invoke
			assertEquals(0, (long) rrl.getInvokeId());
			assertEquals(12, (long) rrl.getOperationCode().getLocalOperationCode());

			// second Invoke has its own operationCode and it has linkedId to the second
			// sent Invoke
			assertEquals(0, (long) inv.getInvokeId());
			assertEquals(14, (long) inv.getOperationCode().getLocalOperationCode());
			assertEquals(1, (long) inv.getLinkedId());

			// we should see operationCode of the second sent Invoke
			assertEquals(13, (long) inv.getLinkedOperationCode().getLocalOperationCode());
		}

		// 3. TC-END
		client.sendEnd(TerminationType.Basic);
		assertNotNull(clientDialog.getLocalAddress());
		assertNotNull(clientDialog.getRemoteDialogId());

		client.awaitSent(EventType.End);
		server.awaitReceived(EventType.End);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.End);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * UNI message test
	 * 
	 * <pre>
	 * UNI
	 * </pre>
	 */
	@Test
	public void uniMsgTest() throws Exception {
		// 1. UNI
		client.startUniDialog();
		client.sendUni();

		client.awaitSent(EventType.Uni);
		server.awaitReceived(EventType.Uni);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Uni);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Uni);
		serverExpected.addReceived(EventType.DialogRelease);

		EventTestUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		EventTestUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}
}