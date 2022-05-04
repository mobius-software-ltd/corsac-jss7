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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.indicator.RoutingIndicator;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageSegmentationTest;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0011;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author kulikov
 * @author sergey vetyunev
 * @author yulianoifa
 */
public class SccpAddressTest {

    // private SccpAddressCodec codec = new SccpAddressCodec(false);
    private ByteBuf data = Unpooled.wrappedBuffer(new byte[] { 0x12, (byte) 0x92, 0x00, 0x11, 0x04, (byte) 0x97, 0x20, (byte) 0x73, 0x00, (byte) 0x92,
            0x09 });

    private ByteBuf data4 = Unpooled.wrappedBuffer(new byte[] { -123, -110, 0, 17, -105, 32, 115, 0, -110, 9 });
    private ByteBuf data5 = Unpooled.wrappedBuffer(new byte[] { -121, -110, 0, 18, 122, 0, 17, -105, 32, 115, 0, -110, 9 });

    private ParameterFactoryImpl factory = new ParameterFactoryImpl();
    public SccpAddressTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUp() {
    }

    @AfterMethod
    public void tearDown() {
    }

    /**
     * Test of decode method, of class SccpAddressCodec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode1() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(Unpooled.wrappedBuffer(data), factory, SccpProtocolVersion.ITU);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getGlobalTitle().getDigits(), "79023700299");
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode2() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(Unpooled.wrappedBuffer(new byte[] { 0x42, 0x08 }), factory, SccpProtocolVersion.ITU);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 8);
        assertNull(address.getGlobalTitle());
    }

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecode4() throws Exception {
        SccpAddressImpl address = new SccpAddressImpl();
        address.decode(Unpooled.wrappedBuffer(data4), factory, SccpProtocolVersion.ANSI);
        assertEquals(address.getSignalingPointCode(), 0);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertFalse(address.getAddressIndicator().isPCPresent());
        assertTrue(address.getAddressIndicator().isSSNPresent());
        assertTrue(address.getAddressIndicator().isReservedForNationalUseBit());
        assertEquals(address.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        GlobalTitle gt = address.getGlobalTitle();
        assertEquals(gt.getDigits(), "79023700299");
        assertEquals(gt.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        GlobalTitle0011 gtt = (GlobalTitle0011)gt;
        assertEquals(gtt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gtt.getEncodingScheme(), BCDOddEncodingScheme.INSTANCE);
        assertEquals(gtt.getTranslationType(), 0);

        address.decode(Unpooled.wrappedBuffer(data5), factory, SccpProtocolVersion.ANSI);
        assertEquals(address.getSignalingPointCode(), 8000000);
        assertEquals(address.getSubsystemNumber(), 146);
        assertEquals(address.getAddressIndicator().getRoutingIndicator(), RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE);
        assertTrue(address.getAddressIndicator().isPCPresent());
        assertTrue(address.getAddressIndicator().isSSNPresent());
        assertTrue(address.getAddressIndicator().isReservedForNationalUseBit());
        assertEquals(address.getAddressIndicator().getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        gt = address.getGlobalTitle();
        assertEquals(gt.getDigits(), "79023700299");
        assertEquals(gt.getGlobalTitleIndicator(), GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_AND_ENCODING_SCHEME);
        gtt = (GlobalTitle0011)gt;
        assertEquals(gtt.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gtt.getEncodingScheme(), BCDOddEncodingScheme.INSTANCE);
        assertEquals(gtt.getTranslationType(), 0);
    }

    /**
     * Test of encode method, of class SccpAddressCodec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode() throws Exception {
        GlobalTitle gt = factory.createGlobalTitle("79023700299",0,NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL); 
        SccpAddressImpl address = (SccpAddressImpl)factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE,gt, 0, 146); 
                
        ByteBuf bin=Unpooled.buffer();
        address.encode(bin, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data), bin);
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode2() throws Exception {
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, null,0,  8);
        ByteBuf bin=Unpooled.buffer();
        address.encode(bin, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(new byte[] { 0x42, 0x08 }), bin);
    }

    /**
     * Test to see if the DPC is removed from the SCCP Address when instructed
     *
     * @throws Exception
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode3() throws Exception {
        byte[] data1 = new byte[] { 0x12, 0x06, 0x00, 0x11, 0x04, 0x39, 0x07, (byte) 0x92, 0x49, 0x00, 0x06 };
        // SccpAddressCodec codec = new SccpAddressCodec(true);

        GlobalTitle gt = factory.createGlobalTitle("93702994006",0, NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE,NatureOfAddress.INTERNATIONAL);
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 5530, 6);
        ByteBuf bin=Unpooled.buffer();
        address.encode(bin, true, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data1), bin);

        // Now test decode

    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncode4() throws Exception {
        GlobalTitle gt = factory.createGlobalTitle("79023700299", 0, NumberingPlan.ISDN_TELEPHONY, BCDOddEncodingScheme.INSTANCE);
        SccpAddressImpl address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 146);

        ByteBuf bin=Unpooled.buffer();
        address.encode(bin ,false, SccpProtocolVersion.ANSI);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data4), bin);

        bin=Unpooled.buffer();
        address.encode(bin, true, SccpProtocolVersion.ANSI);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data4), bin);

        address = (SccpAddressImpl) factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 8000000, 146);

        bin=Unpooled.buffer();
        address.encode(bin, false, SccpProtocolVersion.ANSI);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data5), bin);

        bin=Unpooled.buffer();
        address.encode(bin, true, SccpProtocolVersion.ANSI);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data4), bin);
    }

    /**
     * Test of getAddressIndicator method, of class SccpAddress.
     */
    @Test
    public void testEquals() {
        GlobalTitle gt = factory.createGlobalTitle("123",NatureOfAddress.NATIONAL);
        SccpAddress a1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        SccpAddress a2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt, 0, 0);
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    public void testEquals1() {
        GlobalTitle gt1 = factory.createGlobalTitle("79023700271",0,NumberingPlan.ISDN_TELEPHONY,null,NatureOfAddress.INTERNATIONAL); 
        SccpAddress a1 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt1, 146, 0);

        GlobalTitle gt2 = factory.createGlobalTitle("79023700271",0,NumberingPlan.ISDN_TELEPHONY,null,NatureOfAddress.INTERNATIONAL);
        SccpAddress a2 = factory.createSccpAddress(RoutingIndicator.ROUTING_BASED_ON_GLOBAL_TITLE, gt2, 146, 0);


        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }
}
