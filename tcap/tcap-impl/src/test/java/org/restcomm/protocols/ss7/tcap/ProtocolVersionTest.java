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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.RemoteSccpStatus;
import org.restcomm.protocols.ss7.sccp.SccpConnection;
import org.restcomm.protocols.ss7.sccp.SccpListener;
import org.restcomm.protocols.ss7.sccp.SignallingPointStatus;
import org.restcomm.protocols.ss7.sccp.impl.SccpHarness;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.ErrorCause;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ProtocolClass;
import org.restcomm.protocols.ss7.sccp.parameter.RefusalCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCause;
import org.restcomm.protocols.ss7.sccp.parameter.ResetCause;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.asn.ASNComponentPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.ASNDialogPortionObjectImpl;
import org.restcomm.protocols.ss7.tcap.asn.DialogRequestAPDU;
import org.restcomm.protocols.ss7.tcap.asn.comp.InvokeImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.RejectImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnErrorImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.ReturnResultLastImpl;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.TCUnifiedMessage;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogAbortAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogRequestAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.DialogResponseAPDUImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.listeners.Client;
import org.restcomm.protocols.ss7.tcap.listeners.events.EventType;

import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Test protocol version.
 *
 * @author Nosach Konstantin
 * @author yulianoifa
 *
 */
public class ProtocolVersionTest extends SccpHarness {
	private static final long INVOKE_TIMEOUT = 0;
	private static final long SCCP_MESSAGE_RETRIEVAL_TIMEOUT = 5000;

	private TCAPStackImpl tcapStack1;
	private TCAPStackImpl tcapStack2;
	private SccpAddress peer1Address;
	private SccpAddress peer2Address;
	private Client client;

	private TestSccpListener sccpListener;

	@Before
	public void beforeEach() throws Exception {
		this.sccpStack1Name = "TCAPFunctionalTestSccpStack1";
		this.sccpStack2Name = "TCAPFunctionalTestSccpStack2";

		super.setUp();

		peer1Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);
		peer2Address = parameterFactory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 2, 8);

		sccpListener = new TestSccpListener();
		super.sccpProvider2.registerSccpListener(8, sccpListener);

		tcapStack1 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider1, 8, workerPool);
		tcapStack2 = new TCAPStackImpl("TCAPFunctionalTest", this.sccpProvider2, 7, workerPool);

		tcapStack1.start();
		tcapStack2.start();

		tcapStack1.setInvokeTimeout(INVOKE_TIMEOUT);
		tcapStack2.setInvokeTimeout(INVOKE_TIMEOUT);

		client = new Client(tcapStack1, super.parameterFactory, peer1Address, peer2Address);
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
	public void doNotSendProtocolVersionDialogTest() throws Exception {
		client.startClientDialog();
		client.dialog.setDoNotSendProtocolVersion(true);

		client.sendBegin();
		client.awaitSent(EventType.Begin);
		{
			TCBeginMessage tcb = (TCBeginMessage) sccpListener.messages.poll(SCCP_MESSAGE_RETRIEVAL_TIMEOUT,
					TimeUnit.MILLISECONDS);
			assertNotNull(tcb);
			DialogRequestAPDU apdu = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();

			assertNull(apdu.getProtocolVersion());
		}
	}

	@Test
	public void sendProtocolVersionDialogTest() throws Exception {
		client.startClientDialog();
		client.dialog.setDoNotSendProtocolVersion(false);

		client.sendBegin();

		client.awaitSent(EventType.Begin);
		{
			TCBeginMessage tcb = (TCBeginMessage) sccpListener.messages.poll(SCCP_MESSAGE_RETRIEVAL_TIMEOUT,
					TimeUnit.MILLISECONDS);
			assertNotNull(tcb);
			DialogRequestAPDU apdu = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();

			assertNotNull(apdu.getProtocolVersion());
		}
	}

	@Test
	public void doNotSendProtocolVersionStackTest() throws Exception {
		tcapStack1.setDoNotSendProtocolVersion(true);

		client.startClientDialog();

		client.sendBegin();
		client.awaitSent(EventType.Begin);
		{
			TCBeginMessage tcb = (TCBeginMessage) sccpListener.messages.poll(SCCP_MESSAGE_RETRIEVAL_TIMEOUT,
					TimeUnit.MILLISECONDS);
			assertNotNull(tcb);
			DialogRequestAPDU apdu = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();

			assertNull(apdu.getProtocolVersion());
		}
	}

	@Test
	public void sendProtocolVersionStackTest() throws Exception {
		tcapStack1.setDoNotSendProtocolVersion(false);

		client.startClientDialog();

		client.sendBegin();
		client.awaitSent(EventType.Begin);
		{
			TCBeginMessage tcb = (TCBeginMessage) sccpListener.messages.poll(SCCP_MESSAGE_RETRIEVAL_TIMEOUT,
					TimeUnit.MILLISECONDS);
			assertNotNull(tcb);
			DialogRequestAPDU apdu = (DialogRequestAPDU) tcb.getDialogPortion().getDialogAPDU();

			assertNotNull(apdu.getProtocolVersion());
		}
	}

	private class TestSccpListener implements SccpListener {
		public BlockingQueue<TCUnifiedMessage> messages = new LinkedBlockingQueue<>();

		private static final long serialVersionUID = 1L;
		private ASNParser parser = new ASNParser(true);

		private TestSccpListener() {
			parser.loadClass(TCBeginMessageImpl.class);

			parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogRequestAPDUImpl.class);
			parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogResponseAPDUImpl.class);
			parser.registerAlternativeClassMapping(ASNDialogPortionObjectImpl.class, DialogAbortAPDUImpl.class);

			parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, InvokeImpl.class);
			parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultImpl.class);
			parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnResultLastImpl.class);
			parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, RejectImpl.class);
			parser.registerAlternativeClassMapping(ASNComponentPortionObjectImpl.class, ReturnErrorImpl.class);
		}

		@Override
		public void onMessage(SccpDataMessage message) {
			ByteBuf buffer = Unpooled.wrappedBuffer(message.getData());
			Object output = null;
			try {
				output = parser.decode(buffer).getResult();
			} catch (ASNException ex) {
			}

			if (output != null && output instanceof TCUnifiedMessage)
				messages.add((TCUnifiedMessage) output);
		}

		@Override
		public void onNotice(SccpNoticeMessage message) {
		}

		@Override
		public void onCoordResponse(int ssn, int multiplicityIndicator) {
		}

		@Override
		public void onState(int dpc, int ssn, boolean inService, int multiplicityIndicator) {
		}

		@Override
		public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel,
				RemoteSccpStatus remoteSccpStatus) {
		}

		@Override
		public void onConnectIndication(SccpConnection conn, SccpAddress calledAddress, SccpAddress callingAddress,
				ProtocolClass clazz, Credit credit, ByteBuf data, Importance importance) throws Exception {

		}

		@Override
		public void onConnectConfirm(SccpConnection conn, ByteBuf data) {
		}

		@Override
		public void onDisconnectIndication(SccpConnection conn, ReleaseCause reason, ByteBuf data) {
		}

		@Override
		public void onDisconnectIndication(SccpConnection conn, RefusalCause reason, ByteBuf data) {
		}

		@Override
		public void onDisconnectIndication(SccpConnection conn, ErrorCause errorCause) {
		}

		@Override
		public void onResetIndication(SccpConnection conn, ResetCause reason) {
		}

		@Override
		public void onResetConfirm(SccpConnection conn) {
		}

		@Override
		public void onData(SccpConnection conn, ByteBuf data) {
		}

		@Override
		public void onDisconnectConfirm(SccpConnection conn) {
		}
	}
}
