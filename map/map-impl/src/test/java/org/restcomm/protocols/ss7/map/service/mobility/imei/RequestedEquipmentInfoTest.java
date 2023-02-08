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

package org.restcomm.protocols.ss7.map.service.mobility.imei;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author normandes
 * @author yulianoifa
 *
 */
public class RequestedEquipmentInfoTest {

    private byte[] getEncodedData() {
        // TODO this is self generated trace. We need trace from operator
        return new byte[] { 3, 2, 6, -128 };
    }

    @Test(groups = { "functional.decode", "imei" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedEquipmentInfoImpl.class);
    	
    	byte[] data = getEncodedData();
    	ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof RequestedEquipmentInfoImpl);
        RequestedEquipmentInfoImpl imp = (RequestedEquipmentInfoImpl)result.getResult();
        
        assertTrue(imp.getEquipmentStatus());
        assertFalse(imp.getBmuef());
    }

    @Test(groups = { "functional.encode", "imei" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(RequestedEquipmentInfoImpl.class);
    	
    	RequestedEquipmentInfoImpl imp = new RequestedEquipmentInfoImpl(true, false);

    	byte[] data=this.getEncodedData();
    	ByteBuf buffer=parser.encode(imp);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}