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

package org.restcomm.protocols.ss7.map.service.oam;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepth;
import org.restcomm.protocols.ss7.map.api.service.oam.TraceDepthListImpl;
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
public class TraceDepthListTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 30, (byte) 128, 1, 0, (byte) 129, 1, 1, (byte) 130, 1, 2, (byte) 131, 1, 0, (byte) 132, 1, 0, (byte) 133, 1, 1, (byte) 134, 1,
                1, (byte) 135, 1, 2, (byte) 136, 1, 2, (byte) 137, 1, 0 };
    }

    @Test(groups = { "functional.decode", "service.oam" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceDepthListImpl.class);
    	
        byte[] rawData = getEncodedData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof TraceDepthListImpl);
        TraceDepthListImpl asc = (TraceDepthListImpl)result.getResult();
        
        assertEquals(asc.getMscSTraceDepth(), TraceDepth.minimum);
        assertEquals(asc.getMgwTraceDepth(), TraceDepth.medium);
        assertEquals(asc.getSgsnTraceDepth(), TraceDepth.maximum);
        assertEquals(asc.getGgsnTraceDepth(), TraceDepth.minimum);
        assertEquals(asc.getRncTraceDepth(), TraceDepth.minimum);
        assertEquals(asc.getBmscTraceDepth(), TraceDepth.medium);
        assertEquals(asc.getMmeTraceDepth(), TraceDepth.medium);
        assertEquals(asc.getSgwTraceDepth(), TraceDepth.maximum);
        assertEquals(asc.getPgwTraceDepth(), TraceDepth.maximum);
        assertEquals(asc.getEnbTraceDepth(), TraceDepth.minimum);
    }

    @Test(groups = { "functional.encode", "service.oam" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(TraceDepthListImpl.class);
    	
        TraceDepthListImpl asc = new TraceDepthListImpl(TraceDepth.minimum, TraceDepth.medium, TraceDepth.maximum, TraceDepth.minimum, TraceDepth.minimum,
                TraceDepth.medium, TraceDepth.medium, TraceDepth.maximum, TraceDepth.maximum, TraceDepth.minimum);

        ByteBuf buffer=parser.encode(asc);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
