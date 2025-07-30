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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageSegmentationTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author kulikov
 * @author baranowb
 * @author yulianoifa
 */
public class GT0100Test {

    private ByteBuf dataEven = Unpooled.wrappedBuffer(new byte[] { 0, 0x12, 0x03, 0x09, 0x32, 0x26, 0x59, 0x18 }); // Es.Even -> 0x12 & 0x0F
    private ByteBuf dataOdd = Unpooled.wrappedBuffer(new byte[] { 0, 0x11, 0x03, 0x09, 0x32, 0x26, 0x59, 0x08 }); // Es.Odd -> 0x11 & 0x0F - thus leading
    private ByteBuf dataHex = Unpooled.wrappedBuffer(new byte[] { 0, 17, 3, 9, -94, -53, 89, 8 });
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public GT0100Test() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of decode method, of class GT0011.
     */
    @Test
    public void testDecodeEven() throws Exception {
    	// create GT object and read data from ByteBuf
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(Unpooled.wrappedBuffer(dataEven), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test
    public void testEncodeEven() throws Exception {
        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("9023629581",0, BCDEvenEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(dataEven, bout);
    }

    /**
     * Test of decode method, of class GT0011.
     */
    @Test
    public void testDecodeOdd() throws Exception {
        // create GT object and read data from ByteBuf
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(Unpooled.wrappedBuffer(dataOdd), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "902362958");
    }

    @Test
    public void testEncodeHex() throws Exception {
        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("902ABC958",0, BCDOddEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(dataHex), bout);


        bout = Unpooled.buffer();
        gt = new GlobalTitle0100Impl("902abc958",0, BCDOddEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(dataHex), bout);
    }

    @Test
    public void testDecodeHex() throws Exception {
        // create GT object and read data from ByteBuf
        GlobalTitle0100Impl gt1 = new GlobalTitle0100Impl();
        gt1.decode(Unpooled.wrappedBuffer(dataHex), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 0);
        assertEquals(gt1.getNumberingPlan(), NumberingPlan.ISDN_TELEPHONY);
        assertEquals(gt1.getDigits(), "902abc958");
    }

    /**
     * Test of encode method, of class GT0011.
     */
    @Test
    public void testEncodeOdd() throws Exception {
        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0100Impl gt = new GlobalTitle0100Impl("902362958",0, BCDOddEncodingScheme.INSTANCE,NumberingPlan.ISDN_TELEPHONY, NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(dataOdd, bout);        
    }
}
