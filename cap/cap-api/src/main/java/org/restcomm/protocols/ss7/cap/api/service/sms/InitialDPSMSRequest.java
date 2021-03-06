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

package org.restcomm.protocols.ss7.cap.api.service.sms;

import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;

/**
 *
 initialDPSMS {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT InitialDPSMSArg {bound}
   RETURN RESULT FALSE
   ERRORS {missingCustomerRecord | missingParameter | parameterOutOfRange | systemFailure | taskRefused | unexpectedComponentSequence |
     unexpectedDataValue | unexpectedParameter} CODE opcode-initialDPSMS}
 -- Direction: gsmSSF or gprsSSF -> gsmSCF, Timer: Tidpsms
 -- This operation is used after a TDP to indicate request for service.

InitialDPSMSArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  serviceKey                  [0] ServiceKey,
  destinationSubscriberNumber [1] CalledPartyBCDNumber {bound} OPTIONAL,
  callingPartyNumber          [2] SMS-AddressString OPTIONAL,
  eventTypeSMS                [3] EventTypeSMS OPTIONAL,
  iMSI                        [4] IMSI OPTIONAL,
  locationInformationMSC      [5] LocationInformation OPTIONAL,
  locationInformationGPRS     [6] LocationInformationGPRS OPTIONAL,
  sMSCAddress                 [7] ISDN-AddressString OPTIONAL,
  timeAndTimezone             [8] TimeAndTimezone {bound} OPTIONAL,
  tPShortMessageSpecificInfo  [9] TPShortMessageSpecificInfo OPTIONAL,
  tPProtocolIdentifier        [10] TPProtocolIdentifier OPTIONAL,
  tPDataCodingScheme          [11] TPDataCodingScheme OPTIONAL,
  tPValidityPeriod            [12] TPValidityPeriod OPTIONAL,
  extensions                  [13] Extensions {bound} OPTIONAL,
  ...,
  smsReferenceNumber          [14] CallReferenceNumber OPTIONAL,
  mscAddress                  [15] ISDN-AddressString OPTIONAL,
  sgsn-Number                 [16] ISDN-AddressString OPTIONAL,
  ms-Classmark2               [17] MS-Classmark2 OPTIONAL,
  gPRSMSClass                 [18] GPRSMSClass OPTIONAL,
  iMEI                        [19] IMEI OPTIONAL,
  calledPartyNumber           [20] ISDN-AddressString OPTIONAL
}
 *
 * ServiceKey ::= Integer4
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InitialDPSMSRequest extends SmsMessage {

    int getServiceKey();

    CalledPartyBCDNumberImpl getDestinationSubscriberNumber();

    SMSAddressStringImpl getCallingPartyNumber();

    EventTypeSMS getEventTypeSMS();

    IMSIImpl getImsi();

    LocationInformationImpl getLocationInformationMSC();

    LocationInformationGPRSImpl getLocationInformationGPRS();

    ISDNAddressStringImpl getSMSCAddress();

    TimeAndTimezoneImpl getTimeAndTimezone();

    TPShortMessageSpecificInfoImpl getTPShortMessageSpecificInfo();

    TPProtocolIdentifierImpl getTPProtocolIdentifier();

    TPDataCodingSchemeImpl getTPDataCodingScheme();

    TPValidityPeriodImpl getTPValidityPeriod();

    CAPExtensionsImpl getExtensions();

    CallReferenceNumberImpl getSmsReferenceNumber();

    ISDNAddressStringImpl getMscAddress();

    ISDNAddressStringImpl getSgsnNumber();

    MSClassmark2Impl getMSClassmark2();

    GPRSMSClassImpl getGPRSMSClass();

    IMEIImpl getImei();
    ISDNAddressStringImpl getCalledPartyNumber();
}