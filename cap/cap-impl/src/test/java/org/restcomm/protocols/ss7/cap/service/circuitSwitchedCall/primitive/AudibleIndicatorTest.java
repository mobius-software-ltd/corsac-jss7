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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AudibleIndicatorImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AudibleIndicatorWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BurstImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BurstListImpl;
import org.testng.annotations.Test;

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
public class AudibleIndicatorTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, 1, 1, 0 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 10, (byte) 161, 8, (byte) 128, 1, 1, (byte) 161, 3, (byte) 128, 1, 2 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AudibleIndicatorWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AudibleIndicatorWrapperImpl);
        
        AudibleIndicatorWrapperImpl elem = (AudibleIndicatorWrapperImpl)result.getResult();        
        assertFalse(elem.getAudibleIndicator().getTone());
        assertNull(elem.getAudibleIndicator().getBurstList());


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AudibleIndicatorWrapperImpl);
        
        elem = (AudibleIndicatorWrapperImpl)result.getResult();     
        assertNull(elem.getAudibleIndicator().getTone());
        assertEquals((int) elem.getAudibleIndicator().getBurstList().getWarningPeriod(), 1);
        assertEquals((int) elem.getAudibleIndicator().getBurstList().getBursts().getNumberOfBursts(), 2);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AudibleIndicatorWrapperImpl.class);
    	
        AudibleIndicatorImpl elem = new AudibleIndicatorImpl(false);
        AudibleIndicatorWrapperImpl wrapper = new AudibleIndicatorWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));


        BurstImpl burst = new BurstImpl(2, null, null, null, null);
        BurstListImpl burstList = new BurstListImpl(1, burst);
        // Integer warningPeriod, Burst burst
        elem = new AudibleIndicatorImpl(burstList);
        wrapper = new AudibleIndicatorWrapperImpl(elem);
        
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
