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

package org.restcomm.protocols.ss7.map.primitives;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.lsm.DeferredLocationEventTypeImpl;
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
public class BitStringBaseTest {

    private byte[] getEncodedData() {
        return new byte[] { 3, 3, 4, (byte) 0xF0, (byte) 0xF0 };
    }

    private byte[] getEncodedDataTooShort() {
        return new byte[] { 3, 2, 4, (byte) 0xF0 };
    }

    private byte[] getEncodedDataTooLong() {
        return new byte[] { 3, 5, 4, (byte) 0xF0, (byte) 0xF0, 0, 0 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestBitStringImpl.class);
    	
        // correct data
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TestBitStringImpl);
        TestBitStringImpl pi = (TestBitStringImpl)result.getResult();
                
        assertTrue(pi.isBitSet(0));
        assertTrue(pi.isBitSet(1));
        assertTrue(pi.isBitSet(2));
        assertTrue(pi.isBitSet(3));
        assertFalse(pi.isBitSet(4));
        assertFalse(pi.isBitSet(5));
        assertFalse(pi.isBitSet(6));
        assertFalse(pi.isBitSet(7));
        assertTrue(pi.isBitSet(8));
        assertTrue(pi.isBitSet(9));
        assertTrue(pi.isBitSet(10));
        assertTrue(pi.isBitSet(11));        

        rawData = getEncodedDataTooShort();
        result = parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TestBitStringImpl);
        pi = (TestBitStringImpl)result.getResult();
                
        assertTrue(pi.isBitSet(0));
        assertTrue(pi.isBitSet(1));
        assertTrue(pi.isBitSet(2));
        assertTrue(pi.isBitSet(3));
        assertFalse(pi.isBitSet(4));
        assertFalse(pi.isBitSet(5));
        assertFalse(pi.isBitSet(6));
        assertFalse(pi.isBitSet(7));
        assertFalse(pi.isBitSet(8));
        assertFalse(pi.isBitSet(9));
        assertFalse(pi.isBitSet(10));
        assertFalse(pi.isBitSet(11));   
        
        rawData = getEncodedDataTooLong();
        result = parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TestBitStringImpl);
        pi = (TestBitStringImpl)result.getResult();
                
        assertTrue(pi.isBitSet(0));
        assertTrue(pi.isBitSet(1));
        assertTrue(pi.isBitSet(2));
        assertTrue(pi.isBitSet(3));
        assertFalse(pi.isBitSet(4));
        assertFalse(pi.isBitSet(5));
        assertFalse(pi.isBitSet(6));
        assertFalse(pi.isBitSet(7));
        assertTrue(pi.isBitSet(8));
        assertTrue(pi.isBitSet(9));
        assertTrue(pi.isBitSet(10));
        assertTrue(pi.isBitSet(11));   
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(TestBitStringImpl.class);
    	
        // correct data
    	TestBitStringImpl pi=new TestBitStringImpl();
    	pi.setBit(0);
    	pi.setBit(1);
    	pi.setBit(2);
    	pi.setBit(3);
    	pi.setBit(8);
    	pi.setBit(9);
    	pi.setBit(10);
    	pi.setBit(11);

        ByteBuf buffer=parser.encode(pi);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));

        // bad data
        try {
        	pi = new TestBitStringImpl(null);            
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test(groups = { "functional.encode", "equality" })
    public void testEquality() throws Exception {
        DeferredLocationEventTypeImpl imp1 = new DeferredLocationEventTypeImpl(true, false, true, false);
        DeferredLocationEventTypeImpl imp2 = new DeferredLocationEventTypeImpl(true, false, true, false);
        DeferredLocationEventTypeImpl imp3 = new DeferredLocationEventTypeImpl(false, true, true, false);
        
        assertTrue(imp1.equals(imp2));
        assertFalse(imp1.equals(imp3));
        assertFalse(imp2.equals(imp3));
    }
}