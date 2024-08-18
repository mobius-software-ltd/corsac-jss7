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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
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
public class Ext3QoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 74, (byte) 250 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 2, 108, 0 };
    };

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(Ext3QoSSubscribedImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof Ext3QoSSubscribedImpl);
        Ext3QoSSubscribedImpl prim = (Ext3QoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(prim.getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertFalse(prim.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());

        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof Ext3QoSSubscribedImpl);
        prim = (Ext3QoSSubscribedImpl)result.getResult();

        assertEquals(prim.getMaximumBitRateForUplinkExtended().getBitRate(), 50000);
        assertEquals(prim.getGuaranteedBitRateForUplinkExtended().getBitRate(), 0);
        assertTrue(prim.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(Ext3QoSSubscribedImpl.class);
    	
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext3QoSSubscribedImpl prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertArrayEquals(encodedData, this.getData1());


        maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(50000, false);
        guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(0, true);
        prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertArrayEquals(encodedData, this.getData2());
    }
}