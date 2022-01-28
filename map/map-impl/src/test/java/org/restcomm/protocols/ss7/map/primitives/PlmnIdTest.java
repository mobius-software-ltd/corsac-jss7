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
 * @author sergey vetyutnev
 *
 */
public class PlmnIdTest {

    private byte[] getEncodedData3Dig() {
        return new byte[] { 4, 3, 0x04, 0x15, (byte) 0x93 };
    }

    private byte[] getEncodedData2Dig() {
        return new byte[] { 4, 3, 0x04, (byte) 0xF5, (byte) 0x93 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PlmnIdImpl.class);
    	        
        byte[] rawData= getEncodedData3Dig();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PlmnIdImpl);
        PlmnIdImpl pi = (PlmnIdImpl)result.getResult();
        
        assertEquals(pi.getMcc(), 405);
        assertEquals(pi.getMnc(), 391);

        rawData = getEncodedData2Dig();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PlmnIdImpl);
        pi = (PlmnIdImpl)result.getResult();
        
        assertEquals(pi.getMcc(), 405);
        assertEquals(pi.getMnc(), 39);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PlmnIdImpl.class);
    	                
        PlmnIdImpl pi = new PlmnIdImpl(405, 391);
        ByteBuf buffer=parser.encode(pi);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        byte[] rawData = getEncodedData3Dig();
        assertTrue(Arrays.equals(rawData, encodedData));

        pi = new PlmnIdImpl(405, 39);
        buffer=parser.encode(pi);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        rawData = getEncodedData2Dig();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}