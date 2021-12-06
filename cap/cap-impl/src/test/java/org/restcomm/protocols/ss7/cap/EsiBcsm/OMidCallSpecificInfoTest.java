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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.isup.DigitsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericDigitsImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
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
public class OMidCallSpecificInfoTest {

    public byte[] getData1() {
        return new byte[] { 48, 9, (byte) 161, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getDigitsData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OMidCallSpecificInfoImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof OMidCallSpecificInfoImpl);
        
        OMidCallSpecificInfoImpl elem = (OMidCallSpecificInfoImpl)result.getResult();        
        assertEquals(elem.getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BINARY);
        ByteBuf buffer=elem.getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertEquals(data, getDigitsData());
        assertNull(elem.getMidCallEvents().getDTMFDigitsTimeOut());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(OMidCallSpecificInfoImpl.class);
    	
    	GenericDigitsImpl genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, Unpooled.wrappedBuffer(getDigitsData()));
        // int encodingScheme, int typeOfDigits, byte[] digits
        DigitsImpl dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEventsImpl midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl elem = new OMidCallSpecificInfoImpl(midCallEvents);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        GenericDigits genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, getDigitsData());
        Digits dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEvents midCallEvents = new MidCallEventsImpl(dtmfDigits, true);
        OMidCallSpecificInfoImpl original = new OMidCallSpecificInfoImpl(midCallEvents);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "oMidCallSpecificInfo", OMidCallSpecificInfoImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        OMidCallSpecificInfoImpl copy = reader.read("oMidCallSpecificInfo", OMidCallSpecificInfoImpl.class);

        assertEquals(copy.getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme(), original.getMidCallEvents()
                .getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme());
        assertEquals(copy.getMidCallEvents().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original.getMidCallEvents()
                .getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());
        assertNull(copy.getMidCallEvents().getDTMFDigitsTimeOut());
    }*/
}
