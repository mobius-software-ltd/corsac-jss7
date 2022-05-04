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

package org.restcomm.protocols.ss7.inap.api;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CAI_GSM0224;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
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
import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.BearerIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ISDNAccessRelatedInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledPartyIDIsup;
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
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeodeticInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.GeographicalInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformationEPS;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationNumberMap;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.UserCSGInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.LSAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AlertingSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnalyzedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.BusySpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.MidCallEvents;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.MidCallSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NotReachableSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.charging.AddOnCharge;
import org.restcomm.protocols.ss7.inap.api.charging.AddOnChargingInformation;
import org.restcomm.protocols.ss7.inap.api.charging.ChargeUnitTimeInterval;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingReferenceIdentification;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariff;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariffInformation;
import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargeCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargePulse;
import org.restcomm.protocols.ss7.inap.api.charging.Currency;
import org.restcomm.protocols.ss7.inap.api.charging.CurrencyFactorScale;
import org.restcomm.protocols.ss7.inap.api.charging.EventTypeCharging;
import org.restcomm.protocols.ss7.inap.api.charging.PulseUnits;
import org.restcomm.protocols.ss7.inap.api.charging.SubTariffControl;
import org.restcomm.protocols.ss7.inap.api.charging.TariffControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrencyFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffDuration;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulse;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulseFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchPulse;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchoverTime;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ApplicationID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNSIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppression;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeMessage;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeNoChargeIndication;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingAnalysisInputData;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DataItemInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DialogueUserInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.EventSpecificInfoCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ExistingLegs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ForwardSuppression;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericName;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GlobalTitle;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GlobalTitleAndSSN;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.InstructionIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.IntervalAccuracy;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LegIDs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LimitIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.PointCodeAndSSN;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.PointCodeAndSSNANSI;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ProtocolIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ProtocolIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReceivingFunctionsRequested;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RequestedReportInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RouteOrigin;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCPDialogueInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SendingFunctionsActive;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TCAPDialogueLevel;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TariffInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AChBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.AddressAndService;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DisplayInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Entry;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteredCallTreatment;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringTimeOut;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.GenericNumbers;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.HoldCause;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.INServiceCompatibilityIndication;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegStatus;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallControlInfoINAP;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallControlInfoItem;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallInfoType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.MidCallReportType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceAddressInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceProfileIdentifier;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.Tariff;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.USIInformation;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.USIServiceIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.CauseIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardGVNS;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.LocationNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.OriginalCalledNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectingNumber;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserServiceInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0100;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
public interface INAPParameterFactory {

	CauseIsup createCause(CauseIndicators causeIndicators) throws INAPException;

	ForwardCallIndicatorsIsup createForwardCallIndicatorsIsup(ForwardCallIndicators forwardCallIndicators) throws INAPException;
    
    ForwardGVNSIsup createForwardGVNS(ForwardGVNS forwardGVNS) throws INAPException;
        
    ISDNAccessRelatedInformationIsup createISDNAccessRelatedInformationIsup(LocationNumber locationNumber) throws INAPException;
    
    OriginalCalledPartyIDIsup createOriginalCalledPartyIDIsup(OriginalCalledNumber originalCalledNumber) throws INAPException;
    
    DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer);

    DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo);

    DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt);

    BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
            DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm);

    CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
            String address) throws INAPException;

    ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, ByteBuf data, boolean isConstructed);

    ExtensionField createExtensionField(List<Long> globalCode, CriticalityType criticalityType, ByteBuf data, boolean isConstructed);

    CAPINAPExtensions createINAPExtensions(List<ExtensionField> fieldsList);

    AChBillingChargingCharacteristics createAChBillingChargingCharacteristics(ByteBuf data);

    DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second);

    TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second, int timeZone);

    BearerIsup createBearer(UserServiceInformation userServiceInformation) throws INAPException;

    BearerCapability createBearerCapability(BearerIsup bearer);

    DigitsIsup createDigits_GenericNumber(GenericNumber genericNumber) throws INAPException;

    DigitsIsup createDigits_GenericDigits(GenericDigits genericDigits) throws INAPException;

    CalledPartyNumberIsup createCalledPartyNumber(CalledPartyNumber calledPartyNumber) throws INAPException;

    CallingPartyNumberIsup createCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws INAPException;

    GenericNumberIsup createGenericNumber(GenericNumber genericNumber) throws INAPException;

    LocationNumberIsup createLocationNumber(LocationNumber locationNumber) throws INAPException;

    OriginalCalledNumberIsup createOriginalCalledNumber(OriginalCalledNumber originalCalledNumber) throws INAPException;

    RedirectingPartyIDIsup createRedirectingPartyID(RedirectingNumber redirectingNumber) throws INAPException;

    RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseIsup failureCause);

    BusySpecificInfo createBusySpecificInfo(CauseIsup busyCause);

    AlertingSpecificInfo createAlertingSpecificInfo(BackwardCallIndicatorsIsup backwardCallIndicators);

    NoAnswerSpecificInfo createNoAnswerSpecificInfo();

    AnswerSpecificInfo createAnswerSpecificInfo(Integer timeToAnswer,BackwardCallIndicatorsIsup backwardCallIndicators,BackwardGVNSIndicator backwardGVNSIndicator);

    DisconnectSpecificInfo createDisconnectSpecificInfo(CauseIsup releaseCause,Integer connectTime);

    NotReachableSpecificInfo createNotReachableSpecificInfo(CauseIsup releaseCause);
    
    AnalyzedInfoSpecificInfo createAnalyzedInfoSpecificInfo(CalledPartyNumberIsup calledPartyNumber);

    CollectedInfoSpecificInfo createCollectedInfoSpecificInfo(CalledPartyNumberIsup calledPartyNumber);

    DestinationRoutingAddress createDestinationRoutingAddress(List<CalledPartyNumberIsup> calledPartyNumber);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(CollectedInfoSpecificInfo collectedInfoSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(AnalyzedInfoSpecificInfo analyzedInfoSpecificInfo);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(BusySpecificInfo busySpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(NoAnswerSpecificInfo oNoAnswerSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(AnswerSpecificInfo oAnswerSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(MidCallSpecificInfo oMidCallSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(DisconnectSpecificInfo oDisconnectSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(NotReachableSpecificInfo notReachableSpecificInfo, boolean isTermination);

    EventSpecificInformationBCSM createEventSpecificInformationBCSM(AlertingSpecificInfo alertingSpecificInfo, boolean isTermination);

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
            boolean GenerationOfVoiceAnnouncementsFromTextSupported, ByteBuf extraData);

    AlertingPatternWrapper createAlertingPattern(AlertingPattern alertingPattern);

    NAOliInfo createNAOliInfo(int value);

    ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(
            ForwardServiceInteractionInd forwardServiceInteractionInd,
            BackwardServiceInteractionInd backwardServiceInteractionInd,
            BothwayThroughConnectionInd bothwayThroughConnectionInd, ConnectedNumberTreatmentInd connectedNumberTreatmentInd,
            boolean nonCUGCall, HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
            EctTreatmentIndicator ectTreatmentIndicator);

    FCIBCCCAMELSequence1 createFCIBCCCAMELsequence1(FreeFormatData freeFormatData, LegType partyToCharge,
            AppendFreeFormatData appendFreeFormatData);

    CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6, Integer e7);

    SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(ByteBuf value);

    VariablePartPrice createVariablePartPrice(double price);

    VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart);

    VariablePartDate createVariablePartDate(int year, int month, int day);

    VariablePartTime createVariablePartTime(int hour, int minute);

    VariablePart createVariablePart(Integer integer);

    VariablePart createVariablePart(DigitsIsup number);

    VariablePart createVariablePart(VariablePartTime time);

    VariablePart createVariablePart(VariablePartDate date);

    VariablePart createVariablePart(VariablePartPrice price);

    MessageIDText createMessageIDText(String messageContent, ByteBuf attributes);

    VariableMessage createVariableMessage(int elementaryMessageID, List<VariablePart> variableParts);

    MessageID createMessageID(Integer elementaryMessageID);

    MessageID createMessageID(MessageIDText text);

    MessageID createMessageID(List<Integer> elementaryMessageIDs);

    MessageID createMessageID(VariableMessage variableMessage);

    InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval);

    Tone createTone(int toneID, Integer duration);

    InformationToSend createInformationToSend(InbandInfo inbandInfo);

    InformationToSend createInformationToSend(Tone tone);

    CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits, ByteBuf endOfReplyDigit,
    		ByteBuf cancelDigit, ByteBuf startDigit, Integer firstDigitTimeOut, Integer interDigitTimeOut,
            ErrorTreatment errorTreatment, Boolean interruptableAnnInd, Boolean voiceInformation, Boolean voiceBack);

    CollectedInfo createCollectedInfo(CollectedDigits collectedDigits);

    CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID);

    FreeFormatData createFreeFormatData(ByteBuf data);

    LegOrCallSegment createLegOrCallSegment(Integer callSegmentID);

    LegOrCallSegment createLegOrCallSegment(LegID legID);

    BackwardServiceInteractionInd createBackwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator);

    Carrier createCarrier(ByteBuf data);

    ForwardServiceInteractionInd createForwardServiceInteractionInd(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator);

    LowLayerCompatibility createLowLayerCompatibility(ByteBuf data);

    MidCallEvents createMidCallEvents_Flash();

    MidCallEvents createMidCallEvents_UserCallSuspend();

    MidCallEvents createMidCallEvents_UserCallResume();

    MidCallEvents createMidCallEvents_Completed(DigitsIsup dtmfDigits);

    MidCallEvents createMidCallEvents_TimeOut(DigitsIsup dtmfDigits);

    MidCallSpecificInfo createMidCallSpecificInfo(MidCallEvents midCallEvents);

    MidCallSpecificInfo createMidCallSpecificInfo(Integer connectTime);

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
    
    //billing
    AddOnCharge createAddOnCharge(CurrencyFactorScale currencyFactorScale);
    
    AddOnCharge createAddOnCharge(PulseUnits pulseUnits);
    
    AddOnChargingInformation createAddOnChargingInformation(ChargingControlIndicators getChargingControlIndicators,
    		AddOnCharge addOncharge,CAPINAPExtensions extensions,ChargingReferenceIdentification originationIdentification,
    		ChargingReferenceIdentification destinationIdentification,Currency currency);
    
    ChargeUnitTimeInterval getChargeUnitTimeInterval(Integer data);
    
    ChargingControlIndicators getChargingControlIndicators(boolean getSubscriberCharge,boolean getImmediateChangeOfActuallyAppliedTariff,boolean getDelayUntilStart);
    
    ChargingReferenceIdentification getChargingReferenceIdentification(List<Long> networkIdentification,Long referenceID);
    
    ChargingTariff getChargingTariff(TariffCurrency tariffCurrency);
    
    ChargingTariff getChargingTariff(TariffPulse tariffPulse);
    
    ChargingTariffInformation getChargingTariffInformation(ChargingControlIndicators chargingControlIndicators,
    		ChargingTariff chargingTariff,CAPINAPExtensions extensions,ChargingReferenceIdentification originationIdentification,
    		ChargingReferenceIdentification destinationIdentification,Currency currency);
    
    CommunicationChargeCurrency getCommunicationChargeCurrency(CurrencyFactorScale currencyFactorScale,
    		Integer tariffDuration,SubTariffControl subTariffControl);
    
    CommunicationChargePulse getCommunicationChargePulse(PulseUnits pulseUnits,
    		Integer chargeUnitTimeInterval,Integer tariffDuration);
    
    CurrencyFactorScale getCurrencyFactorScale(Integer currencyFactor,Integer currencyScale);
    
    PulseUnits getPulseUnits(Integer data);
    
    SubTariffControl getSubTariffControl(boolean oneTimeCharge);
    
    TariffControlIndicators getTariffControlIndicators(boolean nonCyclicTariff);
    
    TariffCurrency getTariffCurrency(TariffCurrencyFormat tariffCurrencyFormat,TariffSwitchCurrency getTariffSwitchCurrency);
    
    TariffCurrencyFormat getTariffCurrencyFormat(List<CommunicationChargeCurrency> communicationChargeSequenceCurrency, 
    		TariffControlIndicators tariffControlIndicators,CurrencyFactorScale callAttemptChargeCurrency,
    		CurrencyFactorScale callSetupChargeCurrency);
    
    TariffDuration getTariffDuration(Integer data);
    
    TariffPulse getTariffPulse(TariffPulseFormat currentTariffPulse,TariffSwitchPulse tariffSwitchPulse);
    
    TariffPulseFormat getTariffPulseFormat(List<CommunicationChargePulse> communicationChargeSequencePulse,
    		TariffControlIndicators tariffControlIndicators,PulseUnits callAttemptChargePulse,
    		PulseUnits callSetupChargePulse);
    
    TariffSwitchCurrency getTariffSwitchCurrency(TariffCurrencyFormat nextTariffCurrency,TariffSwitchoverTime tariffSwitchoverTime);
    
    TariffSwitchoverTime getTariffSwitchoverTime(Integer data);
    
    TariffSwitchPulse getTariffSwitchPulse(TariffPulseFormat nextTariffPulse,TariffSwitchoverTime tariffSwitchoverTime);
    
    //cs1 plus
    ApplicationID getApplicationID(Integer data);
    
    BackwardGVNSIndicator getBackwardGVNSIndicator(BackwardGVNS backwardGVNS);
    
    BackwardSuppressionIndicators getBackwardSuppressionIndicators(BackwardSuppression backwardSuppression,
    	InstructionIndicator instructionIndicator);
    
    CUGCallIndicator getCUGCallIndicator(CUGCall cugCall);
    
    CUGInterLockCode getCUGInterLockCode(ByteBuf value);
    
    DataItemID getDataItemID(ByteBuf attribute0,	ByteBuf attribute1, ByteBuf attribute2,    
    	ByteBuf attribute3,ByteBuf attribute4, ByteBuf attribute5, ByteBuf attribute6,    
    	ByteBuf attribute7, ByteBuf attribute8, ByteBuf attribute9, ByteBuf attribute10,    
    	ByteBuf attribute11, ByteBuf attribute12, ByteBuf attribute13, ByteBuf attribute14,    
    	ByteBuf attribute15, ByteBuf attribute16, ByteBuf attribute17, ByteBuf attribute18,
    	ByteBuf attribute19, ByteBuf attribute20, ByteBuf attribute21, ByteBuf attribute22,
    	ByteBuf attribute23, ByteBuf attribute24, ByteBuf attribute25, ByteBuf attribute26,
    	ByteBuf attribute27, ByteBuf attribute28, ByteBuf attribute29, ByteBuf attribute30);
    
    DataItemInformation getDataItemInformation(ByteBuf attribute0,	ByteBuf attribute1, ByteBuf attribute2,    
        	ByteBuf attribute3,ByteBuf attribute4, ByteBuf attribute5, ByteBuf attribute6,    
        	ByteBuf attribute7, ByteBuf attribute8, ByteBuf attribute9, ByteBuf attribute10,    
        	ByteBuf attribute11, ByteBuf attribute12, ByteBuf attribute13, ByteBuf attribute14,    
        	ByteBuf attribute15, ByteBuf attribute16, ByteBuf attribute17, ByteBuf attribute18,
        	ByteBuf attribute19, ByteBuf attribute20, ByteBuf attribute21, ByteBuf attribute22,
        	ByteBuf attribute23, ByteBuf attribute24, ByteBuf attribute25, ByteBuf attribute26,
        	ByteBuf attribute27, ByteBuf attribute28, ByteBuf attribute29, ByteBuf attribute30);
    
    DialogueUserInformation getDialogueUserInformation(SendingFunctionsActive sendingFunctionsActive,
    		ReceivingFunctionsRequested receivingFunctionsRequested,Integer trafficSimulationSessionID);
    
    ExistingLegs getExistingLegs(LegType legID,boolean linkInd);
    
    ForwardSuppressionIndicators getForwardSuppressionIndicators(ForwardSuppression forwardSuppression,
    		InstructionIndicator instructionIndicator);
    
    GenericDigitsSet getGenericDigitsSet(List<DigitsIsup> genericDigits);
    
    GenericName getGenericName(ByteBuf value);
    
    GenericNumbersSet getGenericNumbersSet(List<DigitsIsup> geneicNumbers);
    
    GlobalTitle getGlobalTitle(GlobalTitle0100 title) throws INAPException;
    
    GlobalTitleAndSSN getGlobalTitleAndSSN(GlobalTitle0100 title, Integer ssn) throws INAPException;
    
    HandOverInfo getHandOverInfo(Integer handoverCounter, SCPAddress sendingSCPAddress,
    		SCPDialogueInfo sendingSCPDialogueInfo, ByteBuf sendingSCPCorrelationInfo,
    		SCPAddress receivingSCPAddress, SCPDialogueInfo receivingSCPDialogueInfo,
    		ByteBuf receivingSCPCorrelationInfo, CalledPartyNumberIsup handoverNumber,Integer handoverData);
    
    LegIDs getLegIDs(List<ExistingLegs> existingLegs);
    
    LimitIndicators getLimitIndicators(Integer duration);
    
    PointCodeAndSSN getPointCodeAndSSN(Integer spc,Integer ssn);
    
    PointCodeAndSSNANSI getPointCodeAndSSNANSI(Integer network,Integer cluster,Integer member,Integer ssn);
    
    ProtocolIndicator getProtocolIndicator(ProtocolIdentifier protocolIdentifier,TCAPDialogueLevel tcapDialogueLevel);
    
    RouteOrigin getRouteOrigin(ByteBuf value);
    
    SCPAddress getSCPAddress(boolean colocated);
    
    SCPAddress getSCPAddress(PointCodeAndSSN pointCodeAndSSN);
    
    SCPAddress getSCPAddress(GlobalTitle globalTitle);
    
    SCPAddress getSCPAddress(GlobalTitleAndSSN globalTitleAndSSN);
    
    SCPAddress getSCPAddress(PointCodeAndSSNANSI pointCodeAndSubSystemNumberANSI);
    
    SCPDialogueInfo getSCPDialogueInfo(ProtocolIndicator protocolIndicator,DialogueUserInformation dialogueUserInformation);
    
    //cicruit switched call - inap
    AddressAndService getAddressAndService(DigitsIsup calledAddressValue,Integer serviceKey,
    		DigitsIsup callingAddressValue,LocationNumberIsup locationNumber);
    
    CalledPartyBusinessGroupID getCalledPartyBusinessGroupID(ByteBuf data);
    
    CalledPartySubaddress getCalledPartySubaddress(ByteBuf data);
    
    CallingPartyBusinessGroupID getCallingPartyBusinessGroupID(ByteBuf data);
    
    CallingPartySubaddress getCallingPartySubaddress(ByteBuf data);
    
    ChargingEvent getChargingEvent(EventTypeCharging eventTypeCharging,MonitorMode monitorMode,LegID legID);
    
    CounterAndValue getCounterAndValue(Integer counterID,Integer counterValue);
    
    DisplayInformation getDisplayInformation(String value);
    
    DpSpecificCommonParameters getDpSpecificCommonParameters(ServiceAddressInformation serviceAddressInformation,
    		BearerCapability bearerCapability,CalledPartyNumberIsup calledPartyNumber,CallingPartyNumberIsup callingPartyNumber,
    		CallingPartysCategoryIsup callingPartysCategory,IPSSPCapabilities ipsspCapabilities,
    		IPAvailable ipAvailable,ISDNAccessRelatedInformation isdnAccessRelatedInformation,
    		CGEncountered cgEncountered,LocationNumberIsup locationNumber,ServiceProfileIdentifier serviceProfileIdentifier,
    		TerminalType terminalType,CAPINAPExtensions extensions,LocationNumberIsup chargeNumber,
    		LocationNumberIsup servingAreaID);
    
    Entry getEntry(List<Long> agreements);
    
    Entry getEntry(Integer networkSpecific);
    
    FacilityGroup getFacilityGroup(Integer value,boolean isTrunkGroup);
    
    FacilityGroup getFacilityGroup(ByteBuf value,boolean isHuntGroup);
    
    FilteredCallTreatment getFilteredCallTreatment(ByteBuf sfBillingChargingCharacteristics,
    		InformationToSend informationToSend,Integer maximumNumberOfCounters,CauseIsup cause);
    
    FilteringCharacteristics getFilteringCharacteristics(Integer value,Boolean isInterval);
    
    FilteringCriteria getFilteringCriteria(Integer serviceKey);
    
    FilteringCriteria getFilteringCriteria(AddressAndService addressAndService);
    
    FilteringTimeOut getFilteringTimeOut(Integer duration);
    
    FilteringTimeOut getFilteringTimeOut(DateAndTime stopTime);
    
    GenericNumbers getGenericNumbers(List<GenericNumberIsup> genericNumbers);
    
    HoldCause getHoldCause(ByteBuf value);
    
    INServiceCompatibilityIndication getINServiceCompatibilityIndication(List<Entry> entries);
    
    IPAvailable getIPAvailable(ByteBuf value);
    
    ISDNAccessRelatedInformation getISDNAccessRelatedInformation(ByteBuf value);
    
    MidCallControlInfoINAP getMidCallControlInfo(List<MidCallControlInfoItem> midCallControlInfoItems);
    
    MidCallControlInfoItem getMidCallControlInfoItem(MidCallInfoType midCallInfoType, MidCallReportType midCallReportType);
    
    MidCallInfoType getMidCallInfoType(DigitsIsup inServiceControlCodeLow,DigitsIsup inServiceControlCodeHigh);
    
    ResourceAddress getResourceAddress(CalledPartyNumberIsup ipRoutingAddress);
    
    ResourceAddress getResourceAddress(LegType legID);
    
    ResourceAddress getResourceAddress(boolean none);
    
    ResourceID getResourceID(DigitsIsup lineID);
    
    ResourceID getResourceID(FacilityGroup facilityGroup);
    
    ResourceID getResourceID(Integer value,boolean isTrunkGroupID);
    
    RouteList getRouteList(List<ByteBuf> data);
    
    ServiceAddressInformation getServiceAddressInformation(Integer serviceKey,MiscCallInfo miscCallInfo,TriggerType triggerType);
    
    ServiceInteractionIndicators getServiceInteractionIndicators(ByteBuf value);
    
    ServiceProfileIdentifier getServiceProfileIdentifier(ByteBuf value);
    
    Tariff getTariff(ChargingTariffInformation chargingTariffInformation);
    
    Tariff getTariff(AddOnChargingInformation addOnChargingInformation);
    
    USIInformation getUSIInformation(ByteBuf value);
    
    USIServiceIndicator getUSIServiceIndicator(List<Long> global);
    
    USIServiceIndicator getUSIServiceIndicator(ByteBuf local);
    
    TariffInformation getTariffInformation(Integer numberOfStartPulses,Integer startInterval,IntervalAccuracy startIntervalAccuracy,
    		Integer numberOfPeriodicPulses,Integer periodicInterval,IntervalAccuracy periodicIntervalAccuracy,DateAndTime activationTime);
    
    EventSpecificInfoCharging getEventSpecificInfoCharging(TariffInformation tariffInformation);
    
    EventSpecificInfoCharging getEventSpecificInfoCharging(ByteBuf tariffIndicator);
    
    EventSpecificInfoCharging getEventSpecificInfoCharging(ChargeNoChargeIndication chargeNoChargeIndication);
    
    ChargeMessage getChargeMessage(EventTypeCharging eventTypeCharging,EventSpecificInfoCharging eventSpecificInfoCharging);
    
    ChargingInformation getChargingInformation(boolean orderStartOfCharging,ChargeMessage chargeMessage,
    		Integer pulseBurst,boolean createDefaultBillingRecord);
    
    ChargingAnalysisInputData getChargingAnalysisInputData(ByteBuf chargingOrigin, ByteBuf tariffActivityCode, Integer chargingCode);
    
    SCIBillingChargingCharacteristicsCS1 getSCIBillingChargingCharacteristicsCS1(ChargingInformation chargingInformation);
    
    SCIBillingChargingCharacteristicsCS1 getSCIBillingChargingCharacteristicsCS1(ChargingAnalysisInputData chargingAnalysisInputData);
    
    ReportCondition getReportCondition(boolean value,boolean immediately);
    
    ReportCondition getReportCondition(Integer reportAtChargeLimit); 
    
    RequestedReportInfo getRequestedReportInfo(boolean accumulatedCharge,boolean actualTariff,boolean chargeableDuration,boolean timeOfAnswer);
    
    AchBillingChargingCharacteristicsCS1 getAchBillingChargingCharacteristicsCS1(ReportCondition reportCondition,RequestedReportInfo requestedReportInfo);
    
    LegInformation getLegInformation(LegType legType,LegStatus legStatus);
}