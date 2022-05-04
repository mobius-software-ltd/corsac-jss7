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

package org.restcomm.protocols.ss7.inap;

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
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
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
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AudibleIndicatorImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BackwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.BearerCapabilityImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CAI_GSM0224Impl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CallSegmentToCancelImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationAltImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ChangeOfLocationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedDigitsImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CollectedInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DestinationRoutingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaAltImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.DpSpecificCriteriaImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FCIBCCCAMELSequence1Impl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ForwardServiceInteractionIndImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.FreeFormatDataImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.IPSSPCapabilitiesImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InbandInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.InformationToSendImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LegOrCallSegmentImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.LowLayerCompatibilityImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MessageIDTextImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.MidCallControlInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.NAOliInfoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ServiceInteractionIndicatorsTwoImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeDurationChargingResultImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeIfTariffSwitchImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.TimeInformationImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ToneImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariableMessageImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartDateImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartPriceImpl;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.VariablePartTimeImpl;
import org.restcomm.protocols.ss7.commonapp.isup.BearerIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CallingPartysCategoryIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardCallIndicatorsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ForwardGVNSIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.GenericNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.HighLayerCompatibilityIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.ISDNAccessRelatedInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.AChChargingAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BCSMEventImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BurstImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.BurstListImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ExtensionFieldImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.TimeAndTimezoneImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberInformation.LocationInformationImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.AlertingSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.AnalyzedInfoSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.AnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.BusySpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.CollectedInfoSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.DisconnectSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.MidCallEventsImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.MidCallSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.NoAnswerSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.NotReachableSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
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
import org.restcomm.protocols.ss7.inap.charging.AddOnChargeImpl;
import org.restcomm.protocols.ss7.inap.charging.AddOnChargingInformationImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargeUnitTimeIntervalImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargingControlIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargingReferenceIdentificationImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargingTariffImpl;
import org.restcomm.protocols.ss7.inap.charging.ChargingTariffInformationImpl;
import org.restcomm.protocols.ss7.inap.charging.CommunicationChargeCurrencyImpl;
import org.restcomm.protocols.ss7.inap.charging.CommunicationChargePulseImpl;
import org.restcomm.protocols.ss7.inap.charging.CurrencyFactorScaleImpl;
import org.restcomm.protocols.ss7.inap.charging.PulseUnitsImpl;
import org.restcomm.protocols.ss7.inap.charging.SubTariffControlIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffControlIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffCurrencyFormatImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffCurrencyImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffDurationImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffPulseFormatImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffPulseImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffSwitchCurrencyImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffSwitchPulseImpl;
import org.restcomm.protocols.ss7.inap.charging.TariffSwitchoverTimeImpl;
import org.restcomm.protocols.ss7.inap.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.AchBillingChargingCharacteristicsCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ApplicationIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.BackwardGVNSIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGCallIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.CUGInterLockCodeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargeMessageImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargingAnalysisInputDataImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ChargingInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DataItemInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.DialogueUserInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.EventSpecificInfoChargingImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ExistingLegsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicatorsmpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericDigitsSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericNameImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GenericNumbersSetImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GlobalTitleAndSSNImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.GlobalTitleImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.HandOverInfoImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.LegIDsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.LimitIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.PointCodeAndSSNANSIImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.PointCodeAndSSNImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ProtocolIndicatorImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.ReportConditionImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.RequestedReportInfoImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.RouteOriginImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1Impl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.SCPAddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.SCPDialogueInfoImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.TariffInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.AChBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.AddressAndServiceImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CalledPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CalledPartySubaddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartySubaddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ChargingEventImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CounterAndValueImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DisplayInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DpSpecificCommonParametersImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.EntryImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.EventSpecificInformationBCSMImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FacilityGroupImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteredCallTreatmentImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringCharacteristicsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringCriteriaImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FilteringTimeOutImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.GenericNumbersImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.HoldCauseImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.INServiceCompatibilityIndicationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.IPAvailableImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ISDNAccessRelatedInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.LegInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.MidCallControlInfoINAPImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.MidCallControlInfoItemImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.MidCallInfoTypeImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RequestedInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceAddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RouteListImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceAddressInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceProfileIdentifierImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.TariffImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.USIInformationImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.USIServiceIndicatorImpl;
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

import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPParameterFactoryImpl implements INAPParameterFactory {
	@Override
	public RouteSelectFailureSpecificInfo createRouteSelectFailureSpecificInfo(CauseIsup failureCause) {
		return new RouteSelectFailureSpecificInfoImpl(failureCause);
	}

	@Override
	public CauseIsup createCause(CauseIndicators causeIndicators) throws INAPException {
		try {
			return new CauseIsupImpl(causeIndicators);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public ForwardCallIndicatorsIsup createForwardCallIndicatorsIsup(ForwardCallIndicators forwardCallIndicators)
			throws INAPException {
		try {
			return new ForwardCallIndicatorsIsupImpl(forwardCallIndicators);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public ForwardGVNSIsup createForwardGVNS(ForwardGVNS forwardGVNS) throws INAPException {
		try {
			return new ForwardGVNSIsupImpl(forwardGVNS);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public ISDNAccessRelatedInformationIsup createISDNAccessRelatedInformationIsup(LocationNumber locationNumber)
			throws INAPException {
		try {
			return new ISDNAccessRelatedInformationIsupImpl(locationNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public OriginalCalledPartyIDIsup createOriginalCalledPartyIDIsup(OriginalCalledNumber originalCalledNumber)
			throws INAPException {
		try {
			return new OriginalCalledPartyIDIsupImpl(originalCalledNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(Integer applicationTimer) {
		return new DpSpecificCriteriaImpl(applicationTimer);
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(MidCallControlInfo midCallControlInfo) {
		return new DpSpecificCriteriaImpl(midCallControlInfo);
	}

	@Override
	public DpSpecificCriteria createDpSpecificCriteria(DpSpecificCriteriaAlt dpSpecificCriteriaAlt) {
		return new DpSpecificCriteriaImpl(dpSpecificCriteriaAlt);
	}

	@Override
	public BCSMEvent createBCSMEvent(EventTypeBCSM eventTypeBCSM, MonitorMode monitorMode, LegID legID,
			DpSpecificCriteria dpSpecificCriteria, boolean automaticRearm) {
		return new BCSMEventImpl(eventTypeBCSM, monitorMode, legID, dpSpecificCriteria, automaticRearm);
	}

	@Override
	public CalledPartyBCDNumber createCalledPartyBCDNumber(AddressNature addressNature, NumberingPlan numberingPlan,
			String address) throws INAPException {
		try {
			return new CalledPartyBCDNumberImpl(addressNature, numberingPlan, address);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public ExtensionField createExtensionField(Integer localCode, CriticalityType criticalityType, ByteBuf data,
			boolean isConstructed) {
		return new ExtensionFieldImpl(localCode, criticalityType, data, isConstructed);
	}

	@Override
	public ExtensionField createExtensionField(List<Long> globalCode, CriticalityType criticalityType, ByteBuf data,
			boolean isConstructed) {
		return new ExtensionFieldImpl(globalCode, criticalityType, data, isConstructed);
	}

	@Override
	public CAPINAPExtensions createINAPExtensions(List<ExtensionField> fieldsList) {
		return new CAPINAPExtensionsImpl(fieldsList);
	}

	@Override
	public AChBillingChargingCharacteristics createAChBillingChargingCharacteristics(ByteBuf data) {
		return new AChBillingChargingCharacteristicsImpl(data);
	}

	@Override
	public DateAndTime createDateAndTime(int year, int month, int day, int hour, int minute, int second) {
		return new DateAndTimeImpl(year, month, day, hour, minute, second);
	}

	@Override
	public TimeAndTimezone createTimeAndTimezone(int year, int month, int day, int hour, int minute, int second,
			int timeZone) {
		return new TimeAndTimezoneImpl(year, month, day, hour, minute, second, timeZone);
	}

	@Override
	public BearerIsup createBearer(UserServiceInformation userServiceInformation) throws INAPException {
		try {
			return new BearerIsupImpl(userServiceInformation);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public BearerCapability createBearerCapability(BearerIsup bearerCap) {
		return new BearerCapabilityImpl(bearerCap);
	}

	@Override
	public DigitsIsup createDigits_GenericNumber(GenericNumber genericNumber) throws INAPException {
		try {
			return new DigitsIsupImpl(genericNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public DigitsIsup createDigits_GenericDigits(GenericDigits genericDigits) throws INAPException {
		try {
			return new DigitsIsupImpl(genericDigits);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public CalledPartyNumberIsup createCalledPartyNumber(CalledPartyNumber calledPartyNumber) throws INAPException {
		try {
			return new CalledPartyNumberIsupImpl(calledPartyNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public CallingPartyNumberIsup createCallingPartyNumber(CallingPartyNumber callingPartyNumber) throws INAPException {
		try {
			return new CallingPartyNumberIsupImpl(callingPartyNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public GenericNumberIsup createGenericNumber(GenericNumber genericNumber) throws INAPException {
		try {
			return new GenericNumberIsupImpl(genericNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public LocationNumberIsup createLocationNumber(LocationNumber locationNumber) throws INAPException {
		try {
			return new LocationNumberIsupImpl(locationNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public OriginalCalledNumberIsup createOriginalCalledNumber(OriginalCalledNumber originalCalledNumber)
			throws INAPException {
		try {
			return new OriginalCalledNumberIsupImpl(originalCalledNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public RedirectingPartyIDIsup createRedirectingPartyID(RedirectingNumber redirectingNumber) throws INAPException {
		try {
			return new RedirectingPartyIDIsupImpl(redirectingNumber);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public BusySpecificInfo createBusySpecificInfo(CauseIsup busyCause) {
		return new BusySpecificInfoImpl(busyCause);
	}

	@Override
	public AlertingSpecificInfo createAlertingSpecificInfo(BackwardCallIndicatorsIsup backwardCallIndicators) {
		return new AlertingSpecificInfoImpl(backwardCallIndicators);
	}

	@Override
	public NoAnswerSpecificInfo createNoAnswerSpecificInfo() {
		return new NoAnswerSpecificInfoImpl();
	}

	@Override
	public AnswerSpecificInfo createAnswerSpecificInfo(Integer timeToAnswer,
			BackwardCallIndicatorsIsup backwardCallIndicators, BackwardGVNSIndicator backwardGVNSIndicator) {
		return new AnswerSpecificInfoImpl(timeToAnswer, backwardCallIndicators, backwardGVNSIndicator);
	}

	@Override
	public DisconnectSpecificInfo createDisconnectSpecificInfo(CauseIsup releaseCause, Integer connectTime) {
		return new DisconnectSpecificInfoImpl(releaseCause, connectTime);
	}

	@Override
	public DestinationRoutingAddress createDestinationRoutingAddress(List<CalledPartyNumberIsup> calledPartyNumber) {
		return new DestinationRoutingAddressImpl(calledPartyNumber);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
			CollectedInfoSpecificInfo collectedInfoSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(collectedInfoSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
			AnalyzedInfoSpecificInfo analyzedInfoSpecificInfo) {
		return new EventSpecificInformationBCSMImpl(analyzedInfoSpecificInfo);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
			RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo, boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(routeSelectFailureSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(BusySpecificInfo busySpecificInfo,
			boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(busySpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(NoAnswerSpecificInfo oNoAnswerSpecificInfo,
			boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(oNoAnswerSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(AnswerSpecificInfo oAnswerSpecificInfo,
			boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(oAnswerSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(MidCallSpecificInfo oMidCallSpecificInfo,
			boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(oMidCallSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
			DisconnectSpecificInfo oDisconnectSpecificInfo, boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(oDisconnectSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(
			NotReachableSpecificInfo notReachableSpecificInfo, boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(notReachableSpecificInfo, isTermination);
	}

	@Override
	public EventSpecificInformationBCSM createEventSpecificInformationBCSM(AlertingSpecificInfo alertingSpecificInfo,
			boolean isTermination) {
		return new EventSpecificInformationBCSMImpl(alertingSpecificInfo, isTermination);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallAttemptElapsedTime(int callAttemptElapsedTimeValue) {
		return new RequestedInformationImpl(RequestedInformationType.callAttemptElapsedTime,
				callAttemptElapsedTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallConnectedElapsedTime(int callConnectedElapsedTimeValue) {
		return new RequestedInformationImpl(RequestedInformationType.callConnectedElapsedTime,
				callConnectedElapsedTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_CallStopTime(DateAndTime callStopTimeValue) {
		return new RequestedInformationImpl(callStopTimeValue);
	}

	@Override
	public RequestedInformation createRequestedInformation_ReleaseCause(CauseIsup releaseCauseValue) {
		return new RequestedInformationImpl(releaseCauseValue);
	}

	@Override
	public TimeDurationChargingResult createTimeDurationChargingResult(LegType partyToCharge,
			TimeInformation timeInformation, boolean legActive, boolean callLegReleasedAtTcpExpiry,
			CAPINAPExtensions extensions, AChChargingAddress aChChargingAddress) {
		return new TimeDurationChargingResultImpl(partyToCharge, timeInformation, legActive, callLegReleasedAtTcpExpiry,
				extensions, aChChargingAddress);
	}

	@Override
	public TimeIfTariffSwitch createTimeIfTariffSwitch(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
		return new TimeIfTariffSwitchImpl(timeSinceTariffSwitch, tariffSwitchInterval);
	}

	@Override
	public TimeInformation createTimeInformation(int timeIfNoTariffSwitch) {
		return new TimeInformationImpl(timeIfNoTariffSwitch);
	}

	@Override
	public TimeInformation createTimeInformation(TimeIfTariffSwitch timeIfTariffSwitch) {
		return new TimeInformationImpl(timeIfTariffSwitch);
	}

	@Override
	public IPSSPCapabilities createIPSSPCapabilities(boolean IPRoutingAddressSupported, boolean VoiceBackSupported,
			boolean VoiceInformationSupportedViaSpeechRecognition, boolean VoiceInformationSupportedViaVoiceRecognition,
			boolean GenerationOfVoiceAnnouncementsFromTextSupported, ByteBuf extraData) {
		return new IPSSPCapabilitiesImpl(IPRoutingAddressSupported, VoiceBackSupported,
				VoiceInformationSupportedViaSpeechRecognition, VoiceInformationSupportedViaVoiceRecognition,
				GenerationOfVoiceAnnouncementsFromTextSupported, extraData);
	}

	@Override
	public AlertingPatternWrapper createAlertingPattern(AlertingPattern alertingPattern) {
		return new AlertingPatternWrapperImpl(alertingPattern);
	}

	@Override
	public NAOliInfo createNAOliInfo(int value) {
		return new NAOliInfoImpl(value);
	}

	@Override
	public ServiceInteractionIndicatorsTwo createServiceInteractionIndicatorsTwo(
			ForwardServiceInteractionInd forwardServiceInteractionInd,
			BackwardServiceInteractionInd backwardServiceInteractionInd,
			BothwayThroughConnectionInd bothwayThroughConnectionInd,
			ConnectedNumberTreatmentInd connectedNumberTreatmentInd, boolean nonCUGCall,
			HoldTreatmentIndicator holdTreatmentIndicator, CwTreatmentIndicator cwTreatmentIndicator,
			EctTreatmentIndicator ectTreatmentIndicator) {
		return new ServiceInteractionIndicatorsTwoImpl(forwardServiceInteractionInd, backwardServiceInteractionInd,
				bothwayThroughConnectionInd, connectedNumberTreatmentInd, nonCUGCall, holdTreatmentIndicator,
				cwTreatmentIndicator, ectTreatmentIndicator);
	}

	@Override
	public FCIBCCCAMELSequence1 createFCIBCCCAMELsequence1(FreeFormatData freeFormatData, LegType partyToCharge,
			AppendFreeFormatData appendFreeFormatData) {
		return new FCIBCCCAMELSequence1Impl(freeFormatData, partyToCharge, appendFreeFormatData);
	}

	@Override
	public CAI_GSM0224 createCAI_GSM0224(Integer e1, Integer e2, Integer e3, Integer e4, Integer e5, Integer e6,
			Integer e7) {
		return new CAI_GSM0224Impl(e1, e2, e3, e4, e5, e6, e7);
	}

	@Override
	public SCIBillingChargingCharacteristics createSCIBillingChargingCharacteristics(ByteBuf value) {
		return new SCIBillingChargingCharacteristicsImpl(value);
	}

	@Override
	public VariablePartPrice createVariablePartPrice(double price) {
		return new VariablePartPriceImpl(price);
	}

	@Override
	public VariablePartPrice createVariablePartPrice(int integerPart, int hundredthPart) {
		return new VariablePartPriceImpl(integerPart, hundredthPart);
	}

	@Override
	public VariablePartDate createVariablePartDate(int year, int month, int day) {
		return new VariablePartDateImpl(year, month, day);
	}

	@Override
	public VariablePartTime createVariablePartTime(int hour, int minute) {
		return new VariablePartTimeImpl(hour, minute);
	}

	@Override
	public VariablePart createVariablePart(Integer integer) {
		return new VariablePartImpl(integer);
	}

	@Override
	public VariablePart createVariablePart(DigitsIsup number) {
		return new VariablePartImpl(number);
	}

	@Override
	public VariablePart createVariablePart(VariablePartTime time) {
		return new VariablePartImpl(time);
	}

	@Override
	public VariablePart createVariablePart(VariablePartDate date) {
		return new VariablePartImpl(date);
	}

	@Override
	public VariablePart createVariablePart(VariablePartPrice price) {
		return new VariablePartImpl(price);
	}

	@Override
	public MessageIDText createMessageIDText(String messageContent, ByteBuf attributes) {
		return new MessageIDTextImpl(messageContent, attributes);
	}

	@Override
	public VariableMessage createVariableMessage(int elementaryMessageID, List<VariablePart> variableParts) {
		return new VariableMessageImpl(elementaryMessageID, variableParts);
	}

	@Override
	public MessageID createMessageID(Integer elementaryMessageID) {
		return new MessageIDImpl(elementaryMessageID);
	}

	@Override
	public MessageID createMessageID(MessageIDText text) {
		return new MessageIDImpl(text);
	}

	@Override
	public MessageID createMessageID(List<Integer> elementaryMessageIDs) {
		return new MessageIDImpl(elementaryMessageIDs);
	}

	@Override
	public MessageID createMessageID(VariableMessage variableMessage) {
		return new MessageIDImpl(variableMessage);
	}

	@Override
	public InbandInfo createInbandInfo(MessageID messageID, Integer numberOfRepetitions, Integer duration,
			Integer interval) {
		return new InbandInfoImpl(messageID, numberOfRepetitions, duration, interval);
	}

	@Override
	public Tone createTone(int toneID, Integer duration) {
		return new ToneImpl(toneID, duration);
	}

	@Override
	public InformationToSend createInformationToSend(InbandInfo inbandInfo) {
		return new InformationToSendImpl(inbandInfo);
	}

	@Override
	public InformationToSend createInformationToSend(Tone tone) {
		return new InformationToSendImpl(tone);
	}

	@Override
	public CollectedDigits createCollectedDigits(Integer minimumNbOfDigits, int maximumNbOfDigits,
			ByteBuf endOfReplyDigit, ByteBuf cancelDigit, ByteBuf startDigit, Integer firstDigitTimeOut,
			Integer interDigitTimeOut, ErrorTreatment errorTreatment, Boolean interruptableAnnInd,
			Boolean voiceInformation, Boolean voiceBack) {
		return new CollectedDigitsImpl(minimumNbOfDigits, maximumNbOfDigits, endOfReplyDigit, cancelDigit, startDigit,
				firstDigitTimeOut, interDigitTimeOut, errorTreatment, interruptableAnnInd, voiceInformation, voiceBack);
	}

	@Override
	public CollectedInfo createCollectedInfo(CollectedDigits collectedDigits) {
		return new CollectedInfoImpl(collectedDigits);
	}

	@Override
	public CallSegmentToCancel createCallSegmentToCancel(Integer invokeID, Integer callSegmentID) {
		return new CallSegmentToCancelImpl(invokeID, callSegmentID);
	}

	@Override
	public LegOrCallSegment createLegOrCallSegment(Integer callSegmentID) {
		return new LegOrCallSegmentImpl(callSegmentID);
	}

	@Override
	public LegOrCallSegment createLegOrCallSegment(LegID legID) {
		return new LegOrCallSegmentImpl(legID);
	}

	@Override
	public BackwardServiceInteractionInd createBackwardServiceInteractionInd(
			ConferenceTreatmentIndicator conferenceTreatmentIndicator,
			CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
		return new BackwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callCompletionTreatmentIndicator);
	}

	@Override
	public Carrier createCarrier(ByteBuf data) {
		return new CarrierImpl(data);
	}

	@Override
	public ForwardServiceInteractionInd createForwardServiceInteractionInd(
			ConferenceTreatmentIndicator conferenceTreatmentIndicator,
			CallDiversionTreatmentIndicator callDiversionTreatmentIndicator,
			CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
		return new ForwardServiceInteractionIndImpl(conferenceTreatmentIndicator, callDiversionTreatmentIndicator,
				callingPartyRestrictionIndicator);
	}

	@Override
	public LowLayerCompatibility createLowLayerCompatibility(ByteBuf value) {
		return new LowLayerCompatibilityImpl(value);
	}

	@Override
	public MidCallEvents createMidCallEvents_Completed(DigitsIsup dtmfDigits) {
		return new MidCallEventsImpl(dtmfDigits, true);
	}

	@Override
	public MidCallEvents createMidCallEvents_TimeOut(DigitsIsup dtmfDigits) {
		return new MidCallEventsImpl(dtmfDigits, false);
	}

	@Override
	public MidCallEvents createMidCallEvents_Flash() {
		return new MidCallEventsImpl(false, false);
	}

	@Override
	public MidCallEvents createMidCallEvents_UserCallSuspend() {
		return new MidCallEventsImpl(true, false);
	}

	@Override
	public MidCallEvents createMidCallEvents_UserCallResume() {
		return new MidCallEventsImpl(false, true);
	}

	@Override
	public NotReachableSpecificInfo createNotReachableSpecificInfo(CauseIsup releaseCause) {
		return new NotReachableSpecificInfoImpl(releaseCause);
	}

	@Override
	public AnalyzedInfoSpecificInfo createAnalyzedInfoSpecificInfo(CalledPartyNumberIsup calledPartyNumber) {
		return new AnalyzedInfoSpecificInfoImpl(calledPartyNumber);
	}

	@Override
	public MidCallSpecificInfo createMidCallSpecificInfo(MidCallEvents midCallEvents) {
		return new MidCallSpecificInfoImpl(midCallEvents);
	}

	@Override
	public MidCallSpecificInfo createMidCallSpecificInfo(Integer connectTime) {
		return new MidCallSpecificInfoImpl(connectTime);
	}

	@Override
	public CollectedInfoSpecificInfo createCollectedInfoSpecificInfo(CalledPartyNumberIsup calledPartyNumber) {
		return new CollectedInfoSpecificInfoImpl(calledPartyNumber);
	}

	@Override
	public ChangeOfLocationAlt createChangeOfLocationAlt() {
		return new ChangeOfLocationAltImpl();
	}

	@Override
	public ChangeOfLocation createChangeOfLocation_cellGlobalId(CellGlobalIdOrServiceAreaIdFixedLength value) {
		return new ChangeOfLocationImpl(value,
				ChangeOfLocation.CellGlobalIdOrServiceAreaIdFixedLength_Option.cellGlobalId);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation_serviceAreaId(CellGlobalIdOrServiceAreaIdFixedLength value) {
		return new ChangeOfLocationImpl(value,
				ChangeOfLocation.CellGlobalIdOrServiceAreaIdFixedLength_Option.serviceAreaId);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation(LAIFixedLength locationAreaId) {
		return new ChangeOfLocationImpl(locationAreaId);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation_interSystemHandOver() {
		return new ChangeOfLocationImpl(ChangeOfLocation.Boolean_Option.interSystemHandOver);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation_interPLMNHandOver() {
		return new ChangeOfLocationImpl(ChangeOfLocation.Boolean_Option.interPLMNHandOver);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation_interMSCHandOver() {
		return new ChangeOfLocationImpl(ChangeOfLocation.Boolean_Option.interMSCHandOver);
	}

	@Override
	public ChangeOfLocation createChangeOfLocation(ChangeOfLocationAlt changeOfLocationAlt) {
		return new ChangeOfLocationImpl(changeOfLocationAlt);
	}

	@Override
	public DpSpecificCriteriaAlt createDpSpecificCriteriaAlt(List<ChangeOfLocation> changeOfPositionControlInfo,
			Integer numberOfDigits) {
		return new DpSpecificCriteriaAltImpl(changeOfPositionControlInfo, numberOfDigits);
	}

	@Override
	public MidCallControlInfo createMidCallControlInfo(Integer minimumNumberOfDigits, Integer maximumNumberOfDigits,
			String endOfReplyDigit, String cancelDigit, String startDigit, Integer interDigitTimeout) {
		return new MidCallControlInfoImpl(minimumNumberOfDigits, maximumNumberOfDigits, endOfReplyDigit, cancelDigit,
				startDigit, interDigitTimeout);
	}

	@Override
	public Burst createBurst(Integer numberOfBursts, Integer burstInterval, Integer numberOfTonesInBurst,
			Integer toneDuration, Integer toneInterval) {
		return new BurstImpl(numberOfBursts, burstInterval, numberOfTonesInBurst, toneDuration, toneInterval);
	}

	@Override
	public BurstList createBurstList(Integer warningPeriod, Burst burst) {
		return new BurstListImpl(warningPeriod, burst);
	}

	@Override
	public AudibleIndicator createAudibleIndicator(Boolean tone) {
		return new AudibleIndicatorImpl(tone);
	}

	@Override
	public AudibleIndicator createAudibleIndicator(BurstList burstList) {
		return new AudibleIndicatorImpl(burstList);
	}

	@Override
	public AChChargingAddress createAChChargingAddress(LegID legID) {
		return new AChChargingAddressImpl(legID);
	}

	@Override
	public AChChargingAddress createAChChargingAddress(int srfConnection) {
		return new AChChargingAddressImpl(srfConnection);
	}

	@Override
	public CallingPartysCategoryIsup createCallingPartysCategoryInap(CallingPartyCategory callingPartyCategory)
			throws INAPException {
		try {
			return new CallingPartysCategoryIsupImpl(callingPartyCategory);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public HighLayerCompatibilityIsup createHighLayerCompatibilityInap(
			UserTeleserviceInformation highLayerCompatibility) throws INAPException {
		try {
			return new HighLayerCompatibilityIsupImpl(highLayerCompatibility);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public RedirectionInformationIsup createRedirectionInformationInap(RedirectionInformation redirectionInformation)
			throws INAPException {
		try {
			return new RedirectionInformationIsupImpl(redirectionInformation);
		} catch (ASNParsingException ex) {
			throw new INAPException(ex.getMessage(), ex.getCause());
		}
	}

	@Override
	public LegID createLegID(LegType receivingLeg, LegType sendingLeg) {
		return new LegIDImpl(receivingLeg, sendingLeg);
	}

	@Override
	public MiscCallInfo createMiscCallInfo(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment) {
		return new MiscCallInfoImpl(messageType, dpAssignment);
	}

	@Override
	public IMSI createIMSI(String data) {
		return new IMSIImpl(data);
	}

	@Override
	public ISDNAddressString createISDNAddressString(AddressNature addNature, NumberingPlan numPlan, String address) {
		return new ISDNAddressStringImpl(addNature, numPlan, address);
	}

	@Override
	public ISDNAddressString createISDNAddressString(boolean extension, AddressNature addNature, NumberingPlan numPlan,
			String address) {
		return new ISDNAddressStringImpl(extension, addNature, numPlan, address);
	}

	@Override
	public SupportedCamelPhases createSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3,
			boolean phase4) {
		return new SupportedCamelPhasesImpl(phase1, phase2, phase3, phase4);
	}

	@Override
	public LocationInformation createLocationInformation(Integer ageOfLocationInformation,
			GeographicalInformation geographicalInformation, ISDNAddressString vlrNumber,
			LocationNumberMap locationNumber, CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI,
			MAPExtensionContainer extensionContainer, LSAIdentity selectedLSAId, ISDNAddressString mscNumber,
			GeodeticInformation geodeticInformation, boolean currentLocationRetrieved, boolean saiPresent,
			LocationInformationEPS locationInformationEPS, UserCSGInformation userCSGInformation) {
		return new LocationInformationImpl(ageOfLocationInformation, geographicalInformation, vlrNumber, locationNumber,
				cellGlobalIdOrServiceAreaIdOrLAI, extensionContainer, selectedLSAId, mscNumber, geodeticInformation,
				currentLocationRetrieved, saiPresent, locationInformationEPS, userCSGInformation);
	}

	@Override
	public FreeFormatData createFreeFormatData(ByteBuf value) {
		return new FreeFormatDataImpl(value);
	}

	// billing
	@Override
	public AddOnCharge createAddOnCharge(CurrencyFactorScale currencyFactorScale) {
		return new AddOnChargeImpl(currencyFactorScale);
	}

	@Override
	public AddOnCharge createAddOnCharge(PulseUnits pulseUnits) {
		return new AddOnChargeImpl(pulseUnits);
	}

	@Override
	public AddOnChargingInformation createAddOnChargingInformation(
			ChargingControlIndicators getChargingControlIndicators, AddOnCharge addOncharge,
			CAPINAPExtensions extensions, ChargingReferenceIdentification originationIdentification,
			ChargingReferenceIdentification destinationIdentification, Currency currency) {
		return new AddOnChargingInformationImpl(getChargingControlIndicators, addOncharge, extensions,
				originationIdentification, destinationIdentification, currency);
	}

	@Override
	public ChargeUnitTimeInterval getChargeUnitTimeInterval(Integer data) {
		return new ChargeUnitTimeIntervalImpl(data);
	}

	@Override
	public ChargingControlIndicators getChargingControlIndicators(boolean getSubscriberCharge,
			boolean getImmediateChangeOfActuallyAppliedTariff, boolean getDelayUntilStart) {
		return new ChargingControlIndicatorsImpl(getSubscriberCharge, getImmediateChangeOfActuallyAppliedTariff,
				getDelayUntilStart);
	}

	@Override
	public ChargingReferenceIdentification getChargingReferenceIdentification(List<Long> networkIdentification,
			Long referenceID) {
		return new ChargingReferenceIdentificationImpl(networkIdentification, referenceID);
	}

	@Override
	public ChargingTariff getChargingTariff(TariffCurrency tariffCurrency) {
		return new ChargingTariffImpl(tariffCurrency);
	}

	@Override
	public ChargingTariff getChargingTariff(TariffPulse tariffPulse) {
		return new ChargingTariffImpl(tariffPulse);
	}

	@Override
	public ChargingTariffInformation getChargingTariffInformation(ChargingControlIndicators chargingControlIndicators,
			ChargingTariff chargingTariff, CAPINAPExtensions extensions,
			ChargingReferenceIdentification originationIdentification,
			ChargingReferenceIdentification destinationIdentification, Currency currency) {
		return new ChargingTariffInformationImpl(chargingControlIndicators, chargingTariff, extensions,
				originationIdentification, destinationIdentification, currency);
	}

	@Override
	public CommunicationChargeCurrency getCommunicationChargeCurrency(CurrencyFactorScale currencyFactorScale,
			Integer tariffDuration, SubTariffControl subTariffControl) {
		return new CommunicationChargeCurrencyImpl(currencyFactorScale, tariffDuration, subTariffControl);
	}

	@Override
	public CommunicationChargePulse getCommunicationChargePulse(PulseUnits pulseUnits, Integer chargeUnitTimeInterval,
			Integer tariffDuration) {
		return new CommunicationChargePulseImpl(pulseUnits, chargeUnitTimeInterval, tariffDuration);
	}

	@Override
	public CurrencyFactorScale getCurrencyFactorScale(Integer currencyFactor, Integer currencyScale) {
		return new CurrencyFactorScaleImpl(currencyFactor, currencyScale);
	}

	@Override
	public PulseUnits getPulseUnits(Integer data) {
		return new PulseUnitsImpl(data);
	}

	@Override
	public SubTariffControl getSubTariffControl(boolean oneTimeCharge) {
		return new SubTariffControlIndicatorsImpl(oneTimeCharge);
	}

	@Override
	public TariffControlIndicators getTariffControlIndicators(boolean nonCyclicTariff) {
		return new TariffControlIndicatorsImpl(nonCyclicTariff);
	}

	@Override
	public TariffCurrency getTariffCurrency(TariffCurrencyFormat tariffCurrencyFormat,
			TariffSwitchCurrency getTariffSwitchCurrency) {
		return new TariffCurrencyImpl(tariffCurrencyFormat, getTariffSwitchCurrency);
	}

	@Override
	public TariffCurrencyFormat getTariffCurrencyFormat(
			List<CommunicationChargeCurrency> communicationChargeSequenceCurrency,
			TariffControlIndicators tariffControlIndicators, CurrencyFactorScale callAttemptChargeCurrency,
			CurrencyFactorScale callSetupChargeCurrency) {
		return new TariffCurrencyFormatImpl(communicationChargeSequenceCurrency, tariffControlIndicators,
				callAttemptChargeCurrency, callSetupChargeCurrency);
	}

	@Override
	public TariffDuration getTariffDuration(Integer data) {
		return new TariffDurationImpl(data);
	}

	@Override
	public TariffPulse getTariffPulse(TariffPulseFormat currentTariffPulse, TariffSwitchPulse tariffSwitchPulse) {
		return new TariffPulseImpl(currentTariffPulse, tariffSwitchPulse);
	}

	@Override
	public TariffPulseFormat getTariffPulseFormat(List<CommunicationChargePulse> communicationChargeSequencePulse,
			TariffControlIndicators tariffControlIndicators, PulseUnits callAttemptChargePulse,
			PulseUnits callSetupChargePulse) {
		return new TariffPulseFormatImpl(communicationChargeSequencePulse, tariffControlIndicators,
				callAttemptChargePulse, callSetupChargePulse);
	}

	@Override
	public TariffSwitchCurrency getTariffSwitchCurrency(TariffCurrencyFormat nextTariffCurrency,
			TariffSwitchoverTime tariffSwitchoverTime) {
		return new TariffSwitchCurrencyImpl(nextTariffCurrency, tariffSwitchoverTime);
	}

	@Override
	public TariffSwitchoverTime getTariffSwitchoverTime(Integer data) {
		return new TariffSwitchoverTimeImpl(data);
	}

	@Override
	public TariffSwitchPulse getTariffSwitchPulse(TariffPulseFormat nextTariffPulse,
			TariffSwitchoverTime tariffSwitchoverTime) {
		return new TariffSwitchPulseImpl(nextTariffPulse, tariffSwitchoverTime);
	}

	// cs1 plus
	@Override
	public ApplicationID getApplicationID(Integer data) {
		return new ApplicationIDImpl(data);
	}

	@Override
	public BackwardGVNSIndicator getBackwardGVNSIndicator(BackwardGVNS backwardGVNS) {
		BackwardGVNSIndicatorImpl result = new BackwardGVNSIndicatorImpl(backwardGVNS);
		return result;
	}

	@Override
	public BackwardSuppressionIndicators getBackwardSuppressionIndicators(BackwardSuppression backwardSuppression,
			InstructionIndicator instructionIndicator) {
		return new BackwardSuppressionIndicatorsImpl(backwardSuppression, instructionIndicator);
	}

	@Override
	public CUGCallIndicator getCUGCallIndicator(CUGCall cugCall) {
		CUGCallIndicatorImpl result = new CUGCallIndicatorImpl(cugCall);
		return result;
	}

	@Override
	public CUGInterLockCode getCUGInterLockCode(ByteBuf value) {
		return new CUGInterLockCodeImpl(value);
	}

	@Override
	public DataItemID getDataItemID(ByteBuf attribute0, ByteBuf attribute1, ByteBuf attribute2, ByteBuf attribute3,
			ByteBuf attribute4, ByteBuf attribute5, ByteBuf attribute6, ByteBuf attribute7, ByteBuf attribute8,
			ByteBuf attribute9, ByteBuf attribute10, ByteBuf attribute11, ByteBuf attribute12, ByteBuf attribute13,
			ByteBuf attribute14, ByteBuf attribute15, ByteBuf attribute16, ByteBuf attribute17, ByteBuf attribute18,
			ByteBuf attribute19, ByteBuf attribute20, ByteBuf attribute21, ByteBuf attribute22, ByteBuf attribute23,
			ByteBuf attribute24, ByteBuf attribute25, ByteBuf attribute26, ByteBuf attribute27, ByteBuf attribute28,
			ByteBuf attribute29, ByteBuf attribute30) {
		return new DataItemIDImpl(attribute0, attribute1, attribute2, attribute3, attribute4, attribute5, attribute6,
				attribute7, attribute8, attribute9, attribute10, attribute11, attribute12, attribute13, attribute14,
				attribute15, attribute16, attribute17, attribute18, attribute19, attribute20, attribute21, attribute22,
				attribute23, attribute24, attribute25, attribute26, attribute27, attribute28, attribute29, attribute30);
	}

	@Override
	public DataItemInformation getDataItemInformation(ByteBuf attribute0, ByteBuf attribute1, ByteBuf attribute2,
			ByteBuf attribute3, ByteBuf attribute4, ByteBuf attribute5, ByteBuf attribute6, ByteBuf attribute7,
			ByteBuf attribute8, ByteBuf attribute9, ByteBuf attribute10, ByteBuf attribute11, ByteBuf attribute12,
			ByteBuf attribute13, ByteBuf attribute14, ByteBuf attribute15, ByteBuf attribute16, ByteBuf attribute17,
			ByteBuf attribute18, ByteBuf attribute19, ByteBuf attribute20, ByteBuf attribute21, ByteBuf attribute22,
			ByteBuf attribute23, ByteBuf attribute24, ByteBuf attribute25, ByteBuf attribute26, ByteBuf attribute27,
			ByteBuf attribute28, ByteBuf attribute29, ByteBuf attribute30) {
		return new DataItemInformationImpl(attribute0, attribute1, attribute2, attribute3, attribute4, attribute5,
				attribute6, attribute7, attribute8, attribute9, attribute10, attribute11, attribute12, attribute13,
				attribute14, attribute15, attribute16, attribute17, attribute18, attribute19, attribute20, attribute21,
				attribute22, attribute23, attribute24, attribute25, attribute26, attribute27, attribute28, attribute29,
				attribute30);
	}

	@Override
	public DialogueUserInformation getDialogueUserInformation(SendingFunctionsActive sendingFunctionsActive,
			ReceivingFunctionsRequested receivingFunctionsRequested, Integer trafficSimulationSessionID) {
		return new DialogueUserInformationImpl(sendingFunctionsActive, receivingFunctionsRequested,
				trafficSimulationSessionID);
	}

	@Override
	public ExistingLegs getExistingLegs(LegType legID, boolean linkInd) {
		return new ExistingLegsImpl(legID, linkInd);
	}

	@Override
	public ForwardSuppressionIndicators getForwardSuppressionIndicators(ForwardSuppression forwardSuppression,
			InstructionIndicator instructionIndicator) {
		return new ForwardSuppressionIndicatorsmpl(forwardSuppression, instructionIndicator);
	}

	@Override
	public GenericDigitsSet getGenericDigitsSet(List<DigitsIsup> genericDigits) {
		return new GenericDigitsSetImpl(genericDigits);
	}

	@Override
	public GenericName getGenericName(ByteBuf value) {
		return new GenericNameImpl(value);
	}

	@Override
	public GenericNumbersSet getGenericNumbersSet(List<DigitsIsup> geneicNumbers) {
		return new GenericNumbersSetImpl(geneicNumbers);
	}

	@Override
	public GlobalTitle getGlobalTitle(GlobalTitle0100 title) throws INAPException {
		return new GlobalTitleImpl(title);
	}

	@Override
	public GlobalTitleAndSSN getGlobalTitleAndSSN(GlobalTitle0100 title, Integer ssn) throws INAPException {
		return new GlobalTitleAndSSNImpl(title, ssn);
	}

	@Override
	public HandOverInfo getHandOverInfo(Integer handoverCounter, SCPAddress sendingSCPAddress,
			SCPDialogueInfo sendingSCPDialogueInfo, ByteBuf sendingSCPCorrelationInfo, SCPAddress receivingSCPAddress,
			SCPDialogueInfo receivingSCPDialogueInfo, ByteBuf receivingSCPCorrelationInfo,
			CalledPartyNumberIsup handoverNumber, Integer handoverData) {
		return new HandOverInfoImpl(handoverCounter, sendingSCPAddress, sendingSCPDialogueInfo,
				sendingSCPCorrelationInfo, receivingSCPAddress, receivingSCPDialogueInfo, receivingSCPCorrelationInfo,
				handoverNumber, handoverData);
	}

	@Override
	public LegIDs getLegIDs(List<ExistingLegs> existingLegs) {
		return new LegIDsImpl(existingLegs);
	}

	@Override
	public LimitIndicators getLimitIndicators(Integer duration) {
		return new LimitIndicatorsImpl(duration);
	}

	@Override
	public PointCodeAndSSN getPointCodeAndSSN(Integer spc, Integer ssn) {
		return new PointCodeAndSSNImpl(spc, ssn);
	}

	@Override
	public PointCodeAndSSNANSI getPointCodeAndSSNANSI(Integer network, Integer cluster, Integer member, Integer ssn) {
		return new PointCodeAndSSNANSIImpl(network, cluster, member, ssn);
	}

	@Override
	public ProtocolIndicator getProtocolIndicator(ProtocolIdentifier protocolIdentifier,
			TCAPDialogueLevel tcapDialogueLevel) {
		return new ProtocolIndicatorImpl(protocolIdentifier, tcapDialogueLevel);
	}

	@Override
	public RouteOrigin getRouteOrigin(ByteBuf value) {
		return new RouteOriginImpl(value);
	}

	@Override
	public SCPAddress getSCPAddress(boolean colocated) {
		return new SCPAddressImpl(colocated);
	}

	@Override
	public SCPAddress getSCPAddress(PointCodeAndSSN pointCodeAndSSN) {
		return new SCPAddressImpl(pointCodeAndSSN);
	}

	@Override
	public SCPAddress getSCPAddress(GlobalTitle globalTitle) {
		return new SCPAddressImpl(globalTitle);
	}

	@Override
	public SCPAddress getSCPAddress(GlobalTitleAndSSN globalTitleAndSSN) {
		return new SCPAddressImpl(globalTitleAndSSN);
	}

	@Override
	public SCPAddress getSCPAddress(PointCodeAndSSNANSI pointCodeAndSubSystemNumberANSI) {
		return new SCPAddressImpl(pointCodeAndSubSystemNumberANSI);
	}

	@Override
	public SCPDialogueInfo getSCPDialogueInfo(ProtocolIndicator protocolIndicator,
			DialogueUserInformation dialogueUserInformation) {
		return new SCPDialogueInfoImpl(protocolIndicator, dialogueUserInformation);
	}

	// cicruit switched call - inap
	@Override
	public AddressAndService getAddressAndService(DigitsIsup calledAddressValue, Integer serviceKey,
			DigitsIsup callingAddressValue, LocationNumberIsup locationNumber) {
		return new AddressAndServiceImpl(calledAddressValue, serviceKey, callingAddressValue, locationNumber);
	}

	@Override
	public CalledPartyBusinessGroupID getCalledPartyBusinessGroupID(ByteBuf value) {
		return new CalledPartyBusinessGroupIDImpl(value);
	}

	@Override
	public CalledPartySubaddress getCalledPartySubaddress(ByteBuf value) {
		return new CalledPartySubaddressImpl(value);
	}

	@Override
	public CallingPartyBusinessGroupID getCallingPartyBusinessGroupID(ByteBuf value) {
		return new CallingPartyBusinessGroupIDImpl(value);
	}

	@Override
	public CallingPartySubaddress getCallingPartySubaddress(ByteBuf data) {
		return new CallingPartySubaddressImpl(data);
	}

	@Override
	public ChargingEvent getChargingEvent(EventTypeCharging eventTypeCharging, MonitorMode monitorMode, LegID legID) {
		return new ChargingEventImpl(eventTypeCharging, monitorMode, legID);
	}

	@Override
	public CounterAndValue getCounterAndValue(Integer counterID, Integer counterValue) {
		return new CounterAndValueImpl(counterID, counterValue);
	}

	@Override
	public DisplayInformation getDisplayInformation(String value) {
		return new DisplayInformationImpl(value);
	}

	@Override
	public DpSpecificCommonParameters getDpSpecificCommonParameters(ServiceAddressInformation serviceAddressInformation,
			BearerCapability bearerCapability, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, CGEncountered cgEncountered,
			LocationNumberIsup locationNumber, ServiceProfileIdentifier serviceProfileIdentifier,
			TerminalType terminalType, CAPINAPExtensions extensions, LocationNumberIsup chargeNumber,
			LocationNumberIsup servingAreaID) {
		return new DpSpecificCommonParametersImpl(serviceAddressInformation, bearerCapability, calledPartyNumber,
				callingPartyNumber, callingPartysCategory, ipsspCapabilities, ipAvailable, null, cgEncountered,
				locationNumber, serviceProfileIdentifier, terminalType, extensions, chargeNumber, servingAreaID);

	}

	@Override
	public Entry getEntry(List<Long> agreements) {
		return new EntryImpl(agreements);
	}

	@Override
	public Entry getEntry(Integer networkSpecific) {
		return new EntryImpl(networkSpecific);
	}

	@Override
	public FacilityGroup getFacilityGroup(Integer value, boolean isTrunkGroup) {
		return new FacilityGroupImpl(value, isTrunkGroup);
	}

	@Override
	public FacilityGroup getFacilityGroup(ByteBuf value, boolean isHuntGroup) {
		return new FacilityGroupImpl(value, isHuntGroup);
	}

	@Override
	public FilteredCallTreatment getFilteredCallTreatment(ByteBuf sfBillingChargingCharacteristics,
			InformationToSend informationToSend, Integer maximumNumberOfCounters, CauseIsup cause) {
		return new FilteredCallTreatmentImpl(sfBillingChargingCharacteristics, informationToSend,
				maximumNumberOfCounters, cause);
	}

	@Override
	public FilteringCharacteristics getFilteringCharacteristics(Integer value, Boolean isInterval) {
		return new FilteringCharacteristicsImpl(value, isInterval);
	}

	@Override
	public FilteringCriteria getFilteringCriteria(Integer serviceKey) {
		return new FilteringCriteriaImpl(serviceKey);
	}

	@Override
	public FilteringCriteria getFilteringCriteria(AddressAndService addressAndService) {
		return new FilteringCriteriaImpl(addressAndService);
	}

	@Override
	public FilteringTimeOut getFilteringTimeOut(Integer duration) {
		return new FilteringTimeOutImpl(duration);
	}

	@Override
	public FilteringTimeOut getFilteringTimeOut(DateAndTime stopTime) {
		return new FilteringTimeOutImpl(stopTime);
	}

	@Override
	public GenericNumbers getGenericNumbers(List<GenericNumberIsup> genericNumbers) {
		return new GenericNumbersImpl(genericNumbers);
	}

	@Override
	public HoldCause getHoldCause(ByteBuf value) {
		return new HoldCauseImpl(value);
	}

	@Override
	public INServiceCompatibilityIndication getINServiceCompatibilityIndication(List<Entry> entries) {
		return new INServiceCompatibilityIndicationImpl(entries);
	}

	@Override
	public IPAvailable getIPAvailable(ByteBuf value) {
		return new IPAvailableImpl(value);
	}

	@Override
	public ISDNAccessRelatedInformation getISDNAccessRelatedInformation(ByteBuf value) {
		return new ISDNAccessRelatedInformationImpl(value);
	}

	@Override
	public MidCallControlInfoINAP getMidCallControlInfo(List<MidCallControlInfoItem> midCallControlInfoItems) {
		return new MidCallControlInfoINAPImpl(midCallControlInfoItems);
	}

	@Override
	public MidCallControlInfoItem getMidCallControlInfoItem(MidCallInfoType midCallInfoType,
			MidCallReportType midCallReportType) {
		return new MidCallControlInfoItemImpl(midCallInfoType, midCallReportType);
	}

	@Override
	public MidCallInfoType getMidCallInfoType(DigitsIsup inServiceControlCodeLow, DigitsIsup inServiceControlCodeHigh) {
		return new MidCallInfoTypeImpl(inServiceControlCodeLow, inServiceControlCodeHigh);
	}

	@Override
	public ResourceAddress getResourceAddress(CalledPartyNumberIsup ipRoutingAddress) {
		return new ResourceAddressImpl(ipRoutingAddress);
	}

	@Override
	public ResourceAddress getResourceAddress(LegType legID) {
		return new ResourceAddressImpl(legID);
	}

	@Override
	public ResourceAddress getResourceAddress(boolean none) {
		return new ResourceAddressImpl(none);
	}

	@Override
	public ResourceID getResourceID(DigitsIsup lineID) {
		return new ResourceIDImpl(lineID);
	}

	@Override
	public ResourceID getResourceID(FacilityGroup facilityGroup) {
		return new ResourceIDImpl(facilityGroup);
	}

	@Override
	public ResourceID getResourceID(Integer value, boolean isTrunkGroupID) {
		return new ResourceIDImpl(value, isTrunkGroupID);
	}

	@Override
	public RouteList getRouteList(List<ByteBuf> data) {
		return new RouteListImpl(data);
	}

	@Override
	public ServiceAddressInformation getServiceAddressInformation(Integer serviceKey, MiscCallInfo miscCallInfo,
			TriggerType triggerType) {
		return new ServiceAddressInformationImpl(serviceKey, miscCallInfo, triggerType);
	}

	@Override
	public ServiceInteractionIndicators getServiceInteractionIndicators(ByteBuf value) {
		return new ServiceInteractionIndicatorsImpl(value);
	}

	@Override
	public ServiceProfileIdentifier getServiceProfileIdentifier(ByteBuf value) {
		return new ServiceProfileIdentifierImpl(value);
	}

	@Override
	public Tariff getTariff(ChargingTariffInformation chargingTariffInformation) {
		return new TariffImpl(chargingTariffInformation);
	}

	@Override
	public Tariff getTariff(AddOnChargingInformation addOnChargingInformation) {
		return new TariffImpl(addOnChargingInformation);
	}

	@Override
	public USIInformation getUSIInformation(ByteBuf value) {
		return new USIInformationImpl(value);
	}

	@Override
	public USIServiceIndicator getUSIServiceIndicator(List<Long> global) {
		return new USIServiceIndicatorImpl(global);
	}

	@Override
	public USIServiceIndicator getUSIServiceIndicator(ByteBuf local) {
		return new USIServiceIndicatorImpl(local);
	}

	@Override
	public TariffInformation getTariffInformation(Integer numberOfStartPulses, Integer startInterval,
			IntervalAccuracy startIntervalAccuracy, Integer numberOfPeriodicPulses, Integer periodicInterval,
			IntervalAccuracy periodicIntervalAccuracy, DateAndTime activationTime) {
		return new TariffInformationImpl(numberOfStartPulses, startInterval, startIntervalAccuracy,
				numberOfPeriodicPulses, periodicInterval, periodicIntervalAccuracy, activationTime);
	}

	@Override
	public EventSpecificInfoCharging getEventSpecificInfoCharging(TariffInformation tariffInformation) {
		return new EventSpecificInfoChargingImpl(tariffInformation);
	}

	@Override
	public EventSpecificInfoCharging getEventSpecificInfoCharging(ByteBuf tariffIndicator) {
		return new EventSpecificInfoChargingImpl(tariffIndicator);
	}

	@Override
	public EventSpecificInfoCharging getEventSpecificInfoCharging(ChargeNoChargeIndication chargeNoChargeIndication) {
		return new EventSpecificInfoChargingImpl(chargeNoChargeIndication);
	}

	@Override
	public ChargeMessage getChargeMessage(EventTypeCharging eventTypeCharging,
			EventSpecificInfoCharging eventSpecificInfoCharging) {
		return new ChargeMessageImpl(eventTypeCharging, eventSpecificInfoCharging);
	}

	@Override
	public ChargingInformation getChargingInformation(boolean orderStartOfCharging, ChargeMessage chargeMessage,
			Integer pulseBurst, boolean createDefaultBillingRecord) {
		return new ChargingInformationImpl(orderStartOfCharging, chargeMessage, pulseBurst, createDefaultBillingRecord);
	}

	@Override
	public ChargingAnalysisInputData getChargingAnalysisInputData(ByteBuf chargingOrigin, ByteBuf tariffActivityCode,
			Integer chargingCode) {
		return new ChargingAnalysisInputDataImpl(chargingOrigin, tariffActivityCode, chargingCode);
	}

	@Override
	public SCIBillingChargingCharacteristicsCS1 getSCIBillingChargingCharacteristicsCS1(
			ChargingInformation chargingInformation) {
		return new SCIBillingChargingCharacteristicsCS1Impl(chargingInformation);
	}

	@Override
	public SCIBillingChargingCharacteristicsCS1 getSCIBillingChargingCharacteristicsCS1(
			ChargingAnalysisInputData chargingAnalysisInputData) {
		return new SCIBillingChargingCharacteristicsCS1Impl(chargingAnalysisInputData);
	}

	@Override
	public ReportCondition getReportCondition(boolean value, boolean immediately) {
		return new ReportConditionImpl(value, immediately);
	}

	@Override
	public ReportCondition getReportCondition(Integer reportAtChargeLimit) {
		return new ReportConditionImpl(reportAtChargeLimit);
	}

	@Override
	public RequestedReportInfo getRequestedReportInfo(boolean accumulatedCharge, boolean actualTariff,
			boolean chargeableDuration, boolean timeOfAnswer) {
		return new RequestedReportInfoImpl(accumulatedCharge, actualTariff, chargeableDuration, timeOfAnswer);
	}

	@Override
	public AchBillingChargingCharacteristicsCS1 getAchBillingChargingCharacteristicsCS1(ReportCondition reportCondition,
			RequestedReportInfo requestedReportInfo) {
		return new AchBillingChargingCharacteristicsCS1Impl(reportCondition, requestedReportInfo);
	}

	@Override
	public LegInformation getLegInformation(LegType legType, LegStatus legStatus) {
		return new LegInformationImpl(legType, legStatus);
	}
}