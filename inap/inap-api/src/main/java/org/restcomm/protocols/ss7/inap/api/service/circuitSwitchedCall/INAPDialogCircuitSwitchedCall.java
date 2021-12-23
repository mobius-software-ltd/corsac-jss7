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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AChChargingAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FeatureRequestIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringTimeOut;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ForwardingCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.HoldCause;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResponseCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceProfileIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

/**
 *
 * @author yulian.oifa
 *
 */
public interface INAPDialogCircuitSwitchedCall extends INAPDialog {

    Long addInitialDPRequest(int serviceKey, CallingPartyNumberIsup dialledDigits, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartyBusinessGroupID callingPartyBusinessGroupID, 
            CallingPartysCategoryIsup callingPartysCategory, CallingPartySubaddress callingPartySubaddress,
            CGEncountered CGEncountered, IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable,
            LocationNumberIsup locationNumber, MiscCallInfo miscCallInfo, OriginalCalledNumberIsup originalCalledPartyID, 
            ServiceProfileIdentifier serviceProfileIdentifier,TerminalType terminalType,CAPINAPExtensions extensions,
            TriggerType triggerType, HighLayerCompatibilityIsup highLayerCompatibility, 
            ServiceInteractionIndicators serviceInteractionIndicators,DigitsIsup additionalCallingPartyNumber,
            ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, 
            RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
            throws INAPException;

    Long addInitialDPRequest(int customInvokeTimeout, int serviceKey, CallingPartyNumberIsup dialledDigits, 
    		CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber, 
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,  CallingPartysCategoryIsup callingPartysCategory, 
    		CallingPartySubaddress callingPartySubaddress, CGEncountered CGEncountered, IPSSPCapabilities ipsspCapabilities, 
    		IPAvailable ipAvailable, LocationNumberIsup locationNumber, MiscCallInfo miscCallInfo, 
    		OriginalCalledNumberIsup originalCalledPartyID,  ServiceProfileIdentifier serviceProfileIdentifier,
    		TerminalType terminalType, CAPINAPExtensions extensions, TriggerType triggerType, 
    		HighLayerCompatibilityIsup highLayerCompatibility,  ServiceInteractionIndicators serviceInteractionIndicators,
    		DigitsIsup additionalCallingPartyNumber, ForwardCallIndicators forwardCallIndicators, 
    		BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    Long addApplyChargingReportRequest(byte[] callResult) throws INAPException;

    Long addApplyChargingReportRequest(int customInvokeTimeout, byte[] callResult) throws INAPException;

    Long addApplyChargingRequest(CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics,
            LegType partyToCharge, CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws INAPException;

    Long addApplyChargingRequest(int customInvokeTimeout,
            CAMELAChBillingChargingCharacteristics aChBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) throws INAPException;

    Long addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
    		DigitsIsup correlationID,CAPINAPExtensions extensions) throws INAPException;

    Long addCallInformationReportRequest(int customInvokeTimeout,List<RequestedInformation> requestedInformationList, 
    		DigitsIsup correlationID,CAPINAPExtensions extensions) throws INAPException;

    Long addCallInformationRequest(List<RequestedInformationType> requestedInformationTypeList,
    		DigitsIsup correlationID,CAPINAPExtensions extensions) throws INAPException;

    Long addCallInformationRequest(int customInvokeTimeout,List<RequestedInformationType> requestedInformationTypeList, 
    		DigitsIsup correlationID,CAPINAPExtensions extensions)
            throws INAPException;

    Long addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPatternWrapper alertingPattern,
    		DigitsIsup correlationID, Integer cutAndPaste, ForwardingCondition forwardingCondition, 
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList, ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    Long addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress, 
    		AlertingPatternWrapper alertingPattern, DigitsIsup correlationID, Integer cutAndPaste, 
    		ForwardingCondition forwardingCondition, ISDNAccessRelatedInformation isdnAccessRelatedInformation, 
    		OriginalCalledNumberIsup originalCalledPartyID, RouteList routeList, ScfID scfID, 
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    Long addContinueRequest() throws INAPException;

    Long addContinueRequest(int customInvokeTimeout) throws INAPException;

    Long addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM, DigitsIsup bcsmEventCorrelationID,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Long addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,  DigitsIsup bcsmEventCorrelationID,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegType legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Long addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, DigitsIsup bcsmEventCorrelationID, 
    		CAPINAPExtensions extensions) throws INAPException;

    Long addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList, 
    		DigitsIsup bcsmEventCorrelationID, CAPINAPExtensions extensions) throws INAPException;

    Long addReleaseCallRequest(CauseIsup cause) throws INAPException;

    Long addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws INAPException;

    Long addActivityTestRequest() throws INAPException;

    Long addActivityTestRequest(int customInvokeTimeout) throws INAPException;

    void addActivityTestResponse(long invokeId) throws INAPException;

    Long addAssistRequestInstructionsRequest(DigitsIsup correlationID, IPAvailable ipAvailable,
    		IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException;

    Long addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup correlationID,
    		IPAvailable ipAvailable,IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, 
    		LegID legID,ScfID scfID,CAPINAPExtensions extensions, Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, LegID legID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addDisconnectForwardConnectionRequest() throws INAPException;

    Long addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws INAPException;

    Long addConnectToResourceRequest(CalledPartyNumberIsup ipRoutingAddress, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(LegID legID, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(ResourceAddress resourceAddress, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup ipRoutingAddress, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, LegID legID, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, ResourceAddress resourceAddress, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addConnectToResourceRequest(int customInvokeTimeout, CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Long addFurnishChargingInformationRequest(FCIBCCCAMELSequence1 FCIBCCCAMELsequence1) throws INAPException;

    Long addFurnishChargingInformationRequest(int customInvokeTimeout, FCIBCCCAMELSequence1 FCIBCCCAMELsequence1)
            throws INAPException;

    Long addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Long addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws INAPException;

    Long addSpecializedResourceReportRequest(Long linkedId) throws INAPException;

    Long addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException;

    Long addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, 
            CAPINAPExtensions extensions) throws INAPException;

    Long addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    Long addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    void addPromptAndCollectUserInformationResponse(long invokeId, DigitsIsup digitsResponse)
            throws INAPException;
    
    void addPromptAndCollectUserInformationResponse(long invokeId, String ia5Response)
            throws INAPException;

    Long addCancelRequest(Integer invokeID) throws INAPException;

    Long addCancelRequest() throws INAPException;

    Long addCancelRequest(int customInvokeTimeout, Integer invokeID) throws INAPException;

    Long addCancelRequest(int customInvokeTimeout) throws INAPException;

    Long addInitiateCallAttemptRequest(DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPatternWrapper alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber)
            throws INAPException;

    Long addInitiateCallAttemptRequest(int customInvokeTimeout,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPatternWrapper alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber)
            throws INAPException;
    
    Long addCollectInformationRequest(AlertingPattern alertingPattern,NumberingPlan numberingPlan,
    		OriginalCalledPartyIDIsup originalCalledPartyID,LocationNumberIsup travellingClassMark,CAPINAPExtensions extensions,
    		CallingPartyNumberIsup сallingPartyNumber,CalledPartyNumberIsup dialledDigits) throws INAPException;

    Long addCollectInformationRequest(int customInvokeTimeout,AlertingPattern alertingPattern,NumberingPlan numberingPlan,
    		OriginalCalledPartyIDIsup originalCalledPartyID,LocationNumberIsup travellingClassMark,
    	    CAPINAPExtensions extensions,CallingPartyNumberIsup сallingPartyNumber,CalledPartyNumberIsup dialledDigits) throws INAPException;

    Long addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators,
    		ControlType controlType, GapTreatment gapTreatment,CAPINAPExtensions capExtensions) throws INAPException;

    Long addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria,
    		GapIndicators gapIndicators, ControlType controlType,GapTreatment gapTreatment, 
    		CAPINAPExtensions capExtensions) throws INAPException;

    Long addActivateServiceFiltering(FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions) throws INAPException;

    Long addActivateServiceFiltering(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions) throws INAPException;
    
    Long addEventNotificationCharging(byte[] eventTypeCharging,  byte[] eventSpecificInformationCharging, 
    		LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException;

    Long addEventNotificationCharging(int customInvokeTimeout,byte[] eventTypeCharging, 
    		byte[] eventSpecificInformationCharging, LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException;
    
    Long addRequestNotificationChargingEvent(List<ChargingEvent> chargingEventList) throws INAPException;

    Long addRequestNotificationChargingEvent(int customInvokeTimeout,List<ChargingEvent> chargingEventList) throws INAPException;
    
    Long addServiceFilteringResponse(long invokeId,List<CounterAndValue> counterAndValue, FilteringCriteria filteringCriteria, 
    		CAPINAPExtensions extensions, ResponseCondition responseCondition) throws INAPException;

    Long addAnalysedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
    		LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;

    Long addAnalysedInformationRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;
    
    Long addAnalyseInformationRequest(DestinationRoutingAddress destinationRoutingAddress,AlertingPatternWrapper alertingPattern,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID,
    		CAPINAPExtensions extensions,CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		CalledPartyNumberIsup calledPartyNumber, LocationNumberIsup chargeNumber, LocationNumberIsup travellingClassMark,
    		Carrier carrier) throws INAPException;

    Long addAnalyseInformationRequest(int customInvokeTimeout,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPatternWrapper alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,
    		CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		CalledPartyNumberIsup calledPartyNumber, LocationNumberIsup chargeNumber, 
    		LocationNumberIsup travellingClassMark,Carrier carrier) throws INAPException;
    
    Long addCollectedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
    		LocationNumberIsup accessCode,Carrier carrier) throws INAPException;

    Long addCollectedInformationRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
			LocationNumberIsup accessCode,Carrier carrier) throws INAPException;
    
    Long addHoldCallInNetworkRequest(HoldCause holdCause) throws INAPException;

    Long addHoldCallInNetworkRequest(int customInvokeTimeout,HoldCause holdCause) throws INAPException;    
    
    Long addHoldCallInNetworkRequest() throws INAPException;

    Long addHoldCallInNetworkRequest(int customInvokeTimeout) throws INAPException;    
    
    Long addOMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addOMidCallRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FeatureRequestIndicator featureRequestIndicator,CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Long addTMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addTMidCallRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FeatureRequestIndicator featureRequestIndicator,CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Long addOAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation, RouteList routeList, LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions) throws INAPException;

    Long addOAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID, 
    		RedirectingPartyIDIsup redirectingPartyID,  RedirectionInformationIsup redirectionInformation, 
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Long addOriginationAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions, Carrier carrier) throws INAPException;

    Long addOriginationAttemptAuthorizedRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember, LocationNumberIsup travellingClassMark, 
			CAPINAPExtensions extensions, Carrier carrier) throws INAPException;        
    
    Long addRouteSelectFailureRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,CauseIsup failureCause,
    		OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation,RouteList routeList, LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions,LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;

    Long addRouteSelectFailureRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,CauseIsup getFailureCause,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, 
			RedirectionInformationIsup redirectionInformation, RouteList routeList, LocationNumberIsup travellingClassMark, 
			CAPINAPExtensions extensions,LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;
    
    Long addOCalledPartyBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addOCalledPartyBusyRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;        
    
    Long addONoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addONoAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Long addODisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		CauseIsup releaseCause, RouteList routeList, CAPINAPExtensions extensions,Integer connectTime) throws INAPException;

    Long addODisconnectRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			CauseIsup releaseCause, RouteList routeList, CAPINAPExtensions extensions,Integer connectTime) throws INAPException;
    
    Long addTermAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Long addTermAttemptAuthorizedRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,OriginalCalledNumberIsup originalCalledPartyID,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,RouteList routeList, 
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Long addTBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Long addTBusyRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Long addTNoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Long addTNoAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Long addTAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,CAPINAPExtensions extensions) throws INAPException;

    Long addTAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,CAPINAPExtensions extensions) throws INAPException;
    
    Long addTDisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,CauseIsup releaseCause,CAPINAPExtensions extensions,Integer connectTime) throws INAPException;

    Long addTDisconnectRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,CauseIsup releaseCause,
    		CAPINAPExtensions extensions,Integer connectTime) throws INAPException;
    
    Long addSelectRouteRequest(CalledPartyNumberIsup destinationNumberRoutingAddress,AlertingPatternWrapper alertingPattern,
    		DigitsIsup correlationID,ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID,
    		RouteList routeList,ScfID scfID,LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addSelectRouteRequest(int customInvokeTimeout,CalledPartyNumberIsup destinationNumberRoutingAddress,
    		AlertingPatternWrapper alertingPattern,DigitsIsup correlationID,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		OriginalCalledNumberIsup originalCalledPartyID,RouteList routeList,ScfID scfID,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;       
    
    Long addSelectFacilityRequest(CalledPartyNumberIsup destinationNumberRoutingAddress,AlertingPatternWrapper alertingPattern,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID, 
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Long addSelectFacilityRequest(int customInvokeTimeout,CalledPartyNumberIsup destinationNumberRoutingAddress,
    		AlertingPatternWrapper alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		CAPINAPExtensions extensions) throws INAPException;
    
    Long addRequestCurrentStatusReportRequest(ResourceID resourceID) throws INAPException;

    Long addRequestCurrentStatusReportRequest(int customInvokeTimeout,ResourceID resourceID) throws INAPException;
    
    Long addRequestCurrentStatusReportResponse(long invokeId,ResourceStatus resourceStatus,ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;

    Long addCancelStatusReportRequest(ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;

    Long addCancelStatusReportRequest(int customInvokeTimeout,ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;
    	    
    Long addRequestEveryStatusChangeReportRequest(ResourceID resourceID,DigitsIsup correlationID,
    		Integer duration,CAPINAPExtensions extensions) throws INAPException;

    Long addRequestEveryStatusChangeReportRequest(int customInvokeTimeout,ResourceID resourceID,
    		DigitsIsup correlationID,Integer duration,CAPINAPExtensions extensions) throws INAPException;
    
    Long addRequestFirstStatusMatchReportRequest(ResourceID resourceID,ResourceStatus resourceStatus,DigitsIsup correlationID,
    		Integer duration,CAPINAPExtensions extensions,BearerCapability bearerCapability) throws INAPException;

    Long addRequestFirstStatusMatchReportRequest(int customInvokeTimeout,ResourceID resourceID,ResourceStatus resourceStatus,
    		DigitsIsup correlationID,Integer duration,CAPINAPExtensions extensions,BearerCapability bearerCapability) throws INAPException;
    
    Long addStatusReportRequest(ResourceStatus resourceStatus,DigitsIsup correlationID,ResourceID resourceID,
    		CAPINAPExtensions extensions,ReportCondition reportCondition) throws INAPException;

    Long addStatusReportRequest(int customInvokeTimeout,ResourceStatus resourceStatus,DigitsIsup correlationID,
    		ResourceID resourceID,CAPINAPExtensions extensions,ReportCondition reportCondition) throws INAPException;
}