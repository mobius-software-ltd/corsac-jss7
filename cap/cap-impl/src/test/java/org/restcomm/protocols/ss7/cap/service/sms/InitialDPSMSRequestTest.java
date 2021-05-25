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
package org.restcomm.protocols.ss7.cap.service.sms;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsTest;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeodeticInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GeographicalInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSNetworkCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSRadioAccessCapabilityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentityImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentityImpl;
import org.testng.annotations.Test;

import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class InitialDPSMSRequestTest {

    public byte[] getData() {
        return new byte[] { 48, -127, -44, -128, 1, 2, -127, 7, -111, 20, -121, 8, 80, 64, -9, -126, 9, -111, 33, 67, 101, -121, 25, 50, 84, 118, -125, 1, 3, -124, 5, 17, 17, 33, 34, 34, -91, 3, 2, 1, 111, -90, 57, -96, 7, -127, 5, 82, -16, 16, 17, 92, -127, 6, 11, 12, 13, 14, 15, 16, -126, 8, 31, 32, 33, 34, 35, 36, 37, 38, -125, 4, -111, 86, 52, 18, -124, 3, 91, 92, 93, -122, 0, -121, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -120, 0, -119, 1, 13, -121, 6, -111, 34, 112, 87, 0, 112, -120, 8, 2, 80, 17, 66, 49, 1, 101, 0, -119, 1, 5, -118, 1, 5, -117, 1, 5, -116, 6, 1, 2, 3, 4, 5, 6, -83, 18, 48, 5, 2, 1, 2, -127, 0, 48, 9, 2, 1, 3, 10, 1, 1, -127, 1, -1, -114, 6, 1, 2, 3, 4, 5, 6, -113, 6, -111, 34, 112, 87, 0, -128, -112, 6, -111, 34, 112, 87, 0, -112, -111, 3, 1, 2, 3, -78, 11, -128, 3, 1, 2, 3, -127, 4, 11, 22, 33, 44, -109, 8, 17, 34, 51, 68, 85, 102, 119, -120, -108, 6, -111, 34, 112, 87, 0, 1 };
    };

    private byte[] getGeographicalInformation() {
        return new byte[] { 31, 32, 33, 34, 35, 36, 37, 38 };
    }

    private byte[] getEncodedDataRAIdentity() {
        return new byte[] { 11, 12, 13, 14, 15, 16 };
    }

    private byte[] getEncodedDataLSAIdentity() {
        return new byte[] { 91, 92, 93 };
    }

    private byte[] getGeodeticInformation() {
        return new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    private byte[] getTPValidityPeriod() {
        return new byte[] { 1, 2, 3, 4, 5, 6 };
    }

    private byte[] getCallReferenceNumber() {
        return new byte[] { 1, 2, 3, 4, 5, 6 };
    }

    private byte[] getMSClassmark2() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataNetworkCapability() {
        return new byte[] { 1, 2, 3 };
    }

    private byte[] getEncodedDataRadioAccessCapability() {
        return new byte[] { 11, 22, 33, 44 };
    }

    @Test(groups = { "functional.decode", "primitives" })
    public void testDecode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPSMSRequestImpl.class);
    	
    	byte[] rawData = this.getData();
        ASNDecodeResult result=parser.decode(Unpooled.wrappedBuffer(rawData));

        assertFalse(result.getHadErrors());
        assertTrue(result.getResult() instanceof InitialDPSMSRequestImpl);
        
        InitialDPSMSRequestImpl prim = (InitialDPSMSRequestImpl)result.getResult();        
        int serviceKey = prim.getServiceKey();
        CalledPartyBCDNumberImpl destinationSubscriberNumber = prim.getDestinationSubscriberNumber();
        SMSAddressStringImpl callingPartyNumber = prim.getCallingPartyNumber();
        EventTypeSMS eventTypeSMS = prim.getEventTypeSMS();
        IMSIImpl imsi = prim.getImsi();
        LocationInformationImpl locationInformationMSC = prim.getLocationInformationMSC();
        // LocationInformationGPRS locationInformationGPRS =
        // prim.getLocationInformationGPRS();
        ISDNAddressStringImpl smscCAddress = prim.getSMSCAddress();
        // TimeAndTimezone timeAndTimezone = prim.getTimeAndTimezone();
        TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo = prim.getTPShortMessageSpecificInfo();
        TPProtocolIdentifierImpl tPProtocolIdentifier = prim.getTPProtocolIdentifier();
        TPDataCodingSchemeImpl tPDataCodingScheme = prim.getTPDataCodingScheme();
        TPValidityPeriodImpl tPValidityPeriod = prim.getTPValidityPeriod();
        // CAPExtensions extensions = prim.getExtensions();
        CallReferenceNumberImpl smsReferenceNumber = prim.getSmsReferenceNumber();
        ISDNAddressStringImpl mscAddress = prim.getMscAddress();
        ISDNAddressStringImpl sgsnNumber = prim.getSgsnNumber();
        GPRSMSClassImpl gprsMSClass = prim.getGPRSMSClass();
        ISDNAddressStringImpl calledPartyNumber = prim.getCalledPartyNumber();

        assertEquals(serviceKey, 2);
        assertNotNull(destinationSubscriberNumber);
        assertTrue(destinationSubscriberNumber.getAddress().equals("41788005047"));

        assertNotNull(callingPartyNumber);
        assertTrue(callingPartyNumber.getAddress().equals("1234567891234567"));

        assertEquals(eventTypeSMS, EventTypeSMS.oSmsSubmission);

        // getImsi
        assertTrue(imsi.getData().equals("1111122222"));

        assertNotNull(locationInformationMSC);

        // locationInformationGPRS
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getMCC(), 250);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getMNC(), 1);
        assertEquals(prim.getLocationInformationGPRS().getCellGlobalIdOrServiceAreaIdOrLAI().getLAIFixedLength()
                .getLac(), 4444);
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getRouteingAreaIdentity().getData(),
                this.getEncodedDataRAIdentity()));
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeographicalInformation().getData(),
                this.getGeographicalInformation()));
        assertTrue(prim.getLocationInformationGPRS().getSGSNNumber().getAddress().equals("654321"));
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getLSAIdentity().getData(),
                this.getEncodedDataLSAIdentity()));
        assertTrue(prim.getLocationInformationGPRS().isSaiPresent());
        assertTrue(Arrays.equals(prim.getLocationInformationGPRS().getGeodeticInformation().getData(),
                this.getGeodeticInformation()));
        assertTrue(prim.getLocationInformationGPRS().isCurrentLocationRetrieved());
        assertEquals((int) prim.getLocationInformationGPRS().getAgeOfLocationInformation(), 13);

        assertNotNull(smscCAddress);
        assertTrue(smscCAddress.getAddress().equals("2207750007"));

        // gprsMSClass
        assertTrue(Arrays
                .equals(gprsMSClass.getMSNetworkCapability().getData(), this.getEncodedDataNetworkCapability()));
        assertTrue(Arrays.equals(gprsMSClass.getMSRadioAccessCapability().getData(),
                this.getEncodedDataRadioAccessCapability()));

        // getTimeAndTimezone
        assertEquals(prim.getTimeAndTimezone().getYear(), 2005);
        assertEquals(prim.getTimeAndTimezone().getMonth(), 11);
        assertEquals(prim.getTimeAndTimezone().getDay(), 24);
        assertEquals(prim.getTimeAndTimezone().getHour(), 13);
        assertEquals(prim.getTimeAndTimezone().getMinute(), 10);
        assertEquals(prim.getTimeAndTimezone().getSecond(), 56);
        assertEquals(prim.getTimeAndTimezone().getTimeZone(), 0);

        assertEquals(tPShortMessageSpecificInfo.getData(), 5);

        assertEquals(tPProtocolIdentifier.getData(), 5);

        assertEquals(tPDataCodingScheme.getData(), 5);

        assertTrue(Arrays.equals(tPValidityPeriod.getData(), this.getTPValidityPeriod()));

        // extensions
        CAPExtensionsTest.checkTestCAPExtensions(prim.getExtensions());

        assertTrue(Arrays.equals(smsReferenceNumber.getData(), this.getCallReferenceNumber()));

        assertNotNull(mscAddress);
        assertTrue(mscAddress.getAddress().equals("2207750008"));

        assertNotNull(sgsnNumber);
        assertTrue(sgsnNumber.getAddress().equals("2207750009"));

        // getImei
        assertTrue(prim.getImei().getIMEI().equals("1122334455667788"));

        assertNotNull(calledPartyNumber);
        assertTrue(calledPartyNumber.getAddress().equals("2207750010"));

    }

    @Test(groups = { "functional.encode", "primitives" })
    public void testEncode() throws Exception {
    	ASNParser parser=new ASNParser(true);
    	parser.replaceClass(InitialDPSMSRequestImpl.class);
    	
        int serviceKey = 2;
        CalledPartyBCDNumberImpl destinationSubscriberNumber = new CalledPartyBCDNumberImpl(
                AddressNature.international_number, NumberingPlan.ISDN, "41788005047");
        SMSAddressStringImpl callingPartyNumber = new SMSAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "1234567891234567");
        EventTypeSMS eventTypeSMS = EventTypeSMS.oSmsSubmission;
        IMSIImpl imsi = new IMSIImpl("1111122222");
        LocationInformationImpl locationInformationMSC = new LocationInformationImpl(111, null, null, null, null, null,
                null, null, null, false, false, null, null);

        // locationInformationGPRS
        LAIFixedLengthImpl lai = new LAIFixedLengthImpl(250, 1, 4444);
        CellGlobalIdOrServiceAreaIdOrLAIImpl cgi = new CellGlobalIdOrServiceAreaIdOrLAIImpl(lai);
        RAIdentityImpl ra = new RAIdentityImpl(this.getEncodedDataRAIdentity());
        GeographicalInformationImpl ggi = new GeographicalInformationImpl(this.getGeographicalInformation());
        ISDNAddressStringImpl sgsn = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN,
                "654321");
        LSAIdentityImpl lsa = new LSAIdentityImpl(this.getEncodedDataLSAIdentity());
        GeodeticInformationImpl gdi = new GeodeticInformationImpl(this.getGeodeticInformation());
        LocationInformationGPRSImpl locationInformationGPRS = new LocationInformationGPRSImpl(cgi, ra, ggi, sgsn, lsa,
                null, true, gdi, true, 13);

        ISDNAddressStringImpl smscCAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750007");
        TimeAndTimezoneImpl timeAndTimezone = new TimeAndTimezoneImpl(2005, 11, 24, 13, 10, 56, 0);
        TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo = new TPShortMessageSpecificInfoImpl(5);
        TPProtocolIdentifierImpl tPProtocolIdentifier = new TPProtocolIdentifierImpl(5);
        TPDataCodingSchemeImpl tPDataCodingScheme = new TPDataCodingSchemeImpl(5);
        TPValidityPeriodImpl tPValidityPeriod = new TPValidityPeriodImpl(getTPValidityPeriod());
        CAPExtensionsImpl extensions = CAPExtensionsTest.createTestCAPExtensions();
        CallReferenceNumberImpl smsReferenceNumber = new CallReferenceNumberImpl(getCallReferenceNumber());
        ISDNAddressStringImpl mscAddress = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750008");
        ISDNAddressStringImpl sgsnNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750009");
        MSClassmark2Impl mSClassmark2 = new MSClassmark2Impl(getMSClassmark2());

        // gprsMSClass
        MSNetworkCapabilityImpl nc = new MSNetworkCapabilityImpl(this.getEncodedDataNetworkCapability());
        MSRadioAccessCapabilityImpl rac = new MSRadioAccessCapabilityImpl(this.getEncodedDataRadioAccessCapability());
        GPRSMSClassImpl gprsMSClass = new GPRSMSClassImpl(nc, rac);

        IMEIImpl imei = new IMEIImpl("1122334455667788");
        ISDNAddressStringImpl calledPartyNumber = new ISDNAddressStringImpl(AddressNature.international_number,
                NumberingPlan.ISDN, "2207750010");

        InitialDPSMSRequestImpl prim = new InitialDPSMSRequestImpl(serviceKey, destinationSubscriberNumber,
                callingPartyNumber, eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress,
                timeAndTimezone, tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme,
                tPValidityPeriod, extensions, smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass,
                imei, calledPartyNumber);
        byte[] rawData = this.getData();
        ByteBuf buffer=parser.encode(prim);
        byte[] encodedData = new byte[buffer.readableBytes()];
        buffer.readBytes(encodedData);
        assertTrue(Arrays.equals(rawData, encodedData));
    }
}