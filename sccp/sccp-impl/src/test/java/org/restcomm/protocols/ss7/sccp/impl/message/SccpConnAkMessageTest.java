/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReceiveSequenceNumberImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.SequenceNumberImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnAkMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnAkMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
    }

    public ByteBuf getDataAk() {
        return Unpooled.wrappedBuffer(new byte[] { 0x08, 0x00, 0x00, 0x01, 0x00, 0x64 });
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {
        ByteBuf buf = this.getDataAk();
        int type = buf.readByte();
        SccpConnAkMessageImpl testObjectDecoded = (SccpConnAkMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getReceiveSequenceNumber().getNumber().getValue(), 0);
        assertEquals(testObjectDecoded.getCredit().getValue(), 100);
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {
        SccpConnAkMessageImpl original = new SccpConnAkMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setReceiveSequenceNumber(new ReceiveSequenceNumberImpl(new SequenceNumberImpl((byte)0)));
        original.setCredit(new CreditImpl(100));


        EncodingResultData encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataAk());
    }
}
