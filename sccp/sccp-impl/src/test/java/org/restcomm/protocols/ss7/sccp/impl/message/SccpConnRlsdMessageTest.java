/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReleaseCauseImpl;
import org.restcomm.protocols.ss7.sccp.parameter.ReleaseCauseValue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnRlsdMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnRlsdMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @Before
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @After
    public void tearDown() {
    }

    public ByteBuf getDataRlsdNoOptParams() {
        return Unpooled.wrappedBuffer(new byte[] { 0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x00 });
    }

    public ByteBuf getDataRlsdOneOptParam() {
        return Unpooled.wrappedBuffer(new byte[] { 0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x01, 0x12, 0x01, 0x02, 0x00 });
    }

    public ByteBuf getDataRlsdAllParams() {
        return Unpooled.wrappedBuffer(new byte[] {
                0x04, 0x00, 0x00, 0x02, 0x00, 0x00, 0x03, 0x07, 0x01, 0x0F, 0x05, 0x01, 0x02, 0x03, 0x04, 0x05, 0x12,
                0x01, 0x02, 0x00
        });
    }

    @Test
    public void testDecode() throws Exception {
        // ---- no optional params
        ByteBuf buf = this.getDataRlsdNoOptParams();
        int type = buf.readByte();
        SccpConnRlsdMessageImpl testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);

        // ---- one optional param
        buf = this.getDataRlsdOneOptParam();
        type = buf.readByte();
        testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);
        assertEquals(testObjectDecoded.getImportance().getValue(), 2);

        // ---- all param
        buf = this.getDataRlsdAllParams();
        type = buf.readByte();
        testObjectDecoded = (SccpConnRlsdMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 3);
        assertEquals(testObjectDecoded.getReleaseCause().getValue(), ReleaseCauseValue.ACCESS_CONGESTION);
        MessageSegmentationTest.assertByteBufs(testObjectDecoded.getUserData(), Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4, 5}));
        assertEquals(testObjectDecoded.getImportance().getValue(), 2);
    }

    @Test
    public void testEncode() throws Exception {
        // ---- no optional params
        SccpConnRlsdMessageImpl original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));

        EncodingResultData encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataRlsdNoOptParams());

        // ---- one optional param
        original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));
        original.setImportance(new ImportanceImpl((byte)10));

        encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataRlsdOneOptParam());

        // ---- all param
        original = new SccpConnRlsdMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(3));
        original.setReleaseCause(new ReleaseCauseImpl(ReleaseCauseValue.ACCESS_CONGESTION));
        original.setUserData(Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4, 5}));
        original.setImportance(new ImportanceImpl((byte)10));

        encoded = original.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataRlsdAllParams());
    }
}

