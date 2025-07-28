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

package org.restcomm.protocols.ss7.tcapAnsi.dialog.timeout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Client;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Server;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.events.EventType;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class DialogIdleTest extends SccpHarness {
	private static final long INVOKE_TIMEOUT = 0;

	private static final long CLIENT_DIALOG_TIMEOUT = 2000;
	private static final long SERVER_DIALOG_TIMEOUT = 1000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "DialogIdleTestSccpStack1";
		this.sccpStack2Name = "DialogIdleTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("DialogIdleTest_1", super.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("DialogIdleTest_2", super.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(INVOKE_TIMEOUT);
		tcapStack2.setInvokeTimeout(INVOKE_TIMEOUT);

		tcapStack1.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(SERVER_DIALOG_TIMEOUT);

		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach1() {
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
	public void testCreateOnly() throws Exception {
		client.startClientDialog();
		TestEventUtils.updateStamp();

		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	@Test
	public void testAfterBeginOnly1() throws TCAPException, TCAPSendException {
		client.startClientDialog();
		client.sendBegin();

		server.awaitReceived(EventType.DialogTimeout); // server timeout first (default)

		client.awaitReceived(EventType.PAbort);
		server.awaitReceived(EventType.PAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	@Test
	public void testAfterBeginOnly2() throws Exception {
		// client timeout first
		tcapStack1.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT * 2);

		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);
		TestEventUtils.updateStamp();

		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT * 2);

		server.awaitReceived(EventType.PAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	@Test
	public void testAfterContinue() throws TCAPException, TCAPSendException {
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue(true);
		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		server.awaitReceived(EventType.PAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	@Test
	public void testAfterContinue2() throws TCAPException, TCAPSendException {
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue(true);
		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		client.sendContinue(true);
		client.awaitSent(EventType.Continue);
		server.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.PAbort);
		server.awaitReceived(EventType.PAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	@Test
	public void testAfterEnd() throws TCAPException, TCAPSendException {
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue(true);
		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		client.sendEnd(true);
		client.awaitSent(EventType.End);
		server.awaitReceived(EventType.End);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.End);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}
}
