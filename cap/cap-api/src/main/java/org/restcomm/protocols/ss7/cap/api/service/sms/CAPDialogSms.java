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

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEventImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
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
 * @author sergey vetyutnev
 *
 */
public interface CAPDialogSms extends CAPDialog {
    Long addConnectSMSRequest(SMSAddressStringImpl callingPartysNumber, CalledPartyBCDNumberImpl destinationSubscriberNumber, ISDNAddressStringImpl smscAddress,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addConnectSMSRequest(int customInvokeTimeout, SMSAddressStringImpl callingPartysNumber, CalledPartyBCDNumberImpl destinationSubscriberNumber,
            ISDNAddressStringImpl smscAddress, CAPExtensionsImpl extensions) throws CAPException;

    Long addEventReportSMSRequest(EventTypeSMS eventTypeSMS, EventSpecificInformationSMSImpl eventSpecificInformationSMS, MiscCallInfo miscCallInfo,
            CAPExtensionsImpl extensions) throws CAPException;

    Long addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS, EventSpecificInformationSMSImpl eventSpecificInformationSMS,
            MiscCallInfo miscCallInfo, CAPExtensionsImpl extensions) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(FCIBCCCAMELSequence1SMSImpl fciBCCCAMELsequence1) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELSequence1SMSImpl fciBCCCAMELsequence1) throws CAPException;

    Long addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumberImpl destinationSubscriberNumber, SMSAddressStringImpl callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSIImpl imsi, LocationInformationImpl locationInformationMSC, LocationInformationGPRSImpl locationInformationGPRS,
            ISDNAddressStringImpl smscCAddress, TimeAndTimezoneImpl timeAndTimezone, TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo,
            TPProtocolIdentifierImpl tPProtocolIdentifier, TPDataCodingSchemeImpl tPDataCodingScheme, TPValidityPeriodImpl tPValidityPeriod, CAPExtensionsImpl extensions,
            CallReferenceNumberImpl smsReferenceNumber, ISDNAddressStringImpl mscAddress, ISDNAddressStringImpl sgsnNumber, MSClassmark2Impl mSClassmark2,
            GPRSMSClassImpl gprsMSClass, IMEIImpl imei, ISDNAddressStringImpl calledPartyNumber) throws CAPException;

    Long addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey, CalledPartyBCDNumberImpl destinationSubscriberNumber, SMSAddressStringImpl callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSIImpl imsi, LocationInformationImpl locationInformationMSC, LocationInformationGPRSImpl locationInformationGPRS,
            ISDNAddressStringImpl smscCAddress, TimeAndTimezoneImpl timeAndTimezone, TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo,
            TPProtocolIdentifierImpl tPProtocolIdentifier, TPDataCodingSchemeImpl tPDataCodingScheme, TPValidityPeriodImpl tPValidityPeriod, CAPExtensionsImpl extensions,
            CallReferenceNumberImpl smsReferenceNumber, ISDNAddressStringImpl mscAddress, ISDNAddressStringImpl sgsnNumber, MSClassmark2Impl mSClassmark2,
            GPRSMSClassImpl gprsMSClass, IMEIImpl imei, ISDNAddressStringImpl calledPartyNumber) throws CAPException;

    Long addReleaseSMSRequest(RPCauseImpl rpCause) throws CAPException;

    Long addReleaseSMSRequest(int customInvokeTimeout, RPCauseImpl rpCause) throws CAPException;

    Long addRequestReportSMSEventRequest(List<SMSEventImpl> smsEvents, CAPExtensionsImpl extensions) throws CAPException;

    Long addRequestReportSMSEventRequest(int customInvokeTimeout, List<SMSEventImpl> smsEvents, CAPExtensionsImpl extensions) throws CAPException;

    Long addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPExtensionsImpl extensions) throws CAPException;

    Long addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensionsImpl extensions) throws CAPException;

    Long addContinueSMSRequest() throws CAPException;

    Long addContinueSMSRequest(int customInvokeTimeout) throws CAPException;
}
