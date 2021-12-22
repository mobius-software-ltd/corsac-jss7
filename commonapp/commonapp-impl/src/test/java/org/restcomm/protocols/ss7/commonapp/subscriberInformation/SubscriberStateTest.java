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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
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
public class SubscriberStateTest {

    private byte[] getEncodedData1() {
        return new byte[] { 48, 2, (byte) 128, 0 };
    }

    private byte[] getEncodedData2() {
        return new byte[] { 48, 2, (byte) 129, 0 };
    }

    private byte[] getEncodedData3() {
        return new byte[] { 48, 3, 10, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberStateImpl.class);
    	
        byte[] rawData = getEncodedData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberStateImpl);
        SubscriberStateImpl impl = (SubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.assumedIdle);
        assertNull(impl.getNotReachableReason());

        rawData = getEncodedData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberStateImpl);
        impl = (SubscriberStateImpl)result.getResult();
        
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.camelBusy);
        assertNull(impl.getNotReachableReason());

        rawData = getEncodedData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof SubscriberStateImpl);
        impl = (SubscriberStateImpl)result.getResult();
        assertEquals(impl.getSubscriberStateChoice(), SubscriberStateChoice.netDetNotReachable);
        assertEquals(impl.getNotReachableReason(), NotReachableReason.imsiDetached);
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(SubscriberStateImpl.class);
    	        
        SubscriberStateImpl impl = new SubscriberStateImpl(SubscriberStateChoice.assumedIdle, null);
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData1();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new SubscriberStateImpl(SubscriberStateChoice.camelBusy, null);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData2();
        assertTrue(Arrays.equals(rawData, encodedData));

        impl = new SubscriberStateImpl(SubscriberStateChoice.netDetNotReachable, NotReachableReason.imsiDetached);
        buffer=parser.encode(impl);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        rawData = getEncodedData3();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}