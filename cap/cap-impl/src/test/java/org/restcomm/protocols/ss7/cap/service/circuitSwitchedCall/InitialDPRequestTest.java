/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionV1Impl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberStateChoice;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.TeleserviceCodeValue;
import org.restcomm.protocols.ss7.commonapp.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtTeleserviceCodeImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CalledPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyCategoryImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CallingPartyNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.CauseIndicatorsImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.GenericNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.LocationNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.OriginalCalledNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectingNumberImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.RedirectionInformationImpl;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.UserServiceInformationImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
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
public class InitialDPRequestTest {

    public byte[] getData1() {
        return new byte[] { 48, 107, (byte) 128, 1, 110, (byte) 130, 8, (byte) 131, (byte) 144, 33, 114, 16, (byte) 144, 0, 0,
                (byte) 131, 3, 3, (byte) 151, 87, (byte) 133, 1, 10, (byte) 140, 6, (byte) 131, 20, 7, 1, 9, 0, (byte) 187, 5,
                (byte) 128, 3, (byte) 128, (byte) 144, (byte) 163, (byte) 156, 1, 2, (byte) 157, 6, (byte) 131, 20, 7, 1, 9, 0,
                (byte) 158, 2, 3, 97, (byte) 159, 50, 8, 6, 7, (byte) 146, 9, 16, 4, (byte) 145, (byte) 249, (byte) 191, 53, 3,
                (byte) 131, 1, 17, (byte) 159, 54, 5, 19, (byte) 250, 61, 61, (byte) 234, (byte) 159, 55, 6, (byte) 145, 34,
                112, 87, 0, 112, (byte) 159, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, (byte) 191, 59, 8, (byte) 129, 6, (byte) 145,
                34, 112, 87, 0, 112 };
    }

    public byte[] getData2() {
        return new byte[] { 48, -127, -89, -128, 1, 110, -126, 8, -125, -112, 33, 114, 16, -112, 0, 0, -125, 3, 3, -105, 87,
                -123, 1, 10, -120, 1, 19, -118, 2, 0, 11, -116, 6, -125, 20, 7, 1, 9, 0, -81, 18, 48, 5, 2, 1, 2, -127, 0, 48,
                9, 2, 1, 3, 10, 1, 1, -127, 1, -1, -105, 2, (byte)0x88, (byte) 0x89, -103, 3, 20, 2, 1, -69, 5, -128, 3, -128, -112, -93, -100, 1, 2,
                -99, 6, -125, 20, 7, 1, 9, 0, -98, 2, 3, 97, -97, 50, 8, 6, 7, -110, 9, 16, 4, -111, -7, -65, 51, 2, -126, 0,
                -65, 52, 3, 2, 1, 111, -65, 53, 3, -125, 1, 17, -97, 54, 5, 19, -6, 61, 61, -22, -97, 55, 6, -111, 34, 112, 87,
                0, 112, -97, 56, 7, -111, 20, -121, 8, 80, 64, -9, -97, 57, 8, 2, 80, 17, 66, 49, 1, 101, 0, -97, 58, 0, -65,
                59, 8, -127, 6, -111, 34, 112, 87, 0, 112 };
    }

    public byte[] getData3() {
        return new byte[] { 48, 48, (byte) 128, 1, 110, (byte) 130, 7, 4, 16, 17, 17, 34, 34, 102, (byte) 135, 1, 2, (byte) 145, 2, (byte) 196, (byte) 186,
                (byte) 191, 32, 4, (byte) 159, 50, 1, 2, (byte) 159, 37, 4, 1, 2, 3, 4, (byte) 159, 45, 2, 0, (byte) 211, (byte) 159, 46, 4, 11, 12, 13, 14,
                (byte) 159, 47, 0 };
    }

    public byte[] getDataCalledPartyNumber() {
        return new byte[] { -125, -112, 33, 114, 16, -112, 0, 0 };
    }

    public byte[] getCallingPartyNumber() {
        return new byte[] { 3, -105, 87 };
    }

    public byte[] getCallingPartysCategory() {
        return new byte[] { 10 };
    }

    public byte[] getOriginalCalledPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getBearerCapability() {
        return new byte[] { -128, -112, -93 };
    }

    public byte[] getRedirectingPartyID() {
        return new byte[] { -125, 20, 7, 1, 9, 0 };
    }

    public byte[] getRedirectionInformation() {
        return new byte[] { 3, 97 };
    }

    public byte[] getCallReferenceNumber() {
        return new byte[] { 19, -6, 61, 61, -22 };
    }

    public byte[] getLocationNumber() {
        return new byte[] { 0, 11 };
    }

    public byte[] getHighLayerCompatibility() {
        return new byte[] { (byte)0x88, (byte) 0x89 };
    }

    public byte[] getAdditionalCallingPartyNumberCap() {
        return new byte[] { 20, 2 , 1};
    }

    public byte[] getCalledPartyBCDNumber() {
        return new byte[] { (byte) 145, 20, (byte) 135, 8, 80, 64, (byte) 247 };
    }

    public byte[] getCarrier() {
        return new byte[] { 1, 2, 3, 4 };
    }

    public byte[] getCUGInterlock() {
        return new byte[] { 11, 12, 13, 14 };
    }

    @Test(groups = { "functional.decode", "circuitSwitchedCall" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPRequestV1Impl.class);
    	
    	byte[] rawData = this.getData1();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPRequestV1Impl);
        
        InitialDPRequestV1Impl elem = (InitialDPRequestV1Impl)result.getResult();        
        assertEquals(elem.getServiceKey(), 110);
        assertTrue(ByteBufUtil.equals(CalledPartyNumberIsupImpl.translate(elem.getCalledPartyNumber().getCalledPartyNumber()),Unpooled.wrappedBuffer(getDataCalledPartyNumber())));
        assertTrue(ByteBufUtil.equals(CallingPartyNumberIsupImpl.translate(elem.getCallingPartyNumber().getCallingPartyNumber()),Unpooled.wrappedBuffer(getCallingPartyNumber())));
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), getCallingPartysCategory()[0]);
        assertTrue(ByteBufUtil.equals(OriginalCalledNumberIsupImpl.translate(elem.getOriginalCalledPartyID().getOriginalCalledNumber()),Unpooled.wrappedBuffer(getOriginalCalledPartyID())));
        assertTrue(ByteBufUtil.equals(BearerIsupImpl.translate(elem.getBearerCapability().getBearerCap().getUserServiceInformation()), Unpooled.wrappedBuffer(getBearerCapability())));
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.collectedInfo);
        assertTrue(ByteBufUtil.equals(RedirectingPartyIDIsupImpl.translate(elem.getRedirectingPartyID().getRedirectingNumber()),Unpooled.wrappedBuffer(getRedirectingPartyID())));
        
        ByteBuf value=Unpooled.buffer();
        ((RedirectionInformationIsupImpl)elem.getRedirectionInformation()).encode(parser,value);
        assertNotNull(value);
        byte[] data = new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getRedirectionInformation()));
        assertTrue(elem.getIMSI().getData().equals("607029900140199"));
        assertEquals(elem.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);
        assertTrue(ByteBufUtil.equals(elem.getCallReferenceNumber().getValue(),Unpooled.wrappedBuffer(getCallReferenceNumber())));
        assertEquals(elem.getMscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getMscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getMscAddress().getAddress().equals("2207750007"));
        assertEquals(elem.getTimeAndTimezone().getYear(), 2005);
        assertEquals(elem.getTimeAndTimezone().getMonth(), 11);
        assertEquals(elem.getTimeAndTimezone().getDay(), 24);
        assertEquals(elem.getTimeAndTimezone().getHour(), 13);
        assertEquals(elem.getTimeAndTimezone().getMinute(), 10);
        assertEquals(elem.getTimeAndTimezone().getSecond(), 56);
        assertEquals(elem.getTimeAndTimezone().getTimeZone(), 0);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getInitialDPArgExtension().getGmscAddress().getAddress().equals("2207750007"));
        assertFalse(elem.getCallForwardingSSPending());
        assertNull(elem.getCGEncountered());
        assertNull(elem.getCause());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getCarrier());
        assertNull(elem.getCugIndex());
        assertNull(elem.getCugInterlock());
        assertFalse(elem.getCugOutgoingAccess());

        rawData = this.getData2();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPRequestV1Impl);
        
        elem = (InitialDPRequestV1Impl)result.getResult();  
        assertEquals(elem.getServiceKey(), 110);
        assertTrue(ByteBufUtil.equals(CalledPartyNumberIsupImpl.translate(elem.getCalledPartyNumber().getCalledPartyNumber()),Unpooled.wrappedBuffer(getDataCalledPartyNumber())));
        assertTrue(ByteBufUtil.equals(CallingPartyNumberIsupImpl.translate(elem.getCallingPartyNumber().getCallingPartyNumber()),Unpooled.wrappedBuffer(getCallingPartyNumber())));
        assertEquals(elem.getCallingPartysCategory().getCallingPartyCategory().getCallingPartyCategory(), getCallingPartysCategory()[0]);
        assertTrue(ByteBufUtil.equals(OriginalCalledNumberIsupImpl.translate(elem.getOriginalCalledPartyID().getOriginalCalledNumber()),Unpooled.wrappedBuffer(getOriginalCalledPartyID())));
        assertTrue(ByteBufUtil.equals(BearerIsupImpl.translate(elem.getBearerCapability().getBearerCap().getUserServiceInformation()),Unpooled.wrappedBuffer(getBearerCapability())));
        assertEquals(elem.getEventTypeBCSM(), EventTypeBCSM.collectedInfo);
        assertTrue(ByteBufUtil.equals(RedirectingPartyIDIsupImpl.translate(elem.getRedirectingPartyID().getRedirectingNumber()),Unpooled.wrappedBuffer(getRedirectingPartyID())));
        
        value=Unpooled.buffer();
        ((RedirectionInformationIsupImpl)elem.getRedirectionInformation()).encode(parser,value);
        data = new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getRedirectionInformation()));
        assertTrue(elem.getIMSI().getData().equals("607029900140199"));
        assertEquals(elem.getExtBasicServiceCode().getExtTeleservice().getTeleserviceCodeValue(), TeleserviceCodeValue.telephony);
        assertTrue(ByteBufUtil.equals(elem.getCallReferenceNumber().getValue(), Unpooled.wrappedBuffer(getCallReferenceNumber())));
        assertEquals(elem.getMscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getMscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getMscAddress().getAddress().equals("2207750007"));
        assertEquals(elem.getTimeAndTimezone().getYear(), 2005);
        assertEquals(elem.getTimeAndTimezone().getMonth(), 11);
        assertEquals(elem.getTimeAndTimezone().getDay(), 24);
        assertEquals(elem.getTimeAndTimezone().getHour(), 13);
        assertEquals(elem.getTimeAndTimezone().getMinute(), 10);
        assertEquals(elem.getTimeAndTimezone().getSecond(), 56);
        assertEquals(elem.getTimeAndTimezone().getTimeZone(), 0);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getAddressNature(), AddressNature.international_number);
        assertEquals(elem.getInitialDPArgExtension().getGmscAddress().getNumberingPlan(), NumberingPlan.ISDN);
        assertTrue(elem.getInitialDPArgExtension().getGmscAddress().getAddress().equals("2207750007"));

        assertTrue(elem.getIPSSPCapabilities().getIPRoutingAddressSupported());
        assertTrue(elem.getIPSSPCapabilities().getVoiceBackSupported());
        assertFalse(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaSpeechRecognition());
        assertFalse(elem.getIPSSPCapabilities().getVoiceInformationSupportedViaVoiceRecognition());
        assertTrue(elem.getIPSSPCapabilities().getGenerationOfVoiceAnnouncementsFromTextSupported());
        assertNull(elem.getIPSSPCapabilities().getExtraData());
        assertTrue(ByteBufUtil.equals(LocationNumberIsupImpl.translate(elem.getLocationNumber().getLocationNumber()),Unpooled.wrappedBuffer(getLocationNumber())));
        assertTrue(CAPExtensionsTest.checkTestCAPExtensions(elem.getExtensions()));
        
        value=Unpooled.buffer();
        ((HighLayerCompatibilityIsupImpl)elem.getHighLayerCompatibility()).encode(parser,value);
        assertNotNull(value);
        data = new byte[value.readableBytes()];
        value.readBytes(data);
        assertTrue(Arrays.equals(data, getHighLayerCompatibility()));
        assertTrue(ByteBufUtil.equals(DigitsIsupImpl.translate(elem.getAdditionalCallingPartyNumber().getGenericNumber()),Unpooled.wrappedBuffer(getAdditionalCallingPartyNumberCap())));
        assertEquals(elem.getSubscriberState().getSubscriberStateChoice(), SubscriberStateChoice.notProvidedFromVLR);
        assertNull(elem.getSubscriberState().getNotReachableReason());
        assertEquals((int) elem.getLocationInformation().getAgeOfLocationInformation(), 111);
        
        ByteBuf buffer=Unpooled.buffer();
        ((CalledPartyBCDNumberImpl)elem.getCalledPartyBCDNumber()).encode(parser, buffer);
        data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        assertTrue(Arrays.equals(data, getCalledPartyBCDNumber()));
        assertTrue(elem.getCallForwardingSSPending());

        assertNull(elem.getCGEncountered());
        assertNull(elem.getCause());
        assertNull(elem.getServiceInteractionIndicatorsTwo());
        assertNull(elem.getCarrier());
        assertNull(elem.getCugIndex());
        assertNull(elem.getCugInterlock());
        assertFalse(elem.getCugOutgoingAccess());

        rawData = this.getData3();
        result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPRequestV1Impl);
        
        elem = (InitialDPRequestV1Impl)result.getResult();        
        assertEquals(elem.getServiceKey(), 110);

        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getAddress(), "1111222266");
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getInternalNetworkNumberIndicator(), 0);
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNatureOfAddressIndicator(), CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        assertEquals(elem.getCalledPartyNumber().getCalledPartyNumber().getNumberingPlanIndicator(), CalledPartyNumber._NPI_ISDN);

        assertNull(elem.getCallingPartyNumber());
        assertNull(elem.getCallingPartysCategory());
        assertNull(elem.getOriginalCalledPartyID());
        assertNull(elem.getBearerCapability());
        assertNull(elem.getEventTypeBCSM());
        assertNull(elem.getRedirectingPartyID());
        assertNull(elem.getRedirectionInformation());
        assertNull(elem.getIMSI());
        assertNull(elem.getExtBasicServiceCode());
        assertNull(elem.getCallReferenceNumber());
        assertNull(elem.getMscAddress());
        assertNull(elem.getTimeAndTimezone());
        assertNull(elem.getInitialDPArgExtension());
        assertFalse(elem.getCallForwardingSSPending());

        assertEquals(elem.getCGEncountered(), CGEncountered.scpOverload);
        assertEquals(elem.getCause().getCauseIndicators().getCauseValue(), CauseIndicators._CV_BEARER_CAPABILITY_NOT_AVAILABLE);
        assertEquals(elem.getCause().getCauseIndicators().getCodingStandard(), CauseIndicators._CODING_STANDARD_NATIONAL);
        assertEquals(elem.getCause().getCauseIndicators().getLocation(), CauseIndicators._LOCATION_PUBLIC_NSRU);
        assertEquals(elem.getCause().getCauseIndicators().getRecommendation(), 0);
        assertEquals(elem.getServiceInteractionIndicatorsTwo().getHoldTreatmentIndicator(), HoldTreatmentIndicator.rejectHoldRequest);
        assertTrue(ByteBufUtil.equals(elem.getCarrier().getValue(),Unpooled.wrappedBuffer(getCarrier())));
        assertEquals(elem.getCugIndex().getData(), 211);
        assertTrue(ByteBufUtil.equals(elem.getCugInterlock().getValue(), Unpooled.wrappedBuffer(getCUGInterlock())));
        assertTrue(elem.getCugOutgoingAccess());
     // CGEncountered
     // Cause
     // serviceInteractionIndicatorsTwo
     // carrier
     // cugIndex
     // cugInterlock
     // cugOutgoingAccess
    }

    @Test(groups = { "functional.encode", "circuitSwitchedCall" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPRequestV1Impl.class);

        CalledPartyNumberIsupImpl calledPartyNumber = new CalledPartyNumberIsupImpl(new CalledPartyNumberImpl(Unpooled.wrappedBuffer(getDataCalledPartyNumber())));
        CallingPartyNumberIsupImpl callingPartyNumber = new CallingPartyNumberIsupImpl(new CallingPartyNumberImpl(Unpooled.wrappedBuffer(getCallingPartyNumber())));
        CallingPartysCategoryIsupImpl callingPartysCategory = new CallingPartysCategoryIsupImpl(new CallingPartyCategoryImpl(getCallingPartysCategory()[0]));
        OriginalCalledNumberIsupImpl originalCalledPartyID = new OriginalCalledNumberIsupImpl(new OriginalCalledNumberImpl(Unpooled.wrappedBuffer(getOriginalCalledPartyID())));
        BearerIsupImpl bearerCap = new BearerIsupImpl(new UserServiceInformationImpl(Unpooled.wrappedBuffer(getBearerCapability())));
        BearerCapabilityImpl bearerCapability = new BearerCapabilityImpl(bearerCap);
        RedirectingPartyIDIsupImpl redirectingPartyID = new RedirectingPartyIDIsupImpl(new RedirectingNumberImpl(Unpooled.wrappedBuffer(getRedirectingPartyID())));
        RedirectionInformationIsupImpl redirectionInformation = new RedirectionInformationIsupImpl(new RedirectionInformationImpl(Unpooled.wrappedBuffer(getRedirectionInformation())));
        IMSIImpl imsi = new IMSIImpl("607029900140199");
        ExtTeleserviceCodeImpl extTeleservice = new ExtTeleserviceCodeImpl(TeleserviceCodeValue.telephony);
        ExtBasicServiceCodeImpl extBasicServiceCode = new ExtBasicServiceCodeImpl(extTeleservice);
        CallReferenceNumberImpl callReferenceNumber = new CallReferenceNumberImpl(Unpooled.wrappedBuffer(getCallReferenceNumber()));
        ISDNAddressStringImpl mscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);
        ISDNAddressStringImpl gmscAddress = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "2207750007");
        InitialDPArgExtensionV1Impl initialDPArgExtension = new InitialDPArgExtensionV1Impl(null, gmscAddress);

        InitialDPRequestV1Impl elem = new InitialDPRequestV1Impl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory,
                null, null, null, originalCalledPartyID, null, null, null, bearerCapability, EventTypeBCSM.collectedInfo,
                redirectingPartyID, redirectionInformation, null, null, null, null, null, false, imsi, null, null,
                extBasicServiceCode, callReferenceNumber, mscAddress, null, timeAndTimezone, false, initialDPArgExtension);
        byte[] rawData = this.getData1();
        ByteBuf buffer=parser.encode(elem);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));

        IPSSPCapabilitiesImpl IPSSPCapabilities = new IPSSPCapabilitiesImpl(true, true, false, false, true, null);
        LocationNumberIsupImpl locationNumber = new LocationNumberIsupImpl(new LocationNumberImpl(Unpooled.wrappedBuffer(getLocationNumber())));
        HighLayerCompatibilityIsupImpl highLayerCompatibility = new HighLayerCompatibilityIsupImpl();
        highLayerCompatibility.decode(parser,null,Unpooled.wrappedBuffer(getHighLayerCompatibility()),false);
        DigitsIsupImpl additionalCallingPartyNumber = new DigitsIsupImpl(new GenericNumberImpl(Unpooled.wrappedBuffer(getAdditionalCallingPartyNumberCap())));
        SubscriberStateImpl subscriberState = new SubscriberStateImpl(SubscriberStateChoice.notProvidedFromVLR, null);
        LocationInformationImpl locationInformation = new LocationInformationImpl(111, null, null, null, null, null, null,
                null, null, false, false, null, null);
        CalledPartyBCDNumberImpl calledPartyBCDNumber = new CalledPartyBCDNumberImpl();
        calledPartyBCDNumber.decode(parser,null,Unpooled.wrappedBuffer(getCalledPartyBCDNumber()),false);

        elem = new InitialDPRequestV1Impl(110, calledPartyNumber, callingPartyNumber, callingPartysCategory, null,
                IPSSPCapabilities, locationNumber, originalCalledPartyID, CAPExtensionsTest.createTestCAPExtensions(),
                highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, EventTypeBCSM.collectedInfo,
                redirectingPartyID, redirectionInformation, null, null, null, null, null, false, imsi, subscriberState,
                locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress, calledPartyBCDNumber,
                timeAndTimezone, true,initialDPArgExtension);
        rawData = this.getData2();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        
        CalledPartyNumber calledPartyNumber2 = new CalledPartyNumberImpl();
        calledPartyNumber2.setAddress("1111222266");
        calledPartyNumber2.setInternalNetworkNumberIndicator(0);
        calledPartyNumber2.setNatureOfAddresIndicator(CalledPartyNumber._NAI_INTERNATIONAL_NUMBER);
        calledPartyNumber2.setNumberingPlanIndicator(CalledPartyNumber._NPI_ISDN);
        CalledPartyNumberIsupImpl calledPartyNumberCap2 = new CalledPartyNumberIsupImpl(calledPartyNumber2);
        CauseIndicators causeIndicators = new CauseIndicatorsImpl();
        causeIndicators.setCauseValue(CauseIndicators._CV_BEARER_CAPABILITY_NOT_AVAILABLE);
        causeIndicators.setCodingStandard(CauseIndicators._CODING_STANDARD_NATIONAL);
        causeIndicators.setLocation(CauseIndicators._LOCATION_PUBLIC_NSRU);
        causeIndicators.setRecommendation(0);
        CauseIsupImpl cause = new CauseIsupImpl(causeIndicators);
        ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo = new ServiceInteractionIndicatorsTwoImpl(null, null, null, null, false,
                HoldTreatmentIndicator.rejectHoldRequest, null, null);
        CarrierImpl carrier = new CarrierImpl(Unpooled.wrappedBuffer(getCarrier()));
        CUGIndexImpl cugIndex = new CUGIndexImpl(211);
        CUGInterlockImpl cugInterlock = new CUGInterlockImpl(Unpooled.wrappedBuffer(getCUGInterlock()));
        elem = new InitialDPRequestV1Impl(110, calledPartyNumberCap2, null, null, CGEncountered.scpOverload, null, null, null, null, null, null, null, null,
                null, null, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, true, null, null, null, null, null, null, null, null,
                false, null);
        rawData = this.getData3();
        buffer=parser.encode(elem);
        encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
        // CGEncountered
        // Cause
        // serviceInteractionIndicatorsTwo
        // carrier
        // cugIndex
        // cugInterlock
        // cugOutgoingAccess

        // int serviceKey, CalledPartyNumberCap calledPartyNumber, CallingPartyNumberCap callingPartyNumber,
        // CallingPartysCategoryInap callingPartysCategory, CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities,
        // LocationNumberCap locationNumber, OriginalCalledNumberCap originalCalledPartyID, CAPExtensions extensions,
        // HighLayerCompatibilityInap highLayerCompatibility, AdditionalCallingPartyNumberCap additionalCallingPartyNumber,
        // BearerCapability bearerCapability,
        // EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCap redirectingPartyID, RedirectionInformationInap
        // redirectionInformation, CauseCap cause,
        // ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex, CUGInterlock
        // cugInterlock,
        // boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState, LocationInformation locationInformation,
        // ExtBasicServiceCode extBasicServiceCode, CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
        // CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
        // InitialDPArgExtension initialDPArgExtension, boolean isCAPVersion3orLater

    }
}
