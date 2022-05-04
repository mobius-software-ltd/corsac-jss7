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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageSegmentationTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author kulikov
 * @author baranowb
 * @author yulianoifa
 */
public class GT0001Test {

    private ByteBuf dataEven = Unpooled.wrappedBuffer(new byte[] { 3, 0x09, 0x32, 0x26, 0x59, 0x18 });
    private ByteBuf dataOdd = Unpooled.wrappedBuffer(new byte[] { (byte) (3 | 0x80), 0x09, 0x32, 0x26, 0x59, 0x08 });
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public GT0001Test() {
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
     * Test of decode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeEven() throws Exception {
    	
    	// create GT object and read data from ByteBuf
        GlobalTitle0001Impl gt1 = new GlobalTitle0001Impl();
        gt1.decode(Unpooled.wrappedBuffer(dataEven), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeEven() throws Exception {
        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0001Impl gt = new GlobalTitle0001Impl( "9023629581",NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(dataEven), bout);
    }

    /**
     * Test of decode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeOdd() throws Exception {
        // create GT object and read data from ByteBuf
        GlobalTitle0001Impl gt1 = new GlobalTitle0001Impl();
        gt1.decode(Unpooled.wrappedBuffer(dataOdd), factory, SccpProtocolVersion.ITU);
        // check results
        assertEquals(gt1.getNatureOfAddress(), NatureOfAddress.NATIONAL);
        assertEquals(gt1.getDigits(), "902362958");
    }

    /**
     * Test of encode method, of class GT0001Codec.
     */
    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeOdd() throws Exception {
    	ByteBuf bout = Unpooled.buffer();
        GlobalTitle0001Impl gt = new GlobalTitle0001Impl("902362958",NatureOfAddress.NATIONAL);
        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(dataOdd), bout);
    }
}
