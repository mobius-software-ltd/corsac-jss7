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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
/**
 * 
 * @author yulianoifa
 *
 */
public class SuperChargerInfoTest {

    private byte[] getEncodedData1() {
        return new byte[] { 4, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 4, 3, (byte) 129, 1, 5 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SuperChargerInfoImpl.class);
    	
        byte[] data = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SuperChargerInfoImpl);
        SuperChargerInfoImpl asc = (SuperChargerInfoImpl)result.getResult();
        
        assertTrue(asc.getSendSubscriberData());
        assertNull(asc.getSubscriberDataStored());

        data = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SuperChargerInfoImpl);
        asc = (SuperChargerInfoImpl)result.getResult();

        assertFalse(asc.getSendSubscriberData());
        assertEquals(asc.getSubscriberDataStored().readableBytes(), 1);
        assertEquals(asc.getSubscriberDataStored().readByte(), 5);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SuperChargerInfoImpl.class);
    	
        SuperChargerInfoImpl asc = new SuperChargerInfoImpl(true);

        byte[] data=this.getEncodedData1();
        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));

        asc = new SuperChargerInfoImpl(Unpooled.wrappedBuffer(new byte[] { 5 }));
        data=this.getEncodedData2();
        buffer=parser.encode(asc);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(data, encodedData));
    }
}