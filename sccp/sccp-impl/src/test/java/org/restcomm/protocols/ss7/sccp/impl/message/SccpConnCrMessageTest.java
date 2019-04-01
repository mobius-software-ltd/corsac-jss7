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

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.EncodingResultData;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.restcomm.protocols.ss7.sccp.impl.message.SccpConnCrMessageImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.CreditImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.LocalReferenceImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class SccpConnCrMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpConnCrMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
    }

    public ByteBuf getDataCrNoOptParams() {
        return Unpooled.wrappedBuffer(new byte[] { 0x01, 0x00, 0x00, 0x01, 0x03, 0x02, 0x00, 0x02, 0x42, 0x08 });
    }

    public ByteBuf getDataCrOneOptParam() {
        return Unpooled.wrappedBuffer(new byte[] { 0x01, 0x00, 0x00, 0x01, 0x03, 0x02, 0x04, 0x02, 0x42, 0x08, 0x12, 0x01, 0x05, 0x00  });
    }

    public ByteBuf getDataCrAllParams() {
        return Unpooled.wrappedBuffer(new byte[] {
                0x01, 0x00, 0x00, 0x01, 0x03, 0x02, 0x04, 0x02, 0x42, 0x08, 0x09, 0x01, 0x0A, 0x04, 0x04, 0x43, 0x01,
                0x00, 0x08, 0x0F, 0x04, 0x01, 0x02, 0x03, 0x04, 0x11, 0x01, 0x0F, 0x12, 0x01, 0x05, 0x00
        });
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {
        // ---- no optional params
        ByteBuf buf = this.getDataCrNoOptParams();
        int type = buf.readByte();
        SccpConnCrMessageImpl testObjectDecoded = (SccpConnCrMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);
        assertNotNull(testObjectDecoded.getCalledPartyAddress());
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSignalingPointCode(), 0);
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCalledPartyAddress().getGlobalTitle());

        // ---- one optional param
        buf = this.getDataCrOneOptParam();
        type = buf.readByte();
        testObjectDecoded = (SccpConnCrMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);
        assertNotNull(testObjectDecoded.getCalledPartyAddress());
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSignalingPointCode(), 0);
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCalledPartyAddress().getGlobalTitle());
        assertEquals(testObjectDecoded.getImportance().getValue(), 5);

        // ---- all param
        buf = this.getDataCrAllParams();
        type = buf.readByte();
        testObjectDecoded = (SccpConnCrMessageImpl) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);

        assertNotNull(testObjectDecoded);
        assertEquals(testObjectDecoded.getSourceLocalReferenceNumber().getValue(), 1);
        assertEquals(testObjectDecoded.getProtocolClass().getProtocolClass(), 3);
        assertNotNull(testObjectDecoded.getCalledPartyAddress());
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSignalingPointCode(), 0);
        assertEquals(testObjectDecoded.getCalledPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCalledPartyAddress().getGlobalTitle());
        assertEquals(testObjectDecoded.getCredit().getValue(), 10);

        assertNotNull(testObjectDecoded.getCallingPartyAddress());
        assertEquals(testObjectDecoded.getCallingPartyAddress().getSignalingPointCode(), 1);
        assertEquals(testObjectDecoded.getCallingPartyAddress().getSubsystemNumber(), 8);
        assertNull(testObjectDecoded.getCallingPartyAddress().getGlobalTitle());

        MessageSegmentationTest.assertByteBufs(testObjectDecoded.getUserData(), Unpooled.wrappedBuffer(new byte[]{1, 2, 3, 4}));
        assertEquals(testObjectDecoded.getHopCounter().getValue(), 15);
        assertEquals(testObjectDecoded.getImportance().getValue(), 5);
    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {
        // ---- no optional params
        SccpAddress calledAddress = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);

        SccpConnCrMessageImpl original = (SccpConnCrMessageImpl) messageFactory.createConnectMessageClass3(8, calledAddress, null,null, null, null);
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));

        EncodingResultData encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataCrNoOptParams());

        // ---- one optional param
        original = (SccpConnCrMessageImpl) messageFactory.createConnectMessageClass3(8, calledAddress, null,null, null, new ImportanceImpl((byte)5));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));

        encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        assertEquals(encoded.getSolidData(), this.getDataCrOneOptParam());

        // ---- all param
        SccpAddress callingAddress = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 8);

        original = (SccpConnCrMessageImpl) messageFactory.createConnectMessageClass3(8, calledAddress, callingAddress, new CreditImpl(10),
                Unpooled.wrappedBuffer(new byte[] {1, 2, 3, 4}), new ImportanceImpl((byte)5));
        original.setSourceLocalReferenceNumber(new LocalReferenceImpl(1));
        original.setHopCounter(new HopCounterImpl(15));

        encoded = original.encode(stack, LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);

        MessageSegmentationTest.assertByteBufs(encoded.getSolidData(), this.getDataCrAllParams());
    }
}

