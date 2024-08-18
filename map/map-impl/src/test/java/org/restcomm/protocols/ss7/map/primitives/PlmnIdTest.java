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

package org.restcomm.protocols.ss7.map.primitives;

import static org.junit.Assert.assertEquals;
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
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class PlmnIdTest {

    private byte[] getEncodedData3Dig() {
        return new byte[] { 4, 3, 0x04, 0x15, (byte) 0x93 };
    }

    private byte[] getEncodedData2Dig() {
        return new byte[] { 4, 3, 0x04, (byte) 0xF5, (byte) 0x93 };
    }

    @Test
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

    @Test
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