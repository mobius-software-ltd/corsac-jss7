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
package org.restcomm.protocols.ss7.cap.EsiGprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPTypeNumberImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
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
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class PdpContextChangeOfPositionSpecificInformationTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -106, -128, 3, 52, 20, 30, -127, 4, 41, 42, 43, 44, -94, 57, -96, 7, -127, 5, 82, -16,
                16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 16, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52,
                18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 16, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -93, 11, -128,
                1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -92, 48, -96, 5, -128, 3, 4, 7, 7, -95, 11, -127, 9, 1, 0, 0, 0, 0, 0, 0, 0,
                0, -94, 5, -128, 3, 4, 7, 7, -93, 5, -128, 3, 0, 0, 0, -92, 5, -128, 3, 16, 0, 0 , -91, 5, -128, 3, 17, 0, 0, -123, 8, 2, 17, 33, 3, 
                1, 112, -127, 35, -122, 5, 4, 1, 1, 1, 1 };
    };

    private byte[] getAccessPointNameData() {
        return new byte[] { 52, 20, 30 };
    }

    private byte[] getEncodedChargingId() {
        return new byte[] { 41, 42, 43, 44 };
    }

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getGeographicalInformation() {
        return new byte[] { 16, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 16, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    private byte[] getGSNAddressData() {
        return new byte[] { 1, 1, 1, 1 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PdpContextChangeOfPositionSpecificInformationImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof PdpContextChangeOfPositionSpecificInformationImpl);
        
        PdpContextChangeOfPositionSpecificInformationImpl prim = (PdpContextChangeOfPositionSpecificInformationImpl)result.getResult();                        
        assertTrue(ByteBufUtil.equals(prim.getAccessPointName().getValue(), Unpooled.wrappedBuffer(this.getAccessPointNameData())));
        assertTrue(ByteBufUtil.equals(prim.getChargingID().getValue(),Unpooled.wrappedBuffer(this.getEncodedChargingId())));

        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);

        assertEquals(prim.getEndUserAddress().getPDPTypeNumber().getPDPTypeNumberValue(), PDPTypeNumberValue.PPP);
        assertEquals(prim.getEndUserAddress().getPDPTypeOrganization().getPDPTypeOrganizationValue(),
                PDPTypeOrganizationValue.ETSI);
        assertTrue(ByteBufUtil.equals(prim.getEndUserAddress().getPDPAddress().getValue(), Unpooled.wrappedBuffer(this.getPDPAddressData())));

        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),false);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(prim.getQualityOfService().getSubscribedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(prim.getQualityOfService().getSubscribedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),true);
        assertEquals(prim.getQualityOfService().getSubscribedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getQualityOfService().getSubscribedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(prim.getQualityOfService().getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.speech);
        assertEquals(prim.getQualityOfService().getNegotiatedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),true);
        assertEquals(prim.getQualityOfService().getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getQualityOfService().getNegotiatedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        assertEquals(prim.getTimeAndTimezone().getYear(), 2011);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 18);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 32);

        assertTrue(ByteBufUtil.equals(Unpooled.wrappedBuffer(getGSNAddressData()), prim.getGSNAddress().getGSNAddressData()));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(PdpContextChangeOfPositionSpecificInformationImpl.class);
    	
        AccessPointNameImpl accessPointName = new AccessPointNameImpl(Unpooled.wrappedBuffer(this.getAccessPointNameData()));
        GPRSChargingIDImpl chargingID = new GPRSChargingIDImpl(Unpooled.wrappedBuffer(getEncodedChargingId()));

        // locationInformationGPRS
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity()));
        
        ByteBuf geoBuffer=Unpooled.wrappedBuffer(getGeographicalInformation());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(GeographicalInformationImpl.decodeTypeOfShape(geoBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geoBuffer), GeographicalInformationImpl.decodeLongitude(geoBuffer), GeographicalInformationImpl.decodeUncertainty(geoBuffer.readByte() & 0x0FF));
        
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity()));
        
        ByteBuf geodeticBuffer=Unpooled.wrappedBuffer(getGeodeticInformation());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(geodeticBuffer.readByte() & 0x0FF, GeographicalInformationImpl.decodeTypeOfShape(geodeticBuffer.readByte() & 0x0FF), GeographicalInformationImpl.decodeLatitude(geodeticBuffer), GeographicalInformationImpl.decodeLongitude(geodeticBuffer), GeographicalInformationImpl.decodeUncertainty(geodeticBuffer.readByte() & 0x0FF),geodeticBuffer.readByte() & 0x0FF);
        
        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa, null, true,
                gdi, true, 13);

        // endUserAddress
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(getPDPAddressData()));
        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.PPP);
        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl(PDPTypeOrganizationValue.ETSI);
        EndUserAddressImpl endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);

        // qualityOfService
        QoSSubscribedImpl qosSubscribed = new QoSSubscribedImpl(QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData,
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved,QoSSubscribed_PrecedenceClass.reserved,QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved,
        		QoSSubscribed_MeanThroughput._10000_octetH);
        GPRSQoSImpl requestedQoS = new GPRSQoSImpl(qosSubscribed);
        ExtQoSSubscribedImpl extQoSSubscribed = new ExtQoSSubscribedImpl(1,ExtQoSSubscribed_DeliveryOfErroneousSdus.subscribedDeliveryOfErroneousSdus_Reserved,
                ExtQoSSubscribed_DeliveryOrder.subscribeddeliveryOrder_Reserved, ExtQoSSubscribed_TrafficClass.subscribedTrafficClass_Reserved, new ExtQoSSubscribed_MaximumSduSize(0,true),
                new ExtQoSSubscribed_BitRate(0,true), new ExtQoSSubscribed_BitRate(0,true), ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved,
                ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved, ExtQoSSubscribed_TrafficHandlingPriority.subscribedTrafficHandlingPriority_Reserved,
                new ExtQoSSubscribed_TransferDelay(0,true), new ExtQoSSubscribed_BitRate(0,true),new ExtQoSSubscribed_BitRate(0,true));
        GPRSQoSImpl subscribedQoS = new GPRSQoSImpl(extQoSSubscribed);
        GPRSQoSImpl negotiatedQoS = new GPRSQoSImpl(qosSubscribed);
        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        Ext2QoSSubscribedImpl qos2Subscribed2 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl subscribedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed2);
        Ext2QoSSubscribedImpl qos2Subscribed3 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.speech,true,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl negotiatedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed3);
        QualityOfServiceImpl qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS,
                requestedQoSExtension, subscribedQoSExtension, negotiatedQoSExtension);

        // timeAndTimezone
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2011, 12, 30, 10, 7, 18, 32);
        GSNAddressImpl gsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4, Unpooled.wrappedBuffer(getGSNAddressData()));

        PdpContextChangeOfPositionSpecificInformationImpl prim = new PdpContextChangeOfPositionSpecificInformationImpl(
                accessPointName, chargingID, locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone,
                gsnAddress);
        
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
