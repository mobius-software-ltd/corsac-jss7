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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.AChChargingAddressImpl;
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
public class ApplyChargingRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 14, (byte) 128, 7, (byte) 160, 5, (byte) 128, 3, 0, (byte) 140, (byte) 160, (byte) 162, 3,
                (byte) 128, 1, 1 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 34, (byte) 128, 7, (byte) 160, 5, (byte) 128, 3, 0, (byte) 140, (byte) 160, (byte) 162, 3,
                (byte) 128, 1, 1, (byte) 163, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1,
                (byte) 255 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 16, (byte) 128, 7, (byte) 160, 5, (byte) 128, 3, 0, (byte) 140, (byte) 160, (byte) 191, 50, 4, (byte) 159, 50, 1, 10 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingRequestImpl);
        
        ApplyChargingRequestImpl elem = (ApplyChargingRequestImpl)result.getResult();
        assertEquals((long) elem.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 36000);
        assertNull(elem.getAChBillingChargingCharacteristics().getAudibleIndicator());
        assertNull(elem.getAChBillingChargingCharacteristics().getExtensions());
        assertFalse(elem.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertNull(elem.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
        assertEquals(elem.getPartyToCharge(), LegType.leg1);
        assertNull(elem.getExtensions());
        assertNotNull(elem.getAChChargingAddress());
        assertNotNull(elem.getAChChargingAddress().getLegID());
        assertNotNull(elem.getAChChargingAddress().getLegID().getSendingSideID());
        assertNull(elem.getAChChargingAddress().getLegID().getReceivingSideID());
        assertEquals(elem.getAChChargingAddress().getLegID().getSendingSideID(), LegType.leg1);
        
        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingRequestImpl);
        
        elem = (ApplyChargingRequestImpl)result.getResult();
        assertEquals((long) elem.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 36000);
        assertNull(elem.getAChBillingChargingCharacteristics().getAudibleIndicator());
        assertNull(elem.getAChBillingChargingCharacteristics().getExtensions());
        assertFalse(elem.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertNull(elem.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
        assertEquals(elem.getPartyToCharge(), LegType.leg1);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertNotNull(elem.getAChChargingAddress());
        assertNotNull(elem.getAChChargingAddress().getLegID());
        assertNotNull(elem.getAChChargingAddress().getLegID().getSendingSideID());
        assertNull(elem.getAChChargingAddress().getLegID().getReceivingSideID());
        assertEquals(elem.getAChChargingAddress().getLegID().getSendingSideID(), LegType.leg1);
        
        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ApplyChargingRequestImpl);
        
        elem = (ApplyChargingRequestImpl)result.getResult();
        assertEquals((long) elem.getAChBillingChargingCharacteristics().getMaxCallPeriodDuration(), 36000);
        assertNull(elem.getAChBillingChargingCharacteristics().getAudibleIndicator());
        assertNull(elem.getAChBillingChargingCharacteristics().getExtensions());
        assertFalse(elem.getAChBillingChargingCharacteristics().getReleaseIfdurationExceeded());
        assertNull(elem.getAChBillingChargingCharacteristics().getTariffSwitchInterval());
        assertNotNull(elem.getPartyToCharge());
        assertEquals(elem.getPartyToCharge(), LegType.leg1);
        assertNull(elem.getExtensions());
        assertEquals(elem.getAChChargingAddress().getSrfConnection(), 10);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ApplyChargingRequestImpl.class);
    	
        CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics = new CAMELAChBillingChargingCharacteristicsImpl(
                36000, false, null, null, null);
        // long maxCallPeriodDuration, boolean releaseIfdurationExceeded, Long
        // tariffSwitchInterval,
        // AudibleIndicator audibleIndicator, CAPExtensions extensions, boolean
        ApplyChargingRequestImpl elem = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, LegType.leg1, null,
                null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // CAMELAChBillingChargingCharacteristics
        // aChBillingChargingCharacteristics, SendingSideID partyToCharge,
        // CAPExtensions extensions, AChChargingAddress aChChargingAddress


        elem = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, LegType.leg1,
                CAPExtensionsTest.createTestCAPExtensions(), null);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        AChChargingAddressImpl aChChargingAddress = new AChChargingAddressImpl(10);
        elem = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, null, null, aChChargingAddress);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
