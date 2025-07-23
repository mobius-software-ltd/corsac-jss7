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

import static org.junit.Assert.fail;

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
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TerminationType;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.DialogServiceUserType;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.listeners.EventTestHarness;
import org.restcomm.protocols.ss7.tcap.listeners.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.TestEvent;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class DialogIdleEndTest extends SccpHarness {
	private static final int _DIALOG_TIMEOUT = 1000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;

	private SccpAddress peer1Address;
	private SccpAddress peer2Address;

	private Client client;
	private Server server;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "DialogIdleEndTestSccpStack1";
		this.sccpStack2Name = "DialogIdleEndTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		this.tcapStack1 = new TCAPStackImpl("DialogIdleEndTest1", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("DialogIdleEndTest2", this.sccpProvider2, 8, workerPool);

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT * 2);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT); // so other side don't timeout :)
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
	public void testAfterBeginOnly1() throws TCAPException, TCAPSendException {
		// server timeout first
		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);
				EventTestHarness.waitFor(20);

				// send abort :)
				try {
					// UI is required...
					UserInformation _ui = this.tcapProvider.getDialogPrimitiveFactory().createUserInformation();

					ASNBitString bitString = new ASNBitString();
					bitString.setBit(0);
					bitString.setBit(3);
					_ui.setChild(bitString);
					_ui.setIdentifier(_ACN_);
					ApplicationContextName _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContextName(_ACN_);
					sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 2, stamp + _DIALOG_TIMEOUT);
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
		// client timeout first
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT * 2);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);

				// send abort :)
				try {
					// UI is required...
					UserInformation _ui = this.tcapProvider.getDialogPrimitiveFactory().createUserInformation();

					ASNBitString bitString = new ASNBitString();
					bitString.setBit(0);
					bitString.setBit(3);
					_ui.setChild(bitString);
					_ui.setIdentifier(_ACN_);
					ApplicationContextName _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContextName(_ACN_);
					sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);

				// send abort :)
				try {
					// UI is required...

					UserInformation _ui = this.tcapProvider.getDialogPrimitiveFactory().createUserInformation();

					ASNBitString bitString = new ASNBitString();
					bitString.setBit(0);
					bitString.setBit(3);
					_ui.setChild(bitString);
					_ui.setIdentifier(_ACN_);
					ApplicationContextName _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContextName(_ACN_);
					sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT);
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

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);

				// send abort :)
				try {
					// UI is required...
					UserInformation _ui = this.tcapProvider.getDialogPrimitiveFactory().createUserInformation();

					ASNBitString bitString = new ASNBitString();
					bitString.setBit(0);
					bitString.setBit(3);
					_ui.setChild(bitString);
					_ui.setIdentifier(_ACN_);
					ApplicationContextName _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContextName(_ACN_);
					sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 4, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 5, stamp + _DIALOG_TIMEOUT);
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

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);

				// send abort :)
				try {
					// UI is required...
					UserInformation _ui = this.tcapProvider.getDialogPrimitiveFactory().createUserInformation();

					ASNBitString bitString = new ASNBitString();
					bitString.setBit(0);
					bitString.setBit(3);
					_ui.setChild(bitString);
					_ui.setIdentifier(_ACN_);
					ApplicationContextName _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContextName(_ACN_);
					sendAbort(_acn, _ui, DialogServiceUserType.NoReasonGive);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 3, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 3, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.sendContinue();
		client.awaitSent(EventType.Continue);
		server.awaitReceived(EventType.Continue);

		client.sendEnd(TerminationType.Basic);
		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue_NoTimeout() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			private boolean sendContinue = false;

			@Override
			public void onDialogTimeout(Dialog dialog) {

				super.onDialogTimeout(dialog);
				if (!sendContinue) {
					// send continue
					try {
						sendContinue();
					} catch (TCAPException e) {
						e.printStackTrace();
						fail("Received exception. Message: " + e.getMessage());
					} catch (TCAPSendException e) {
						e.printStackTrace();
						fail("Received exception. Message: " + e.getMessage());
					}
					sendContinue = true;
				}

			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 3, stamp + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 4, stamp + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 5, stamp + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 6, stamp + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();
		server.awaitReceived(EventType.DialogTimeout);
		server.awaitReceived(EventType.DialogTimeout);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testKeepAlive() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog dialog) {
				super.onDialogTimeout(dialog);
				dialog.keepAlive();
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT * 2);

		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();

		final int timeouts = 3;
		for (int i = 0; i < timeouts; i++) {
			te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, i, stamp + _DIALOG_TIMEOUT * (i + 1));
			serverExpectedEvents.add(te);

			server.awaitReceived(EventType.DialogTimeout);
		}

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

}
