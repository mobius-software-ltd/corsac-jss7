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
import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpDataMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpRemoveSpcTest {

    private Logger logger;
    private SccpStackImpl stack;
    private MessageFactoryImpl messageFactory;

    @Before
    public void setUp() {
        this.stack = new SccpStackImpl("SccpRemoveSpcTest");
        this.stack.start();
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @After
    public void tearDown() {
        this.stack.stop();
    }

    public ByteBuf getDataUdt_GT_WithDpc() {
        return Unpooled.wrappedBuffer(new byte[] { 9, 1, 3, 11, 19, 8, 11, -89, 5, 8, 0, 68, 68, 68, 8, 11, -90, 5, 8, 0, 85, 85, 85, 5, 22, 22, 22,
                22, 22 });
    }

    public ByteBuf getDataUdt_GT_WithOutDpc() {
        return Unpooled.wrappedBuffer(new byte[] { 9, 1, 3, 9, 15, 6, 10, 8, 0, 68, 68, 68, 6, 10, 8, 0, 85, 85, 85, 5, 22, 22, 22, 22, 22 });
    }

    public ByteBuf getDataUdt_DpcSsn() {
        return Unpooled.wrappedBuffer(new byte[] { 9, 1, 3, 7, 11, 4, 67, -89, 5, 8, 4, 67, -90, 5, 8, 5, 22, 22, 22, 22, 22 });
    }

    public ByteBuf getData() {
        return Unpooled.wrappedBuffer(new byte[] { 22, 22, 22, 22, 22 });
    }

    @Test
    public void testDecode() throws Exception {

        // ---- Encoding based on GT - removeSpc on
        this.stack.setRemoveSpc(true);
        ByteBuf buf = this.getDataUdt_GT_WithOutDpc();

        int type = buf.readByte();
        SccpDataMessage testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        assertNotNull(testObjectDecoded);

        SccpAddress calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        GlobalTitle gt = calledAdd.getGlobalTitle();
        assertTrue(gt.getDigits().equals("444444"));

        SccpAddress callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertEquals(callingAdd.getSignalingPointCode(), 0);
        gt = callingAdd.getGlobalTitle();
        assertTrue(gt.getDigits().equals("555555"));

        // ---- Encoding based on GT - removeSpc off
        this.stack.setRemoveSpc(false);
        buf = this.getDataUdt_GT_WithDpc();
        type = buf.readByte();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        assertNotNull(testObjectDecoded);

        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertEquals(calledAdd.getSignalingPointCode(), 1447);
        gt = calledAdd.getGlobalTitle();
        assertTrue(gt.getDigits().equals("444444"));

        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertEquals(callingAdd.getSignalingPointCode(), 1446);
        gt = callingAdd.getGlobalTitle();
        assertTrue(gt.getDigits().equals("555555"));

        // ---- Encoding based on DPC+SSN - removeSpc on
        this.stack.setRemoveSpc(true);
        buf = this.getDataUdt_DpcSsn();
        type = buf.readByte();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        assertNotNull(testObjectDecoded);

        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertEquals(calledAdd.getSignalingPointCode(), 1447);
        gt = calledAdd.getGlobalTitle();
        assertNull(gt);

        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertEquals(callingAdd.getSignalingPointCode(), 1446);
        gt = callingAdd.getGlobalTitle();
        assertNull(gt);

        // ---- Encoding based on DPC+SSN - removeSpc off
        this.stack.setRemoveSpc(false);
        buf = this.getDataUdt_DpcSsn();
        type = buf.readByte();
        testObjectDecoded = (SccpDataMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        assertNotNull(testObjectDecoded);

        calledAdd = testObjectDecoded.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertEquals(calledAdd.getSignalingPointCode(), 1447);
        gt = calledAdd.getGlobalTitle();
        assertNull(gt);

        callingAdd = testObjectDecoded.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSubsystemNumber(), 8);
        assertEquals(callingAdd.getSignalingPointCode(), 1446);
        gt = callingAdd.getGlobalTitle();
        assertNull(gt);

    }

    @Test
    public void testEncode() throws Exception {

        // ---- Encoding based on GT - removeSpc on
        // ---- removeSpc on
        this.stack.setRemoveSpc(true);
        GlobalTitle gt1 = stack.getSccpProvider().getParameterFactory().createGlobalTitle("444444",0);
        GlobalTitle gt2 = stack.getSccpProvider().getParameterFactory().createGlobalTitle("555555",0);
        SccpAddress calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 1447, 8);
        SccpAddress callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 1446, 8);
        SccpDataMessageImpl msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd,
                getData(), 0, 8, false, null, null);

        EncodingResultData res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);

        MessageSegmentationTest.assertByteBufs(res.getSolidData(), this.getDataUdt_GT_WithOutDpc());

        // ---- removeSpc off
        this.stack.setRemoveSpc(false);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);

//        logger.debug("--------------------------");
//        logger.debug(arrToString(res.getSolidData()));
//        logger.debug("--------------------------");
//        logger.debug(arrToString(this.getDataUdt_GT_WithDpc()));
//        logger.debug("--------------------------");
//        
//        String s1 = arrToString(res.getSolidData());
//        s1 = s1 + "\nXXX\n";
//        s1 = s1 + arrToString(this.getDataUdt_GT_WithDpc());
//        boolean b1 = Arrays.equals(res.getSolidData(), this.getDataUdt_GT_WithDpc());
//        s1 = s1 + "\n\nResult: " + b1;
//        fail(s1);
        
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), this.getDataUdt_GT_WithDpc());

        // ---- Encoding based on DPC+SSN - removeSpc on
        // ---- removeSpc on
        this.stack.setRemoveSpc(true);
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1447, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1446, 8);
        msg = (SccpDataMessageImpl) messageFactory.createDataMessageClass1(calledAdd, callingAdd, getData(), 0, 8, false, null,
                null);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), this.getDataUdt_DpcSsn());

        // ---- removeSpc off
        this.stack.setRemoveSpc(false);

        res = msg.encode(stack,LongMessageRuleType.LONG_MESSAGE_FORBBIDEN, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), this.getDataUdt_DpcSsn());
    }
}
