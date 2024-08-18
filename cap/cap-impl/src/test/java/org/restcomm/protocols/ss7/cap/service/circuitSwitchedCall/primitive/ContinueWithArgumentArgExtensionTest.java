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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
*
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ContinueWithArgumentArgExtensionTest {

    public byte[] getData1() {
        return new byte[] { 48, 11, (byte) 128, 0, (byte) 129, 0, (byte) 130, 0, (byte) 163, 3, (byte) 128, 1, 12 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 9, (byte) 129, 0, (byte) 163, 5, (byte) 161, 3, (byte) 129, 1, 4 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentArgExtensionImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentArgExtensionImpl);
        
        ContinueWithArgumentArgExtensionImpl elem = (ContinueWithArgumentArgExtensionImpl)result.getResult();        
        assertTrue(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertTrue(elem.getSuppressOutgoingCallBarring());
        assertEquals((int) elem.getLegOrCallSegment().getCallSegmentID(), 12);

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ContinueWithArgumentArgExtensionImpl);
        
        elem = (ContinueWithArgumentArgExtensionImpl)result.getResult();
        assertFalse(elem.getSuppressDCsi());
        assertTrue(elem.getSuppressNCsi());
        assertFalse(elem.getSuppressOutgoingCallBarring());
        assertEquals(elem.getLegOrCallSegment().getLegID().getReceivingSideID(), LegType.leg4);
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(ContinueWithArgumentArgExtensionImpl.class);
    	
        LegOrCallSegmentImpl legOrCallSegment = new LegOrCallSegmentImpl(12);
        ContinueWithArgumentArgExtensionImpl elem = new ContinueWithArgumentArgExtensionImpl(true, true, true, legOrCallSegment);
//        boolean suppressDCSI, boolean suppressNCSI,
//        boolean suppressOutgoingCallBarring, LegOrCallSegment legOrCallSegment
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LegIDImpl legID = new LegIDImpl(LegType.leg4 , null);
        legOrCallSegment = new LegOrCallSegmentImpl(legID);
        elem = new ContinueWithArgumentArgExtensionImpl(false, true, false, legOrCallSegment);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
