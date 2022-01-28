/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.primitives;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
* @author sergey vetyutnev
*
*/
public class AChChargingAddressTest {

    public byte[] getData1() {
        return new byte[] { 48, 5, (byte) 162, 3, (byte) 129, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 4, (byte) 159, 50, 1, 5 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AChChargingAddressImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AChChargingAddressImpl);
        
        AChChargingAddressImpl elem = (AChChargingAddressImpl)result.getResult();
        assertEquals(elem.getLegID().getReceivingSideID(), LegType.leg2);
        assertEquals(elem.getSrfConnection(), 0);


        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof AChChargingAddressImpl);
        
        elem = (AChChargingAddressImpl)result.getResult();
        assertNull(elem.getLegID());
        assertEquals(elem.getSrfConnection(), 5);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(AChChargingAddressImpl.class);
    	
        LegIDImpl legID = new LegIDImpl(LegType.leg2, null);
        AChChargingAddressImpl elem = new AChChargingAddressImpl(legID);

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        elem = new AChChargingAddressImpl(5);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
