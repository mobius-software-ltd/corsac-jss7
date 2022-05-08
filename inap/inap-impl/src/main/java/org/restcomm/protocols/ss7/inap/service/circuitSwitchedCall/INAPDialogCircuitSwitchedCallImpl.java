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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

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
import org.restcomm.protocols.ss7.inap.INAPDialogImpl;
import org.restcomm.protocols.ss7.inap.INAPProviderImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.INAPServiceBase;
import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
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
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPDialogCircuitSwitchedCallImpl extends INAPDialogImpl implements INAPDialogCircuitSwitchedCall {
	private static final long serialVersionUID = 1L;

	protected INAPDialogCircuitSwitchedCallImpl(INAPApplicationContext appCntx, Dialog tcapDialog,
			INAPProviderImpl capProviderImpl, INAPServiceBase capService) {
		super(appCntx, tcapDialog, capProviderImpl, capService);
	}

	@Override
	public Integer addInitialDPRequest(int serviceKey, CallingPartyNumberIsup dialledDigits,
			CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartysCategoryIsup callingPartysCategory,
			CallingPartySubaddress callingPartySubaddress, CGEncountered cgEncountered,
			IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable, LocationNumberIsup locationNumber,
			MiscCallInfo miscCallInfo, OriginalCalledNumberIsup originalCalledPartyID,
			ServiceProfileIdentifier serviceProfileIdentifier, TerminalType terminalType, CAPINAPExtensions extensions,
			TriggerType triggerType, HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
			throws INAPException {
		return addInitialDPRequest(_Timer_Default, serviceKey, dialledDigits, calledPartyNumber, callingPartyNumber,
				callingPartyBusinessGroupID, callingPartysCategory, callingPartySubaddress, cgEncountered,
				ipsspCapabilities, ipAvailable, locationNumber, miscCallInfo, originalCalledPartyID,
				serviceProfileIdentifier, terminalType, extensions, triggerType, highLayerCompatibility,
				serviceInteractionIndicators, additionalCallingPartyNumber, forwardCallIndicators, bearerCapability,
				eventTypeBCSM, redirectingPartyID, redirectionInformation);
	}

	@Override
	public Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CallingPartyNumberIsup dialledDigits,
			CalledPartyNumberIsup calledPartyNumber, CallingPartyNumberIsup callingPartyNumber,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartysCategoryIsup callingPartysCategory,
			CallingPartySubaddress callingPartySubaddress, CGEncountered cgEncountered,
			IPSSPCapabilities ipsspCapabilities, IPAvailable ipAvailable, LocationNumberIsup locationNumber,
			MiscCallInfo miscCallInfo, OriginalCalledNumberIsup originalCalledPartyID,
			ServiceProfileIdentifier serviceProfileIdentifier, TerminalType terminalType, CAPINAPExtensions extensions,
			TriggerType triggerType, HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
			throws INAPException {

		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC)
			throw new INAPException(
					"Bad application context name for addInitialDPRequest: must be Q1218_generic_SSF_to_SCF_AC or Core_INAP_CS1_SSP_to_SCP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		InitialDPRequestImpl req = new InitialDPRequestImpl(serviceKey, dialledDigits, calledPartyNumber,
				callingPartyNumber, callingPartyBusinessGroupID, callingPartysCategory, callingPartySubaddress,
				cgEncountered, ipsspCapabilities, ipAvailable, locationNumber, miscCallInfo, originalCalledPartyID,
				serviceProfileIdentifier, terminalType, extensions, triggerType, highLayerCompatibility,
				serviceInteractionIndicators, additionalCallingPartyNumber, forwardCallIndicators, bearerCapability,
				eventTypeBCSM, redirectingPartyID, redirectionInformation);

		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.initialDP, req, true, false);
	}

	@Override
	public Integer addInitialDPRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, LocationNumberIsup locationNumber,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, TriggerType triggerType,
			HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation, LegIDs legIDs,
			RouteOrigin routeOrigin, boolean testIndication, CUGCallIndicator cugCallIndicator,
			CUGInterLockCode cugInterLockCode, GenericDigitsSet genericDigitsSet, GenericNumbersSet genericNumberSet,
			CauseIsup cause, HandOverInfo handOverInfo, ForwardGVNSIsup forwardGVNSIndicator,
			BackwardGVNS backwardGVNSIndicator) throws INAPException {
		return addInitialDPRequest(serviceKey, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
				cgEncountered, ipsspCapabilities, locationNumber, originalCalledPartyID, extensions, triggerType,
				highLayerCompatibility, serviceInteractionIndicators, additionalCallingPartyNumber,
				forwardCallIndicators, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation,
				legIDs, routeOrigin, testIndication, cugCallIndicator, cugInterLockCode, genericDigitsSet,
				genericNumberSet, cause, handOverInfo, forwardGVNSIndicator, backwardGVNSIndicator);
	}

	@Override
	public Integer addInitialDPRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, LocationNumberIsup locationNumber,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, TriggerType triggerType,
			HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation, LegIDs legIDs,
			RouteOrigin routeOrigin, boolean testIndication, CUGCallIndicator cugCallIndicator,
			CUGInterLockCode cugInterLockCode, GenericDigitsSet genericDigitsSet, GenericNumbersSet genericNumberSet,
			CauseIsup cause, HandOverInfo handOverInfo, ForwardGVNSIsup forwardGVNSIndicator,
			BackwardGVNS backwardGVNSIndicator) throws INAPException {

		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addInitialDPRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		InitialDPRequestImpl req = new InitialDPRequestImpl(serviceKey, calledPartyNumber, callingPartyNumber,
				callingPartysCategory, cgEncountered, ipsspCapabilities, locationNumber, originalCalledPartyID,
				extensions, triggerType, highLayerCompatibility, serviceInteractionIndicators,
				additionalCallingPartyNumber, forwardCallIndicators, bearerCapability, eventTypeBCSM,
				redirectingPartyID, redirectionInformation, legIDs, routeOrigin, testIndication, cugCallIndicator,
				cugInterLockCode, genericDigitsSet, genericNumberSet, cause, handOverInfo, forwardGVNSIndicator,
				backwardGVNSIndicator);

		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.initialDP, req, true, false);
	}

	@Override
	public Integer addApplyChargingReportRequest(ByteBuf callResult) throws INAPException {
		return addApplyChargingReportRequest(_Timer_Default, callResult);
	}

	@Override
	public Integer addApplyChargingReportRequest(int customInvokeTimeout, ByteBuf callResult) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addApplyChargingReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC,"
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ApplyChargingReportRequestImpl req = new ApplyChargingReportRequestImpl(callResult);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.applyChargingReport, req, true, false);
	}

	@Override
	public Integer addApplyChargingReportRequest(CallResultCS1 callResult) throws INAPException {
		return addApplyChargingReportRequest(_Timer_Default, callResult);
	}

	@Override
	public Integer addApplyChargingReportRequest(int customInvokeTimeout, CallResultCS1 callResult) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addApplyChargingReportRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B,"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC,Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B,Ericcson_cs1plus_SCP_to_SSP_AC or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ApplyChargingReportRequestCS1Impl req = new ApplyChargingReportRequestCS1Impl(callResult);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.applyChargingReport, req, true, false);
	}
	
	@Override
	public Integer addApplyChargingRequest(AChBillingChargingCharacteristics aChBillingChargingCharacteristics,
			Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions)
			throws INAPException {
		return addApplyChargingRequest(_Timer_Default, aChBillingChargingCharacteristics,
				sendCalculationToSCPIndication, partyToCharge, extensions);
	}

	@Override
	public Integer addApplyChargingRequest(int customInvokeTimeout,
			AChBillingChargingCharacteristics aChBillingChargingCharacteristics, Boolean sendCalculationToSCPIndication,
			LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addApplyChargingRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC,"
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ApplyChargingRequestImpl req = new ApplyChargingRequestImpl(aChBillingChargingCharacteristics,
				sendCalculationToSCPIndication, partyToCharge, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.applyCharging, req, true, false);
	}

	@Override
	public Integer addApplyChargingRequest(AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics,
			Boolean sendCalculationToSCPIndication, LegID partyToCharge, CAPINAPExtensions extensions)
			throws INAPException {
		return addApplyChargingRequest(_Timer_Default, aChBillingChargingCharacteristics,
				sendCalculationToSCPIndication, partyToCharge, extensions);
	}

	@Override
	public Integer addApplyChargingRequest(int customInvokeTimeout,
			AchBillingChargingCharacteristicsCS1 aChBillingChargingCharacteristics, Boolean sendCalculationToSCPIndication,
			LegID partyToCharge, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addApplyChargingRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B,"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC,Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B,Ericcson_cs1plus_SCP_to_SSP_AC or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ApplyChargingRequestCS1Impl req = new ApplyChargingRequestCS1Impl(aChBillingChargingCharacteristics,
				sendCalculationToSCPIndication, partyToCharge, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.applyCharging, req, true, false);
	}

	@Override
	public Integer addCallInformationReportRequest(List<RequestedInformation> requestedInformationList,
			DigitsIsup correlationID, CAPINAPExtensions extensions) throws INAPException {
		return addCallInformationReportRequest(_Timer_Default, requestedInformationList, correlationID, extensions);
	}

	@Override
	public Integer addCallInformationReportRequest(int customInvokeTimeout,
			List<RequestedInformation> requestedInformationList, DigitsIsup correlationID, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addCallInformationReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallInformationReportRequestImpl req = new CallInformationReportRequestImpl(requestedInformationList,
				correlationID, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.callInformationReport, req, true, false);
	}

	@Override
	public Integer addCallInformationReportRequest(LegType legID, List<RequestedInformation> requestedInformationList,
			CAPINAPExtensions extensions) throws INAPException {
		return addCallInformationReportRequest(_Timer_Default, legID, requestedInformationList, extensions);
	}

	@Override
	public Integer addCallInformationReportRequest(int customInvokeTimeout, LegType legID,
			List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCallInformationReportRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallInformationReportRequestImpl req = new CallInformationReportRequestImpl(legID, requestedInformationList,
				extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.callInformationReport, req, true, false);
	}

	@Override
	public Integer addCallInformationRequest(List<RequestedInformationType> requestedInformationTypeList,
			DigitsIsup correlationID, CAPINAPExtensions extensions) throws INAPException {
		return addCallInformationRequest(_Timer_Default, requestedInformationTypeList, correlationID, extensions);
	}

	@Override
	public Integer addCallInformationRequest(int customInvokeTimeout,
			List<RequestedInformationType> requestedInformationTypeList, DigitsIsup correlationID,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addCallInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallInformationRequestImpl req = new CallInformationRequestImpl(requestedInformationTypeList, correlationID,
				extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.callInformationRequest, req, true, false);
	}

	@Override
	public Integer addCallInformationRequest(LegType legID, List<RequestedInformationType> requestedInformationTypeList,
			CAPINAPExtensions extensions) throws INAPException {
		return addCallInformationRequest(_Timer_Default, legID, requestedInformationTypeList, extensions);
	}

	@Override
	public Integer addCallInformationRequest(int customInvokeTimeout, LegType legID,
			List<RequestedInformationType> requestedInformationTypeList, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCallInformationRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallInformationRequestImpl req = new CallInformationRequestImpl(legID, requestedInformationTypeList,
				extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.callInformationRequest, req, true, false);
	}

	@Override
	public Integer addConnectRequest(DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
			DigitsIsup correlationID, Integer cutAndPaste, ForwardingCondition forwardingCondition,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID,
			RouteList routeList, ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
			throws INAPException {
		return addConnectRequest(_Timer_Default, destinationRoutingAddress, alertingPattern, correlationID, cutAndPaste,
				forwardingCondition, isdnAccessRelatedInformation, originalCalledPartyID, routeList, scfID,
				travellingClassMark, extensions, carrier, serviceInteractionIndicators, callingPartyNumber,
				callingPartysCategory, redirectingPartyID, redirectionInformation);
	}

	@Override
	public Integer addConnectRequest(int customInvokeTimeout, DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, DigitsIsup correlationID, Integer cutAndPaste,
			ForwardingCondition forwardingCondition, ISDNAccessRelatedInformation isdnAccessRelatedInformation,
			OriginalCalledNumberIsup originalCalledPartyID, RouteList routeList, ScfID scfID,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier,
			ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber,
			CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC"
							+ "Ericcson_cs1plus_SCP_to_SSP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectRequestImpl req = new ConnectRequestImpl(destinationRoutingAddress, alertingPattern, correlationID,
				cutAndPaste, forwardingCondition, isdnAccessRelatedInformation, originalCalledPartyID, routeList, scfID,
				travellingClassMark, extensions, carrier, serviceInteractionIndicators, callingPartyNumber,
				callingPartysCategory, redirectingPartyID, redirectionInformation);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connect, req, true, false);
	}

	@Override
	public Integer addConnectRequest(LegType legToBeCreated, BearerCapability bearerCapabilities,
			CUGCallIndicator cugCallIndicator, CUGInterLockCode cugInterLockCode,
			ForwardCallIndicatorsIsup forwardCallIndicators, GenericDigitsSet genericDigitsSet,
			GenericNumbersSet genericNumberSet, HighLayerCompatibilityIsup highLayerCompatibility,
			ForwardGVNSIsup forwardGVNSIndicator, DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, DigitsIsup correlationID, Integer cutAndPaste,
			OriginalCalledNumberIsup originalCalledPartyID, RouteList routeList, ScfID scfID,
			CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
			throws INAPException {
		return addConnectRequest(_Timer_Default, legToBeCreated, bearerCapabilities, cugCallIndicator, cugInterLockCode,
				forwardCallIndicators, genericDigitsSet, genericNumberSet, highLayerCompatibility, forwardGVNSIndicator,
				destinationRoutingAddress, alertingPattern, correlationID, cutAndPaste, originalCalledPartyID,
				routeList, scfID, extensions, carrier, serviceInteractionIndicators, callingPartyNumber,
				callingPartysCategory, redirectingPartyID, redirectionInformation);
	}

	@Override
	public Integer addConnectRequest(int customInvokeTimeout, LegType legToBeCreated, BearerCapability bearerCapabilities,
			CUGCallIndicator cugCallIndicator, CUGInterLockCode cugInterLockCode,
			ForwardCallIndicatorsIsup forwardCallIndicators, GenericDigitsSet genericDigitsSet,
			GenericNumbersSet genericNumberSet, HighLayerCompatibilityIsup highLayerCompatibility,
			ForwardGVNSIsup forwardGVNSIndicator, DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, DigitsIsup correlationID, Integer cutAndPaste,
			OriginalCalledNumberIsup originalCalledPartyID, RouteList routeList, ScfID scfID,
			CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectRequestImpl req = new ConnectRequestImpl(legToBeCreated, bearerCapabilities, cugCallIndicator,
				cugInterLockCode, forwardCallIndicators, genericDigitsSet, genericNumberSet, highLayerCompatibility,
				forwardGVNSIndicator, destinationRoutingAddress, alertingPattern, correlationID, cutAndPaste,
				originalCalledPartyID, routeList, scfID, extensions, carrier, serviceInteractionIndicators,
				callingPartyNumber, callingPartysCategory, redirectingPartyID, redirectionInformation);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connect, req, true, false);
	}

	@Override
	public Integer addContinueRequest() throws INAPException {
		return addContinueRequest(_Timer_Default);
	}

	@Override
	public Integer addContinueRequest(int customInvokeTimeout) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addContinueRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC,Core_INAP_CS1_SCP_to_SSP_AC"
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B,Ericcson_cs1plus_SCP_to_SSP_AC or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.continueCode, null, true, false);
	}

	@Override
	public Integer addContinueRequest(LegType legID) throws INAPException {
		return addContinueRequest(_Timer_Default, legID);
	}

	@Override
	public Integer addContinueRequest(int customInvokeTimeout, LegType legID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ContinueCS1PlusRequestImpl req = new ContinueCS1PlusRequestImpl(legID);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.continueCode, req, true, false);
	}

	@Override
	public Integer addEventReportBCSMRequest(EventTypeBCSM eventTypeBCSM, DigitsIsup bcsmEventCorrelationID,
			EventSpecificInformationBCSM eventSpecificInformationBCSM, LegID legID, MiscCallInfo miscCallInfo,
			CAPINAPExtensions extensions) throws INAPException {
		return addEventReportBCSMRequest(_Timer_Default, eventTypeBCSM, bcsmEventCorrelationID,
				eventSpecificInformationBCSM, legID, miscCallInfo, extensions);
	}

	@Override
	public Integer addEventReportBCSMRequest(int customInvokeTimeout, EventTypeBCSM eventTypeBCSM,
			DigitsIsup bcsmEventCorrelationID, EventSpecificInformationBCSM eventSpecificInformationBCSM, LegID legID,
			MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addEventReportBCSMRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, "
							+ "Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		EventReportBCSMRequestImpl req = new EventReportBCSMRequestImpl(eventTypeBCSM, bcsmEventCorrelationID,
				eventSpecificInformationBCSM, legID, miscCallInfo, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.eventReportBCSM, req, true, false);
	}

	@Override
	public Integer addRequestReportBCSMEventRequest(List<BCSMEvent> bcsmEventList, DigitsIsup bcsmEventCorrelationID,
			CAPINAPExtensions extensions) throws INAPException {
		return addRequestReportBCSMEventRequest(_Timer_Default, bcsmEventList, bcsmEventCorrelationID, extensions);
	}

	@Override
	public Integer addRequestReportBCSMEventRequest(int customInvokeTimeout, List<BCSMEvent> bcsmEventList,
			DigitsIsup bcsmEventCorrelationID, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addRequestReportBCSMEventRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, "
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RequestReportBCSMEventRequestImpl req = new RequestReportBCSMEventRequestImpl(bcsmEventList,
				bcsmEventCorrelationID, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.requestReportBCSMEvent, req, true, false);
	}

	@Override
	public Integer addReleaseCallRequest(CauseIsup cause) throws INAPException {
		return addReleaseCallRequest(_Timer_Default, cause);
	}

	@Override
	public Integer addReleaseCallRequest(int customInvokeTimeout, CauseIsup cause) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addReleaseCallRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC,Core_INAP_CS1_SCP_to_SSP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ReleaseCallRequestImpl req = new ReleaseCallRequestImpl(cause);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.releaseCall, req, true, false);
	}

	@Override
	public Integer addActivityTestRequest() throws INAPException {
		return addActivityTestRequest(_Timer_Default);
	}

	@Override
	public Integer addActivityTestRequest(int customInvokeTimeout) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addActivityTestRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC,Core_INAP_CS1_IP_to_SCP_AC,Core_INAP_CS1_SCP_to_SSP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC, "
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, Ericcson_cs1plus_IP_to_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.activityTest, null, true, false);
	}

	@Override
	public void addActivityTestResponse(int invokeId) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addActivityTestResponse: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, "
							+ "Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, "
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, Ericcson_cs1plus_IP_to_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		this.sendDataComponent(invokeId, null, null, null, null, null, false, true);
	}

	@Override
	public Integer addAssistRequestInstructionsRequest(DigitsIsup correlationID, IPAvailable ipAvailable,
			IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions) throws INAPException {
		return addAssistRequestInstructionsRequest(_Timer_Default, correlationID, ipAvailable, ipSSPCapabilities,
				extensions);
	}

	@Override
	public Integer addAssistRequestInstructionsRequest(int customInvokeTimeout, DigitsIsup correlationID,
			IPAvailable ipAvailable, IPSSPCapabilities ipSSPCapabilities, CAPINAPExtensions extensions)
			throws INAPException {		
		if (this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addAssistRequestInstructionsRequest: must be Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_IP_to_SCP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		AssistRequestInstructionsRequestImpl req = new AssistRequestInstructionsRequestImpl(correlationID, ipAvailable,
				ipSSPCapabilities, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.assistRequestInstructions, req, true, false);
	}

	@Override
	public Integer addEstablishTemporaryConnectionRequest(DigitsIsup assistingSSPIPRoutingAddress,
			DigitsIsup correlationID, LegID legID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		return addEstablishTemporaryConnectionRequest(_Timer_Default, assistingSSPIPRoutingAddress, correlationID,
				legID, scfID, extensions, carrier, serviceInteractionIndicators);
	}

	@Override
	public Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, DigitsIsup assistingSSPIPRoutingAddress,
			DigitsIsup correlationID, LegID legID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addEstablishTemporaryConnectionRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		EstablishTemporaryConnectionRequestImpl req = new EstablishTemporaryConnectionRequestImpl(
				assistingSSPIPRoutingAddress, correlationID, legID, scfID, extensions, carrier,
				serviceInteractionIndicators);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.establishTemporaryConnection, req, true, false);
	}

	@Override
	public Integer addEstablishTemporaryConnectionRequest(LegType LegID, DigitsIsup assistingSSPIPRoutingAddress,
			DigitsIsup correlationID, ScfID scfID, CAPINAPExtensions extensions, Carrier carrier,
			ServiceInteractionIndicators serviceInteractionIndicators, RouteList routeList) throws INAPException {
		return addEstablishTemporaryConnectionRequest(_Timer_Default, LegID, assistingSSPIPRoutingAddress,
				correlationID, scfID, extensions, carrier, serviceInteractionIndicators, routeList);
	}

	@Override
	public Integer addEstablishTemporaryConnectionRequest(int customInvokeTimeout, LegType LegID,
			DigitsIsup assistingSSPIPRoutingAddress, DigitsIsup correlationID, ScfID scfID,
			CAPINAPExtensions extensions, Carrier carrier, ServiceInteractionIndicators serviceInteractionIndicators,
			RouteList routeList) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addEstablishTemporaryConnectionRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		EstablishTemporaryConnectionRequestImpl req = new EstablishTemporaryConnectionRequestImpl(LegID,
				assistingSSPIPRoutingAddress, correlationID, scfID, extensions, carrier, serviceInteractionIndicators,
				routeList);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.establishTemporaryConnection, req, true, false);
	}

	@Override
	public Integer addDisconnectForwardConnectionRequest() throws INAPException {
		return addDisconnectForwardConnectionRequest(_Timer_Default);
	}

	@Override
	public Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addDisconnectForwardConnectionRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.disconnectForwardConnection, null, true, false);
	}

	@Override
	public Integer addDisconnectForwardConnectionRequest(LegType legID) throws INAPException {
		return addDisconnectForwardConnectionRequest(_Timer_Default, legID);
	}

	@Override
	public Integer addDisconnectForwardConnectionRequest(int customInvokeTimeout, LegType legID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addDisconnectForwardConnectionRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		DisconnectForwardConnectionCS1PlusRequestImpl req = new DisconnectForwardConnectionCS1PlusRequestImpl(legID);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.disconnectForwardConnection, req, true, false);
	}

	@Override
	public Integer addConnectToResourceRequest(CalledPartyNumberIsup ipRoutingAddress, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		return addConnectToResourceRequest(_Timer_Default, ipRoutingAddress, extensions, serviceInteractionIndicators);
	}

	@Override
	public Integer addConnectToResourceRequest(LegType legID, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		return addConnectToResourceRequest(_Timer_Default, legID, extensions, serviceInteractionIndicators);
	}

	@Override
	public Integer addConnectToResourceRequest(ResourceAddress resourceAddress, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		return addConnectToResourceRequest(_Timer_Default, resourceAddress, extensions, serviceInteractionIndicators);
	}

	@Override
	public Integer addConnectToResourceRequest(boolean none, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		return addConnectToResourceRequest(_Timer_Default, none, extensions, serviceInteractionIndicators);
	}

	@Override
	public Integer addConnectToResourceRequest(int customInvokeTimeout, CalledPartyNumberIsup ipRoutingAddress,
			CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectToResourceRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(ipRoutingAddress, extensions,
				serviceInteractionIndicators);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connectToResource, req, true, false);
	}

	@Override
	public Integer addConnectToResourceRequest(int customInvokeTimeout, LegType legID, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectToResourceRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(legID, extensions,
				serviceInteractionIndicators);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connectToResource, req, true, false);
	}

	@Override
	public Integer addConnectToResourceRequest(int customInvokeTimeout, ResourceAddress resourceAddress,
			CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectToResourceRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(resourceAddress, extensions,
				serviceInteractionIndicators);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connectToResource, req, true, false);
	}

	@Override
	public Integer addConnectToResourceRequest(int customInvokeTimeout, boolean none, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addConnectToResourceRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ConnectToResourceRequestImpl req = new ConnectToResourceRequestImpl(none, extensions,
				serviceInteractionIndicators);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.connectToResource, req, true, false);
	}

	@Override
	public Integer addFurnishChargingInformationRequest(ByteBuf FCIBCCCAMELsequence1) throws INAPException {
		return addFurnishChargingInformationRequest(_Timer_Default, FCIBCCCAMELsequence1);
	}

	@Override
	public Integer addFurnishChargingInformationRequest(int customInvokeTimeout, ByteBuf FCIBCCCAMELsequence1)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addFurnishChargingInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		FurnishChargingInformationRequestImpl req = new FurnishChargingInformationRequestImpl(FCIBCCCAMELsequence1);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.furnishChargingInformation, req, true, false);
	}

	//cs1 flavour
	@Override
	public Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
			LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException {
		return addSendChargingInformationRequest(_Timer_Default, sciBillingChargingCharacteristics, partyToCharge,
				extensions);
	}

	@Override
	public Integer addSendChargingInformationRequest(int customInvokeTimeout,
			SCIBillingChargingCharacteristics sciBillingChargingCharacteristics, LegType partyToCharge,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addSendChargingInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SendChargingInformationRequestImpl req = new SendChargingInformationRequestImpl(
				sciBillingChargingCharacteristics, partyToCharge, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.sendChargingInformation, req, true, false);
	}
	
	//cs1+ flavour
	@Override
	public Integer addSendChargingInformationRequest(SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics,
			LegType partyToCharge, CAPINAPExtensions extensions) throws INAPException {
		return addSendChargingInformationRequest(_Timer_Default, sciBillingChargingCharacteristics, partyToCharge,
				extensions);
	}

	@Override
	public Integer addSendChargingInformationRequest(int customInvokeTimeout,
			SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics, LegType partyToCharge,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addSendChargingInformationRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B, "
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B, Ericcson_cs1plus_SCP_to_SSP_AC or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SendChargingInformationCS1RequestImpl req = new SendChargingInformationCS1RequestImpl(
				sciBillingChargingCharacteristics, partyToCharge, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.sendChargingInformation, req, true, false);
	}
	
	@Override
	public Integer addSpecializedResourceReportRequest(Integer linkedId) throws INAPException {
		return addSpecializedResourceReportRequest(_Timer_Default, linkedId);
	}

	@Override
	public Integer addSpecializedResourceReportRequest(int customInvokeTimeout, Integer linkedId) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addSpecializedResourceReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, "
							+ "Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC,Ericcson_cs1plus_SCP_to_SSP_AC"
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_SCP_to_SSP_AC_REV_B or Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		return this.sendDataComponent(null, linkedId, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.specializedResourceReport, null, true, false);
	}

	@Override
	public Integer addSpecializedResourceReportRequest(Integer linkedId, boolean value, boolean isStarted)
			throws INAPException {
		return addSpecializedResourceReportRequest(_Timer_Default, linkedId, value, isStarted);
	}

	@Override
	public Integer addSpecializedResourceReportRequest(int customInvokeTimeout, Integer linkedId, boolean value,
			boolean isStarted) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addSpecializedResourceReportRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC or , Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SpecializedResourceReportCS1PlusRequestImpl req = new SpecializedResourceReportCS1PlusRequestImpl(value,
				isStarted);
		return this.sendDataComponent(null, linkedId, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.specializedResourceReport, req, true, false);
	}

	@Override
	public Integer addPlayAnnouncementRequest(InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
			Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException {
		return addPlayAnnouncementRequest(_Timer_Default, informationToSend, disconnectFromIPForbidden,
				requestAnnouncementCompleteNotification, extensions);
	}

	@Override
	public Integer addPlayAnnouncementRequest(int customInvokeTimeout, InformationToSend informationToSend,
			Boolean disconnectFromIPForbidden, Boolean requestAnnouncementCompleteNotification,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPlayAnnouncementRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, "
							+ "Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC,Ericcson_cs1plus_IP_to_SCP_AC,Ericcson_cs1plus_SCP_to_SSP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B,Ericcson_cs1plus_IP_to_SCP_AC_REV_B or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlLong();
		else
			customTimeout = customInvokeTimeout;

		PlayAnnouncementRequestImpl req = new PlayAnnouncementRequestImpl(informationToSend, disconnectFromIPForbidden,
				requestAnnouncementCompleteNotification, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.playAnnouncement, req, true, false);
	}

	@Override
	public Integer addPlayAnnouncementRequest(LegType legID, Boolean requestAnnouncementStarted,
			InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
			Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException {
		return addPlayAnnouncementRequest(_Timer_Default, legID, requestAnnouncementStarted, informationToSend,
				disconnectFromIPForbidden, requestAnnouncementCompleteNotification, extensions);
	}

	@Override
	public Integer addPlayAnnouncementRequest(int customInvokeTimeout, LegType legID, Boolean requestAnnouncementStarted,
			InformationToSend informationToSend, Boolean disconnectFromIPForbidden,
			Boolean requestAnnouncementCompleteNotification, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPlayAnnouncementRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC or , Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlLong();
		else
			customTimeout = customInvokeTimeout;

		PlayAnnouncementRequestImpl req = new PlayAnnouncementRequestImpl(legID, requestAnnouncementStarted,
				informationToSend, disconnectFromIPForbidden, requestAnnouncementCompleteNotification, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.playAnnouncement, req, true, false);
	}

	@Override
	public Integer addPromptAndCollectUserInformationRequest(CollectedInfo collectedInfo,
			Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions)
			throws INAPException {
		return addPromptAndCollectUserInformationRequest(_Timer_Default, collectedInfo, disconnectFromIPForbidden,
				informationToSend, extensions);
	}

	@Override
	public Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, CollectedInfo collectedInfo,
			Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPromptAndCollectUserInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, "
							+ "Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC or Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlLong();
		else
			customTimeout = customInvokeTimeout;

		PromptAndCollectUserInformationRequestImpl req = new PromptAndCollectUserInformationRequestImpl(collectedInfo,
				disconnectFromIPForbidden, informationToSend, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.promptAndCollectUserInformation, req, true, false);
	}

	@Override
	public Integer addPromptAndCollectUserInformationRequest(LegType legID, Boolean requestAnnouncementStarted,
			Boolean requestAnnouncementComplete, CollectedInfo collectedInfo, Boolean disconnectFromIPForbidden,
			InformationToSend informationToSend, CAPINAPExtensions extensions) throws INAPException {
		return addPromptAndCollectUserInformationRequest(_Timer_Default, legID, requestAnnouncementStarted,
				requestAnnouncementComplete, collectedInfo, disconnectFromIPForbidden, informationToSend, extensions);
	}

	@Override
	public Integer addPromptAndCollectUserInformationRequest(int customInvokeTimeout, LegType legID,
			Boolean requestAnnouncementStarted, Boolean requestAnnouncementComplete, CollectedInfo collectedInfo,
			Boolean disconnectFromIPForbidden, InformationToSend informationToSend, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPromptAndCollectUserInformationRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC or , Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlLong();
		else
			customTimeout = customInvokeTimeout;

		PromptAndCollectUserInformationRequestImpl req = new PromptAndCollectUserInformationRequestImpl(legID,
				requestAnnouncementStarted, requestAnnouncementComplete, collectedInfo, disconnectFromIPForbidden,
				informationToSend, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.promptAndCollectUserInformation, req, true, false);
	}

	@Override
	public void addPromptAndCollectUserInformationResponse(int invokeId, DigitsIsup digitsResponse)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPromptAndCollectUserInformationResponse: must be "
							+ "Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_IP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		PromptAndCollectUserInformationResponseImpl res = new PromptAndCollectUserInformationResponseImpl(
				digitsResponse);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.promptAndCollectUserInformation,
				res, false, true);
	}

	@Override
	public void addPromptAndCollectUserInformationResponse(int invokeId, String ia5Response) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addPromptAndCollectUserInformationResponse: must be "
							+ "Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, "
							+ "Core_INAP_CS1_IP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		PromptAndCollectUserInformationResponseImpl res = new PromptAndCollectUserInformationResponseImpl(ia5Response);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.promptAndCollectUserInformation,
				res, false, true);
	}

	@Override
	public Integer addCancelRequest(Integer invokeID) throws INAPException {
		return addCancelRequest(_Timer_Default, invokeID);
	}

	@Override
	public Integer addCancelRequest() throws INAPException {
		return addCancelRequest(_Timer_Default);
	}

	@Override
	public Integer addCancelRequest(int customInvokeTimeout, Integer invokeID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCancelRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, "
							+ "Q1218_SCF_to_SSF_status_reporting_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC, "
							+ "Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, Ericcson_cs1plus_IP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CancelRequestImpl req = new CancelRequestImpl(invokeID);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.cancelCode, req, true, false);
	}

	@Override
	public Integer addCancelRequest(int customInvokeTimeout) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCancelRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, "
							+ "Q1218_SCF_to_SSF_status_reporting_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC, Core_INAP_CS1_IP_to_SCP_AC, "
							+ "Core_INAP_CS1_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, Ericcson_cs1plus_IP_to_SCP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CancelRequestImpl req = new CancelRequestImpl(true);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.cancelCode, req, true, false);
	}

	@Override
	public Integer addInitiateCallAttemptRequest(DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, ISDNAccessRelatedInformation isdnAccessRelatedInformation,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber)
			throws INAPException {
		return addInitiateCallAttemptRequest(_Timer_Default, destinationRoutingAddress, alertingPattern,
				isdnAccessRelatedInformation, travellingClassMark, extensions, serviceInteractionIndicators,
				callingPartyNumber);
	}

	@Override
	public Integer addInitiateCallAttemptRequest(int customInvokeTimeout,
			DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, LocationNumberIsup travellingClassMark,
			CAPINAPExtensions extensions, ServiceInteractionIndicators serviceInteractionIndicators,
			CallingPartyNumberIsup callingPartyNumber) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC)
			throw new INAPException(
					"Bad application context name for addInitiateCallAttemptRequest: must be Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or "
							+ "Core_INAP_CS1_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		InitiateCallAttemptRequestImpl req = new InitiateCallAttemptRequestImpl(destinationRoutingAddress,
				alertingPattern, isdnAccessRelatedInformation, travellingClassMark, extensions,
				serviceInteractionIndicators, callingPartyNumber);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.initiateCallAttempt, req, true, false);
	}

	@Override
	public Integer addInitiateCallAttemptRequest(OriginalCalledNumberIsup originalCalledPartyID, LegType legToBeCreated,
			CallingPartysCategoryIsup callingPartysCategory, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, BearerCapability nearerCapability,
			CUGCallIndicator cugCallIndicator, CUGInterLockCode cugInterLockCode,
			ForwardCallIndicators forwardCallIndicators, GenericDigitsSet genericDigitsSet,
			GenericNumbersSet genericNumberSet, HighLayerCompatibilityIsup highLayerCompatibility,
			ForwardGVNSIsup forwardGVNSIndicator, DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber,
			RouteList routeList) throws INAPException {
		return addInitiateCallAttemptRequest(_Timer_Default, originalCalledPartyID, legToBeCreated,
				callingPartysCategory, redirectingPartyID, redirectionInformation, nearerCapability, cugCallIndicator,
				cugInterLockCode, forwardCallIndicators, genericDigitsSet, genericNumberSet, highLayerCompatibility,
				forwardGVNSIndicator, destinationRoutingAddress, alertingPattern, extensions,
				serviceInteractionIndicators, callingPartyNumber, routeList);
	}

	@Override
	public Integer addInitiateCallAttemptRequest(int customInvokeTimeout, OriginalCalledNumberIsup originalCalledPartyID,
			LegType legToBeCreated, CallingPartysCategoryIsup callingPartysCategory,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			BearerCapability nearerCapability, CUGCallIndicator cugCallIndicator, CUGInterLockCode cugInterLockCode,
			ForwardCallIndicators forwardCallIndicators, GenericDigitsSet genericDigitsSet,
			GenericNumbersSet genericNumberSet, HighLayerCompatibilityIsup highLayerCompatibility,
			ForwardGVNSIsup forwardGVNSIndicator, DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, CAPINAPExtensions extensions,
			ServiceInteractionIndicators serviceInteractionIndicators, CallingPartyNumberIsup callingPartyNumber,
			RouteList routeList) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addInitiateCallAttemptRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		InitiateCallAttemptRequestImpl req = new InitiateCallAttemptRequestImpl(originalCalledPartyID, legToBeCreated,
				callingPartysCategory, redirectingPartyID, redirectionInformation, nearerCapability, cugCallIndicator,
				cugInterLockCode, forwardCallIndicators, genericDigitsSet, genericNumberSet, highLayerCompatibility,
				forwardGVNSIndicator, destinationRoutingAddress, alertingPattern, extensions,
				serviceInteractionIndicators, callingPartyNumber, routeList);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.initiateCallAttempt, req, true, false);
	}

	@Override
	public Integer addCollectInformationRequest(AlertingPattern alertingPattern, NumberingPlan numberingPlan,
			OriginalCalledPartyIDIsup originalCalledPartyID, LocationNumberIsup travellingClassMark,
			CAPINAPExtensions extensions, CallingPartyNumberIsup сallingPartyNumber,
			CalledPartyNumberIsup dialledDigits) throws INAPException {
		return addCollectInformationRequest(_Timer_Default, alertingPattern, numberingPlan, originalCalledPartyID,
				travellingClassMark, extensions, сallingPartyNumber, dialledDigits);
	}

	@Override
	public Integer addCollectInformationRequest(int customInvokeTimeout, AlertingPattern alertingPattern,
			NumberingPlan numberingPlan, OriginalCalledPartyIDIsup originalCalledPartyID,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			CallingPartyNumberIsup сallingPartyNumber, CalledPartyNumberIsup dialledDigits) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCollectInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, "
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		CollectInformationRequestImpl req = new CollectInformationRequestImpl(alertingPattern, numberingPlan,
				originalCalledPartyID, travellingClassMark, extensions, сallingPartyNumber, dialledDigits);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.collectInformation, req, true, false);
	}

	@Override
	public Integer addCallGapRequest(GapCriteria gapCriteria, GapIndicators gapIndicators, ControlType controlType,
			GapTreatment gapTreatment, CAPINAPExtensions capExtensions) throws INAPException {
		return addCallGapRequest(_Timer_Default, gapCriteria, gapIndicators, controlType, gapTreatment, capExtensions);
	}

	@Override
	public Integer addCallGapRequest(int customInvokeTimeout, GapCriteria gapCriteria, GapIndicators gapIndicators,
			ControlType controlType, GapTreatment gapTreatment, CAPINAPExtensions capExtensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCallGapRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_SCF_to_SSF_traffic_management_AC, Core_INAP_CS1_SSP_to_SCP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC,Ericcson_cs1plus_SCP_to_SSP_AC"
							+ "Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC,Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B,Ericcson_cs1plus_SCP_to_SSP_AC_REV_B  or Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallGapRequestImpl req = new CallGapRequestImpl(gapCriteria, gapIndicators, controlType, gapTreatment,
				capExtensions);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.callGap, req, true, false);
	}

	@Override
	public Integer addCallGapRequest(DateAndTime startTime, GapCriteria gapCriteria, GapIndicators gapIndicators,
			ControlType controlType, GapTreatment gapTreatment, CAPINAPExtensions capExtensions) throws INAPException {
		return addCallGapRequest(_Timer_Default, startTime, gapCriteria, gapIndicators, controlType, gapTreatment,
				capExtensions);
	}

	@Override
	public Integer addCallGapRequest(int customInvokeTimeout, DateAndTime startTime, GapCriteria gapCriteria,
			GapIndicators gapIndicators, ControlType controlType, GapTreatment gapTreatment,
			CAPINAPExtensions capExtensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCallGapRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_SCP_to_SSP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallGapRequestImpl req = new CallGapRequestImpl(startTime, gapCriteria, gapIndicators, controlType,
				gapTreatment, capExtensions);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.callGap, req, true, false);
	}

	@Override
	public Integer addActivateServiceFilteringRequest(FilteredCallTreatment filteredCallTreatment,
			FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut,
			FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions)
			throws INAPException {
		return addActivateServiceFilteringRequest(_Timer_Default, filteredCallTreatment, filteringCharacteristics,
				filteringTimeOut, filteringCriteria, startTime, extensions);
	}

	@Override
	public Integer addActivateServiceFilteringRequest(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment,
			FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut,
			FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_service_management_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SSF_to_SCF_service_management_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_service_management_AC)
			throw new INAPException(
					"Bad application context name for addActivateServiceFilteringRequest: must be Q1218_SCF_to_SSF_service_management_AC, Q1218_SSF_to_SCF_service_management_AC, "
							+ "Core_INAP_CS1_SCP_to_SSP_service_management_AC or Core_INAP_CS1_SSP_to_SCP_service_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		ActivateServiceFilteringRequestImpl req = new ActivateServiceFilteringRequestImpl(filteredCallTreatment,
				filteringCharacteristics, filteringTimeOut, filteringCriteria, startTime, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.activateServiceFiltering, req, true, false);
	}

	@Override
	public Integer addActivateServiceFilteringRequest(FilteredCallTreatment filteredCallTreatment,
			FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut,
			FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions,
			ByteBuf scfCorrelationInfo) throws INAPException {
		return addActivateServiceFilteringRequest(_Timer_Default, filteredCallTreatment, filteringCharacteristics,
				filteringTimeOut, filteringCriteria, startTime, extensions, scfCorrelationInfo);
	}

	@Override
	public Integer addActivateServiceFilteringRequest(int customInvokeTimeout, FilteredCallTreatment filteredCallTreatment,
			FilteringCharacteristics filteringCharacteristics, FilteringTimeOut filteringTimeOut,
			FilteringCriteria filteringCriteria, DateAndTime startTime, CAPINAPExtensions extensions,
			ByteBuf scfCorrelationInfo) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addActivateServiceFilteringRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_SCP_to_SSP_AC or "
							+ "Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		ActivateServiceFilteringRequestImpl req = new ActivateServiceFilteringRequestImpl(filteredCallTreatment,
				filteringCharacteristics, filteringTimeOut, filteringCriteria, startTime, extensions,
				scfCorrelationInfo);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.activateServiceFiltering, req, true, false);
	}

	@Override
	public Integer addEventNotificationCharging(ByteBuf eventTypeCharging, ByteBuf eventSpecificInformationCharging,
			LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode) throws INAPException {
		return addEventNotificationCharging(_Timer_Default, eventTypeCharging, eventSpecificInformationCharging, legID,
				extensions, monitorMode);
	}

	@Override
	public Integer addEventNotificationCharging(int customInvokeTimeout, ByteBuf eventTypeCharging,
			ByteBuf eventSpecificInformationCharging, LegID legID, CAPINAPExtensions extensions, MonitorMode monitorMode)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addEventNotificationCharging: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, "
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		EventNotificationChargingRequestImpl req = new EventNotificationChargingRequestImpl(eventTypeCharging,
				eventSpecificInformationCharging, legID, extensions, monitorMode);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.eventNotificationCharging, req, true, false);
	}

	@Override
	public Integer addRequestNotificationChargingEvent(List<ChargingEvent> chargingEventList) throws INAPException {
		return addRequestNotificationChargingEvent(_Timer_Default, chargingEventList);
	}

	@Override
	public Integer addRequestNotificationChargingEvent(int customInvokeTimeout, List<ChargingEvent> chargingEventList)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addRequestNotificationChargingEvent: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC, Core_INAP_CS1_SSP_to_SCP_AC, Core_INAP_CS1_SCP_to_SSP_AC, "
							+ "Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RequestNotificationChargingEventRequestImpl req = new RequestNotificationChargingEventRequestImpl(
				chargingEventList);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.requestNotificationChargingEvent, req, true, false);
	}

	@Override
	public Integer addServiceFilteringResponseRequest(List<CounterAndValue> counterAndValue,
			FilteringCriteria filteringCriteria, CAPINAPExtensions extensions, ResponseCondition responseCondition)
			throws INAPException {
		return addServiceFilteringResponseRequest(_Timer_Default, counterAndValue, filteringCriteria, extensions,
				responseCondition);
	}

	@Override
	public Integer addServiceFilteringResponseRequest(int customInvokeTimeout, List<CounterAndValue> counterAndValue,
			FilteringCriteria filteringCriteria, CAPINAPExtensions extensions, ResponseCondition responseCondition)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_service_management_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SSF_to_SCF_service_management_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_service_management_AC)
			throw new INAPException(
					"Bad application context name for addServiceFilteringResponseRequest: must be Q1218_SCF_to_SSF_service_management_AC, Q1218_SSF_to_SCF_service_management_AC, "
							+ "Core_INAP_CS1_SCP_to_SSP_service_management_AC or Core_INAP_CS1_SSP_to_SCP_service_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ServiceFilteringResponseRequestImpl req = new ServiceFilteringResponseRequestImpl(counterAndValue,
				filteringCriteria, extensions, responseCondition);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.serviceFilteringResponse, req, true, false);
	}

	@Override
	public Integer addServiceFilteringResponseRequest(List<CounterAndValue> counterAndValue,
			FilteringCriteria filteringCriteria, ResponseCondition responseCondition, ByteBuf scfCorrelationInfo)
			throws INAPException {
		return addServiceFilteringResponseRequest(_Timer_Default, counterAndValue, filteringCriteria, responseCondition,
				scfCorrelationInfo);
	}

	@Override
	public Integer addServiceFilteringResponseRequest(int customInvokeTimeout, List<CounterAndValue> counterAndValue,
			FilteringCriteria filteringCriteria, ResponseCondition responseCondition, ByteBuf scfCorrelationInfo)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addServiceFilteringResponseRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_SCP_to_SSP_service_management_AC or "
							+ "Ericcson_cs1plus_SSP_to_SCP_service_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ServiceFilteringResponseRequestImpl req = new ServiceFilteringResponseRequestImpl(counterAndValue,
				filteringCriteria, responseCondition, scfCorrelationInfo);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.serviceFilteringResponse, req, true, false);
	}

	@Override
	public Integer addAnalysedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyNumberIsup dialedDigits, CallingPartyBusinessGroupID callingPartyBusinessGroupID,
			CallingPartySubaddress callingPartySubaddress, FacilityGroup callingFacilityGroup,
			Integer callingFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			LocationNumberIsup featureCode, LocationNumberIsup accessCode, Carrier carrier) throws INAPException {
		return addAnalysedInformationRequest(_Timer_Default, dpSpecificCommonParameters, dialedDigits,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions, featureCode, accessCode, carrier);
	}

	@Override
	public Integer addAnalysedInformationRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters, CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			LocationNumberIsup featureCode, LocationNumberIsup accessCode, Carrier carrier) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addAnalysedInformationRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		AnalysedInformationRequestImpl req = new AnalysedInformationRequestImpl(dpSpecificCommonParameters,
				dialedDigits, callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup,
				callingFacilityGroupMember, originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation,
				routeList, travellingClassMark, extensions, featureCode, accessCode, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.analysedInformation, req, true, false);
	}

	@Override
	public Integer addAnalyseInformationRequest(DestinationRoutingAddress destinationRoutingAddress,
			AlertingPattern alertingPattern, ISDNAccessRelatedInformation isdnAccessRelatedInformation,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CalledPartyNumberIsup calledPartyNumber, LocationNumberIsup chargeNumber,
			LocationNumberIsup travellingClassMark, Carrier carrier) throws INAPException {
		return addAnalyseInformationRequest(_Timer_Default, destinationRoutingAddress, alertingPattern,
				isdnAccessRelatedInformation, originalCalledPartyID, extensions, callingPartyNumber,
				callingPartysCategory, calledPartyNumber, chargeNumber, travellingClassMark, carrier);
	}

	@Override
	public Integer addAnalyseInformationRequest(int customInvokeTimeout,
			DestinationRoutingAddress destinationRoutingAddress, AlertingPattern alertingPattern,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID,
			CAPINAPExtensions extensions, CallingPartyNumberIsup callingPartyNumber,
			CallingPartysCategoryIsup callingPartysCategory, CalledPartyNumberIsup calledPartyNumber,
			LocationNumberIsup chargeNumber, LocationNumberIsup travellingClassMark, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addAnalyseInformationRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		AnalyseInformationRequestImpl req = new AnalyseInformationRequestImpl(destinationRoutingAddress,
				alertingPattern, isdnAccessRelatedInformation, originalCalledPartyID, extensions, callingPartyNumber,
				callingPartysCategory, calledPartyNumber, chargeNumber, travellingClassMark, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.analyseInformation, req, true, false);
	}

	@Override
	public Integer addCollectedInformationRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyNumberIsup dialedDigits, CallingPartyBusinessGroupID callingPartyBusinessGroupID,
			CallingPartySubaddress callingPartySubaddress, FacilityGroup callingFacilityGroup,
			Integer callingFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
			LocationNumberIsup accessCode, Carrier carrier) throws INAPException {
		return addCollectedInformationRequest(_Timer_Default, dpSpecificCommonParameters, dialedDigits,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, travellingClassMark,
				extensions, featureCode, accessCode, carrier);
	}

	@Override
	public Integer addCollectedInformationRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters, CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, LocationNumberIsup featureCode,
			LocationNumberIsup accessCode, Carrier carrier) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addCollectedInformationRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CollectedInformationRequestImpl req = new CollectedInformationRequestImpl(dpSpecificCommonParameters,
				dialedDigits, callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup,
				callingFacilityGroupMember, originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation,
				travellingClassMark, extensions, featureCode, accessCode, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.collectedInformation, req, true, false);
	}

	@Override
	public Integer addHoldCallInNetworkRequest(HoldCause holdCause) throws INAPException {
		return addHoldCallInNetworkRequest(_Timer_Default, holdCause);
	}

	@Override
	public Integer addHoldCallInNetworkRequest(int customInvokeTimeout, HoldCause holdCause) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addHoldCallInNetworkRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		HoldCallInNetworkRequestImpl req = new HoldCallInNetworkRequestImpl(holdCause);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.holdCallInNetwork, req, true, false);
	}

	@Override
	public Integer addHoldCallInNetworkRequest() throws INAPException {
		return addHoldCallInNetworkRequest(_Timer_Default);
	}

	@Override
	public Integer addHoldCallInNetworkRequest(int customInvokeTimeout) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addHoldCallInNetworkRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		HoldCallInNetworkRequestImpl req = new HoldCallInNetworkRequestImpl(true);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.holdCallInNetwork, req, true, false);
	}

	@Override
	public Integer addOMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FeatureRequestIndicator featureRequestIndicator, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		return addOMidCallRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, callingPartyBusinessGroupID, callingPartySubaddress, featureRequestIndicator,
				extensions, carrier);
	}

	@Override
	public Integer addOMidCallRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FeatureRequestIndicator featureRequestIndicator, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addOMidCallRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		OMidCallRequestImpl req = new OMidCallRequestImpl(dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, callingPartyBusinessGroupID, callingPartySubaddress, featureRequestIndicator,
				extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.oMidCall, req, true, false);
	}

	@Override
	public Integer addTMidCallRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FeatureRequestIndicator featureRequestIndicator, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		return addTMidCallRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, callingPartyBusinessGroupID, callingPartySubaddress, featureRequestIndicator,
				extensions, carrier);
	}

	@Override
	public Integer addTMidCallRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FeatureRequestIndicator featureRequestIndicator, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTMidCallRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TMidCallRequestImpl req = new TMidCallRequestImpl(dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, callingPartyBusinessGroupID, callingPartySubaddress, featureRequestIndicator,
				extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.tMidCall, req, true, false);
	}

	@Override
	public Integer addOAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, RouteList routeList,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException {
		return addOAnswerRequest(_Timer_Default, dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, originalCalledPartyID,
				redirectingPartyID, redirectionInformation, routeList, travellingClassMark, extensions);
	}

	@Override
	public Integer addOAnswerRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, RouteList routeList,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addOAnswerRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		OAnswerRequestImpl req = new OAnswerRequestImpl(dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, originalCalledPartyID,
				redirectingPartyID, redirectionInformation, routeList, travellingClassMark, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.oAnswer, req, true, false);
	}

	@Override
	public Integer addOriginationAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyNumberIsup dialedDigits, CallingPartyBusinessGroupID callingPartyBusinessGroupID,
			CallingPartySubaddress callingPartySubaddress, FacilityGroup callingFacilityGroup,
			Integer callingFacilityGroupMember, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			Carrier carrier) throws INAPException {
		return addOriginationAttemptAuthorizedRequest(_Timer_Default, dpSpecificCommonParameters, dialedDigits,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				travellingClassMark, extensions, carrier);
	}

	@Override
	public Integer addOriginationAttemptAuthorizedRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters, CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addOriginationAttemptAuthorizedRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		OriginationAttemptAuthorizedRequestImpl req = new OriginationAttemptAuthorizedRequestImpl(
				dpSpecificCommonParameters, dialedDigits, callingPartyBusinessGroupID, callingPartySubaddress,
				callingFacilityGroup, callingFacilityGroupMember, travellingClassMark, extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.originationAttemptAuthorized, req, true, false);
	}

	@Override
	public Integer addRouteSelectFailureRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyNumberIsup dialedDigits, CallingPartyBusinessGroupID callingPartyBusinessGroupID,
			CallingPartySubaddress callingPartySubaddress, FacilityGroup callingFacilityGroup,
			Integer callingFacilityGroupMember, CauseIsup failureCause, OriginalCalledNumberIsup originalCalledPartyID,
			DigitsIsup prefix, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, RouteList routeList,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		return addRouteSelectFailureRequest(_Timer_Default, dpSpecificCommonParameters, dialedDigits,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				failureCause, originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions, carrier);
	}

	@Override
	public Integer addRouteSelectFailureRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters, CalledPartyNumberIsup dialedDigits,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember, CauseIsup getFailureCause,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addRouteSelectFailureRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RouteSelectFailureRequestImpl req = new RouteSelectFailureRequestImpl(dpSpecificCommonParameters, dialedDigits,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				getFailureCause, originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.routeSelectFailure, req, true, false);
	}

	@Override
	public Integer addOCalledPartyBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters, CauseIsup busyCause,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		return addOCalledPartyBusyRequest(_Timer_Default, dpSpecificCommonParameters, busyCause,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions, carrier);
	}

	@Override
	public Integer addOCalledPartyBusyRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters, CauseIsup busyCause,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addOCalledPartyBusyRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		OCalledPartyBusyRequestImpl req = new OCalledPartyBusyRequestImpl(dpSpecificCommonParameters, busyCause,
				callingPartyBusinessGroupID, callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember,
				originalCalledPartyID, prefix, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.oCalledPartyBusy, req, true, false);
	}

	@Override
	public Integer addONoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		return addONoAnswerRequest(_Timer_Default, dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, originalCalledPartyID, prefix,
				redirectingPartyID, redirectionInformation, routeList, travellingClassMark, extensions, carrier);
	}

	@Override
	public Integer addONoAnswerRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, DigitsIsup prefix,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions, Carrier carrier)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addONoAnswerRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ONoAnswerRequestImpl req = new ONoAnswerRequestImpl(dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, originalCalledPartyID, prefix,
				redirectingPartyID, redirectionInformation, routeList, travellingClassMark, extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.oNoAnswer, req, true, false);
	}

	@Override
	public Integer addODisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember, CauseIsup releaseCause,
			RouteList routeList, CAPINAPExtensions extensions, Carrier carrier, Integer connectTime)
			throws INAPException {
		return addODisconnectRequest(_Timer_Default, dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, releaseCause, routeList,
				extensions, carrier, connectTime);
	}

	@Override
	public Integer addODisconnectRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, CallingPartySubaddress callingPartySubaddress,
			FacilityGroup callingFacilityGroup, Integer callingFacilityGroupMember, CauseIsup releaseCause,
			RouteList routeList, CAPINAPExtensions extensions, Carrier carrier, Integer connectTime)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addODisconnectRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ODisconnectRequestImpl req = new ODisconnectRequestImpl(dpSpecificCommonParameters, callingPartyBusinessGroupID,
				callingPartySubaddress, callingFacilityGroup, callingFacilityGroupMember, releaseCause, routeList,
				extensions, carrier, connectTime);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.oDisconnect, req, true, false);
	}

	@Override
	public Integer addTermAttemptAuthorizedRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, OriginalCalledNumberIsup originalCalledPartyID,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions)
			throws INAPException {
		return addTermAttemptAuthorizedRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, callingPartyBusinessGroupID, originalCalledPartyID, redirectingPartyID,
				redirectionInformation, routeList, travellingClassMark, extensions);
	}

	@Override
	public Integer addTermAttemptAuthorizedRequest(int customInvokeTimeout,
			DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			CallingPartyBusinessGroupID callingPartyBusinessGroupID, OriginalCalledNumberIsup originalCalledPartyID,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTermAttemptAuthorizedRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TermAttemptAuthorizedRequestImpl req = new TermAttemptAuthorizedRequestImpl(dpSpecificCommonParameters,
				calledPartyBusinessGroupID, calledPartySubaddress, callingPartyBusinessGroupID, originalCalledPartyID,
				redirectingPartyID, redirectionInformation, routeList, travellingClassMark, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.termAttemptAuthorized, req, true, false);
	}

	@Override
	public Integer addTBusyRequest(DpSpecificCommonParameters dpSpecificCommonParameters, CauseIsup busyCause,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, RouteList routeList,
			LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions) throws INAPException {
		return addTBusyRequest(_Timer_Default, dpSpecificCommonParameters, busyCause, calledPartyBusinessGroupID,
				calledPartySubaddress, originalCalledPartyID, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions);
	}

	@Override
	public Integer addTBusyRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CauseIsup busyCause, CalledPartyBusinessGroupID calledPartyBusinessGroupID,
			CalledPartySubaddress calledPartySubaddress, OriginalCalledNumberIsup originalCalledPartyID,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation,
			RouteList routeList, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTBusyRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TBusyRequestImpl req = new TBusyRequestImpl(dpSpecificCommonParameters, busyCause, calledPartyBusinessGroupID,
				calledPartySubaddress, originalCalledPartyID, redirectingPartyID, redirectionInformation, routeList,
				travellingClassMark, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.tBusy, req, true, false);
	}

	@Override
	public Integer addTNoAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, LocationNumberIsup travellingClassMark,
			CAPINAPExtensions extensions) throws INAPException {
		return addTNoAnswerRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, originalCalledPartyID,
				redirectingPartyID, redirectionInformation, travellingClassMark, extensions);
	}

	@Override
	public Integer addTNoAnswerRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember,
			OriginalCalledNumberIsup originalCalledPartyID, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, LocationNumberIsup travellingClassMark,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTNoAnswerRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TNoAnswerRequestImpl req = new TNoAnswerRequestImpl(dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, originalCalledPartyID,
				redirectingPartyID, redirectionInformation, travellingClassMark, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.tNoAnswer, req, true, false);
	}

	@Override
	public Integer addTAnswerRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember, CAPINAPExtensions extensions)
			throws INAPException {
		return addTAnswerRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, extensions);
	}

	@Override
	public Integer addTAnswerRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember, CAPINAPExtensions extensions)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTAnswerRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TAnswerRequestImpl req = new TAnswerRequestImpl(dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.tAnswer, req, true, false);
	}

	@Override
	public Integer addTDisconnectRequest(DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember, CauseIsup releaseCause,
			CAPINAPExtensions extensions, Integer connectTime) throws INAPException {
		return addTDisconnectRequest(_Timer_Default, dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, releaseCause, extensions,
				connectTime);
	}

	@Override
	public Integer addTDisconnectRequest(int customInvokeTimeout, DpSpecificCommonParameters dpSpecificCommonParameters,
			CalledPartyBusinessGroupID calledPartyBusinessGroupID, CalledPartySubaddress calledPartySubaddress,
			FacilityGroup calledFacilityGroup, Integer calledFacilityGroupMember, CauseIsup releaseCause,
			CAPINAPExtensions extensions, Integer connectTime) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addTDisconnectRequest: must be Q1218_DP_specific_SSF_to_SCF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		TDisconnectRequestImpl req = new TDisconnectRequestImpl(dpSpecificCommonParameters, calledPartyBusinessGroupID,
				calledPartySubaddress, calledFacilityGroup, calledFacilityGroupMember, releaseCause, extensions,
				connectTime);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.tDisconnect, req, true, false);
	}

	@Override
	public Integer addSelectRouteRequest(CalledPartyNumberIsup destinationNumberRoutingAddress,
			AlertingPattern alertingPattern, DigitsIsup correlationID,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID,
			RouteList routeList, ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			Carrier carrier) throws INAPException {
		return addSelectRouteRequest(_Timer_Default, destinationNumberRoutingAddress, alertingPattern, correlationID,
				isdnAccessRelatedInformation, originalCalledPartyID, routeList, scfID, travellingClassMark, extensions,
				carrier);
	}

	@Override
	public Integer addSelectRouteRequest(int customInvokeTimeout, CalledPartyNumberIsup destinationNumberRoutingAddress,
			AlertingPattern alertingPattern, DigitsIsup correlationID,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, OriginalCalledNumberIsup originalCalledPartyID,
			RouteList routeList, ScfID scfID, LocationNumberIsup travellingClassMark, CAPINAPExtensions extensions,
			Carrier carrier) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addSelectRouteRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SelectRouteRequestImpl req = new SelectRouteRequestImpl(destinationNumberRoutingAddress, alertingPattern,
				correlationID, isdnAccessRelatedInformation, originalCalledPartyID, routeList, scfID,
				travellingClassMark, extensions, carrier);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.selectRoute, req, true, false);
	}

	@Override
	public Integer addSelectFacilityRequest(AlertingPattern alertingPattern,
			CalledPartyNumberIsup destinationNumberRoutingAddress,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, FacilityGroup calledFacilityGroup,
			Integer calledFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID,
			CAPINAPExtensions extensions, Carrier carrier) throws INAPException {
		return addSelectFacilityRequest(_Timer_Default, alertingPattern, destinationNumberRoutingAddress,
				isdnAccessRelatedInformation, calledFacilityGroup, calledFacilityGroupMember, originalCalledPartyID,
				extensions);
	}

	@Override
	public Integer addSelectFacilityRequest(int customInvokeTimeout, AlertingPattern alertingPattern,
			CalledPartyNumberIsup destinationNumberRoutingAddress,
			ISDNAccessRelatedInformation isdnAccessRelatedInformation, FacilityGroup calledFacilityGroup,
			Integer calledFacilityGroupMember, OriginalCalledNumberIsup originalCalledPartyID,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC)
			throw new INAPException(
					"Bad application context name for addSelectFacilityRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_generic_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SelectFacilityRequestImpl req = new SelectFacilityRequestImpl(alertingPattern, destinationNumberRoutingAddress,
				isdnAccessRelatedInformation, calledFacilityGroup, calledFacilityGroupMember, originalCalledPartyID,
				extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.selectFacility, req, true, false);
	}

	@Override
	public Integer addRequestCurrentStatusReportRequest(ResourceID resourceID) throws INAPException {
		return addRequestCurrentStatusReportRequest(_Timer_Default, resourceID);
	}

	@Override
	public Integer addRequestCurrentStatusReportRequest(int customInvokeTimeout, ResourceID resourceID)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException(
					"Bad application context name for addRequestCurrentStatusReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RequestCurrentStatusReportRequestImpl req = new RequestCurrentStatusReportRequestImpl(resourceID);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.requestCurrentStatusReport, req, true, false);
	}

	@Override
	public void addRequestCurrentStatusReportResponse(int invokeId, ResourceStatus resourceStatus,
			ResourceID resourceID, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException("Bad application context name for addRequestCurrentStatusReportResponse: must be "
					+ "Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, "
					+ "Q1218_DP_specific_SCF_to_SSF_AC or Q1218_SCF_to_SSF_status_reporting_AC");

		RequestCurrentStatusReportResponseImpl res = new RequestCurrentStatusReportResponseImpl(resourceStatus,
				resourceID, extensions);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.requestCurrentStatusReport, res,
				false, true);
	}

	@Override
	public Integer addCancelStatusReportRequest(ResourceID resourceID, CAPINAPExtensions extensions) throws INAPException {
		return addCancelStatusReportRequest(_Timer_Default, resourceID, extensions);
	}

	@Override
	public Integer addCancelStatusReportRequest(int customInvokeTimeout, ResourceID resourceID,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SRF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException(
					"Bad application context name for addCancelStatusReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_SRF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or "
							+ "Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CancelStatusReportRequestImpl req = new CancelStatusReportRequestImpl(resourceID, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.cancelStatusReportRequest, req, true, false);
	}

	@Override
	public Integer addRequestEveryStatusChangeReportRequest(ResourceID resourceID, DigitsIsup correlationID,
			Integer duration, CAPINAPExtensions extensions) throws INAPException {
		return addRequestEveryStatusChangeReportRequest(_Timer_Default, resourceID, correlationID, duration,
				extensions);
	}

	@Override
	public Integer addRequestEveryStatusChangeReportRequest(int customInvokeTimeout, ResourceID resourceID,
			DigitsIsup correlationID, Integer duration, CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException(
					"Bad application context name for addRequestEveryStatusChangeReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RequestEveryStatusChangeReportRequestImpl req = new RequestEveryStatusChangeReportRequestImpl(resourceID,
				correlationID, duration, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.requestEveryStatusChangeReport, req, true, false);
	}

	@Override
	public Integer addRequestFirstStatusMatchReportRequest(ResourceID resourceID, ResourceStatus resourceStatus,
			DigitsIsup correlationID, Integer duration, CAPINAPExtensions extensions, BearerCapability bearerCapability)
			throws INAPException {
		return addRequestFirstStatusMatchReportRequest(_Timer_Default, resourceID, resourceStatus, correlationID,
				duration, extensions, bearerCapability);
	}

	@Override
	public Integer addRequestFirstStatusMatchReportRequest(int customInvokeTimeout, ResourceID resourceID,
			ResourceStatus resourceStatus, DigitsIsup correlationID, Integer duration, CAPINAPExtensions extensions,
			BearerCapability bearerCapability) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException(
					"Bad application context name for addRequestFirstStatusMatchReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RequestFirstStatusMatchReportRequestImpl req = new RequestFirstStatusMatchReportRequestImpl(resourceID,
				resourceStatus, correlationID, duration, extensions, bearerCapability);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.requestFirstStatusMatchReport, req, true, false);
	}

	@Override
	public Integer addStatusReportRequest(ResourceStatus resourceStatus, DigitsIsup correlationID, ResourceID resourceID,
			CAPINAPExtensions extensions, ReportCondition reportCondition) throws INAPException {
		return addStatusReportRequest(_Timer_Default, resourceStatus, correlationID, resourceID, extensions,
				reportCondition);
	}

	@Override
	public Integer addStatusReportRequest(int customInvokeTimeout, ResourceStatus resourceStatus, DigitsIsup correlationID,
			ResourceID resourceID, CAPINAPExtensions extensions, ReportCondition reportCondition) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC)
			throw new INAPException(
					"Bad application context name for addStatusReportRequest: must be Q1218_generic_SSF_to_SCF_AC, Q1218_DP_specific_SSF_to_SCF_AC, "
							+ "Q1218_assist_handoff_SSF_to_SCF_AC, Q1218_generic_SCF_to_SSF_AC, Q1218_DP_specific_SCF_to_SSF_AC or Q1218_DP_specific_SCF_to_SSF_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		StatusReportRequestImpl req = new StatusReportRequestImpl(resourceStatus, correlationID, resourceID, extensions,
				reportCondition);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.statusReport, req, true, false);
	}

	@Override
	public Integer addUpdateRequest(ByteBuf operationID, ApplicationID applicationID, DataItemID dataItemID,
			DataItemInformation dataItemInformation) throws INAPException {
		return addUpdateRequest(_Timer_Default, operationID, applicationID, dataItemID, dataItemInformation);
	}

	@Override
	public Integer addUpdateRequest(int customInvokeTimeout, ByteBuf operationID, ApplicationID applicationID,
			DataItemID dataItemID, DataItemInformation dataItemInformation) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addUpdateRequest: must be Ericcson_cs1plus_data_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		UpdateRequestImpl req = new UpdateRequestImpl(operationID, applicationID, dataItemID, dataItemInformation);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.update, req, true, false);
	}

	@Override
	public void addUpdateResponse(int invokeId, ByteBuf operationReturnID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addUpdateResponse: must be Ericcson_cs1plus_data_management_AC");

		UpdateResponseImpl res = new UpdateResponseImpl(operationReturnID);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.update, res, false, true);
	}

	@Override
	public Integer addRetrieveRequest(ByteBuf operationID, ApplicationID applicationID, DataItemID dataItemID)
			throws INAPException {
		return addRetrieveRequest(_Timer_Default, operationID, applicationID, dataItemID);
	}

	@Override
	public Integer addRetrieveRequest(int customInvokeTimeout, ByteBuf operationID, ApplicationID applicationID,
			DataItemID dataItemID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addRetrieveRequest: must be Ericcson_cs1plus_data_management_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		RetrieveRequestImpl req = new RetrieveRequestImpl(operationID, applicationID, dataItemID);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.retrieve, req, true, false);
	}

	@Override
	public void addRetrieveResponse(int invokeId, ByteBuf operationReturnID, DataItemInformation dataItemInformation)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addUpdateResponse: must be Ericcson_cs1plus_data_management_AC");

		RetrieveResponseImpl res = new RetrieveResponseImpl(operationReturnID, dataItemInformation);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.retrieve, res, false, true);
	}

	@Override
	public Integer addSignallingInformationRequest(BackwardSuppressionIndicators backwardSuppressionIndicators,
			CalledPartyNumberIsup connectedNumber, ForwardSuppressionIndicators forwardSuppressionIndicators,
			BackwardGVNS backwardGVNS, CAPINAPExtensions extensions) throws INAPException {
		return addSignallingInformationRequest(_Timer_Default, backwardSuppressionIndicators, connectedNumber,
				forwardSuppressionIndicators, backwardGVNS, extensions);
	}

	@Override
	public Integer addSignallingInformationRequest(int customInvokeTimeout,
			BackwardSuppressionIndicators backwardSuppressionIndicators, CalledPartyNumberIsup connectedNumber,
			ForwardSuppressionIndicators forwardSuppressionIndicators, BackwardGVNS backwardGVNS,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addSignallingInformationRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_SCP_to_SSP_AC or"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		SignallingInformationRequestImpl req = new SignallingInformationRequestImpl(backwardSuppressionIndicators,
				connectedNumber, forwardSuppressionIndicators, backwardGVNS, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.signallingInformation, req, true, false);
	}

	@Override
	public Integer addReleaseCallPartyConnectionRequest(LegType legToBeReleased,Integer callID, CauseIsup releaseCause)
			throws INAPException {
		return addReleaseCallPartyConnectionRequest(_Timer_Default, legToBeReleased,callID,releaseCause);
	}

	@Override
	public Integer addReleaseCallPartyConnectionRequest(int customInvokeTimeout, LegType legToBeReleased,
			Integer callID,CauseIsup releaseCause) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addReleaseCallPartyConnectionRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ReleaseCallPartyConnectionRequestImpl req = new ReleaseCallPartyConnectionRequestImpl(legToBeReleased,
				callID,releaseCause);
		return this.sendDataComponent(null, null, InvokeClass.Class1, customTimeout.longValue(),
				INAPOperationCode.releaseCallPartyConnection, req, true, false);
	}
	
	@Override
	public void addReleaseCallPartyConnectionResponse(int invokeId, List<LegInformation> legInformation) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addReleaseCallPartyConnectionResponse: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		ReleaseCallPartyConnectionResponseImpl res = new ReleaseCallPartyConnectionResponseImpl(legInformation);
		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.releaseCallPartyConnection, res, false, true);
	}
	
	@Override
	public void addReleaseCallPartyConnectionResponse(int invokeId) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addReleaseCallPartyConnectionResponse: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		this.sendDataComponent(invokeId, null, null, null, INAPOperationCode.releaseCallPartyConnection, null, false, true);
	}
	
	@Override
	public Integer addReconnectRequest(LegType legID) throws INAPException {
		return addReconnectRequest(_Timer_Default, legID);
	}

	@Override
	public Integer addReconnectRequest(int customInvokeTimeout, LegType legID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addReconnectRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ReconnectRequestImpl req = new ReconnectRequestImpl(legID);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.reconnect, req, true, false);
	}

	@Override
	public Integer addHoldCallPartyConnectionRequest(LegType legID) throws INAPException {
		return addHoldCallPartyConnectionRequest(_Timer_Default, legID);
	}

	@Override
	public Integer addHoldCallPartyConnectionRequest(int customInvokeTimeout, LegType legID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addHoldCallPartyConnectionRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		HoldCallPartyConnectionRequestImpl req = new HoldCallPartyConnectionRequestImpl(legID);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.holdCallPartyConnection, req, true, false);
	}

	@Override
	public Integer addHandoverRequest(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, LocationNumberIsup locationNumber,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, TriggerType triggerType,
			HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation, LegIDs legIDs,
			RouteOrigin routeOrigin, boolean testIndication, CUGCallIndicator cugCallIndicator,
			CUGInterLockCode cugInterLockCode, GenericDigitsSet genericDigitsSet, GenericNumbersSet genericNumberSet,
			CauseIsup cause, HandOverInfo handOverInfo, ForwardGVNSIsup forwardGVNSIndicator,
			BackwardGVNS backwardGVNSIndicator) throws INAPException {
		return addHandoverRequest(serviceKey, serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory,
				cgEncountered, ipsspCapabilities, locationNumber, originalCalledPartyID, extensions, triggerType,
				highLayerCompatibility, serviceInteractionIndicators, additionalCallingPartyNumber,
				forwardCallIndicators, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation,
				legIDs, routeOrigin, testIndication, cugCallIndicator, cugInterLockCode, genericDigitsSet,
				genericNumberSet, cause, handOverInfo, forwardGVNSIndicator, backwardGVNSIndicator);
	}

	@Override
	public Integer addHandoverRequest(int customInvokeTimeout, int serviceKey, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities, LocationNumberIsup locationNumber,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions, TriggerType triggerType,
			HighLayerCompatibilityIsup highLayerCompatibility,
			ServiceInteractionIndicators serviceInteractionIndicators, DigitsIsup additionalCallingPartyNumber,
			ForwardCallIndicators forwardCallIndicators, BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM,
			RedirectingPartyIDIsup redirectingPartyID, RedirectionInformationIsup redirectionInformation, LegIDs legIDs,
			RouteOrigin routeOrigin, boolean testIndication, CUGCallIndicator cugCallIndicator,
			CUGInterLockCode cugInterLockCode, GenericDigitsSet genericDigitsSet, GenericNumbersSet genericNumberSet,
			CauseIsup cause, HandOverInfo handOverInfo, ForwardGVNSIsup forwardGVNSIndicator,
			BackwardGVNS backwardGVNSIndicator) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addHandoverRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlMedium();
		else
			customTimeout = customInvokeTimeout;

		HandOverRequestImpl req = new HandOverRequestImpl(serviceKey, calledPartyNumber, callingPartyNumber,
				callingPartysCategory, cgEncountered, ipsspCapabilities, locationNumber, originalCalledPartyID,
				extensions, triggerType, highLayerCompatibility, serviceInteractionIndicators,
				additionalCallingPartyNumber, forwardCallIndicators, bearerCapability, eventTypeBCSM,
				redirectingPartyID, redirectionInformation, legIDs, routeOrigin, testIndication, cugCallIndicator,
				cugInterLockCode, genericDigitsSet, genericNumberSet, cause, handOverInfo, forwardGVNSIndicator,
				backwardGVNSIndicator);

		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.handOver, req, true, false);
	}

	@Override
	public Integer addDialogueUserInformationRequest(SendingFunctionsActive sendingFunctionsActive,
			ReceivingFunctionsRequested receivingFunctionsRequested, Integer trafficSimulationSessionID)
			throws INAPException {
		return addDialogueUserInformationRequest(_Timer_Default, sendingFunctionsActive, receivingFunctionsRequested,
				trafficSimulationSessionID);
	}

	@Override
	public Integer addDialogueUserInformationRequest(int customInvokeTimeout,
			SendingFunctionsActive sendingFunctionsActive, ReceivingFunctionsRequested receivingFunctionsRequested,
			Integer trafficSimulationSessionID) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addDialogueUserInformationRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC, Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC, "
							+ "Ericcson_cs1plus_IP_to_SCP_AC, Ericcson_cs1plus_SCP_to_SSP_AC, Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC, Ericcson_cs1plus_SCP_to_SSP_service_management_AC, "
							+ "Ericcson_cs1plus_SSP_to_SCP_service_management_AC, Ericcson_cs1plus_data_management_AC, or Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		DialogueUserInformationRequestImpl req = new DialogueUserInformationRequestImpl(sendingFunctionsActive,
				receivingFunctionsRequested, trafficSimulationSessionID);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.dialogueUserInformation, req, true, false);
	}

	@Override
	public Integer addCallLimitRequest(DateAndTime startTime, GapCriteria limitCriteria, LimitIndicators limitIndicators,
			GapTreatment limitTreatment) throws INAPException {
		return addCallLimitRequest(_Timer_Default, startTime, limitCriteria, limitIndicators, limitTreatment);
	}

	@Override
	public Integer addCallLimitRequest(int customInvokeTimeout, DateAndTime startTime, GapCriteria limitCriteria,
			LimitIndicators limitIndicators, GapTreatment limitTreatment) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addCallLimitRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		CallLimitRequestImpl req = new CallLimitRequestImpl(startTime, limitCriteria, limitIndicators, limitTreatment);
		return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(),
				INAPOperationCode.callLimit, req, true, false);
	}

	@Override
	public Integer addContinueWithArgumentRequest(LegType legID, GenericName genericName) throws INAPException {
		return addContinueWithArgumentRequest(_Timer_Default, legID, genericName);
	}

	@Override
	public Integer addContinueWithArgumentRequest(int customInvokeTimeout, LegType legID, GenericName genericName)
			throws INAPException {
		if (this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addContinueWithArgumentRequest: must be Ericcson_cs1plus_SSP_TO_SCP_AC or Ericcson_cs1plus_SCP_to_SSP_AC");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ContinueWithArgumentRequestImpl req = new ContinueWithArgumentRequestImpl(legID, genericName);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.continueWithArgument, req, true, false);
	}

	@Override
	public Integer addResetTimerRequest(TimerID timerID, int timerValue, CAPINAPExtensions extensions)
			throws INAPException {
		return addResetTimerRequest(_Timer_Default, timerID, timerValue, extensions);
	}

	@Override
	public Integer addResetTimerRequest(int customInvokeTimeout, TimerID timerID, int timerValue,
			CAPINAPExtensions extensions) throws INAPException {
		if (this.appCntx != INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
				&& this.appCntx != INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B)
			throw new INAPException(
					"Bad application context name for addResetTimerRequest: must be Q1218_generic_SSF_to_SCF_AC,Q1218_DP_specific_SSF_to_SCF_AC"
							+ "Q1218_assist_handoff_SSF_to_SCF_AC,Q1218_generic_SCF_to_SSF_AC,Q1218_DP_specific_SCF_to_SSF_AC,Core_INAP_CS1_SSP_to_SCP_AC"
							+ "Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC,Core_INAP_CS1_IP_to_SCP_AC,Core_INAP_CS1_SCP_to_SSP_AC,Ericcson_cs1plus_SSP_TO_SCP_AC"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC,Ericcson_cs1plus_IP_to_SCP_AC,Ericcson_cs1plus_SCP_to_SSP_AC, Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B"
							+ "Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B,Ericcson_cs1plus_IP_to_SCP_AC_REV_B or Ericcson_cs1plus_SCP_to_SSP_AC_REV_B");

		Integer customTimeout;
		if (customInvokeTimeout == _Timer_Default)
			customTimeout = getTimerCircuitSwitchedCallControlShort();
		else
			customTimeout = customInvokeTimeout;

		ResetTimerRequestImpl req = new ResetTimerRequestImpl(timerID, timerValue, extensions);
		return this.sendDataComponent(null, null, InvokeClass.Class2, customTimeout.longValue(),
				INAPOperationCode.resetTimer, req, true, false);
	}
}