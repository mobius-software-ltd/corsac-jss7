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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 * @author yulianoifa
 *
 */
public class CAPDialogCircuitSwitchedCallImpl extends CAPDialogImpl implements CAPDialogCircuitSwitchedCall {
	private static final long serialVersionUID = 1L;

	protected CAPDialogCircuitSwitchedCallImpl(CAPApplicationContext appCntx, Dialog tcapDialog,
            CAPProviderImpl capProviderImpl, CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Integer addInitialDPRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
            HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup additionalCallingPartyNumber,
            BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, CauseIsup cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress, CalledPartyBCDNumber calledPartyBCDNumber,
            TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException {

        return addInitialDPRequest(_Timer_Default, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
                CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility,
                additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation,
                cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, cugOutgoingAccess, imsi,
                subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress,
                calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension);
    }

    @Override
    public Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup  callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup  originalCalledPartyID, CAPINAPExtensions extensions,
            HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup  additionalCallingPartyNumber,
            BearerCapability  bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, CauseIsup cause,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI  imsi, SubscriberState subscriberState,
            LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
            CallReferenceNumber  callReferenceNumber, ISDNAddressString    mscAddress, CalledPartyBCDNumber  calledPartyBCDNumber,
            TimeAndTimezone   timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtension initialDPArgExtension)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
            throw new CAPException(
                    "Bad application context name for addInitialDPRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        //doesnt really matter which one to create, both supported for encoding
        InitialDPRequest req = new InitialDPRequestV3Impl(serviceKey, calledPartyNumber, callingPartyNumber,
                callingPartysCategory, CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions,
                highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID,
                redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock,
                cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber,
                mscAddress, calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.initialDP, req, true, false);        
    }

    @Override
    public Integer addApplyChargingReportRequest(TimeDurationChargingResult timeDurationChargingResult) throws CAPException {
        return addApplyChargingReportRequest(_Timer_Default, timeDurationChargingResult);
    }

    @Override
    public Integer addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResult timeDurationChargingResult)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addApplyChargingReportRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ApplyChargingReportRequestImpl req = new ApplyChargingReportRequestImpl(timeDurationChargingResult);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.applyChargingReport, req, true, false);        
    }

    @Override
    public Integer addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            LegType partyToCharge, CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException {

        return addApplyChargingRequest(_Timer_Default, aChBillingChargingCharacteristics, partyToCharge, extensions,
                aChChargingAddress);
    }

    @Override
    public Integer addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addApplyChargingRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ApplyChargingRequestImpl req = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics, partyToCharge,
                extensions, aChChargingAddress);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.applyCharging, req, true, false);        
    }

    @Override
    public Integer addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
            CAPINAPExtensions extensions, LegType legID) throws CAPException {

        return addCallInformationReportRequest(_Timer_Default, requestedInformationList, extensions, legID);
    }

    @Override
    public Integer addCallInformationReportRequest(int customInvokeTimeout,
            List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions, LegType legID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addCallInformationReportRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        CallInformationReportRequestImpl req = new CallInformationReportRequestImpl(requestedInformationList, extensions, legID);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.callInformationReport, req, true, false);        
    }

    @Override
    public Integer addCallInformationRequestRequest(List<RequestedInformationType> requestedInformationTypeList,
            CAPINAPExtensions extensions, LegType legID) throws CAPException {

        return addCallInformationRequestRequest(_Timer_Default, requestedInformationTypeList, extensions, legID);
    }

    @Override
    public Integer addCallInformationRequestRequest(int customInvokeTimeout,
            List<RequestedInformationType> requestedInformationTypeList, CAPINAPExtensions extensions, LegType legID)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addCallInformationRequestRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        CallInformationRequestRequestImpl req = new CallInformationRequestRequestImpl(requestedInformationTypeList, extensions,
                legID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.callInformationRequest, req, true, false);        
    }

    @Override
    public Integer addConnectRequest(DestinationRoutingAddress   destinationRoutingAddress, AlertingPattern alertingPattern,
            OriginalCalledNumberIsup  originalCalledPartyID, CAPINAPExtensions extensions, Carrier carrier,
            CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

        return addConnectRequest(_Timer_Default, destinationRoutingAddress, alertingPattern, originalCalledPartyID, extensions,
                carrier, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers,
                serviceInteractionIndicatorsTwo, chargeNumber, legToBeConnected, cugInterlock, cugOutgoingAccess,
                suppressionOfAnnouncement, ocsIApplicable, naoliInfo, borInterrogationRequested, suppressNCSI);
    }

    @Override
    public Integer addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress   destinationRoutingAddress,
    		AlertingPattern alertingPattern, OriginalCalledNumberIsup  originalCalledPartyID, CAPINAPExtensions extensions,
            Carrier carrier, CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
            RedirectionInformationIsup redirectionInformation, List<GenericNumberIsup> genericNumbers,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, LocationNumberIsup chargeNumber,
            LegID legToBeConnected, CUGInterlock cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfo naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addConnectRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ConnectRequestImpl req = new ConnectRequestImpl(destinationRoutingAddress, alertingPattern, originalCalledPartyID,
                extensions, carrier, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers,
                serviceInteractionIndicatorsTwo, chargeNumber, legToBeConnected, cugInterlock, cugOutgoingAccess,
                suppressionOfAnnouncement, ocsIApplicable, naoliInfo, borInterrogationRequested, suppressNCSI);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.connect, req, true, false);        
    }

    @Override
    public Integer addContinueRequest() throws CAPException {

        return addContinueRequest(_Timer_Default);
    }

    @Override
    public Integer addContinueRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.continue_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.continueCode, null, true, false);        
    }

    @Override
    public Integer addContinueWithArgumentRequest(AlertingPattern alertingPattern, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory, List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException {

        return addContinueWithArgumentRequest(_Timer_Default, alertingPattern, extensions,
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock,
                cugOutgoingAccess, chargeNumber, carrier, suppressionOfAnnouncement, naOliInfo,
                borInterrogationRequested, suppressOCsi, continueWithArgumentArgExtension);
    }

    @Override
    public Integer addContinueWithArgumentRequest(int customInvokeTimeout, AlertingPattern alertingPattern,
            CAPINAPExtensions extensions, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            CallingPartysCategoryIsup callingPartysCategory, List<GenericNumberIsup> genericNumbers,
            CUGInterlock cugInterlock, boolean cugOutgoingAccess, LocationNumberIsup chargeNumber, Carrier carrier,
            boolean suppressionOfAnnouncement, NAOliInfo naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtension continueWithArgumentArgExtension)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueWithArgumentRequest: must be CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ContinueWithArgumentRequestImpl req = new ContinueWithArgumentRequestImpl(alertingPattern, extensions,
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock,
                cugOutgoingAccess, chargeNumber, carrier, suppressionOfAnnouncement, naOliInfo,
                borInterrogationRequested, suppressOCsi, continueWithArgumentArgExtension);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.continueWithArgument, req, true, false);        
    }

    @Override
    public Integer addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException {

        return addEventReportBCSMRequest(_Timer_Default, eventTypeBCSM, eventSpecificInformationBCSM, legID, miscCallInfo,
                extensions);
    }

    @Override
    public Integer addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addEventReportBCSMRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EventReportBCSMRequestImpl req = new EventReportBCSMRequestImpl(eventTypeBCSM, eventSpecificInformationBCSM, legID,
                miscCallInfo, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.eventReportBCSM, req, true, false);        
    }

    @Override
    public Integer addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, CAPINAPExtensions extensions)
            throws CAPException {

        return addRequestReportBCSMEventRequest(_Timer_Default, bcsmEventList, extensions);
    }

    @Override
    public Integer addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addRequestReportBCSMEventRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        RequestReportBCSMEventRequestImpl req = new RequestReportBCSMEventRequestImpl(bcsmEventList, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.requestReportBCSMEvent, req, true, false);        
    }

    @Override
    public Integer addReleaseCallRequest(CauseIsup cause) throws CAPException {

        return addReleaseCallRequest(_Timer_Default, cause);
    }

    @Override
    public Integer addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addReleaseCallRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ReleaseCallRequestImpl req = new ReleaseCallRequestImpl(cause);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.releaseCall, req, true, false);        
    }

    @Override
    public Integer addActivityTestRequest() throws CAPException {

        return addActivityTestRequest(_Timer_Default);
    }

    @Override
    public Integer addActivityTestRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addActivityTestRequest: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF, CapV4_gsmSRF_gsmSCF "
                            + "or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.activityTest_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class3, customTimeout.longValue(), CAPOperationCode.activityTest, null, true, false);        
    }

    public void addActivityTestResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addActivityTestResponse: must be CapV1_gsmSSF_to_gsmSCF, CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF, CapV4_gsmSRF_gsmSCF "
                            + "or CapV4_scf_gsmSSFGeneric");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.activityTest_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, null, null, false, true);
    }

    @Override
    public Integer addAssistRequestInstructionsRequest(DigitsIsup  correlationID, IPSSPCapabilities ipSSPCapabilities,
            CAPINAPExtensions extensions) throws CAPException {

        return addAssistRequestInstructionsRequest(_Timer_Default, correlationID, ipSSPCapabilities, extensions);
    }

    @Override
    public Integer addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup  correlationID,
            IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addAssistRequestInstructionsRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF  "
                            + "or CapV4_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        AssistRequestInstructionsRequestImpl req = new AssistRequestInstructionsRequestImpl(correlationID, ipSSPCapabilities,
                extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.assistRequestInstructions, req, true, false);        
    }

    @Override
    public Integer addEstablishTemporaryConnectionRequest(DigitsIsup  assistingSSPIPRoutingAddress, DigitsIsup  correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup  originalCalledPartyID, CallingPartyNumberIsup  callingPartyNumber) throws CAPException {

        return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID, scfID,
                extensions, carrier, serviceInteractionIndicatorsTwo, callSegmentID, naOliInfo, chargeNumber,
                originalCalledPartyID, callingPartyNumber);
    }

    @Override
    public Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup  assistingSSPIPRoutingAddress,
            DigitsIsup  correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup  originalCalledPartyID,
            CallingPartyNumberIsup  callingPartyNumber) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addEstablishTemporaryConnectionRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EstablishTemporaryConnectionRequestImpl req = new EstablishTemporaryConnectionRequestImpl(assistingSSPIPRoutingAddress,
                correlationID, scfID, extensions, carrier, serviceInteractionIndicatorsTwo, callSegmentID, naOliInfo,
                chargeNumber, originalCalledPartyID, callingPartyNumber);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.establishTemporaryConnection, req, true, false);        
    }
    


    @Override
    public Integer addEstablishTemporaryConnectionRequest(DigitsIsup  assistingSSPIPRoutingAddress, DigitsIsup  correlationID, ScfID scfID,
            CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo,
            NAOliInfo naOliInfo, LocationNumberIsup chargeNumber,
            OriginalCalledNumberIsup  originalCalledPartyID, CallingPartyNumberIsup  callingPartyNumber) throws CAPException {

        return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID, scfID,
                extensions, carrier, serviceInteractionIndicatorsTwo, naOliInfo, chargeNumber,
                originalCalledPartyID, callingPartyNumber);
    }

    @Override
    public Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup  assistingSSPIPRoutingAddress,
            DigitsIsup  correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, NAOliInfo naOliInfo,
            LocationNumberIsup chargeNumber, OriginalCalledNumberIsup  originalCalledPartyID,
            CallingPartyNumberIsup  callingPartyNumber) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addEstablishTemporaryConnectionRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        EstablishTemporaryConnectionRequestImpl req = new EstablishTemporaryConnectionRequestImpl(assistingSSPIPRoutingAddress,
                correlationID, scfID, extensions, carrier, serviceInteractionIndicatorsTwo, naOliInfo,
                chargeNumber, originalCalledPartyID, callingPartyNumber);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.establishTemporaryConnection, req, true, false);        
    }

    @Override
    public Integer addDisconnectForwardConnectionRequest() throws CAPException {

        return addDisconnectForwardConnectionRequest(_Timer_Default);
    }

    @Override
    public Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectForwardConnectionRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.disconnectForwardConnection_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.disconnectForwardConnection, null, true, false);        
    }

    @Override
    public Integer addDisconnectForwardConnectionWithArgumentRequest(Integer callSegmentID, CAPINAPExtensions extensions)
            throws CAPException {
        return addDisconnectForwardConnectionWithArgumentRequest(_Timer_Default, callSegmentID, extensions);
    }

    @Override
    public Integer addDisconnectForwardConnectionWithArgumentRequest(int customInvokeTimeout, Integer callSegmentID,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectForwardConnectionWithArgumentRequest: must be CapV4_gsmSSF_scfGeneric, " +
                    "CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        DisconnectForwardConnectionWithArgumentRequestImpl req = new DisconnectForwardConnectionWithArgumentRequestImpl(
                callSegmentID, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.dFCWithArgument, req, true, false);        
    }

    @Override
    public Integer addConnectToResourceRequest(CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

        return addConnectToResourceRequest(_Timer_Default, resourceAddress_IPRoutingAddress, resourceAddress_Null, extensions,
                serviceInteractionIndicatorsTwo, callSegmentID);
    }

    @Override
    public Integer addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPINAPExtensions extensions,
            ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addConnectToResourceRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(resourceAddress_IPRoutingAddress,
                resourceAddress_Null, extensions, serviceInteractionIndicatorsTwo, callSegmentID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.connectToResource, req, true, false);        
    }

    @Override
    public Integer addResetTimerRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions, Integer callSegmentID)
            throws CAPException {

        return addResetTimerRequest(_Timer_Default, timerID, timerValue, extensions, callSegmentID);
    }

    @Override
    public Integer addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPINAPExtensions extensions,
            Integer callSegmentID) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addResetTimerRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        ResetTimerRequestImpl req = new ResetTimerRequestImpl(timerID, timerValue, extensions, callSegmentID);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.resetTimer, req, true, false);        
    }

    @Override
    public Integer addFurnishChargingInformationRequest(FCIBCCCAMELSequence1 FCIBCCCAMELsequence1) throws CAPException {

        return addFurnishChargingInformationRequest(_Timer_Default, FCIBCCCAMELsequence1);
    }

    @Override
    public Integer addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1 FCIBCCCAMELsequence1)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addFurnishChargingInformationRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        FurnishChargingInformationRequestImpl req = new FurnishChargingInformationRequestImpl(FCIBCCCAMELsequence1);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.furnishChargingInformation, req, true, false);        
    }

    @Override
    public Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
            LegType partyToCharge, CAPINAPExtensions extensions) throws CAPException {

        return addSendChargingInformationRequest(_Timer_Default, sciBillingChargingCharacteristics, partyToCharge, extensions);
    }

    @Override
    public Integer addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
            throw new CAPException(
                    "Bad application context name for addSendChargingInformationRequest: must be CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        SendChargingInformationRequestImpl req = new SendChargingInformationRequestImpl(sciBillingChargingCharacteristics,
                partyToCharge, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.sendChargingInformation, req, true, false);        
    }

    @Override
    public Integer addSpecializedResourceReportRequest_CapV23(Integer linkedId) throws CAPException {

        return addSpecializedResourceReportRequest_CapV23(linkedId, _Timer_Default);
    }

    @Override
    public Integer addSpecializedResourceReportRequest_CapV4(Integer linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException {

        return addSpecializedResourceReportRequest_CapV4(linkedId, _Timer_Default, isAllAnnouncementsComplete,
                isFirstAnnouncementStarted);
    }

    @Override
    public Integer addSpecializedResourceReportRequest_CapV23(Integer linkedId, int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addSpecializedResourceReportRequest_CapV23: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV2_gsmSRF_to_gsmSCF or CapV3_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        SpecializedResourceReportRequest req = new SpecializedResourceReportRequestImpl(false,false);
        return this.sendDataComponent(null, linkedId, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.specializedResourceReport, req, true, false);        
    }

    @Override
    public Integer addSpecializedResourceReportRequest_CapV4(Integer linkedId, int customInvokeTimeout,
            boolean isAllAnnouncementsComplete, boolean isFirstAnnouncementStarted) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addSpecializedResourceReportRequest_CapV4: "
                            + "must be CapV4_gsmSSF_scfGeneric, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric or CapV4_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        SpecializedResourceReportRequestImpl req = new SpecializedResourceReportRequestImpl(isAllAnnouncementsComplete,
                isFirstAnnouncementStarted);
        return this.sendDataComponent(null, linkedId, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.specializedResourceReport, req, true, false);        
    }

    @Override
    public Integer addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPlayAnnouncementRequest(_Timer_Default, informationToSend, disconnectFromIPForbidden,
                requestAnnouncementCompleteNotification, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Integer addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPlayAnnouncementRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        PlayAnnouncementRequestImpl req = new PlayAnnouncementRequestImpl(informationToSend, disconnectFromIPForbidden,
                requestAnnouncementCompleteNotification, extensions, callSegmentID, requestAnnouncementStartedNotification);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.playAnnouncement, req, true, false);        
    }

    @Override
    public Integer addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPromptAndCollectUserInformationRequest(_Timer_Default, collectedInfo, disconnectFromIPForbidden,
                informationToSend, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions,
            Integer callSegmentID, Boolean requestAnnouncementStartedNotification) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPromptAndCollectUserInformationRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        PromptAndCollectUserInformationRequestImpl req = new PromptAndCollectUserInformationRequestImpl(collectedInfo,
                disconnectFromIPForbidden, informationToSend, extensions, callSegmentID, requestAnnouncementStartedNotification);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.promptAndCollectUserInformation, req, true, false);        
    }

    @Override
    public void addPromptAndCollectUserInformationResponse_DigitsResponse(int invokeId, DigitsIsup  digitsResponse)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addPromptAndCollectUserInformationResponse: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        PromptAndCollectUserInformationResponseImpl res = new PromptAndCollectUserInformationResponseImpl(digitsResponse);
        this.sendDataComponent(invokeId, null, null, null, CAPOperationCode.promptAndCollectUserInformation, res, false, true);        
    }

    @Override
    public Integer addCancelRequest_InvokeId(Integer invokeID) throws CAPException {

        return addCancelRequest_InvokeId(_Timer_Default, invokeID);
    }

    @Override
    public Integer addCancelRequest_AllRequests() throws CAPException {

        return addCancelRequest_AllRequests(_Timer_Default);
    }

    @Override
    public Integer addCancelRequest_CallSegmentToCancel(CallSegmentToCancel callSegmentToCancel) throws CAPException {

        return addCancelRequest_CallSegmentToCancel(_Timer_Default, callSegmentToCancel);
    }

    @Override
    public Integer addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(invokeID);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Integer addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(true);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Integer addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancel callSegmentToCancel)
            throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(callSegmentToCancel);
        return addCancelRequest(customInvokeTimeout, req);
    }

    private Integer addCancelRequest(int customInvokeTimeout, CancelRequest req) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                && this.appCntx != CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV3_gsmSRF_gsmSCF
                && this.appCntx != CAPApplicationContext.CapV4_gsmSRF_gsmSCF)
            throw new CAPException(
                    "Bad application context name for addCancelRequest: must be "
                            + "CapV2_gsmSSF_to_gsmSCF, CapV3_gsmSSF_scfGeneric, CapV4_gsmSSF_scfGeneric, "
                            + "CapV2_assistGsmSSF_to_gsmSCF, CapV3_gsmSSF_scfAssistHandoff, CapV4_gsmSSF_scfAssistHandoff, CapV4_scf_gsmSSFGeneric"
                            + ", CapV2_gsmSRF_to_gsmSCF, CapV3_gsmSRF_gsmSCF or CapV4_gsmSRF_gsmSCF");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.cancel_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), CAPOperationCode.cancelCode, req, true, false);        
    }

    @Override
    public Integer addDisconnectLegRequest(LegID logToBeReleased, CauseIsup releaseCause, CAPINAPExtensions extensions)
            throws CAPException {
        return addDisconnectLegRequest(_Timer_Default, logToBeReleased, releaseCause, extensions);
    }

    @Override
    public Integer addDisconnectLegRequest(int customInvokeTimeout, LegID logToBeReleased, CauseIsup releaseCause,
            CAPINAPExtensions extensions) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addDisconnectLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        DisconnectLegRequestImpl req = new DisconnectLegRequestImpl(logToBeReleased, releaseCause, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.disconnectLeg, req, true, false);        
    }

    @Override
    public void addDisconnectLegResponse(int invokeId) throws CAPException {
        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException("Bad application context name for addDisconnectLegResponse: must be " + "CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.disconnectLeg_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, CAPOperationCode.disconnectLeg, null, false, true);        
    }

    @Override
    public Integer addInitiateCallAttemptRequest(DestinationRoutingAddress   destinationRoutingAddress,
            CAPINAPExtensions extensions, LegID legToBeCreated, Integer newCallSegment,
            CallingPartyNumberIsup  callingPartyNumber, CallReferenceNumber  callReferenceNumber,
            ISDNAddressString    gsmSCFAddress, boolean suppressTCsi) throws CAPException {
        return addInitiateCallAttemptRequest(_Timer_Default, destinationRoutingAddress, extensions, legToBeCreated,
                newCallSegment, callingPartyNumber, callReferenceNumber, gsmSCFAddress, suppressTCsi);
    }

    @Override
    public Integer addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddress   destinationRoutingAddress, CAPINAPExtensions extensions, LegID legToBeCreated,
            Integer newCallSegment, CallingPartyNumberIsup  callingPartyNumber, CallReferenceNumber  callReferenceNumber,
            ISDNAddressString    gsmSCFAddress, boolean suppressTCsi) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addInitiateCallAttemptRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        InitiateCallAttemptRequestImpl req = new InitiateCallAttemptRequestImpl(destinationRoutingAddress, extensions,
                legToBeCreated, newCallSegment, callingPartyNumber, callReferenceNumber, gsmSCFAddress, suppressTCsi);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.initiateCallAttempt, req, true, false);        
    }

    @Override
    public void addInitiateCallAttemptResponse(int invokeId, SupportedCamelPhases supportedCamelPhases,
            OfferedCamel4Functionalities offeredCamel4Functionalities, CAPINAPExtensions extensions,
            boolean releaseCallArgExtensionAllowed) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addInitiateCallAttemptResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        InitiateCallAttemptResponseImpl res = new InitiateCallAttemptResponseImpl(supportedCamelPhases,
                offeredCamel4Functionalities, extensions, releaseCallArgExtensionAllowed);
        this.sendDataComponent(invokeId, null, null, null, CAPOperationCode.initiateCallAttempt, res, false, true);
    }

    @Override
    public Integer addMoveLegRequest(LegID logIDToMove, CAPINAPExtensions extensions) throws CAPException {
        return addMoveLegRequest(_Timer_Default, logIDToMove, extensions);
    }

    @Override
    public Integer addMoveLegRequest(int customInvokeTimeout, LegID logIDToMove, CAPINAPExtensions extensions)
            throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addMoveLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        MoveLegRequestImpl req = new MoveLegRequestImpl(logIDToMove, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.moveLeg, req, true, false);        
    }

    @Override
    public void addMoveLegResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addMoveLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.moveLeg_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, CAPOperationCode.moveLeg, null, false, true);        
    }

    @Override
    public Integer addCollectInformationRequest() throws CAPException {

        return addCollectInformationRequest(_Timer_Default);
    }

    @Override
    public Integer addCollectInformationRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.collectInformation_Request.name(), getNetworkId());               
    	return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.collectInformation, null, true, false);        
    }

    public Integer addSplitLegRequest(LegID legIDToSplit, Integer newCallSegmentId, CAPINAPExtensions extensions)
            throws CAPException {
        return addSplitLegRequest(_Timer_Default, legIDToSplit, newCallSegmentId, extensions);
    }

    public Integer addSplitLegRequest(int customInvokeTimeout, LegID legIDToSplit, Integer newCallSegmentId,
            CAPINAPExtensions extensions) throws CAPException {
        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context for addSplitLegRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        SplitLegRequestImpl req = new SplitLegRequestImpl(legIDToSplit, newCallSegmentId, extensions);
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), CAPOperationCode.splitLeg, req, true, false);        
    }

    public void addSplitLegResponse(int invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context for addSplitLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        capProviderImpl.getCAPStack().newMessageSent(CAPMessageType.splitLeg_Response.name(), getNetworkId());               
    	this.sendDataComponent(invokeId, null, null, null, CAPOperationCode.splitLeg, null, false, true);        
    }

    @Override
    public Integer addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators, ControlType controlType, GapTreatment gapTreatment, CAPINAPExtensions capExtension) throws CAPException {
        return addCallGapRequest(_Timer_Default, gapCriteria, gapIndicators, controlType, gapTreatment, capExtension);
    }

    @Override
    public Integer addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria, GapIndicators gapIndicators, ControlType controlType, GapTreatment gapTreatment, CAPINAPExtensions capExtension) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
            throw new CAPException(
                    "Bad application context name for addApplyChargingReportRequest: must be CapV3_gsmSSF_scfGeneric or CapV4_gsmSSF_scfGeneric");
        }

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        CallGapRequestImpl req = new CallGapRequestImpl(gapCriteria, gapIndicators, controlType, gapTreatment, capExtension);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), CAPOperationCode.callGap, req, true, false);        
    }
}
