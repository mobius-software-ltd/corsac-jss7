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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class IMEITest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 8, 33, 67, 101, (byte) 135, 9, 33, 67, 101 };
    }

    private byte[] getEncodedDataImeiLengthLessThan15() {
        return new byte[] { 4, 1, -15 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IMEIImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IMEIImpl);
        IMEIImpl imei = (IMEIImpl)result.getResult();
        
        assertEquals(imei.getIMEI(), "1234567890123456");

        // Testing IMEI length != 15
        rawData = getEncodedDataImeiLengthLessThan15();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IMEIImpl);
        imei = (IMEIImpl)result.getResult();

        assertEquals(imei.getIMEI(), "1");
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IMEIImpl.class);
    	
        IMEIImpl imei = new IMEIImpl("1234567890123456");
        ByteBuf buffer=parser.encode(imei);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // Testing IMEI length != 15
        imei = new IMEIImpl("1");
        buffer=parser.encode(imei);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedDataImeiLengthLessThan15();

        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        IMEIImpl original = new IMEIImpl("12345123450000");

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "imei", IMEIImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        IMEIImpl copy = reader.read("imei", IMEIImpl.class);

        assertEquals(copy.getIMEI(), original.getIMEI());
    }*/
}
