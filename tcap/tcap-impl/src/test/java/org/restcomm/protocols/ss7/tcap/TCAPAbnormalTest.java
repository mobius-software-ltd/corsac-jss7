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
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.TRPseudoState;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCPAbortIndication;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.UserInformation;
import org.restcomm.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.Server;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;

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
	private static final long INVOKE_TIMEOUT = 0;
	private static final long DIALOG_TIMEOUT = 5000;

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
		MessageFactory messageFactory1 = sccpProvider1.getMessageFactory();
		SccpDataMessage message = messageFactory1.createDataMessageClass1(peer2Address, peer1Address,
				getMessageWithUnsupportedProtocolVersion(), 0, 0, false, null, null);

		// 1. TC-BEGIN + AARQ apdu (unsupported protocol version)
		client.startClientDialog();
		sccpProvider1.send(message, dummyCallback);

		// 2. TC-ABORT + PAbortCauseType.NoCommonDialogPortion
		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.NoCommonDialoguePortion, ind.getPAbortCause());
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
	 * Case of receiving TC-Begin that has a bad structure
	 * 
	 * <pre>
	 * TC-BEGIN (bad formatted)
	 * TC-ABORT + PAbortCauseType.IncorrectTxPortion
	 * </pre>
	 */
	@Test
	public void badSyntaxMessageTest() throws Exception {
		// 1. TC-BEGIN (bad formatted)
		client.startClientDialog();

		MessageFactory messageFactory1 = sccpProvider1.getMessageFactory();
		SccpDataMessage message = messageFactory1.createDataMessageClass1(peer2Address, peer1Address,
				getMessageBadSyntax(), 0, 0, false, null, null);
		sccpProvider1.send(message, dummyCallback);

		// 2. TC-ABORT + PAbortCauseType.IncorrectTxPortion
		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.IncorrectTxPortion, ind.getPAbortCause());
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
	 * Case of receiving a reply for TC-Continue the message with a bad TAG
	 * 
	 * <pre>
	 * TC-BEGIN
	 * TC-Continue (bad message Tag)
	 * TC-ABORT + PAbortCauseType.UnrecognizedMessageType
	 * </pre>
	 */
	@Test
	public void badMessageTagTest() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-Continue (bad message Tag)
		server.sendContinue();

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);
		TestEventUtils.updateStamp();

		// 3. TC-ABORT + PAbortCauseType.UnrecognizedMessageType
		MessageFactory messageFactory2 = sccpProvider2.getMessageFactory();
		SccpDataMessage message = messageFactory2.createDataMessageClass1(peer1Address, peer2Address,
				getMessageBadTag(), 0, 0, false, null, null);
		sccpProvider2.send(message, dummyCallback);

		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(DIALOG_TIMEOUT);

		client.awaitReceived(EventType.PAbort);
		server.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.UnrecognizedMessageType, ind.getPAbortCause());
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
	 * TC-ABORT + PAbortCauseType.UnrecognizedTxID
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
		server.sendContinue();

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		// 3. we are destroying a Dialog at a client side
		client.releaseDialog();

		// 4. TC-CONTINUE
		server.sendContinue();

		// client is off already so no awaiting for client events
		server.awaitSent(EventType.Continue);

		// 5. TC-ABORT + PAbortCauseType.UnrecognizedTxID
		server.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.UnrecognizedTxID, ind.getPAbortCause());
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
		serverExpected.addReceived(EventType.PAbort);
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
	 * we are setting a State of a Client Dialog to TRPseudoState.InitialSent like it has just been sent a TC-BEGIN message 
	 * TC-CONTINUE 
	 * TC-ABORT + PAbortCauseType.AbnormalDialogue
	 * </pre>
	 */
	@Test
	public void abnormalDialogTest() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBegin();

		client.awaitSent(EventType.Begin);
		server.awaitReceived(EventType.Begin);

		// 2. TC-CONTINUE
		server.sendContinue();

		server.awaitSent(EventType.Continue);
		client.awaitReceived(EventType.Continue);

		// 3. we are setting a State of a Client Dialog to TRPseudoState.InitialSent
		// like it has just been sent a TC-BEGIN message
		client.getCurrentDialog().setState(TRPseudoState.InitialSent);

		// 4. TC-CONTINUE
		server.sendContinue();

		// client has abnormal state so no awaiting for client events
		server.awaitSent(EventType.Continue);

		// 5. TC-ABORT + PAbortCauseType.AbnormalDialogue
		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.AbnormalDialogue, ind.getPAbortCause());
		}
		server.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = server.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.AbnormalDialogue, ind.getPAbortCause());
		}

		client.awaitReceived(EventType.DialogRelease);
		server.awaitReceived(EventType.DialogRelease);

		// Client expected events
		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addSent(EventType.Begin);
		clientExpected.addReceived(EventType.Continue);
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		// Server expected events
		TestEventFactory<EventType> serverExpected = TestEventFactory.create();
		serverExpected.addReceived(EventType.Begin);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addSent(EventType.Continue);
		serverExpected.addReceived(EventType.PAbort);
		serverExpected.addReceived(EventType.DialogRelease);

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
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
		UserInformation userInformation = TcapFactory.createUserInformation();
		userInformation.setIdentifier(Arrays.asList(new Long[] { 1L, 2L, 3L }));
		userInformation.setChild(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
		server.sendAbort(null, userInformation, null);

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
	 * TC - BEGIN
	 * DialogTimeout
	 * </pre>
	 */
	@Test
	public void badAddressMessage1Test() throws Exception {
		// 1. TC-BEGIN
		client.startClientDialog();
		client.sendBeginUnreachableAddress(false, dummyCallback);
		TestEventUtils.updateStamp();

		client.awaitSent(EventType.Begin);

		// 2. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(DIALOG_TIMEOUT);

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
		client.sendBeginUnreachableAddress(true, dummyCallback);

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
		final long invokeTimeout = 500;

		// 1. TC-BEGIN
		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurrentDialog();
		tcapDialog.sendData(null, null, null, invokeTimeout, null, null, true, false);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		client.awaitSent(EventType.Begin);
		TestEventUtils.updateStamp();

		// 2. InvokeTimeout
		client.awaitReceived(EventType.InvokeTimeout);
		TestEventUtils.assertPassed(invokeTimeout);

		// 3. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(DIALOG_TIMEOUT);

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
		// 1. TC-BEGIN
		client.startClientDialog();

		DialogImpl tcapDialog = client.getCurrentDialog();
		tcapDialog.sendData(null, null, null, DIALOG_TIMEOUT * 2L, null, null, true, false);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		TestEventUtils.updateStamp();

		// 2. DialogTimeout
		client.awaitReceived(EventType.DialogTimeout);
		TestEventUtils.assertPassed(DIALOG_TIMEOUT);

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
	 * A case of receiving
	 * 
	 * <pre>
	 * TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
	 * </pre>
	 */
	@Test
	public void unrecognizedMessageTypeTest() throws Exception {
		MessageFactory messageFactory1 = sccpProvider1.getMessageFactory();
		SccpDataMessage message = messageFactory1.createDataMessageClass1(peer2Address, peer1Address,
				getUnrecognizedMessageTypeMessage(), 0, 0, false, null, null);

		// 1. TC-Begin + AARQ apdu + unsupported protocol version (supported only V2)
		client.startClientDialog();
		sccpProvider1.send(message, dummyCallback);

		client.awaitReceived(EventType.PAbort);
		{
			TestEvent<EventType> event = client.getNextEvent(EventType.PAbort);
			TCPAbortIndication ind = (TCPAbortIndication) event.getEvent();

			assertEquals(PAbortCauseType.UnrecognizedMessageType, ind.getPAbortCause());
		}

		client.awaitReceived(EventType.DialogRelease);

		TestEventFactory<EventType> clientExpected = TestEventFactory.create();
		clientExpected.addReceived(EventType.PAbort);
		clientExpected.addReceived(EventType.DialogRelease);

		TestEventFactory<EventType> serverExpected = TestEventFactory.create();

		TestEventUtils.assertEvents(clientExpected.getEvents(), client.getEvents());
		TestEventUtils.assertEvents(serverExpected.getEvents(), server.getEvents());
	}

	private static ByteBuf getMessageWithUnsupportedProtocolVersion() {
		return Unpooled.wrappedBuffer(new byte[] { 98, 117, 72, 1, 1, 107, 69, 40, 67, 6, 7, 0, 17, (byte) 134, 5, 1, 1,
				1, (byte) 160, 56, 96, 54, (byte) 128, 2, 6, 64, (byte) 161, 9, 6, 7, 4, 0, 0, 1, 0, 19, 2, (byte) 190,
				37, 40, 35, 6, 7, 4, 0, 0, 1, 1, 1, 1, (byte) 160, 24, (byte) 160, (byte) 128, (byte) 128, 9,
				(byte) 150, 2, 36, (byte) 128, 3, 0, (byte) 128, 0, (byte) 242, (byte) 129, 7, (byte) 145, 19, 38,
				(byte) 152, (byte) 134, 3, (byte) 240, 0, 0, 108, 41, (byte) 161, 39, 2, 1, (byte) 128, 2, 2, 2, 79, 36,
				30, 4, 1, 15, 4, 16, (byte) 170, (byte) 152, (byte) 172, (byte) 166, 90, (byte) 205, 98, 54, 25, 14, 55,
				(byte) 203, (byte) 229, 114, (byte) 185, 17, (byte) 128, 7, (byte) 145, 19, 38, (byte) 136, (byte) 131,
				0, (byte) 242 });
	}

	private static ByteBuf getMessageBadSyntax() {
		return Unpooled.wrappedBuffer(new byte[] { 98, 6, 72, 1, 1, 1, 2, 3 });
	}

	private static ByteBuf getMessageBadTag() {
		return Unpooled.wrappedBuffer(new byte[] { 106, 6, 72, 1, 1, 73, 1, 1 });
	}

	private static ByteBuf getUnrecognizedMessageTypeMessage() {
		return Unpooled.wrappedBuffer(new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 });
	}
}
