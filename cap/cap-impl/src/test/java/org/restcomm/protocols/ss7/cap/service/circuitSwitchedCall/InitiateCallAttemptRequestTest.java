/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class InitiateCallAttemptRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 10, (byte) 160, 8, 4, 6, (byte) 129, 0, 34, 66, 68, 4 };
    }

    public byte[] getData2() {
        return new byte[] { 48, 62, (byte) 160, 8, 4, 6, (byte) 129, 0, 34, 66, 68, 4, (byte) 164, 18, 48, 5, 2, 1, 2, (byte) 129, 0, 48, 9, 2, 1, 3, 10, 1, 1,
                (byte) 129, 1, (byte) 255, (byte) 165, 3, (byte) 129, 1, 6, (byte) 134, 1, 15, (byte) 158, 5, (byte) 130, 1, 16, 98, 7, (byte) 159, 51, 4, 10,
                11, 12, 13, (byte) 159, 52, 4, (byte) 145, (byte) 136, 68, (byte) 248, (byte) 159, 53, 0 };
    }

    public byte[] getDataCallReferenceNumber() {
        return new byte[] { 10, 11, 12, 13 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitiateCallAttemptRequestImpl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitiateCallAttemptRequestImpl);
        
        InitiateCallAttemptRequestImpl elem = (InitiateCallAttemptRequestImpl)result.getResult();        
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        CalledPartyNumberIsup cpn = elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0);
        assertEquals(cpn.getCalledPartyNumber().getAddress(), "2224444");
        assertEquals(cpn.getCalledPartyNumber().getNatureOfAddressIndicator(), 1);
        assertNull(elem.getExtensions());
        assertNull(elem.getLegToBeCreated());
        assertNull(elem.getNewCallSegment());
        assertNull(elem.getCallingPartyNumber());
        assertNull(elem.getCallReferenceNumber());
        assertNull(elem.getGsmSCFAddress());
        assertFalse(elem.getSuppressTCsi());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitiateCallAttemptRequestImpl);
        
        elem = (InitiateCallAttemptRequestImpl)result.getResult();  
        assertEquals(elem.getDestinationRoutingAddress().getCalledPartyNumber().size(), 1);
        cpn = elem.getDestinationRoutingAddress().getCalledPartyNumber().get(0);
        assertEquals(cpn.getCalledPartyNumber().getAddress(), "2224444");
        assertEquals(cpn.getCalledPartyNumber().getNatureOfAddressIndicator(), 1);
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        assertEquals(elem.getLegToBeCreated().getReceivingSideID(), LegType.leg6);
        assertEquals((int) elem.getNewCallSegment(), 15);
        assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getAddress(), "01267");
        assertEquals(elem.getCallingPartyNumber().getCallingPartyNumber().getNatureOfAddressIndicator(), 2);
        assertTrue(ByteBufUtil.equals(elem.getCallReferenceNumber().getValue(), Unpooled.wrappedBuffer(getDataCallReferenceNumber())));
        assertEquals(elem.getGsmSCFAddress().getAddress(), "88448");
        assertTrue(elem.getSuppressTCsi());
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitiateCallAttemptRequestImpl.class);
    	
        CalledPartyNumberImpl calledPartyNumber = new CalledPartyNumberImpl(1, "2224444", 0, 0);
//        int natureOfAddresIndicator, String address, int numberingPlanIndicator,
//        int internalNetworkNumberIndicator
        CalledPartyNumberIsupImpl cpn = new CalledPartyNumberIsupImpl(calledPartyNumber);
        List<CalledPartyNumberIsup> calledPartyNumberArr = new ArrayList<CalledPartyNumberIsup>();
        calledPartyNumberArr.add(cpn);
        DestinationRoutingAddressImpl destinationRoutingAddress = new DestinationRoutingAddressImpl(calledPartyNumberArr);
        InitiateCallAttemptRequestImpl elem = new InitiateCallAttemptRequestImpl(destinationRoutingAddress, null, null, null, null, null, null, false);
//        DestinationRoutingAddress destinationRoutingAddress,
//        CAPExtensions extensions, LegID legToBeCreated, Integer newCallSegment,
//        CallingPartyNumberCap callingPartyNumber, CallReferenceNumber callReferenceNumber,
//        ISDNAddressString gsmSCFAddress, boolean suppressTCsi

        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        LegIDImpl legToBeCreated = new LegIDImpl(LegType.leg6,null);
        CallingPartyNumberImpl cpn2 = new CallingPartyNumberImpl(2, "01267", 0, 0, 0, 1);
//        int natureOfAddresIndicator, String address, int numberingPlanIndicator,
//        int numberIncompleteIndicator, int addressRepresentationREstrictedIndicator, int screeningIndicator
        CallingPartyNumberIsupImpl callingPartyNumber = new CallingPartyNumberIsupImpl(cpn2);
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(Unpooled.wrappedBuffer(getDataCallReferenceNumber()));
        ISDNAddressStringImpl gsmSCFAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "88448");
        elem = new InitiateCallAttemptRequestImpl(destinationRoutingAddress, CAPExtensionsTest.createTestCAPExtensions(), legToBeCreated, 15,
                callingPartyNumber, callReferenceNumber, gsmSCFAddress, true);

        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}
