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

package org.restcomm.protocols.ss7.inap.api;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.ChargeIndicator;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.ChargeIndicatorValue;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.DpSpecificInfoAlt;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.MetDPCriterionAlt;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.MidCallEvents;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OAbandonSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OMidCallSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TDisconnectSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TMidCallSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.EsiBcsm.TServiceChangeSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AOCSubsequent;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAMELSCIBillingChargingCharacteristicsAlt;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallCompletionTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallingPartyRestrictionIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ChangeOfLocationAlt;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedDigits;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConnectedNumberTreatmentInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CwTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DestinationRoutingAddress;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteria;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.DpSpecificCriteriaAlt;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EctTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FCIBCCCAMELSequence1;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ForwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.FreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InbandInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LegOrCallSegment;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LowLayerCompatibility;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageID;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageIDText;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MidCallControlInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeDurationChargingResult;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeIfTariffSwitch;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Tone;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariableMessage;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePart;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartDate;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartPrice;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePartTime;
import org.restcomm.protocols.ss7.commonapp.api.isup.BearerIsup;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BCSMEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BothwayThroughConnectionInd;
import org.restcomm.protocols.ss7.commonapp.api.primitives.Burst;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BurstList;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoDpAssignment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 *
 * @author sergey vetyutnev
 * @author yulian.oifa
 *
 */
public interface INAPParameterFactory {

	CauseIsup createCause(byte[] data);

    CauseIsup createCause(CauseIndicators causeIndicators) throws INAPException;

    DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer);

    DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo);

    DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt);

    BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm);

    CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws INAPException;

    ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data);

    ExtensionField createExtensionField(List<Long> globalCode, CriticalityType criticalityType, byte[] data);

    CAPINAPExtensions createINAPExtensions(List<ExtensionField> fieldsList);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, CAPINAPExtensions extensions,
            Long tariffSwitchInterval);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, CAPINAPExtensions extensions);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPINAPExtensions extensions);

    DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second);

    TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

    BearerIsup createBearer(byte[] data);

    BearerIsup createBearer(UserServiceInformation userServiceInformation) throws INAPException;

    BearerCapability createBearerCapability(BearerIsup bearer);

    DigitsIsup createDigits_GenericNumber(byte[] data);

    DigitsIsup createDigits_GenericDigits(byte[] data);

    DigitsIsup createDigits_GenericNumber(GenericNumber genericNumber) throws INAPException;

    DigitsIsup createDigits_GenericDigits(GenericDigits genericDigits) throws INAPException;

    CalledPartyNumberIsup createCalledPartyNumber(byte[] data);

    CalledPartyNumberIsup createCalledPartyNumber(CalledPartyNumber calledPartyNumber) throws INAPException;

    CallingPartyNumberIsup createCallingPartyNumber(byte[] data);

    CallingPartyNumberIsup createCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws INAPException;

    GenericNumberIsup createGenericNumber(byte[] data);

    GenericNumberIsup createGenericNumber(GenericNumber genericNumber) throws INAPException;

    LocationNumberIsup createLocationNumber(byte[] data);

    LocationNumberIsup createLocationNumber(LocationNumber locationNumber) throws INAPException;

    OriginalCalledNumberIsup createOriginalCalledNumber(byte[] data);

    OriginalCalledNumberIsup createOriginalCalledNumber(OriginalCalledNumber originalCalledNumber) throws INAPException;

    RedirectingPartyIDIsup createRedirectingPartyID(byte[] data);

    RedirectingPartyIDIsup createRedirectingPartyID(RedirectingNumber redirectingNumber) throws INAPException;

    RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseIsup failureCause);

    OCalledPartyBusySpecificInfo createOCalledPartyBusySpecificInfo(CauseIsup busyCause);

    OAbandonSpecificInfo createOAbandonSpecificInfo(boolean routeNotPermitted);

    ONoAnswerSpecificInfo createONoAnswerSpecificInfo();

    OAnswerSpecificInfo createOAnswerSpecificInfo(CalledPartyNumberIsup destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2);

    ODisconnectSpecificInfo createODisconnectSpecificInfo(CauseIsup releaseCause);

    TBusySpecificInfo createTBusySpecificInfo(CauseIsup busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberIsup forwardingDestinationNumber);

    TNoAnswerSpecificInfo createTNoAnswerSpecificInfo(boolean callForwarded,
            CalledPartyNumberIsup forwardingDestinationNumber);

    TAnswerSpecificInfo createTAnswerSpecificInfo(CalledPartyNumberIsup destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode,
            ExtBasicServiceCode extBasicServiceCode2);

    TDisconnectSpecificInfo createTDisconnectSpecificInfo(CauseIsup releaseCause);

    DestinationRoutingAddress createDestinationRoutingAddress(List<CalledPartyNumberIsup> calledPartyNumber);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(
            OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(ONoAnswerSpecificInfo oNoAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAnswerSpecificInfo oAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OMidCallSpecificInfo oMidCallSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(ODisconnectSpecificInfo oDisconnectSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TBusySpecificInfo tBusySpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TNoAnswerSpecificInfo tNoAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TAnswerSpecificInfo tAnswerSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TMidCallSpecificInfo tMidCallSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TDisconnectSpecificInfo tDisconnectSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OTermSeizedSpecificInfo oTermSeizedSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(CallAcceptedSpecificInfo callAcceptedSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OAbandonSpecificInfo oAbandonSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(DpSpecificInfoAlt dpSpecificInfoAlt);

    RequestedInformation createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue);

    RequestedInformation createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue);

    RequestedInformation createRequestedInformation_CallStopTime(DateAndTime callStopTimeValue);

    RequestedInformation createRequestedInformation_ReleaseCause(CauseIsup releaseCauseValue);

    TimeDurationChargingResult createTimeDurationChargingResult(LegType partyToCharge,
            TimeInformation timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry, CAPINAPExtensions extensions,
            AChChargingAddress aChChargingAddress);

    TimeIfTariffSwitch createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval);

    TimeInformation createTimeInformation(int timeIfNoTariffSwitch);

    TimeInformation createTimeInformation(TimeIfTariffSwitch timeIfTariffSwitch);

    IPSSPCapabilities createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData);

    InitialDPArgExtension createInitialDPArgExtension(ISDNAddressString gmscAddress,
            CalledPartyNumberIsup forwardingDestinationNumber, MSClassmark2 msClassmark2, IMEI imei,
            SupportedCamelPhases supportedCamelPhases, OfferedCamel4Functionalities offeredCamel4Functionalities,
            BearerCapability bearerCapability2, ExtBasicServiceCode extBasicServiceCode2,
            HighLayerCompatibilityIsup highLayerCompatibility2, LowLayerCompatibility lowLayerCompatibility,
            LowLayerCompatibility lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUData uuData,
            boolean collectInformationAllowed, boolean releaseCallArgExtensionAllowed);

    AlertingPatternWrapper createAlertingPattern(AlertingPattern alertingPattern);

    AlertingPatternWrapper createAlertingPattern(byte[] data);

    NAOliInfo createNAOliInfo(int value);

    ScfID createScfID(byte[] data);

    ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator);

    FCIBCCCAMELSequence1 createFCIBCCCAMELsequence1(FreeFormatData freeFormatData, LegType partyToCharge,
            AppendFreeFormatData appendFreeFormatData);

    CAMELSCIBillingChargingCharacteristicsAlt createCAMELSCIBillingChargingCharacteristicsAlt();

    CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7);

    AOCSubsequent createAOCSubsequent(CAI_GSM0224 cai_GSM0224, Integer tariffSwitchInterval);

    AOCBeforeAnswer createAOCBeforeAnswer(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCBeforeAnswer aocBeforeAnswer);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(AOCSubsequent aocSubsequent);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(
            CAMELSCIBillingChargingCharacteristicsAlt aocExtension);

    VariablePartPrice createVariablePartPrice(byte[] data);

    VariablePartPrice createVariablePartPrice(double price);

    VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart);

    VariablePartDate createVariablePartDate(byte[] data);

    VariablePartDate createVariablePartDate(int year, int month, int day);

    VariablePartTime createVariablePartTime(byte[] data);

    VariablePartTime createVariablePartTime(int hour, int minute);

    VariablePart createVariablePart(Integer integer);

    VariablePart createVariablePart(DigitsIsup number);

    VariablePart createVariablePart(VariablePartTime time);

    VariablePart createVariablePart(VariablePartDate date);

    VariablePart createVariablePart(VariablePartPrice price);

    MessageIDText createMessageIDText(String messageContent, byte[] attributes);

    VariableMessage createVariableMessage(int elementaryMessageID, List<VariablePart> variableParts);

    MessageID createMessageID(Integer elementaryMessageID);

    MessageID createMessageID(MessageIDText text);

    MessageID createMessageID(List<Integer> elementaryMessageIDs);

    MessageID createMessageID(VariableMessage variableMessage);

    InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval);

    Tone createTone(int toneID, Integer duration);

    InformationToSend createInformationToSend(InbandInfo inbandInfo);

    InformationToSend createInformationToSend(Tone tone);

    CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit,
            byte[] cancelDigit, byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack);

    CollectedInfo createCollectedInfo(CollectedDigits collectedDigits);

    CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID);

    FreeFormatData createFreeFormatData(byte[] data);

    LegOrCallSegment createLegOrCallSegment(Integer callSegmentID);

    LegOrCallSegment createLegOrCallSegment(LegID legID);

    ChargeIndicator createChargeIndicator(int data);

    ChargeIndicator createChargeIndicator(ChargeIndicatorValue value);

    BackwardServiceInteractionInd createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator);

    Carrier createCarrier(byte[] data);

    ForwardServiceInteractionInd createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator);

    LowLayerCompatibility createLowLayerCompatibility(byte[] data);

    MidCallEvents createMidCallEvents_Completed(DigitsIsup dtmfDigits);

    MidCallEvents createMidCallEvents_TimeOut(DigitsIsup dtmfDigits);

    OMidCallSpecificInfo createOMidCallSpecificInfo(MidCallEvents midCallEvents);

    TMidCallSpecificInfo createTMidCallSpecificInfo(MidCallEvents midCallEvents);

    OTermSeizedSpecificInfo createOTermSeizedSpecificInfo(LocationInformation locationInformation);

    CallAcceptedSpecificInfo createCallAcceptedSpecificInfo(LocationInformation locationInformation);

    MetDPCriterionAlt createMetDPCriterionAlt();

    MetDPCriterion createMetDPCriterion_enteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_enteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    MetDPCriterion createMetDPCriterion_enteringLocationAreaId(LAIFixedLength value);

    MetDPCriterion createMetDPCriterion_leavingLocationAreaId(LAIFixedLength value);

    MetDPCriterion createMetDPCriterion_interSystemHandOverToUMTS();

    MetDPCriterion createMetDPCriterion_interSystemHandOverToGSM();

    MetDPCriterion createMetDPCriterion_interPLMNHandOver();

    MetDPCriterion createMetDPCriterion_interMSCHandOver();

    MetDPCriterion createMetDPCriterion(MetDPCriterionAlt metDPCriterionAlt);

    OChangeOfPositionSpecificInfo createOChangeOfPositionSpecificInfo(LocationInformation locationInformation, List<MetDPCriterion> metDPCriteriaList);

    TChangeOfPositionSpecificInfo createTChangeOfPositionSpecificInfo(LocationInformation locationInformation, List<MetDPCriterion> metDPCriteriaList);

    OServiceChangeSpecificInfo createOServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode);

    TServiceChangeSpecificInfo createTServiceChangeSpecificInfo(ExtBasicServiceCode extBasicServiceCode);

    CollectedInfoSpecificInfo createCollectedInfoSpecificInfo(CalledPartyNumberIsup calledPartyNumber);

    DpSpecificInfoAlt createDpSpecificInfoAlt(OServiceChangeSpecificInfo oServiceChangeSpecificInfo, CollectedInfoSpecificInfo collectedInfoSpecificInfo,
            TServiceChangeSpecificInfo tServiceChangeSpecificInfo);

    ChangeOfLocationAlt createChangeOfLocationAlt();

    ChangeOfLocation createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value);

    ChangeOfLocation createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value);

    ChangeOfLocation createChangeOfLocation(LAIFixedLength locationAreaId);

    ChangeOfLocation createChangeOfLocation_interSystemHandOver();

    ChangeOfLocation createChangeOfLocation_interPLMNHandOver();

    ChangeOfLocation createChangeOfLocation_interMSCHandOver();

    ChangeOfLocation createChangeOfLocation(ChangeOfLocationAlt changeOfLocationAlt);

    DpSpecificCriteriaAlt createDpSpecificCriteriaAlt(List<ChangeOfLocation> changeOfPositionControlInfo, Integer numberOfDigits);

    MidCallControlInfo createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit,
            String startDigit, Integer interDigitTimeout);

    Burst createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval);

    BurstList createBurstList(Integer warningPeriod, Burst burst);

    AudibleIndicator createAudibleIndicator(Boolean tone);

    AudibleIndicator createAudibleIndicator(BurstList burstList);

    AChChargingAddress createAChChargingAddress(LegID legID);

    AChChargingAddress createAChChargingAddress(int srfConnection);

    CallingPartysCategoryIsup createCallingPartysCategoryInap(CallingPartyCategory callingPartyCategory)
            throws INAPException;

    HighLayerCompatibilityIsup createHighLayerCompatibilityInap(UserTeleserviceInformation highLayerCompatibility)
            throws INAPException;

    RedirectionInformationIsup createRedirectionInformationInap(RedirectionInformation redirectionInformation)
            throws INAPException;

    LegID createLegID(LegType receivingLeg,LegType sendingLeg);
    
    MiscCallInfo createMiscCallInfo(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment);
    
    ISDNAddressString createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address);
    ISDNAddressString createISDNAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan, String address);
    SupportedCamelPhases createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4);

    /**
     * Creates a new instance of {@link IMSI}
     *
     * @param data whole data string
     * @return new instance of {@link IMSI}
     */
    IMSI createIMSI(String data);
    
    LocationInformation createLocationInformation(Integer ageOfLocationInformation,
            GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber, LocationNumberMap locationNumber,
            CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI, MAPExtensionContainer extensionContainer,
            LSAIdentity selectedLSAId, ISDNAddressString mscNumber, GeodeticInformation geodeticInformation,
            boolean currentLocationRetrieved, boolean saiPresent, LocationInformationEPS locationInformationEPS,
            UserCSGInformation userCSGInformation);    
}
