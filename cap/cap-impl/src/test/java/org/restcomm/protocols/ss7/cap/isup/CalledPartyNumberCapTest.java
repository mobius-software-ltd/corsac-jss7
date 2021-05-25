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

import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
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
public class CalledPartyNumberCapTest {

    public byte[] getData() {
        return new byte[] { 4, 7, 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
    }

    public byte[] getIntData() {
        return new byte[] { 3, (byte) 144, 33, 114, 16, (byte) 144, 0 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyNumberCapImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CalledPartyNumberCapImpl);
        
        CalledPartyNumberCapImpl elem = (CalledPartyNumberCapImpl)result.getResult();        
        CalledPartyNumber cpn = elem.getCalledPartyNumber();
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        assertFalse(cpn.isOddFlag());
        assertEquals(cpn.getNumberingPlanIndicator(), 1);
        assertEquals(cpn.getInternalNetworkNumberIndicator(), 1);
        assertEquals(cpn.getNatureOfAddressIndicator(), 3);
        assertTrue(cpn.getAddress().equals("1227010900"));
    }

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyNumberCapImpl.class);
    	
        CalledPartyNumberCapImpl elem = new CalledPartyNumberCapImpl(this.getIntData());
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CalledPartyNumber cpn = new CalledPartyNumberImpl(3, "1227010900", 1, 1);
        elem = new CalledPartyNumberCapImpl(cpn);
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // int natureOfAddresIndicator, String address, int numberingPlanIndicator, int internalNetworkNumberIndicator
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        CalledPartyNumberCapImpl original = new CalledPartyNumberCapImpl(new CalledPartyNumberImpl(
                CalledPartyNumber._NAI_INTERNATIONAL_NUMBER, "664422", CalledPartyNumber._NPI_ISDN,
                CalledPartyNumber._NAI_NRNINNF));

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "calledPartyNumberCap", CalledPartyNumberCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CalledPartyNumberCapImpl copy = reader.read("calledPartyNumberCap", CalledPartyNumberCapImpl.class);

        assertEquals(copy.getCalledPartyNumber().getNatureOfAddressIndicator(), original.getCalledPartyNumber()
                .getNatureOfAddressIndicator());
        assertEquals(copy.getCalledPartyNumber().getAddress(), original.getCalledPartyNumber().getAddress());
        assertEquals(copy.getCalledPartyNumber().getNumberingPlanIndicator(), original.getCalledPartyNumber()
                .getNumberingPlanIndicator());
        assertEquals(copy.getCalledPartyNumber().getInternalNetworkNumberIndicator(), original.getCalledPartyNumber()
                .getInternalNetworkNumberIndicator());

    }*/
}
