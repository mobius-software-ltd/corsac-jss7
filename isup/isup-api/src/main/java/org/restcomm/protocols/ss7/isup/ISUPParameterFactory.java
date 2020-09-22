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

/**
 *
 */
package org.restcomm.protocols.ss7.isup;


import java.util.List;

import org.restcomm.protocols.ss7.isup.message.parameter.*;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReasonType;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingRedirectIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.RejectImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnResultImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokeImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnErrorImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingPivotReasonType;
import org.restcomm.protocols.ss7.isup.message.parameter.Status;

/**
 * Factory for parameters.
 *
 * @author baranowb
 *
 */
public interface ISUPParameterFactory {

    AccessDeliveryInformation createAccessDeliveryInformation();

    AccessTransport createAccessTransport();

    ApplicationTransport createApplicationTransport();

    AutomaticCongestionLevel createAutomaticCongestionLevel();

    BackwardCallIndicators createBackwardCallIndicators();

    BackwardGVNS createBackwardGVNS();

    CallDiversionInformation createCallDiversionInformation();

    CallDiversionTreatmentIndicators createCallDiversionTreatmentIndicators();

    CalledDirectoryNumber createCalledDirectoryNumber();

    CalledINNumber createCalledINNumber();

    CalledPartyNumber createCalledPartyNumber();

    CallHistoryInformation createCallHistoryInformation();

    CallingPartyCategory createCallingPartyCategory();

    CallingPartyNumber createCallingPartyNumber();

    CallOfferingTreatmentIndicators createCallOfferingTreatmentIndicators();

    CallReference createCallReference();

    CallTransferNumber createCallTransferNumber();

    CallTransferReference createCallTransferReference();

    CauseIndicators createCauseIndicators();

    CCNRPossibleIndicator createCCNRPossibleIndicator();

    CCSS createCCSS();

    ChargedPartyIdentification createChargedPartyIdentification();

    CircuitAssigmentMap createCircuitAssigmentMap();

    CircuitGroupSuperVisionMessageType createCircuitGroupSuperVisionMessageType();

    CircuitIdentificationCode createCircuitIdentificationCode();

    CircuitStateIndicator createCircuitStateIndicator();

    ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode();

    CollectCallRequest createCollectCallRequest();

    ConferenceTreatmentIndicators createConferenceTreatmentIndicators();

    ConnectedNumber createConnectedNumber();

    ConnectionRequest createConnectionRequest();

    ContinuityIndicators createContinuityIndicators();

    CorrelationID createCorrelationID();

    DisplayInformation createDisplayInformation();

    EchoControlInformation createEchoControlInformation();

    ErrorCodeImpl createLocalErrorCode(Long value);

    ErrorCodeImpl createGlobalErrorCode(List<Long> value);

    EventInformation createEventInformation();

    FacilityIndicator createFacilityIndicator();

    ForwardCallIndicators createForwardCallIndicators();

    ForwardGVNS createForwardGVNS();

    GenericDigits createGenericDigits();

    GenericNotificationIndicator createGenericNotificationIndicator();

    GenericNumber createGenericNumber();

    GenericReference createGenericReference();

    GVNSUserGroup createGVNSUserGroup();

    HopCounter createHopCounter();

    HTRInformation createHTRInformation();

    InformationIndicators createInformationIndicators();

    InformationRequestIndicators createInformationRequestIndicators();

    InvokeImpl createInvoke();

    InvokingPivotReason createInvokingPivotReason(InvokingPivotReasonType type);

    InvokingRedirectReason createInvokingRedirectReason(InvokingRedirectReasonType type);

    LocationNumber createLocationNumber();

    LoopPreventionIndicators createLoopPreventionIndicators();

    MCIDRequestIndicators createMCIDRequestIndicators();

    MCIDResponseIndicators createMCIDResponseIndicators();

    MessageCompatibilityInformation createMessageCompatibilityInformation();

    MessageCompatibilityInstructionIndicator createMessageCompatibilityInstructionIndicator();

    MLPPPrecedence createMLPPPrecedence();

    NatureOfConnectionIndicators createNatureOfConnectionIndicators();

    NetworkManagementControls createNetworkManagementControls();

    NetworkRoutingNumber createNetworkRoutingNumber();

    NetworkSpecificFacility createNetworkSpecificFacility();

    OperationCodeImpl createLocalOperationCode(Long value);

    OperationCodeImpl createGlobalOperationCode(List<Long> value);

    OptionalBackwardCallIndicators createOptionalBackwardCallIndicators();

    OptionalForwardCallIndicators createOptionalForwardCallIndicators();

    OriginalCalledINNumber createOriginalCalledINNumber();

    OriginalCalledNumber createOriginalCalledNumber();

    OriginatingISCPointCode createOriginatingISCPointCode();

    OriginatingParticipatingServiceProvider createOriginatingParticipatingServiceProvider();

    ParameterCompatibilityInformation createParameterCompatibilityInformation();

    ParameterCompatibilityInstructionIndicators createParameterCompatibilityInstructionIndicators();

    PerformingPivotIndicator createPerformingPivotIndicator();

    PerformingRedirectIndicator createPerformingRedirectIndicator();

    PivotCapability createPivotCapability();

    PivotCounter createPivotCounter();

    PivotReason createPivotReason();

    PivotRoutingBackwardInformation createPivotRoutingBackwardInformation();

    PivotRoutingForwardInformation createPivotRoutingForwardInformation();

    PivotRoutingIndicators createPivotRoutingIndicators();

    PivotStatus createPivotStatus();

    ProblemImpl createProblem();

    PropagationDelayCounter createPropagationDelayCounter();

    QueryOnReleaseCapability createQueryOnReleaseCapability();

    RangeAndStatus createRangeAndStatus();

    RedirectBackwardInformation createRedirectBackwardInformation();

    RedirectCapability createRedirectCapability();

    RedirectCounter createRedirectCounter();

    RedirectForwardInformation createRedirectForwardformation();

    RedirectingNumber createRedirectingNumber();

    RedirectionInformation createRedirectionInformation();

    RedirectionNumber createRedirectionNumber();

    RedirectionNumberRestriction createRedirectionNumberRestriction();

    RedirectReason createRedirectReason();

    RedirectStatus createRedirectStatus();

    RejectImpl createReject();

    RemoteOperations createRemoteOperations();

    Reserved createReserved();

    ReturnErrorImpl createReturnError();

    ReturnResultImpl createReturnResult();

    ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier();

    ReturnToInvokingExchangeDuration createReturnToInvokingExchangeDuration();

    ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible();

    SCFID createSCFID();

    ServiceActivation createServiceActivation();

    SignalingPointCode createSignalingPointCode();
    Status createStatus();
    SubsequentNumber createSubsequentNumber();
    SuspendResumeIndicators createSuspendResumeIndicators();
    TerminatingNetworkRoutingNumber createTerminatingNetworkRoutingNumber();
    TransimissionMediumRequierementPrime createTransimissionMediumRequierementPrime();
    TransitNetworkSelection createTransitNetworkSelection();
    TransmissionMediumRequirement createTransmissionMediumRequirement();
    TransmissionMediumUsed createTransmissionMediumUsed();
    UIDActionIndicators createUIDActionIndicators();
    UIDCapabilityIndicators createUIDCapabilityIndicators();
    UserServiceInformation createUserServiceInformation();
    UserServiceInformationPrime createUserServiceInformationPrime();
    UserTeleserviceInformation createUserTeleserviceInformation();
    UserToUserIndicators createUserToUserIndicators();
    UserToUserInformation createUserToUserInformation();
    RedirectForwardInformation createRedirectForwardInformation();
}
