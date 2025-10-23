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

package org.restcomm.protocols.ss7.tcap.asn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.tcap.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.DraftParsedMessage;
import org.restcomm.protocols.ss7.tcap.asn.comp.ASNReturnResultParameterImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCAbortMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCBeginMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCContinueMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCEndMessageImpl;
import org.restcomm.protocols.ss7.tcap.asn.tx.TCUniMessageImpl;

import com.mobius.software.common.dal.timers.WorkerPool;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.Unpooled;

/**
 * 
 * @author yulianoifa
 *
 */
public class ParseMessageDraftTest {

	private byte[] dataTcBegin = new byte[] {
			// TCBegin
			0x62, 38,
			// oidTx
			// OrigTran ID (full)............ 145031169
			0x48, 4, 8, (byte) 0xA5, 0, 1,
			// dialog portion
			0x6B, 30,
			// extrnal tag
			0x28, 28,
			// oid
			0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) // asn
			160, 17,
			// DialogPDU - Request
			0x60, 15, (byte) // protocol version
			0x80, 2, 7, (byte) 0x80, (byte) // acn
			161, 9,
			// oid
			6, 7, 4, 0, 1, 1, 1, 3, 0

	};

	private byte[] dataTcContinue = new byte[] { 0x65, 0x16,
			// org txid
			0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
			// dtx
			0x49, 0x04, 8, (byte) 0xA4, 0, 1,
			// comp portion
			0x6C, 8,
			// invoke
			(byte) 0xA1, 6,
			// invoke ID
			0x02, 0x01, 0x01,
			// op code
			0x02, 0x01, 0x37 };

	private byte[] dataTcEnd = new byte[] {
			// TCEnd
			0x64, 65,
			// dialog portion
			// empty
			// dtx
			// DestTran ID (full)............ 144965633
			0x49, 4, 8, (byte) 0xA4, 0, 1,
			// comp portion
			0x6C, 57,
			// invoke
			(byte) 0xA1, 6,
			// invoke ID
			0x02, 0x01, 0x01,
			// op code
			0x02, 0x01, 0x37,
			// return result last
			(byte) 0xA2, 47,
			// inoke id
			0x02, 0x01, 0x02,
			// sequence start
			0x30, 42,
			// local operation
			0x02, 0x02, 0x00, (byte) 0xFF,
			// parameter
			0x30, 36, (byte) 0xA0, // some tag.1
			17, (byte) 0x80, // some tag.1.1
			2, 0x11, 0x11, (byte) 0xA1, // some tag.1.2
			04, (byte) 0x82, // some tag.1.3 ?
			2, 0x00, 0x00, (byte) 0x82,
			// some tag.1.4
			1, 12, (byte) 0x83, // some tag.1.5
			2, 0x33, 0x33, (byte) 0xA1, // some trash here
			// tension indicator 2........ ???
			// use value.................. ???
			// some tag.2
			3, (byte) 0x80, // some tag.2.1
			1, -1, (byte) 0xA2, // some tag.3
			3, (byte) 0x80, // some tag.3.1
			1, -1, (byte) 0xA3, // some tag.4
			5, (byte) 0x82, // some tag.4.1
			3, (byte) 0xAB, // - 85 serviceKey................... 123456 // dont care about this content,
							// lets just make len
							// correct
			(byte) 0xCD, (byte) 0xEF };

	private byte[] dataTcAbort = new byte[] { 103, 9, 73, 4, 123, -91, 52, 19, 74, 1, 126 };

	private byte[] dataTcUnidirectional = new byte[] { 97, 45, 107, 27, 40, 25, 6, 7, 0, 17, -122, 5, 1, 2, 1, -96, 14,
			96, 12, -128, 2, 7, -128, -95, 6, 6, 4, 40, 2, 3, 4, 108, 14, -95, 12, 2, 1, -128, 2, 2, 2, 79, 4, 3, 1, 2,
			3 };

	@Test
	public void testTCBegin() throws IOException, EncodeException, ParseException {
		WorkerPool workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest", workerPool);
		TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, workerPool);
		TCAPProvider provider = stack.getProvider();

		ASNParser parser = provider.getParser();
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);

		DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcBegin));

		assertTrue(msg.getMessage() instanceof TCBeginMessageImpl);
		assertEquals(
				Utils.decodeTransactionId(msg.getMessage().getOriginatingTransactionId(), stack.getSwapTcapIdBytes()),
				145031169);
		assertNull(msg.getMessage().getDestinationTransactionId());
		assertNull(msg.getParsingErrorReason());

		workerPool.stop();
	}

	@Test
	public void testTCContinue() throws IOException, EncodeException, ParseException {
		WorkerPool workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest", workerPool);
		TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8,
				workerPool);
		TCAPProvider provider = stack.getProvider();

		ASNParser parser = provider.getParser();
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);

		DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcContinue));

		assertTrue(msg.getMessage() instanceof TCContinueMessageImpl);
		assertEquals(
				Utils.decodeTransactionId(msg.getMessage().getOriginatingTransactionId(), stack.getSwapTcapIdBytes()),
				145031169);
		assertEquals(
				Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(), stack.getSwapTcapIdBytes()),
				144965633);
		assertNull(msg.getParsingErrorReason());

		workerPool.stop();
	}

	@Test
	public void testTCEnd() throws IOException, EncodeException, ParseException {
		WorkerPool workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest", workerPool);
		TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8,
				workerPool);
		TCAPProvider provider = stack.getProvider();

		ASNParser parser = provider.getParser();
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);

		DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcEnd));

		assertTrue(msg.getMessage() instanceof TCEndMessageImpl);
		assertNull(msg.getMessage().getOriginatingTransactionId());
		assertEquals(
				Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(), stack.getSwapTcapIdBytes()),
				144965633);
		assertNull(msg.getParsingErrorReason());

		workerPool.stop();
	}

	@Test
	public void testTCAbort() throws IOException, EncodeException, ParseException {
		WorkerPool workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest", workerPool);
		TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8,
				workerPool);
		TCAPProvider provider = stack.getProvider();

		ASNParser parser = provider.getParser();
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);

		DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcAbort));

		assertTrue(msg.getMessage() instanceof TCAbortMessageImpl);
		assertNull(msg.getMessage().getOriginatingTransactionId());
		assertEquals(
				Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(), stack.getSwapTcapIdBytes()),
				2074424339);
		assertNull(msg.getParsingErrorReason());

		workerPool.stop();
	}

	@Test
	public void testTCUnidirectional() throws IOException, EncodeException, ParseException {
		WorkerPool workerPool = new WorkerPool("TCAP");
		workerPool.start(4);

		SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest", workerPool);
		TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8,
				workerPool);
		TCAPProvider provider = stack.getProvider();

		ASNParser parser = provider.getParser();
		parser.clearClassMapping(ASNReturnResultParameterImpl.class);
		parser.registerAlternativeClassMapping(ASNReturnResultParameterImpl.class, TCEndTestASN.class);

		DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcUnidirectional));

		assertTrue(msg.getMessage() instanceof TCUniMessageImpl);
		assertNull(msg.getMessage().getOriginatingTransactionId());
		assertNull(msg.getMessage().getDestinationTransactionId());
		assertNull(msg.getParsingErrorReason());

		workerPool.stop();
	}

}
