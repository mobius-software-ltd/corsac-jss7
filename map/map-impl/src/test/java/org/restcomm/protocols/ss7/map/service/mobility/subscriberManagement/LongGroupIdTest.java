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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LongGroupIdImpl;
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

    @Test(groups = { "functional.decode", "primitives" })
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

    @Test(groups = { "functional.encode", "primitives" })
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