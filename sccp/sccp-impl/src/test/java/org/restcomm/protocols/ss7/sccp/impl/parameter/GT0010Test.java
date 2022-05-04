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

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageSegmentationTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class GT0010Test {
    private ByteBuf data = Unpooled.wrappedBuffer(new byte[] { 3, 0x09, 0x32, 0x26, 0x59, 0x18 });
    private ParameterFactoryImpl factory = new ParameterFactoryImpl();

    public GT0010Test() {
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

    @Test(groups = { "parameter", "functional.decode" })
    public void testDecodeEven() throws Exception {
        // TODO: we are testing here BCD even. We will need to add national encoding when we add soem staff

        // create GT object and read data from ByteBuf
        GlobalTitle0010Impl gt1 = new GlobalTitle0010Impl();
        gt1.decode(Unpooled.wrappedBuffer(data), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 3);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    @Test(groups = { "parameter", "functional.encode" })
    public void testEncodeEven() throws Exception {
        // TODO: we are testing here BCD even. We will need to add national encoding when we add soem staff

        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0010Impl gt = new GlobalTitle0010Impl("9023629581", 3);

        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data), bout);
    }
}
