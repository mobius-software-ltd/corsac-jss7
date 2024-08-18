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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRate;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_BitRateExtended;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOfErroneousSdus;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_DeliveryOrder;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_MaximumSduSize;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_ResidualBER;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_SduErrorRatio;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TrafficHandlingPriority;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed_TransferDelay;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
import org.junit.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class PDPContextInfoTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, -127, -116, -128, 1, 10, -127, 0, -126, 2, -15, 33, -125, 1, 21, -124, 3, 2, 22, 23, -123, 3, 
        		2, 24, 25, -122, 1, 11, -121, 1, 26, -120, 4, 27, 28, 29, 30, -119, 4, 31, 32, 33, 34, -118, 5, 4, 36, 37, 38, 
        		39, -117, 9, 3, 115, -106, -2, -2, 116, 3, 0, 0, -116, 9, 15, -126, -105, 23, -128, 22, 17, 24, 25, -115, 9, 3,
        		115, -106, -2, -2, 116, 3, 0, 0, -114, 4, 41, 42, 43, 44, -113, 2, 12, 0, -112, 5, 4, 48, 49, 50, 51, -110, 3, 
        		0, 0, 0, -109, 3, 16, 0, 0, -108, 3, 17, 0, 0, -107, 2, 74, -6, -106, 2, 108, 0, -105, 2, 0, 0, -103, 1, 91, 
        		-102, 1, 92, -101, 1, 93, -100, 2, 58, 59, -99, 1, 60 };
    }

    private byte[] getEncodedPDPAddress() {
        return new byte[] { 21 };
    }

    private byte[] getEncodedapnSubscribed() {
        return new byte[] { 22, 23 };
    }

    private byte[] getEncodedgetapnInUse() {
        return new byte[] { 24, 25 };
    }

    private byte[] getEncodedTransactionId() {
        return new byte[] { 26 };
    }

    private byte[] getEncodedTEID_1() {
        return new byte[] { 27, 28, 29, 30 };
    }

    private byte[] getEncodedTEID_2() {
        return new byte[] { 31, 32, 33, 34 };
    }

    private byte[] getEncodedggsnAddress() {
        return new byte[] { 36, 37, 38, 39 };
    }

    /*private byte[] getEncodedggsnAddress2() {
        return new byte[] { (byte) 192, (byte) 168, 0, 1 };
    }*/

    private byte[] getEncodedchargingId() {
        return new byte[] { 41, 42, 43, 44 };
    }

    private byte[] getEncodedrncAddress() {
        return new byte[] { 48, 49, 50, 51 };
    }

    /*private byte[] getEncodedrncAddress2() {
        return new byte[] { (byte) 192, (byte) 168, 5, 51 };
    }*/

    private byte[] getEncodedExtPDPType() {
        return new byte[] { 58, 59 };
    }

    private byte[] getEncodedExtPdpAddress() {
        return new byte[] { 60 };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPContextInfoImpl.class);
    	
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PDPContextInfoImpl);
        PDPContextInfoImpl impl = (PDPContextInfoImpl)result.getResult();
        
        assertEquals((int) impl.getPdpContextIdentifier(), 10);
        assertTrue(impl.getPdpContextActive());
        assertEquals(impl.getPdpType().getPDPTypeValue(), PDPTypeValue.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getPdpAddress().getValue(),Unpooled.wrappedBuffer(this.getEncodedPDPAddress())));
        assertEquals(impl.getApnSubscribed().getApn(), new String(this.getEncodedapnSubscribed()));
        assertEquals(impl.getApnInUse().getApn(), new String(this.getEncodedgetapnInUse()));
        assertEquals((int) impl.getNsapi(), 11);
        assertTrue(ByteBufUtil.equals(impl.getTransactionId().getValue(),Unpooled.wrappedBuffer(this.getEncodedTransactionId())));
        assertTrue(ByteBufUtil.equals(impl.getTeidForGnAndGp().getValue(),Unpooled.wrappedBuffer(this.getEncodedTEID_1())));
        assertTrue(ByteBufUtil.equals(impl.getTeidForIu().getValue(), Unpooled.wrappedBuffer(this.getEncodedTEID_2())));
        
        assertEquals(impl.getGgsnAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getGgsnAddress().getGSNAddressData(), Unpooled.wrappedBuffer(this.getEncodedggsnAddress())));
        
        assertEquals(impl.getQosSubscribed().getAllocationRetentionPriority(), 3);
        assertEquals(impl.getQosSubscribed().getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(impl.getQosSubscribed().getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(impl.getQosSubscribed().getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(impl.getQosSubscribed().getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(impl.getQosSubscribed().getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(impl.getQosSubscribed().getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(impl.getQosSubscribed().getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(impl.getQosSubscribed().getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(impl.getQosSubscribed().getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(impl.getQosSubscribed().getTransferDelay().getSourceData(), 0);
        assertEquals(impl.getQosSubscribed().getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(impl.getQosSubscribed().getGuaranteedBitRateForDownlink().getSourceData(), 0);
        
        assertEquals(impl.getQosRequested().getAllocationRetentionPriority(), 15);
        assertEquals(impl.getQosRequested().getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes);
        assertEquals(impl.getQosRequested().getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved);
        assertEquals(impl.getQosRequested().getTrafficClass(), ExtQoSSubscribed_TrafficClass.backgroundClass);
        assertEquals(impl.getQosRequested().getMaximumSduSize().getMaximumSduSize(), 1502);
        assertEquals(impl.getQosRequested().getMaximumBitRateForUplink().getBitRate(), 23);
        assertEquals(impl.getQosRequested().getMaximumBitRateForDownlink().getBitRate(), 576);
        assertEquals(impl.getQosRequested().getResidualBER(), ExtQoSSubscribed_ResidualBER._5_10_minus_2);
        assertEquals(impl.getQosRequested().getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_6);
        assertEquals(impl.getQosRequested().getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1);
        assertEquals(impl.getQosRequested().getTransferDelay().getTransferDelay(), 40);
        assertEquals(impl.getQosRequested().getGuaranteedBitRateForUplink().getBitRate(), 24);
        assertEquals(impl.getQosRequested().getGuaranteedBitRateForDownlink().getBitRate(), 25);
        
        assertEquals(impl.getQosNegotiated().getAllocationRetentionPriority(), 3);
        assertEquals(impl.getQosNegotiated().getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(impl.getQosNegotiated().getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(impl.getQosNegotiated().getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(impl.getQosNegotiated().getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(impl.getQosNegotiated().getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(impl.getQosNegotiated().getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(impl.getQosNegotiated().getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(impl.getQosNegotiated().getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(impl.getQosNegotiated().getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(impl.getQosNegotiated().getTransferDelay().getSourceData(), 0);
        assertEquals(impl.getQosNegotiated().getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(impl.getQosNegotiated().getGuaranteedBitRateForDownlink().getSourceData(), 0);
        
        assertTrue(ByteBufUtil.equals(impl.getChargingId().getValue(), Unpooled.wrappedBuffer(this.getEncodedchargingId())));
        
        assertEquals(impl.getChargingCharacteristics().isNormalCharging(), true);
        assertEquals(impl.getChargingCharacteristics().isPrepaidCharging(), true);
        assertEquals(impl.getChargingCharacteristics().isChargingByHotBillingCharging(), false);
        assertEquals(impl.getChargingCharacteristics().isFlatRateChargingCharging(), false);
        
        assertEquals(impl.getRncAddress().getGSNAddressAddressType(), GSNAddressAddressType.IPv4);
        assertTrue(ByteBufUtil.equals(impl.getRncAddress().getGSNAddressData(),Unpooled.wrappedBuffer(this.getEncodedrncAddress())));
        
        assertNull(impl.getExtensionContainer());
        
        assertEquals(impl.getQos2Subscribed().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(impl.getQos2Subscribed().isOptimisedForSignallingTraffic(),false);
        assertEquals(impl.getQos2Subscribed().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(impl.getQos2Subscribed().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(impl.getQos2Requested().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(impl.getQos2Requested().isOptimisedForSignallingTraffic(),true);
        assertEquals(impl.getQos2Requested().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(impl.getQos2Requested().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(impl.getQos2Negotiated().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.speech);
        assertEquals(impl.getQos2Negotiated().isOptimisedForSignallingTraffic(),true);
        assertEquals(impl.getQos2Negotiated().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(impl.getQos2Negotiated().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(impl.getQos3Subscribed().getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(impl.getQos3Subscribed().getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertEquals(impl.getQos3Requested().getMaximumBitRateForUplinkExtended().getBitRate(), 50000);
        assertEquals(impl.getQos3Requested().getGuaranteedBitRateForUplinkExtended().getBitRate(), 0);
        assertEquals(impl.getQos3Negotiated().getMaximumBitRateForUplinkExtended().getBitRate(), 0);
        assertEquals(impl.getQos3Negotiated().getGuaranteedBitRateForUplinkExtended().getBitRate(), 0);
        
        assertEquals(impl.getQos4Subscribed().getData(), new Integer(91));
        assertEquals(impl.getQos4Requested().getData(), new Integer(92));
        assertEquals(impl.getQos4Negotiated().getData(), new Integer(93));
        assertTrue(ByteBufUtil.equals(impl.getExtPdpType().getValue(),Unpooled.wrappedBuffer(this.getEncodedExtPDPType())));
        assertTrue(ByteBufUtil.equals(impl.getExtPdpAddress().getValue(),Unpooled.wrappedBuffer(this.getEncodedExtPdpAddress())));
    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPContextInfoImpl.class);
    	
        PDPTypeImpl pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(getEncodedPDPAddress()));
        APNImpl apnSubscribed = new APNImpl(new String(getEncodedapnSubscribed()));
        APNImpl apnInUse = new APNImpl(new String(getEncodedgetapnInUse()));
        TransactionIdImpl transactionId = new TransactionIdImpl(Unpooled.wrappedBuffer(getEncodedTransactionId()));
        TEIDImpl teidForGnAndGp = new TEIDImpl(Unpooled.wrappedBuffer(getEncodedTEID_1()));
        TEIDImpl teidForIu = new TEIDImpl(Unpooled.wrappedBuffer(getEncodedTEID_2()));
        GSNAddressImpl ggsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getEncodedggsnAddress()));
        
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1500, false);
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelay(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribedImpl qosSubscribed = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        ExtQoSSubscribedImpl qosNegotiated = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        
        maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1502, false);
        maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(23, false);
        maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(576, false);
        transferDelay = new ExtQoSSubscribed_TransferDelay(40, false);
        guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(24, false);
        guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(25, false);
        ExtQoSSubscribedImpl qosRequested = new ExtQoSSubscribedImpl(15, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreDelivered_Yes,
                ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved, ExtQoSSubscribed_TrafficClass.backgroundClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._5_10_minus_2, ExtQoSSubscribed_SduErrorRatio._1_10_minus_6,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_1, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);
        
        GPRSChargingIDImpl chargingId = new GPRSChargingIDImpl(Unpooled.wrappedBuffer(getEncodedchargingId()));
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(true,true,false,false);
        GSNAddressImpl rncAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(getEncodedrncAddress()));
        
        Ext2QoSSubscribedImpl qos2Subscribed = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        Ext2QoSSubscribedImpl qos2Requested = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        Ext2QoSSubscribedImpl qos2Negotiated = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.speech,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext3QoSSubscribedImpl qos3Subscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
        
        maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(50000, false);
        guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(0, true);
        Ext3QoSSubscribedImpl qos3Requested = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
        
        maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(0, false);
        guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(0, true);
        Ext3QoSSubscribedImpl qos3Negotiated = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
        
        Ext4QoSSubscribedImpl qos4Subscribed = new Ext4QoSSubscribedImpl(91);
        Ext4QoSSubscribedImpl qos4Requested = new Ext4QoSSubscribedImpl(92);
        Ext4QoSSubscribedImpl qos4Negotiated = new Ext4QoSSubscribedImpl(93);
        ExtPDPTypeImpl extPdpType = new ExtPDPTypeImpl(Unpooled.wrappedBuffer(getEncodedExtPDPType()));
        PDPAddressImpl extPdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(getEncodedExtPdpAddress()));

        PDPContextInfoImpl impl = new PDPContextInfoImpl(10, true, pdpType, pdpAddress, apnSubscribed, apnInUse, 11,
                transactionId, teidForGnAndGp, teidForIu, ggsnAddress, qosSubscribed, qosRequested, qosNegotiated, chargingId,
                chargingCharacteristics, rncAddress, null, qos2Subscribed, qos2Requested, qos2Negotiated, qos3Subscribed,
                qos3Requested, qos3Negotiated, qos4Subscribed, qos4Requested, qos4Negotiated, extPdpType, extPdpAddress);
        
        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}