/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPServiceBase;
import org.restcomm.protocols.ss7.cap.api.gap.GapCriteriaImpl;
import org.restcomm.protocols.ss7.cap.api.gap.GapIndicatorsImpl;
import org.restcomm.protocols.ss7.cap.api.gap.GapTreatmentImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CGEncountered;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ContinueWithArgumentArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ControlType;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInapImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallReferenceNumberImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SubscriberStateImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGIndexImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInterlockImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 *
 */
public class CAPDialogCircuitSwitchedCallImpl extends CAPDialogImpl implements CAPDialogCircuitSwitchedCall {
	private static final long serialVersionUID = 1L;

	protected CAPDialogCircuitSwitchedCallImpl(CAPApplicationContext appCntx, Dialog tcapDialog,
            CAPProviderImpl capProviderImpl, CAPServiceBase capService) {
        super(appCntx, tcapDialog, capProviderImpl, capService);
    }

    @Override
    public Long addInitialDPRequest(int serviceKey, CalledPartyNumberCapImpl calledPartyNumber,
            CallingPartyNumberCapImpl callingPartyNumber, CallingPartysCategoryInapImpl callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilitiesImpl IPSSPCapabilities, LocationNumberCapImpl locationNumber,
            OriginalCalledNumberCapImpl originalCalledPartyID, CAPExtensionsImpl extensions,
            HighLayerCompatibilityInapImpl highLayerCompatibility, DigitsImpl additionalCallingPartyNumber,
            BearerCapabilityImpl bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInapImpl redirectionInformation, CauseCapImpl cause,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, CarrierImpl carrier, CUGIndexImpl cugIndex,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, IMSIImpl imsi, SubscriberStateImpl subscriberState,
            LocationInformationImpl locationInformation, ExtBasicServiceCodeImpl extBasicServiceCode,
            CallReferenceNumberImpl callReferenceNumber, ISDNAddressStringImpl mscAddress, CalledPartyBCDNumberImpl calledPartyBCDNumber,
            TimeAndTimezoneImpl timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtensionImpl initialDPArgExtension)
            throws CAPException {

        return addInitialDPRequest(_Timer_Default, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
                CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility,
                additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation,
                cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock, cugOutgoingAccess, imsi,
                subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber, mscAddress,
                calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension);
    }

    @Override
    public Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberCapImpl calledPartyNumber,
            CallingPartyNumberCapImpl  callingPartyNumber, CallingPartysCategoryInapImpl callingPartysCategory,
            CGEncountered CGEncountered, IPSSPCapabilitiesImpl IPSSPCapabilities, LocationNumberCapImpl locationNumber,
            OriginalCalledNumberCapImpl  originalCalledPartyID, CAPExtensionsImpl extensions,
            HighLayerCompatibilityInapImpl highLayerCompatibility, DigitsImpl  additionalCallingPartyNumber,
            BearerCapabilityImpl  bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInapImpl redirectionInformation, CauseCapImpl cause,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, CarrierImpl carrier, CUGIndexImpl cugIndex,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, IMSIImpl  imsi, SubscriberStateImpl subscriberState,
            LocationInformationImpl locationInformation, ExtBasicServiceCodeImpl extBasicServiceCode,
            CallReferenceNumberImpl  callReferenceNumber, ISDNAddressStringImpl    mscAddress, CalledPartyBCDNumberImpl  calledPartyBCDNumber,
            TimeAndTimezoneImpl   timeAndTimezone, boolean callForwardingSSPending, InitialDPArgExtensionImpl initialDPArgExtension)
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
        
        InitialDPRequestImpl req = new InitialDPRequestImpl(serviceKey, calledPartyNumber, callingPartyNumber,
                callingPartysCategory, CGEncountered, IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions,
                highLayerCompatibility, additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID,
                redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock,
                cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber,
                mscAddress, calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending, initialDPArgExtension);
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.initialDP, req, true, false);        
    }

    @Override
    public Long addApplyChargingReportRequest(TimeDurationChargingResultImpl timeDurationChargingResult) throws CAPException {
        return addApplyChargingReportRequest(_Timer_Default, timeDurationChargingResult);
    }

    @Override
    public Long addApplyChargingReportRequest(int customInvokeTimeout, TimeDurationChargingResultImpl timeDurationChargingResult)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.applyChargingReport, req, true, false);        
    }

    @Override
    public Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics,
            SendingLegIDImpl partyToCharge, CAPExtensionsImpl extensions, AChChargingAddressImpl aChChargingAddress) throws CAPException {

        return addApplyChargingRequest(_Timer_Default, aChBillingChargingCharacteristics, partyToCharge, extensions,
                aChChargingAddress);
    }

    @Override
    public Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristicsImpl aChBillingChargingCharacteristics, SendingLegIDImpl partyToCharge,
            CAPExtensionsImpl extensions, AChChargingAddressImpl aChChargingAddress) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.applyCharging, req, true, false);        
    }

    @Override
    public Long addCallInformationReportRequest(List<RequestedInformationImpl> requestedInformationList,
            CAPExtensionsImpl extensions, ReceivingLegIDImpl legID) throws CAPException {

        return addCallInformationReportRequest(_Timer_Default, requestedInformationList, extensions, legID);
    }

    @Override
    public Long addCallInformationReportRequest(int customInvokeTimeout,
            List<RequestedInformationImpl> requestedInformationList, CAPExtensionsImpl extensions, ReceivingLegIDImpl legID)
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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.callInformationReport, req, true, false);        
    }

    @Override
    public Long addCallInformationRequestRequest(List<RequestedInformationType> requestedInformationTypeList,
            CAPExtensionsImpl extensions, SendingLegIDImpl legID) throws CAPException {

        return addCallInformationRequestRequest(_Timer_Default, requestedInformationTypeList, extensions, legID);
    }

    @Override
    public Long addCallInformationRequestRequest(int customInvokeTimeout,
            List<RequestedInformationType> requestedInformationTypeList, CAPExtensionsImpl extensions, SendingLegIDImpl legID)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.callInformationRequest, req, true, false);        
    }

    @Override
    public Long addConnectRequest(DestinationRoutingAddressImpl   destinationRoutingAddress, AlertingPatternCapImpl alertingPattern,
            OriginalCalledNumberCapImpl  originalCalledPartyID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            CallingPartysCategoryInapImpl callingPartysCategory, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInapImpl redirectionInformation, List<GenericNumberCapImpl> genericNumbers,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, LocationNumberCapImpl chargeNumber,
            LegIDImpl legToBeConnected, CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfoImpl naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

        return addConnectRequest(_Timer_Default, destinationRoutingAddress, alertingPattern, originalCalledPartyID, extensions,
                carrier, callingPartysCategory, redirectingPartyID, redirectionInformation, genericNumbers,
                serviceInteractionIndicatorsTwo, chargeNumber, legToBeConnected, cugInterlock, cugOutgoingAccess,
                suppressionOfAnnouncement, ocsIApplicable, naoliInfo, borInterrogationRequested, suppressNCSI);
    }

    @Override
    public Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddressImpl   destinationRoutingAddress,
            AlertingPatternCapImpl alertingPattern, OriginalCalledNumberCapImpl  originalCalledPartyID, CAPExtensionsImpl extensions,
            CarrierImpl carrier, CallingPartysCategoryInapImpl callingPartysCategory, RedirectingPartyIDCapImpl redirectingPartyID,
            RedirectionInformationInapImpl redirectionInformation, List<GenericNumberCapImpl> genericNumbers,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, LocationNumberCapImpl chargeNumber,
            LegIDImpl legToBeConnected, CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, boolean suppressionOfAnnouncement,
            boolean ocsIApplicable, NAOliInfoImpl naoliInfo, boolean borInterrogationRequested, boolean suppressNCSI) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.connect, req, true, false);        
    }

    @Override
    public Long addContinueRequest() throws CAPException {

        return addContinueRequest(_Timer_Default);
    }

    @Override
    public Long addContinueRequest(int customInvokeTimeout) throws CAPException {

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
        
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.continueCode, null, true, false);        
    }

    @Override
    public Long addContinueWithArgumentRequest(AlertingPatternCapImpl alertingPattern, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInapImpl callingPartysCategory, List<GenericNumberCapImpl> genericNumbers,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, LocationNumberCapImpl chargeNumber, CarrierImpl carrier,
            boolean suppressionOfAnnouncement, NAOliInfoImpl naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension)
            throws CAPException {

        return addContinueWithArgumentRequest(_Timer_Default, alertingPattern, extensions,
                serviceInteractionIndicatorsTwo, callingPartysCategory, genericNumbers, cugInterlock,
                cugOutgoingAccess, chargeNumber, carrier, suppressionOfAnnouncement, naOliInfo,
                borInterrogationRequested, suppressOCsi, continueWithArgumentArgExtension);
    }

    @Override
    public Long addContinueWithArgumentRequest(int customInvokeTimeout, AlertingPatternCapImpl alertingPattern,
            CAPExtensionsImpl extensions, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            CallingPartysCategoryInapImpl callingPartysCategory, List<GenericNumberCapImpl> genericNumbers,
            CUGInterlockImpl cugInterlock, boolean cugOutgoingAccess, LocationNumberCapImpl chargeNumber, CarrierImpl carrier,
            boolean suppressionOfAnnouncement, NAOliInfoImpl naOliInfo, boolean borInterrogationRequested,
            boolean suppressOCsi, ContinueWithArgumentArgExtensionImpl continueWithArgumentArgExtension)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.continueWithArgument, req, true, false);        
    }

    @Override
    public Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSMImpl eventSpecificInformationBCSM, ReceivingLegIDImpl legID, MiscCallInfoImpl miscCallInfo,
            CAPExtensionsImpl extensions) throws CAPException {

        return addEventReportBCSMRequest(_Timer_Default, eventTypeBCSM, eventSpecificInformationBCSM, legID, miscCallInfo,
                extensions);
    }

    @Override
    public Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
            EventSpecificInformationBCSMImpl eventSpecificInformationBCSM, ReceivingLegIDImpl legID, MiscCallInfoImpl miscCallInfo,
            CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.eventReportBCSM, req, true, false);        
    }

    @Override
    public Long addRequestReportBCSMEventRequest(List<BCSMEventImpl> bcsmEventList, CAPExtensionsImpl extensions)
            throws CAPException {

        return addRequestReportBCSMEventRequest(_Timer_Default, bcsmEventList, extensions);
    }

    @Override
    public Long addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEventImpl> bcsmEventList,
            CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.requestReportBCSMEvent, req, true, false);        
    }

    @Override
    public Long addReleaseCallRequest(CauseCapImpl cause) throws CAPException {

        return addReleaseCallRequest(_Timer_Default, cause);
    }

    @Override
    public Long addReleaseCallRequest(int customInvokeTimeout, CauseCapImpl cause) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.releaseCall, req, true, false);        
    }

    @Override
    public Long addActivityTestRequest() throws CAPException {

        return addActivityTestRequest(_Timer_Default);
    }

    @Override
    public Long addActivityTestRequest(int customInvokeTimeout) throws CAPException {

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
        
        return this.sendDataComponent(null, null, InvokeClass.Class3, customTimeout.longValue(), (long) CAPOperationCode.activityTest, null, true, false);        
    }

    public void addActivityTestResponse(long invokeId) throws CAPException {

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

        this.sendDataComponent(invokeId, null, null, null, null, null, false, true);
    }

    @Override
    public Long addAssistRequestInstructionsRequest(DigitsImpl  correlationID, IPSSPCapabilitiesImpl ipSSPCapabilities,
            CAPExtensionsImpl extensions) throws CAPException {

        return addAssistRequestInstructionsRequest(_Timer_Default, correlationID, ipSSPCapabilities, extensions);
    }

    @Override
    public Long addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsImpl  correlationID,
            IPSSPCapabilitiesImpl ipSSPCapabilities, CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.assistRequestInstructions, req, true, false);        
    }

    @Override
    public Long addEstablishTemporaryConnectionRequest(DigitsImpl  assistingSSPIPRoutingAddress, DigitsImpl  correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            Integer callSegmentID, NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl  originalCalledPartyID, CallingPartyNumberCapImpl  callingPartyNumber) throws CAPException {

        return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID, scfID,
                extensions, carrier, serviceInteractionIndicatorsTwo, callSegmentID, naOliInfo, chargeNumber,
                originalCalledPartyID, callingPartyNumber);
    }

    @Override
    public Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsImpl  assistingSSPIPRoutingAddress,
            DigitsImpl  correlationID, ScfIDImpl scfID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID, NAOliInfoImpl naOliInfo,
            LocationNumberCapImpl chargeNumber, OriginalCalledNumberCapImpl  originalCalledPartyID,
            CallingPartyNumberCapImpl  callingPartyNumber) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.establishTemporaryConnection, req, true, false);        
    }
    


    @Override
    public Long addEstablishTemporaryConnectionRequest(DigitsImpl  assistingSSPIPRoutingAddress, DigitsImpl  correlationID, ScfIDImpl scfID,
            CAPExtensionsImpl extensions, CarrierImpl carrier, ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo,
            NAOliInfoImpl naOliInfo, LocationNumberCapImpl chargeNumber,
            OriginalCalledNumberCapImpl  originalCalledPartyID, CallingPartyNumberCapImpl  callingPartyNumber) throws CAPException {

        return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID, scfID,
                extensions, carrier, serviceInteractionIndicatorsTwo, naOliInfo, chargeNumber,
                originalCalledPartyID, callingPartyNumber);
    }

    @Override
    public Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsImpl  assistingSSPIPRoutingAddress,
            DigitsImpl  correlationID, ScfIDImpl scfID, CAPExtensionsImpl extensions, CarrierImpl carrier,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, NAOliInfoImpl naOliInfo,
            LocationNumberCapImpl chargeNumber, OriginalCalledNumberCapImpl  originalCalledPartyID,
            CallingPartyNumberCapImpl  callingPartyNumber) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.establishTemporaryConnection, req, true, false);        
    }

    @Override
    public Long addDisconnectForwardConnectionRequest() throws CAPException {

        return addDisconnectForwardConnectionRequest(_Timer_Default);
    }

    @Override
    public Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws CAPException {

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
        
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.disconnectForwardConnection, null, true, false);        
    }

    @Override
    public Long addDisconnectForwardConnectionWithArgumentRequest(Integer callSegmentID, CAPExtensionsImpl extensions)
            throws CAPException {
        return addDisconnectForwardConnectionWithArgumentRequest(_Timer_Default, callSegmentID, extensions);
    }

    @Override
    public Long addDisconnectForwardConnectionWithArgumentRequest(int customInvokeTimeout, Integer callSegmentID,
            CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.dFCWithArgument, req, true, false);        
    }

    @Override
    public Long addConnectToResourceRequest(CalledPartyNumberCapImpl resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

        return addConnectToResourceRequest(_Timer_Default, resourceAddress_IPRoutingAddress, resourceAddress_Null, extensions,
                serviceInteractionIndicatorsTwo, callSegmentID);
    }

    @Override
    public Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberCapImpl resourceAddress_IPRoutingAddress,
            boolean resourceAddress_Null, CAPExtensionsImpl extensions,
            ServiceInteractionIndicatorsTwoImpl serviceInteractionIndicatorsTwo, Integer callSegmentID) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.connectToResource, req, true, false);        
    }

    @Override
    public Long addResetTimerRequest(TimerID timerID, int timerValue, CAPExtensionsImpl extensions, Integer callSegmentID)
            throws CAPException {

        return addResetTimerRequest(_Timer_Default, timerID, timerValue, extensions, callSegmentID);
    }

    @Override
    public Long addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPExtensionsImpl extensions,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.resetTimer, req, true, false);        
    }

    @Override
    public Long addFurnishChargingInformationRequest(FCIBCCCAMELSequence1Impl FCIBCCCAMELsequence1) throws CAPException {

        return addFurnishChargingInformationRequest(_Timer_Default, FCIBCCCAMELsequence1);
    }

    @Override
    public Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1Impl FCIBCCCAMELsequence1)
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.furnishChargingInformation, req, true, false);        
    }

    @Override
    public Long addSendChargingInformationRequest(SCIBillingChargingCharacteristicsImpl sciBillingChargingCharacteristics,
            SendingLegIDImpl partyToCharge, CAPExtensionsImpl extensions) throws CAPException {

        return addSendChargingInformationRequest(_Timer_Default, sciBillingChargingCharacteristics, partyToCharge, extensions);
    }

    @Override
    public Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristicsImpl sciBillingChargingCharacteristics, SendingLegIDImpl partyToCharge,
            CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.sendChargingInformation, req, true, false);        
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV23(Long linkedId) throws CAPException {

        return addSpecializedResourceReportRequest_CapV23(linkedId, _Timer_Default);
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV4(Long linkedId, boolean isAllAnnouncementsComplete,
            boolean isFirstAnnouncementStarted) throws CAPException {

        return addSpecializedResourceReportRequest_CapV4(linkedId, _Timer_Default, isAllAnnouncementsComplete,
                isFirstAnnouncementStarted);
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV23(Long linkedId, int customInvokeTimeout) throws CAPException {

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
        
        SpecializedResourceReportRequestImpl req = new SpecializedResourceReportRequestImpl(false,false);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.specializedResourceReport, req, true, false);        
    }

    @Override
    public Long addSpecializedResourceReportRequest_CapV4(Long linkedId, int customInvokeTimeout,
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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.specializedResourceReport, req, true, false);        
    }

    @Override
    public Long addPlayAnnouncementRequest(InformationToSendImpl informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPExtensionsImpl extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPlayAnnouncementRequest(_Timer_Default, informationToSend, disconnectFromIPForbidden,
                requestAnnouncementCompleteNotification, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSendImpl informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, CAPExtensionsImpl extensions,
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
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.playAnnouncement, req, true, false);        
    }

    @Override
    public Long addPromptAndCollectUserInformationRequest(CollectedInfoImpl collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSendImpl informationToSend, CAPExtensionsImpl extensions, Integer callSegmentID,
            Boolean requestAnnouncementStartedNotification) throws CAPException {

        return addPromptAndCollectUserInformationRequest(_Timer_Default, collectedInfo, disconnectFromIPForbidden,
                informationToSend, extensions, callSegmentID, requestAnnouncementStartedNotification);
    }

    @Override
    public Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfoImpl collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSendImpl informationToSend, CAPExtensionsImpl extensions,
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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.promptAndCollectUserInformation, req, true, false);        
    }

    @Override
    public void addPromptAndCollectUserInformationResponse_DigitsResponse(long invokeId, DigitsImpl  digitsResponse)
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
        this.sendDataComponent(invokeId, null, null, null, (long) CAPOperationCode.promptAndCollectUserInformation, res, false, true);        
    }

    @Override
    public Long addCancelRequest_InvokeId(Integer invokeID) throws CAPException {

        return addCancelRequest_InvokeId(_Timer_Default, invokeID);
    }

    @Override
    public Long addCancelRequest_AllRequests() throws CAPException {

        return addCancelRequest_AllRequests(_Timer_Default);
    }

    @Override
    public Long addCancelRequest_CallSegmentToCancel(CallSegmentToCancelImpl callSegmentToCancel) throws CAPException {

        return addCancelRequest_CallSegmentToCancel(_Timer_Default, callSegmentToCancel);
    }

    @Override
    public Long addCancelRequest_InvokeId(int customInvokeTimeout, Integer invokeID) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(invokeID);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Long addCancelRequest_AllRequests(int customInvokeTimeout) throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(true);
        return addCancelRequest(customInvokeTimeout, req);
    }

    @Override
    public Long addCancelRequest_CallSegmentToCancel(int customInvokeTimeout, CallSegmentToCancelImpl callSegmentToCancel)
            throws CAPException {

        CancelRequestImpl req = new CancelRequestImpl(callSegmentToCancel);
        return addCancelRequest(customInvokeTimeout, req);
    }

    private Long addCancelRequest(int customInvokeTimeout, CancelRequestImpl req) throws CAPException {

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
        
        return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(), (long) CAPOperationCode.cancelCode, req, true, false);        
    }

    @Override
    public Long addDisconnectLegRequest(LegIDImpl logToBeReleased, CauseCapImpl releaseCause, CAPExtensionsImpl extensions)
            throws CAPException {
        return addDisconnectLegRequest(_Timer_Default, logToBeReleased, releaseCause, extensions);
    }

    @Override
    public Long addDisconnectLegRequest(int customInvokeTimeout, LegIDImpl logToBeReleased, CauseCapImpl releaseCause,
            CAPExtensionsImpl extensions) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.disconnectLeg, req, true, false);        
    }

    @Override
    public void addDisconnectLegResponse(long invokeId) throws CAPException {
        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException("Bad application context name for addDisconnectLegResponse: must be " + "CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        this.sendDataComponent(invokeId, null, null, null, (long) CAPOperationCode.disconnectLeg, null, false, true);        
    }

    @Override
    public Long addInitiateCallAttemptRequest(DestinationRoutingAddressImpl   destinationRoutingAddress,
            CAPExtensionsImpl extensions, LegIDImpl legToBeCreated, Integer newCallSegment,
            CallingPartyNumberCapImpl  callingPartyNumber, CallReferenceNumberImpl  callReferenceNumber,
            ISDNAddressStringImpl    gsmSCFAddress, boolean suppressTCsi) throws CAPException {
        return addInitiateCallAttemptRequest(_Timer_Default, destinationRoutingAddress, extensions, legToBeCreated,
                newCallSegment, callingPartyNumber, callReferenceNumber, gsmSCFAddress, suppressTCsi);
    }

    @Override
    public Long addInitiateCallAttemptRequest(int customInvokeTimeout,
            DestinationRoutingAddressImpl   destinationRoutingAddress, CAPExtensionsImpl extensions, LegIDImpl legToBeCreated,
            Integer newCallSegment, CallingPartyNumberCapImpl  callingPartyNumber, CallReferenceNumberImpl  callReferenceNumber,
            ISDNAddressStringImpl    gsmSCFAddress, boolean suppressTCsi) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.initiateCallAttempt, req, true, false);        
    }

    @Override
    public void addInitiateCallAttemptResponse(long invokeId, SupportedCamelPhasesImpl supportedCamelPhases,
            OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities, CAPExtensionsImpl extensions,
            boolean releaseCallArgExtensionAllowed) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addInitiateCallAttemptResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        InitiateCallAttemptResponseImpl res = new InitiateCallAttemptResponseImpl(supportedCamelPhases,
                offeredCamel4Functionalities, extensions, releaseCallArgExtensionAllowed);
        this.sendDataComponent(invokeId, null, null, null, (long) CAPOperationCode.initiateCallAttempt, res, false, true);
    }

    @Override
    public Long addMoveLegRequest(LegIDImpl logIDToMove, CAPExtensionsImpl extensions) throws CAPException {
        return addMoveLegRequest(_Timer_Default, logIDToMove, extensions);
    }

    @Override
    public Long addMoveLegRequest(int customInvokeTimeout, LegIDImpl logIDToMove, CAPExtensionsImpl extensions)
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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.moveLeg, req, true, false);        
    }

    @Override
    public void addMoveLegResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addMoveLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        this.sendDataComponent(invokeId, null, null, null, (long) CAPOperationCode.moveLeg, null, false, true);        
    }

    @Override
    public Long addCollectInformationRequest() throws CAPException {

        return addCollectInformationRequest(_Timer_Default);
    }

    @Override
    public Long addCollectInformationRequest(int customInvokeTimeout) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context name for addContinueRequest: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getTimerCircuitSwitchedCallControlShort();
        else
        	customTimeout = customInvokeTimeout;
        
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.collectInformation, null, true, false);        
    }

    public Long addSplitLegRequest(LegIDImpl legIDToSplit, Integer newCallSegmentId, CAPExtensionsImpl extensions)
            throws CAPException {
        return addSplitLegRequest(_Timer_Default, legIDToSplit, newCallSegmentId, extensions);
    }

    public Long addSplitLegRequest(int customInvokeTimeout, LegIDImpl legIDToSplit, Integer newCallSegmentId,
            CAPExtensionsImpl extensions) throws CAPException {
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
        return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(), (long) CAPOperationCode.splitLeg, req, true, false);        
    }

    public void addSplitLegResponse(long invokeId) throws CAPException {

        if (this.appCntx != CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                && this.appCntx != CAPApplicationContext.CapV4_scf_gsmSSFGeneric)
            throw new CAPException(
                    "Bad application context for addSplitLegResponse: must be CapV4_gsmSSF_scfGeneric or CapV4_scf_gsmSSFGeneric");

        this.sendDataComponent(invokeId, null, null, null, (long) CAPOperationCode.splitLeg, null, false, true);        
    }

    @Override
    public Long addCallGapRequest(GapCriteriaImpl gapCriteria, GapIndicatorsImpl gapIndicators, ControlType controlType, GapTreatmentImpl gapTreatment, CAPExtensionsImpl capExtension) throws CAPException {
        return addCallGapRequest(_Timer_Default, gapCriteria, gapIndicators, controlType, gapTreatment, capExtension);
    }

    @Override
    public Long addCallGapRequest(int customInvokeTimeout, GapCriteriaImpl gapCriteria, GapIndicatorsImpl gapIndicators, ControlType controlType, GapTreatmentImpl gapTreatment, CAPExtensionsImpl capExtension) throws CAPException {

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
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) CAPOperationCode.callGap, req, true, false);        
    }
}
