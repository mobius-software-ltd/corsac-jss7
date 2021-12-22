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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
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
public class IPSSPCapabilitiesTest {

    public byte[] getData1() {
        return new byte[] { 4, 1, 5 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 4, 26, 11, 22, 33 };
    }

    public byte[] getIntData1() {
        return new byte[] { 11, 22, 33 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IPSSPCapabilitiesImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IPSSPCapabilitiesImpl);
        
        IPSSPCapabilitiesImpl elem = (IPSSPCapabilitiesImpl)result.getResult();                
        assertTrue(elem.getIPRoutingAddressSupported());
        assertFalse(elem.getVoiceBackSupported());
        assertTrue(elem.getVoiceInformationSupportedViaSpeechRecognition());
        assertFalse(elem.getVoiceInformationSupportedViaVoiceRecognition());
        assertFalse(elem.getGenerationOfVoiceAnnouncementsFromTextSupported());
        assertNull(elem.getExtraData());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof IPSSPCapabilitiesImpl);
        
        elem = (IPSSPCapabilitiesImpl)result.getResult();         
        assertFalse(elem.getIPRoutingAddressSupported());
        assertTrue(elem.getVoiceBackSupported());
        assertFalse(elem.getVoiceInformationSupportedViaSpeechRecognition());
        assertTrue(elem.getVoiceInformationSupportedViaVoiceRecognition());
        assertTrue(elem.getGenerationOfVoiceAnnouncementsFromTextSupported());
        assertTrue(Arrays.equals(elem.getExtraData(), this.getIntData1()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(IPSSPCapabilitiesImpl.class);
    	
        IPSSPCapabilitiesImpl elem = new IPSSPCapabilitiesImpl(true, false, true, false, false, null);
        // boolean IPRoutingAddressSupported, boolean VoiceBackSupported, boolean VoiceInformationSupportedViaSpeechRecognition,
        // boolean VoiceInformationSupportedViaVoiceRecognition, boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[]
        // extraData
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new IPSSPCapabilitiesImpl(false, true, false, true, true, getIntData1());
        // boolean IPRoutingAddressSupported, boolean VoiceBackSupported, boolean VoiceInformationSupportedViaSpeechRecognition,
        // boolean VoiceInformationSupportedViaVoiceRecognition, boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[]
        // extraData
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall.primitive" })
    public void testXMLSerialize() throws Exception {

        IPSSPCapabilitiesImpl original = new IPSSPCapabilitiesImpl(true, false, true, false, false, null);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "ipsspCapabilities", IPSSPCapabilitiesImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        IPSSPCapabilitiesImpl copy = reader.read("ipsspCapabilities", IPSSPCapabilitiesImpl.class);

        assertEquals(copy.getData(), original.getData());

        original = new IPSSPCapabilitiesImpl(true, true, true, true, true, getIntData1());

        // Writes the area to a file.
        baos = new ByteArrayOutputStream();
        writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for indentation).
        writer.write(original, "ipsspCapabilities", IPSSPCapabilitiesImpl.class);
        writer.close();

        rawData = baos.toByteArray();
        serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        bais = new ByteArrayInputStream(rawData);
        reader = XMLObjectReader.newInstance(bais);
        copy = reader.read("ipsspCapabilities", IPSSPCapabilitiesImpl.class);

        assertEquals(copy.getData(), original.getData());

    }*/
}
