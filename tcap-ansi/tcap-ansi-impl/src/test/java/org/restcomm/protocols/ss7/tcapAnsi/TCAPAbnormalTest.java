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
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.restcomm.protocols.ss7.tcapAnsi.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationElementImpl;
import org.restcomm.protocols.ss7.tcapAnsi.asn.UserInformationImpl;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Client;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.EventTestHarness;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.EventType;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.Server;
import org.restcomm.protocols.ss7.tcapAnsi.listeners.TestEvent;

import io.netty.buffer.Unpooled;

/**
 * Test for abnormal situation processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPAbnormalTest extends SccpHarness {
	public static final long WAIT_TIME = 500;
	public static final long INVOKE_WAIT_TIME = 500;
	private static final int _DIALOG_TIMEOUT = 5000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	@Before
	public void afterEach() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1,
				8);
		peer2Address = super.parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2,
				8);

		this.tcapStack1 = new TCAPStackImpl("TCAPAbnormalTest_1", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("TCAPAbnormalTest_2", this.sccpProvider2, 8, workerPool);

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT + 200);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT);

		this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void beforeEach() {
		this.tcapStack1.stop();
		this.tcapStack2.stop();

		super.tearDown();
	}

	/**
	 * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version
	 * (supported only V2) TC-BEGIN (unsupported protocol version) TC-ABORT +
	 * PAbortCauseType.NoCommonDialogPortion
	 */
	@Test
	public void badDialogProtocolVersionTest() throws Exception {

		// TODO:
		// we do not test this now because incorrect protocolVersion is not processed

//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//
//        client.startClientDialog();
//        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
//                getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null, null);
//        this.sccpProvider1.send(message, dummyCallback);
//        client.waitFor(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.NoCommonDialoguePortion);
	}

	/**
	 * Case of receiving TC-Query that has a bad structure TC-Query (bad dialog
	 * portion formatted) TC-ABORT + PAbortCauseType.BadlyStructuredDialoguePortion
	 */
	@Test
	public void badSyntaxMessageTest_PAbort() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getMessageBadSyntax()), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);
		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCause.BadlyStructuredDialoguePortion);
	}

	/**
	 * Case of receiving TC-Query that has a bad structure TC-Query (no dialog
	 * portion + bad component portion) TC-End +
	 * PAbortCauseType.BadlyStructuredDialoguePortion
	 */
	@Test
	public void badSyntaxMessageTest_Reject() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 2, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.End, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		server.sendContinue(false);
		Thread.sleep(WAIT_TIME);

		assertNull(client.rejectProblem);

		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getMessageBadSyntax2()), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);
		Thread.sleep(WAIT_TIME);
//        client.waitFor(WAIT_TIME);

		server.sendEnd(false);

		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		// server.compareEvents(serverExpectedEvents);

		assertEquals(client.rejectProblem, RejectProblem.generalUnrecognisedComponentType);
	}

	@Test
	/**
	 * Case of receiving a reply for TC-Begin the message with a bad TAG TC-BEGIN
	 * (bad message Tag - not Begin, Continue, ...) TC-ABORT +
	 * PAbortCauseType.UnrecognizedMessageType
	 */
	public void badMessageTagTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + WAIT_TIME + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		server.sendContinue(false);
		Thread.sleep(WAIT_TIME);

		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer1Address,
				peer2Address, Unpooled.wrappedBuffer(getMessageBadTag()), 0, 0, false, null, null);
		this.sccpProvider2.send(message, dummyCallback);
		Thread.sleep(WAIT_TIME + _DIALOG_TIMEOUT);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		// assertEquals(client.pAbortCauseType,
		// PAbortCauseType.UnrecognizedMessageType);
		assertEquals(server.pAbortCauseType, PAbortCause.UnrecognizedPackageType);
	}

	@Test
	/**
	 * Case of receiving a message TC-Continue when a local Dialog has been released
	 * TC-BEGIN TC-CONTINUE we are destroying a Dialog at a client side TC-CONTINUE
	 * TC-ABORT + PAbortCauseType.UnrecognizedTxID
	 */
	public void noDialogTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.End, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		server.sendContinue(false);
		Thread.sleep(WAIT_TIME);

		client.releaseDialog();
		server.sendContinue(false);
		Thread.sleep(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertNull(server.pAbortCauseType);
		assertEquals(server.rejectProblem, RejectProblem.transactionUnassignedRespondingTransID);
	}

	/**
	 * Case of receiving a message TC-Continue without AARE apdu at the InitialSent
	 * state of a Dialog. This will cause an error TC-BEGIN TC-CONTINUE we are
	 * setting a State of a Client Dialog to TRPseudoState.InitialSent like it has
	 * just been sent a TC-BEGIN message TC-CONTINUE TC-ABORT +
	 * PAbortCauseType.AbnormalDialogue
	 */
	@Test
	public void abnormalDialogTest() throws Exception {

		// TODO:
		// we do not test this now because apdu's are not used in AMSI

//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//        te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
//        serverExpectedEvents.add(te);
//
//        client.startClientDialog();
//        client.sendBegin();
//        Thread.sleep(WAIT_TIME);
//
//        server.sendContinue();
//        Thread.sleep(WAIT_TIME);
//
//        client.getCurDialog().setState(TRPseudoState.InitialSent);
//        server.sendContinue();
//        Thread.sleep(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.AbnormalDialogue);
//        assertEquals(server.pAbortCauseType, PAbortCause.AbnormalDialogue);
	}

	/**
	 * TC-U-Abort as a response to TC-Begin
	 *
	 * TC-BEGIN TC-ABORT + UserAbort by TCAP user
	 */
	@Test
	public void userAbortTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.UAbort, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.UAbort, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		UserInformationImpl ui = new UserInformationImpl();
		UserInformationElementImpl uie = new UserInformationElementImpl();
		uie.setIdentifier(Arrays.asList(new Long[] { 1L, 2L, 3L }));
		uie.setChild(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
		ui.setUserInformationElements(Arrays.asList(new UserInformationElement[] { uie }));
		ApplicationContext ac = TcapFactory.createApplicationContext(Arrays.asList(new Long[] { 1L, 2L, 3L }));
		server.sendAbort(ac, ui);
		Thread.sleep(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending a message with unreachable CalledPartyAddress TC-BEGIN
	 */
	@Test
	public void badAddressMessage1Test() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		client.sendBeginUnreachableAddress(false, dummyCallback);
		Thread.sleep(WAIT_TIME);
		Thread.sleep(_DIALOG_TIMEOUT);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending a message with unreachable CalledPartyAddress + returnMessageOnError
	 * -> TC-Notice TC-BEGIN + returnMessageOnError TC-NOTICE
	 */
	@Test
	public void badAddressMessage2Test() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Notice, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		client.sendBeginUnreachableAddress(true, dummyCallback);
		Thread.sleep(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Invoke timeouts before dialog timeout TC-BEGIN InvokeTimeout DialogTimeout
	 */
	@Test
	public void invokeTimeoutTest1() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.InvokeTimeout, null, 1, stamp + INVOKE_WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurDialog();
		Invoke invoke = client.createNewInvoke();
		invoke.setTimeout(INVOKE_WAIT_TIME);
		tcapDialog.sendComponent(invoke);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		Thread.sleep(WAIT_TIME);
		Thread.sleep(_DIALOG_TIMEOUT);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Invoke timeouts after dialog timeout TC-BEGIN DialogTimeout
	 */
	@Test
	public void invokeTimeoutTest2() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + (_DIALOG_TIMEOUT));
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + (_DIALOG_TIMEOUT));
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurDialog();
		Invoke invoke = client.createNewInvoke();
		invoke.setTimeout(_DIALOG_TIMEOUT * 2);
		tcapDialog.sendComponent(invoke);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		Thread.sleep(WAIT_TIME);
		Thread.sleep(_DIALOG_TIMEOUT * 2);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
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

//    @Test
//    public void UnrecognizedMessageTypeTest() throws Exception {
//
//        // case of receiving TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
//        long stamp = System.currentTimeMillis();
//        List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
//        TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
//        clientExpectedEvents.add(te);
//        te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
//        clientExpectedEvents.add(te);
//
//        List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
//
//        client.startClientDialog();
//        SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address, peer1Address,
//                getUnrecognizedMessageTypeMessage(), 0, 0, false, null, null);
//        this.sccpProvider1.send(message, dummyCallback);
//        client.waitFor(WAIT_TIME);
//
//        client.compareEvents(clientExpectedEvents);
//        server.compareEvents(serverExpectedEvents);
//
//        assertEquals(client.pAbortCauseType, PAbortCause.UnrecognizedMessageType);
//    }
//
//    public static byte[] getUnrecognizedMessageTypeMessage() {
//        return new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 };
//    }
}
