/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
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
        assertTrue(ByteBufUtil.equals(elem.getExtraData(),Unpooled.wrappedBuffer(this.getIntData1())));
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

        elem = new IPSSPCapabilitiesImpl(false, true, false, true, true,Unpooled.wrappedBuffer(getIntData1()));
        // boolean IPRoutingAddressSupported, boolean VoiceBackSupported, boolean VoiceInformationSupportedViaSpeechRecognition,
        // boolean VoiceInformationSupportedViaVoiceRecognition, boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[]
        // extraData
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
