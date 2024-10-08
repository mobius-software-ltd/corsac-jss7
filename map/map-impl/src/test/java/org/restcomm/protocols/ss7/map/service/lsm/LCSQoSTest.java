/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.map.service.lsm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;
import org.junit.AfterClass;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class LCSQoSTest {
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

    public byte[] getData() {
        return new byte[] { 0x30, 0x05, (byte) 0xa3, 0x03, 0x0a, 0x01, 0x00 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 54, -128, 1, 10, -127, 0, -126, 1, 20, -93, 3, 10, 1, 0, -92, 39, -96, 32, 48, 10, 6, 3, 42, 3,
                4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32,
                33 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSQoSImpl.class);
    	
        byte[] data = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSQoSImpl);
        LCSQoSImpl lcsQos = (LCSQoSImpl)result.getResult();

        assertNotNull(lcsQos.getResponseTime());
        ResponseTime resTime = lcsQos.getResponseTime();
        assertNotNull(resTime.getResponseTimeCategory());
        assertEquals(resTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        assertNull(lcsQos.getHorizontalAccuracy());
        assertFalse(lcsQos.getVerticalCoordinateRequest());
        assertNull(lcsQos.getVerticalAccuracy());
        assertNull(lcsQos.getExtensionContainer());

        data = getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSQoSImpl);
        lcsQos = (LCSQoSImpl)result.getResult();

        assertNotNull(lcsQos.getResponseTime());
        resTime = lcsQos.getResponseTime();
        assertNotNull(resTime.getResponseTimeCategory());
        assertEquals(resTime.getResponseTimeCategory(), ResponseTimeCategory.lowdelay);

        assertEquals((int) lcsQos.getHorizontalAccuracy(), 10);
        assertTrue(lcsQos.getVerticalCoordinateRequest());
        assertEquals((int) lcsQos.getVerticalAccuracy(), 20);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(lcsQos.getExtensionContainer()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSQoSImpl.class);
    	
        byte[] data = getData();

        LCSQoSImpl lcsQos = new LCSQoSImpl(null, null, false, new ResponseTimeImpl(ResponseTimeCategory.lowdelay), null);
        ByteBuf buffer=parser.encode(lcsQos);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        lcsQos = new LCSQoSImpl(10, 20, true, new ResponseTimeImpl(ResponseTimeCategory.lowdelay),
                MAPExtensionContainerTest.GetTestExtensionContainer());
        buffer=parser.encode(lcsQos);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}