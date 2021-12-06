/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.primitives.USSDStringImpl;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Real trace.
 *
 * TODO get trace with optional parameters and test
 *
 * @author amit bhayani
 *
 */
public class ProcessUnstructuredSSRequestTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeTest
    public void setUp() {
    }

    @AfterTest
    public void tearDown() {
    }

    @Test(groups = { "functional.decode", "service.ussd" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProcessUnstructuredSSRequestImpl.class);
    	
        byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ProcessUnstructuredSSRequestImpl);
        ProcessUnstructuredSSRequestImpl addNum = (ProcessUnstructuredSSRequestImpl)result.getResult();
       
        CBSDataCodingScheme dataCodingScheme = addNum.getDataCodingScheme();
        assertEquals(dataCodingScheme.getCode(), 0x0f);

        USSDString ussdString = addNum.getUSSDString();
        assertNotNull(ussdString);

        assertTrue(ussdString.getString(null).equals("*234#"));

    }

    @Test(groups = { "functional.encode", "service.ussd" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ProcessUnstructuredSSRequestImpl.class);
    	
        byte[] data = new byte[] { 0x30, 0x0a, 0x04, 0x01, 0x0f, 0x04, 0x05, 0x2a, (byte) 0xd9, (byte) 0x8c, 0x36, 0x02 };

        USSDStringImpl ussdStr = new USSDStringImpl("*234#", null, null);
        ProcessUnstructuredSSRequestImpl addNum = new ProcessUnstructuredSSRequestImpl(new CBSDataCodingSchemeImpl(0x0f),
                ussdStr, null, null);

        ByteBuf buffer=parser.encode(addNum);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);

        assertTrue(Arrays.equals(data, encodedData));
    }
}
