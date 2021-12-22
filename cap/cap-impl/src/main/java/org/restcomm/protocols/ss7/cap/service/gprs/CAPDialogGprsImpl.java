/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.gprs;

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
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
 *
 */
public class CAPDialogGprsImpl extends CAPDialogImpl implements CAPDialogGprs {
	private static final long serialVersionUID = 1L;

	protected CAPDialogGprsImpl(CAPApplicationContext appCntx, Dialog tcapDialog, CAPProviderImpl capProviderImpl,
            CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addInitialDpGprsRequest(int serviceKey, GPRSEventType gprsEventType, ISDNAddressString msisdn, IMSI imsi,
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
    public Long addInitialDpGprsRequest(int customInvokeTimeout, int serviceKey, GPRSEventType gprsEventType,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.initialDPGPRS, req, true, false);        
    }

    @Override
    public Long addRequestReportGPRSEventRequest(List<GPRSEvent> gprsEvent, PDPID pdpID) throws CAPException {
        return addRequestReportGPRSEventRequest(_Timer_Default, gprsEvent, pdpID);
    }

    @Override
    public Long addRequestReportGPRSEventRequest(int customInvokeTimeout, List<GPRSEvent> gprsEvent, PDPID pdpID)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.requestReportGPRSEvent, req, true, false);        
    }

    @Override
    public Long addApplyChargingGPRSRequest(ChargingCharacteristics chargingCharacteristics, Integer tariffSwitchInterval,
            PDPID pdpID) throws CAPException {

        return addApplyChargingGPRSRequest(_Timer_Default, chargingCharacteristics, tariffSwitchInterval, pdpID);
    }

    @Override
    public Long addApplyChargingGPRSRequest(int customInvokeTimeout, ChargingCharacteristics chargingCharacteristics,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.applyChargingGPRS, req, true, false);        
    }

    @Override
    public Long addEntityReleasedGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addEntityReleasedGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Long addEntityReleasedGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.entityReleasedGPRS, req, true, false);        
    }

    @Override
    public void addEntityReleasedGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for EntityReleasedGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Long addConnectGPRSRequest(AccessPointName accessPointName, PDPID pdpID) throws CAPException {

        return addConnectGPRSRequest(_Timer_Default, accessPointName, pdpID);
    }

    @Override
    public Long addConnectGPRSRequest(int customInvokeTimeout, AccessPointName accessPointName, PDPID pdpID)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.connectGPRS, req, true, false);        
    }

    @Override
    public Long addContinueGPRSRequest(PDPID pdpID) throws CAPException {

        return addContinueGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Long addContinueGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.continueGPRS, req, true, false);        
    }

    @Override
    public Long addReleaseGPRSRequest(GPRSCause gprsCause, PDPID pdpID) throws CAPException {

        return addReleaseGPRSRequest(_Timer_Default, gprsCause, pdpID);
    }

    @Override
    public Long addReleaseGPRSRequest(int customInvokeTimeout, GPRSCause gprsCause, PDPID pdpID) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.releaseGPRS, req, true, false);        
    }

    @Override
    public Long addResetTimerGPRSRequest(TimerID timerID, int timerValue) throws CAPException {

        return addResetTimerGPRSRequest(_Timer_Default, timerID, timerValue);
    }

    @Override
    public Long addResetTimerGPRSRequest(int customInvokeTimeout, TimerID timerID, int timerValue) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.resetTimerGPRS, req, true, false);        
    }

    @Override
    public Long addFurnishChargingInformationGPRSRequest(
            CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) throws CAPException {

        return addFurnishChargingInformationGPRSRequest(_Timer_Default, fciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Long addFurnishChargingInformationGPRSRequest(int customInvokeTimeout,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.furnishChargingInformationGPRS, req, true, false);        
    }

    @Override
    public Long addCancelGPRSRequest(PDPID pdpID) throws CAPException {

        return addCancelGPRSRequest(_Timer_Default, pdpID);
    }

    @Override
    public Long addCancelGPRSRequest(int customInvokeTimeout, PDPID pdpID) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.cancelGPRS, req, true, false);        
    }

    @Override
    public Long addSendChargingInformationGPRSRequest(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) throws CAPException {

        return addSendChargingInformationGPRSRequest(_Timer_Default, sciGPRSBillingChargingCharacteristics);
    }

    @Override
    public Long addSendChargingInformationGPRSRequest(int customInvokeTimeout,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.sendChargingInformationGPRS, req, true, false);        
    }

    @Override
    public Long addApplyChargingReportGPRSRequest(ChargingResult chargingResult, QualityOfService qualityOfService,
            boolean active, PDPID pdpID, ChargingRollOver chargingRollOver) throws CAPException {

        return addApplyChargingReportGPRSRequest(_Timer_Default, chargingResult, qualityOfService, active, pdpID,
                chargingRollOver);
    }

    @Override
    public Long addApplyChargingReportGPRSRequest(int customInvokeTimeout, ChargingResult chargingResult,
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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.applyChargingReportGPRS, req, true, false);        
    }

    @Override
    public void addApplyChargingReportGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ApplyChargingReportGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Long addEventReportGPRSRequest(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) throws CAPException {

        return addEventReportGPRSRequest(_Timer_Default, gprsEventType, miscGPRSInfo, gprsEventSpecificInformation, pdpID);
    }

    @Override
    public Long addEventReportGPRSRequest(int customInvokeTimeout, GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.eventReportGPRS, req, true, false);        
    }

    @Override
    public void addEventReportGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for RequestReportGPRSEventRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        this.sendDataComponent(invokeId, null, null, null, null, null, false, true); 
    }

    @Override
    public Long addActivityTestGPRSRequest() throws CAPException {
        return addActivityTestGPRSRequest(_Timer_Default);
    }

    @Override
    public Long addActivityTestGPRSRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSRequest: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerGprsShort();
        else
        	customTimeout = customInvokeTimeout;
        
        return this.sendDataComponent(null, null, InvokeClass.Class3, customTimeout.longValue(), (long) CAPOperationCode.activityTestGPRS, null, true, false);        
    }

    @Override
    public void addActivityTestGPRSResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gprsSSF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSCF_gprsSSF)
            throw new CAPException(
                    "Bad application context name for ActivityTestGPRSResponse: must be CapV3_gsmSCF_gprsSSF or CapV3_gsmSCF_gprsSSF");

        this.sendDataComponent(invokeId, null, null, null, null, null, false, true);        
    }

}
