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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
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
public class ISDNAddressStringTest {

    private byte[] getEncodedData() {
        return new byte[] { 4, 7, -111, -105, 114, 99, 80, 24, -7 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(false);
    	parser.replaceClass(ISDNAddressStringImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ISDNAddressStringImpl);
        ISDNAddressStringImpl addStr = (ISDNAddressStringImpl)result.getResult();
        
        assertFalse(addStr.isExtension());
        assertEquals(addStr.getAddressNature(), AddressNature.international_number);
        assertEquals(addStr.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(addStr.getAddress(), "79273605819");
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ISDNAddressStringImpl.class);
    	
        ISDNAddressStringImpl addStr = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "79273605819");
        ByteBuf buffer=parser.encode(addStr);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
