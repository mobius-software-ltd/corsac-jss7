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

package org.restcomm.protocols.ss7.commonapp.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;
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
public class ExtQoSSubscribedTest {

    public byte[] getData1() {
        return new byte[] { 4, 9, 3, 115, (byte) 150, (byte) 254, (byte) 254, 116, 3, 0, 0 };
    };

    public byte[] getData2() {
        return new byte[] { 4, 9, 15, (byte) 130, (byte) 151, 23, (byte) 128, 22, 17, 24, 25 };
    };

    @Test(groups = { "functional.decode", "mobility.subscriberManagement" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtQoSSubscribedImpl.class);
    	
        byte[] data = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtQoSSubscribedImpl);
        ExtQoSSubscribedImpl prim = (ExtQoSSubscribedImpl)result.getResult();
        
        assertEquals(prim.getAllocationRetentionPriority(), 3);
        assertEquals(prim.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(prim.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(prim.getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(prim.getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(prim.getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(prim.getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(prim.getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(prim.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(prim.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(prim.getTransferDelay().getSourceData(), 0);
        assertEquals(prim.getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(prim.getGuaranteedBitRateForDownlink().getSourceData(), 0);


        data = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof ExtQoSSubscribedImpl);
        prim = (ExtQoSSubscribedImpl)result.getResult();

        assertEquals(prim.getAllocationRetentionPriority(), 15);
        assertEquals(prim.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(prim.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved);
        assertEquals(prim.getTrafficClass(), ExtQoSSubscribed_TrafficClass.backgroundClass);
        assertEquals(prim.getMaximumSduSize().getMaximumSduSize(), 1502);
        assertEquals(prim.getMaximumBitRateForUplink().getBitRate(), 23);
        assertEquals(prim.getMaximumBitRateForDownlink().getBitRate(), 576);
        assertEquals(prim.getResidualBER(), ExtQoSSubscribed_ResidualBER._5_10_minus_2);
        assertEquals(prim.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_6);
        assertEquals(prim.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1);
        assertEquals(prim.getTransferDelay().getTransferDelay(), 40);
        assertEquals(prim.getGuaranteedBitRateForUplink().getBitRate(), 24);
        assertEquals(prim.getGuaranteedBitRateForDownlink().getBitRate(), 25);
    }

    @Test(groups = { "functional.encode", "mobility.subscriberManagement" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(ExtQoSSubscribedImpl.class);
    	
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1500, false);
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelay(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribedImpl prim = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);

        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertEquals(encodedData, this.getData1());


        maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1502, false);
        maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(23, false);
        maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(576, false);
        transferDelay = new ExtQoSSubscribed_TransferDelay(40, false);
        guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(24, false);
        guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(25, false);
        prim = new ExtQoSSubscribedImpl(15, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes,
                ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved, ExtQoSSubscribed_TrafficClass.backgroundClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._5_10_minus_2, ExtQoSSubscribed_SduErrorRatio._1_10_minus_6,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);

        buffer=parser.encode(prim);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertEquals(encodedData, this.getData2());
    }
}
