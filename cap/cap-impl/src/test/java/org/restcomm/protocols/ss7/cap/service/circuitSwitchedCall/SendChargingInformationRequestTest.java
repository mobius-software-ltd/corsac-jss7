/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SendChargingInformationRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 43, (byte) 128, 16, (byte) 161, 14, (byte) 160, 9, (byte) 128, 1, 1, (byte) 129, 1, 2,
                (byte) 130, 1, 3, (byte) 129, 1, 100, (byte) 161, 3, (byte) 128, 1, 2, (byte) 162, 18, 48, 5, 2, 1, 2,
                (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SendChargingInformationRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SendChargingInformationRequestImpl);
        
        SendChargingInformationRequestImpl elem = (SendChargingInformationRequestImpl)result.getResult();                
        this.testCAI_GSM0224(elem.getSCIBillingChargingCharacteristics().getAOCSubsequent().getCAI_GSM0224());
        assertEquals((int) elem.getSCIBillingChargingCharacteristics().getAOCSubsequent().getTariffSwitchInterval(), 100);
        assertEquals(elem.getPartyToCharge(), LegType.leg2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SendChargingInformationRequestImpl.class);
    	
        CAI_GSM0224Impl gsm224 = new CAI_GSM0224Impl(1, 2, 3, null, null, null, null);
        AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(gsm224, 100);
        
        SCIBillingChargingCharacteristicsImpl sciBillingChargingCharacteristics = new SCIBillingChargingCharacteristicsImpl(
                aocSubsequent);
        SendChargingInformationRequestImpl elem = new SendChargingInformationRequestImpl(sciBillingChargingCharacteristics,
        		LegType.leg2, CAPExtensionsTest.createTestCAPExtensions());
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, SendingSideID partyToCharge,
        // CAPExtensions extensions
    }

    private void testCAI_GSM0224(CAI_GSM0224 gsm224) {
        assertEquals((int) gsm224.getE1(), 1);
        assertEquals((int) gsm224.getE2(), 2);
        assertEquals((int) gsm224.getE3(), 3);
        assertNull(gsm224.getE4());
        assertNull(gsm224.getE5());
        assertNull(gsm224.getE6());
        assertNull(gsm224.getE7());
    }
}
