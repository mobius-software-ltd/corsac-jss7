/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class AddressStringTest {

    byte[] rawData = new byte[] { 4, 9, (byte) 0x96, 0x02, 0x24, (byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2 };
    byte[] rawData2 = new byte[] { 4, 5, -106, 33, -29, 78, -11 };

    @Test(groups = { "functional.decode", "primitives" })
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

    @Test(groups = { "functional.encode", "primitives" })
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
