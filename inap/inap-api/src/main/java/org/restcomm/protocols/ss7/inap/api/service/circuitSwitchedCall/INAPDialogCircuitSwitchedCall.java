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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ControlType;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapCriteria;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;
import org.restcomm.protocols.ss7.commonapp.api.gap.GapTreatment;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ApplicationID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CallResultCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericName;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LegIDs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LimitIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReceivingFunctionsRequested;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RouteOrigin;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SendingFunctionsActive;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResponseCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceProfileIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
public interface INAPDialogCircuitSwitchedCall extends INAPDialog {

	//cs1 flavour
    Integer addInitialDPRequest(int serviceKey, CallingPartyNumberIsup dialledDigits, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber, CallingPartyBusinessGroupID callingPartyBusinessGroupID, 
            CallingPartysCategoryIsup callingPartysCategory, CallingPartySubaddress callingPartySubaddress,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable,
            LocationNumberIsup locationNumber, MiscCallInfo miscCallInfo, OriginalCalledNumberIsup originalCalledPartyID, 
            ServiceProfileIdentifier serviceProfileIdentifier,TerminalType terminalType,CAPINAPExtensions extensions,
            TriggerType triggerType, HighLayerCompatibilityIsup highLayerCompatibility, 
            ServiceInteractionIndicators serviceInteractionIndicators,DigitsIsup additionalCallingPartyNumber,
            ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, 
            RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
            throws INAPException;

    Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CallingPartyNumberIsup dialledDigits, 
    		CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber, 
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,  CallingPartysCategoryIsup callingPartysCategory, 
    		CallingPartySubaddress callingPartySubaddress, CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, 
    		IPAvailable ipAvailable, LocationNumberIsup locationNumber, MiscCallInfo miscCallInfo, 
    		OriginalCalledNumberIsup originalCalledPartyID,  ServiceProfileIdentifier serviceProfileIdentifier,
    		TerminalType terminalType, CAPINAPExtensions extensions, TriggerType triggerType, 
    		HighLayerCompatibilityIsup highLayerCompatibility,  ServiceInteractionIndicators serviceInteractionIndicators,
    		DigitsIsup additionalCallingPartyNumber, ForwardCallIndicators forwardCallIndicators, 
    		BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    //cs1+ flavour
    Integer addInitialDPRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) throws INAPException;

    Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) throws INAPException;

    //CS1 Flavour
    Integer addApplyChargingReportRequest(ByteBuf callResult) throws INAPException;

    Integer addApplyChargingReportRequest(int customInvokeTimeout, ByteBuf callResult) throws INAPException;

    //CS1+ Flavour
    Integer addApplyChargingReportRequest(CallResultCS1 callResult) throws INAPException;

    Integer addApplyChargingReportRequest(int customInvokeTimeout, CallResultCS1 callResult) throws INAPException;

    //CS1 Flavour
    Integer addApplyChargingRequest(AChBillingChargingCharacteristics aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Integer addApplyChargingRequest(int customInvokeTimeout,
    		AChBillingChargingCharacteristics aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    //cs1+ flavour
    Integer addApplyChargingRequest(AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Integer addApplyChargingRequest(int customInvokeTimeout,
    		AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics,
    		Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    //CS1 Flavour
    Integer addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
    		DigitsIsup correlationID,CAPINAPExtensions extensions) throws INAPException;

    Integer addCallInformationReportRequest(int customInvokeTimeout,List<RequestedInformation> requestedInformationList, 
    		DigitsIsup correlationID,CAPINAPExtensions extensions) throws INAPException;

    //CS1+ Flavour
    Integer addCallInformationReportRequest(LegType legID,List<RequestedInformation> requestedInformationList,
    		CAPINAPExtensions extensions) throws INAPException;

    Integer addCallInformationReportRequest(int customInvokeTimeout,LegType legID,List<RequestedInformation> requestedInformationList, 
    		CAPINAPExtensions extensions) throws INAPException;

    //CS1 Flavour
    Integer addCallInformationRequest(List<RequestedInformationType> requestedInformationTypeList, DigitsIsup correlationID,
    		CAPINAPExtensions extensions) throws INAPException;

    Integer addCallInformationRequest(int customInvokeTimeout,List<RequestedInformationType> requestedInformationTypeList, 
    		DigitsIsup correlationID, CAPINAPExtensions extensions) throws INAPException;
    
    //CS1+ Flavour
    Integer addCallInformationRequest(LegType legID,List<RequestedInformationType> requestedInformationTypeList, CAPINAPExtensions extensions) throws INAPException;

    Integer addCallInformationRequest(int customInvokeTimeout,LegType legID,List<RequestedInformationType> requestedInformationTypeList, 
    		CAPINAPExtensions extensions)
            throws INAPException;

    //CS1 Flavour
    Integer addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
    		DigitsIsup correlationID, Integer cutAndPaste, ForwardingCondition forwardingCondition, 
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList, ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    Integer addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress, 
    		AlertingPattern alertingPattern, DigitsIsup correlationID, Integer cutAndPaste, 
    		ForwardingCondition forwardingCondition, ISDNAccessRelatedInformation isdnAccessRelatedInformation, 
    		OriginalCalledNumberIsup originalCalledPartyID, RouteList routeList, ScfID scfID, 
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    //CS1+ Flavour
    Integer addConnectRequest(LegType legToBeCreated,BearerCapability bearerCapabilities,CUGCallIndicator cugCallIndicator,
    		CUGInterLockCode cugInterLockCode,ForwardCallIndicatorsIsup forwardCallIndicators,GenericDigitsSet genericDigitsSet,
    		GenericNumbersSet genericNumberSet,HighLayerCompatibilityIsup highLayerCompatibility,ForwardGVNSIsup forwardGVNSIndicator,
    		DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
    		DigitsIsup correlationID, Integer cutAndPaste, OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList, ScfID scfID, CAPINAPExtensions extensions, 
    		Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    Integer addConnectRequest(int customInvokeTimeout, LegType legToBeCreated,BearerCapability bearerCapabilities,
    		CUGCallIndicator cugCallIndicator,CUGInterLockCode cugInterLockCode,
    		ForwardCallIndicatorsIsup forwardCallIndicators,GenericDigitsSet genericDigitsSet,
    		GenericNumbersSet genericNumberSet,HighLayerCompatibilityIsup highLayerCompatibility,ForwardGVNSIsup forwardGVNSIndicator,
    		DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern, 
    		DigitsIsup correlationID, Integer cutAndPaste, OriginalCalledNumberIsup originalCalledPartyID, 
    		RouteList routeList, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation) throws INAPException;

    //CS1 Flavour
    Integer addContinueRequest() throws INAPException;

    Integer addContinueRequest(int customInvokeTimeout) throws INAPException;

    //CS1+ Flavour
    Integer addContinueRequest(LegType legID) throws INAPException;

    Integer addContinueRequest(int customInvokeTimeout,LegType legID) throws INAPException;

    Integer addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM, DigitsIsup bcsmEventCorrelationID,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegID legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Integer addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,  DigitsIsup bcsmEventCorrelationID,
            EventSpecificInformationBCSM eventSpecificInformationBCSM, LegID legID, MiscCallInfo miscCallInfo,
            CAPINAPExtensions extensions) throws INAPException;

    Integer addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, DigitsIsup bcsmEventCorrelationID, 
    		CAPINAPExtensions extensions) throws INAPException;

    Integer addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList, 
    		DigitsIsup bcsmEventCorrelationID, CAPINAPExtensions extensions) throws INAPException;

    Integer addReleaseCallRequest(CauseIsup cause) throws INAPException;

    Integer addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws INAPException;

    Integer addActivityTestRequest() throws INAPException;

    Integer addActivityTestRequest(int customInvokeTimeout) throws INAPException;

    void addActivityTestResponse(int invokeId) throws INAPException;

    Integer addAssistRequestInstructionsRequest(DigitsIsup correlationID, IPAvailable ipAvailable,
    		IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException;

    Integer addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup correlationID,
    		IPAvailable ipAvailable,IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException;

    //CS1 Flavour
    Integer addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, 
    		LegID legID,ScfID scfID,CAPINAPExtensions extensions, Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
            DigitsIsup correlationID, LegID legID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
            ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    //CS1+ Flavour
    Integer addEstablishTemporaryConnectionRequest(LegType LegID, DigitsIsup assistingSSPIPRoutingAddress, 
    		DigitsIsup correlationID,  ScfID scfID,CAPINAPExtensions extensions,Carrier carrier, 
    		ServiceInteractionIndicators serviceInteractionIndicators, RouteList routeList) throws INAPException;

    Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, LegType LegID, 
    		DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID, 
    		CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators, 
    		RouteList routeList) throws INAPException;

    //CS1 Flavour
    Integer addDisconnectForwardConnectionRequest() throws INAPException;

    Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws INAPException;

    //CS1+ Flavour
    Integer addDisconnectForwardConnectionRequest(LegType legID) throws INAPException;

    Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout,LegType legID) throws INAPException;

    Integer addConnectToResourceRequest(CalledPartyNumberIsup ipRoutingAddress, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(LegType legID, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(ResourceAddress resourceAddress, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(boolean none, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup ipRoutingAddress, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(int customInvokeTimeout, LegType legID, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(int customInvokeTimeout, ResourceAddress resourceAddress, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addConnectToResourceRequest(int customInvokeTimeout, boolean none, 
    		CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException;

    Integer addFurnishChargingInformationRequest(ByteBuf FCIBCCCAMELsequence1) throws INAPException;

    Integer addFurnishChargingInformationRequest(int customInvokeTimeout, ByteBuf FCIBCCCAMELsequence1)
            throws INAPException;

    //cs1 flavour
    Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Integer addSendChargingInformationRequest(int customInvokeTimeout,
            SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws INAPException;

    //cs1+ flavour
    Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException;

    Integer addSendChargingInformationRequest(int customInvokeTimeout,
    		SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics, LegType partyToCharge,
            CAPINAPExtensions extensions) throws INAPException;

    //cs1 flavour
    Integer addSpecializedResourceReportRequest(Integer linkedId) throws INAPException;

    Integer addSpecializedResourceReportRequest(int customInvokeTimeout, Integer linkedId) throws INAPException;

    //cs1+ flavour
    Integer addSpecializedResourceReportRequest(Integer linkedId,boolean value,boolean isStarted) throws INAPException;

    Integer addSpecializedResourceReportRequest(int customInvokeTimeout, Integer linkedId,boolean value,boolean isStarted) throws INAPException;

    //cs1 flavour
    Integer addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException;

    Integer addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
            Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, 
            CAPINAPExtensions extensions) throws INAPException;

    //cs1+ flavour
    Integer addPlayAnnouncementRequest(LegType legID,Boolean requestAnnouncementStarted,
    		InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
            Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException;

    Integer addPlayAnnouncementRequest(int customInvokeTimeout,LegType legID,Boolean requestAnnouncementStarted,
    		InformationToSend informationToSend, Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification, 
            CAPINAPExtensions extensions) throws INAPException;

    //cs1 flavour
    Integer addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    //cs1+ flavour
    Integer addPromptAndCollectUserInformationRequest(LegType legID,Boolean requestAnnouncementStarted,
    		Boolean requestAnnouncementComplete,CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
            InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, LegType legID,
    		Boolean requestAnnouncementStarted,Boolean requestAnnouncementComplete,CollectedInfo collectedInfo,
            Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException;

    void addPromptAndCollectUserInformationResponse(int invokeId, DigitsIsup digitsResponse)
            throws INAPException;
    
    void addPromptAndCollectUserInformationResponse(int invokeId, String ia5Response)
            throws INAPException;

    Integer addCancelRequest(Integer invokeID) throws INAPException;

    Integer addCancelRequest() throws INAPException;

    Integer addCancelRequest(int customInvokeTimeout, Integer invokeID) throws INAPException;

    Integer addCancelRequest(int customInvokeTimeout) throws INAPException;

    //cs1 flavour
    Integer addInitiateCallAttemptRequest(DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPattern alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber)
            throws INAPException;

    Integer addInitiateCallAttemptRequest(int customInvokeTimeout,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPattern alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber)
            throws INAPException;
        
    //cs1+ flavour
    Integer addInitiateCallAttemptRequest(OriginalCalledNumberIsup originalCalledPartyID,LegType legToBeCreated,
    		CallingPartysCategoryIsup callingPartysCategory,RedirectingPartyIDIsup redirectingPartyID,
    		RedirectionInformationIsup redirectionInformation,BearerCapability nearerCapability,
    		CUGCallIndicator cugCallIndicator,CUGInterLockCode cugInterLockCode,ForwardCallIndicators forwardCallIndicators,
    		GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,HighLayerCompatibilityIsup highLayerCompatibility,
    		ForwardGVNSIsup forwardGVNSIndicator,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPattern alertingPattern,CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber,
    		RouteList routeList) throws INAPException;

    Integer addInitiateCallAttemptRequest(int customInvokeTimeout,OriginalCalledNumberIsup originalCalledPartyID,
    		LegType legToBeCreated,CallingPartysCategoryIsup callingPartysCategory,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		BearerCapability nearerCapability,CUGCallIndicator cugCallIndicator,CUGInterLockCode cugInterLockCode,
    		ForwardCallIndicators forwardCallIndicators,GenericDigitsSet genericDigitsSet,
    		GenericNumbersSet genericNumberSet,HighLayerCompatibilityIsup highLayerCompatibility,
    		ForwardGVNSIsup forwardGVNSIndicator,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPattern alertingPattern, CAPINAPExtensions extensions, 
    		ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber,
    		RouteList routeList) throws INAPException;

    Integer addCollectInformationRequest(AlertingPattern alertingPattern,NumberingPlan numberingPlan,
    		OriginalCalledPartyIDIsup originalCalledPartyID,LocationNumberIsup travellingClassMark,CAPINAPExtensions extensions,
    		CallingPartyNumberIsup сallingPartyNumber,CalledPartyNumberIsup dialledDigits) throws INAPException;

    Integer addCollectInformationRequest(int customInvokeTimeout,AlertingPattern alertingPattern,NumberingPlan numberingPlan,
    		OriginalCalledPartyIDIsup originalCalledPartyID,LocationNumberIsup travellingClassMark,
    	    CAPINAPExtensions extensions,CallingPartyNumberIsup сallingPartyNumber,CalledPartyNumberIsup dialledDigits) throws INAPException;

    //CS1 Flavour
    Integer addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators,
    		ControlType controlType, GapTreatment gapTreatment,CAPINAPExtensions capExtensions) throws INAPException;

    Integer addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria,
    		GapIndicators gapIndicators, ControlType controlType,GapTreatment gapTreatment, 
    		CAPINAPExtensions capExtensions) throws INAPException;

   //CS1+ Flavour
    Integer addCallGapRequest(DateAndTime startTime,GapCriteria gapCriteria, GapIndicators gapIndicators,
    		ControlType controlType, GapTreatment gapTreatment,CAPINAPExtensions capExtensions) throws INAPException;

    Integer addCallGapRequest(int customInvokeTimeout, DateAndTime startTime, GapCriteria gapCriteria,
    		GapIndicators gapIndicators, ControlType controlType,GapTreatment gapTreatment, 
    		CAPINAPExtensions capExtensions) throws INAPException;

    //CS1 Flavour
    Integer addActivateServiceFilteringRequest(FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions) throws INAPException;

    Integer addActivateServiceFilteringRequest(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions) throws INAPException;
    
    //CS1+ Flavour
    Integer addActivateServiceFilteringRequest(FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions,
    		ByteBuf scfCorrelationInfo) throws INAPException;

    Integer addActivateServiceFilteringRequest(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment, 
    		FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut, 
    		FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions,
    		ByteBuf scfCorrelationInfo) throws INAPException;
    
    Integer addEventNotificationCharging(ByteBuf eventTypeCharging,  ByteBuf eventSpecificInformationCharging, 
    		LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException;

    Integer addEventNotificationCharging(int customInvokeTimeout,ByteBuf eventTypeCharging, 
    		ByteBuf eventSpecificInformationCharging, LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException;
    
    Integer addRequestNotificationChargingEvent(List<ChargingEvent> chargingEventList) throws INAPException;

    Integer addRequestNotificationChargingEvent(int customInvokeTimeout,List<ChargingEvent> chargingEventList) throws INAPException;
    
    //cs1 flavour
    Integer addServiceFilteringResponseRequest(List<CounterAndValue> counterAndValue, FilteringCriteria filteringCriteria, 
    		CAPINAPExtensions extensions, ResponseCondition responseCondition) throws INAPException;

    Integer addServiceFilteringResponseRequest(int customInvokeTimeout,List<CounterAndValue> counterAndValue, FilteringCriteria filteringCriteria, 
    		CAPINAPExtensions extensions, ResponseCondition responseCondition) throws INAPException;

    //cs1+ flavour
    Integer addServiceFilteringResponseRequest(List<CounterAndValue> counterAndValue, FilteringCriteria filteringCriteria, 
    		ResponseCondition responseCondition,ByteBuf scfCorrelationInfo) throws INAPException;

    Integer addServiceFilteringResponseRequest(int customInvokeTimeout,List<CounterAndValue> counterAndValue, 
    		FilteringCriteria filteringCriteria, ResponseCondition responseCondition,ByteBuf scfCorrelationInfo) throws INAPException;

    Integer addAnalysedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
    		LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;

    Integer addAnalysedInformationRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			LocationNumberIsup featureCode,LocationNumberIsup accessCode,Carrier carrier) throws INAPException;
    
    Integer addAnalyseInformationRequest(DestinationRoutingAddress destinationRoutingAddress,AlertingPattern alertingPattern,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID,
    		CAPINAPExtensions extensions,CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		CalledPartyNumberIsup calledPartyNumber, LocationNumberIsup chargeNumber, LocationNumberIsup travellingClassMark,
    		Carrier carrier) throws INAPException;

    Integer addAnalyseInformationRequest(int customInvokeTimeout,DestinationRoutingAddress destinationRoutingAddress,
    		AlertingPattern alertingPattern,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,
    		CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
    		CalledPartyNumberIsup calledPartyNumber, LocationNumberIsup chargeNumber, 
    		LocationNumberIsup travellingClassMark,Carrier carrier) throws INAPException;
    
    Integer addCollectedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
    		LocationNumberIsup accessCode,Carrier carrier) throws INAPException;

    Integer addCollectedInformationRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
			LocationNumberIsup accessCode,Carrier carrier) throws INAPException;
    
    Integer addHoldCallInNetworkRequest(HoldCause holdCause) throws INAPException;

    Integer addHoldCallInNetworkRequest(int customInvokeTimeout,HoldCause holdCause) throws INAPException;    
    
    Integer addHoldCallInNetworkRequest() throws INAPException;

    Integer addHoldCallInNetworkRequest(int customInvokeTimeout) throws INAPException;    
    
    Integer addOMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addOMidCallRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FeatureRequestIndicator featureRequestIndicator,CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Integer addTMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addTMidCallRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FeatureRequestIndicator featureRequestIndicator,CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Integer addOAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation, RouteList routeList, LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions) throws INAPException;

    Integer addOAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID, 
    		RedirectingPartyIDIsup redirectingPartyID,  RedirectionInformationIsup redirectionInformation, 
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Integer addOriginationAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions, Carrier carrier) throws INAPException;

    Integer addOriginationAttemptAuthorizedRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember, LocationNumberIsup travellingClassMark, 
			CAPINAPExtensions extensions, Carrier carrier) throws INAPException;        
    
    Integer addRouteSelectFailureRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,CauseIsup failureCause,
    		OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, 
    		RedirectionInformationIsup redirectionInformation,RouteList routeList, LocationNumberIsup travellingClassMark, 
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addRouteSelectFailureRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,CauseIsup getFailureCause,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, 
			RedirectionInformationIsup redirectionInformation, RouteList routeList, LocationNumberIsup travellingClassMark, 
			CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Integer addOCalledPartyBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addOCalledPartyBusyRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;        
    
    Integer addONoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addONoAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;
    
    Integer addODisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
    		FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember, CauseIsup releaseCause, 
    		RouteList routeList,CAPINAPExtensions extensions,Carrier carrier, Integer connectTime) throws INAPException;

    Integer addODisconnectRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID,CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup,	Integer callingFacilityGroupMember,
			CauseIsup releaseCause, RouteList routeList, CAPINAPExtensions extensions,Carrier carrier,Integer connectTime) throws INAPException;
    
    Integer addTermAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Integer addTermAttemptAuthorizedRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		CallingPartyBusinessGroupID callingPartyBusinessGroupID,OriginalCalledNumberIsup originalCalledPartyID,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,RouteList routeList, 
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Integer addTBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Integer addTBusyRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,CauseIsup busyCause,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Integer addTNoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;

    Integer addTNoAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException;
    
    Integer addTAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,CAPINAPExtensions extensions) throws INAPException;

    Integer addTAnswerRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,CAPINAPExtensions extensions) throws INAPException;
    
    Integer addTDisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress, FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,CauseIsup releaseCause,CAPINAPExtensions extensions,Integer connectTime) throws INAPException;

    Integer addTDisconnectRequest(int customInvokeTimeout,DpSpecificCommonParameters dpSpecificCommonParameters,
    		CalledPartyBusinessGroupID calledPartyBusinessGroupID,CalledPartySubaddress calledPartySubaddress, 
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,CauseIsup releaseCause,
    		CAPINAPExtensions extensions,Integer connectTime) throws INAPException;
    
    Integer addSelectRouteRequest(CalledPartyNumberIsup destinationNumberRoutingAddress,AlertingPattern alertingPattern,
    		DigitsIsup correlationID,ISDNAccessRelatedInformation isdnAccessRelatedInformation,OriginalCalledNumberIsup originalCalledPartyID,
    		RouteList routeList,ScfID scfID,LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addSelectRouteRequest(int customInvokeTimeout,CalledPartyNumberIsup destinationNumberRoutingAddress,
    		AlertingPattern alertingPattern,DigitsIsup correlationID,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		OriginalCalledNumberIsup originalCalledPartyID,RouteList routeList,ScfID scfID,
    		LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,Carrier carrier) throws INAPException;       
    
    Integer addSelectFacilityRequest(AlertingPattern alertingPattern,CalledPartyNumberIsup destinationNumberRoutingAddress,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,FacilityGroup calledFacilityGroup,
    		Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID, 
    		CAPINAPExtensions extensions,Carrier carrier) throws INAPException;

    Integer addSelectFacilityRequest(int customInvokeTimeout,AlertingPattern alertingPattern,
    		CalledPartyNumberIsup destinationNumberRoutingAddress,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,OriginalCalledNumberIsup originalCalledPartyID,
    		CAPINAPExtensions extensions) throws INAPException;
    
    Integer addRequestCurrentStatusReportRequest(ResourceID resourceID) throws INAPException;

    Integer addRequestCurrentStatusReportRequest(int customInvokeTimeout,ResourceID resourceID) throws INAPException;
    
    void addRequestCurrentStatusReportResponse(int invokeId,ResourceStatus resourceStatus,ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;

    Integer addCancelStatusReportRequest(ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;

    Integer addCancelStatusReportRequest(int customInvokeTimeout,ResourceID resourceID,CAPINAPExtensions extensions) throws INAPException;
    	    
    Integer addRequestEveryStatusChangeReportRequest(ResourceID resourceID,DigitsIsup correlationID,
    		Integer duration,CAPINAPExtensions extensions) throws INAPException;

    Integer addRequestEveryStatusChangeReportRequest(int customInvokeTimeout,ResourceID resourceID,
    		DigitsIsup correlationID,Integer duration,CAPINAPExtensions extensions) throws INAPException;
    
    Integer addRequestFirstStatusMatchReportRequest(ResourceID resourceID,ResourceStatus resourceStatus,DigitsIsup correlationID,
    		Integer duration,CAPINAPExtensions extensions,BearerCapability bearerCapability) throws INAPException;

    Integer addRequestFirstStatusMatchReportRequest(int customInvokeTimeout,ResourceID resourceID,ResourceStatus resourceStatus,
    		DigitsIsup correlationID,Integer duration,CAPINAPExtensions extensions,BearerCapability bearerCapability) throws INAPException;
    
    Integer addStatusReportRequest(ResourceStatus resourceStatus,DigitsIsup correlationID,ResourceID resourceID,
    		CAPINAPExtensions extensions,ReportCondition reportCondition) throws INAPException;

    Integer addStatusReportRequest(int customInvokeTimeout,ResourceStatus resourceStatus,DigitsIsup correlationID,
    		ResourceID resourceID,CAPINAPExtensions extensions,ReportCondition reportCondition) throws INAPException;
    
    Integer addUpdateRequest(ByteBuf operationID,ApplicationID applicationID,DataItemID dataItemID,
    		DataItemInformation dataItemInformation) throws INAPException;

    Integer addUpdateRequest(int customInvokeTimeout,ByteBuf operationID,ApplicationID applicationID,
    		DataItemID dataItemID, DataItemInformation dataItemInformation) throws INAPException;
    
    void addUpdateResponse(int invokeId, ByteBuf operationReturnID) throws INAPException;
    
    Integer addRetrieveRequest(ByteBuf operationID,ApplicationID applicationID,DataItemID dataItemID) throws INAPException;

    Integer addRetrieveRequest(int customInvokeTimeout,ByteBuf operationID,ApplicationID applicationID,
    		DataItemID dataItemID) throws INAPException;
    
    void addRetrieveResponse(int invokeId, ByteBuf operationReturnID, DataItemInformation dataItemInformation) throws INAPException;
    
    Integer addSignallingInformationRequest(BackwardSuppressionIndicators backwardSuppressionIndicators,
    		CalledPartyNumberIsup connectedNumber,ForwardSuppressionIndicators forwardSuppressionIndicators,
    		BackwardGVNS backwardGVNS,CAPINAPExtensions extensions) throws INAPException;

    Integer addSignallingInformationRequest(int customInvokeTimeout,BackwardSuppressionIndicators backwardSuppressionIndicators,
    		CalledPartyNumberIsup connectedNumber,ForwardSuppressionIndicators forwardSuppressionIndicators,
    		BackwardGVNS backwardGVNS,CAPINAPExtensions extensions) throws INAPException;
    
    Integer addReleaseCallPartyConnectionRequest(LegType legToBeReleased,Integer callID,CauseIsup releaseCause) throws INAPException;

    Integer addReleaseCallPartyConnectionRequest(int customInvokeTimeout,LegType legToBeReleased,Integer callID,CauseIsup releaseCause) throws INAPException;        
    
    void addReleaseCallPartyConnectionResponse(int invokeId, List<LegInformation> legInformation) throws INAPException;
    
    void addReleaseCallPartyConnectionResponse(int invokeId) throws INAPException;
    
    Integer addReconnectRequest(LegType legID) throws INAPException;

    Integer addReconnectRequest(int customInvokeTimeout,LegType legID) throws INAPException;
    
    Integer addHoldCallPartyConnectionRequest(LegType legID) throws INAPException;

    Integer addHoldCallPartyConnectionRequest(int customInvokeTimeout,LegType legID) throws INAPException;
    
    Integer addHandoverRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) throws INAPException;

    Integer addHandoverRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) throws INAPException;

    Integer addDialogueUserInformationRequest(SendingFunctionsActive sendingFunctionsActive,
    		ReceivingFunctionsRequested receivingFunctionsRequested,Integer trafficSimulationSessionID) throws INAPException;

    Integer addDialogueUserInformationRequest(int customInvokeTimeout,SendingFunctionsActive sendingFunctionsActive,
    		ReceivingFunctionsRequested receivingFunctionsRequested,Integer trafficSimulationSessionID) throws INAPException;                
    
    Integer addCallLimitRequest(DateAndTime startTime,GapCriteria limitCriteria,
    		LimitIndicators limitIndicators,GapTreatment limitTreatment) throws INAPException;

    Integer addCallLimitRequest(int customInvokeTimeout,DateAndTime startTime,GapCriteria limitCriteria,
    		LimitIndicators limitIndicators,GapTreatment limitTreatment) throws INAPException;
    
    Integer addContinueWithArgumentRequest(LegType legID,GenericName genericName) throws INAPException;

    Integer addContinueWithArgumentRequest(int customInvokeTimeout,LegType legID,GenericName genericName) throws INAPException;
    
    Integer addResetTimerRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions)
            throws INAPException;

    Integer addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue, CAPINAPExtensions extensions) throws INAPException;
}