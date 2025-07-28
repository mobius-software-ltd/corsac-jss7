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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEvent;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventFactory;
import org.restcomm.protocols.ss7.sccp.impl.events.TestEventUtils;
import org.restcomm.protocols.ss7.sccp.message.MessageFactory;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCConversationIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.TCResponseIndication;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationElementImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Client;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Server;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.events.EventType;

import io.netty.buffer.Unpooled;

/**
 * Test for abnormal situation processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPAbnormalTest extends SccpHarness {
	private static final long INVOKE_TIMEOUT = 0;

	private static final long CLIENT_DIALOG_TIMEOUT = 5500;
	private static final long SERVER_DIALOG_TIMEOUT = 5000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void afterEach() throws Exception {
		super.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		super.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		tcapStack1 = new TCAPStackImpl("TCAPAbnormalTest_1", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPAbnormalTest_2", this.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(INVOKE_TIMEOUT);
		tcapStack2.setInvokeTimeout(INVOKE_TIMEOUT);

		tcapStack1.setDialogIdleTimeout(CLIENT_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(SERVER_DIALOG_TIMEOUT);

		client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void beforeEach() {
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
	 * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version
	 * (supported only V2)
	 * 
	 * <pre>
	 * TC-BEGIN (unsupported protocol version)
	 * TC-ABORT + PAbortCauseType.NoCommonDialogPortion
	 * </pre>
	 */
	@Test
	public void badDialogProtocolVersionTest() throws Exception {
		// TODO:
		// we do not test this now because incorrect protocolVersion is not processed

		// long stamp = System.currentTimeMillis();
		// List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		// TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0,
		// stamp);
		// clientExpectedEvents.add(te);
		// te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		// clientExpectedEvents.add(te);
		//
		// List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		//
		// client.startClientDialog();
		// SccpDataMessage message =
		// this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
		// peer1Address, getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null,
		// null);
		// this.sccpProvider1.send(message, dummyCallback);
		// client.waitFor(WAIT_TIME);
		//
		// client.compareEvents(clientExpectedEvents);
		// server.compareEvents(serverExpectedEvents);
		//
		// assertEquals(client.pAbortCauseType, PAbortCause.NoCommonDialoguePortion);
	}

	/**
	 * Case of receiving TC-Query that has a bad structure with Abort
	 * 
	 * <pre>
	 * TC-Query (bad dialog portion formatted)
	 * TC-ABORT + PAbortCauseType.BadlyStructuredDialoguePortion
	 * </pre>
	 */
	@Test
	public void badSyntaxMessageTest_PAbort() throws Exception {
		// 1. TC-Query (bad dialog portion formatted)
		MessageFactory messageFactory1 = sccpProvider1.getMessageFactory();
		SccpDataMessage message = messageFactory1.createDataMessageClass1(peer2Address, peer1Address,
				Unpooled.wrappedBuffer(getMessageBadSyntax()), 0, 0, false, null, null);

		client.startClientDialog();
		sccpProvider1.send(message, dummyCallback);

		// 2. TC-ABORT + PAbortCauseType.BadlyStructuredDialoguePortion
		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCause.BadlyStructuredDialoguePortion, ind.getPAbortCause());
		}

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Case of receiving TC-Query that has a bad structure wirh Reject
	 * 
	 * <pre>
	 * TC-Query (no dialog portion + bad component portion)
	 * TC-CONTINUE 
	 * TC-End + PAbortCauseType.BadlyStructuredDialoguePortion
	 * </pre>
	 */
	// TODO: Second End is received after DialogRelease and can be not received at
	// all
	@Test
	public void badSyntaxMessageTest_Reject() throws Exception {
		// 1. TC-Query (no dialog portion + bad component portion)
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue(false);

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.Continue);
			TCConversationIndication ind = (TCConversationIndication) event.getEvent();

			ComponentPortion compp = ind.getComponents();
			assertNotNull(compp);
			assertEquals(2, compp.getComponents().size());

			assertEquals(ComponentType.ReturnResultLast, compp.getComponents().get(0).getType());
			assertEquals(ComponentType.InvokeNotLast, compp.getComponents().get(1).getType());
		}

		// 3. TC-End + PAbortCauseType.BadlyStructuredDialoguePortion
		MessageFactory messageFactory1 = sccpProvider1.getMessageFactory();
		SccpDataMessage message = messageFactory1.createDataMessageClass1(peer2Address, peer1Address,
				Unpooled.wrappedBuffer(getMessageBadSyntax2()), 0, 0, false, null, null);

		sccpProvider1.send(message, dummyCallback);
		server.sendEnd(false);

		server.awaitSent(EventType.End);
		client.awaitReceived(EventType.End);

		server.awaitReceived(EventType.DialogRelease);
		client.awaitReceived(EventType.DialogRelease);

		// client.awaitReceived(EventType.End);
		// {
		// TestEvent<EventType> event = client.getNextEventWithSkip(EventType.End, 1);
		// TCResponseIndication ind = (TCResponseIndication) event.getEvent();
		//
		// ComponentPortion compp = ind.getComponents();
		// assertNotNull(compp);
		// assertEquals(1, compp.getComponents().size());
		//
		// Reject rej = (Reject) compp.getComponents().get(0);
		// assertEquals(ComponentType.Reject, rej.getType());
		// assertEquals(RejectProblem.generalUnrecognisedComponentType,
		// rej.getProblem());
		// }
		//

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.End);
		clientExpected.addReceived(EventType.DialogRelease);
		// clientExpected.addReceived(EventType.End); // why end is received after
		// dialog release?

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addSent(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		// TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Case of receiving a reply for TC-Begin the message with a bad TAG
	 * 
	 * <pre>
	 * TC-BEGIN (bad message Tag - not Begin, Continue, ...)
	 * TC-CONTINUE
	 * DialogTimeout
	 * TC-ABORT + PAbortCauseType.UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void badMessageTagTest() throws Exception {
		// 1. TC-BEGIN (bad message Tag - not Begin, Continue, ...)
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue(false);

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		// 3. TC-ABORT + PAbortCauseType.UnrecognizedMessageType
		MessageFactory messageFactory2 = sccpProvider2.getMessageFactory();
		SccpDataMessage message = messageFactory2.createDataMessageClass1(peer1Address, peer2Address,
				Unpooled.wrappedBuffer(getMessageBadTag()), 0, 0, false, null, null);

		sccpProvider2.send(message, dummyCallback);
		TestEventUtils.updateStamp();

		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCause.InconsistentDialoguePortion, ind.getPAbortCause());
		}
		server.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCause.UnrecognizedPackageType, ind.getPAbortCause());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Case of receiving a message TC-Continue when a local Dialog has been released
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * we are destroying a Dialog at a client side
	 * TC-CONTINUE
	 * TC-END + PAbortCauseType.UnrecognizedTxID
	 * </pre>
	 */
	@Test
	public void noDialogTest() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue(false);

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		// 3. we are destroying a Dialog at a client side
		client.releaseDialog();

		// 4. TC-CONTINUE
		server.sendContinue(false);
		server.awaitSent(EventType.Continue);

		// 5. TC-END + PAbortCauseType.UnrecognizedTxID
		server.awaitReceived(EventType.End);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.End);
			TCResponseIndication ind = (TCResponseIndication) event.getEvent();

			ComponentPortion compp = ind.getComponents();
			assertNotNull(compp);
			assertEquals(1, compp.getComponents().size());

			Reject rej = (Reject) compp.getComponents().get(0);
			assertEquals(ComponentType.Reject, rej.getType());
			assertEquals(RejectProblem.transactionUnassignedRespondingTransID, rej.getProblem());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.End);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Case of receiving a message TC-Continue without AARE apdu at the InitialSent
	 * state of a Dialog. This will cause an error
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * we are setting a State of a Client Dialog to TRPseudoState.InitialSent like it has
	 * just been sent a TC-BEGIN message 
	 * TC-CONTINUE
	 * TC-ABORT + PAbortCauseType.AbnormalDialogue
	 * </pre>
	 */
	@Test
	public void abnormalDialogTest() throws Exception {
		// TODO:
		// we do not test this now because apdu's are not used in AMSI

		// TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		// clientExpected.addSent(EventType.Begin);
		// clientExpected.addReceived(EventType.Continue);
		// clientExpected.addReceived(EventType.PAbort);
		// clientExpected.addReceived(EventType.DialogRelease);

		// TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		// serverExpected.addReceived(EventType.Begin);
		// serverExpected.addSent(EventType.Continue);
		// serverExpected.addSent(EventType.Continue);
		// serverExpected.addReceived(EventType.PAbort);
		// serverExpected.addReceived(EventType.DialogRelease);

		// client.startClientDialog();
		// client.sendBegin();
		// Thread.sleep(WAIT_TIME);

		// server.sendContinue();
		// Thread.sleep(WAIT_TIME);

		// client.getCurDialog().setState(TRPseudoState.InitialSent);
		// server.sendContinue();
		// Thread.sleep(WAIT_TIME);

		// client.compareEvents(clientExpected.getEvents());
		// server.compareEvents(serverExpected.getEvents());

		// assertEquals(client.pAbortCauseType, PAbortCause.AbnormalDialogue);
		// assertEquals(server.pAbortCauseType, PAbortCause.AbnormalDialogue);
	}

	/**
	 * TC-U-Abort as a response to TC-Begin
	 *
	 * <pre>
	 * TC-BEGIN 
	 * TC-ABORT + UserAbort by TCAP user
	 * </pre>
	 */
	@Test
	public void userAbortTest() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-ABORT + UserAbort by TCAP user
		UserInformationImpl ui = new UserInformationImpl();
		UserInformationElementImpl uie = new UserInformationElementImpl();
		uie.setIdentifier(Arrays.asList(1L, 2L, 3L));
		uie.setChild(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
		ui.setUserInformationElements(Arrays.asList(uie));
		ApplicationContext ac = TcapFactory.createApplicationContext(Arrays.asList(1L, 2L, 3L));

		server.sendAbort(ac, ui);
		server.awaitSent(EventType.UAbort);
		client.awaitReceived(EventType.UAbort);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.UAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.UAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Sending a message with unreachable CalledPartyAddress
	 * 
	 * <pre>
	 * TC-BEGIN with unreachable CalledPartyAddress
	 * DialogTimeout
	 * </pre>
	 */
	@Test
	public void badAddressMessage1Test() throws Exception {
		// 1. TC-BEGIN with unreachable CalledPartyAddress
		client.startClientDialog();
		client.sendBeginUnreachableAddress(false, dummyCallback);

		client.awaitSent(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Sending a message with unreachable CalledPartyAddress + returnMessageOnError
	 * -> TC-Notice
	 * 
	 * <pre>
	 * TC-BEGIN + returnMessageOnError
	 * TC-NOTICE
	 * </pre>
	 */
	@Test
	public void badAddressMessage2Test() throws Exception {
		// 1. TC-BEGIN + returnMessageOnError
		client.startClientDialog();
		client.sendBeginUnreachableAddress(true, dummyCallback); // true -> notice will be returned

		client.awaitSent(EventType.Begin);

		// 2. TC-NOTICE
		client.awaitReceived(EventType.Notice);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Notice);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Invoke timeouts before dialog timeout
	 * 
	 * <pre>
	 * TC-BEGIN 
	 * InvokeTimeout 
	 * DialogTimeout
	 * </pre>
	 */
	@Test
	public void invokeTimeoutTest1() throws Exception {
		final long invokeTimeout = CLIENT_DIALOG_TIMEOUT / 2;

		// 1. TC-BEGIN
		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurrentDialog();
		Invoke invoke = client.createNewInvoke();
		invoke.setTimeout(invokeTimeout);
		tcapDialog.sendComponent(invoke);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		client.awaitSent(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. InvokeTimeout
		client.awaitReceived(EventType.InvokeTimeout);
		TestEventUtils.assertPassed(invokeTimeout);

		// 3. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.InvokeTimeout);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	/**
	 * Invoke timeouts after dialog timeout
	 * 
	 * <pre>
	 * TC-BEGIN
	 * DialogTimeout
	 * </pre>
	 */
	@Test
	public void invokeTimeoutTest2() throws Exception {
		final long invokeTimeout = CLIENT_DIALOG_TIMEOUT * 2;

		// 1. TC-BEGIN
		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurrentDialog();
		Invoke invoke = client.createNewInvoke();
		invoke.setTimeout(invokeTimeout);
		tcapDialog.sendComponent(invoke);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		client.awaitSent(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(CLIENT_DIALOG_TIMEOUT);

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.DialogTimeout);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	public static byte[] getMessageWithUnsupportedProtocolVersion() {
		return new byte[] { (byte) 0xe2, 0x2a, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x01, (byte) 0xf9, 0x0c,
				(byte) 0xda, 0x01, 0x04, (byte) 0xdc, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xe8, 0x14,
				(byte) 0xed, 0x08, (byte) 0xcf, 0x01, 0x01, (byte) 0xd1, 0x01, 0x0c, (byte) 0xf0, 0x00, (byte) 0xe9,
				0x08, (byte) 0xcf, 0x01, 0x02, (byte) 0xd1, 0x01, 0x0d, (byte) 0xf0, 0x00 };
	}

	// bad structured dialog portion -> PAbort
	public static byte[] getMessageBadSyntax() {
		return new byte[] { (byte) 0xe2, 0x2a, (byte) 0xc7, 0x04, 0x00, 0x00, 0x00, 0x01, (byte) 0xf9, 0x0c,
				(byte) 0xda, 0x01, 0x03, (byte) 0xff, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x13, 0x02, (byte) 0xe8, 0x14,
				(byte) 0xed, 0x08, (byte) 0xcf, 0x01, 0x01, (byte) 0xd1, 0x01, 0x0c, (byte) 0xf0, 0x00, (byte) 0xe9,
				0x08, (byte) 0xcf, 0x01, 0x02, (byte) 0xd1, 0x01, 0x0d, (byte) 0xf0, 0x00 };
	}

	// bad structured component portion -> Reject
	public static byte[] getMessageBadSyntax2() {
		return new byte[] { (byte) 0xe5, 13, (byte) 0xc7, 8, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0 };
	}

	public static byte[] getMessageBadTag() {
		return new byte[] { 106, 13, (byte) 0xc7, 8, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0 };
	}
}
