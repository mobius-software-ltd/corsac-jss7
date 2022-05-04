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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed_SourceStatisticsDescriptor;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
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
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext3QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext4QoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtPDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPType;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPTypeValue;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class GPRSSubscriptionDataTest {

	public byte[] getData() {
        return new byte[] { 48, -127, -84, -95, 118, 48, 116, 2, 1, 1, -112, 2, -15, 33, -111, 3, 5, 6, 7, -110, 3, 4, 7, 7, -108, 3, 2, 6, 7, -75, 39, 
        		-96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 
        		32, 33, -128, 9, 3, 115, -106, -2, -2, 116, 3, 0, 0, -127, 2, 6, 0, -126, 3, 0, 0, 0, -125, 2, 74, -6, -124, 1, 2, -123, 9, 48, 12, 17, 
        		17, 119, 22, 62, 34, 12, -122, 2, 6, 5, -121, 3, 4, 6, 5, -120, 1, 0, -119, 1, 2, -94, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 
        		14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -125, 9, 48, 12, 17, 17, 119, 22, 62, 34, 12};
    }

    public byte[] getAPNOIReplacementData() {
        return new byte[] { 48, 12, 17, 17, 119, 22, 62, 34, 12 };
    };

    public byte[] getPDPTypeData() {
        return new byte[] { -15, 33 };
    };

    public byte[] getPDPAddressData() {
        return new byte[] { 5, 6, 7 };
    };

    public byte[] getPDPAddressData2() {
        return new byte[] { 4, 6, 5 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getAPNData() {
        return new byte[] { 6, 7 };
    };

    public byte[] getExtQoSSubscribedData() {
        return new byte[] { 1, 7 };
    };

    public byte[] getExt2QoSSubscribedData() {
        return new byte[] { 1, 8 };
    };

    public byte[] getExt3QoSSubscribedData() {
        return new byte[] { 2, 6 };
    };

    public byte[] getChargingCharacteristicsData() {
        return new byte[] { 6, 0 };
    };

    public byte[] getExtPDPTypeData() {
        return new byte[] { 6, 5 };
    };

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataImpl.class);
    	
        byte[] data = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(data));
        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof GPRSSubscriptionDataImpl);
        GPRSSubscriptionDataImpl prim = (GPRSSubscriptionDataImpl)result.getResult();
        
        assertTrue(!prim.getCompleteDataListIncluded());
        List<PDPContext> gprsDataList = prim.getGPRSDataList();
        assertNotNull(gprsDataList);
        assertEquals(gprsDataList.size(), 1);
        PDPContext pdpContext = gprsDataList.get(0);
        assertNotNull(pdpContext);
        APN apn = pdpContext.getAPN();
        assertEquals(apn.getApn(), new String(this.getAPNData()));
        APNOIReplacement apnoiReplacement = pdpContext.getAPNOIReplacement();
        assertTrue(ByteBufUtil.equals(apnoiReplacement.getValue(), Unpooled.wrappedBuffer(this.getAPNOIReplacementData())));
        
        assertEquals(pdpContext.getChargingCharacteristics().isNormalCharging(), false);
        assertEquals(pdpContext.getChargingCharacteristics().isPrepaidCharging(), true);
        assertEquals(pdpContext.getChargingCharacteristics().isFlatRateChargingCharging(), true);
        assertEquals(pdpContext.getChargingCharacteristics().isChargingByHotBillingCharging(), false);
        
        Ext2QoSSubscribed ext2QoSSubscribed = pdpContext.getExt2QoSSubscribed();
        assertEquals(ext2QoSSubscribed.getSourceStatisticsDescriptor(),Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(ext2QoSSubscribed.isOptimisedForSignallingTraffic(),false);
        assertEquals(ext2QoSSubscribed.getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(ext2QoSSubscribed.getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        
        Ext3QoSSubscribed ext3QoSSubscribed = pdpContext.getExt3QoSSubscribed();
        assertEquals(ext3QoSSubscribed.getMaximumBitRateForUplinkExtended().getBitRate(), 16000);
        assertEquals(ext3QoSSubscribed.getGuaranteedBitRateForUplinkExtended().getBitRate(), 256000);
        assertFalse(ext3QoSSubscribed.getGuaranteedBitRateForUplinkExtended().isUseNonextendedValue());

        Ext4QoSSubscribed ext4QoSSubscribed = pdpContext.getExt4QoSSubscribed();
        assertEquals(ext4QoSSubscribed.getData(), new Integer(2));
        MAPExtensionContainer pdpContextExtensionContainer = pdpContext.getExtensionContainer();
        assertNotNull(pdpContextExtensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(pdpContextExtensionContainer));
        PDPAddress extpdpAddress = pdpContext.getExtPDPAddress();
        assertTrue(ByteBufUtil.equals(extpdpAddress.getValue(), Unpooled.wrappedBuffer(this.getPDPAddressData2())));
        ExtPDPType extpdpType = pdpContext.getExtPDPType();
        assertTrue(ByteBufUtil.equals(extpdpType.getValue(), Unpooled.wrappedBuffer(this.getExtPDPTypeData())));

        ExtQoSSubscribed extQoSSubscribed = pdpContext.getExtQoSSubscribed();
        assertEquals(extQoSSubscribed.getAllocationRetentionPriority(), 3);
        assertEquals(extQoSSubscribed.getDeliveryOfErroneousSdus(), ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No);
        assertEquals(extQoSSubscribed.getDeliveryOrder(), ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo);
        assertEquals(extQoSSubscribed.getTrafficClass(), ExtQoSSubscribed_TrafficClass.interactiveClass);
        assertEquals(extQoSSubscribed.getMaximumSduSize().getMaximumSduSize(), 1500);
        assertEquals(extQoSSubscribed.getMaximumBitRateForUplink().getBitRate(), 8640);
        assertEquals(extQoSSubscribed.getMaximumBitRateForDownlink().getBitRate(), 8640);
        assertEquals(extQoSSubscribed.getResidualBER(), ExtQoSSubscribed_ResidualBER._1_10_minus_5);
        assertEquals(extQoSSubscribed.getSduErrorRatio(), ExtQoSSubscribed_SduErrorRatio._1_10_minus_4);
        assertEquals(extQoSSubscribed.getTrafficHandlingPriority(), ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_3);
        assertEquals(extQoSSubscribed.getTransferDelay().getSourceData(), 0);
        assertEquals(extQoSSubscribed.getGuaranteedBitRateForUplink().getSourceData(), 0);
        assertEquals(extQoSSubscribed.getGuaranteedBitRateForDownlink().getSourceData(), 0);

        assertEquals(pdpContext.getLIPAPermission(), LIPAPermission.lipaConditional);
        PDPAddress pdpAddress = pdpContext.getPDPAddress();
        assertTrue(ByteBufUtil.equals(pdpAddress.getValue(),Unpooled.wrappedBuffer(this.getPDPAddressData())));
        assertEquals(pdpContext.getPDPContextId(), 1);
        PDPType pdpType = pdpContext.getPDPType();
        assertEquals(pdpType.getPDPTypeValue(), PDPTypeValue.IPv4);
        
        QoSSubscribed qosSubscribed = pdpContext.getQoSSubscribed();
        assertEquals(qosSubscribed.getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(qosSubscribed.getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(qosSubscribed.getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(qosSubscribed.getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(qosSubscribed.getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        
        assertEquals(pdpContext.getSIPTOPermission(), SIPTOPermission.siptoAllowed);

        MAPExtensionContainer extensionContainer = prim.getExtensionContainer();
        APNOIReplacement apnOiReplacement = prim.getApnOiReplacement();
        assertNotNull(apnOiReplacement);
        assertTrue(ByteBufUtil.equals(apnOiReplacement.getValue(), Unpooled.wrappedBuffer(this.getAPNOIReplacementData())));
        assertNotNull(extensionContainer);
        assertTrue(MAPExtensionContainerTest.CheckTestExtensionContainer(extensionContainer));
    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser();
    	parser.replaceClass(GPRSSubscriptionDataImpl.class);
    	
        int pdpContextId = 1;
        PDPTypeImpl pdpType = new PDPTypeImpl(PDPTypeValue.IPv4);
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData()));
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        boolean vplmnAddressAllowed = false;
        APNImpl apn = new APNImpl(new String(this.getAPNData()));
        MAPExtensionContainer extensionContainer = MAPExtensionContainerTest.GetTestExtensionContainer();

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
        
        ChargingCharacteristicsImpl chargingCharacteristics = new ChargingCharacteristicsImpl(false,true,true,false);
        
        Ext2QoSSubscribedImpl ext2QoSSubscribed = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        
        ExtQoSSubscribed_BitRateExtended maximumBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(16000, false);
        ExtQoSSubscribed_BitRateExtended guaranteedBitRateForUplinkExtended = new ExtQoSSubscribed_BitRateExtended(256000, false);
        Ext3QoSSubscribedImpl ext3QoSSubscribed = new Ext3QoSSubscribedImpl(maximumBitRateForUplinkExtended, guaranteedBitRateForUplinkExtended);

        Ext4QoSSubscribedImpl ext4QoSSubscribed = new Ext4QoSSubscribedImpl(2);
        APNOIReplacementImpl apnoiReplacement = new APNOIReplacementImpl(Unpooled.wrappedBuffer(this.getAPNOIReplacementData()));
        ExtPDPTypeImpl extpdpType = new ExtPDPTypeImpl(Unpooled.wrappedBuffer(this.getExtPDPTypeData()));
        PDPAddressImpl extpdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(this.getPDPAddressData2()));
        SIPTOPermission sipToPermission = SIPTOPermission.siptoAllowed;
        LIPAPermission lipaPermission = LIPAPermission.lipaConditional;

        PDPContextImpl pdpContext = new PDPContextImpl(pdpContextId, pdpType, pdpAddress, qosSubscribed, vplmnAddressAllowed, apn,
                extensionContainer, extQoSSubscribed, chargingCharacteristics, ext2QoSSubscribed, ext3QoSSubscribed,
                ext4QoSSubscribed, apnoiReplacement, extpdpType, extpdpAddress, sipToPermission, lipaPermission);
        List<PDPContext> gprsDataList = new ArrayList<PDPContext>();
        gprsDataList.add(pdpContext);

        APNOIReplacementImpl apnOiReplacement = new APNOIReplacementImpl(Unpooled.wrappedBuffer(this.getAPNOIReplacementData()));
        GPRSSubscriptionData prim = new GPRSSubscriptionDataImpl(false, gprsDataList, extensionContainer, apnOiReplacement);
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        byte[] rawData=this.getData();
        assertTrue(Arrays.equals(encodedData, rawData));
    }
}
