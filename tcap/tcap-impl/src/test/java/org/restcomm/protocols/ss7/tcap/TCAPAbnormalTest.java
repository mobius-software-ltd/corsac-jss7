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
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.EventType;
import org.restcomm.protocols.ss7.tcap.listeners.Server;
import org.restcomm.protocols.ss7.tcap.listeners.TestEvent;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Test for abnormal situation processing
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class TCAPAbnormalTest extends SccpHarness {
	private static final long WAIT_TIME = 500;
	private static final long INVOKE_WAIT_TIME = 500;
	private static final int _DIALOG_TIMEOUT = 5000;

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

		tcapStack1 = new TCAPStackImpl("TCAPAbnormalTest", super.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPAbnormalTest", super.sccpProvider2, 8, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(0);
		tcapStack2.setInvokeTimeout(0);
		tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT + 200);

		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		server = new Server(tcapStack2, super.parameterFactory, peer2Address, peer1Address);
	}

	@After
	public void afterEach() {
		if (tcapStack1 != null) {
			this.tcapStack1.stop();
			this.tcapStack1 = null;
		}

		if (tcapStack2 != null) {
			this.tcapStack2.stop();
			this.tcapStack2 = null;
		}

		super.tearDown();
	}

	/**
	 * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version
	 * (supported only V2)
	 * 
	 * <pre>
	 * TC-BEGIN + AARQ apdu (unsupported protocol version)
	 * TC-ABORT + PAbortCauseType.NoCommonDialogPortion
	 * </pre>
	 */
	@Test
	public void badDialogProtocolVersionTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.NoCommonDialoguePortion);
	}

	/**
	 * Case of receiving TC-Begin that has a bad structure
	 * 
	 * <pre>
	 * TC-BEGIN (bad formatted)
	 * TC-ABORT + PAbortCauseType.IncorrectTxPortion
	 * </pre>
	 */
	@Test
	public void badSyntaxMessageTest() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, getMessageBadSyntax(), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.IncorrectTxPortion);
	}

	/**
	 * Case of receiving a reply for TC-Begin the message with a bad TAG
	 * 
	 * <pre>
	 * TC-BEGIN (bad message Tag - not Begin, Continue, ...)
	 * TC-ABORT + PAbortCauseType.UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void badMessageTagTest() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();

		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer1Address,
				peer2Address, getMessageBadTag(), 0, 0, false, null, null);
		this.sccpProvider2.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

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

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
	}

	/**
	 * Case of receiving a message TC-Continue when a local Dialog has been released
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE
	 * we are destroying a Dialog at a client side
	 * TC-CONTINUE
	 * TC-ABORT + PAbortCauseType.UnrecognizedTxID
	 * </pre>
	 */
	@Test
	public void noDialogTest() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();

		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.releaseDialog();
		server.sendContinue();

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

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
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedTxID);
	}

	/**
	 * Case of receiving a message TC-Continue without AARE apdu at the InitialSent
	 * state of a Dialog. This will cause an error
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-CONTINUE we are setting a State of a Client Dialog to TRPseudoState.InitialSent like it has just been sent a TC-BEGIN message 
	 * TC-CONTINUE 
	 * TC-ABORT + PAbortCauseType.AbnormalDialogue
	 * </pre>
	 */
	@Test
	public void abnormalDialogTest() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		server.sendContinue();

		client.awaitReceived(EventType.Continue);
		server.awaitSent(EventType.Continue);

		client.getCurDialog().setState(TRPseudoState.InitialSent);
		server.sendContinue();

		server.awaitSent(EventType.Continue);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 2, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 3, stamp + WAIT_TIME * 2);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();
		te = TestEvent.createReceivedEvent(EventType.Begin, null, 0, stamp);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 1, stamp + WAIT_TIME);
		serverExpectedEvents.add(te);
		te = TestEvent.createSentEvent(EventType.Continue, null, 2, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
		assertEquals(server.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
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
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		UserInformation userInformation = TcapFactory.createUserInformation();
		userInformation.setIdentifier(Arrays.asList(new Long[] { 1L, 2L, 3L }));
		userInformation.setChild(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
		server.sendAbort(null, userInformation, null);

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

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

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * Sending a message with unreachable CalledPartyAddress
	 * 
	 * <pre>
	 * TC - BEGIN
	 * </pre>
	 */
	@Test
	public void badAddressMessage1Test() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBeginUnreachableAddress(false, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + _DIALOG_TIMEOUT);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
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
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		client.sendBeginUnreachableAddress(true, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.Notice, null, 1, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
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
		long stamp = System.currentTimeMillis();

		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurDialog();
		tcapDialog.sendData(null, null, null, INVOKE_WAIT_TIME, null, null, true, false);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		client.awaitReceived(EventType.DialogRelease);

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

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
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
		long stamp = System.currentTimeMillis();

		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurDialog();
		tcapDialog.sendData(null, null, null, _DIALOG_TIMEOUT * 2L, null, null, true, false);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		client.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createSentEvent(EventType.Begin, null, 0, stamp);
		clientExpectedEvents.add(te);

		te = TestEvent.createReceivedEvent(EventType.DialogTimeout, null, 1, stamp + (_DIALOG_TIMEOUT));
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 2, stamp + (_DIALOG_TIMEOUT));
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
	}

	/**
	 * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version
	 * (supported only V2)
	 */
	@Test
	public void unrecognizedMessageTypeTest() throws Exception {
		long stamp = System.currentTimeMillis();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getUnrecognizedMessageTypeMessage()), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogRelease);

		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
	}

	public static ByteBuf getMessageWithUnsupportedProtocolVersion() {
		return Unpooled.wrappedBuffer(new byte[] { 98, 117, 72, 1, 1, 107, 69, 40, 67, 6, 7, 0, 17, (byte) 134, 5, 1, 1,
				1, (byte) 160, 56, 96, 54, (byte) 128, 2, 6, 64, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, (byte) 190,
				37, 40, 35, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 24, (byte) 160, (byte) 128, (byte) 128, 9,
				(byte) 150, 2, 36, (byte) 128, 3, 0, (byte) 128, 0, (byte) 242, (byte) 129, 7, (byte) 145, 19, 38,
				(byte) 152, (byte) 134, 3, (byte) 240, 0, 0, 108, 41, (byte) 161, 39, 2, 1, (byte) 128, 2, 2, 2, 79, 36,
				30, 4, 1, 15, 4, 16, (byte) 170, (byte) 152, (byte) 172, (byte) 166, 90, (byte) 205, 98, 54, 25, 14, 55,
				(byte) 203, (byte) 229, 114, (byte) 185, 17, (byte) 128, 7, (byte) 145, 19, 38, (byte) 136, (byte) 131,
				0, (byte) 242 });
	}

	public static ByteBuf getMessageBadSyntax() {
		return Unpooled.wrappedBuffer(new byte[] { 98, 6, 72, 1, 1, 1, 2, 3 });
	}

	public static ByteBuf getMessageBadTag() {
		return Unpooled.wrappedBuffer(new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 });
	}

	public static byte[] getUnrecognizedMessageTypeMessage() {
		return new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 };
	}
}
