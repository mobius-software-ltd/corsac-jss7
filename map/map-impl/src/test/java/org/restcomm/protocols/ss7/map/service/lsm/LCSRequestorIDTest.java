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

import org.restcomm.protocols.ss7.map.MAPParameterFactoryImpl;
import org.restcomm.protocols.ss7.map.api.MAPParameterFactory;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSFormatIndicator;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
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
 *
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class LCSRequestorIDTest {

    MAPParameterFactory MAPParameterFactory = new MAPParameterFactoryImpl();

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
        return new byte[] { 48, 0x13, (byte) 0x80, 0x01, 0x0f, (byte) 0x81, 0x0e, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86,
                (byte) 0xc3, 0x65, 0x6e, 0x72, (byte) 0xfb, 0x1c, (byte) 0x86, (byte) 0xc3, 0x65 };
    }

    public byte[] getDataFull() {
        return new byte[] { 48, 22, -128, 1, 15, -127, 14, 110, 114, -5, 28, -122, -61, 101, 110, 114, -5, 28, -122, -61, 101,
                -126, 1, 1 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSRequestorIDImpl.class);
    	
        byte[] data = getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSRequestorIDImpl);
        LCSRequestorIDImpl lcsRequestorID = (LCSRequestorIDImpl)result.getResult();

        assertEquals(lcsRequestorID.getDataCodingScheme().getCode(), 0x0f);
        assertNotNull(lcsRequestorID.getRequestorIDString());
        assertEquals(lcsRequestorID.getRequestorIDString().getString(null), "ndmgapp2ndmgapp2");

        assertNull(lcsRequestorID.getLCSFormatIndicator());

        data = getDataFull();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LCSRequestorIDImpl);
        lcsRequestorID = (LCSRequestorIDImpl)result.getResult();

        assertEquals(lcsRequestorID.getDataCodingScheme().getCode(), 0x0f);
        assertNotNull(lcsRequestorID.getRequestorIDString());
        assertEquals(lcsRequestorID.getRequestorIDString().getString(null), "ndmgapp2ndmgapp2");

        assertEquals(lcsRequestorID.getLCSFormatIndicator(), LCSFormatIndicator.emailAddress);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LCSRequestorIDImpl.class);
    	
        byte[] data = getData();

        USSDString nameString = MAPParameterFactory.createUSSDString("ndmgapp2ndmgapp2");
        LCSRequestorIDImpl lcsRequestorID = new LCSRequestorIDImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, null);
        ByteBuf buffer=parser.encode(lcsRequestorID);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        data = getDataFull();

        lcsRequestorID = new LCSRequestorIDImpl(new CBSDataCodingSchemeImpl(0x0f), nameString, LCSFormatIndicator.emailAddress);
        buffer=parser.encode(lcsRequestorID);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}