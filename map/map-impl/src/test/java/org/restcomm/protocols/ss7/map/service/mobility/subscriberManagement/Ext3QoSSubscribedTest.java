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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribed_BitRateExtendedImpl;
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
public class Ext3QoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 2, 74, (byte) 250 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 2, 108, 0 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
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

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(Ext3QoSSubscribedImpl.class);
    	
        ExtQoSSubscribed_BitRateExtendedImpl maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(16000, false);
        ExtQoSSubscribed_BitRateExtendedImpl guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(256000, false);
        Ext3QoSSubscribedImpl prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertEquals(encodedData, this.getData1());


        maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(50000, false);
        guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtendedImpl(0, true);
        prim = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertEquals(encodedData, this.getData2());
    }
}