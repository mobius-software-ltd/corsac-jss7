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

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
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
public class CallInformationRequestRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 16, (byte) 160, 9, 10, 1, 1, 10, 1, 2, 10, 1, 30, (byte) 163, 3, (byte) 128, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 36, (byte) 160, 9, 10, 1, 1, 10, 1, 2, 10, 1, 30, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129,
                0, 48, 9, 2, 1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 163, 3, (byte) 128, 1, 2 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallInformationRequestRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallInformationRequestRequestImpl);
        
        CallInformationRequestRequestImpl elem = (CallInformationRequestRequestImpl)result.getResult();
        assertEquals(elem.getRequestedInformationTypeList().get(0), RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationTypeList().get(1), RequestedInformationType.callConnectedElapsedTime);
        assertEquals(elem.getRequestedInformationTypeList().get(2), RequestedInformationType.releaseCause);
        assertEquals(elem.getLegID(), LegType.leg2);
        assertNull(elem.getExtensions());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallInformationRequestRequestImpl);
        
        elem = (CallInformationRequestRequestImpl)result.getResult();
        assertEquals(elem.getRequestedInformationTypeList().get(0), RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationTypeList().get(1), RequestedInformationType.callConnectedElapsedTime);
        assertEquals(elem.getRequestedInformationTypeList().get(2), RequestedInformationType.releaseCause);
        assertEquals(elem.getLegID(), LegType.leg2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallInformationRequestRequestImpl.class);
    	
        ArrayList<RequestedInformationType> requestedInformationTypeList = new ArrayList<RequestedInformationType>();
        requestedInformationTypeList.add(RequestedInformationType.callStopTime);
        requestedInformationTypeList.add(RequestedInformationType.callConnectedElapsedTime);
        requestedInformationTypeList.add(RequestedInformationType.releaseCause);
        CallInformationRequestRequestImpl elem = new CallInformationRequestRequestImpl(requestedInformationTypeList, null,
        		LegType.leg2);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // ArrayList<RequestedInformationType> requestedInformationTypeList, CAPExtensions extensions, SendingSideID legID

        elem = new CallInformationRequestRequestImpl(requestedInformationTypeList, CAPExtensionsTest.createTestCAPExtensions(),
        		LegType.leg2);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}