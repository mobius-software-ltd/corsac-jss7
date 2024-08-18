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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.NotReachableReason;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
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

    @Test
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

    @Test
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