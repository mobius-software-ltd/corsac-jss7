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
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

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
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_DelayClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_MeanThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PeakThroughput;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_PrecedenceClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed_ReliabilityClass;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.APNOIReplacementImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext3QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.Ext4QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtPDPTypeImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPAddressImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.PDPTypeImpl;
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
public class PDPContextTest {

    private byte[] getEncodedData() {
        return new byte[] { 48, 114, 2, 1, 15, -112, 2, -15, 33, -111, 1, 21, -110, 3, 4, 7, 7, -109, 0, -108, 3, 2, 22, 23, -75, 39, -96, 32, 48, 10, 6, 3, 42,
                3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 9, 3, 115, -106, -2, 
                -2, 116, 3, 0, 0, -127, 2, 12, 0, -126, 3, 0, 0, 0, -125, 2, 74, -6, -124, 1, 91, -123, 9, 81, 92, 83, 84, 85, 86, 87, 88, 89, -122, 2, 58, 59, 
                -121, 1, 60, -120, 1, 1, -119, 1, 1 };
    }

    private byte[] getEncodedPDPType() {
        return new byte[] { -15, 33 };
    }

    private byte[] getEncodedPDPAddress() {
        return new byte[] { 21 };
    }

    private byte[] getEncodedApn() {
        return new byte[] { 22, 23 };
    }

    private byte[] getAPNOIReplacement() {
        return new byte[] { 81, 92, 83, 84, 85, 86, 87, 88, 89 };
    }

    private byte[] getEncodedExtPDPType() {
        return new byte[] { 58, 59 };
    }

    private byte[] getEncodedExtPdpAddress() {
        return new byte[] { 60 };
    }

    @Test(groups = { "functional.decode", "subscriberInformation" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPContextImpl.class);
    	
        byte[] rawData = getEncodedData();

        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PDPContextImpl);
        PDPContextImpl impl = (PDPContextImpl)result.getResult();
        
        assertEquals((int) impl.getPDPContextId(), 15);
        assertTrue(ByteBufUtil.equals(impl.getPDPType().getValue(),Unpooled.wrappedBuffer(this.getEncodedPDPType())));
        assertTrue(ByteBufUtil.equals(impl.getPDPAddress().getValue(), Unpooled.wrappedBuffer(this.getEncodedPDPAddress())));
        assertTrue(impl.isVPLMNAddressAllowed());
        assertEquals(impl.getAPN().getApn(), new String(this.getEncodedApn()));
        
        assertEquals(impl.getQoSSubscribed().getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(impl.getQoSSubscribed().getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(impl.getQoSSubscribed().getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(impl.getQoSSubscribed().getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(impl.getQoSSubscribed().getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        assertEquals(impl.getExtQoSSubscribed().getAllocationRetentionPriority(), 3);
        assertEquals(impl.getExtQoSSubscribed().getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(impl.getExtQoSSubscribed().getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(impl.getExtQoSSubscribed().getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(impl.getExtQoSSubscribed().getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(impl.getExtQoSSubscribed().getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(impl.getExtQoSSubscribed().getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(impl.getExtQoSSubscribed().getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(impl.getExtQoSSubscribed().getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(impl.getExtQoSSubscribed().getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(impl.getExtQoSSubscribed().getTransferDelay().getSourceData(), 0);
        assertEquals(impl.getExtQoSSubscribed().getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(impl.getExtQoSSubscribed().getGuaranteedBitRateForDownlink().getSourceData(), 0);

        assertEquals(impl.getChargingCharacteristics().isNormalCharging(), true);
        assertEquals(impl.getChargingCharacteristics().isPrepaidCharging(), true);
        assertEquals(impl.getChargingCharacteristics().isChargingByHotBillingCharging(), false);
        assertEquals(impl.getChargingCharacteristics().isFlatRateChargingCharging(), false);
        
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(impl.getExtensionContainer()));
        
        assertEquals(impl.getExt2QoSSubscribed().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(impl.getExt2QoSSubscribed().isOptimisedForSignallingTraffic(),false);
        assertEquals(impl.getExt2QoSSubscribed().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(impl.getExt2QoSSubscribed().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(impl.getExt3QoSSubscribed().getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(impl.getExt3QoSSubscribed().getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertFalse(impl.getExt3QoSSubscribed().getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());

        assertEquals(impl.getExt4QoSSubscribed().getData(), new Integer(91));
        assertTrue(ByteBufUtil.equals(impl.getExtPDPType().getValue(), Unpooled.wrappedBuffer(this.getEncodedExtPDPType())));
        assertTrue(ByteBufUtil.equals(impl.getExtPDPAddress().getValue(), Unpooled.wrappedBuffer(this.getEncodedExtPdpAddress())));
        assertTrue(ByteBufUtil.equals(impl.getAPNOIReplacement().getValue(), Unpooled.wrappedBuffer(this.getAPNOIReplacement())));
        assertEquals(impl.getSIPTOPermission(), SIPTOPermission.siptoNotAllowed);
        assertEquals(impl.getLIPAPermission(), LIPAPermission.lipaOnly);

    }

    @Test(groups = { "functional.encode", "subscriberInformation" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(PDPContextImpl.class);
    	
        PDPTypeImpl pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(getEncodedPDPAddress()));
        APNImpl apn = new APNImpl(new String(getEncodedApn()));
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        ExtQoSSubscribed_MaximumSduSize maximumSduSize = new ExtQoSSubscribed_MaximumSduSize(1500, false);
        ExtQoSSubscribed_BitRate maximumBitRateForUplink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_BitRate maximumBitRateForDownlink = new ExtQoSSubscribed_BitRate(8640, false);
        ExtQoSSubscribed_TransferDelay transferDelay = new ExtQoSSubscribed_TransferDelay(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForUplink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribed_BitRate guaranteedBitRateForDownlink = new ExtQoSSubscribed_BitRate(0, true);
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(3, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, maximumSduSize, maximumBitRateForUplink,
                maximumBitRateForDownlink, ExtQoSSubscribed_ResidualBER._1_10_minus_5, ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,
                ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3, transferDelay, guaranteedBitRateForUplink, guaranteedBitRateForDownlink);

        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(true,true,false,false);
        
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);
        
        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(91);
        ExtPDPTypeImpl extPdpType = new ExtPDPTypeImpl(Unpooled.wrappedBuffer(getEncodedExtPDPType()));
        PDPAddressImpl extPdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(getEncodedExtPdpAddress()));
        APNOIReplacementImpl apnoiReplacement = new APNOIReplacementImpl(Unpooled.wrappedBuffer(this.getAPNOIReplacement()));

        PDPContextImpl impl = new PDPContextImpl(15, pdpType, pdpAddress, qosSubscribed,
                true, apn, MAPExtensionContainerTest.GetTestExtensionContainer(), extQoSSubscribed,
                chargingCharacteristics, ext2QoSSubscribed,
                ext3QoSSubscribed, ext4QoSSubscribed, apnoiReplacement,
                extPdpType, extPdpAddress, SIPTOPermission.siptoNotAllowed, LIPAPermission.lipaOnly);

        ByteBuf buffer=parser.encode(impl);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData = getEncodedData();
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}