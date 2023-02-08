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

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSMSClass;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class CAPDialogGprsImpl extends CAPDialogImpl implements CAPDialogGprs {
	private static final long serialVersionUID = 1L;

	protected CAPDialogGprsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Integer addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
            TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, AccessPointName accessPointName, RAIdentity routeingAreaIdentity,
            GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities, LocationInformationGPRS locationInformationGPRS,
            PDPInitiationType pdpInitiationType, CAPINAPExtensions extensions, GSNAddress gsnAddress, boolean secondaryPDPContext,
            IMEI imei) throws CAPException {
        return addInitialDpGprsRequest(_Timer_Default, serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone, gprsMSClass,
                endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID, sgsnCapabilities,
                locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);
    }

    @Override
    public Integer addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
            ISDNAddressString msisdn, IMSI imsi, TimeAndTimezone timeAndTimezone, GPRSMSClass gprsMSClass,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, AccessPointName accessPointName,
            RAIdentity routeingAreaIdentity, GPRSChargingID chargingID, SGSNCapabilities sgsnCapabilities,
            LocationInformationGPRS locationInformationGPRS, PDPInitiationType pdpInitiationType, CAPINAPExtensions extensions,
            GSNAddress gsnAddress, boolean secondaryPDPContext, IMEI imei) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF)
            throw new CAPException("Bad application context name for InitialDpGprsRequest: must be CapV3_gprsSSF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        InitialDpGprsRequestImpl req = new InitialDpGprsRequestImpl(serviceKey, gprsEventType, msisdn, imsi, timeAndTimezone,
                gprsMSClass, endUserAddress, qualityOfService, accessPointName, routeingAreaIdentity, chargingID,
                sgsnCapabilities, locationInformationGPRS, pdpInitiationType, extensions, gsnAddress, secondaryPDPContext, imei);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.initialDPGPRS, req, true, false);        
    }

    @Override
    public Integer addRequestReportGPRSEventRequest(List<GPRSEvent> gprsEvent, PDPID pdpID) throws CAPException {
        return addRequestReportGPRSEventRequest(_Timer_Default, gprsEvent, pdpID);
    }

    @Override
    public Integer addRequestReportGPRSEventRequest(int customInvokeTimeout, List<GPRSEvent> gprsEvent, PDPID pdpID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for RequestReportGPRSEventRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        RequestReportGPRSEventRequestImpl req = new RequestReportGPRSEventRequestImpl(gprsEvent, pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.requestReportGPRSEvent, req, true, false);        
    }

    @Override
    public Integer addApplyChargingGPRSRequest(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) throws CAPException {

        return addApplyChargingGPRSRequest(_Timer_Default, chargingCharacteristics, tariffSwitchInterval, pdpID);
    }

    @Override
    public Integer addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristics chargingCharacteristics,
            Integer tariffSwitchInterval, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ApplyChargingGPRSRequestImpl req = new ApplyChargingGPRSRequestImpl(chargingCharacteristics, tariffSwitchInterval,
                pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.applyChargingGPRS, req, true, false);        
    }

    @Override
    public Integer addEntityReleasedGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addEntityReleasedGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Integer addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EntityReleasedGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EntityReleasedGPRSRequestImpl req = new EntityReleasedGPRSRequestImpl(gprsCause, pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.entityReleasedGPRS, req, true, false);        
    }

    @Override
    public void addEntityReleasedGPRSResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EntityReleasedGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.entityReleasedGPRS_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Integer addConnectGPRSRequest(AccessPointName accessPointName, PDPID pdpID) throws CAPException {

        return addConnectGPRSRequest(_Timer_Default, accessPointName, pdpID);
    }

    @Override
    public Integer addConnectGPRSRequest(int customInvokeTimeout, AccessPointName accessPointName, PDPID pdpID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ConnectGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ConnectGPRSRequestImpl req = new ConnectGPRSRequestImpl(accessPointName, pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.connectGPRS, req, true, false);        
    }

    @Override
    public Integer addContinueGPRSRequest(PDPID pdpID) throws CAPException {

        return addContinueGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Integer addContinueGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ContinueGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ContinueGPRSRequestImpl req = new ContinueGPRSRequestImpl(pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.continueGPRS, req, true, false);        
    }

    @Override
    public Integer addReleaseGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addReleaseGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Integer addReleaseGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ReleaseGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ReleaseGPRSRequestImpl req = new ReleaseGPRSRequestImpl(gprsCause, pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.releaseGPRS, req, true, false);        
    }

    @Override
    public Integer addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException {

        return addResetTimerGPRSRequest(_Timer_Default, timerID, timerValue);
    }

    @Override
    public Integer addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ResetTimerGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ResetTimerGPRSRequestImpl req = new ResetTimerGPRSRequestImpl(timerID, timerValue);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.resetTimerGPRS, req, true, false);        
    }

    @Override
    public Integer addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException {

        return addFurnishChargingInformationGPRSRequest(_Timer_Default, fciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Integer addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for FurnishChargingInformationGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        FurnishChargingInformationGPRSRequestImpl req = new FurnishChargingInformationGPRSRequestImpl(
                fciGPRSBillingChargingCharacteristics);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.furnishChargingInformationGPRS, req, true, false);        
    }

    @Override
    public Integer addCancelGPRSRequest(PDPID pdpID) throws CAPException {

        return addCancelGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Integer addCancelGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for CancelGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        CancelGPRSRequestImpl req = new CancelGPRSRequestImpl(pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.cancelGPRS, req, true, false);        
    }

    @Override
    public Integer addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException {

        return addSendChargingInformationGPRSRequest(_Timer_Default, sciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Integer addSendChargingInformationGPRSRequest(int customInvokeTimeout,
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for SendChargingInformationGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        SendChargingInformationGPRSRequestImpl req = new SendChargingInformationGPRSRequestImpl(
                sciGPRSBillingChargingCharacteristics);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.sendChargingInformationGPRS, req, true, false);        
    }

    @Override
    public Integer addApplyChargingReportGPRSRequest(ChargingResult chargingResult, QualityOfService qualityOfService,
            boolean active, PDPID pdpID, ChargingRollOver chargingRollOver) throws CAPException {

        return addApplyChargingReportGPRSRequest(_Timer_Default, chargingResult, qualityOfService, active, pdpID,
                chargingRollOver);
    }

    @Override
    public Integer addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResult chargingResult,
            QualityOfService qualityOfService, boolean active, PDPID pdpID, ChargingRollOver chargingRollOver)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingReportGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ApplyChargingReportGPRSRequestImpl req = new ApplyChargingReportGPRSRequestImpl(chargingResult, qualityOfService,
                active, pdpID, chargingRollOver);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.applyChargingReportGPRS, req, true, false);        
    }

    @Override
    public void addApplyChargingReportGPRSResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingReportGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.applyChargingReportGPRS_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Integer addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException {

        return addEventReportGPRSRequest(_Timer_Default, gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);
    }

    @Override
    public Integer addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EventReportGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EventReportGPRSRequestImpl req = new EventReportGPRSRequestImpl(gprsEventType, miscGPRSInfo,
                gprsEventSpecificInformation, pdpID);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.eventReportGPRS, req, true, false);        
    }

    @Override
    public void addEventReportGPRSResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for RequestReportGPRSEventRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.eventReportGPRS_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Integer addActivityTestGPRSRequest() throws CAPException {
        return addActivityTestGPRSRequest(_Timer_Default);
    }

    @Override
    public Integer addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.activityTestGPRS_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class3, customTimeout.longValue(), CAPOperationCode.activityTestGPRS, null, true, false);        
    }

    @Override
    public void addActivityTestGPRSResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.activityTestGPRS_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, null, null, false, true);        
    }
}