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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class CAMELSCIGPRSBillingChargingCharacteristicsTest {

    public byte[] getData() {
        return new byte[] { 48, 42, -96, 37, -96, 21, -128, 1, 1, -127, 1, 2, -126, 1, 3, -125, 1, 4, -124, 1, 5, -123, 1, 6,
                -122, 1, 7, -95, 12, -96, 6, -125, 1, 4, -124, 1, 5, -127, 2, 0, -34, -127, 1, 1 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAMELSCIGPRSBillingChargingCharacteristicsImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELSCIGPRSBillingChargingCharacteristicsImpl);
        
        CAMELSCIGPRSBillingChargingCharacteristicsImpl prim = (CAMELSCIGPRSBillingChargingCharacteristicsImpl)result.getResult();        
        
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE1(), 1);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE2(), 2);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE3(), 3);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE4(), 4);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE5(), 5);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE6(), 6);
        assertEquals((int) prim.getAOCGPRS().getAOCInitial().getE7(), 7);
        assertNull(prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE1());
        assertNull(prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE2());
        assertNull(prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE3());
        assertEquals((int) prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE4(), 4);
        assertEquals((int) prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE5(), 5);
        assertNull(prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE6());
        assertNull(prim.getAOCGPRS().getAOCSubsequent().getCAI_GSM0224().getE7());
        assertEquals((int) prim.getAOCGPRS().getAOCSubsequent().getTariffSwitchInterval(), 222);
        assertEquals(prim.getPDPID().getId(), 1);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAMELSCIGPRSBillingChargingCharacteristicsImpl.class);
    	
        CAI_GSM0224Impl aocInitial = new CAI_GSM0224Impl(1, 2, 3, 4, 5, 6, 7);
        CAI_GSM0224Impl cai_GSM0224 = new CAI_GSM0224Impl(null, null, null, 4, 5, null, null);
        AOCSubsequentImpl aocSubsequent = new AOCSubsequentImpl(cai_GSM0224, 222);
        AOCGPRSImpl aocGPRS = new AOCGPRSImpl(aocInitial, aocSubsequent);

        PDPIDImpl pdpID = new PDPIDImpl(1);

        CAMELSCIGPRSBillingChargingCharacteristicsImpl prim = new CAMELSCIGPRSBillingChargingCharacteristicsImpl(aocGPRS, pdpID);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}