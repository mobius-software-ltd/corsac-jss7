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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CallSegmentToCancelImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
 *
 */
public class CancelRequestTest {

    public byte[] getData1() {
        return new byte[] { (byte) 128, 2, 42, (byte) 248 };
    }

    public byte[] getData2() {
        return new byte[] { (byte) 129, 0 };
    }

    public byte[] getData3() {
        return new byte[] { (byte) 162, 3, (byte) 129, 1, 20 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CancelRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CancelRequestImpl);
        
        CancelRequestImpl elem = (CancelRequestImpl)result.getResult();        
        assertEquals((int) elem.getInvokeID(), 11000);
        assertFalse(elem.getAllRequests());
        assertNull(elem.getCallSegmentToCancel());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CancelRequestImpl);
        
        elem = (CancelRequestImpl)result.getResult(); 
        assertNull(elem.getInvokeID());
        assertTrue(elem.getAllRequests());
        assertNull(elem.getCallSegmentToCancel());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CancelRequestImpl);
        
        elem = (CancelRequestImpl)result.getResult(); 
        assertNull(elem.getInvokeID());
        assertFalse(elem.getAllRequests());
        assertNull(elem.getCallSegmentToCancel().getInvokeID());
        assertEquals((int) elem.getCallSegmentToCancel().getCallSegmentID(), 20);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CancelRequestImpl.class);
    	
        CancelRequestImpl elem = new CancelRequestImpl(11000);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CancelRequestImpl(true);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        CallSegmentToCancelImpl callSegmentToCancel = new CallSegmentToCancelImpl(null, 20);
        // Integer invokeID, Integer callSegmentID
        elem = new CancelRequestImpl(callSegmentToCancel);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
