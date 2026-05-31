/*
s * Mobius Software LTD
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPException;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.Server;
import org.restcomm.protocols.ss7.tcap.listeners.TCAPTestHarness;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class DialogIdleEndTest extends SccpHarness {
	private static final int CLIENT_DIALOG_TIMEOUT = 2 * 1000;
	private static final int SERVER_DIALOG_TIMEOUT = 1000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;

	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		super.sccpStack1Name = "DialogIdleEndTestSccpStack1";
		super.sccpStack2Name = "DialogIdleEndTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("DialogIdleEndTest1", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("DialogIdleEndTest2", this.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(0);
		tcapStack2.setInvokeTimeout(0);
		tcapStack1.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(SERVER_DIALOG_TIMEOUT);

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
	 * Idle end after Begin from Server
	 * 
	 * <pre>
	 * TC-BEGIN
	 * DialogTimeout (on server)
	 * UAbort (from server)
	 * </pre>
	 */
	@Test
	public void testAfterBeginOnly1() throws Exception {
		// removing old listener
		TCAPProvider serverProvider = server.tcapProvider;
		serverProvider.removeTCListener(server);

		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);

				server.awaitSent(EventType.UAbort);
				client.awaitReceived(EventType.UAbort);
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout - server timeout first (default)
		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		// 3. UAbort (from server)
		UserInformation _ui = serverProvider.getDialogPrimitiveFactory().createUserInformation();

		ASNBitString bitString = new ASNBitString();
		bitString.setBit(0);
		bitString.setBit(3);
		_ui.setChild(bitString);
		_ui.setIdentifier(TCAPTestHarness._ACN_);
		ApplicationContextName _acn = serverProvider.getDialogPrimitiveFactory()
				.createApplicationContextName(TCAPTestHarness._ACN_);
		server.sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);

		// aborts awaiting is implemented in server onDialogTimeout above
		server.awaitReceived(EventType.DialogRelease);
		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.UAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.UAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Idle end after Begin from Client
	 * 
	 * <pre>
	 * TC-BEGIN
	 * DialogTimeout (on client)
	 * UAbort (from client)
	 * </pre>
	 */
	@Test
	public void testAfterBeginOnly2() throws Exception {
		// client timeout first
		tcapStack1.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT * 2);

		TCAPProvider clientProvider = client.tcapProvider;
		clientProvider.removeTCListener(client);

		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address) {
			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);

				client.awaitSent(EventType.UAbort);
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout (on client)
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		// 3. UAbort (from client)
		UserInformation ui = clientProvider.getDialogPrimitiveFactory().createUserInformation();

		ASNBitString bitString = new ASNBitString();
		bitString.setBit(0);
		bitString.setBit(3);
		ui.setChild(bitString);
		ui.setIdentifier(TCAPTestHarness._ACN_);
		ApplicationContextName acn = clientProvider.getDialogPrimitiveFactory()
				.createApplicationContextName(TCAPTestHarness._ACN_);
		client.sendAbort(acn, ui, DialogServiceUserType.NoReasonGive);

		// aborts awaiting is implemented in client's onDialogTimeout method above
		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addSent(EventType.UAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Idle end after Continue from Server
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * DialogTimeout (on server)
	 * UAbort (from server)
	 * </pre>
	 */
	@Test
	public void testAfterContinue() throws TCAPException, TCAPSendException {
		// removing old listener
		TCAPProvider serverProvider = server.tcapProvider;
		serverProvider.removeTCListener(server);

		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);

				server.awaitSent(EventType.UAbort);
				client.awaitReceived(EventType.UAbort);
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();
		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		// 3. DialogTimeout (on server) - server timeout first (default)
		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		// 4. UAbort (from server)
		UserInformation ui = serverProvider.getDialogPrimitiveFactory().createUserInformation();

		ASNBitString bitString = new ASNBitString();
		bitString.setBit(0);
		bitString.setBit(3);
		ui.setChild(bitString);
		ui.setIdentifier(TCAPTestHarness._ACN_);
		ApplicationContextName acn = serverProvider.getDialogPrimitiveFactory()
				.createApplicationContextName(TCAPTestHarness._ACN_);
		server.sendAbort(acn, ui, DialogServiceUserType.NoReasonGive);

		// aborts awaiting is implemented in server onDialogTimeout above
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.UAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.UAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Idle end after several Continue from Server
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * TC-CONTINUE
	 * DialogTimeout (on server)
	 * UAbort (from server)
	 * </pre>
	 */
	@Test
	public void testAfterContinue2() throws TCAPException, TCAPSendException {
		// removing old listener
		TCAPProvider serverProvider = server.tcapProvider;
		serverProvider.removeTCListener(server);

		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);

				server.awaitSent(EventType.UAbort);
				client.awaitReceived(EventType.UAbort);
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();
		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		// 3. TC-CONTINUE
		client.sendContinue();
		client.awaitSent(EventType.Continue);
		server.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		// 4. DialogTimeout (on server)
		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		// 5. UAbort (from server)
		UserInformation ui = serverProvider.getDialogPrimitiveFactory().createUserInformation();

		ASNBitString bitString = new ASNBitString();
		bitString.setBit(0);
		bitString.setBit(3);
		ui.setChild(bitString);
		ui.setIdentifier(TCAPTestHarness._ACN_);
		ApplicationContextName acn = serverProvider.getDialogPrimitiveFactory()
				.createApplicationContextName(TCAPTestHarness._ACN_);
		server.sendAbort(acn, ui, DialogServiceUserType.NoReasonGive);

		// aborts awaiting is implemented in server onDialogTimeout above
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addReceived(EventType.UAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.UAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Idle end after End from Client
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * TC-CONTINUE
	 * TC-END
	 * </pre>
	 */
	@Test
	public void testAfterEnd() throws TCAPException, TCAPSendException {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();
		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		// 3. TC-CONTINUE
		client.sendContinue();
		client.awaitSent(EventType.Continue);
		server.awaitReceived(EventType.Continue);

		// 4. TC-END
		client.sendEnd(TerminationType.Basic);
		client.awaitSent(EventType.End);
		server.awaitReceived(EventType.End);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addSent(EventType.Continue);
		clientExpected.addSent(EventType.End);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.Continue);
		serverExpected.addReceived(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Idle end after several timed out Continue from Server
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * DialogTimeout (on server)
	 * TC-CONTINUE
	 * DialogTimeout (on server)
	 * PAbort
	 * </pre>
	 */
	@Test
	public void testAfterContinue_NoTimeout() throws TCAPException, TCAPSendException {
		// removing old listener
		TCAPProvider serverProvider = server.tcapProvider;
		serverProvider.removeTCListener(server);

		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			private boolean firstContinueSent = false;

			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);

				if (!firstContinueSent) {
					server.awaitSent(EventType.Continue);
					client.awaitReceived(EventType.Continue);

					firstContinueSent = true;
				}
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();
		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		// 3. DialogTimeout (on server)
		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		// 4. TC-CONTINUE
		server.sendContinue();
		TestEventUtils.updateStamp();

		// 5. DialogTimeout (on server)
		// continue awaiting is implemented in onDialogTimeout method above
		server.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT);

		// 6. PAbort
		client.awaitReceived(EventType.PAbort);
		server.awaitReceived(EventType.PAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.DialogTimeout);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Keep alive
	 * 
	 * <pre>
	 * TC-BEGIN
	 * DialogTimeout (on client)
	 * DialogTimeout (on server)
	 * DialogTimeout (on server)
	 * DialogTimeout (on server)
	 * </pre>
	 */
	@Test
	public void testKeepAlive() throws TCAPException, TCAPSendException {
		// removing old listener
		TCAPProvider serverProvider = server.tcapProvider;
		serverProvider.removeTCListener(server);

		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {
			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);
				dialog.keepAlive();
			}
		};

		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout (on client)
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		// 3. DialogTimeout (on server)
		// 4. DialogTimeout (on server)
		// 5. DialogTimeout (on server)
		final int serverTimeouts = 3;
		for (int i = 0; i < serverTimeouts; i++)
			server.awaitReceived(EventType.DialogTimeout);

		TestEventUtils.assertPassed(SERVER_DIALOG_TIMEOUT * serverTimeouts);
		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);

		for (int i = 0; i < serverTimeouts; i++)
			serverExpected.addReceived(EventType.DialogTimeout);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}
}
