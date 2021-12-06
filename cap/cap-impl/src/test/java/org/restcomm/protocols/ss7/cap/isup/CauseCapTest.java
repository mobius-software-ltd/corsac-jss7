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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
public class CauseCapTest {

    public byte[] getData() {
        return new byte[] { 4, 2, (byte) 132, (byte) 144 };
    }

    public byte[] getIntData() {
        return new byte[] { (byte) 132, (byte) 144 };
    }

    @Test(groups = { "functional.decode", "isup" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CauseCapImpl.class);
    	
        byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CauseCapImpl);
        
        CauseCapImpl elem = (CauseCapImpl)result.getResult();        
        assertTrue(Arrays.equals(elem.getData(), this.getIntData()));
        CauseIndicators ci = elem.getCauseIndicators();
        assertEquals(ci.getCodingStandard(), 0);
        assertEquals(ci.getLocation(), 4);
        assertEquals(ci.getCauseValue(), 16);
        assertNull(ci.getDiagnostics());
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerializaion() throws Exception {

        CauseCapImpl original = new CauseCapImpl(this.getIntData());

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.

        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "causeCap", CauseCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CauseCapImpl copy = reader.read("causeCap", CauseCapImpl.class);

        assertEquals(copy.getCauseIndicators().getCauseValue(), original.getCauseIndicators().getCauseValue());
    }*/

    @Test(groups = { "functional.encode", "isup" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallingPartyNumberCapImpl.class);
    	
    	CauseCapImpl elem = new CauseCapImpl(this.getIntData());
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "isup" })
    public void testXMLSerialize() throws Exception {

        CauseIndicatorsImpl original0 = new CauseIndicatorsImpl(CauseIndicators._CODING_STANDARD_NATIONAL,
                CauseIndicators._LOCATION_PRIVATE_NSRU, 1, CauseIndicators._CV_CALL_REJECTED, null);

        CauseCapImpl original = new CauseCapImpl(original0);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "causeCap", CauseCapImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        CauseCapImpl copy = reader.read("causeCap", CauseCapImpl.class);

        assertEquals(copy.getCauseIndicators().getCodingStandard(), original.getCauseIndicators().getCodingStandard());
        assertEquals(copy.getCauseIndicators().getLocation(), original.getCauseIndicators().getLocation());
        assertEquals(copy.getCauseIndicators().getRecommendation(), original.getCauseIndicators().getRecommendation());
        assertEquals(copy.getCauseIndicators().getCauseValue(), original.getCauseIndicators().getCauseValue());
    }*/
}
