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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CalledPartyBCDNumberImpl;

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
public class CalledPartyBCDNumberTest {

    public byte[] getData1() {
        return new byte[] { 4, 7, (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    public byte[] getData2() {
        return new byte[] { 4, 6, (byte) 149, (byte) 232, 50, (byte) 155, (byte) 253, 6 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyBCDNumberImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CalledPartyBCDNumberImpl);
        
        CalledPartyBCDNumberImpl elem = (CalledPartyBCDNumberImpl)result.getResult();
        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getAddress().equals("41788005047"));
        assertFalse(elem.isExtension());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CalledPartyBCDNumberImpl);
        
        elem = (CalledPartyBCDNumberImpl)result.getResult();
        assertEquals(elem.getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getNumberingPlan(), NumberingPlan.spare_5);
        assertTrue(elem.getAddress().equals("hello"));
        assertFalse(elem.isExtension());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CalledPartyBCDNumberImpl.class);
    	
        CalledPartyBCDNumberImpl elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        // GSM 7-bit default alphabet definition and the SMS packing rules
        elem = new CalledPartyBCDNumberImpl(AddressNature.international_number, NumberingPlan.spare_5, "hello");
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
