/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
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
public class Ext2QoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 3, 17, 74, (byte) 250 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 3, 0, (byte) 142, 0 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(Ext2QoSSubscribedImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof Ext2QoSSubscribedImpl);
        Ext2QoSSubscribedImpl prim = (Ext2QoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getSourceStatisticsDescriptor(), Ext2QoSSubscribed_SourceStatisticsDescriptor.speech);
        assertTrue(prim.isOptimisedForSignallingTraffic());
        assertEquals(prim.getMaximumBitRateForDownlinkExtended().getBitRate(), 16000);
        assertEquals(prim.getGuaranteedBitRateForDownlinkExtended().getBitRate(), 256000);
        assertFalse(prim.getGuaranteedBitRateForDownlinkExtended().isUseNonextendedValue());


        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof Ext2QoSSubscribedImpl);
        prim = (Ext2QoSSubscribedImpl)result.getResult();

        assertEquals(prim.getSourceStatisticsDescriptor(), Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertFalse(prim.isOptimisedForSignallingTraffic());
        assertEquals(prim.getMaximumBitRateForDownlinkExtended().getBitRate(), 84000);
        assertEquals(prim.getGuaranteedBitRateForDownlinkExtended().getBitRate(), 0);
        assertTrue(prim.getGuaranteedBitRateForDownlinkExtended().isUseNonextendedValue());
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(Ext2QoSSubscribedImpl.class);
    	
        ExtQoSSubscribed_BitRateExtended maximumBitRateForDownlinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForDownlinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext2QoSSubscribedImpl prim = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.speech, true, maximumBitRateForDownlinkExtended,
                guaranteedBitRateForDownlinkExtended);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        assertEquals(encodedData, this.getData1());


        maximumBitRateForDownlinkExtended = new ExtQoSSubscribed_BitRateExtended(84000, false);
        guaranteedBitRateForDownlinkExtended = new ExtQoSSubscribed_BitRateExtended(0, true);
        prim = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown, false, maximumBitRateForDownlinkExtended,
                guaranteedBitRateForDownlinkExtended);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        
        assertEquals(encodedData, this.getData2());
    }
}