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

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicatorValue;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.CollectedInfoSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.DpSpecificInfoAltImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionAltImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MetDPCriterionImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEventsImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OServiceChangeSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TServiceChangeSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DetachSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.DisconnectSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentAcknowledgementSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PDPContextEstablishmentSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiGprs.PdpContextChangeOfPositionSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.OSmsSubmissionSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsDeliverySpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.EsiSms.TSmsFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.dialog.CAPGprsReferenceNumber;
import org.restcomm.protocols.ss7.cap.api.isup.BearerCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CallingPartyNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.CauseCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.DigitsImpl;
import org.restcomm.protocols.ss7.cap.api.isup.GenericNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.LocationNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.OriginalCalledNumberCapImpl;
import org.restcomm.protocols.ss7.cap.api.isup.RedirectingPartyIDCapImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.restcomm.protocols.ss7.cap.api.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.BurstImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.BurstListImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CriticalityType;
import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.ErrorTreatment;
import org.restcomm.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.cap.api.primitives.ExtensionFieldImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.cap.api.primitives.ScfIDImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCBeforeAnswerImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AOCSubsequentImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AlertingPatternCapImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.AudibleIndicatorImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BackwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAI_GSM0224Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELAChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CAMELSCIBillingChargingCharacteristicsAltImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallCompletionTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancelImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallingPartyRestrictionIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CarrierImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocationAltImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedDigitsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CollectedInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ConnectedNumberTreatmentInd;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CwTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaAltImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EctTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ForwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.HoldTreatmentIndicator;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InformationToSendImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.LowLayerCompatibilityImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageIDTextImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MidCallControlInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NACarrierInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.NAOliInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeDurationChargingResultImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.ToneImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariableMessageImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartDateImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartPriceImpl;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.VariablePartTimeImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AOCGPRSImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResultImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOverImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOverImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FCIBCCCAMELSequence1GprsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatDataGprsImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtensionImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.InitiatingEntity;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPAddressImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPInitiationType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumberValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganizationValue;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfServiceImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROTimeGPRSIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilitiesImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOverImpl;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FreeFormatDataSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MOSMSCause;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.MTSMSCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCauseImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEventImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPProtocolIdentifierImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPShortMessageSpecificInfoImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.TPValidityPeriodImpl;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInapImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.BothwayThroughConnectionInd;
import org.restcomm.protocols.ss7.inap.api.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMEIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLengthImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.callhandling.UUDataImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.GPRSChargingIDImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationGPRSImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.MSClassmark2Impl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.Ext2QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtQoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4FunctionalitiesImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSSubscribedImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface CAPParameterFactory {

    CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference);

    CauseCapImpl createCauseCap(byte[] data);

    CauseCapImpl createCauseCap(CauseIndicators causeIndicators) throws CAPException;

    DpSpecificCriteriaImpl createDpSpecificCriteria(Integer applicationTimer);

    DpSpecificCriteriaImpl createDpSpecificCriteria(MidCallControlInfoImpl midCallControlInfo);

    DpSpecificCriteriaImpl createDpSpecificCriteria(DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt);

    BCSMEventImpl createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegIDImpl legID,
            DpSpecificCriteriaImpl dpSpecificCriteria, boolean automaticRearm);

    CalledPartyBCDNumberImpl createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws CAPException;

    ExtensionFieldImpl createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data);

    ExtensionFieldImpl createExtensionField(List<Long> globalCode, CriticalityType criticalityType, byte[] data);

    CAPExtensionsImpl createCAPExtensions(List<ExtensionFieldImpl> fieldsList);

    CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, CAPExtensionsImpl extensions,
            Long tariffSwitchInterval);

    CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, CAPExtensionsImpl extensions);

    CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, AudibleIndicatorImpl audibleIndicator, CAPExtensionsImpl extensions);

    DateAndTimeImpl createDateAndTime(int year, int month, int day, int hour, int minute, int second);

    TimeAndTimezoneImpl createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

    SendingLegIDImpl createSendingLegID(LegType sendingSideID);

    ReceivingLegIDImpl createReceivingLegID(LegType receivingSideID);

    BearerCapImpl createBearerCap(byte[] data);

    BearerCapImpl createBearerCap(UserServiceInformation userServiceInformation) throws CAPException;

    BearerCapabilityImpl createBearerCapability(BearerCapImpl bearerCap);

    DigitsImpl createDigits_GenericNumber(byte[] data);

    DigitsImpl createDigits_GenericDigits(byte[] data);

    DigitsImpl createDigits_GenericNumber(GenericNumber genericNumber) throws CAPException;

    DigitsImpl createDigits_GenericDigits(GenericDigits genericDigits) throws CAPException;

    CalledPartyNumberCapImpl createCalledPartyNumberCap(byte[] data);

    CalledPartyNumberCapImpl createCalledPartyNumberCap(CalledPartyNumber calledPartyNumber) throws CAPException;

    CallingPartyNumberCapImpl createCallingPartyNumberCap(byte[] data);

    CallingPartyNumberCapImpl createCallingPartyNumberCap(CallingPartyNumber callingPartyNumber) throws CAPException;

    GenericNumberCapImpl createGenericNumberCap(byte[] data);

    GenericNumberCapImpl createGenericNumberCap(GenericNumber genericNumber) throws CAPException;

    LocationNumberCapImpl createLocationNumberCap(byte[] data);

    LocationNumberCapImpl createLocationNumberCap(LocationNumber locationNumber) throws CAPException;

    OriginalCalledNumberCapImpl createOriginalCalledNumberCap(byte[] data);

    OriginalCalledNumberCapImpl createOriginalCalledNumberCap(OriginalCalledNumber originalCalledNumber) throws CAPException;

    RedirectingPartyIDCapImpl createRedirectingPartyIDCap(byte[] data);

    RedirectingPartyIDCapImpl createRedirectingPartyIDCap(RedirectingNumber redirectingNumber) throws CAPException;

    RouteSelectFailureSpecificInfoImpl createRouteSelectFailureSpecificInfo(CauseCapImpl failureCause);

    OCalledPartyBusySpecificInfoImpl createOCalledPartyBusySpecificInfo(CauseCapImpl busyCause);

    OAbandonSpecificInfoImpl createOAbandonSpecificInfo(boolean routeNotPermitted);

    ONoAnswerSpecificInfoImpl createONoAnswerSpecificInfo();

    OAnswerSpecificInfoImpl createOAnswerSpecificInfo(CalledPartyNumberCapImpl destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicatorImpl chargeIndicator, ExtBasicServiceCodeImpl extBasicServiceCode,
            ExtBasicServiceCodeImpl extBasicServiceCode2);

    ODisconnectSpecificInfoImpl createODisconnectSpecificInfo(CauseCapImpl releaseCause);

    TBusySpecificInfoImpl createTBusySpecificInfo(CauseCapImpl busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberCapImpl forwardingDestinationNumber);

    TNoAnswerSpecificInfoImpl createTNoAnswerSpecificInfo(boolean callForwarded,
            CalledPartyNumberCapImpl forwardingDestinationNumber);

    TAnswerSpecificInfoImpl createTAnswerSpecificInfo(CalledPartyNumberCapImpl destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicatorImpl chargeIndicator, ExtBasicServiceCodeImpl extBasicServiceCode,
            ExtBasicServiceCodeImpl extBasicServiceCode2);

    TDisconnectSpecificInfoImpl createTDisconnectSpecificInfo(CauseCapImpl releaseCause);

    DestinationRoutingAddressImpl createDestinationRoutingAddress(List<CalledPartyNumberCapImpl> calledPartyNumber);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OAnswerSpecificInfoImpl oAnswerSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OMidCallSpecificInfoImpl oMidCallSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(ODisconnectSpecificInfoImpl oDisconnectSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TBusySpecificInfoImpl tBusySpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TAnswerSpecificInfoImpl tAnswerSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TMidCallSpecificInfoImpl tMidCallSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TDisconnectSpecificInfoImpl tDisconnectSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OTermSeizedSpecificInfoImpl oTermSeizedSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OAbandonSpecificInfoImpl oAbandonSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OChangeOfPositionSpecificInfoImpl oChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TChangeOfPositionSpecificInfoImpl tChangeOfPositionSpecificInfo);

    EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(DpSpecificInfoAltImpl dpSpecificInfoAlt);

    RequestedInformationImpl createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue);

    RequestedInformationImpl createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue);

    RequestedInformationImpl createRequestedInformation_CallStopTime(DateAndTimeImpl callStopTimeValue);

    RequestedInformationImpl createRequestedInformation_ReleaseCause(CauseCapImpl releaseCauseValue);

    TimeDurationChargingResultImpl createTimeDurationChargingResult(ReceivingLegIDImpl partyToCharge,
            TimeInformationImpl timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry, CAPExtensionsImpl extensions,
            AChChargingAddressImpl aChChargingAddress);

    TimeIfTariffSwitchImpl createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval);

    TimeInformationImpl createTimeInformation(int timeIfNoTariffSwitch);

    TimeInformationImpl createTimeInformation(TimeIfTariffSwitchImpl timeIfTariffSwitch);

    IPSSPCapabilitiesImpl createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData);

    InitialDPArgExtensionImpl createInitialDPArgExtension(NACarrierInformationImpl naCarrierInformation, ISDNAddressStringImpl gmscAddress);

    InitialDPArgExtensionImpl createInitialDPArgExtension(ISDNAddressStringImpl gmscAddress,
            CalledPartyNumberCapImpl forwardingDestinationNumber, MSClassmark2Impl msClassmark2, IMEIImpl imei,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities,
            BearerCapabilityImpl bearerCapability2, ExtBasicServiceCodeImpl extBasicServiceCode2,
            HighLayerCompatibilityInapImpl highLayerCompatibility2, LowLayerCompatibilityImpl lowLayerCompatibility,
            LowLayerCompatibilityImpl lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUDataImpl uuData,
            boolean collectInformationAllowed, boolean releaseCallArgExtensionAllowed);

    AlertingPatternCapImpl createAlertingPatternCap(AlertingPatternImpl alertingPattern);

    AlertingPatternCapImpl createAlertingPatternCap(byte[] data);

    NAOliInfoImpl createNAOliInfo(int value);

    ScfIDImpl createScfID(byte[] data);

    ServiceInteractionIndicatorsTwoImpl createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionIndImpl forwardServiceInteractionInd,
            BackwardServiceInteractionIndImpl backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator);

    FCIBCCCAMELSequence1Impl createFCIBCCCAMELsequence1(FreeFormatDataImpl freeFormatData, SendingLegIDImpl partyToCharge,
            AppendFreeFormatData appendFreeFormatData);

    CAMELSCIBillingChargingCharacteristicsAltImpl createCAMELSCIBillingChargingCharacteristicsAlt();

    CAI_GSM0224Impl createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7);

    AOCSubsequentImpl createAOCSubsequent(CAI_GSM0224Impl cai_GSM0224, Integer tariffSwitchInterval);

    AOCBeforeAnswerImpl createAOCBeforeAnswer(CAI_GSM0224Impl aocInitial, AOCSubsequentImpl aocSubsequent);

    SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(AOCBeforeAnswerImpl aocBeforeAnswer);

    SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(AOCSubsequentImpl aocSubsequent);

    SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(
            CAMELSCIBillingChargingCharacteristicsAltImpl aocExtension);

    VariablePartPriceImpl createVariablePartPrice(byte[] data);

    VariablePartPriceImpl createVariablePartPrice(double price);

    VariablePartPriceImpl createVariablePartPrice(int integerPart, int hundredthPart);

    VariablePartDateImpl createVariablePartDate(byte[] data);

    VariablePartDateImpl createVariablePartDate(int year, int month, int day);

    VariablePartTimeImpl createVariablePartTime(byte[] data);

    VariablePartTimeImpl createVariablePartTime(int hour, int minute);

    VariablePartImpl createVariablePart(Integer integer);

    VariablePartImpl createVariablePart(DigitsImpl number);

    VariablePartImpl createVariablePart(VariablePartTimeImpl time);

    VariablePartImpl createVariablePart(VariablePartDateImpl date);

    VariablePartImpl createVariablePart(VariablePartPriceImpl price);

    MessageIDTextImpl createMessageIDText(String messageContent, byte[] attributes);

    VariableMessageImpl createVariableMessage(int elementaryMessageID, List<VariablePartImpl> variableParts);

    MessageIDImpl createMessageID(Integer elementaryMessageID);

    MessageIDImpl createMessageID(MessageIDTextImpl text);

    MessageIDImpl createMessageID(List<Integer> elementaryMessageIDs);

    MessageIDImpl createMessageID(VariableMessageImpl variableMessage);

    InbandInfoImpl createInbandInfo(MessageIDImpl messageID, Integer numberOfRepetitions, Integer duration, Integer interval);

    ToneImpl createTone(int toneID, Integer duration);

    InformationToSendImpl createInformationToSend(InbandInfoImpl inbandInfo);

    InformationToSendImpl createInformationToSend(ToneImpl tone);

    CollectedDigitsImpl createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit,
            byte[] cancelDigit, byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack);

    CollectedInfoImpl createCollectedInfo(CollectedDigitsImpl collectedDigits);

    CallSegmentToCancelImpl createCallSegmentToCancel(Integer invokeID, Integer callSegmentID);

    AccessPointNameImpl createAccessPointName(byte[] data);

    AOCGPRSImpl createAOCGPRS(CAI_GSM0224Impl aocInitial, AOCSubsequentImpl aocSubsequent);

    CAMELFCIGPRSBillingChargingCharacteristicsImpl createCAMELFCIGPRSBillingChargingCharacteristics(
            FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1);

    CAMELSCIGPRSBillingChargingCharacteristicsImpl createCAMELSCIGPRSBillingChargingCharacteristics(AOCGPRSImpl aocGPRS,
            PDPIDImpl pdpID);

    ChargingCharacteristicsImpl createChargingCharacteristics(long maxTransferredVolume);

    ChargingCharacteristicsImpl createChargingCharacteristics(int maxElapsedTime);

    ChargingResultImpl createChargingResult(TransferredVolumeImpl transferredVolume);

    ChargingResultImpl createChargingResult(ElapsedTimeImpl elapsedTime);

    ChargingRollOverImpl createChargingRollOver(ElapsedTimeRollOverImpl elapsedTimeRollOver);

    ChargingRollOverImpl createChargingRollOver(TransferredVolumeRollOverImpl transferredVolumeRollOver);

    ElapsedTimeImpl createElapsedTime(Integer timeGPRSIfNoTariffSwitch);

    ElapsedTimeImpl createElapsedTime(TimeGPRSIfTariffSwitchImpl timeGPRSIfTariffSwitch);

    ElapsedTimeRollOverImpl createElapsedTimeRollOver(Integer roTimeGPRSIfNoTariffSwitch);

    ElapsedTimeRollOverImpl createElapsedTimeRollOver(ROTimeGPRSIfTariffSwitchImpl roTimeGPRSIfTariffSwitch);

    EndUserAddressImpl createEndUserAddress(PDPTypeOrganizationImpl pdpTypeOrganization, PDPTypeNumberImpl pdpTypeNumber,
            PDPAddressImpl pdpAddress);

    FCIBCCCAMELSequence1GprsImpl createFCIBCCCAMELsequence1(
            FreeFormatDataGprsImpl freeFormatData, PDPIDImpl pdpID, AppendFreeFormatData appendFreeFormatData);

    FreeFormatDataImpl createFreeFormatData(byte[] data);

    FreeFormatDataGprsImpl createFreeFormatDataGprs(byte[] data);

    GPRSCauseImpl createGPRSCause(int data);

    GPRSEventImpl createGPRSEvent(GPRSEventType gprsEventType, MonitorMode monitorMode);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(LocationInformationGPRSImpl locationInformationGPRS);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(DetachSpecificInformationImpl detachSpecificInformation);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            DisconnectSpecificInformationImpl disconnectSpecificInformation);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PDPContextEstablishmentSpecificInformationImpl pdpContextEstablishmentSpecificInformation);

    GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PDPContextEstablishmentAcknowledgementSpecificInformationImpl pdpContextEstablishmentAcknowledgementSpecificInformation);

    GPRSQoSExtensionImpl createGPRSQoSExtension(Ext2QoSSubscribedImpl supplementToLongQoSFormat);

    GPRSQoSImpl createGPRSQoS(QoSSubscribedImpl shortQoSFormat);

    GPRSQoSImpl createGPRSQoS(ExtQoSSubscribedImpl longQoSFormat);

    PDPAddressImpl createPDPAddress(byte[] data);

    PDPIDImpl createPDPID(int data);

    PDPTypeNumberImpl createPDPTypeNumber(int data);

    PDPTypeNumberImpl createPDPTypeNumber(PDPTypeNumberValue value);

    PDPTypeOrganizationImpl createPDPTypeOrganization(int data);

    PDPTypeOrganizationImpl createPDPTypeOrganization(PDPTypeOrganizationValue value);

    QualityOfServiceImpl createQualityOfService(GPRSQoSImpl requestedQoS, GPRSQoSImpl subscribedQoS, GPRSQoSImpl negotiatedQoS,
            GPRSQoSExtensionImpl requestedQoSExtension, GPRSQoSExtensionImpl subscribedQoSExtension,
            GPRSQoSExtensionImpl negotiatedQoSExtension);

    ROTimeGPRSIfTariffSwitchImpl createROTimeGPRSIfTariffSwitch(Integer roTimeGPRSSinceLastTariffSwitch,
            Integer roTimeGPRSTariffSwitchInterval);

    ROVolumeIfTariffSwitchImpl createROVolumeIfTariffSwitch(Integer roVolumeSinceLastTariffSwitch,
            Integer roVolumeTariffSwitchInterval);

    SGSNCapabilitiesImpl createSGSNCapabilities(int data);

    SGSNCapabilitiesImpl createSGSNCapabilities(boolean aoCSupportedBySGSN);

    TimeGPRSIfTariffSwitchImpl createTimeGPRSIfTariffSwitch(int timeGPRSSinceLastTariffSwitch,
            Integer timeGPRSTariffSwitchInterval);

    TransferredVolumeImpl createTransferredVolume(Long volumeIfNoTariffSwitch);

    TransferredVolumeImpl createTransferredVolume(VolumeIfTariffSwitchImpl volumeIfTariffSwitch);

    TransferredVolumeRollOverImpl createTransferredVolumeRollOver(Integer roVolumeIfNoTariffSwitch);

    TransferredVolumeRollOverImpl createTransferredVolumeRollOver(ROVolumeIfTariffSwitchImpl roVolumeIfTariffSwitch);

    VolumeIfTariffSwitchImpl createVolumeIfTariffSwitch(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval);

    DetachSpecificInformationImpl createDetachSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    DisconnectSpecificInformationImpl createDisconnectSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate);

    PdpContextChangeOfPositionSpecificInformationImpl createPdpContextchangeOfPositionSpecificInformation(
            AccessPointNameImpl accessPointName, GPRSChargingIDImpl chargingID, LocationInformationGPRSImpl locationInformationGPRS,
            EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService, TimeAndTimezoneImpl timeAndTimezone,
            GSNAddressImpl gsnAddress);

    PDPContextEstablishmentAcknowledgementSpecificInformationImpl createPDPContextEstablishmentAcknowledgementSpecificInformation(
            AccessPointNameImpl accessPointName, GPRSChargingIDImpl chargingID, LocationInformationGPRSImpl locationInformationGPRS,
            EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService, TimeAndTimezoneImpl timeAndTimezone,
            GSNAddressImpl gsnAddress);

    PDPContextEstablishmentSpecificInformationImpl createPDPContextEstablishmentSpecificInformation(AccessPointNameImpl accessPointName, EndUserAddressImpl endUserAddress,
            QualityOfServiceImpl qualityOfService, LocationInformationGPRSImpl locationInformationGPRS, TimeAndTimezoneImpl timeAndTimezone,
            PDPInitiationType pdpInitiationType, boolean secondaryPDPContext);

    TPValidityPeriodImpl createTPValidityPeriod(byte[] data);

    TPShortMessageSpecificInfoImpl createTPShortMessageSpecificInfo(int data);

    TPProtocolIdentifierImpl createTPProtocolIdentifier(int data);

    TPDataCodingSchemeImpl createTPDataCodingScheme(int data);

    SMSEventImpl createSMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode);

    SMSAddressStringImpl createSMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address);

    RPCauseImpl createRPCause(int data);

    MTSMSCauseImpl createMTSMSCause(int data);

    FreeFormatDataSMSImpl createFreeFormatDataSMS(byte[] data);

    FCIBCCCAMELSequence1SMSImpl createFCIBCCCAMELsequence1(FreeFormatDataSMSImpl freeFormatData, AppendFreeFormatData appendFreeFormatData);

    EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo);

    EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(OSmsSubmissionSpecificInfoImpl oSmsSubmissionSpecificInfo);

    EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(TSmsFailureSpecificInfoImpl tSmsFailureSpecificInfo);

    EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(TSmsDeliverySpecificInfoImpl tSmsDeliverySpecificInfo);

    OSmsFailureSpecificInfoImpl createOSmsFailureSpecificInfo(MOSMSCause failureCause);

    OSmsSubmissionSpecificInfoImpl createOSmsSubmissionSpecificInfo();

    TSmsFailureSpecificInfoImpl createTSmsFailureSpecificInfo(MTSMSCauseImpl failureCause);

    TSmsDeliverySpecificInfoImpl createTSmsDeliverySpecificInfo();

    LegOrCallSegmentImpl createLegOrCallSegment(Integer callSegmentID);

    LegOrCallSegmentImpl createLegOrCallSegment(LegIDImpl legID);

    ChargeIndicatorImpl createChargeIndicator(int data);

    ChargeIndicatorImpl createChargeIndicator(ChargeIndicatorValue value);

    BackwardServiceInteractionIndImpl createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator);

    CarrierImpl createCarrier(byte[] data);

    ForwardServiceInteractionIndImpl createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator);

    LowLayerCompatibilityImpl createLowLayerCompatibility(byte[] data);

    MidCallEventsImpl createMidCallEvents_Completed(DigitsImpl dtmfDigits);

    MidCallEventsImpl createMidCallEvents_TimeOut(DigitsImpl dtmfDigits);

    OMidCallSpecificInfoImpl createOMidCallSpecificInfo(MidCallEventsImpl midCallEvents);

    TMidCallSpecificInfoImpl createTMidCallSpecificInfo(MidCallEventsImpl midCallEvents);

    OTermSeizedSpecificInfoImpl createOTermSeizedSpecificInfo(LocationInformationImpl locationInformation);

    CallAcceptedSpecificInfoImpl createCallAcceptedSpecificInfo(LocationInformationImpl locationInformation);

    MetDPCriterionAltImpl createMetDPCriterionAlt();

    MetDPCriterionImpl createMetDPCriterion_enteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_leavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_enteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_leavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_enteringLocationAreaId(LAIFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_leavingLocationAreaId(LAIFixedLengthImpl value);

    MetDPCriterionImpl createMetDPCriterion_interSystemHandOverToUMTS();

    MetDPCriterionImpl createMetDPCriterion_interSystemHandOverToGSM();

    MetDPCriterionImpl createMetDPCriterion_interPLMNHandOver();

    MetDPCriterionImpl createMetDPCriterion_interMSCHandOver();

    MetDPCriterionImpl createMetDPCriterion(MetDPCriterionAltImpl metDPCriterionAlt);

    OChangeOfPositionSpecificInfoImpl createOChangeOfPositionSpecificInfo(LocationInformationImpl locationInformation, List<MetDPCriterionImpl> metDPCriteriaList);

    TChangeOfPositionSpecificInfoImpl createTChangeOfPositionSpecificInfo(LocationInformationImpl locationInformation, List<MetDPCriterionImpl> metDPCriteriaList);

    OServiceChangeSpecificInfoImpl createOServiceChangeSpecificInfo(ExtBasicServiceCodeImpl extBasicServiceCode);

    TServiceChangeSpecificInfoImpl createTServiceChangeSpecificInfo(ExtBasicServiceCodeImpl extBasicServiceCode);

    CollectedInfoSpecificInfoImpl createCollectedInfoSpecificInfo(CalledPartyNumberCapImpl calledPartyNumber);

    DpSpecificInfoAltImpl createDpSpecificInfoAlt(OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo, CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo,
            TServiceChangeSpecificInfoImpl tServiceChangeSpecificInfo);

    ChangeOfLocationAltImpl createChangeOfLocationAlt();

    ChangeOfLocationImpl createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    ChangeOfLocationImpl createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value);

    ChangeOfLocationImpl createChangeOfLocation(LAIFixedLengthImpl locationAreaId);

    ChangeOfLocationImpl createChangeOfLocation_interSystemHandOver();

    ChangeOfLocationImpl createChangeOfLocation_interPLMNHandOver();

    ChangeOfLocationImpl createChangeOfLocation_interMSCHandOver();

    ChangeOfLocationImpl createChangeOfLocation(ChangeOfLocationAltImpl changeOfLocationAlt);

    DpSpecificCriteriaAltImpl createDpSpecificCriteriaAlt(List<ChangeOfLocationImpl> changeOfPositionControlInfo, Integer numberOfDigits);

    MidCallControlInfoImpl createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit, String cancelDigit,
            String startDigit, Integer interDigitTimeout);

    BurstImpl createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval);

    BurstListImpl createBurstList(Integer warningPeriod, BurstImpl burst);

    AudibleIndicatorImpl createAudibleIndicator(Boolean tone);

    AudibleIndicatorImpl createAudibleIndicator(BurstListImpl burstList);

    AChChargingAddressImpl createAChChargingAddress(LegIDImpl legID);

    AChChargingAddressImpl createAChChargingAddress(int srfConnection);

}
