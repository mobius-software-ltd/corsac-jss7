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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

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
public class LongGroupIdTest {

    public byte[] getData() {
        return new byte[] { 4, 4, 33, 67, 101, -121 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 4, 33, 67, 101, -9 };
    };

    public byte[] getData3() {
        return new byte[] { 4, 4, 33, 67, 101, -1 };
    };

    public byte[] getData4() {
        return new byte[] { 4, 4, 33, 67, -11, -1 };
    };

    public byte[] getData5() {
        return new byte[] { 4, 4, 33, 67, -1, -1 };
    };

    public byte[] getData6() {
        return new byte[] { 4, 4, 33, -13, -1, -1 };
    };

    public byte[] getData7() {
        return new byte[] { 4, 4, 33, -1, -1, -1 };
    };

    public byte[] getData8() {
        return new byte[] { 4, 4, -15, -1, -1, -1 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LongGroupIdImpl.class);
    	
        // option 1
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        LongGroupIdImpl prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("12345678"));

        // option 2
        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("1234567"));

        // option 3
        data = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("123456"));

        // option 4
        data = this.getData4();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("12345"));

        // option 5
        data = this.getData5();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("1234"));

        // option 6
        data = this.getData6();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("123"));

        // option 7
        data = this.getData7();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("12"));

        // option 8
        data = this.getData8();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LongGroupIdImpl);
        prim = (LongGroupIdImpl)result.getResult();
        assertTrue(prim.getLongGroupId().equals("1"));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(LongGroupIdImpl.class);
    	
        // option 1
        LongGroupIdImpl prim = new LongGroupIdImpl("12345678");
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 2
        prim = new LongGroupIdImpl("1234567");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData2();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 3
        prim = new LongGroupIdImpl("123456");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData3();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 4
        prim = new LongGroupIdImpl("12345");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData4();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 5
        prim = new LongGroupIdImpl("1234");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData5();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 6
        prim = new LongGroupIdImpl("123");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData6();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 7
        prim = new LongGroupIdImpl("12");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData7();
        assertTrue(Arrays.equals(encodedData, rawData));

        // option 8
        prim = new LongGroupIdImpl("1");
        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData); 
        rawData=this.getData8();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}