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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
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
 * @author abhayani
 * @author yulianoifa
 *
 */
public class ProcessUnstructuredSSResponseTest {
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
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProcessUnstructuredSSResponseImpl.class);
    	
    	byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87,
                (byte) 0xd9, 0x61, (byte) 0xf7, (byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProcessUnstructuredSSResponseImpl);
        ProcessUnstructuredSSResponseImpl addNum = (ProcessUnstructuredSSResponseImpl)result.getResult();

        CBSDataCodingScheme dataCodingScheme = addNum.getDataCodingScheme();
        assertEquals(dataCodingScheme.getCode(), (byte) 0x0f);

        USSDString ussdString = addNum.getUSSDString();
        assertNotNull(ussdString);

        assertEquals(ussdString.getString(null), "Your balance = 350");

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProcessUnstructuredSSResponseImpl.class);
    	
    	byte[] data = new byte[] { 0x30, 0x15, 0x04, 0x01, 0x0f, 0x04, 0x10, (byte) 0xd9, 0x77, 0x5d, 0x0e, 0x12, (byte) 0x87,
                (byte) 0xd9, 0x61, (byte) 0xf7, (byte) 0xb8, 0x0c, (byte) 0xea, (byte) 0x81, 0x66, 0x35, 0x18 };

        USSDStringImpl ussdStr = new USSDStringImpl("Your balance = 350", null, null);
        ProcessUnstructuredSSResponseImpl addNum = new ProcessUnstructuredSSResponseImpl(new CBSDataCodingSchemeImpl(0x0f),
                ussdStr);

        ByteBuf buffer=parser.encode(addNum);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);

        assertTrue(Arrays.equals(data, encodedData));
    }
}
