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

import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectingNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
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
public class RedirectingPartyIDCapTest {

    public byte[] getData() {
        return new byte[] { 4, 6, (byte) 131, 20, 7, 1, 9, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 131, 20, 7, 1, 9, 0 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RedirectingPartyIDCapImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RedirectingPartyIDCapImpl);
        
        RedirectingPartyIDCapImpl elem = (RedirectingPartyIDCapImpl)result.getResult();                
        RedirectingNumber rn = elem.getRedirectingNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(rn.getNatureOfAddressIndicator(), 3);
        assertTrue(rn.getAddress().equals("7010900"));
        assertEquals(rn.getNumberingPlanIndicator(), 1);
        assertEquals(rn.getAddressRepresentationRestrictedIndicator(), 1);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(RedirectingPartyIDCapImpl.class);
    	
        RedirectingPartyIDCapImpl elem = new RedirectingPartyIDCapImpl(this.getIntData());
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        RedirectingNumber rn = new RedirectingNumberImpl(3, "7010900", 1, 1);
        elem = new RedirectingPartyIDCapImpl(rn);
        rawData = this.getData();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int addressRepresentationRestrictedIndicator
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        RedirectingPartyIDCapImpl original = new RedirectingPartyIDCapImpl(new RedirectingNumberImpl(
                RedirectingNumber._NAI_NATIONAL_SN, "12345", RedirectingNumber._NPI_TELEX, RedirectingNumber._APRI_RESTRICTED));

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "redirectingPartyIDCap", RedirectingPartyIDCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        RedirectingPartyIDCapImpl copy = reader.read("redirectingPartyIDCap", RedirectingPartyIDCapImpl.class);

        assertEquals(copy.getRedirectingNumber().getNatureOfAddressIndicator(), original.getRedirectingNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getRedirectingNumber().getAddress(), original.getRedirectingNumber().getAddress());
        assertEquals(copy.getRedirectingNumber().getNumberingPlanIndicator(), original.getRedirectingNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getRedirectingNumber().getAddressRepresentationRestrictedIndicator(), original.getRedirectingNumber()
                .getAddressRepresentationRestrictedIndicator());
    }*/
}
