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

package org.restcomm.protocols.ss7.cap;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPParameterFactory;
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
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
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
import org.restcomm.protocols.ss7.cap.dialog.CAPGprsReferenceNumberImpl;
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
public class CAPParameterFactoryImpl implements CAPParameterFactory {
    @Override
    public CAPGprsReferenceNumber createCAPGprsReferenceNumber(Integer destinationReference, Integer originationReference) {
        return new CAPGprsReferenceNumberImpl(destinationReference, originationReference);
    }

    @Override
    public RouteSelectFailureSpecificInfoImpl createRouteSelectFailureSpecificInfo(CauseCapImpl failureCause) {
        return new RouteSelectFailureSpecificInfoImpl(failureCause);
    }

    @Override
    public CauseCapImpl createCauseCap(byte[] data) {
        return new CauseCapImpl(data);
    }

    @Override
    public CauseCapImpl createCauseCap(CauseIndicators causeIndicators) throws CAPException {
        return new CauseCapImpl(causeIndicators);
    }

    @Override
    public DpSpecificCriteriaImpl createDpSpecificCriteria(Integer applicationTimer) {
        return new DpSpecificCriteriaImpl(applicationTimer);
    }

    @Override
    public DpSpecificCriteriaImpl createDpSpecificCriteria(MidCallControlInfoImpl midCallControlInfo) {
        return new DpSpecificCriteriaImpl(midCallControlInfo);
    }

    @Override
    public DpSpecificCriteriaImpl createDpSpecificCriteria(DpSpecificCriteriaAltImpl dpSpecificCriteriaAlt) {
        return new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
    }

    @Override
    public BCSMEventImpl createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegIDImpl legID,
            DpSpecificCriteriaImpl dpSpecificCriteria, boolean automaticRearm) {
        return new BCSMEventImpl(eventTypeBCSM, monitorMode, legID, dpSpecificCriteria, automaticRearm);
    }

    @Override
    public CalledPartyBCDNumberImpl createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws CAPException {
        return new CalledPartyBCDNumberImpl(addressNature, numberingPlan, address);
    }

    @Override
    public ExtensionFieldImpl createExtensionField(Integer localCode, CriticalityType criticalityType, byte[] data) {
        return new ExtensionFieldImpl(localCode, criticalityType, data);
    }

    @Override
    public ExtensionFieldImpl createExtensionField(List<Long> globalCode, CriticalityType criticalityType, byte[] data) {
        return new ExtensionFieldImpl(globalCode, criticalityType, data);
    }

    @Override
    public CAPExtensionsImpl createCAPExtensions(List<ExtensionFieldImpl> fieldsList) {
        return new CAPExtensionsImpl(fieldsList);
    }

    @Override
    public CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, Boolean tone, CAPExtensionsImpl extensions,
            Long tariffSwitchInterval) {
        return new CAMELAChBillingChargingCharacteristicsImpl(maxCallPeriodDuration,tone,extensions,tariffSwitchInterval);
    }

    @Override
    public CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration, boolean releaseIfdurationExceeded,
            Long tariffSwitchInterval, Boolean tone, CAPExtensionsImpl extensions) {
        return new CAMELAChBillingChargingCharacteristicsImpl(maxCallPeriodDuration,releaseIfdurationExceeded,tariffSwitchInterval,tone,extensions);
    }

    @Override
    public CAMELAChBillingChargingCharacteristicsImpl createCAMELAChBillingChargingCharacteristics(long maxCallPeriodDuration,
            boolean releaseIfdurationExceeded, Long tariffSwitchInterval, AudibleIndicatorImpl audibleIndicator,
            CAPExtensionsImpl extensions) {
        return new CAMELAChBillingChargingCharacteristicsImpl(maxCallPeriodDuration, releaseIfdurationExceeded,
                tariffSwitchInterval, audibleIndicator, extensions);
    }

    @Override
    public DateAndTimeImpl createDateAndTime(int year, int month, int day, int hour, int minute, int second) {
        return new DateAndTimeImpl(year, month, day, hour, minute, second);
    }

    @Override
    public TimeAndTimezoneImpl createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone) {
        return new TimeAndTimezoneImpl(year, month, day, hour, minute, second, timeZone);
    }

    @Override
    public SendingLegIDImpl createSendingLegIDImpl(LegType sendingSideID) {
        return new SendingLegIDImpl(sendingSideID);
    }

    @Override
    public ReceivingLegIDImpl createReceivingLegIDImpl(LegType receivingSideID) {
        return new ReceivingLegIDImpl(receivingSideID);
    }

    @Override
    public BearerCapImpl createBearerCap(byte[] data) {
        return new BearerCapImpl(data);
    }

    @Override
    public BearerCapImpl createBearerCap(UserServiceInformation userServiceInformation) throws CAPException {
        return new BearerCapImpl(userServiceInformation);
    }

    @Override
    public BearerCapabilityImpl createBearerCapability(BearerCapImpl bearerCap) {
        return new BearerCapabilityImpl(bearerCap);
    }

    @Override
    public DigitsImpl createDigits_GenericNumber(byte[] data) {
        DigitsImpl res = new DigitsImpl(data);
        res.setIsGenericNumber();
        return res;
    }

    @Override
    public DigitsImpl createDigits_GenericDigits(byte[] data) {
        DigitsImpl res = new DigitsImpl(data);
        res.setIsGenericDigits();
        return res;
    }

    @Override
    public DigitsImpl createDigits_GenericNumber(GenericNumber genericNumber) throws CAPException {
        return new DigitsImpl(genericNumber);
    }

    @Override
    public DigitsImpl createDigits_GenericDigits(GenericDigits genericDigits) throws CAPException {
        return new DigitsImpl(genericDigits);
    }

    @Override
    public CalledPartyNumberCapImpl createCalledPartyNumberCap(byte[] data) {
        return new CalledPartyNumberCapImpl(data);
    }

    @Override
    public CalledPartyNumberCapImpl createCalledPartyNumberCap(CalledPartyNumber calledPartyNumber) throws CAPException {
        return new CalledPartyNumberCapImpl(calledPartyNumber);
    }

    @Override
    public CallingPartyNumberCapImpl createCallingPartyNumberCap(byte[] data) {
        return new CallingPartyNumberCapImpl(data);
    }

    @Override
    public CallingPartyNumberCapImpl createCallingPartyNumberCap(CallingPartyNumber callingPartyNumber) throws CAPException {
        return new CallingPartyNumberCapImpl(callingPartyNumber);
    }

    @Override
    public GenericNumberCapImpl createGenericNumberCap(byte[] data) {
        return new GenericNumberCapImpl(data);
    }

    @Override
    public GenericNumberCapImpl createGenericNumberCap(GenericNumber genericNumber) throws CAPException {
        return new GenericNumberCapImpl(genericNumber);
    }

    @Override
    public LocationNumberCapImpl createLocationNumberCap(byte[] data) {
        return new LocationNumberCapImpl(data);
    }

    @Override
    public LocationNumberCapImpl createLocationNumberCap(LocationNumber locationNumber) throws CAPException {
        return new LocationNumberCapImpl(locationNumber);
    }

    @Override
    public OriginalCalledNumberCapImpl createOriginalCalledNumberCap(byte[] data) {
        return new OriginalCalledNumberCapImpl(data);
    }

    @Override
    public OriginalCalledNumberCapImpl createOriginalCalledNumberCap(OriginalCalledNumber originalCalledNumber) throws CAPException {
        return new OriginalCalledNumberCapImpl(originalCalledNumber);
    }

    @Override
    public RedirectingPartyIDCapImpl createRedirectingPartyIDCap(byte[] data) {
        return new RedirectingPartyIDCapImpl(data);
    }

    @Override
    public RedirectingPartyIDCapImpl createRedirectingPartyIDCap(RedirectingNumber redirectingNumber) throws CAPException {
        return new RedirectingPartyIDCapImpl(redirectingNumber);
    }

    @Override
    public OCalledPartyBusySpecificInfoImpl createOCalledPartyBusySpecificInfo(CauseCapImpl busyCause) {
        return new OCalledPartyBusySpecificInfoImpl(busyCause);
    }

    @Override
    public OAbandonSpecificInfoImpl createOAbandonSpecificInfo(boolean routeNotPermitted) {
        return new OAbandonSpecificInfoImpl(routeNotPermitted);
    }

    @Override
    public ONoAnswerSpecificInfoImpl createONoAnswerSpecificInfo() {
        return new ONoAnswerSpecificInfoImpl();
    }

    @Override
    public OAnswerSpecificInfoImpl createOAnswerSpecificInfo(CalledPartyNumberCapImpl destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicatorImpl chargeIndicator, ExtBasicServiceCodeImpl extBasicServiceCode,
            ExtBasicServiceCodeImpl extBasicServiceCode2) {
        return new OAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode,
                extBasicServiceCode2);
    }

    @Override
    public ODisconnectSpecificInfoImpl createODisconnectSpecificInfo(CauseCapImpl releaseCause) {
        return new ODisconnectSpecificInfoImpl(releaseCause);
    }

    @Override
    public TBusySpecificInfoImpl createTBusySpecificInfo(CauseCapImpl busyCause, boolean callForwarded, boolean routeNotPermitted,
            CalledPartyNumberCapImpl forwardingDestinationNumber) {
        return new TBusySpecificInfoImpl(busyCause, callForwarded, routeNotPermitted, forwardingDestinationNumber);
    }

    @Override
    public TNoAnswerSpecificInfoImpl createTNoAnswerSpecificInfo(boolean callForwarded,
            CalledPartyNumberCapImpl forwardingDestinationNumber) {
        return new TNoAnswerSpecificInfoImpl(callForwarded, forwardingDestinationNumber);
    }

    @Override
    public TAnswerSpecificInfoImpl createTAnswerSpecificInfo(CalledPartyNumberCapImpl destinationAddress, boolean orCall,
            boolean forwardedCall, ChargeIndicatorImpl chargeIndicator, ExtBasicServiceCodeImpl extBasicServiceCode,
            ExtBasicServiceCodeImpl extBasicServiceCode2) {
        return new TAnswerSpecificInfoImpl(destinationAddress, orCall, forwardedCall, chargeIndicator, extBasicServiceCode,
                extBasicServiceCode2);
    }

    @Override
    public TDisconnectSpecificInfoImpl createTDisconnectSpecificInfo(CauseCapImpl releaseCause) {
        return new TDisconnectSpecificInfoImpl(releaseCause);
    }

    @Override
    public DestinationRoutingAddressImpl createDestinationRoutingAddress(List<CalledPartyNumberCapImpl> calledPartyNumber) {
        return new DestinationRoutingAddressImpl(calledPartyNumber);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            RouteSelectFailureSpecificInfoImpl routeSelectFailureSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            OCalledPartyBusySpecificInfoImpl oCalledPartyBusySpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oCalledPartyBusySpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(ONoAnswerSpecificInfoImpl oNoAnswerSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OAnswerSpecificInfoImpl oAnswerSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OMidCallSpecificInfoImpl oMidCallSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(ODisconnectSpecificInfoImpl oDisconnectSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TBusySpecificInfoImpl tBusySpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tBusySpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TNoAnswerSpecificInfoImpl tNoAnswerSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tNoAnswerSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TAnswerSpecificInfoImpl tAnswerSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tAnswerSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TMidCallSpecificInfoImpl tMidCallSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tMidCallSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(TDisconnectSpecificInfoImpl tDisconnectSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tDisconnectSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OTermSeizedSpecificInfoImpl oTermSeizedSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oTermSeizedSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(CallAcceptedSpecificInfoImpl callAcceptedSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(callAcceptedSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(OAbandonSpecificInfoImpl oAbandonSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oAbandonSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            OChangeOfPositionSpecificInfoImpl oChangeOfPositionSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(oChangeOfPositionSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(
            TChangeOfPositionSpecificInfoImpl tChangeOfPositionSpecificInfo) {
        return new EventSpecificInformationBCSMImpl(tChangeOfPositionSpecificInfo);
    }

    @Override
    public EventSpecificInformationBCSMImpl createEventSpecificInformationBCSM(DpSpecificInfoAltImpl dpSpecificInfoAlt) {
        return new EventSpecificInformationBCSMImpl(dpSpecificInfoAlt);
    }

    @Override
    public RequestedInformationImpl createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue) {
        return new RequestedInformationImpl(RequestedInformationType.callAttemptElapsedTime, callAttemptElapsedTimeValue);
    }

    @Override
    public RequestedInformationImpl createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue) {
        return new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime, callConnectedElapsedTimeValue);
    }

    @Override
    public RequestedInformationImpl createRequestedInformation_CallStopTime(DateAndTimeImpl callStopTimeValue) {
        return new RequestedInformationImpl(callStopTimeValue);
    }

    @Override
    public RequestedInformationImpl createRequestedInformation_ReleaseCause(CauseCapImpl releaseCauseValue) {
        return new RequestedInformationImpl(releaseCauseValue);
    }

    @Override
    public TimeDurationChargingResultImpl createTimeDurationChargingResult(ReceivingLegIDImpl partyToCharge,
            TimeInformationImpl timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry, CAPExtensionsImpl extensions,
            AChChargingAddressImpl aChChargingAddress) {
        return new TimeDurationChargingResultImpl(partyToCharge, timeInformation, legActive, callLegReleasedAtTcpExpiry,
                extensions, aChChargingAddress);
    }

    @Override
    public TimeIfTariffSwitchImpl createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
        return new TimeIfTariffSwitchImpl(timeSinceTariffSwitch, tariffSwitchInterval);
    }

    @Override
    public TimeInformationImpl createTimeInformation(int timeIfNoTariffSwitch) {
        return new TimeInformationImpl(timeIfNoTariffSwitch);
    }

    @Override
    public TimeInformationImpl createTimeInformation(TimeIfTariffSwitchImpl timeIfTariffSwitch) {
        return new TimeInformationImpl(timeIfTariffSwitch);
    }

    @Override
    public IPSSPCapabilitiesImpl createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
            boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, byte[] extraData) {
        return new IPSSPCapabilitiesImpl(IPRoutingAddressSupported, VoiceBackSupported,
                VoiceInformationSupportedViaSpeechRecognition, VoiceInformationSupportedViaVoiceRecognition,
                GenerationOfVoiceAnnouncementsFromTextSupported, extraData);
    }

    @Override
    public InitialDPArgExtensionImpl createInitialDPArgExtension(NACarrierInformationImpl naCarrierInformation, ISDNAddressStringImpl gmscAddress) {
        return new InitialDPArgExtensionImpl(naCarrierInformation, gmscAddress);
    }

    @Override
    public InitialDPArgExtensionImpl createInitialDPArgExtension(ISDNAddressStringImpl gmscAddress,
            CalledPartyNumberCapImpl forwardingDestinationNumber, MSClassmark2Impl msClassmark2, IMEIImpl imei,
            SupportedCamelPhasesImpl supportedCamelPhases, OfferedCamel4FunctionalitiesImpl offeredCamel4Functionalities,
            BearerCapabilityImpl bearerCapability2, ExtBasicServiceCodeImpl extBasicServiceCode2,
            HighLayerCompatibilityInapImpl highLayerCompatibility2, LowLayerCompatibilityImpl lowLayerCompatibility,
            LowLayerCompatibilityImpl lowLayerCompatibility2, boolean enhancedDialledServicesAllowed, UUDataImpl uuData,
            boolean collectInformationAllowed, boolean releaseCallArgExtensionAllowed) {
        return new InitialDPArgExtensionImpl(gmscAddress, forwardingDestinationNumber, msClassmark2, imei,
                supportedCamelPhases, offeredCamel4Functionalities, bearerCapability2, extBasicServiceCode2,
                highLayerCompatibility2, lowLayerCompatibility, lowLayerCompatibility2, enhancedDialledServicesAllowed, uuData,
                collectInformationAllowed, releaseCallArgExtensionAllowed);
    }

    @Override
    public AlertingPatternCapImpl createAlertingPatternCap(AlertingPatternImpl alertingPattern) {
        return new AlertingPatternCapImpl(alertingPattern);
    }

    @Override
    public AlertingPatternCapImpl createAlertingPatternCap(byte[] data) {
        return new AlertingPatternCapImpl(data);
    }

    @Override
    public NAOliInfoImpl createNAOliInfo(int value) {
        return new NAOliInfoImpl(value);
    }

    @Override
    public ScfIDImpl createScfID(byte[] data) {
        return new ScfIDImpl(data);
    }

    @Override
    public ServiceInteractionIndicatorsTwoImpl createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionIndImpl forwardServiceInteractionInd,
            BackwardServiceInteractionIndImpl backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator) {
        return new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
                bothwayThroughConnectionInd, connectedNumberTreatmentInd, nonCUGCall, holdTreatmentIndicator,
                cwTreatmentIndicator, ectTreatmentIndicator);
    }

    @Override
    public FCIBCCCAMELSequence1Impl createFCIBCCCAMELsequence1(FreeFormatDataImpl freeFormatData, SendingLegIDImpl partyToCharge,
            AppendFreeFormatData appendFreeFormatData) {
        return new FCIBCCCAMELSequence1Impl(freeFormatData, partyToCharge, appendFreeFormatData);
    }

    @Override
    public CAMELSCIBillingChargingCharacteristicsAltImpl createCAMELSCIBillingChargingCharacteristicsAlt() {
        return new CAMELSCIBillingChargingCharacteristicsAltImpl();
    }

    @Override
    public CAI_GSM0224Impl createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7) {
        return new CAI_GSM0224Impl(e1, e2, e3, e4, e5, e6, e7);
    }

    @Override
    public AOCSubsequentImpl createAOCSubsequent(CAI_GSM0224Impl cai_GSM0224, Integer tariffSwitchInterval) {
        return new AOCSubsequentImpl(cai_GSM0224, tariffSwitchInterval);
    }

    @Override
    public AOCBeforeAnswerImpl createAOCBeforeAnswer(CAI_GSM0224Impl aocInitial, AOCSubsequentImpl aocSubsequent) {
        return new AOCBeforeAnswerImpl(aocInitial, aocSubsequent);
    }

    @Override
    public SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(AOCBeforeAnswerImpl aocBeforeAnswer) {
        return new SCIBillingChargingCharacteristicsImpl(aocBeforeAnswer);
    }

    @Override
    public SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(AOCSubsequentImpl aocSubsequent) {
        return new SCIBillingChargingCharacteristicsImpl(aocSubsequent);
    }

    @Override
    public SCIBillingChargingCharacteristicsImpl createSCIBillingChargingCharacteristics(
            CAMELSCIBillingChargingCharacteristicsAltImpl aocExtension) {
        return new SCIBillingChargingCharacteristicsImpl(aocExtension);
    }

    @Override
    public VariablePartPriceImpl createVariablePartPrice(byte[] data) {
        return new VariablePartPriceImpl(data);
    }

    @Override
    public VariablePartPriceImpl createVariablePartPrice(double price) {
        return new VariablePartPriceImpl(price);
    }

    @Override
    public VariablePartPriceImpl createVariablePartPrice(int integerPart, int hundredthPart) {
        return new VariablePartPriceImpl(integerPart, hundredthPart);
    }

    @Override
    public VariablePartDateImpl createVariablePartDate(byte[] data) {
        return new VariablePartDateImpl(data);
    }

    @Override
    public VariablePartDateImpl createVariablePartDate(int year, int month, int day) {
        return new VariablePartDateImpl(year, month, day);
    }

    @Override
    public VariablePartTimeImpl createVariablePartTime(byte[] data) {
        return new VariablePartTimeImpl(data);
    }

    @Override
    public VariablePartTimeImpl createVariablePartTime(int hour, int minute) {
        return new VariablePartTimeImpl(hour, minute);
    }

    @Override
    public VariablePartImpl createVariablePart(Integer integer) {
        return new VariablePartImpl(integer);
    }

    @Override
    public VariablePartImpl createVariablePart(DigitsImpl number) {
        return new VariablePartImpl(number);
    }

    @Override
    public VariablePartImpl createVariablePart(VariablePartTimeImpl time) {
        return new VariablePartImpl(time);
    }

    @Override
    public VariablePartImpl createVariablePart(VariablePartDateImpl date) {
        return new VariablePartImpl(date);
    }

    @Override
    public VariablePartImpl createVariablePart(VariablePartPriceImpl price) {
        return new VariablePartImpl(price);
    }

    @Override
    public MessageIDTextImpl createMessageIDText(String messageContent, byte[] attributes) {
        return new MessageIDTextImpl(messageContent, attributes);
    }

    @Override
    public VariableMessageImpl createVariableMessage(int elementaryMessageID, List<VariablePartImpl> variableParts) {
        return new VariableMessageImpl(elementaryMessageID, variableParts);
    }

    @Override
    public MessageIDImpl createMessageID(Integer elementaryMessageID) {
        return new MessageIDImpl(elementaryMessageID);
    }

    @Override
    public MessageIDImpl createMessageID(MessageIDTextImpl text) {
        return new MessageIDImpl(text);
    }

    @Override
    public MessageIDImpl createMessageID(List<Integer> elementaryMessageIDs) {
        return new MessageIDImpl(elementaryMessageIDs);
    }

    @Override
    public MessageIDImpl createMessageID(VariableMessageImpl variableMessage) {
        return new MessageIDImpl(variableMessage);
    }

    @Override
    public InbandInfoImpl createInbandInfo(MessageIDImpl messageID, Integer numberOfRepetitions, Integer duration, Integer interval) {
        return new InbandInfoImpl(messageID, numberOfRepetitions, duration, interval);
    }

    @Override
    public ToneImpl createTone(int toneID, Integer duration) {
        return new ToneImpl(toneID, duration);
    }

    @Override
    public InformationToSendImpl createInformationToSend(InbandInfoImpl inbandInfo) {
        return new InformationToSendImpl(inbandInfo);
    }

    @Override
    public InformationToSendImpl createInformationToSend(ToneImpl tone) {
        return new InformationToSendImpl(tone);
    }

    @Override
    public CollectedDigitsImpl createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, byte[] endOfReplyDigit,
            byte[] cancelDigit, byte[] startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack) {
        return new CollectedDigitsImpl(minimumNbOfDigits, maximumNbOfDigits, endOfReplyDigit, cancelDigit, startDigit,
                firstDigitTimeOut, interDigitTimeOut, errorTreatment, interruptableAnnInd, voiceInformation, voiceBack);
    }

    @Override
    public CollectedInfoImpl createCollectedInfo(CollectedDigitsImpl collectedDigits) {
        return new CollectedInfoImpl(collectedDigits);
    }

    @Override
    public CallSegmentToCancelImpl createCallSegmentToCancel(Integer invokeID, Integer callSegmentID) {
        return new CallSegmentToCancelImpl(invokeID, callSegmentID);
    }

    @Override
    public AccessPointNameImpl createAccessPointName(byte[] data) {
        return new AccessPointNameImpl(data);
    }

    @Override
    public AOCGPRSImpl createAOCGPRS(CAI_GSM0224Impl aocInitial, AOCSubsequentImpl aocSubsequent) {
        return new AOCGPRSImpl(aocInitial, aocSubsequent);
    }

    @Override
    public CAMELFCIGPRSBillingChargingCharacteristicsImpl createCAMELFCIGPRSBillingChargingCharacteristics(
            FCIBCCCAMELSequence1GprsImpl fcIBCCCAMELsequence1) {

        return new CAMELFCIGPRSBillingChargingCharacteristicsImpl(fcIBCCCAMELsequence1);
    }

    @Override
    public CAMELSCIGPRSBillingChargingCharacteristicsImpl createCAMELSCIGPRSBillingChargingCharacteristics(AOCGPRSImpl aocGPRS,
            PDPIDImpl pdpID) {

        return new CAMELSCIGPRSBillingChargingCharacteristicsImpl(aocGPRS, pdpID);
    }

    @Override
    public ChargingCharacteristicsImpl createChargingCharacteristics(long maxTransferredVolume) {
        return new ChargingCharacteristicsImpl(maxTransferredVolume);
    }

    @Override
    public ChargingCharacteristicsImpl createChargingCharacteristics(int maxElapsedTime) {
        return new ChargingCharacteristicsImpl(maxElapsedTime);
    }

    @Override
    public ChargingResultImpl createChargingResult(TransferredVolumeImpl transferredVolume) {
        return new ChargingResultImpl(transferredVolume);
    }

    @Override
    public ChargingResultImpl createChargingResult(ElapsedTimeImpl elapsedTime) {
        return new ChargingResultImpl(elapsedTime);
    }

    @Override
    public ChargingRollOverImpl createChargingRollOver(ElapsedTimeRollOverImpl elapsedTimeRollOver) {
        return new ChargingRollOverImpl(elapsedTimeRollOver);
    }

    @Override
    public ChargingRollOverImpl createChargingRollOver(TransferredVolumeRollOverImpl transferredVolumeRollOver) {
        return new ChargingRollOverImpl(transferredVolumeRollOver);
    }

    @Override
    public ElapsedTimeImpl createElapsedTime(Integer timeGPRSIfNoTariffSwitch) {
        return new ElapsedTimeImpl(timeGPRSIfNoTariffSwitch);
    }

    @Override
    public ElapsedTimeImpl createElapsedTime(TimeGPRSIfTariffSwitchImpl timeGPRSIfTariffSwitch) {
        return new ElapsedTimeImpl(timeGPRSIfTariffSwitch);
    }

    @Override
    public ElapsedTimeRollOverImpl createElapsedTimeRollOver(Integer roTimeGPRSIfNoTariffSwitch) {
        return new ElapsedTimeRollOverImpl(roTimeGPRSIfNoTariffSwitch);
    }

    @Override
    public ElapsedTimeRollOverImpl createElapsedTimeRollOver(ROTimeGPRSIfTariffSwitchImpl roTimeGPRSIfTariffSwitch) {
        return new ElapsedTimeRollOverImpl(roTimeGPRSIfTariffSwitch);
    }

    @Override
    public EndUserAddressImpl createEndUserAddress(PDPTypeOrganizationImpl pdpTypeOrganization, PDPTypeNumberImpl pdpTypeNumber,
            PDPAddressImpl pdpAddress) {
        return new EndUserAddressImpl(pdpTypeOrganization, pdpTypeNumber, pdpAddress);
    }

    @Override
    public FCIBCCCAMELSequence1GprsImpl createFCIBCCCAMELsequence1(
            FreeFormatDataGprsImpl freeFormatData, PDPIDImpl pdpID, AppendFreeFormatData appendFreeFormatData) {
        return new FCIBCCCAMELSequence1GprsImpl(freeFormatData, pdpID,
                appendFreeFormatData);
    }

    @Override
    public FreeFormatDataGprsImpl createFreeFormatDataGprs(byte[] data) {
        return new FreeFormatDataGprsImpl(data);
    }

    @Override
    public GPRSCauseImpl createGPRSCause(int data) {
        return new GPRSCauseImpl(data);
    }

    @Override
    public GPRSEventImpl createGPRSEvent(GPRSEventType gprsEventType, MonitorMode monitorMode) {
        return new GPRSEventImpl(gprsEventType, monitorMode);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(LocationInformationGPRSImpl locationInformationGPRS) {
        return new GPRSEventSpecificInformationImpl(locationInformationGPRS);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PdpContextChangeOfPositionSpecificInformationImpl pdpContextchangeOfPositionSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextchangeOfPositionSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(DetachSpecificInformationImpl detachSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(detachSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            DisconnectSpecificInformationImpl disconnectSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(disconnectSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PDPContextEstablishmentSpecificInformationImpl pdpContextEstablishmentSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextEstablishmentSpecificInformation);
    }

    @Override
    public GPRSEventSpecificInformationImpl createGPRSEventSpecificInformation(
            PDPContextEstablishmentAcknowledgementSpecificInformationImpl pdpContextEstablishmentAcknowledgementSpecificInformation) {
        return new GPRSEventSpecificInformationImpl(pdpContextEstablishmentAcknowledgementSpecificInformation);
    }

    @Override
    public GPRSQoSExtensionImpl createGPRSQoSExtension(Ext2QoSSubscribedImpl supplementToLongQoSFormat) {
        return new GPRSQoSExtensionImpl(supplementToLongQoSFormat);
    }

    @Override
    public GPRSQoSImpl createGPRSQoS(QoSSubscribedImpl shortQoSFormat) {
        return new GPRSQoSImpl(shortQoSFormat);
    }

    @Override
    public GPRSQoSImpl createGPRSQoS(ExtQoSSubscribedImpl longQoSFormat) {
        return new GPRSQoSImpl(longQoSFormat);
    }

    @Override
    public PDPAddressImpl createPDPAddress(byte[] data) {
        return new PDPAddressImpl(data);
    }

    @Override
    public PDPIDImpl createPDPID(int data) {
        return new PDPIDImpl(data);
    }

    @Override
    public PDPTypeNumberImpl createPDPTypeNumber(int data) {
        return new PDPTypeNumberImpl(data);
    }

    @Override
    public PDPTypeNumberImpl createPDPTypeNumber(PDPTypeNumberValue value) {
        return new PDPTypeNumberImpl(value);
    }

    @Override
    public PDPTypeOrganizationImpl createPDPTypeOrganization(int data) {
        return new PDPTypeOrganizationImpl(data);
    }

    @Override
    public PDPTypeOrganizationImpl createPDPTypeOrganization(PDPTypeOrganizationValue value) {
        return new PDPTypeOrganizationImpl(value);
    }

    @Override
    public QualityOfServiceImpl createQualityOfService(GPRSQoSImpl requestedQoS, GPRSQoSImpl subscribedQoS, GPRSQoSImpl negotiatedQoS,
            GPRSQoSExtensionImpl requestedQoSExtension, GPRSQoSExtensionImpl subscribedQoSExtension,
            GPRSQoSExtensionImpl negotiatedQoSExtension) {
        return new QualityOfServiceImpl(requestedQoS, subscribedQoS, negotiatedQoS, requestedQoSExtension,
                subscribedQoSExtension, negotiatedQoSExtension);
    }

    @Override
    public ROTimeGPRSIfTariffSwitchImpl createROTimeGPRSIfTariffSwitch(Integer roTimeGPRSSinceLastTariffSwitch,
            Integer roTimeGPRSTariffSwitchInterval) {
        return new ROTimeGPRSIfTariffSwitchImpl(roTimeGPRSSinceLastTariffSwitch, roTimeGPRSTariffSwitchInterval);
    }

    @Override
    public ROVolumeIfTariffSwitchImpl createROVolumeIfTariffSwitch(Integer roVolumeSinceLastTariffSwitch,
            Integer roVolumeTariffSwitchInterval) {
        return new ROVolumeIfTariffSwitchImpl(roVolumeSinceLastTariffSwitch, roVolumeTariffSwitchInterval);
    }

    @Override
    public SGSNCapabilitiesImpl createSGSNCapabilities(int data) {
        return new SGSNCapabilitiesImpl(data);
    }

    @Override
    public SGSNCapabilitiesImpl createSGSNCapabilities(boolean aoCSupportedBySGSN) {
        return new SGSNCapabilitiesImpl(aoCSupportedBySGSN);
    }

    @Override
    public TimeGPRSIfTariffSwitchImpl createTimeGPRSIfTariffSwitch(int timeGPRSSinceLastTariffSwitch,
            Integer timeGPRSTariffSwitchInterval) {
        return new TimeGPRSIfTariffSwitchImpl(timeGPRSSinceLastTariffSwitch, timeGPRSTariffSwitchInterval);
    }

    @Override
    public TransferredVolumeImpl createTransferredVolume(Long volumeIfNoTariffSwitch) {
        return new TransferredVolumeImpl(volumeIfNoTariffSwitch);
    }

    @Override
    public TransferredVolumeImpl createTransferredVolume(VolumeIfTariffSwitchImpl volumeIfTariffSwitch) {
        return new TransferredVolumeImpl(volumeIfTariffSwitch);
    }

    @Override
    public TransferredVolumeRollOverImpl createTransferredVolumeRollOver(Integer roVolumeIfNoTariffSwitch) {
        return new TransferredVolumeRollOverImpl(roVolumeIfNoTariffSwitch);
    }

    @Override
    public TransferredVolumeRollOverImpl createTransferredVolumeRollOver(ROVolumeIfTariffSwitchImpl roVolumeIfTariffSwitch) {
        return new TransferredVolumeRollOverImpl(roVolumeIfTariffSwitch);
    }

    @Override
    public VolumeIfTariffSwitchImpl createVolumeIfTariffSwitch(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval) {
        return new VolumeIfTariffSwitchImpl(volumeSinceLastTariffSwitch, volumeTariffSwitchInterval);
    }

    @Override
    public DetachSpecificInformationImpl createDetachSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate) {
        return new DetachSpecificInformationImpl(initiatingEntity, routeingAreaUpdate);
    }

    @Override
    public DisconnectSpecificInformationImpl createDisconnectSpecificInformation(InitiatingEntity initiatingEntity,
            boolean routeingAreaUpdate) {
        return new DisconnectSpecificInformationImpl(initiatingEntity, routeingAreaUpdate);
    }

    @Override
    public PdpContextChangeOfPositionSpecificInformationImpl createPdpContextchangeOfPositionSpecificInformation(
            AccessPointNameImpl accessPointName, GPRSChargingIDImpl chargingID, LocationInformationGPRSImpl locationInformationGPRS,
            EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService, TimeAndTimezoneImpl timeAndTimezone,
            GSNAddressImpl gsnAddress) {
        return new PdpContextChangeOfPositionSpecificInformationImpl(accessPointName, chargingID, locationInformationGPRS,
                endUserAddress, qualityOfService, timeAndTimezone, gsnAddress);
    }

    @Override
    public PDPContextEstablishmentAcknowledgementSpecificInformationImpl createPDPContextEstablishmentAcknowledgementSpecificInformation(
            AccessPointNameImpl accessPointName, GPRSChargingIDImpl chargingID, LocationInformationGPRSImpl locationInformationGPRS,
            EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService, TimeAndTimezoneImpl timeAndTimezone,
            GSNAddressImpl gsnAddress) {
        return new PDPContextEstablishmentAcknowledgementSpecificInformationImpl(accessPointName, chargingID,
                locationInformationGPRS, endUserAddress, qualityOfService, timeAndTimezone, gsnAddress);
    }

    @Override
    public PDPContextEstablishmentSpecificInformationImpl createPDPContextEstablishmentSpecificInformation(
            AccessPointNameImpl accessPointName, EndUserAddressImpl endUserAddress, QualityOfServiceImpl qualityOfService,
            LocationInformationGPRSImpl locationInformationGPRS, TimeAndTimezoneImpl timeAndTimezone,
            PDPInitiationType pdpInitiationType, boolean secondaryPDPContext) {
        return new PDPContextEstablishmentSpecificInformationImpl(accessPointName, endUserAddress, qualityOfService,
                locationInformationGPRS, timeAndTimezone, pdpInitiationType, secondaryPDPContext);
    }

    @Override
    public TPValidityPeriodImpl createTPValidityPeriod(byte[] data) {
        return new TPValidityPeriodImpl(data);
    }

    @Override
    public TPShortMessageSpecificInfoImpl createTPShortMessageSpecificInfo(int data) {
        return new TPShortMessageSpecificInfoImpl(data);
    }

    @Override
    public TPProtocolIdentifierImpl createTPProtocolIdentifier(int data) {
        return new TPProtocolIdentifierImpl(data);
    }

    @Override
    public TPDataCodingSchemeImpl createTPDataCodingScheme(int data) {
        return new TPDataCodingSchemeImpl(data);
    }

    @Override
    public SMSEventImpl createSMSEvent(EventTypeSMS eventTypeSMS, MonitorMode monitorMode) {
        return new SMSEventImpl(eventTypeSMS, monitorMode);
    }

    @Override
    public SMSAddressStringImpl createSMSAddressString(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        return new SMSAddressStringImpl(addressNature, numberingPlan, address);
    }

    @Override
    public RPCauseImpl createRPCause(int data) {
        return new RPCauseImpl(data);
    }

    @Override
    public MTSMSCauseImpl createMTSMSCause(int data) {
        return new MTSMSCauseImpl(data);
    }

    @Override
    public FreeFormatDataImpl createFreeFormatData(byte[] data) {
        return new FreeFormatDataImpl(data);
    }

    @Override
    public FCIBCCCAMELSequence1SMSImpl createFCIBCCCAMELsequence1(
            FreeFormatDataSMSImpl freeFormatData,
            AppendFreeFormatData appendFreeFormatData) {
        return new FCIBCCCAMELSequence1SMSImpl(freeFormatData,
                appendFreeFormatData);
    }

    @Override
    public EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(
            OSmsFailureSpecificInfoImpl oSmsFailureSpecificInfo) {
        return new EventSpecificInformationSMSImpl(oSmsFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(
            OSmsSubmissionSpecificInfoImpl oSmsSubmissionSpecificInfo) {
        return new EventSpecificInformationSMSImpl(oSmsSubmissionSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(
            TSmsFailureSpecificInfoImpl tSmsFailureSpecificInfo) {
        return new EventSpecificInformationSMSImpl(tSmsFailureSpecificInfo);
    }

    @Override
    public EventSpecificInformationSMSImpl createEventSpecificInformationSMSImpl(
            TSmsDeliverySpecificInfoImpl tSmsDeliverySpecificInfo) {
        return new EventSpecificInformationSMSImpl(tSmsDeliverySpecificInfo);
    }

    @Override
    public FreeFormatDataSMSImpl createFreeFormatDataSMS(byte[] data) {
        return new FreeFormatDataSMSImpl(data);
    }

    @Override
    public OSmsFailureSpecificInfoImpl createOSmsFailureSpecificInfo(MOSMSCause failureCause) {
        return new OSmsFailureSpecificInfoImpl(failureCause);
    }

    @Override
    public OSmsSubmissionSpecificInfoImpl createOSmsSubmissionSpecificInfo() {
        return new OSmsSubmissionSpecificInfoImpl();
    }

    @Override
    public TSmsFailureSpecificInfoImpl createTSmsFailureSpecificInfo(MTSMSCauseImpl failureCause) {
        return new TSmsFailureSpecificInfoImpl(failureCause);
    }

    @Override
    public TSmsDeliverySpecificInfoImpl createTSmsDeliverySpecificInfo() {
        return new TSmsDeliverySpecificInfoImpl();
    }

    @Override
    public LegOrCallSegmentImpl createLegOrCallSegment(Integer callSegmentID) {
        return new LegOrCallSegmentImpl(callSegmentID);
    }

    @Override
    public LegOrCallSegmentImpl createLegOrCallSegment(LegIDImpl legID) {
        return new LegOrCallSegmentImpl(legID);
    }

    @Override
    public ChargeIndicatorImpl createChargeIndicator(int data) {
        return new ChargeIndicatorImpl(data);
    }

    @Override
    public ChargeIndicatorImpl createChargeIndicator(ChargeIndicatorValue value) {
        return new ChargeIndicatorImpl(value);
    }

    @Override
    public BackwardServiceInteractionIndImpl createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
        return new BackwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callCompletionTreatmentIndicator);
    }

    @Override
    public CarrierImpl createCarrier(byte[] data) {
        return new CarrierImpl(data);
    }

    @Override
    public ForwardServiceInteractionIndImpl createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
        return new ForwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callDiversionTreatmentIndicator, callingPartyRestrictionIndicator);
    }

    @Override
    public LowLayerCompatibilityImpl createLowLayerCompatibility(byte[] data) {
        return new LowLayerCompatibilityImpl(data);
    }

    @Override
    public MidCallEventsImpl createMidCallEvents_Completed(DigitsImpl dtmfDigits) {
        return new MidCallEventsImpl(dtmfDigits, true);
    }

    @Override
    public MidCallEventsImpl createMidCallEvents_TimeOut(DigitsImpl dtmfDigits) {
        return new MidCallEventsImpl(dtmfDigits, false);
    }

    @Override
    public OMidCallSpecificInfoImpl createOMidCallSpecificInfo(MidCallEventsImpl midCallEvents) {
        return new OMidCallSpecificInfoImpl(midCallEvents);
    }

    @Override
    public TMidCallSpecificInfoImpl createTMidCallSpecificInfo(MidCallEventsImpl midCallEvents) {
        return new TMidCallSpecificInfoImpl(midCallEvents);
    }

    @Override
    public OTermSeizedSpecificInfoImpl createOTermSeizedSpecificInfo(LocationInformationImpl locationInformation) {
        return new OTermSeizedSpecificInfoImpl(locationInformation);
    }

    @Override
    public CallAcceptedSpecificInfoImpl createCallAcceptedSpecificInfo(LocationInformationImpl locationInformation) {
        return new CallAcceptedSpecificInfoImpl(locationInformation);
    }

    @Override
    public MetDPCriterionAltImpl createMetDPCriterionAlt() {
        return new MetDPCriterionAltImpl();
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_enteringCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringCellGlobalId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_leavingCellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingCellGlobalId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_enteringServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.enteringServiceAreaId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_leavingServiceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.leavingServiceAreaId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_enteringLocationAreaId(LAIFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.enteringLocationAreaId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_leavingLocationAreaId(LAIFixedLengthImpl value) {
        return new MetDPCriterionImpl(value, MetDPCriterionImpl.LAIFixedLength_Option.leavingLocationAreaId);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_interSystemHandOverToUMTS() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToUMTS);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_interSystemHandOverToGSM() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interSystemHandOverToGSM);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_interPLMNHandOver() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interPLMNHandOver);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion_interMSCHandOver() {
        return new MetDPCriterionImpl(MetDPCriterionImpl.Boolean_Option.interMSCHandOver);
    }

    @Override
    public MetDPCriterionImpl createMetDPCriterion(MetDPCriterionAltImpl metDPCriterionAlt) {
        return new MetDPCriterionImpl(metDPCriterionAlt);
    }

    @Override
    public OChangeOfPositionSpecificInfoImpl createOChangeOfPositionSpecificInfo(LocationInformationImpl locationInformation,
            List<MetDPCriterionImpl> metDPCriteriaList) {
        return new OChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
    }

    @Override
    public TChangeOfPositionSpecificInfoImpl createTChangeOfPositionSpecificInfo(LocationInformationImpl locationInformation,
            List<MetDPCriterionImpl> metDPCriteriaList) {
        return new TChangeOfPositionSpecificInfoImpl(locationInformation, metDPCriteriaList);
    }

    @Override
    public OServiceChangeSpecificInfoImpl createOServiceChangeSpecificInfo(ExtBasicServiceCodeImpl extBasicServiceCode) {
        return new OServiceChangeSpecificInfoImpl(extBasicServiceCode);
    }

    @Override
    public TServiceChangeSpecificInfoImpl createTServiceChangeSpecificInfo(ExtBasicServiceCodeImpl extBasicServiceCode) {
        return new TServiceChangeSpecificInfoImpl(extBasicServiceCode);
    }

    @Override
    public CollectedInfoSpecificInfoImpl createCollectedInfoSpecificInfo(CalledPartyNumberCapImpl calledPartyNumber) {
        return new CollectedInfoSpecificInfoImpl(calledPartyNumber);
    }

    @Override
    public DpSpecificInfoAltImpl createDpSpecificInfoAlt(OServiceChangeSpecificInfoImpl oServiceChangeSpecificInfo,
            CollectedInfoSpecificInfoImpl collectedInfoSpecificInfo, TServiceChangeSpecificInfoImpl tServiceChangeSpecificInfo) {
        return new DpSpecificInfoAltImpl(oServiceChangeSpecificInfo, collectedInfoSpecificInfo, tServiceChangeSpecificInfo);
    }

    @Override
    public ChangeOfLocationAltImpl createChangeOfLocationAlt() {
        return new ChangeOfLocationAltImpl();
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.cellGlobalId);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLengthImpl value) {
        return new ChangeOfLocationImpl(value, ChangeOfLocationImpl.CellGlobalIdOrServiceAreaIdFixedLength_Option.serviceAreaId);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation(LAIFixedLengthImpl locationAreaId) {
        return new ChangeOfLocationImpl(locationAreaId);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation_interSystemHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interSystemHandOver);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation_interPLMNHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interPLMNHandOver);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation_interMSCHandOver() {
        return new ChangeOfLocationImpl(ChangeOfLocationImpl.Boolean_Option.interMSCHandOver);
    }

    @Override
    public ChangeOfLocationImpl createChangeOfLocation(ChangeOfLocationAltImpl changeOfLocationAlt) {
        return new ChangeOfLocationImpl(changeOfLocationAlt);
    }

    @Override
    public DpSpecificCriteriaAltImpl createDpSpecificCriteriaAlt(List<ChangeOfLocationImpl> changeOfPositionControlInfo, Integer numberOfDigits) {
        return new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, numberOfDigits);
    }

    @Override
    public MidCallControlInfoImpl createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits, String endOfReplyDigit,
            String cancelDigit, String startDigit, Integer interDigitTimeout) {
        return new MidCallControlInfoImpl(minimumNumberOfDigits, maximumNumberOfDigits, endOfReplyDigit, cancelDigit, startDigit, interDigitTimeout);
    }

    @Override
    public BurstImpl createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst, Integer toneDuration, Integer toneInterval) {
        return new BurstImpl(numberOfBursts, burstInterval, numberOfTonesInBurst, toneDuration, toneInterval);
    }

    @Override
    public BurstListImpl createBurstList(Integer warningPeriod, BurstImpl burst) {
        return new BurstListImpl(warningPeriod, burst);
    }

    @Override
    public AudibleIndicatorImpl createAudibleIndicator(Boolean tone) {
        return new AudibleIndicatorImpl(tone);
    }

    @Override
    public AudibleIndicatorImpl createAudibleIndicator(BurstListImpl burstList) {
        return new AudibleIndicatorImpl(burstList);
    }

    @Override
    public AChChargingAddressImpl createAChChargingAddress(LegIDImpl legID) {
        return new AChChargingAddressImpl(legID);
    }

    @Override
    public AChChargingAddressImpl createAChChargingAddress(int srfConnection) {
        return new AChChargingAddressImpl(srfConnection);
    }
}
