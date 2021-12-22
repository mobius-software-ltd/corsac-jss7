/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.RequestedInformationImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.DateAndTimeImpl;
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
public class CallInformationReportRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 44, (byte) 160, 37, 48, 8, (byte) 128, 1, 2, (byte) 161, 3, (byte) 130, 1, 0, 48, 14,
                (byte) 128, 1, 1, (byte) 161, 9, (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129, 48, 9, (byte) 128, 1, 30,
                (byte) 161, 4, (byte) 158, 2, (byte) 130, (byte) 145, (byte) 163, 3, (byte) 129, 1, 2 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 64, (byte) 160, 37, 48, 8, (byte) 128, 1, 2, (byte) 161, 3, (byte) 130, 1, 0, 48, 14,
                (byte) 128, 1, 1, (byte) 161, 9, (byte) 129, 7, 2, 17, 33, 3, 1, 112, (byte) 129, 48, 9, (byte) 128, 1, 30,
                (byte) 161, 4, (byte) 158, 2, (byte) 130, (byte) 145, (byte) 162, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2,
                1, 3, 10, 1, 1, (byte) 129, 1, (byte) 255, (byte) 163, 3, (byte) 129, 1, 2 };
    }

    public byte[] getDataInt1() {
        return new byte[] { -126, -111 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallInformationReportRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallInformationReportRequestImpl);
        
        CallInformationReportRequestImpl elem = (CallInformationReportRequestImpl)result.getResult();
        assertEquals(elem.getRequestedInformationList().get(0).getRequestedInformationType(),
                RequestedInformationType.callConnectedElapsedTime);
        assertEquals((int) elem.getRequestedInformationList().get(0).getCallConnectedElapsedTimeValue(), 0);
        assertEquals(elem.getRequestedInformationList().get(1).getRequestedInformationType(),
                RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getYear(), 2011);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMonth(), 12);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getDay(), 30);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getHour(), 10);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMinute(), 7);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getSecond(), 18);
        assertEquals(elem.getRequestedInformationList().get(2).getRequestedInformationType(),
                RequestedInformationType.releaseCause);
        assertTrue(Arrays.equals(elem.getRequestedInformationList().get(2).getReleaseCauseValue().getData(), getDataInt1()));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertNull(elem.getExtensions());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof CallInformationReportRequestImpl);
        
        elem = (CallInformationReportRequestImpl)result.getResult();
        assertEquals(elem.getRequestedInformationList().get(0).getRequestedInformationType(),
                RequestedInformationType.callConnectedElapsedTime);
        assertEquals((int) elem.getRequestedInformationList().get(0).getCallConnectedElapsedTimeValue(), 0);
        assertEquals(elem.getRequestedInformationList().get(1).getRequestedInformationType(),
                RequestedInformationType.callStopTime);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getYear(), 2011);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMonth(), 12);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getDay(), 30);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getHour(), 10);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getMinute(), 7);
        assertEquals(elem.getRequestedInformationList().get(1).getCallStopTimeValue().getSecond(), 18);
        assertEquals(elem.getRequestedInformationList().get(2).getRequestedInformationType(),
                RequestedInformationType.releaseCause);
        assertTrue(Arrays.equals(elem.getRequestedInformationList().get(2).getReleaseCauseValue().getData(), getDataInt1()));
        assertEquals(elem.getLegID(), LegType.leg2);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(CallInformationReportRequestImpl.class);
    	
        List<RequestedInformation> requestedInformationList = new ArrayList<RequestedInformation>();
        RequestedInformationImpl ri = new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime, 0);
        requestedInformationList.add(ri);
        DateAndTimeImpl callStopTimeValue = new DateAndTimeImpl(2011, 12, 30, 10, 7, 18);
        ri = new RequestedInformationImpl(callStopTimeValue);
        requestedInformationList.add(ri);
        CauseIsupImpl releaseCauseValue = new CauseIsupImpl(getDataInt1());
        ri = new RequestedInformationImpl(releaseCauseValue);
        requestedInformationList.add(ri);
        
        CallInformationReportRequestImpl elem = new CallInformationReportRequestImpl(requestedInformationList, null, LegType.leg2);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        elem = new CallInformationReportRequestImpl(requestedInformationList, CAPExtensionsTest.createTestCAPExtensions(),
        		LegType.leg2);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
