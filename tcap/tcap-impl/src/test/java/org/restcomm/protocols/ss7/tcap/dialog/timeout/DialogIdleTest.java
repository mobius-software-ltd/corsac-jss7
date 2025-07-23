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

package org.restcomm.protocols.ss7.tcap.dialog.timeout;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.listeners.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.TestEvent;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class DialogIdleTest extends SccpHarness {
	private static final int _DIALOG_TIMEOUT = 1000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;

	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "DialogIdleTestSccpStack1";
		super.sccpStack2Name = "DialogIdleTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		this.tcapStack1 = new TCAPStackImpl("DialogIdleTest", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("DialogIdleTest", this.sccpProvider2, 8, workerPool);

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT - 100);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);
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

	@Test
	public void testCreateOnly() throws TCAPException {
		long stamp = System.currentTimeMillis();
		List<TestEvent> expectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 0, stamp + _DIALOG_TIMEOUT);
		expectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp + _DIALOG_TIMEOUT);
		expectedEvents.add(te);

		client.startClientDialog();
		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(expectedEvents);
	}

	@Test
	public void testAfterBeginOnly1() throws TCAPException, TCAPSendException {
		// client timeout first
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterBeginOnly2() throws Exception {
		// server timeout first
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT - 200);

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue() throws TCAPException, TCAPSendException {
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue2() throws TCAPException, TCAPSendException {
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 4, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 5, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.sendContinue();
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterEnd() throws TCAPException, TCAPSendException {
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 2, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.sendEnd(TerminationType.Basic);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}
}
