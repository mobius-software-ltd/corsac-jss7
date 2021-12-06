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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
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
public class InitiateCallAttemptResponseTest {

    public byte[] getData1() {
        return new byte[] { 48, 30, -128, 2, 5, -32, -94, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1, -127, 2, 7, -128, -125, 0 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitiateCallAttemptResponseImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitiateCallAttemptResponseImpl);
        
        InitiateCallAttemptResponseImpl elem = (InitiateCallAttemptResponseImpl)result.getResult();         
        assertTrue(elem.getSupportedCamelPhases().getPhase1Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase2Supported());
        assertTrue(elem.getSupportedCamelPhases().getPhase3Supported());
        assertFalse(elem.getSupportedCamelPhases().getPhase4Supported());
        assertTrue(elem.getOfferedCamel4Functionalities().getInitiateCallAttempt());
        assertFalse(elem.getOfferedCamel4Functionalities().getCollectInformation());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertTrue(elem.getReleaseCallArgExtensionAllowed());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitiateCallAttemptResponseImpl.class);
    	
        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(true, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false);
        InitiateCallAttemptResponseImpl elem = new InitiateCallAttemptResponseImpl(supportedCamelPhases, offeredCamel4Functionalities,
                CAPExtensionsTest.createTestCAPExtensions(), true);
//        SupportedCamelPhases supportedCamelPhases,
//        OfferedCamel4Functionalities offeredCamel4Functionalities, CAPExtensions extensions,
//        boolean releaseCallArgExtensionAllowed

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    /*@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {

        SupportedCamelPhasesImpl supportedCamelPhases = new SupportedCamelPhasesImpl(true, true, true, false);
        OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities = new OfferedCamel4FunctionalitiesImpl(true, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false, false, false, false, false, false);
        InitiateCallAttemptResponseImpl original = new InitiateCallAttemptResponseImpl(supportedCamelPhases, offeredCamel4Functionalities,
                CAPExtensionsTest.createTestCAPExtensions(), true);

        // Writes the area to a file.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLObjectWriter writer = XMLObjectWriter.newInstance(baos);
        // writer.setBinding(binding); // Optional.
        writer.setIndentation("\t"); // Optional (use tabulation for
                                     // indentation).
        writer.write(original, "initiateCallAttemptResponse", InitiateCallAttemptResponseImpl.class);
        writer.close();

        byte[] rawData = baos.toByteArray();
        String serializedEvent = new String(rawData);

        System.out.println(serializedEvent);

        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        InitiateCallAttemptResponseImpl copy = reader.read("initiateCallAttemptResponse", InitiateCallAttemptResponseImpl.class);

        assertEquals(original.getSupportedCamelPhases().getPhase1Supported(), copy.getSupportedCamelPhases().getPhase1Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase2Supported(), copy.getSupportedCamelPhases().getPhase2Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase3Supported(), copy.getSupportedCamelPhases().getPhase3Supported());
        assertEquals(original.getSupportedCamelPhases().getPhase4Supported(), copy.getSupportedCamelPhases().getPhase4Supported());
        assertEquals(original.getOfferedCamel4Functionalities().getInitiateCallAttempt(), copy.getOfferedCamel4Functionalities().getInitiateCallAttempt());
        assertEquals(original.getOfferedCamel4Functionalities().getCollectInformation(), copy.getOfferedCamel4Functionalities().getCollectInformation());
        assertEquals(original.getReleaseCallArgExtensionAllowed(), copy.getReleaseCallArgExtensionAllowed());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(original.getExtensions()));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(copy.getExtensions()));

    }*/
}
