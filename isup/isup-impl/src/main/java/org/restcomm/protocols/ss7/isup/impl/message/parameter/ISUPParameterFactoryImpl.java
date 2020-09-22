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
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import java.util.List;

import org.restcomm.protocols.ss7.isup.ISUPParameterFactory;
import org.restcomm.protocols.ss7.isup.impl.message.parameter.accessTransport.AccessTransportImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.*;
import org.restcomm.protocols.ss7.isup.message.parameter.accessTransport.AccessTransport;
import org.restcomm.protocols.ss7.isup.message.parameter.PerformingRedirectIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectForwardInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.MessageCompatibilityInstructionIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectReason;
import org.restcomm.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;
import org.restcomm.protocols.ss7.isup.message.parameter.PivotReason;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingPivotReasonType;
import org.restcomm.protocols.ss7.isup.message.parameter.InvokingRedirectReasonType;

/**
 * Simple factory.
 *
 * @author baranowb
 *
 */
public class ISUPParameterFactoryImpl implements ISUPParameterFactory {

    public AccessDeliveryInformation createAccessDeliveryInformation() {

        return new AccessDeliveryInformationImpl();
    }

    public AccessTransport createAccessTransport() {

        return new AccessTransportImpl();
    }

    public ApplicationTransport createApplicationTransport() {

        return new ApplicationTransportImpl();
    }

    public AutomaticCongestionLevel createAutomaticCongestionLevel() {

        return new AutomaticCongestionLevelImpl();
    }

    public BackwardCallIndicators createBackwardCallIndicators() {

        return new BackwardCallIndicatorsImpl();
    }

    public BackwardGVNS createBackwardGVNS() {

        return new BackwardGVNSImpl();
    }

    public CallDiversionInformation createCallDiversionInformation() {

        return new CallDiversionInformationImpl();
    }

    public CallDiversionTreatmentIndicators createCallDiversionTreatmentIndicators() {

        return new CallDiversionTreatmentIndicatorsImpl();
    }

    public CalledDirectoryNumber createCalledDirectoryNumber() {

        return new CalledDirectoryNumberImpl();
    }

    public CalledINNumber createCalledINNumber() {

        return new CalledINNumberImpl();
    }

    public CalledPartyNumber createCalledPartyNumber() {

        return new CalledPartyNumberImpl();
    }

    public CallHistoryInformation createCallHistoryInformation() {

        return new CallHistoryInformationImpl();
    }

    public CallingPartyCategory createCallingPartyCategory() {

        return new CallingPartyCategoryImpl();
    }

    public CallingPartyNumber createCallingPartyNumber() {

        return new CallingPartyNumberImpl();
    }

    public CallOfferingTreatmentIndicators createCallOfferingTreatmentIndicators() {

        return new CallOfferingTreatmentIndicatorsImpl();
    }

    public CallReference createCallReference() {

        return new CallReferenceImpl();
    }

    public CallTransferNumber createCallTransferNumber() {

        return new CallTransferNumberImpl();
    }

    public CallTransferReference createCallTransferReference() {

        return new CallTransferReferenceImpl();
    }

    public CauseIndicators createCauseIndicators() {

        return new CauseIndicatorsImpl();
    }

    public CCNRPossibleIndicator createCCNRPossibleIndicator() {

        return new CCNRPossibleIndicatorImpl();
    }

    public CCSS createCCSS() {

        return new CCSSImpl();
    }

    public ChargedPartyIdentification createChargedPartyIdentification() {
        return new ChargedPartyIdentificationImpl();
    }

    public CircuitAssigmentMap createCircuitAssigmentMap() {

        return new CircuitAssigmentMapImpl();
    }

    public CircuitGroupSuperVisionMessageType createCircuitGroupSuperVisionMessageType() {

        return new CircuitGroupSuperVisionMessageTypeImpl();
    }

    public CircuitIdentificationCode createCircuitIdentificationCode() {

        return new CircuitIdentificationCodeImpl();
    }

    public CircuitStateIndicator createCircuitStateIndicator() {

        return new CircuitStateIndicatorImpl();
    }

    public ClosedUserGroupInterlockCode createClosedUserGroupInterlockCode() {

        return new ClosedUserGroupInterlockCodeImpl();
    }

    public CollectCallRequest createCollectCallRequest() {

        return new CollectCallRequestImpl();
    }

    public ConferenceTreatmentIndicators createConferenceTreatmentIndicators() {

        return new ConferenceTreatmentIndicatorsImpl();
    }

    public ConnectedNumber createConnectedNumber() {

        return new ConnectedNumberImpl();
    }

    public ConnectionRequest createConnectionRequest() {

        return new ConnectionRequestImpl();
    }

    public ContinuityIndicators createContinuityIndicators() {

        return new ContinuityIndicatorsImpl();
    }

    public CorrelationID createCorrelationID() {

        return new CorrelationIDImpl();
    }

    public DisplayInformation createDisplayInformation() {

        return new DisplayInformationImpl();
    }

    public EchoControlInformation createEchoControlInformation() {

        return new EchoControlInformationImpl();
    }

    public OperationCodeImpl createLocalOperationCode(Long value) {
        OperationCodeImpl oc=new OperationCodeImpl();
        oc.setLocalOperationCode(value);
        return oc;
    }

    public OperationCodeImpl createGlobalOperationCode(List<Long> value) {
    	OperationCodeImpl oc = new OperationCodeImpl();
    	oc.setGlobalOperationCode(value);
    	return oc;
    }

    public ErrorCodeImpl createLocalErrorCode(Long value) {
        ErrorCodeImpl ec = new ErrorCodeImpl();
        ec.setLocalErrorCode(value);
        return ec;
    }

    public ErrorCodeImpl createGlobalErrorCode(List<Long> value) {
        ErrorCodeImpl ec=new ErrorCodeImpl();
        ec.setGlobalErrorCode(value);
        return ec;
    }

    public EventInformation createEventInformation() {

        return new EventInformationImpl();
    }

    public FacilityIndicator createFacilityIndicator() {

        return new FacilityIndicatorImpl();
    }

    public ForwardCallIndicators createForwardCallIndicators() {

        return new ForwardCallIndicatorsImpl();
    }

    public ForwardGVNS createForwardGVNS() {

        return new ForwardGVNSImpl();
    }

    public GenericDigits createGenericDigits() {
        return new GenericDigitsImpl();
    }

    public GenericNotificationIndicator createGenericNotificationIndicator() {

        return new GenericNotificationIndicatorImpl();
    }

    public GenericNumber createGenericNumber() {

        return new GenericNumberImpl();
    }

    public GenericReference createGenericReference() {

        return new GenericReferenceImpl();
    }

    public GVNSUserGroup createGVNSUserGroup() {

        return new GVNSUserGroupImpl();
    }

    public HopCounter createHopCounter() {

        return new HopCounterImpl();
    }

    public HTRInformation createHTRInformation() {

        return new HTRInformationImpl();
    }

    public InformationIndicators createInformationIndicators() {

        return new InformationIndicatorsImpl();
    }

    public InformationRequestIndicators createInformationRequestIndicators() {

        return new InformationRequestIndicatorsImpl();
    }

    @Override
    public InvokeImpl createInvoke() {
        return new InvokeImpl();
    }

    public InvokingPivotReason createInvokingPivotReason(InvokingPivotReasonType type) {
        int tag = -1;
        switch(type){
            case PivotRoutingBackwardInformation:
                tag = PivotRoutingBackwardInformation.INFORMATION_INVOKING_PIVOT_REASON;
                break;
            case PivotRoutingForwardInformation:
                tag = PivotRoutingForwardInformation.INFORMATION_INVOKING_PIVOT_REASON;
                break;
            default:
                throw new IllegalArgumentException();
        }
        InvokingPivotReasonImpl reason = new InvokingPivotReasonImpl();
        reason.setTag(tag);
        return reason;
    }

    @Override
    public InvokingRedirectReason createInvokingRedirectReason(InvokingRedirectReasonType type) {
        int tag = -1;
        switch(type){
            case RedirectBackwardInformation:
                tag = RedirectBackwardInformation.INFORMATION_INVOKING_REDIRECT_REASON;
                break;
            case RedirectForwardInformation:
                tag = RedirectForwardInformation.INFORMATION_INVOKING_REDIRECT_REASON;
                break;
            default:
                throw new IllegalArgumentException();
        }
        InvokingRedirectReasonImpl reason = new InvokingRedirectReasonImpl();
        reason.setTag(tag);
        return reason;
    }

    public LocationNumber createLocationNumber() {

        return new LocationNumberImpl();
    }

    public LoopPreventionIndicators createLoopPreventionIndicators() {

        return new LoopPreventionIndicatorsImpl();
    }

    public MCIDRequestIndicators createMCIDRequestIndicators() {

        return new MCIDRequestIndicatorsImpl();
    }

    public MCIDResponseIndicators createMCIDResponseIndicators() {

        return new MCIDResponseIndicatorsImpl();
    }

    public MessageCompatibilityInformation createMessageCompatibilityInformation() {
        return new MessageCompatibilityInformationImpl();
    }

    @Override
    public MessageCompatibilityInstructionIndicator createMessageCompatibilityInstructionIndicator() {
        return new MessageCompatibilityInstructionIndicatorImpl();
    }

    public MLPPPrecedence createMLPPPrecedence() {

        return new MLPPPrecedenceImpl();
    }

    public NatureOfConnectionIndicators createNatureOfConnectionIndicators() {

        return new NatureOfConnectionIndicatorsImpl();
    }

    public NetworkManagementControls createNetworkManagementControls() {

        return new NetworkManagementControlsImpl();
    }

    public NetworkRoutingNumber createNetworkRoutingNumber() {

        return new NetworkRoutingNumberImpl();
    }

    public NetworkSpecificFacility createNetworkSpecificFacility() {

        return new NetworkSpecificFacilityImpl();
    }

    public OptionalBackwardCallIndicators createOptionalBackwardCallIndicators() {

        return new OptionalBackwardCallIndicatorsImpl();
    }

    public OptionalForwardCallIndicators createOptionalForwardCallIndicators() {

        return new OptionalForwardCallIndicatorsImpl();
    }

    public OriginalCalledINNumber createOriginalCalledINNumber() {

        return new OriginalCalledINNumberImpl();
    }

    public OriginalCalledNumber createOriginalCalledNumber() {

        return new OriginalCalledNumberImpl();
    }

    public OriginatingISCPointCode createOriginatingISCPointCode() {

        return new OriginatingISCPointCodeImpl();
    }

    public OriginatingParticipatingServiceProvider createOriginatingParticipatingServiceProvider() {

        return new OriginatingParticipatingServiceProviderImpl();
    }

    public ParameterCompatibilityInformation createParameterCompatibilityInformation() {

        return new ParameterCompatibilityInformationImpl();
    }

    public ParameterCompatibilityInstructionIndicators createParameterCompatibilityInstructionIndicators() {

        return new ParameterCompatibilityInstructionIndicatorsImpl();
    }

    public PerformingPivotIndicator createPerformingPivotIndicator() {
        PerformingPivotIndicatorImpl indicator = new PerformingPivotIndicatorImpl();
        indicator.setTag(PivotRoutingForwardInformation.INFORMATION_PERFORMING_PIVOT_INDICATOR);
        return indicator;
    }

    @Override
    public PerformingRedirectIndicator createPerformingRedirectIndicator() {
        PerformingRedirectIndicatorImpl indicator = new PerformingRedirectIndicatorImpl();
        indicator.setTag(RedirectForwardInformation.INFORMATION_PERFORMING_REDIRECT_INDICATOR);
        return indicator;
    }

    public PivotCapability createPivotCapability() {

        return new PivotCapabilityImpl();
    }

    public PivotCounter createPivotCounter() {

        return new PivotCounterImpl();
    }

    @Override
    public PivotReason createPivotReason() {
        return new PivotReasonImpl();
    }

    public PivotRoutingBackwardInformation createPivotRoutingBackwardInformation() {

        return new PivotRoutingBackwardInformationImpl();
    }

    public PivotRoutingForwardInformation createPivotRoutingForwardInformation() {

        return new PivotRoutingForwardInformationImpl();
    }

    public PivotRoutingIndicators createPivotRoutingIndicators() {

        return new PivotRoutingIndicatorsImpl();
    }

    public PivotStatus createPivotStatus() {
        return new PivotStatusImpl();
    }

    @Override
    public ProblemImpl createProblem() {
        return new ProblemImpl();
    }

    public PropagationDelayCounter createPropagationDelayCounter() {

        return new PropagationDelayCounterImpl();
    }

    public QueryOnReleaseCapability createQueryOnReleaseCapability() {

        return new QueryOnReleaseCapabilityImpl();
    }

    public RangeAndStatus createRangeAndStatus() {

        return new RangeAndStatusImpl();
    }

    public RedirectBackwardInformation createRedirectBackwardInformation() {

        return new RedirectBackwardInformationImpl();
    }

    public RedirectCapability createRedirectCapability() {

        return new RedirectCapabilityImpl();
    }

    public RedirectCounter createRedirectCounter() {

        return new RedirectCounterImpl();
    }

    @Override
    public RedirectForwardInformation createRedirectForwardformation() {
        return new RedirectForwardInformationImpl();
    }

    public RedirectingNumber createRedirectingNumber() {

        return new RedirectingNumberImpl();
    }

    public RedirectionInformation createRedirectionInformation() {

        return new RedirectionInformationImpl();
    }

    public RedirectionNumber createRedirectionNumber() {

        return new RedirectionNumberImpl();
    }

    public RedirectionNumberRestriction createRedirectionNumberRestriction() {

        return new RedirectionNumberRestrictionImpl();
    }

    @Override
    public RedirectReason createRedirectReason() {
        return new RedirectReasonImpl();
    }

    public RedirectStatus createRedirectStatus() {

        return new RedirectStatusImpl();
    }

    @Override
    public RejectImpl createReject() {
        return new RejectImpl();
    }

    public RemoteOperations createRemoteOperations() {

        return new RemoteOperationsImpl();
    }

    public Reserved createReserved() {

        return new ReservedImpl();
    }

    @Override
    public ReturnErrorImpl createReturnError() {
        return new ReturnErrorImpl();
    }

    @Override
    public ReturnResultImpl createReturnResult() {
        return new ReturnResultImpl();
    }

    public ReturnToInvokingExchangeCallIdentifier createReturnToInvokingExchangeCallIdentifier() {
        ReturnToInvokingExchangeCallIdentifierImpl cid = new ReturnToInvokingExchangeCallIdentifierImpl();
        cid.setTag(PivotRoutingForwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_CALL_ID);
        return cid;
    }

    @Override
    public ReturnToInvokingExchangeDuration createReturnToInvokingExchangeDuration() {
        ReturnToInvokingExchangeDurationImpl duration = new ReturnToInvokingExchangeDurationImpl();
        duration.setTag(PivotRoutingBackwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_DURATION);
        return duration;
    }

    public ReturnToInvokingExchangePossible createReturnToInvokingExchangePossible() {
        ReturnToInvokingExchangePossibleImpl iWonderWhenSomeoneWillNoticeThisLongishName = new ReturnToInvokingExchangePossibleImpl();
        iWonderWhenSomeoneWillNoticeThisLongishName.setTag(PivotRoutingForwardInformation.INFORMATION_RETURN_TO_INVOKING_EXCHANGE_POSSIBLE);
        return new ReturnToInvokingExchangePossibleImpl();
    }

    public SCFID createSCFID() {

        return new SCFIDImpl();
    }

    public ServiceActivation createServiceActivation() {

        return new ServiceActivationImpl();
    }

    public SignalingPointCode createSignalingPointCode() {

        return new SignalingPointCodeImpl();
    }

    public Status createStatus() {
        return new StatusImpl();
    }

    public SubsequentNumber createSubsequentNumber() {

        return new SubsequentNumberImpl();
    }

    public SuspendResumeIndicators createSuspendResumeIndicators() {

        return new SuspendResumeIndicatorsImpl();
    }

    public TerminatingNetworkRoutingNumber createTerminatingNetworkRoutingNumber() {

        return new TerminatingNetworkRoutingNumberImpl();
    }

    public TransimissionMediumRequierementPrime createTransimissionMediumRequierementPrime() {

        return new TransimissionMediumRequierementPrimeImpl();
    }

    public TransitNetworkSelection createTransitNetworkSelection() {

        return new TransitNetworkSelectionImpl();
    }

    public TransmissionMediumRequirement createTransmissionMediumRequirement() {

        return new TransmissionMediumRequirementImpl();
    }

    public TransmissionMediumUsed createTransmissionMediumUsed() {

        return new TransmissionMediumUsedImpl();
    }

    public UIDActionIndicators createUIDActionIndicators() {

        return new UIDActionIndicatorsImpl();
    }

    public UIDCapabilityIndicators createUIDCapabilityIndicators() {

        return new UIDCapabilityIndicatorsImpl();
    }

    public UserServiceInformation createUserServiceInformation() {

        return new UserServiceInformationImpl();
    }

    public UserServiceInformationPrime createUserServiceInformationPrime() {

        return new UserServiceInformationPrimeImpl();
    }

    public UserTeleserviceInformation createUserTeleserviceInformation() {

        return new UserTeleserviceInformationImpl();
    }

    public UserToUserIndicators createUserToUserIndicators() {

        return new UserToUserIndicatorsImpl();
    }

    public UserToUserInformation createUserToUserInformation() {

        return new UserToUserInformationImpl();
    }

    @Override
    public RedirectForwardInformation createRedirectForwardInformation() {
        return new RedirectForwardInformationImpl();
    }
}
