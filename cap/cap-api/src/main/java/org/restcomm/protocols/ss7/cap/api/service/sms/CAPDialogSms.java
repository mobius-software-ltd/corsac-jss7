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
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPDialogSms extends CAPDialog {
    Long addConnectSMSRequest(SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress,
            CAPINAPExtensions extensions) throws CAPException;

    Long addConnectSMSRequest(int customInvokeTimeout, SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber,
            ISDNAddressString smscAddress, CAPINAPExtensions extensions) throws CAPException;

    Long addEventReportSMSRequest(EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException;

    Long addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS,
            MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Long addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Long addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Long addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Long addReleaseSMSRequest(RPCause rpCause) throws CAPException;

    Long addReleaseSMSRequest(int customInvokeTimeout, RPCause rpCause) throws CAPException;

    Long addRequestReportSMSEventRequest(List<SMSEvent> smsEvents, CAPINAPExtensions extensions) throws CAPException;

    Long addRequestReportSMSEventRequest(int customInvokeTimeout, List<SMSEvent> smsEvents, CAPINAPExtensions extensions) throws CAPException;

    Long addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws CAPException;

    Long addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws CAPException;

    Long addContinueSMSRequest() throws CAPException;

    Long addContinueSMSRequest(int customInvokeTimeout) throws CAPException;
}
