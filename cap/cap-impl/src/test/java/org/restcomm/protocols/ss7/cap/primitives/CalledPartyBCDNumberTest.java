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

import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
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
public class CalledPartyBCDNumberTest {

    public byte[] getData1() {
        return new byte[] { 4, 7, (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 6, (byte) 149, (byte) 232, 50, (byte) 155, (byte) 253, 6 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyBCDNumberImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CalledPartyBCDNumberImpl);
        
        CalledPartyBCDNumberImpl elem = (CalledPartyBCDNumberImpl)result.getResult();
        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getAddress().equals("41788005047"));
        assertFalse(elem.isExtension());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CalledPartyBCDNumberImpl);
        
        elem = (CalledPartyBCDNumberImpl)result.getResult();
        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.spare_5);
        assertTrue(elem.getAddress().equals("hello"));
        assertFalse(elem.isExtension());
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyBCDNumberImpl.class);
    	
        CalledPartyBCDNumberImpl elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // GSM 7-bit default alphabet definition and the SMS packing rules
        elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.spare_5, "hello");
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "primitives" })
    public void testXMLSerialize() throws Exception {

        CalledPartyBCDNumberImpl original = new CalledPartyBCDNumberImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "41788005047");

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "calledPartyBCDNumber", CalledPartyBCDNumberImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CalledPartyBCDNumberImpl copy = reader.read("calledPartyBCDNumber", CalledPartyBCDNumberImpl.class);

        assertEquals(copy.getAddressNature(), original.getAddressNature());
        assertEquals(copy.getNumberingPlan(), original.getNumberingPlan());
        assertEquals(copy.getAddress(), original.getAddress());
        assertEquals(copy.isExtension(), original.isExtension());
    }*/
}
