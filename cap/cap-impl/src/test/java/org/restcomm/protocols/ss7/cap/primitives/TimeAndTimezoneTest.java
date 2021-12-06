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

package org.restcomm.protocols.ss7.cap.primitives;

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
public class TimeAndTimezoneTest {

    public byte[] getData1() {
        return new byte[] { 4, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 35 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 8, 2, 17, 33, 3, 1, 112, (byte) 129, 43 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeAndTimezoneImpl.class);
    	
        byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeAndTimezoneImpl);
        
        TimeAndTimezoneImpl elem = (TimeAndTimezoneImpl)result.getResult();        
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), 32);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeAndTimezoneImpl);
        
        elem = (TimeAndTimezoneImpl)result.getResult();    
        assertEquals(elem.getYear(), 2011);
        assertEquals(elem.getMonth(), 12);
        assertEquals(elem.getDay(), 30);
        assertEquals(elem.getHour(), 10);
        assertEquals(elem.getMinute(), 7);
        assertEquals(elem.getSecond(), 18);
        assertEquals(elem.getTimeZone(), -32);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeAndTimezoneImpl.class);
    	
        TimeAndTimezoneImpl elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, -32);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        TimeAndTimezoneImpl original = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "timeAndTimezone", TimeAndTimezoneImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        TimeAndTimezoneImpl copy = reader.read("timeAndTimezone", TimeAndTimezoneImpl.class);

        assertEquals(copy.getYear(), original.getYear());
        assertEquals(copy.getMonth(), original.getMonth());
        assertEquals(copy.getDay(), original.getDay());
        assertEquals(copy.getHour(), original.getHour());
        assertEquals(copy.getMinute(), original.getMinute());
        assertEquals(copy.getSecond(), original.getSecond());
        assertEquals(copy.getTimeZone(), original.getTimeZone());
    }*/
    
}
