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

package org.restcomm.protocols.ss7.cap.api;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAlt;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterion;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAlt;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfo;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswer;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequent;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAlt;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRS;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTime;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELSequence1Gprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatDataGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCause;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganization;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROTimeGPRSIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolume;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitch;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MTSMSCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressString;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingScheme;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifier;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfo;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriod;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ExtensionField;
import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
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
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GPRSChargingID;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationGPRS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.Ext2QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtQoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.QoSSubscribed;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
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
 *
 */
public interface CAPParameterFactory {

    CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference);

    CauseIsup createCause(byte[] data);

    CauseIsup createCause(CauseIndicators causeIndicators) throws CAPException;

    DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer);

    DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo);

    DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt);

    BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm);

    CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws CAPException;

    ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data, boolean isConstructed);

    ExtensionField createExtensionField(List<Long> globalCode, CriticalityType criticalityType, byte[] data, boolean isConstructed);

    CAPINAPExtensions createCAPExtensions(List<ExtensionField> fieldsList);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, CAPINAPExtensions extensions,
            Long tariffSwitchInterval);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, CAPINAPExtensions extensions);

    CAMELAChBillingChargingCharacteristics createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicator audibleIndicator, CAPINAPExtensions extensions);

    DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second);

    TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

    BearerIsup createBearer(byte[] data);

    BearerIsup createBearer(UserServiceInformation userServiceInformation) throws CAPException;

    BearerCapability createBearerCapability(BearerIsup bearer);

    DigitsIsup createDigits_GenericNumber(byte[] data);

    DigitsIsup createDigits_GenericDigits(byte[] data);

    DigitsIsup createDigits_GenericNumber(GenericNumber genericNumber) throws CAPException;

    DigitsIsup createDigits_GenericDigits(GenericDigits genericDigits) throws CAPException;

    CalledPartyNumberIsup createCalledPartyNumber(byte[] data);

    CalledPartyNumberIsup createCalledPartyNumber(CalledPartyNumber calledPartyNumber) throws CAPException;

    CallingPartyNumberIsup createCallingPartyNumber(byte[] data);

    CallingPartyNumberIsup createCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws CAPException;

    GenericNumberIsup createGenericNumber(byte[] data);

    GenericNumberIsup createGenericNumber(GenericNumber genericNumber) throws CAPException;

    LocationNumberIsup createLocationNumber(byte[] data);

    LocationNumberIsup createLocationNumber(LocationNumber locationNumber) throws CAPException;

    OriginalCalledNumberIsup createOriginalCalledNumber(byte[] data);

    OriginalCalledNumberIsup createOriginalCalledNumber(OriginalCalledNumber originalCalledNumber) throws CAPException;

    RedirectingPartyIDIsup createRedirectingPartyID(byte[] data);

    RedirectingPartyIDIsup createRedirectingPartyID(RedirectingNumber redirectingNumber) throws CAPException;

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

    InitialDPArgExtension createInitialDPArgExtension(NACarrierInformation naCarrierInformation, ISDNAddressString gmscAddress);

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

    AccessPointName createAccessPointName(byte[] data);

    AOCGPRS createAOCGPRS(CAI_GSM0224 aocInitial, AOCSubsequent aocSubsequent);

    CAMELFCIGPRSBillingChargingCharacteristics createCAMELFCIGPRSBillingChargingCharacteristics(
            FCIBCCCAMELSequence1Gprs fcIBCCCAMELsequence1);

    CAMELSCIGPRSBillingChargingCharacteristics createCAMELSCIGPRSBillingChargingCharacteristics(AOCGPRS aocGPRS,
            PDPID pdpID);

    ChargingCharacteristics createChargingCharacteristics(long maxTransferredVolume);

    ChargingCharacteristics createChargingCharacteristics(int maxElapsedTime);

    ChargingResult createChargingResult(TransferredVolume transferredVolume);

    ChargingResult createChargingResult(ElapsedTime elapsedTime);

    ChargingRollOver createChargingRollOver(ElapsedTimeRollOver elapsedTimeRollOver);

    ChargingRollOver createChargingRollOver(TransferredVolumeRollOver transferredVolumeRollOver);

    ElapsedTime createElapsedTime(Integer timeGPRSIfNoTariffSwitch);

    ElapsedTime createElapsedTime(TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch);

    ElapsedTimeRollOver createElapsedTimeRollOver(Integer roTimeGPRSIfNoTariffSwitch);

    ElapsedTimeRollOver createElapsedTimeRollOver(ROTimeGPRSIfTariffSwitch roTimeGPRSIfTariffSwitch);

    EndUserAddress createEndUserAddress(PDPTypeOrganization pdpTypeOrganization, PDPTypeNumber pdpTypeNumber,
            PDPAddress pdpAddress);

    FCIBCCCAMELSequence1Gprs createFCIBCCCAMELsequence1(
            FreeFormatDataGprs freeFormatData, PDPID pdpID, AppendFreeFormatData appendFreeFormatData);

    FreeFormatData createFreeFormatData(byte[] data);

    FreeFormatDataGprs createFreeFormatDataGprs(byte[] data);

    GPRSCause createGPRSCause(int data);

    GPRSEvent createGPRSEvent(GPRSEventType gprsEventType, MonitorMode monitorMode);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(LocationInformationGPRS locationInformationGPRS);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PdpContextChangeOfPositionSpecificInformation pdpContextchangeOfPositionSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(DetachSpecificInformation detachSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            DisconnectSpecificInformation disconnectSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentSpecificInformation pdpContextEstablishmentSpecificInformation);

    GPRSEventSpecificInformation createGPRSEventSpecificInformation(
            PDPContextEstablishmentAcknowledgementSpecificInformation pdpContextEstablishmentAcknowledgementSpecificInformation);

    GPRSQoSExtension createGPRSQoSExtension(Ext2QoSSubscribed supplementToLongQoSFormat);

    GPRSQoS createGPRSQoS(QoSSubscribed shortQoSFormat);

    GPRSQoS createGPRSQoS(ExtQoSSubscribed longQoSFormat);

    PDPAddress createPDPAddress(byte[] data);

    PDPID createPDPID(int data);

    PDPTypeNumber createPDPTypeNumber(int data);

    PDPTypeNumber createPDPTypeNumber(PDPTypeNumberValue value);

    PDPTypeOrganization createPDPTypeOrganization(int data);

    PDPTypeOrganization createPDPTypeOrganization(PDPTypeOrganizationValue value);

    QualityOfService createQualityOfService(GPRSQoS requestedQoS, GPRSQoS subscribedQoS, GPRSQoS negotiatedQoS,
            GPRSQoSExtension requestedQoSExtension, GPRSQoSExtension subscribedQoSExtension,
            GPRSQoSExtension negotiatedQoSExtension);

    ROTimeGPRSIfTariffSwitch createROTimeGPRSIfTariffSwitch(Integer roTimeGPRSSinceLastTariffSwitch,
            Integer roTimeGPRSTariffSwitchInterval);

    ROVolumeIfTariffSwitch createROVolumeIfTariffSwitch(Integer roVolumeSinceLastTariffSwitch,
            Integer roVolumeTariffSwitchInterval);

    SGSNCapabilities createSGSNCapabilities(int data);

    SGSNCapabilities createSGSNCapabilities(boolean aoCSupportedBySGSN);

    TimeGPRSIfTariffSwitch createTimeGPRSIfTariffSwitch(int timeGPRSSinceLastTariffSwitch,
            Integer timeGPRSTariffSwitchInterval);

    TransferredVolume createTransferredVolume(Long volumeIfNoTariffSwitch);

    TransferredVolume createTransferredVolume(VolumeIfTariffSwitch volumeIfTariffSwitch);

    TransferredVolumeRollOver createTransferredVolumeRollOver(Integer roVolumeIfNoTariffSwitch);

    TransferredVolumeRollOver createTransferredVolumeRollOver(ROVolumeIfTariffSwitch roVolumeIfTariffSwitch);

    VolumeIfTariffSwitch createVolumeIfTariffSwitch(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval);

    DetachSpecificInformation createDetachSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    DisconnectSpecificInformation createDisconnectSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    PdpContextChangeOfPositionSpecificInformation createPdpContextchangeOfPositionSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress);

    PDPContextEstablishmentAcknowledgementSpecificInformation createPDPContextEstablishmentAcknowledgementSpecificInformation(
            AccessPointName accessPointName, GPRSChargingID chargingID, LocationInformationGPRS locationInformationGPRS,
            EndUserAddress endUserAddress, QualityOfService qualityOfService, TimeAndTimezone timeAndTimezone,
            GSNAddress gsnAddress);

    PDPContextEstablishmentSpecificInformation createPDPContextEstablishmentSpecificInformation(AccessPointName accessPointName, EndUserAddress endUserAddress,
            QualityOfService qualityOfService, LocationInformationGPRS locationInformationGPRS, TimeAndTimezone timeAndTimezone,
            PDPInitiationType pdpInitiationType, boolean secondaryPDPContext);

    TPValidityPeriod createTPValidityPeriod(byte[] data);

    TPShortMessageSpecificInfo createTPShortMessageSpecificInfo(int data);

    TPProtocolIdentifier createTPProtocolIdentifier(int data);

    TPDataCodingScheme createTPDataCodingScheme(int data);

    SMSEvent createSMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode);

    SMSAddressString createSMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address);

    RPCause createRPCause(int data);

    MTSMSCause createMTSMSCause(int data);

    FreeFormatDataSMS createFreeFormatDataSMS(byte[] data);

    FCIBCCCAMELSequence1SMS createFCIBCCCAMELsequence1(FreeFormatDataSMS freeFormatData, AppendFreeFormatData appendFreeFormatData);

    EventSpecificInformationSMS createEventSpecificInformationSMS(OSmsFailureSpecificInfo oSmsFailureSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMS(OSmsSubmissionSpecificInfo oSmsSubmissionSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMS(TSmsFailureSpecificInfo tSmsFailureSpecificInfo);

    EventSpecificInformationSMS createEventSpecificInformationSMS(TSmsDeliverySpecificInfo tSmsDeliverySpecificInfo);

    OSmsFailureSpecificInfo createOSmsFailureSpecificInfo(MOSMSCause failureCause);

    OSmsSubmissionSpecificInfo createOSmsSubmissionSpecificInfo();

    TSmsFailureSpecificInfo createTSmsFailureSpecificInfo(MTSMSCause failureCause);

    TSmsDeliverySpecificInfo createTSmsDeliverySpecificInfo();

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
            throws CAPException;

    HighLayerCompatibilityIsup createHighLayerCompatibilityInap(UserTeleserviceInformation highLayerCompatibility)
            throws CAPException;

    RedirectionInformationIsup createRedirectionInformationInap(RedirectionInformation redirectionInformation)
            throws CAPException;

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
