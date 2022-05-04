/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

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
 * @author Lasith Waruna Perera
 * @author yulianoifa
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