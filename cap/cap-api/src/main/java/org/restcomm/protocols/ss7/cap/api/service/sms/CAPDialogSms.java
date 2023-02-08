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
 * @author yulianoifa
 *
 */
public interface CAPDialogSms extends CAPDialog {
    Integer addConnectSMSRequest(SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addConnectSMSRequest(int customInvokeTimeout, SMSAddressString callingPartysNumber, CalledPartyBCDNumber destinationSubscriberNumber,
            ISDNAddressString smscAddress, CAPINAPExtensions extensions) throws CAPException;

    Integer addEventReportSMSRequest(EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException;

    Integer addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS, EventSpecificInformationSMS eventSpecificInformationSMS,
            MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) throws CAPException;

    Integer addFurnishChargingInformationSMSRequest(FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Integer addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException;

    Integer addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Integer addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException;

    Integer addReleaseSMSRequest(RPCause rpCause) throws CAPException;

    Integer addReleaseSMSRequest(int customInvokeTimeout, RPCause rpCause) throws CAPException;

    Integer addRequestReportSMSEventRequest(List<SMSEvent> smsEvents, CAPINAPExtensions extensions) throws CAPException;

    Integer addRequestReportSMSEventRequest(int customInvokeTimeout, List<SMSEvent> smsEvents, CAPINAPExtensions extensions) throws CAPException;

    Integer addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws CAPException;

    Integer addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws CAPException;

    Integer addContinueSMSRequest() throws CAPException;

    Integer addContinueSMSRequest(int customInvokeTimeout) throws CAPException;
}
