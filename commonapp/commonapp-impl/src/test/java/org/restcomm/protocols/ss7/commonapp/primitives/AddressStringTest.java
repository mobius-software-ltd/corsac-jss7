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
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class AddressStringTest {

    byte[] rawData = new byte[] { 4, 9, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
    byte[] rawData2 = new byte[] { 4, 5, -106, 33, -29, 78, -11 };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AddressStringImpl.class);
    	
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AddressStringImpl);
        AddressStringImpl addStr = (AddressStringImpl)result.getResult();
        
        assertFalse(addStr.isExtension());
        assertEquals(addStr.getAddressNature(), AddressNature.international_number);
        assertEquals(addStr.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(addStr.getAddress(), "204208300008002");


        result=parser.decode(Unpooled.wrappedBuffer(rawData2));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AddressStringImpl);
        addStr = (AddressStringImpl)result.getResult();
        
        assertFalse(addStr.isExtension());
        assertEquals(addStr.getAddressNature(), AddressNature.international_number);
        assertEquals(addStr.getNumberingPlan(), NumberingPlan.land_mobile);
        assertEquals(addStr.getAddress(), "123cc45");
    }

    @Test
    public void testEncode() throws Exception {
        AddressStringImpl addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile,
                "204208300008002");
        ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AddressStringImpl.class);
    	
    	ByteBuf buffer=parser.encode(addStr);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile, "123cc45");
        buffer=parser.encode(addStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData2, encodedData));

        addStr = new AddressStringImpl(AddressNature.international_number, NumberingPlan.land_mobile, "123CC45");
        buffer=parser.encode(addStr);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData2, encodedData));
    }
}
