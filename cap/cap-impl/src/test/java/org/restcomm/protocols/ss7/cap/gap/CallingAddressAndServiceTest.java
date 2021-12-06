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

package org.restcomm.protocols.ss7.cap.gap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.isup.DigitsImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class CallingAddressAndServiceTest {

    public static final int SERVICE_KEY = 821;

    public byte[] getData() {
        return new byte[] {(byte) 48, 10, (byte) 128, 4, 48, 69, 91, 84, (byte) 129, 2, 3, 53};
    }

    public byte[] getDigitsData() {
        return new byte[] {48, 69, 91, 84};
    }

    @Test(groups = { "functional.decode", "gap" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallingAddressAndServiceImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallingAddressAndServiceImpl);
        
        CallingAddressAndServiceImpl elem = (CallingAddressAndServiceImpl)result.getResult();        
        assertEquals(elem.getCallingAddressValue().getData(), getDigitsData());
        assertEquals(elem.getServiceKey(), SERVICE_KEY);
    }

    @Test(groups = { "functional.encode", "gap" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallingAddressAndServiceImpl.class);
    	
        DigitsImpl callingAddressValue = new DigitsImpl(getDigitsData());
        CallingAddressAndServiceImpl elem = new CallingAddressAndServiceImpl(callingAddressValue, SERVICE_KEY);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "gap" })
    public void testXMLSerialize() throws Exception {

        GenericNumberImpl gn = new GenericNumberImpl(GenericNumber._NAI_NATIONAL_SN, "12345",
                GenericNumber._NQIA_CONNECTED_NUMBER, GenericNumber._NPI_TELEX, GenericNumber._APRI_ALLOWED,
                GenericNumber._NI_INCOMPLETE, GenericNumber._SI_USER_PROVIDED_VERIFIED_FAILED);
        Digits digits = new DigitsImpl(gn);

        CallingAddressAndServiceImpl original = new CallingAddressAndServiceImpl(digits, SERVICE_KEY);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "CallingAddressAndServiceArg", CallingAddressAndServiceImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);

        CallingAddressAndServiceImpl copy = reader.read("CallingAddressAndServiceArg", CallingAddressAndServiceImpl.class);

        assertTrue(isEqual(original, copy));
    }

    private boolean isEqual(CallingAddressAndServiceImpl o1, CallingAddressAndServiceImpl o2) {
        if (o1 == o2)
            return true;
        if (o1 == null && o2 != null || o1 != null && o2 == null)
            return false;
        if (o1 == null && o2 == null)
            return true;
        if (!o1.toString().equals(o2.toString()))
            return false;
        return true;
    }*/
}
