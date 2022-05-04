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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertEquals;
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
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class TimeTest {

    public byte[] getData() {
        return new byte[] { 4, 4, -95, 17, 53, -98 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 4, 127, -2, -14, 30 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeImpl.class);
    	
    	// option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeImpl);
        TimeImpl prim = (TimeImpl)result.getResult();        
        
        assertEquals(prim.getYear(), 1985);
        assertEquals(prim.getMonth(), 8);
        assertEquals(prim.getDay(), 19);
        assertEquals(prim.getHour(), 3);
        assertEquals(prim.getMinute(), 40);
        assertEquals(prim.getSecond(), 14);

        // option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TimeImpl);
        prim = (TimeImpl)result.getResult();  

        assertEquals(prim.getYear(), 2104);
        assertEquals(prim.getMonth(), 2);
        assertEquals(prim.getDay(), 25);
        assertEquals(prim.getHour(), 14);
        assertEquals(prim.getMinute(), 30);
        assertEquals(prim.getSecond(), 54);

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TimeImpl.class);
    	
    	TimeImpl prim = new TimeImpl(1985, 8, 19, 3, 40, 14);
    	ByteBuf buffer = parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData()));

        prim = new TimeImpl(2104, 2, 25, 14, 30, 54);
        buffer = parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(encodedData, this.getData2()));
    }
}