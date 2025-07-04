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

import com.mobius.software.common.dal.timers.TaskCallback;

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

	public static final long WAIT_TIME = 500;
	public static final long INVOKE_WAIT_TIME = 500;
	private static final int _DIALOG_TIMEOUT = 5000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;
	private Server server;

	private TaskCallback<Exception> dummyCallback = new TaskCallback<Exception>() {
		@Override
		public void onSuccess() {
		}

		@Override
		public void onError(Exception exception) {
		}
	};

	@Override
	@Before
	public void setUp() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		System.out.println("setUp");
		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		this.tcapStack1 = new TCAPStackImpl("TCAPAbnormalTest", this.sccpProvider1, 8, workerPool);
		this.tcapStack2 = new TCAPStackImpl("TCAPAbnormalTest", this.sccpProvider2, 8, workerPool);

		this.tcapStack1.start();
		this.tcapStack2.start();

		this.tcapStack1.setInvokeTimeout(0);
		this.tcapStack2.setInvokeTimeout(0);
		this.tcapStack1.setDialogIdleTimeout(_DIALOG_TIMEOUT);
		this.tcapStack2.setDialogIdleTimeout(_DIALOG_TIMEOUT + 200);
		// create test classes
		this.client = new Client(this.tcapStack1, super.parameterFactory, peer1Address, peer2Address);
		this.server = new Server(this.tcapStack2, super.parameterFactory, peer2Address, peer1Address);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	@After
	public void tearDown() {
		this.tcapStack1.stop();
		this.tcapStack2.stop();
		super.tearDown();

	}

	/**
	 * A case of receiving TC-Begin + AARQ apdu + unsupported protocol version
	 * (supported only V2) TC-BEGIN + AARQ apdu (unsupported protocol version)
	 * TC-ABORT + PAbortCauseType.NoCommonDialogPortion
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
		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.NoCommonDialoguePortion);
	}

	/**
	 * Case of receiving TC-Begin that has a bad structure TC-BEGIN (bad formatted)
	 * TC-ABORT + PAbortCauseType.IncorrectTxPortion
	 */
	@Test
	public void badSyntaxMessageTest() throws Exception {

		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, getMessageBadSyntax(), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);
		EventTestHarness.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.IncorrectTxPortion);
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

		server.sendContinue();
		Thread.sleep(WAIT_TIME);

		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer1Address,
				peer2Address, getMessageBadTag(), 0, 0, false, null, null);
		this.sccpProvider2.send(message, dummyCallback);
		Thread.sleep(WAIT_TIME + _DIALOG_TIMEOUT);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
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
		te = TestEvent.createReceivedEvent(EventType.PAbort, null, 3, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 4, stamp + WAIT_TIME * 2);
		serverExpectedEvents.add(te);

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		server.sendContinue();
		Thread.sleep(WAIT_TIME);

		client.releaseDialog();
		server.sendContinue();
		Thread.sleep(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(server.pAbortCauseType, PAbortCauseType.UnrecognizedTxID);
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

		long stamp = System.currentTimeMillis();
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

		client.startClientDialog();
		client.sendBegin();
		Thread.sleep(WAIT_TIME);

		server.sendContinue();
		Thread.sleep(WAIT_TIME);

		client.getCurDialog().setState(TRPseudoState.InitialSent);
		server.sendContinue();
		Thread.sleep(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
		assertEquals(server.pAbortCauseType, PAbortCauseType.AbnormalDialogue);
	}

	/**
	 * TC-U-Abort as a response to TC-Begin
	 *
	 * TC-BEGIN TC-ABORT + UserAbort by TCAP user
	 */
	@Test
	public void userAbortTest() throws Exception {

		//
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

		UserInformation userInformation = TcapFactory.createUserInformation();
		userInformation.setIdentifier(Arrays.asList(new Long[] { 1L, 2L, 3L }));
		userInformation.setChild(Unpooled.wrappedBuffer(new byte[] { 11, 22, 33 }));
		server.sendAbort(null, userInformation, null);
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
		tcapDialog.sendData(null, null, null, INVOKE_WAIT_TIME, null, null, true, false);

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
		tcapDialog.sendData(null, null, null, _DIALOG_TIMEOUT * 2L, null, null, true, false);

		client.sendBeginUnreachableAddress(false, dummyCallback);
		Thread.sleep(WAIT_TIME);
		Thread.sleep(_DIALOG_TIMEOUT * 2);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);
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

	@Test
	public void unrecognizedMessageTypeTest() throws Exception {

		// case of receiving TC-Begin + AARQ apdu + unsupported protocol version
		// (supported only V2)
		long stamp = System.currentTimeMillis();
		List<TestEvent> clientExpectedEvents = new ArrayList<TestEvent>();
		TestEvent te = TestEvent.createReceivedEvent(EventType.PAbort, null, 0, stamp);
		clientExpectedEvents.add(te);
		te = TestEvent.createReceivedEvent(EventType.DialogRelease, null, 1, stamp);
		clientExpectedEvents.add(te);

		List<TestEvent> serverExpectedEvents = new ArrayList<TestEvent>();

		client.startClientDialog();
		SccpDataMessage message = this.sccpProvider1.getMessageFactory().createDataMessageClass1(peer2Address,
				peer1Address, Unpooled.wrappedBuffer(getUnrecognizedMessageTypeMessage()), 0, 0, false, null, null);
		this.sccpProvider1.send(message, dummyCallback);
		Client.waitFor(WAIT_TIME);

		client.compareEvents(clientExpectedEvents);
		server.compareEvents(serverExpectedEvents);

		assertEquals(client.pAbortCauseType, PAbortCauseType.UnrecognizedMessageType);
	}

	public static byte[] getUnrecognizedMessageTypeMessage() {
		return new byte[] { 105, 6, 72, 4, 0, 0, 0, 1 };
	}
}
