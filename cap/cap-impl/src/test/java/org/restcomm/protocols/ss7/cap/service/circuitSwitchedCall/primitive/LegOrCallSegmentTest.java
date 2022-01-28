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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LegOrCallSegmentWrapperImpl;
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
public class LegOrCallSegmentTest {

    public byte[] getData1() {
        return new byte[] { 48, 3, (byte) 128, 1, 10 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 5, (byte) 161, 3, (byte) 128, 1, 3 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall.primitive" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LegOrCallSegmentWrapperImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LegOrCallSegmentWrapperImpl);
        
        LegOrCallSegmentWrapperImpl elem = (LegOrCallSegmentWrapperImpl)result.getResult();        
        assertEquals((int)elem.getLegOrCallSegment().getCallSegmentID(), 10);
        assertNull(elem.getLegOrCallSegment().getLegID());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof LegOrCallSegmentWrapperImpl);
        
        elem = (LegOrCallSegmentWrapperImpl)result.getResult(); 
        assertNull(elem.getLegOrCallSegment().getCallSegmentID());
        assertNull(elem.getLegOrCallSegment().getLegID().getReceivingSideID());
        assertEquals(elem.getLegOrCallSegment().getLegID().getSendingSideID(), LegType.leg3);
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall.primitive" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(LegOrCallSegmentWrapperImpl.class);
    	
        LegOrCallSegmentImpl elem = new LegOrCallSegmentImpl(10);
        LegOrCallSegmentWrapperImpl wrapper = new LegOrCallSegmentWrapperImpl(elem);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(wrapper);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LegIDImpl legId = new LegIDImpl(null, LegType.leg3);
        elem = new LegOrCallSegmentImpl(legId);
        wrapper = new LegOrCallSegmentWrapperImpl(elem);
        rawData = this.getData2();
        buffer=parser.encode(wrapper);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
