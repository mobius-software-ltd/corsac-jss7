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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateChoise;
import org.testng.annotations.Test;

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
public class PSSubscriberStateTest {

    private byte[] getEncodedDataDetached() {
        return new byte[] { 48, 2, -127, 0 };
    }

    private byte[] getEncodedDataActiveReachableForPaging() {
        return new byte[] { 48, 12, -91, 10, 48, 3, -128, 1, 5, 48, 3, -128, 1, 6 };
    }

    private byte[] getEncodedDataNotReachableReason() {
        return new byte[] { 48, 3, 10, 1, 0 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PSSubscriberStateImpl.class);
    	        
        byte[] rawData = getEncodedDataDetached();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PSSubscriberStateImpl);
        PSSubscriberStateImpl impl = (PSSubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getChoice(), PSSubscriberStateChoise.psDetached);
        assertNull(impl.getPDPContextInfoList());
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataActiveReachableForPaging();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PSSubscriberStateImpl);
        impl = (PSSubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getChoice(), PSSubscriberStateChoise.psPDPActiveReachableForPaging);
        assertEquals(impl.getPDPContextInfoList().size(), 2);
        assertEquals(impl.getPDPContextInfoList().get(0).getPdpContextIdentifier(), 5);
        assertEquals(impl.getPDPContextInfoList().get(1).getPdpContextIdentifier(), 6);
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataNotReachableReason();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PSSubscriberStateImpl);
        impl = (PSSubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getChoice(), PSSubscriberStateChoise.netDetNotReachable);
        assertNull(impl.getPDPContextInfoList());
        assertEquals(impl.getNetDetNotReachable(), NotReachableReason.msPurged);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PSSubscriberStateImpl.class);
    	
        PSSubscriberStateImpl impl = new PSSubscriberStateImpl(PSSubscriberStateChoise.psDetached, null, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedDataDetached();
        assertTrue(Arrays.equals(rawData, encodedData));

        List<PDPContextInfo> pdpContextInfoList = new ArrayList<PDPContextInfo>();
        PDPContextInfoImpl ci1 = new PDPContextInfoImpl(5, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        PDPContextInfoImpl ci2 = new PDPContextInfoImpl(6, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        pdpContextInfoList.add(ci1);
        pdpContextInfoList.add(ci2);
        impl = new PSSubscriberStateImpl(PSSubscriberStateChoise.psPDPActiveReachableForPaging, null, pdpContextInfoList);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataActiveReachableForPaging();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new PSSubscriberStateImpl(PSSubscriberStateChoise.netDetNotReachable, NotReachableReason.msPurged, null);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataNotReachableReason();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}