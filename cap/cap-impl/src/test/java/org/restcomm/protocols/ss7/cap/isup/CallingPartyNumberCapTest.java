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

package org.restcomm.protocols.ss7.cap.isup;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
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
public class CallingPartyNumberCapTest {

    public byte[] getData() {
        return new byte[] { 4, 8, (byte) 132, 17, 20, (byte) 135, 9, 80, 64, (byte) 7 }; // 247
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 132, 17, 20, (byte) 135, 9, 80, 64, (byte) 7 }; // 247
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallingPartyNumberCapImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallingPartyNumberCapImpl);
        
        CallingPartyNumberCapImpl elem = (CallingPartyNumberCapImpl)result.getResult();        
        CallingPartyNumber cpn = elem.getCallingPartyNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertTrue(cpn.isOddFlag());
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
        assertEquals(cpn.getScreeningIndicator(), 1);
        assertEquals(cpn.getAddressRepresentationRestrictedIndicator(), 0);
        assertEquals(cpn.getNumberIncompleteIndicator(), 0);
        assertEquals(cpn.getNatureOfAddressIndicator(), 4);
        assertTrue(cpn.getAddress().equals("41789005047"));
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallingPartyNumberCapImpl.class);
    	
        CallingPartyNumberCapImpl elem = new CallingPartyNumberCapImpl(this.getIntData());
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CallingPartyNumber cpn = new CallingPartyNumberImpl(4, "41789005047", 1, 0, 0, 1);
        elem = new CallingPartyNumberCapImpl(cpn);
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int numberIncompleteIndicator, int
        // addressRepresentationREstrictedIndicator,
        // int screeningIndicator
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        CallingPartyNumberCapImpl original = new CallingPartyNumberCapImpl(new CallingPartyNumberImpl(
                NAINumber._NAI_NATIONAL_SN, "12345", CallingPartyNumber._NPI_TELEX, CallingPartyNumber._NI_INCOMPLETE,
                CallingPartyNumber._APRI_ALLOWED, CallingPartyNumber._SI_USER_PROVIDED_FAILED));

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "callingPartyNumberCap", CallingPartyNumberCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CallingPartyNumberCapImpl copy = reader.read("callingPartyNumberCap", CallingPartyNumberCapImpl.class);

        assertEquals(copy.getCallingPartyNumber().getNatureOfAddressIndicator(), original.getCallingPartyNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getCallingPartyNumber().getAddress(), original.getCallingPartyNumber().getAddress());
        assertEquals(copy.getCallingPartyNumber().getNumberingPlanIndicator(), original.getCallingPartyNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getCallingPartyNumber().getNumberIncompleteIndicator(), original.getCallingPartyNumber()
                .getNumberIncompleteIndicator());
        assertEquals(copy.getCallingPartyNumber().getAddressRepresentationRestrictedIndicator(), original
                .getCallingPartyNumber().getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getCallingPartyNumber().getScreeningIndicator(), original.getCallingPartyNumber()
                .getScreeningIndicator());
    }*/
}
