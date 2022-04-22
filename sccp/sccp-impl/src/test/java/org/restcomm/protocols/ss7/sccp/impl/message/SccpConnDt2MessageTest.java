/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.sccp.impl.message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class SccpConnDt2MessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnDt2MessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
    }

    public ByteBuf getDataDt2() {
        return Unpooled.wrappedBuffer(new byte[] { 0x07, 0x00, 0x00, 0x01, 0x00, 0x00, 0x01, 0x05, 0x01, 0x02, 0x03, 0x04, 0x05 });
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {
    	ByteBuf buf = this.getDataDt2();
        int type = buf.readByte();
        SccpConnDt2MessageImpl testObjectDecoded = (SccpConnDt2MessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 1);
        assertNotNull(testObjectDecoded.getSequencingSegmenting());
        MessageSegmentationTest.assertByteBufs(testObjectDecoded.getUserData(), Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4, 5}));
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {
        SccpConnDt2MessageImpl original = new SccpConnDt2MessageImpl(stack.getMaxDataMessage(), 0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setSequencingSegmenting(new SequencingSegmentingImpl());
        original.setUserData(Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4, 5}));

        EncodingResultData encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataDt2());
    }
}
