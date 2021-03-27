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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContextInfoImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberState;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PSSubscriberStateImpl;
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
        
        assertEquals(impl.getChoice(), PSSubscriberState.psDetached);
        assertNull(impl.getPDPContextInfoList());
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataActiveReachableForPaging();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PSSubscriberStateImpl);
        impl = (PSSubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getChoice(), PSSubscriberState.psPDPActiveReachableForPaging);
        assertEquals(impl.getPDPContextInfoList().size(), 2);
        assertEquals(impl.getPDPContextInfoList().get(0).getPdpContextIdentifier(), 5);
        assertEquals(impl.getPDPContextInfoList().get(1).getPdpContextIdentifier(), 6);
        assertNull(impl.getNetDetNotReachable());

        rawData = getEncodedDataNotReachableReason();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PSSubscriberStateImpl);
        impl = (PSSubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getChoice(), PSSubscriberState.netDetNotReachable);
        assertNull(impl.getPDPContextInfoList());
        assertEquals(impl.getNetDetNotReachable(), NotReachableReason.msPurged);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PSSubscriberStateImpl.class);
    	
        PSSubscriberStateImpl impl = new PSSubscriberStateImpl(PSSubscriberState.psDetached, null, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedDataDetached();
        assertTrue(Arrays.equals(rawData, encodedData));

        ArrayList<PDPContextInfoImpl> pdpContextInfoList = new ArrayList<PDPContextInfoImpl>();
        PDPContextInfoImpl ci1 = new PDPContextInfoImpl(5, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        PDPContextInfoImpl ci2 = new PDPContextInfoImpl(6, false, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        pdpContextInfoList.add(ci1);
        pdpContextInfoList.add(ci2);
        impl = new PSSubscriberStateImpl(PSSubscriberState.psPDPActiveReachableForPaging, null, pdpContextInfoList);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataActiveReachableForPaging();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new PSSubscriberStateImpl(PSSubscriberState.netDetNotReachable, NotReachableReason.msPurged, null);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedDataNotReachableReason();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}