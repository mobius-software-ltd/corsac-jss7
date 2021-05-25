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

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEventsImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEventsWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
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
public class MidCallEventsTest {

    public byte[] getData1() {
        return new byte[] { 48, 7, (byte) 131, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 7, (byte) 132, 5, 99, 1, 2, 3, 4 };
    }

    public byte[] getDigitsData() {
        return new byte[] { 1, 2, 3, 4 };
    }

    @Test(groups = { "functional.decode", "EsiBcsm" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallEventsWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallEventsWrapperImpl);
        
        MidCallEventsWrapperImpl elem = (MidCallEventsWrapperImpl)result.getResult();        
        assertEquals(elem.getMidCallEventsWrapper().getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BINARY);
        ByteBuf buffer=elem.getMidCallEventsWrapper().getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        byte[] data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertEquals(data, getDigitsData());
        assertNull(elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut());


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof MidCallEventsWrapperImpl);
        
        elem = (MidCallEventsWrapperImpl)result.getResult();    
        assertEquals(elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut().getGenericDigits().getEncodingScheme(), GenericDigits._ENCODING_SCHEME_BINARY);
        buffer=elem.getMidCallEventsWrapper().getDTMFDigitsTimeOut().getGenericDigits().getEncodedDigits();
        assertNotNull(buffer);
        data=new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertEquals(data, getDigitsData());
        assertNull(elem.getMidCallEventsWrapper().getDTMFDigitsCompleted());
    }

    @Test(groups = { "functional.encode", "EsiBcsm" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(MidCallEventsWrapperImpl.class);
    	
        GenericDigitsImpl genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, Unpooled.wrappedBuffer(getDigitsData()));
        // int encodingScheme, int typeOfDigits, byte[] digits
        DigitsImpl dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEventsImpl elem = new MidCallEventsImpl(dtmfDigits, true);
        MidCallEventsWrapperImpl wrapper=new MidCallEventsWrapperImpl(elem);
//        Digits dtmfDigits, boolean isDtmfDigitsCompleted
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new MidCallEventsImpl(dtmfDigits, false);
        wrapper=new MidCallEventsWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "EsiBcsm" })
    public void testXMLSerializaion() throws Exception {
        GenericDigits genericDigits = new GenericDigitsImpl(GenericDigits._ENCODING_SCHEME_BINARY, GenericDigits._TOD_BGCI, getDigitsData());
        Digits dtmfDigits = new DigitsImpl(genericDigits);
        MidCallEventsImpl original = new MidCallEventsImpl(dtmfDigits, true);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "midCallEvents", MidCallEventsImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        MidCallEventsImpl copy = reader.read("midCallEvents", MidCallEventsImpl.class);

        assertEquals(copy.getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme(), original.getDTMFDigitsCompleted().getGenericDigits().getEncodingScheme());
        assertEquals(copy.getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits(), original.getDTMFDigitsCompleted().getGenericDigits().getEncodedDigits());
        assertNull(copy.getDTMFDigitsTimeOut());


        original = new MidCallEventsImpl(dtmfDigits, false);

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        writer.setIndentation("\t");
        writer.write(original, "midCallEvents", MidCallEventsImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("midCallEvents", MidCallEventsImpl.class);

        assertEquals(copy.getDTMFDigitsTimeOut().getGenericDigits().getEncodingScheme(), original.getDTMFDigitsTimeOut().getGenericDigits().getEncodingScheme());
        assertEquals(copy.getDTMFDigitsTimeOut().getGenericDigits().getEncodedDigits(), original.getDTMFDigitsTimeOut().getGenericDigits().getEncodedDigits());
        assertNull(copy.getDTMFDigitsCompleted());
    }*/
}
