/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class AssistRequestInstructionsRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 33, (byte) 128, 8, 0, (byte) 128, 20, 17, 33, 34, 51, 3, (byte) 130, 1, 15, (byte) 163, 18,
                48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getIPSSPCapabilitiesInt() {
        return new byte[] { 111 };
    }

    public byte[] getGenericNumberInt() {
        return new byte[] { 0, -128, 20, 17, 33, 34, 51, 3 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AssistRequestInstructionsRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AssistRequestInstructionsRequestImpl);
        
        AssistRequestInstructionsRequestImpl elem = (AssistRequestInstructionsRequestImpl)result.getResult();        
        assertEquals(elem.getCorrelationID().getGenericNumber().getNatureOfAddressIndicator(), 0);
        assertTrue(elem.getCorrelationID().getGenericNumber().getAddress().equals("111222333"));
        assertEquals(elem.getCorrelationID().getGenericNumber().getNumberQualifierIndicator(), 0);
        assertEquals(elem.getCorrelationID().getGenericNumber().getNumberingPlanIndicator(), 1);
        assertEquals(elem.getCorrelationID().getGenericNumber().getAddressRepresentationRestrictedIndicator(), 1);
        assertEquals(elem.getCorrelationID().getGenericNumber().getScreeningIndicator(), 0);
        assertEquals(elem.getIPSSPCapabilities().getIPRoutingAddressSupported(), true);
        assertEquals(elem.getIPSSPCapabilities().getVoiceBackSupported(), true);
        assertEquals(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaSpeechRecognition(), true);
        assertEquals(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaVoiceRecognition(), true);
        assertEquals(elem.getIPSSPCapabilities().getGenerationOfVoiceAnnouncementsFromTextSupported(), false);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AssistRequestInstructionsRequestImpl.class);
    	
        GenericNumberImpl genericNumber = new GenericNumberImpl(0, "111222333", 0, 1, 1, false, 0);
        // GenericNumberImpl genericNumber = new GenericNumberImpl(getGenericNumberInt());
        // int natureOfAddresIndicator, String address, int numberQualifierIndicator, int numberingPlanIndicator, int
        // addressRepresentationREstrictedIndicator,
        // boolean numberIncomplete, int screeningIndicator
        DigitsIsupImpl correlationID = new DigitsIsupImpl(genericNumber);
        IPSSPCapabilitiesImpl ipSSPCapabilities = new IPSSPCapabilitiesImpl(true, true, true, true, false, null);

        AssistRequestInstructionsRequestImpl elem = new AssistRequestInstructionsRequestImpl(correlationID, ipSSPCapabilities,
                CAPExtensionsTest.createTestCAPExtensions());
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // Digits correlationID, IPSSPCapabilities ipSSPCapabilities, CAPExtensions extensions
    }
}
