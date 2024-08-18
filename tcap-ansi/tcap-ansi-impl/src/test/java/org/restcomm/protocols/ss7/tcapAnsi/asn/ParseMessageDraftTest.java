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

package org.restcomm.protocols.ss7.tcapAnsi.asn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.tcapAnsi.TCAPStackImpl;
import org.restcomm.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.restcomm.protocols.ss7.tcapAnsi.api.tc.dialog.events.DraftParsedMessage;
import org.junit.Test;
/**
 * 
 * @author yulianoifa
 *
 */
public class ParseMessageDraftTest {

    private byte[] dataTcQuery = new byte[] { (byte) 0xe2, 0x35, (byte) 0xc7, 0x04, 0x00, 0x00, 0x01, 0x00, (byte) 0xe8, 0x2d, (byte) 0xe9, 0x2b, (byte) 0xcf, 0x01,
            0x00, (byte) 0xd1, 0x02, 0x09, 0x35, (byte) 0xf2, 0x22, 4, 32, (byte) 0x9f, 0x69, 0x00, (byte) 0x9f, 0x74, 0x00, (byte) 0x9f, (byte) 0x81, 0x00, 0x01,
            0x08, (byte) 0x88, 0x05, 0x16, 0x19, 0x32, 0x04, 0x00, (byte) 0x9f, (byte) 0x81, 0x41, 0x01, 0x01, (byte) 0x9f, (byte) 0x81, 0x43, 0x05, 0x22,
            0x22, 0x22, 0x22, 0x22 };    

    private byte[] dataTcConversation = new byte[] { -27, 30, -57, 8, 1, 1, 2, 2, 3, 3, 4, 4, -24, 18, -23, 16, -49, 1, 0, -47,
            2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] dataTcResponse = { (byte) 0xe4, 0x3e, (byte) 0xc7, 0x04, 0x14, 0x00, 0x00, 0x00, (byte) 0xe8, 0x36, (byte) 0xea, 0x34, (byte) 0xcf, 0x01,
            0x01, (byte) 0xf2, 0x2f, 0x04, 0x2d, (byte) 0x96, 0x01, 0x13, (byte) 0x8e, 0x02, 0x06, 0x00, (byte) 0x95, 0x03, 0x00, 0x0c, 0x10, (byte) 0x9f, 0x4e, 0x01,
            0x01, (byte) 0x99, 0x03, 0x7a, 0x0d, 0x11, (byte) 0x9f, 0x5d, 0x07, 0x00, 0x00, 0x21, 0x06, 0x36, 0x54, 0x10, (byte) 0x97, 0x01, 0x07, (byte) 0x9f,
            0x73, 0x01, 0x00, (byte) 0x9f, 0x75, 0x01, 0x00, (byte) 0x98, 0x01, 0x02 };

    private byte[] dataTcAbort = new byte[] { -10, 28, -57, 4, 20, 0, 0, 0, -8, 20, 40, 18, 6, 7, 4, 0, 0, 1, 1, 1, 1, -96, 7, 1, 2, 3, 4, 5, 6, 7 };

    private byte[] dataTcUnidirectional = new byte[] { -31, 22, -57, 0, -24, 18, -23, 16, -49, 1, 0, -47, 2, 9, 53, -14, 7, 1, 2, 3, 4, 5, 6, 7 };

    @Test
    public void testTCQuery() throws IOException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, 4);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcQuery));

        assertTrue(msg.getMessage() instanceof TCQueryMessageImplWithPerm);
        assertEquals((long) Utils.decodeTransactionId(msg.getMessage().getOriginatingTransactionId(),true), 256);
        assertNull(msg.getMessage().getDestinationTransactionId());
        assertNull(msg.getParsingErrorReason());
    }

    @Test
    public void testTCConversation() throws IOException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, 4);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcConversation));

        assertTrue(msg.getMessage() instanceof TCConversationMessageImplWithPerm);
        assertEquals((long) Utils.decodeTransactionId(msg.getMessage().getOriginatingTransactionId(),true), 16843266);
        assertEquals((long) Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(),true), 50529284);
        assertNull(msg.getParsingErrorReason());
    }

    @Test
    public void testTCResponse() throws IOException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, 4);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcResponse));

        assertTrue(msg.getMessage() instanceof  TCResponseMessageImpl);
        assertNull(msg.getMessage().getOriginatingTransactionId());
        assertEquals((long) Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(),true), 335544320);
        assertNull(msg.getParsingErrorReason());
    }

    @Test
    public void testTCAbort() throws IOException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, 4);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcAbort));

        assertTrue(msg.getMessage() instanceof TCAbortMessageImpl);
        assertNull(msg.getMessage().getOriginatingTransactionId());
        assertEquals((long) Utils.decodeTransactionId(msg.getMessage().getDestinationTransactionId(),true), 335544320);
        assertNull(msg.getParsingErrorReason());
    }

    @Test
    public void testTCUnidirectional() throws IOException, ParseException {

        SccpStackImpl sccpStack = new SccpStackImpl("ParseMessageDraftTest");
        TCAPStackImpl stack = new TCAPStackImpl("TCAPAbnormalTest", sccpStack.getSccpProvider(), 8, 4);
        TCAPProvider provider = stack.getProvider();
        DraftParsedMessage msg = provider.parseMessageDraft(Unpooled.wrappedBuffer(dataTcUnidirectional));

        assertTrue(msg.getMessage() instanceof TCUniMessageImpl);
        assertNull(msg.getMessage().getOriginatingTransactionId());
        assertNull(msg.getMessage().getDestinationTransactionId());
        assertNull(msg.getParsingErrorReason());
    }
}