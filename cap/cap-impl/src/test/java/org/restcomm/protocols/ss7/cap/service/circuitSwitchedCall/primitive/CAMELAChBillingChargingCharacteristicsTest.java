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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AudibleIndicatorImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author alerant appngin
 * @author yulianoifa
 *
 */
public class CAMELAChBillingChargingCharacteristicsTest {

    public byte[] getData1() {
        return new byte[] { 4, 11, (byte) 160, 9, (byte) 128, 2, 46, (byte) 224, (byte) 161, 3, 1, 1, (byte) 255 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 35, (byte) 160, 33, (byte) 128, 2, 39, 16, (byte) 161, 23, 1, 1, (byte) 255,
                (byte) 170, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 130,
                2, 3, (byte) 232 };
    }

    public byte[] getData3() {
        return new byte[] { 4, 33, (byte) 160, 31, (byte) 128, 2, 39, 16, (byte) 129, 1, (byte) 255, (byte) 130, 2, 3,
                (byte) 232, (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255 };
    }

    public byte[] getData4() {
        return new byte[] { 4, 18, (byte) 160, 16, (byte) 128, 2, 39, 16, (byte) 129, 1, (byte) 255, (byte) 130, 2, 3, (byte) 232, (byte) 163, 3, 1, 1,
                (byte) 255 };
    }

    public byte[] getData5() {
        return new byte[] { 4, 11, (byte) 160, 9, (byte) 128, 2, 46, (byte) 224, (byte) 161, 3, 1, 1, 0 };
    }

    public byte[] getData6() {
        return new byte[] { 4, 9, (byte) 160, 7, (byte) 128, 2, 39, 16, (byte) 131, 1, (byte) 255 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAMELAChBillingChargingCharacteristicsImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        CAMELAChBillingChargingCharacteristicsImpl elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();                
        assertEquals(elem.getMaxCallPeriodDuration(), 12000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertTrue(elem.getAudibleIndicator().getTone());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertNull(elem.getAudibleIndicator());
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));

        rawData = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertEquals((int) (long) elem.getTariffSwitchInterval(), 1000);
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());

        rawData = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();
        assertEquals(elem.getMaxCallPeriodDuration(), 12000);
        assertTrue(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertNotNull(elem.getAudibleIndicator());
        assertFalse(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());

        rawData = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CAMELAChBillingChargingCharacteristicsImpl);
        
        elem = (CAMELAChBillingChargingCharacteristicsImpl)result.getResult();
        assertEquals(elem.getMaxCallPeriodDuration(), 10000);
        assertFalse(elem.getReleaseIfdurationExceeded());
        assertNull(elem.getTariffSwitchInterval());
        assertTrue(elem.getAudibleIndicator().getTone());
        assertNull(elem.getExtensions());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CAMELAChBillingChargingCharacteristicsImpl.class);
    	
        AudibleIndicatorImpl audibleIndicator = new AudibleIndicatorImpl(true);
        CAMELAChBillingChargingCharacteristicsImpl elem = new CAMELAChBillingChargingCharacteristicsImpl(12000, true, null, null);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, CAPExtensionsTest.createTestCAPExtensions(), 1000L);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, null, true, 1000L, CAPExtensionsTest.createTestCAPExtensions());
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, 1000L, audibleIndicator, null);
        rawData = this.getData4();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(12000, false, null, null);
        rawData = this.getData5();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CAMELAChBillingChargingCharacteristicsImpl(10000, true, false, null, null);
        rawData = this.getData6();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}