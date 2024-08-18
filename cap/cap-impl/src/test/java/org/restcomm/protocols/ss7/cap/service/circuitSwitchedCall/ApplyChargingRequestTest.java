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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.AChChargingAddressImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
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

    @Test
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

    @Test
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
