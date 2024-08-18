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
import static org.junit.Assert.assertNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ProtocolClassImpl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SccpConnCcMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnCcMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @Before
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @After
    public void tearDown() {
    }

    public ByteBuf getDataCcNoOptParams() {
        return Unpooled.wrappedBuffer(new byte[] { 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x01, 0x03, 0x00  });
    }

    public ByteBuf getDataCcOneOptParam() {
        return Unpooled.wrappedBuffer(new byte[] { 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x01, 0x03, 0x01, 0x03, 0x02, 0x42, 0x08, 0x00 });

    }

    public ByteBuf getDataCcAllParams() {
        return Unpooled.wrappedBuffer(new byte[] {
                0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x01, 0x03, 0x01, 0x03, 0x02, 0x42, 0x08, 0x09, 0x01, 0x64, 0x0F,
                0x05, 0x01, 0x02, 0x03, 0x04, 0x05, 0x12, 0x01, 0x07, 0x00
        });
    }

    @Test
    public void testDecode() throws Exception {
        // ---- no optional params
        ByteBuf buf = this.getDataCcNoOptParams();
        int type = buf.readByte();
        SccpConnCcMessageImpl testObjectDecoded = (SccpConnCcMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);

        // ---- one optional param
        buf = this.getDataCcOneOptParam();
        type = buf.readByte();
        testObjectDecoded = (SccpConnCcMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);
        assertNotNull(testObjectDecoded.getCalledPartyAddress());
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSignalingPointCode(), 0);
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCalledPartyAddress().getGlobalTitle());

        // ---- all param
        buf = this.getDataCcAllParams();
        type = buf.readByte();
        testObjectDecoded = (SccpConnCcMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getDestinationLocalReferenceNumber().getValue(), 2);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);
        assertEquals(testObjectDecoded.getCredit().getValue(), 100);
        assertNotNull(testObjectDecoded.getCalledPartyAddress());
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSignalingPointCode(), 0);
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCalledPartyAddress().getGlobalTitle());
        MessageSegmentationTest.assertByteBufs(testObjectDecoded.getUserData(), Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5}));
        assertEquals(testObjectDecoded.getImportance().getValue(), 7);
    }

    @Test
    public void testEncode() throws Exception {
        // ---- no optional params
        SccpConnCcMessageImpl original = new SccpConnCcMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setProtocolClass(new ProtocolClassImpl(3));

        EncodingResultData encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataCcNoOptParams());

        // ---- one optional param
        original = new SccpConnCcMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setProtocolClass(new ProtocolClassImpl(3));
        original.setCalledPartyAddress(stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,0,  8));

        encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataCcOneOptParam());

        // ---- all param
        original = new SccpConnCcMessageImpl(0, 0);
        original.setDestinationLocalReferenceNumber(new LocalReferenceImpl(2));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setProtocolClass(new ProtocolClassImpl(3));
        original.setCredit(new CreditImpl(100));
        original.setCalledPartyAddress(stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8));
        original.setUserData(Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4, 5}));
        original.setImportance(new ImportanceImpl((byte)15));

        encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataCcAllParams());
    }
}
