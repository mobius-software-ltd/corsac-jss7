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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPException;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPSendException;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.EventTestHarness;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.EventType;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.TestEvent;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * Test for call flow.
 *
 * @author baranowb
 * @author yulianoifa
 *
 */
public class DialogIdleEndTest extends SccpHarness {
	private static final int _DIALOG_TIMEOUT = 5000;
	private static final int _WAIT = _DIALOG_TIMEOUT / 2 - 200;
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

		tcapStack1 = new TCAPStackImpl("DialogIdleEndTest_1", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("DialogIdleEndTest_2", this.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(0);
		tcapStack2.setInvokeTimeout(0);
		tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT * 2);
		tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT); // so other side dont timeout :)
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
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				// send abort :)
				try {
					// UI is required...
					UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory()
							.createUserInformationElement();
					uie = this.tcapProvider.getDialogPrimitiveFactory().createUserInformationElement();
					ASNBitString bs = new ASNBitString();
					bs.setBit(0);
					bs.setBit(3);
					uie.setChild(bs);
					uie.setIdentifier(_ACN_);
					ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContext(_ACN_);
					UserInformationImpl ui = new UserInformationImpl();
					ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
					sendAbort(_acn, ui);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT * 3);
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
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				// send abort :)
				try {
					// UI is required...
					UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory()
							.createUserInformationElement();
					ASNBitString bs = new ASNBitString();
					bs.setBit(0);
					bs.setBit(3);
					uie.setChild(bs);
					uie.setIdentifier(_ACN_);
					ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContext(_ACN_);
					UserInformationImpl ui = new UserInformationImpl();
					ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
					sendAbort(_acn, ui);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT * 3);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				// send abort :)
				try {
					// UI is required...
					UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory()
							.createUserInformationElement();
					ASNBitString bs = new ASNBitString();
					bs.setBit(0);
					bs.setBit(3);
					uie.setChild(bs);
					uie.setIdentifier(_ACN_);
					ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContext(_ACN_);
					UserInformationImpl ui = new UserInformationImpl();
					ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
					sendAbort(_acn, ui);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT);
		server.sendContinue(true);
		EventTestHarness.waitFor(_WAIT * 3);
		// waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue2() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				// send abort :)
				try {
					// UI is required...
					UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory()
							.createUserInformationElement();
					ASNBitString bs = new ASNBitString();
					bs.setBit(0);
					bs.setBit(3);
					uie.setChild(bs);
					uie.setIdentifier(_ACN_);
					ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContext(_ACN_);
					UserInformationImpl ui = new UserInformationImpl();
					ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
					sendAbort(_acn, ui);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 3, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 3, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 4, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 5, stamp + _WAIT * 3 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT);
		server.sendContinue(true);
		EventTestHarness.waitFor(_WAIT);
		client.sendContinue(true);
		EventTestHarness.waitFor(_WAIT * 3);
		// waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterEnd() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				// send abort :)
				try {
					// UI is required...
					UserInformationElement uie = this.tcapProvider.getDialogPrimitiveFactory()
							.createUserInformationElement();
					ASNBitString bs = new ASNBitString();
					bs.setBit(0);
					bs.setBit(3);
					uie.setChild(bs);
					uie.setIdentifier(_ACN_);
					ApplicationContext _acn = this.tcapProvider.getDialogPrimitiveFactory()
							.createApplicationContext(_ACN_);
					UserInformationImpl ui = new UserInformationImpl();
					ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
					sendAbort(_acn, ui);
				} catch (TCAPSendException e) {

					e.printStackTrace();
					fail("Got error! " + e);
				}
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
		clientExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 3, stamp + _WAIT * 4);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 4);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 3);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 3, stamp + _WAIT * 4);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 4);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT);
		server.sendContinue(true);
		EventTestHarness.waitFor(_WAIT);
		client.sendContinue(true);
		EventTestHarness.waitFor(_WAIT);
		client.sendEnd(true);
		EventTestHarness.waitFor(_WAIT);
		// waitForEnd();
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	@Test
	public void testAfterContinue_NoTimeout() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			private boolean sendContinue = false;

			@Override
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);
				if (!sendContinue) {
					// send continue
					try {
						sendContinue(true);
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
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + _WAIT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 3, stamp + _WAIT * 2 + _DIALOG_TIMEOUT);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 4, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 5, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 6, stamp + _WAIT * 2 + _DIALOG_TIMEOUT * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT);
		server.sendContinue(true);
		EventTestHarness.waitFor(_WAIT * 7);
		// client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}

	@Test
	public void testKeepAlive() throws TCAPException, TCAPSendException {

		this.client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);

		this.server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address) {

			@Override
			public void onDialogTimeout(Dialog d) {

				super.onDialogTimeout(d);

				d.keepAlive();
			}

		};

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp + _WAIT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _WAIT + _DIALOG_TIMEOUT * 2); // *2
																													// cause
																													// its
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _WAIT + _DIALOG_TIMEOUT * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp + _WAIT);
		serverExpectedEvents.add(te);
		for (int index = 1; index < 4; index++) {
			te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, index,
					stamp + _WAIT + _DIALOG_TIMEOUT * index);
			serverExpectedEvents.add(te);
		}

		client.startClientDialog();
		EventTestHarness.waitFor(_WAIT);
		client.sendBegin();
		EventTestHarness.waitFor(_WAIT * 7);
		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

	}
}
