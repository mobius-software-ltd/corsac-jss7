/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class SMSAddressStringTest {

    public byte[] getData() {
        return new byte[] { 4, 9, -111, 33, 67, 101, -121, 25, 50, 84, 118 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 11, (byte) 209, (byte) 208, (byte) 178, (byte) 188, 60, (byte) 167, (byte) 203, (byte) 223, (byte) 233, 117, 24 };
    };

	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SMSAddressStringImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SMSAddressStringImpl);
        
        SMSAddressStringImpl prim = (SMSAddressStringImpl)result.getResult();        
        assertEquals(prim.getAddressNature(), AddressNature.international_number);
        assertEquals(prim.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(prim.getAddress(), "1234567891234567");

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SMSAddressStringImpl);
        
        prim = (SMSAddressStringImpl)result.getResult(); 
        assertEquals(prim.getAddressNature(), AddressNature.reserved);
        assertEquals(prim.getNumberingPlan(), NumberingPlan.ISDN);
        assertEquals(prim.getAddress(), "Perestroika");
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		ASNParser parser=new ASNParser(true);
    	parser.replaceClass(SMSAddressStringImpl.class);
    	
    	SMSAddressStringImpl prim = new SMSAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "1234567891234567");
    	byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        prim = new SMSAddressStringImpl(AddressNature.reserved, NumberingPlan.ISDN, "Perestroika");
        rawData = this.getData2();
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
	}
}