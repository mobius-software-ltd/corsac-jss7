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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.LongMessageRuleType;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.SccpStackImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.BCDOddEncodingScheme;
import org.restcomm.protocols.ss7.sccp.impl.parameter.HopCounterImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ImportanceImpl;
import org.restcomm.protocols.ss7.sccp.impl.parameter.ReturnCauseImpl;
import org.restcomm.protocols.ss7.sccp.message.SccpMessage;
import org.restcomm.protocols.ss7.sccp.message.SccpNoticeMessage;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCause;
import org.restcomm.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SccpNoticeMessageTest {

    private Logger logger;
    private SccpStackImpl stack = new SccpStackImpl("SccpNoticeMessageTestStack");
    private MessageFactoryImpl messageFactory;

    @BeforeMethod
    public void setUp() {
        this.stack.start();
        this.messageFactory = new MessageFactoryImpl(stack);
        this.logger = LogManager.getLogger(SccpStackImpl.class.getCanonicalName());
    }

    @AfterMethod
    public void tearDown() {
        this.stack.stop();
    }

    public ByteBuf getDataUdtS() {
        return Unpooled.wrappedBuffer(new byte[] { 0x0A, 0x00, 0x03, 0x0D, 0x11, 0x0A, 0x52, (byte) 0x92, 
                0x03, // this was 0x00, however test method has TT set to 3. 
                0x11, 0x04, (byte) 0x99, (byte) 0x99,
                (byte) 0x99, (byte) 0x99, 0x09, 0x04, 0x03, (byte) 0xBE, 0x06, (byte) 0x92, (byte) 0x83, 0x65, (byte) 0x81,
                (byte) 0x80, 0x48, 0x04, 0x00, 0x00, 0x00, 0x01, 0x49, 0x04, 0x00, 0x17, 0x3A, 0x26, 0x6B, 0x2A, 0x28, 0x28,
                0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x1D, 0x61, 0x1B, (byte) 0x80, 0x02,
                0x07, (byte) 0x80, (byte) 0xA1, 0x09, 0x06, 0x07, 0x04, 0x00, 0x00, 0x01, 0x00, 0x32, 0x01, (byte) 0xA2, 0x03,
                0x02, 0x01, 0x00, (byte) 0xA3, 0x05, (byte) 0xA1, 0x03, 0x02, 0x01, 0x00, 0x6C, 0x46, (byte) 0xA1, 0x3C, 0x02,
                0x01, 0x02, 0x02, 0x01, 0x17, 0x30, 0x34, (byte) 0xA0, 0x32, 0x30, 0x06, (byte) 0x80, 0x01, 0x0E, (byte) 0x81,
                0x01, 0x01, 0x30, 0x06, (byte) 0x80, 0x01, 0x0D, (byte) 0x81, 0x01, 0x01, 0x30, 0x0B, (byte) 0x80, 0x01, 0x11,
                (byte) 0x81, 0x01, 0x01, (byte) 0xA2, 0x03, (byte) 0x80, 0x01, 0x01, 0x30, 0x0B, (byte) 0x80, 0x01, 0x11,
                (byte) 0x81, 0x01, 0x01, (byte) 0xA2, 0x03, (byte) 0x80, 0x01, 0x02, 0x30, 0x06, (byte) 0x80, 0x01, 0x12,
                (byte) 0x81, 0x01, 0x01, (byte) 0xA1, 0x06, 0x02, 0x01, 0x03, 0x02, 0x01, 0x1F });
    }

    public ByteBuf getDataUdtSSrc() {
        return Unpooled.wrappedBuffer(new byte[] { 101, -127, -128, 72, 4, 0, 0, 0, 1, 73, 4, 0, 23, 58, 38, 107, 42, 40, 40, 6, 7, 0, 17, -122, 5, 1,
                1, 1, -96, 29, 97, 27, -128, 2, 7, -128, -95, 9, 6, 7, 4, 0, 0, 1, 0, 50, 1, -94, 3, 2, 1, 0, -93, 5, -95, 3,
                2, 1, 0, 108, 70, -95, 60, 2, 1, 2, 2, 1, 23, 48, 52, -96, 50, 48, 6, -128, 1, 14, -127, 1, 1, 48, 6, -128, 1,
                13, -127, 1, 1, 48, 11, -128, 1, 17, -127, 1, 1, -94, 3, -128, 1, 1, 48, 11, -128, 1, 17, -127, 1, 1, -94, 3,
                -128, 1, 2, 48, 6, -128, 1, 18, -127, 1, 1, -95, 6, 2, 1, 3, 2, 1, 31 });
    };

    public ByteBuf getDataXudt1() {
        return Unpooled.wrappedBuffer(new byte[] { 18, 1, 15, 4, 6, 10, 15, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15, 18, 1, 7, 0 });
    }

    public ByteBuf getDataUdt() {
        return Unpooled.wrappedBuffer(new byte[] { 10, 1, 3, 5, 9, 2, 66, 8, 4, 67, 1, 0, 6, 5, 11, 12, 13, 14, 15 });
    }

    public ByteBuf getDataXudt1Src() {
        return Unpooled.wrappedBuffer(new byte[] { 11, 12, 13, 14, 15 });
    }

    public ByteBuf getDataLudt1() {
        return Unpooled.wrappedBuffer(new byte[] { 20, 5, 10, 7, 00, 8, 00, 11, 00, 00, 00, 2, 66, 8, 4, 67, 1, 0, 6, 5, 00, 11, 12, 13, 14, 15 });
    }

    @Test(groups = { "SccpMessage", "functional.decode" })
    public void testDecode() throws Exception {

        // ---- UDTS
        ByteBuf buf = this.getDataUdtS();
        int type = buf.readByte();
        SccpNoticeMessage msg = (SccpNoticeMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(msg);
        assertNotNull(msg);
        assertEquals(msg.getReturnCause().getValue(), ReturnCauseValue.NO_TRANSLATION_FOR_NATURE);

        SccpAddress calledAdd = msg.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 146);
        assertTrue(calledAdd.getGlobalTitle().getDigits().equals("999999999"));

        SccpAddress callingAdd = msg.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1726);
        assertEquals(callingAdd.getSubsystemNumber(), 146);
        assertNull(callingAdd.getGlobalTitle());
        MessageSegmentationTest.assertByteBufs(msg.getData(), getDataUdtSSrc());

        // ---- XUDT without segm
        buf = this.getDataXudt1();
        type = buf.readByte();
        msg = (SccpNoticeMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(msg);
        assertNotNull(msg);
        assertEquals(msg.getReturnCause().getValue(), ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);

        assertEquals(msg.getHopCounter().getValue(), 15);
        calledAdd = msg.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = msg.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(msg.getSegmentation());
        assertEquals(msg.getImportance().getValue(), 7);
        MessageSegmentationTest.assertByteBufs(msg.getData(), getDataXudt1Src());

        // ---- LUDT
        buf = this.getDataLudt1();
        type = buf.readByte();
        msg = (SccpNoticeMessage) messageFactory.createMessage(type, 1, 2, 0, buf, SccpProtocolVersion.ITU, 0);
        System.out.println(msg);
        assertNotNull(msg);
        assertEquals(msg.getReturnCause().getValue(), ReturnCauseValue.MTP_FAILURE);

        assertEquals(msg.getHopCounter().getValue(), 10);
        calledAdd = msg.getCalledPartyAddress();
        assertNotNull(calledAdd);
        assertEquals(calledAdd.getSignalingPointCode(), 0);
        assertEquals(calledAdd.getSubsystemNumber(), 8);
        assertNull(calledAdd.getGlobalTitle());
        callingAdd = msg.getCallingPartyAddress();
        assertNotNull(callingAdd);
        assertEquals(callingAdd.getSignalingPointCode(), 1);
        assertEquals(callingAdd.getSubsystemNumber(), 6);
        assertNull(callingAdd.getGlobalTitle());
        assertNull(msg.getSegmentation());
        assertNull(msg.getImportance());
        MessageSegmentationTest.assertByteBufs(msg.getData(), getDataXudt1Src());

    }

    @Test(groups = { "SccpMessage", "functional.encode" })
    public void testEncode() throws Exception {

        // ---- UDTS
        this.stack.setRemoveSpc(false);
        GlobalTitle gt = stack.getSccpProvider().getParameterFactory().createGlobalTitle("999999999",3,NumberingPlan.ISDN_TELEPHONY,  BCDOddEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL); 
        SccpAddress calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, gt, 0,  146);
        SccpAddress callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, null, 1726, 146);
        ReturnCause rc = new ReturnCauseImpl(ReturnCauseValue.NO_TRANSLATION_FOR_NATURE);
        SccpNoticeMessageImpl msg = (SccpNoticeMessageImpl) messageFactory.createNoticeMessage(SccpMessage.MESSAGE_TYPE_UDT,
                rc, calledAdd, callingAdd, getDataUdtSSrc(), null, null);

        EncodingResultData res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), getDataUdtS());

        // ---- UDTS
        this.stack.setRemoveSpc(false);

        // ---- XUDT without segm
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        HopCounter hc = new HopCounterImpl(15);
        Importance imp = new ImportanceImpl((byte) 7);
        rc = new ReturnCauseImpl(ReturnCauseValue.NO_TRANSLATION_FOR_ADDRESS);
        msg = (SccpNoticeMessageImpl) messageFactory.createNoticeMessage(SccpMessage.MESSAGE_TYPE_XUDT, rc, calledAdd,
                callingAdd, getDataXudt1Src(), hc, imp);

        res = msg.encode(stack,LongMessageRuleType.XUDT_ENABLED, 272, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), getDataUdt());

        // ---- LUDT without segm
        calledAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 0, 8);
        callingAdd = stack.getSccpProvider().getParameterFactory().createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null, 1, 6);
        hc = new HopCounterImpl(10);
        rc = new ReturnCauseImpl(ReturnCauseValue.MTP_FAILURE);
        msg = (SccpNoticeMessageImpl) messageFactory.createNoticeMessage(SccpMessage.MESSAGE_TYPE_LUDT, rc, calledAdd,
                callingAdd, getDataXudt1Src(), hc, null);

        res = msg.encode(stack,LongMessageRuleType.LUDT_ENABLED, 2000, logger, false, SccpProtocolVersion.ITU);
        assertEquals(res.getEncodingResult(), EncodingResult.Success);
        MessageSegmentationTest.assertByteBufs(res.getSolidData(), getDataLudt1());
    }
}
