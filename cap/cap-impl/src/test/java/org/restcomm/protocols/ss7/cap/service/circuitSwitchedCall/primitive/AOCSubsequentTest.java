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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

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
public class AOCSubsequentTest {

    public byte[] getData1() {
        return new byte[] { 48, 12, (byte) 160, 6, (byte) 131, 1, 4, (byte) 132, 1, 5, (byte) 129, 2, 0, (byte) 222 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AOCSubsequentImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AOCSubsequentImpl);
        
        AOCSubsequentImpl elem = (AOCSubsequentImpl)result.getResult();                
        assertNull(elem.getCAI_GSM0224().getE1());
        assertNull(elem.getCAI_GSM0224().getE2());
        assertNull(elem.getCAI_GSM0224().getE3());
        assertEquals((int) elem.getCAI_GSM0224().getE4(), 4);
        assertEquals((int) elem.getCAI_GSM0224().getE5(), 5);
        assertNull(elem.getCAI_GSM0224().getE6());
        assertNull(elem.getCAI_GSM0224().getE7());
        assertEquals((int) elem.getTariffSwitchInterval(), 222);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AOCSubsequentImpl.class);
    	
        CAI_GSM0224Impl cai_GSM0224 = new CAI_GSM0224Impl(null, null, null, 4, 5, null, null);
        AOCSubsequentImpl elem = new AOCSubsequentImpl(cai_GSM0224, 222);
        // CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
