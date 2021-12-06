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

import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
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
public class LocationNumberCapTest {

    public byte[] getData() {
        return new byte[] { 4, 8, (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 132, (byte) 151, 8, 2, (byte) 151, 1, 32, 0 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LocationNumberCapImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LocationNumberCapImpl);
        
        LocationNumberCapImpl elem = (LocationNumberCapImpl)result.getResult();         
        LocationNumber ln = elem.getLocationNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertEquals(ln.getNatureOfAddressIndicator(), 4);
        assertTrue(ln.getAddress().equals("80207910020"));
        assertEquals(ln.getNumberingPlanIndicator(), 1);
        assertEquals(ln.getInternalNetworkNumberIndicator(), 1);
        assertEquals(ln.getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(ln.getScreeningIndicator(), 3);
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LocationNumberCapImpl.class);
    	
        LocationNumberCapImpl elem = new LocationNumberCapImpl(this.getIntData());
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LocationNumber cpn = new LocationNumberImpl(4, "80207910020", 1, 1, 1, 3);
        elem = new LocationNumberCapImpl(cpn);
        rawData = this.getData();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator, int
        // addressRepresentationREstrictedIndicator,
        // int screeningIndicator
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        LocationNumberImpl ln = new LocationNumberImpl(LocationNumber._NAI_NATIONAL_SN, "12345", LocationNumber._NPI_TELEX,
                LocationNumber._INN_ROUTING_NOT_ALLOWED, LocationNumber._APRI_ALLOWED,
                LocationNumber._SI_USER_PROVIDED_VERIFIED_PASSED);
        LocationNumberCapImpl original = new LocationNumberCapImpl(ln);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "locationNumberCap", LocationNumberCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        LocationNumberCapImpl copy = reader.read("locationNumberCap", LocationNumberCapImpl.class);

        assertEquals(copy.getLocationNumber().getNatureOfAddressIndicator(), original.getLocationNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getLocationNumber().getAddress(), original.getLocationNumber().getAddress());
        assertEquals(copy.getLocationNumber().getNumberingPlanIndicator(), original.getLocationNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getLocationNumber().getInternalNetworkNumberIndicator(), original.getLocationNumber()
                .getInternalNetworkNumberIndicator());
        assertEquals(copy.getLocationNumber().getAddressRepresentationRestrictedIndicator(), original.getLocationNumber()
                .getAddressRepresentationRestrictedIndicator());
        assertEquals(copy.getLocationNumber().getScreeningIndicator(), original.getLocationNumber().getScreeningIndicator());
        assertEquals(copy.getLocationNumber().isOddFlag(), original.getLocationNumber().isOddFlag());
    }*/
}
