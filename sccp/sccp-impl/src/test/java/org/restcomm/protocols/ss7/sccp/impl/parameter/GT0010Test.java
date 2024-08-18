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
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.impl.message.MessageSegmentationTest;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDecodeEven() throws Exception {
        // TODO: we are testing here BCD even. We will need to add national encoding when we add soem staff

        // create GT object and read data from ByteBuf
        GlobalTitle0010Impl gt1 = new GlobalTitle0010Impl();
        gt1.decode(Unpooled.wrappedBuffer(data), factory, SccpProtocolVersion.ITU);

        // check results
        assertEquals(gt1.getTranslationType(), 3);
        assertEquals(gt1.getDigits(), "9023629581");
    }

    @Test
    public void testEncodeEven() throws Exception {
        // TODO: we are testing here BCD even. We will need to add national encoding when we add soem staff

        ByteBuf bout = Unpooled.buffer();
        GlobalTitle0010Impl gt = new GlobalTitle0010Impl("9023629581", 3);

        gt.encode(bout, false, SccpProtocolVersion.ITU);
        MessageSegmentationTest.assertByteBufs(Unpooled.wrappedBuffer(data), bout);
    }
}
