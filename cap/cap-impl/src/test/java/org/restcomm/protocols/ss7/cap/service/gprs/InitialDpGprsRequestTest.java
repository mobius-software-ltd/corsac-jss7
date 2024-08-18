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
package org.restcomm.protocols.ss7.cap.service.gprs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSQoSImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPAddressImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPTypeNumberImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddressAddressType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
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
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSNetworkCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.LSAIdentityImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.QoSSubscribedImpl;
import org.junit.Test;

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
public class InitialDpGprsRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -62, -128, 1, 2, -127, 1, 2, -126, 4, -111, 34, 50, -12, -125, 5, 17, 17, 33, 34, 34, -124, 8, 2, 80, 17, 66, 49, 
            1, 101, 0, -91, 11, -128, 3, 1, 2, 3, -127, 4, 11, 22, 33, 44, -90, 11, -128, 1, -15, -127, 1, 1, -126, 3, 4, 7, 7, -89, 14, -96, 5, -128, 3, 
            4, 7, 7, -93, 5, -128, 3, 0, 0, 0, -120, 3, 52, 20, 30, -119, 6, 12, 52, 23, 20, 30, 45, -118, 4, 52, 34, 20, 30, -117, 1, 1, -84, 57, -96, 7, 
            -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 16, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 
            92, 93, -122, 0, -121, 10, 1, 16, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -115, 1, 1, -82, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 
            1, 1, -127, 1, -1, -113, 5, 4, 6, 8, 9, 10, -112, 0, -111, 8, 17, 34, 51, 68, 85, 102, 119, -120};
    };

    private byte[] getEncodedDataNetworkCapability() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataRadioAccessCapability() {
        return new byte[] { 11, 22, 33, 44 };
    }

    public byte[] getPDPAddressData() {
        return new byte[] { 4, 7, 7 };
    };

    public byte[] getQoSSubscribedData() {
        return new byte[] { 4, 7, 7 };
    };

    private byte[] getAccessPointNameData() {
        return new byte[] { 52, 20, 30 };
    }

    private byte[] getRAIdentityData() {
        return new byte[] { 12, 52, 23, 20, 30, 45 };
    }

    private byte[] getGPRSChargingIDData() {
        return new byte[] { 52, 34, 20, 30 };
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

    private byte[] getGSNAddressData() {
        return new byte[] { 6, 8, 9, 10 };
    }

    public byte[] getDataLiveTrace() {
        return new byte[] { 0x30, (byte) 0x81, /* (byte) 0xd2 */-44 /* end */, (byte) 0x80, 0x01, 0x17, (byte) 0x81, 0x01,
                0x0c, (byte) 0x82, 0x07, (byte) 0x91, 0x55, 0x43, (byte) 0x99, 0x37, 0x09, 0x52, (byte) 0x83, 0x08, 0x27, 0x34,
                0x04, 0x01, 0x10, (byte) 0x83, 0x06, (byte) 0xf0, (byte) 0x84, 0x08, 0x02, 0x31, 0x10, 0x40, 0x60, 0x13, 0x43,
                (byte) 0x88, (byte) 0xa5, 0x22, (byte) 0x80, 0x02, (byte) 0xe5, (byte) 0xe0, (byte) 0x81, 0x1c, 0x17,
                (byte) 0xb3, 0x42, 0x2b, 0x25, (byte) 0x96, 0x62, 0x40, 0x18, (byte) 0x9a, 0x42, (byte) 0x86, 0x62, 0x40, 0x18,
                (byte) 0xa2, 0x42, (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xba, 0x48, (byte) 0x86, 0x62, 0x40, 0x18, 0x00,
                (byte) 0xa6, 0x0c, (byte) 0x80, 0x01, (byte) 0xf1, (byte) 0x81, 0x01, 0x21, (byte) 0x82, 0x04, (byte) 0xb1,
                (byte) 0xbf, (byte) 0x95, (byte) 0xb3, (byte) 0xa7, 0x27, (byte) 0xa0, 0x0b, (byte) 0x81, 0x09, 0x00, 0x10,
                (byte) 0x96, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xa1, 0x0b, (byte) 0x81, 0x09, 0x02, 0x6b, (byte) 0x96,
                0x40, 0x40, 0x74, 0x06, (byte) 0xff, (byte) 0xff, (byte) 0xa2, 0x0b, (byte) 0x81, 0x09, 0x02, 0x71,
                (byte) 0x96, 0x40, 0x40, 0x74, 0x02, (byte) 0xff, (byte) 0xff, (byte) 0x88, 0x1b, 0x04, 0x63, 0x74, 0x62, 0x63,
                0x02, 0x62, 0x72, 0x06, 0x4d, 0x4e, 0x43, 0x30, 0x33, 0x34, 0x06, 0x4d, 0x43, 0x43, 0x37, 0x32, 0x34, 0x04,
                0x47, 0x50, 0x52, 0x53, (byte) 0x89, 0x06, 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04, (byte) 0x8a, 0x04,
                0x14, (byte) 0xf8, 0x55, (byte) 0xd1, (byte) 0x8b, 0x01, 0x00, (byte) 0xac, /* 0x1a, */28, -96, 9,/* end */
                (byte) 0x80, 0x07, 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x15, (byte) 0xa0, (byte) 0x81, 0x06, 0x27,
                (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04, (byte) 0x83, 0x07, (byte) 0x91, 0x55, 0x43, 0x69, 0x26,
                (byte) 0x99, 0x59, (byte) 0x8d, 0x01, 0x00, (byte) 0x8f, 0x05, 0x04, (byte) 0xc9, 0x30, (byte) 0xe2, 0x1b };
    }

    @Test
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDpGprsRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDpGprsRequestImpl);
        
        InitialDpGprsRequestImpl prim = (InitialDpGprsRequestImpl)result.getResult();        
        assertEquals(prim.getServiceKey(), 2);
        assertEquals(prim.getGPRSEventType(), GPRSEventType.attachChangeOfPosition);

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("22234"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // getImsi
        assertTrue(prim.getImsi().getData().equals("1111122222"));

        // getTimeAndTimezone
        assertEquals(prim.getTimeAndTimezone().getYear(), 2005);
        assertEquals(prim.getTimeAndTimezone().getMonth(), 11);
        assertEquals(prim.getTimeAndTimezone().getDay(), 24);
        assertEquals(prim.getTimeAndTimezone().getHour(), 13);
        assertEquals(prim.getTimeAndTimezone().getMinute(), 10);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 56);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 0);

        // gprsMSClass
        GPRSMSClass gprsMSClass = prim.getGPRSMSClass();
        assertTrue(ByteBufUtil.equals(gprsMSClass.getMSNetworkCapability().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataNetworkCapability())));
        assertTrue(ByteBufUtil
                .equals(gprsMSClass.getMSRadioAccessCapability().getValue(),Unpooled.wrappedBuffer(this.getEncodedDataRadioAccessCapability())));

        // endUserAddress
        assertEquals(prim.getEndUserAddress().getPDPTypeNumber().getPDPTypeNumberValue(), PDPTypeNumberValue.PPP);
        assertEquals(prim.getEndUserAddress().getPDPTypeOrganization().getPDPTypeOrganizationValue(),
                PDPTypeOrganizationValue.ETSI);
        assertTrue(ByteBufUtil.equals(prim.getEndUserAddress().getPDPAddress().getValue(), Unpooled.wrappedBuffer(this.getPDPAddressData())));

        // qualityOfService
        assertEquals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getReliabilityClass(),
        		QoSSubscribed_ReliabilityClass.unacknowledgedGtpLlcAndRlc_ProtectedData);
        assertEquals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getDelayClass(),
        		QoSSubscribed_DelayClass.subscribedDelayClass_Reserved);
        assertEquals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getPrecedenceClass(),
        		QoSSubscribed_PrecedenceClass.reserved);
        assertEquals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getPeakThroughput(),
        		QoSSubscribed_PeakThroughput.subscribedPeakThroughput_Reserved);
        assertEquals(prim.getQualityOfService().getRequestedQoS().getShortQoSFormat().getMeanThroughput(),
        		QoSSubscribed_MeanThroughput._10000_octetH);
        
        assertNull(prim.getQualityOfService().getRequestedQoS().getLongQoSFormat());
        
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getSourceStatisticsDescriptor(),
        		Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().isOptimisedForSignallingTraffic(),false);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getGuaranteedBitRateForDownlinkExtended().getBitRate(),0);
        assertEquals(prim.getQualityOfService().getRequestedQoSExtension().getSupplementToLongQoSFormat().getMaximumBitRateForDownlinkExtended().getBitRate(),0);
        
        // getAccessPointName
        assertTrue(ByteBufUtil.equals(prim.getAccessPointName().getValue(), Unpooled.wrappedBuffer(this.getAccessPointNameData())));

        // routeingAreaIdentity
        assertTrue(ByteBufUtil.equals(prim.getRouteingAreaIdentity().getValue(), Unpooled.wrappedBuffer(this.getRAIdentityData())));

        // chargingID
        assertTrue(ByteBufUtil.equals(prim.getChargingID().getValue(),Unpooled.wrappedBuffer(this.getGPRSChargingIDData())));

        // sgsnCapabilities
        assertEquals(prim.getSGSNCapabilities().getData(), 1);

        // locationInformationGPRS
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength().getLac(), 4444);
        assertTrue(ByteBufUtil.equals(prim.getLocationInformationGPRS().getRouteingAreaIdentity().getValue(),
        		Unpooled.wrappedBuffer(this.getEncodedDataRAIdentity())));
        assertTrue(ByteBufUtil.equals(prim.getLocationInformationGPRS().getGeographicalInformation().getValue(),
                Unpooled.wrappedBuffer(this.getGeographicalInformation())));
        assertTrue(prim.getLocationInformationGPRS().getSGSNNumber().getAddress().equals("654321"));
        assertTrue(ByteBufUtil
                .equals(prim.getLocationInformationGPRS().getLSAIdentity().getValue(), Unpooled.wrappedBuffer(this.getEncodedDataLSAIdentity())));
        assertTrue(prim.getLocationInformationGPRS().isSaiPresent());
        assertTrue(ByteBufUtil.equals(prim.getLocationInformationGPRS().getGeodeticInformation().getValue(),
                Unpooled.wrappedBuffer(this.getGeodeticInformation())));
        assertTrue(prim.getLocationInformationGPRS().isCurrentLocationRetrieved());
        assertEquals((int) prim.getLocationInformationGPRS().getAgeOfLocationInformation(), 13);

        // PDPInitiationType
        assertEquals(prim.getPDPInitiationType(), PDPInitiationType.networkInitiated);

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

        // gsnAddress
        assertTrue(ByteBufUtil.equals(prim.getGSNAddress().getGSNAddressData(), Unpooled.wrappedBuffer(this.getGSNAddressData())));

        // getSecondaryPDPContext
        assertTrue(prim.getSecondaryPDPContext());

        // getImei
        assertTrue(prim.getImei().getIMEI().equals("1122334455667788"));
    }

    @Test
    public void testDecodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDpGprsRequestImpl.class);
    	
    	byte[] rawData = this.getDataLiveTrace();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDpGprsRequestImpl);
        
        InitialDpGprsRequestImpl prim = (InitialDpGprsRequestImpl)result.getResult();        
        assertEquals(prim.getServiceKey(), 23);
        assertEquals(prim.getGPRSEventType(), GPRSEventType.pdpContextEstablishmentAcknowledgement);

        // msisdn
        ISDNAddressString msisdn = prim.getMsisdn();
        assertTrue(msisdn.getAddress().equals("553499739025"));
        assertEquals(msisdn.getAddressNature(), AddressNature.international_number);
        assertEquals(msisdn.getNumberingPlan(), NumberingPlan.ISDN);

        // getImsi
        assertTrue(prim.getImsi().getData().equals("724340100138600"));

        // sgsnCapabilities
        assertEquals(prim.getSGSNCapabilities().getData(), 0);

        // locationInformationGPRS
        assertNull(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength());
        assertNotNull(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI()
                .getCellGlobalIdOrServiceAreaIdFixedLength());

        // PDPInitiationType
        assertEquals(prim.getPDPInitiationType(), PDPInitiationType.mSInitiated);

        // getSecondaryPDPContext
        assertTrue(!prim.getSecondaryPDPContext());

    }

    @Test
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDpGprsRequestImpl.class);
    	
        int serviceKey = 2;
        GPRSEventType gprsEventType = GPRSEventType.attachChangeOfPosition;
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22234");
        IMSIImpl imsi = new IMSIImpl("1111122222");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(Unpooled.wrappedBuffer(this.getEncodedDataNetworkCapability()));
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(Unpooled.wrappedBuffer(this.getEncodedDataRadioAccessCapability()));
        GPRSMSClassImpl gprsMSClass = new GPRSMSClassImpl(nc, rac);

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
        Ext2QoSSubscribedImpl qos2Subscribed1 = new Ext2QoSSubscribedImpl(Ext2QoSSubscribed_SourceStatisticsDescriptor.unknown,false,new ExtQoSSubscribed_BitRateExtended(0, true), new ExtQoSSubscribed_BitRateExtended(0, true));
        GPRSQoSExtensionImpl requestedQoSExtension = new GPRSQoSExtensionImpl(qos2Subscribed1);
        QualityOfServiceImpl qualityOfService = new QualityOfServiceImpl(requestedQoS, null, null, requestedQoSExtension, null,
                null);

        // accessPointName
        AccessPointNameImpl accessPointName = new AccessPointNameImpl(Unpooled.wrappedBuffer(this.getAccessPointNameData()));

        // routeingAreaIdentity
        RAIdentityImpl routeingAreaIdentity = new RAIdentityImpl(Unpooled.wrappedBuffer(this.getRAIdentityData()));

        GPRSChargingIDImpl chargingID = new GPRSChargingIDImpl(Unpooled.wrappedBuffer(this.getGPRSChargingIDData()));

        // sgsnCapabilities
        SGSNCapabilitiesImpl sgsnCapabilities = new SGSNCapabilitiesImpl(1);

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

        // pdpInitiationType
        PDPInitiationType pdpInitiationType = PDPInitiationType.networkInitiated;

        // extensions
        CAPINAPExtensions extensions = CAPExtensionsTest.createTestCAPExtensions();

        // GSNAddress
        GSNAddressImpl gsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(this.getGSNAddressData()));

        // secondaryPDPContext
        boolean secondaryPDPContext = true;

        // imei
        IMEIImpl imei = new IMEIImpl("1122334455667788");

        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);

        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

    @Test
    public void testEncodeLiveTrace() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDpGprsRequestImpl.class);
    	
        int serviceKey = 23;
        GPRSEventType gprsEventType = GPRSEventType.pdpContextEstablishmentAcknowledgement;
        ISDNAddressStringImpl msisdn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553499739025");
        IMSIImpl imsi = new IMSIImpl("724340100138600");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2013, 1, 4, 6, 31, 34,-8);                

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(Unpooled.wrappedBuffer(new byte[] { (byte) 0xe5, (byte) 0xe0 }));
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(Unpooled.wrappedBuffer(new byte[] { 0x17, (byte) 0xb3, 0x42, 0x2b, 0x25,
                (byte) 0x96, 0x62, 0x40, 0x18, (byte) 0x9a, 0x42, (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xa2, 0x42,
                (byte) 0x86, 0x62, 0x40, 0x18, (byte) 0xba, 0x48, (byte) 0x86, 0x62, 0x40, 0x18, 0x00 }));

        GPRSMSClassImpl gprsMSClass = new GPRSMSClassImpl(nc, rac);

        // endUserAddress
        PDPAddressImpl pdpAddress = new PDPAddressImpl(Unpooled.wrappedBuffer(new byte[] { (byte) 0xb1, (byte) 0xbf, (byte) 0x95, (byte) 0xb3 }));

        PDPTypeNumberImpl pdpTypeNumber = new PDPTypeNumberImpl(PDPTypeNumberValue.IPV4);

        PDPTypeOrganizationImpl pdpTypeOrganization = new PDPTypeOrganizationImpl((byte) 0xf1);
        EndUserAddressImpl endUserAddress = new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);

        // qualityOfService
        ExtQoSSubscribedImpl longQos1 = new ExtQoSSubscribedImpl(0x00, ExtQoSSubscribed_DeliveryOfErroneousSdus.subscribedDeliveryOfErroneousSdus_Reserved,
                ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.subscribedTrafficClass_Reserved, new ExtQoSSubscribed_MaximumSduSize(0x96, true),
                new ExtQoSSubscribed_BitRate(0,true), new ExtQoSSubscribed_BitRate(0, true), ExtQoSSubscribed_ResidualBER.subscribedResidualBER_Reserved,
                ExtQoSSubscribed_SduErrorRatio.subscribedSduErrorRatio_Reserved, ExtQoSSubscribed_TrafficHandlingPriority.subscribedTrafficHandlingPriority_Reserved,
                new ExtQoSSubscribed_TransferDelay(0,true), new ExtQoSSubscribed_BitRate(0,true),new ExtQoSSubscribed_BitRate(0,true));
        		
        GPRSQoSImpl requestedQoS = new GPRSQoSImpl(longQos1);

        ExtQoSSubscribedImpl longQos2 = new ExtQoSSubscribedImpl(0x02, ExtQoSSubscribed_DeliveryOfErroneousSdus.erroneousSdusAreNotDelivered_No,
        		ExtQoSSubscribed_DeliveryOrder.withdeliveryOrderYes, ExtQoSSubscribed_TrafficClass.interactiveClass, new ExtQoSSubscribed_MaximumSduSize(0x96, true),
        		new ExtQoSSubscribed_BitRate(0x40,true), new ExtQoSSubscribed_BitRate(0x40, true),ExtQoSSubscribed_ResidualBER._1_10_minus_5,
        		ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_2,
        		new ExtQoSSubscribed_TransferDelay(1,true), new ExtQoSSubscribed_BitRate(0xff,true),new ExtQoSSubscribed_BitRate(0xff,true));
        		
        		//new byte[] { 0x06, (byte) 0xff, (byte) 0xff });
        GPRSQoSImpl subscribedQoS = new GPRSQoSImpl(longQos2);

        ExtQoSSubscribedImpl longQos3 = new ExtQoSSubscribedImpl(0x02, ExtQoSSubscribed_DeliveryOfErroneousSdus.noDetect,
        		ExtQoSSubscribed_DeliveryOrder.withoutDeliveryOrderNo, ExtQoSSubscribed_TrafficClass.interactiveClass, new ExtQoSSubscribed_MaximumSduSize(0x96, true),
        		new ExtQoSSubscribed_BitRate(0x40,true), new ExtQoSSubscribed_BitRate(0x40, true),ExtQoSSubscribed_ResidualBER._1_10_minus_5,
        		ExtQoSSubscribed_SduErrorRatio._1_10_minus_4,ExtQoSSubscribed_TrafficHandlingPriority.priorityLevel_2,
        		new ExtQoSSubscribed_TransferDelay(0,true), new ExtQoSSubscribed_BitRate(0xff,true),new ExtQoSSubscribed_BitRate(0xff,true));
        		
        GPRSQoSImpl negotiatedQoS = new GPRSQoSImpl(longQos3);

        QualityOfServiceImpl qualityOfService = new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, null, null,
                null);

        // accessPointName
        AccessPointNameImpl accessPointName = new AccessPointNameImpl(Unpooled.wrappedBuffer(new byte[] { 0x04, 0x63, 0x74, 0x62, 0x63, 0x02, 0x62, 0x72,
                0x06, 0x4d, 0x4e, 0x43, 0x30, 0x33, 0x34, 0x06, 0x4d, 0x43, 0x43, 0x37, 0x32, 0x34, 0x04, 0x47, 0x50, 0x52,
                0x53 }));

        // routeingAreaIdentity
        RAIdentityImpl routeingAreaIdentity = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04 }));

        GPRSChargingIDImpl chargingID = new GPRSChargingIDImpl(Unpooled.wrappedBuffer(new byte[] { 0x14, (byte) 0xf8, 0x55, (byte) 0xd1 }));

        // sgsnCapabilities
        SGSNCapabilitiesImpl sgsnCapabilities = new SGSNCapabilitiesImpl(0x00);

        CellGlobalIdOrServiceAreaIdFixedLengthImpl cellGlobalIdOrServiceAreaIdFixedLength = new CellGlobalIdOrServiceAreaIdFixedLengthImpl(
                724, 34, 0x816e, 0x15a0);

        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(
                cellGlobalIdOrServiceAreaIdFixedLength);

        RAIdentityImpl ra = new RAIdentityImpl(Unpooled.wrappedBuffer(new byte[] { 0x27, (byte) 0xf4, 0x43, (byte) 0x81, 0x6e, 0x04 }));

        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "553496629995");

        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, null, sgsn, null, null,
                false, null, false, null);

        // pdpInitiationType
        PDPInitiationType pdpInitiationType = PDPInitiationType.mSInitiated;

        // GSNAddress
        GSNAddressImpl gsnAddress = new GSNAddressImpl(GSNAddressAddressType.IPv4,Unpooled.wrappedBuffer(new byte[] { (byte) 0xc9, 0x30, (byte) 0xe2, 0x1b }));

        // secondaryPDPContext
        boolean secondaryPDPContext = false;

        InitialDpGprsRequestImpl prim = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, null, gsnAddress, secondaryPDPContext, null);

        byte[] rawData = this.getDataLiveTrace();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }

}
