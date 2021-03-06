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

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
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
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSMSClassImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPDialogSmsImpl extends CAPDialogImpl implements CAPDialogSms {
	private static final long serialVersionUID = 1L;

	protected CAPDialogSmsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addConnectSMSRequest(SMSAddressStringImpl callingPartysNumber,
            CalledPartyBCDNumberImpl destinationSubscriberNumber, ISDNAddressStringImpl smscAddress, CAPExtensionsImpl extensions)
            throws CAPException {
        return addConnectSMSRequest(_Timer_Default, callingPartysNumber, destinationSubscriberNumber, smscAddress,
                extensions);

    }

    @Override
    public Long addConnectSMSRequest(int customInvokeTimeout, SMSAddressStringImpl callingPartysNumber,
            CalledPartyBCDNumberImpl destinationSubscriberNumber, ISDNAddressStringImpl smscAddress, CAPExtensionsImpl extensions)
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

        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.connectSMS, req, true, false);        
    }

    @Override
    public Long addEventReportSMSRequest(EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMSImpl eventSpecificInformationSMS, MiscCallInfoImpl miscCallInfo, CAPExtensionsImpl extensions)
            throws CAPException {
        return this.addEventReportSMSRequest(_Timer_Default, eventTypeSMS, eventSpecificInformationSMS, miscCallInfo,
                extensions);
    }

    @Override
    public Long addEventReportSMSRequest(int customInvokeTimeout, EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMSImpl eventSpecificInformationSMS, MiscCallInfoImpl miscCallInfo, CAPExtensionsImpl extensions)
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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.eventReportSMS, req, true, false);        
    }

    @Override
    public Long addFurnishChargingInformationSMSRequest(FCIBCCCAMELSequence1SMSImpl fciBCCCAMELsequence1) throws CAPException {
        return this.addFurnishChargingInformationSMSRequest(_Timer_Default, fciBCCCAMELsequence1);
    }

    @Override
    public Long addFurnishChargingInformationSMSRequest(int customInvokeTimeout, FCIBCCCAMELSequence1SMSImpl fciBCCCAMELsequence1) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        FurnishChargingInformationSMSRequestImpl req = new FurnishChargingInformationSMSRequestImpl(fciBCCCAMELsequence1);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.furnishChargingInformationSMS, req, true, false);        
    }

    @Override
    public Long addInitialDPSMSRequest(int serviceKey, CalledPartyBCDNumberImpl destinationSubscriberNumber,
            SMSAddressStringImpl callingPartyNumber, EventTypeSMS eventTypeSMS, IMSIImpl imsi,
            LocationInformationImpl locationInformationMSC, LocationInformationGPRSImpl locationInformationGPRS,
            ISDNAddressStringImpl smscCAddress, TimeAndTimezoneImpl timeAndTimezone,
            TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo, TPProtocolIdentifierImpl tPProtocolIdentifier,
            TPDataCodingSchemeImpl tPDataCodingScheme, TPValidityPeriodImpl tPValidityPeriod, CAPExtensionsImpl extensions,
            CallReferenceNumberImpl smsReferenceNumber, ISDNAddressStringImpl mscAddress, ISDNAddressStringImpl sgsnNumber,
            MSClassmark2Impl mSClassmark2, GPRSMSClassImpl gprsMSClass, IMEIImpl imei, ISDNAddressStringImpl calledPartyNumber)
            throws CAPException {
        return this.addInitialDPSMSRequest(_Timer_Default, serviceKey, destinationSubscriberNumber, callingPartyNumber,
                eventTypeSMS, imsi, locationInformationMSC, locationInformationGPRS, smscCAddress, timeAndTimezone,
                tPShortMessageSpecificInfo, tPProtocolIdentifier, tPDataCodingScheme, tPValidityPeriod, extensions,
                smsReferenceNumber, mscAddress, sgsnNumber, mSClassmark2, gprsMSClass, imei, calledPartyNumber);
    }

    @Override
    public Long addInitialDPSMSRequest(int customInvokeTimeout, int serviceKey,
            CalledPartyBCDNumberImpl destinationSubscriberNumber, SMSAddressStringImpl callingPartyNumber,
            EventTypeSMS eventTypeSMS, IMSIImpl imsi, LocationInformationImpl locationInformationMSC,
            LocationInformationGPRSImpl locationInformationGPRS, ISDNAddressStringImpl smscCAddress,
            TimeAndTimezoneImpl timeAndTimezone, TPShortMessageSpecificInfoImpl tPShortMessageSpecificInfo,
            TPProtocolIdentifierImpl tPProtocolIdentifier, TPDataCodingSchemeImpl tPDataCodingScheme,
            TPValidityPeriodImpl tPValidityPeriod, CAPExtensionsImpl extensions, CallReferenceNumberImpl smsReferenceNumber,
            ISDNAddressStringImpl mscAddress, ISDNAddressStringImpl sgsnNumber, MSClassmark2Impl mSClassmark2,
            GPRSMSClassImpl gprsMSClass, IMEIImpl imei, ISDNAddressStringImpl calledPartyNumber) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.initialDPSMS, req, true, false);
    }

    @Override
    public Long addReleaseSMSRequest(RPCauseImpl rpCause) throws CAPException {
        return this.addReleaseSMSRequest(_Timer_Default, rpCause);
    }

    @Override
    public Long addReleaseSMSRequest(int customInvokeTimeout, RPCauseImpl rpCause) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ReleaseSMSRequestImpl req = new ReleaseSMSRequestImpl(rpCause);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.releaseSMS, req, true, false);        
    }

    @Override
    public Long addRequestReportSMSEventRequest(List<SMSEventImpl> smsEvents, CAPExtensionsImpl extensions)
            throws CAPException {
        return this.addRequestReportSMSEventRequest(_Timer_Default, smsEvents, extensions);
    }

    @Override
    public Long addRequestReportSMSEventRequest(int customInvokeTimeout, List<SMSEventImpl> smsEvents,
            CAPExtensionsImpl extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        RequestReportSMSEventRequestImpl req = new RequestReportSMSEventRequestImpl(smsEvents, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.requestReportSMSEvent, req, true, false);        
    }

    @Override
    public Long addResetTimerSMSRequest(TimerID timerID, int timerValue, CAPExtensionsImpl extensions) throws CAPException {
        return this.addResetTimerSMSRequest(_Timer_Default, timerID, timerValue, extensions);
    }

    @Override
    public Long addResetTimerSMSRequest(int customInvokeTimeout, TimerID timerID, int timerValue,
            CAPExtensionsImpl extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ResetTimerSMSRequestImpl req = new ResetTimerSMSRequestImpl(timerID, timerValue, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.resetTimerSMS, req, true, false);        
    }

    @Override
    public Long addContinueSMSRequest() throws CAPException {

        return addContinueSMSRequest(_Timer_Default);
    }

    @Override
    public Long addContinueSMSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_cap3_sms && this.appCntx != CAPApplicationContext.CapV4_cap4_sms)
            throw new CAPException("Bad application context name for ConnectSMSRequest: must be CapV3_cap3_sms or CapV4_cap4_sms");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerSmsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.continueSMS, null, true, false);        
    }

}
