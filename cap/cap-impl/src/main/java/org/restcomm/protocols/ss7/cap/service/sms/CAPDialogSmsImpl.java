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

package org.restcomm.protocols.ss7.cap.service.sms;

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
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
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPDialogSmsImpl extends CAPDialogImpl implements CAPDialogSms {
	private static final long serialVersionUID = 1L;

	protected CAPDialogSmsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Integer addConnectSMSRequest(SMSAddressString callingPartysNumber,
            CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress, CAPINAPExtensions extensions)
            throws CAPException {
        return addConnectSMSRequest(_Timer_Default, callingPartysNumber, destinationSubscriberNumber, smscAddress,
                extensions);

    }

    @Override
    public Integer addConnectSMSRequest(int customInvokeTimeout, SMSAddressString callingPartysNumber,
            CalledPartyBCDNumber destinationSubscriberNumber, ISDNAddressString smscAddress, CAPINAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ConnectSMSRequestImpl req = new ConnectSMSRequestImpl(callingPartysNumber, destinationSubscriberNumber,
                smscAddress, extensions);

        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.connectSMS, req, true, false);        
    }

    @Override
    public Integer addEventReportSMSRequest(EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPINAPExtensions extensions)
            throws CAPException {
        return this.addEventReportSMSRequest(_Timer_Default, eventTypeSMS, eventSpecificInformationSMS, miscCallInfo,
                extensions);
    }

    @Override
    public Integer addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPINAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EventReportSMSRequestImpl req = new EventReportSMSRequestImpl(eventTypeSMS, eventSpecificInformationSMS,
                miscCallInfo, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.eventReportSMS, req, true, false);        
    }

    @Override
    public Integer addFurnishChargingInformationSMSRequest(FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException {
        return this.addFurnishChargingInformationSMSRequest(_Timer_Default, fciBCCCAMELsequence1);
    }

    @Override
    public Integer addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        FurnishChargingInformationSMSRequestImpl req = new FurnishChargingInformationSMSRequestImpl(fciBCCCAMELsequence1);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.furnishChargingInformationSMS, req, true, false);        
    }

    @Override
    public Integer addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumber destinationSubscriberNumber,
            SMSAddressString callingPartyNumber, EventTypeSMS eventTypeSMS, IMSI imsi,
            LocationInformation locationInformationMSC, LocationInformationGPRS locationInformationGPRS,
            ISDNAddressString smscCAddress, TimeAndTimezone timeAndTimezone,
            TPShortMessageSpecificInfo tPShortMessageSpecificInfo, TPProtocolIdentifier tPProtocolIdentifier,
            TPDataCodingScheme tPDataCodingScheme, TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions,
            CallReferenceNumber smsReferenceNumber, ISDNAddressString mscAddress, ISDNAddressString sgsnNumber,
            MSClassmark2 mSClassmark2, GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber)
            throws CAPException {
        return this.addInitialDPSMSRequest(_Timer_Default, serviceKey, destinationSubscriberNumber, callingPartyNumber,
                eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress, timeAndTimezone,
                tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme, tPValidityPeriod, extensions,
                smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass, imei, calledPartyNumber);
    }

    @Override
    public Integer addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey,
            CalledPartyBCDNumber destinationSubscriberNumber, SMSAddressString callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSI imsi, LocationInformation locationInformationMSC,
            LocationInformationGPRS locationInformationGPRS, ISDNAddressString smscCAddress,
            TimeAndTimezone timeAndTimezone, TPShortMessageSpecificInfo tPShortMessageSpecificInfo,
            TPProtocolIdentifier tPProtocolIdentifier, TPDataCodingScheme tPDataCodingScheme,
            TPValidityPeriod tPValidityPeriod, CAPINAPExtensions extensions, CallReferenceNumber smsReferenceNumber,
            ISDNAddressString mscAddress, ISDNAddressString sgsnNumber, MSClassmark2 mSClassmark2,
            GPRSMSClass gprsMSClass, IMEI imei, ISDNAddressString calledPartyNumber) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        InitialDPSMSRequestImpl req = new InitialDPSMSRequestImpl(serviceKey, destinationSubscriberNumber,
                callingPartyNumber, eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress,
                timeAndTimezone, tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme,
                tPValidityPeriod, extensions, smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass,
                imei, calledPartyNumber);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.initialDPSMS, req, true, false);
    }

    @Override
    public Integer addReleaseSMSRequest(RPCause rpCause) throws CAPException {
        return this.addReleaseSMSRequest(_Timer_Default, rpCause);
    }

    @Override
    public Integer addReleaseSMSRequest(int customInvokeTimeout, RPCause rpCause) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ReleaseSMSRequestImpl req = new ReleaseSMSRequestImpl(rpCause);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.releaseSMS, req, true, false);        
    }

    @Override
    public Integer addRequestReportSMSEventRequest(List<SMSEvent> smsEvents, CAPINAPExtensions extensions)
            throws CAPException {
        return this.addRequestReportSMSEventRequest(_Timer_Default, smsEvents, extensions);
    }

    @Override
    public Integer addRequestReportSMSEventRequest(int customInvokeTimeout, List<SMSEvent> smsEvents,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        RequestReportSMSEventRequestImpl req = new RequestReportSMSEventRequestImpl(smsEvents, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.requestReportSMSEvent, req, true, false);        
    }

    @Override
    public Integer addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws CAPException {
        return this.addResetTimerSMSRequest(_Timer_Default, timerID, timerValue, extensions);
    }

    @Override
    public Integer addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ResetTimerSMSRequestImpl req = new ResetTimerSMSRequestImpl(timerID, timerValue, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.resetTimerSMS, req, true, false);        
    }

    @Override
    public Integer addContinueSMSRequest() throws CAPException {

        return addContinueSMSRequest(_Timer_Default);
    }

    @Override
    public Integer addContinueSMSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.continueSMS_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.continueSMS, null, true, false);        
    }

}
