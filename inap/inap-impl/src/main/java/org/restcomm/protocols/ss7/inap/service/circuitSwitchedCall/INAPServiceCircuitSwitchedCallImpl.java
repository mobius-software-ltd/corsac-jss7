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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.inap.INAPDialogImpl;
import org.restcomm.protocols.ss7.inap.INAPProviderImpl;
import org.restcomm.protocols.ss7.inap.INAPServiceBaseImpl;
import org.restcomm.protocols.ss7.inap.api.INAPApplicationContext;
import org.restcomm.protocols.ss7.inap.api.INAPDialog;
import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPMessage;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentException;
import org.restcomm.protocols.ss7.inap.api.INAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.inap.api.INAPServiceListener;
import org.restcomm.protocols.ss7.inap.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.inap.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivateServiceFilteringRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalyseInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AnalysedInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallLimitRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CollectedInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DialogueUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DisconnectForwardConnectionCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventNotificationChargingRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HandOverRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HoldCallInNetworkRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HoldCallPartyConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.INAPServiceCircuitSwitchedCallListener;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OCalledPartyBusyRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ODisconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OMidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ONoAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OriginationAttemptAuthorizedRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestEveryStatusChangeReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestFirstStatusMatchReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestNotificationChargingEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RetrieveResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RouteSelectFailureRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectFacilityRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectRouteRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ServiceFilteringResponseRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SignallingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportCS1PlusRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.StatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TBusyRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TDisconnectRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TMidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TNoAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TermAttemptAuthorizedRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.UpdateResponse;
import org.restcomm.protocols.ss7.inap.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author yulian.oifa
 *
 */
public class INAPServiceCircuitSwitchedCallImpl extends INAPServiceBaseImpl implements INAPServiceCircuitSwitchedCall {
	public static String NAME="CircuitSwitchedCall";
	
	protected Logger loger = LogManager.getLogger(INAPServiceCircuitSwitchedCallImpl.class);

	public INAPServiceCircuitSwitchedCallImpl(INAPProviderImpl inapProviderImpl) {
		super(inapProviderImpl);
	}

	@Override
	public INAPDialogCircuitSwitchedCall createNewDialog(INAPApplicationContext appCntx, SccpAddress origAddress,
			SccpAddress destAddress) throws INAPException {
		inapProviderImpl.getStack().newDialogSent(NAME);
		return this.createNewDialog(appCntx, origAddress, destAddress, null);
	}

	@Override
	public INAPDialogCircuitSwitchedCall createNewDialog(INAPApplicationContext appCntx, SccpAddress origAddress,
			SccpAddress destAddress, Long localTrId) throws INAPException {

		// We cannot create a dialog if the service is not activated
		if (!this.isActivated())
			throw new INAPException(
					"Cannot create INAPDialogCircuitSwitchedCall because INAPServiceCircuitSwitchedCall is not activated");

		Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
		INAPDialogCircuitSwitchedCallImpl dialog = new INAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog,
				this.inapProviderImpl, this);

		this.putINAPDialogIntoCollection(dialog);

		return dialog;
	}

	@Override
	public void addINAPServiceListener(INAPServiceCircuitSwitchedCallListener inapServiceListener) {
		super.addINAPServiceListener(inapServiceListener);
	}

	@Override
	public void removeINAPServiceListener(INAPServiceCircuitSwitchedCallListener inapServiceListener) {
		super.removeINAPServiceListener(inapServiceListener);
	}

	@Override
	protected INAPDialogImpl createNewDialogIncoming(INAPApplicationContext appCntx, Dialog tcapDialog) {
		inapProviderImpl.getStack().newDialogReceived(NAME);
		return new INAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog, this.inapProviderImpl, this);
	}

	@Override
	public ServingCheckData isServingService(INAPApplicationContext dialogApplicationContext) {

		switch (dialogApplicationContext) {
		case Core_INAP_CS1_IP_to_SCP_AC:
		case Core_INAP_CS1_SCP_to_SSP_AC:
		case Core_INAP_CS1_SCP_to_SSP_service_management_AC:
		case Core_INAP_CS1_SCP_to_SSP_traffic_management_AC:
		case Core_INAP_CS1_SSP_to_SCP_AC:
		case Core_INAP_CS1_SSP_to_SCP_service_management_AC:
		case Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC:
		case Ericcson_cs1plus_IP_to_SCP_AC:
		case Ericcson_cs1plus_SCP_to_SSP_AC:
		case Ericcson_cs1plus_SCP_to_SSP_service_management_AC:
		case Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC:
		case Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC:
		case Ericcson_cs1plus_SSP_TO_SCP_AC:
		case Ericcson_cs1plus_SSP_to_SCP_service_management_AC:
		case Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC:
		case Ericcson_cs1plus_data_management_AC:
		case Ericcson_cs1plus_IP_to_SCP_AC_REV_B:
		case Ericcson_cs1plus_SCP_to_SSP_AC_REV_B:
		case Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B:
		case Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B:
		case Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B:
		case Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B:
		case Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B:
		case Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B:
		case Ericcson_cs1plus_data_management_AC_REV_B:
		case Q1218_DP_specific_SCF_to_SSF_AC:
		case Q1218_DP_specific_SSF_to_SCF_AC:
		case Q1218_SCF_to_SSF_service_management_AC:
		case Q1218_SCF_to_SSF_status_reporting_AC:
		case Q1218_SCF_to_SSF_traffic_management_AC:
		case Q1218_SRF_to_SCF_AC:
		case Q1218_SSF_to_SCF_service_management_AC:
		case Q1218_assist_handoff_SSF_to_SCF_AC:
		case Q1218_generic_SCF_to_SSF_AC:
		case Q1218_generic_SSF_to_SCF_AC:
			return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
		default:
			break;
		}

		return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
	}

	public long[] getLinkedOperationList(long operCode) {
		if (operCode == INAPOperationCode.playAnnouncement
				|| operCode == INAPOperationCode.promptAndCollectUserInformation) {
			return new long[] { INAPOperationCode.specializedResourceReport };
		}

		return null;
	}

	@Override
	public void processComponent(ComponentType compType, OperationCode oc, INAPMessage parameter, INAPDialog inapDialog,
			Integer invokeId, Integer linkedId) throws INAPParsingComponentException {

		Integer ocValue = oc.getLocalOperationCode();
		if (ocValue == null)
			new INAPParsingComponentException("", INAPParsingComponentExceptionReason.UnrecognizedOperation);
		INAPApplicationContext acn = inapDialog.getApplicationContext();
		int ocValueInt = (int) (long) ocValue;
		boolean processed = false;

		switch (ocValueInt) {
		case INAPOperationCode.initialDP:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B) {
				if (parameter instanceof InitialDPRequest) {
					processed = true;
					InitialDPRequest ind = (InitialDPRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onInitialDPRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing initialDPRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.originationAttemptAuthorized:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof OriginationAttemptAuthorizedRequest) {
					processed = true;
					OriginationAttemptAuthorizedRequest ind = (OriginationAttemptAuthorizedRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis)
									.onOriginationAttemptAuthorizedRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing originationAttemptAuthorizedRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.collectedInformation:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof CollectedInformationRequest) {
					processed = true;
					CollectedInformationRequest ind = (CollectedInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCollectedInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing collectedInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.analysedInformation:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof AnalysedInformationRequest) {
					processed = true;
					AnalysedInformationRequest ind = (AnalysedInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onAnalysedInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing analysedInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.routeSelectFailure:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof RouteSelectFailureRequest) {
					processed = true;
					RouteSelectFailureRequest ind = (RouteSelectFailureRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onRouteSelectFailureRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing routeSelectFailureRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.oCalledPartyBusy:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof OCalledPartyBusyRequest) {
					processed = true;
					OCalledPartyBusyRequest ind = (OCalledPartyBusyRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onOCalledPartyBusyRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing oCalledPartyBusyRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.oNoAnswer:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof ONoAnswerRequest) {
					processed = true;
					ONoAnswerRequest ind = (ONoAnswerRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onONoAnswerRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing oNoAnswerRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.oAnswer:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof OAnswerRequest) {
					processed = true;
					OAnswerRequest ind = (OAnswerRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onOAnswerRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing oAnswerRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.oDisconnect:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof ODisconnectRequest) {
					processed = true;
					ODisconnectRequest ind = (ODisconnectRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onODisconnectRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing oDisconnectRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.termAttemptAuthorized:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TermAttemptAuthorizedRequest) {
					processed = true;
					TermAttemptAuthorizedRequest ind = (TermAttemptAuthorizedRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTermAttemptAuthorizedRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing termAttemptAuthorizedRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.tBusy:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TBusyRequest) {
					processed = true;
					TBusyRequest ind = (TBusyRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTBusyRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing tBusyRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.tNoAnswer:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TNoAnswerRequest) {
					processed = true;
					TNoAnswerRequest ind = (TNoAnswerRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTNoAnswerRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing tNoAnswerRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.tAnswer:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TAnswerRequest) {
					processed = true;
					TAnswerRequest ind = (TAnswerRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTAnswerRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing tAnswerRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.tDisconnect:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TDisconnectRequest) {
					processed = true;
					TDisconnectRequest ind = (TDisconnectRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTDisconnectRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing tDisconnectRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.oMidCall:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof OMidCallRequest) {
					processed = true;
					OMidCallRequest ind = (OMidCallRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onOMidCallRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing oMidCallRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.tMidCall:
			if (acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof TMidCallRequest) {
					processed = true;
					TMidCallRequest ind = (TMidCallRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onTMidCallRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing tMidCallRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.assistRequestInstructions:
			// also retreive command, which has same id
			// case INAPOperationCode.retrieve:
			if (acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B) {
				if (parameter instanceof AssistRequestInstructionsRequest) {
					processed = true;
					AssistRequestInstructionsRequest ind = (AssistRequestInstructionsRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onAssistRequestInstructionsRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing assistRequestInstructionsRequest: " + e.getMessage(), e);
						}
					}
				}
			} else if (acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B) {
				if (compType == ComponentType.Invoke) {
					if (parameter instanceof RetrieveRequest) {
						processed = true;
						RetrieveRequest ind = (RetrieveRequest) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onRetrieveRequest(ind);
							} catch (Exception e) {
								loger.error("Error processing retrieveRequest: " + e.getMessage(), e);
							}
						}
					}
				} else if (compType == ComponentType.ReturnResultLast) {
					if (parameter instanceof RetrieveResponse) {
						processed = true;
						RetrieveResponse ind = (RetrieveResponse) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onRetrieveResponse(ind);
							} catch (Exception e) {
								loger.error("Error processing retrieveResponse: " + e.getMessage(), e);
							}
						}
					}
				}
			}
			break;
		case INAPOperationCode.establishTemporaryConnection:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof EstablishTemporaryConnectionRequest) {
					processed = true;
					EstablishTemporaryConnectionRequest ind = (EstablishTemporaryConnectionRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis)
									.onEstablishTemporaryConnectionRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing establishTemporaryConnectionRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.disconnectForwardConnection:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter == null) {
					processed = true;
					DisconnectForwardConnectionRequest ind = new DisconnectForwardConnectionRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing disconnectForwardConnectionRequest: " + e.getMessage(), e);
						}
					}
				}
			} else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof DisconnectForwardConnectionCS1PlusRequest) {
					processed = true;
					DisconnectForwardConnectionCS1PlusRequest ind = (DisconnectForwardConnectionCS1PlusRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing disconnectForwardConnectionRequest: " + e.getMessage(), e);
						}
					}
				} else if (parameter == null) {
					processed = true;
					DisconnectForwardConnectionRequest ind = new DisconnectForwardConnectionRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing disconnectForwardConnectionRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.connectToResource:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ConnectToResourceRequest) {
					processed = true;
					ConnectToResourceRequest ind = (ConnectToResourceRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onConnectToResourceRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing connectToResourceRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.connect:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ConnectRequest) {
					processed = true;
					ConnectRequest ind = (ConnectRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onConnectRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing connectRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.holdCallInNetwork:
			// also update command which has same id
			// case INAPOperationCode.update:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof HoldCallInNetworkRequest) {
					processed = true;
					HoldCallInNetworkRequest ind = (HoldCallInNetworkRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onHoldCallInNetworkRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing holdCallInNetworkRequest: " + e.getMessage(), e);
						}
					}
				}
			} else if (acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B) {
				if (compType == ComponentType.Invoke) {
					if (parameter instanceof UpdateRequest) {
						processed = true;
						UpdateRequest ind = (UpdateRequest) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onUpdateRequest(ind);
							} catch (Exception e) {
								loger.error("Error processing updateRequest: " + e.getMessage(), e);
							}
						}
					}
				} else if (compType == ComponentType.ReturnResultLast) {
					if (parameter instanceof UpdateResponse) {
						processed = true;
						UpdateResponse ind = (UpdateResponse) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onUpdateResponse(ind);
							} catch (Exception e) {
								loger.error("Error processing updateResponse: " + e.getMessage(), e);
							}
						}
					}
				}
			}
			break;
		case INAPOperationCode.releaseCall:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ReleaseCallRequest) {
					processed = true;
					ReleaseCallRequest ind = (ReleaseCallRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing releaseCallRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.requestReportBCSMEvent:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof RequestReportBCSMEventRequest) {
					processed = true;
					RequestReportBCSMEventRequest ind = (RequestReportBCSMEventRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onRequestReportBCSMEventRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing requestReportBCSMEventRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.eventReportBCSM:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof EventReportBCSMRequest) {
					processed = true;
					EventReportBCSMRequest ind = (EventReportBCSMRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onEventReportBCSMRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing eventReportBCSMRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.requestNotificationChargingEvent:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof RequestNotificationChargingEventRequest) {
					processed = true;
					RequestNotificationChargingEventRequest ind = (RequestNotificationChargingEventRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis)
									.onRequestNotificationChargingEventRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing requestNotificationChargingEventRequest: " + e.getMessage(),
									e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.eventNotificationCharging:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof EventNotificationChargingRequest) {
					processed = true;
					EventNotificationChargingRequest ind = (EventNotificationChargingRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onEventNotificationChargingRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing eventNotificationChargingRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.collectInformation:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof CollectInformationRequest) {
					processed = true;
					CollectInformationRequest ind = (CollectInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCollectInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing collectInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.analyseInformation:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof AnalyseInformationRequest) {
					processed = true;
					AnalyseInformationRequest ind = (AnalyseInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onAnalyseInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing analyseInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.selectRoute:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof SelectRouteRequest) {
					processed = true;
					SelectRouteRequest ind = (SelectRouteRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSelectRouteRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing selectRouteRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.selectFacility:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC) {
				if (parameter instanceof SelectFacilityRequest) {
					processed = true;
					SelectFacilityRequest ind = (SelectFacilityRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSelectFacilityRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing selectFacilityRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.continueCode:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter == null) {
					processed = true;
					ContinueRequest ind = new ContinueRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onContinueRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing continueRequest: " + e.getMessage(), e);
						}
					}
				}
			} else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter == null) {
					processed = true;
					ContinueCS1PlusRequest ind = new ContinueCS1PlusRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onContinueRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing continueRequest: " + e.getMessage(), e);
						}
					}
				} else if (parameter instanceof ContinueCS1PlusRequest) {
					processed = true;
					ContinueCS1PlusRequest ind = (ContinueCS1PlusRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onContinueRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing continueRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.initiateCallAttempt:
			if (acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof InitiateCallAttemptRequest) {
					processed = true;
					InitiateCallAttemptRequest ind = (InitiateCallAttemptRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onInitiateCallAttemptRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing initiateCallAttemptRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.resetTimer:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ResetTimerRequest) {
					processed = true;
					ResetTimerRequest ind = (ResetTimerRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onResetTimerRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing resetTimerRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.furnishChargingInformation:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof FurnishChargingInformationRequest) {
					processed = true;
					FurnishChargingInformationRequest ind = (FurnishChargingInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onFurnishChargingInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing furnishChargingInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.applyCharging:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter instanceof ApplyChargingRequest) {
					processed = true;
					ApplyChargingRequest ind = (ApplyChargingRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing applyChargingRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ApplyChargingRequest) {
					processed = true;
					ApplyChargingRequest ind = (ApplyChargingRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing applyChargingRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.applyChargingReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter instanceof ApplyChargingReportRequest) {
					processed = true;
					ApplyChargingReportRequest ind = (ApplyChargingReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing applyChargingReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ApplyChargingReportRequest) {
					processed = true;
					ApplyChargingReportRequest ind = (ApplyChargingReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing applyChargingReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.requestCurrentStatusReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC) {
				if (compType == ComponentType.Invoke) {
					if (parameter instanceof RequestCurrentStatusReportRequest) {
						processed = true;
						RequestCurrentStatusReportRequest ind = (RequestCurrentStatusReportRequest) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis)
										.onRequestCurrentStatusReportRequest(ind);
							} catch (Exception e) {
								loger.error("Error processing requestCurrentStatusReportRequest: " + e.getMessage(), e);
							}
						}
					}
				} else if (compType == ComponentType.ReturnResultLast) {
					if (parameter instanceof RequestCurrentStatusReportResponse) {
						processed = true;
						RequestCurrentStatusReportResponse ind = (RequestCurrentStatusReportResponse) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis)
										.onRequestCurrentStatusReportResponse(ind);
							} catch (Exception e) {
								loger.error("Error processing requestCurrentStatusReportResponse: " + e.getMessage(),
										e);
							}
						}
					}
				}
			}
			break;
		case INAPOperationCode.requestEveryStatusChangeReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC) {
				if (parameter instanceof RequestEveryStatusChangeReportRequest) {
					processed = true;
					RequestEveryStatusChangeReportRequest ind = (RequestEveryStatusChangeReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis)
									.onRequestEveryStatusChangeReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing requestEveryStatusChangeReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.requestFirstStatusMatchReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC) {
				if (parameter instanceof RequestFirstStatusMatchReportRequest) {
					processed = true;
					RequestFirstStatusMatchReportRequest ind = (RequestFirstStatusMatchReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis)
									.onRequestFirstStatusMatchReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing requestFirstStatusMatchReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.statusReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC) {
				if (parameter instanceof StatusReportRequest) {
					processed = true;
					StatusReportRequest ind = (StatusReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onStatusReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing statusReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.callGap:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_traffic_management_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_traffic_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B) {
				if (parameter instanceof CallGapRequest) {
					processed = true;
					CallGapRequest ind = (CallGapRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCallGapRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing callGapRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.activateServiceFiltering:
			if (acn == INAPApplicationContext.Q1218_SCF_to_SSF_service_management_AC
					|| acn == INAPApplicationContext.Q1218_SSF_to_SCF_service_management_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_service_management_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B) {
				if (parameter instanceof ActivateServiceFilteringRequest) {
					processed = true;
					ActivateServiceFilteringRequest ind = (ActivateServiceFilteringRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onActivateServiceFilteringRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing activateServiceFilteringRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.serviceFilteringResponse:
			if (acn == INAPApplicationContext.Q1218_SCF_to_SSF_service_management_AC
					|| acn == INAPApplicationContext.Q1218_SSF_to_SCF_service_management_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_service_management_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B) {
				if (parameter instanceof ServiceFilteringResponseRequest) {
					processed = true;
					ServiceFilteringResponseRequest ind = (ServiceFilteringResponseRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onServiceFilteringResponseRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing serviceFilteringResponseRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.callInformationReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof CallInformationReportRequest) {
					processed = true;
					CallInformationReportRequest ind = (CallInformationReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCallInformationReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing callInformationReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.callInformationRequest:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof CallInformationRequest) {
					processed = true;
					CallInformationRequest ind = (CallInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCallInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing callInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.sendChargingInformation:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter instanceof SendChargingInformationRequest) {
					processed = true;
					SendChargingInformationRequest ind = (SendChargingInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSendChargingInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing sendChargingInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof SendChargingInformationCS1RequestImpl) {
					processed = true;
					SendChargingInformationRequest ind = (SendChargingInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSendChargingInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing sendChargingInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.playAnnouncement:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof PlayAnnouncementRequest) {
					processed = true;
					PlayAnnouncementRequest ind = (PlayAnnouncementRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onPlayAnnouncementRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing playAnnouncementRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.promptAndCollectUserInformation:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (compType == ComponentType.Invoke) {
					if (parameter instanceof PromptAndCollectUserInformationRequest) {
						processed = true;
						PromptAndCollectUserInformationRequest ind = (PromptAndCollectUserInformationRequest) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis)
										.onPromptAndCollectUserInformationRequest(ind);
							} catch (Exception e) {
								loger.error(
										"Error processing promptAndCollectUserInformationRequest: " + e.getMessage(),
										e);
							}
						}
					}
				} else if (compType == ComponentType.ReturnResultLast) {
					if (parameter instanceof PromptAndCollectUserInformationResponse) {
						processed = true;
						PromptAndCollectUserInformationResponse ind = (PromptAndCollectUserInformationResponse) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis)
										.onPromptAndCollectUserInformationResponse(ind);
							} catch (Exception e) {
								loger.error(
										"Error processing promptAndCollectUserInformationResponse: " + e.getMessage(),
										e);
							}
						}
					}
				}
			}
			break;
		case INAPOperationCode.specializedResourceReport:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC) {
				if (parameter == null) {
					processed = true;
					SpecializedResourceReportRequest ind = new SpecializedResourceReportRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSpecializedResourceReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing specializedResourceReportRequest: " + e.getMessage(), e);
						}
					}
				}
			} else if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof SpecializedResourceReportCS1PlusRequest) {
					processed = true;
					SpecializedResourceReportCS1PlusRequest ind = (SpecializedResourceReportCS1PlusRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSpecializedResourceReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing specializedResourceReportRequest: " + e.getMessage(), e);
						}
					}
				} else if (parameter == null) {
					processed = true;
					SpecializedResourceReportRequest ind = new SpecializedResourceReportRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSpecializedResourceReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing specializedResourceReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.cancelCode:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof CancelRequest) {
					processed = true;
					CancelRequest ind = (CancelRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCancelRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing cancelRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.cancelStatusReportRequest:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_SRF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_SCF_to_SSF_status_reporting_AC) {
				if (parameter instanceof CancelStatusReportRequest) {
					processed = true;
					CancelStatusReportRequest ind = (CancelStatusReportRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCancelStatusReportRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing cancelStatusReportRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.activityTest:
			if (acn == INAPApplicationContext.Q1218_generic_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_assist_handoff_SSF_to_SCF_AC
					|| acn == INAPApplicationContext.Q1218_generic_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Q1218_DP_specific_SCF_to_SSF_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Core_INAP_CS1_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (compType == ComponentType.Invoke && parameter == null) {
					processed = true;
					ActivityTestRequest ind = new ActivityTestRequestImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onActivityTestRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing activityTestRequest: " + e.getMessage(), e);
						}
					}
				} else if (compType == ComponentType.ReturnResultLast && parameter == null) {
					processed = true;
					ActivityTestResponse ind = new ActivityTestResponseImpl();
					ind.setInvokeId(invokeId);
					ind.setINAPDialog(inapDialog);
					inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
					
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onActivityTestResponse(ind);
						} catch (Exception e) {
							loger.error("Error processing activityTestResponse: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.callLimit:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B) {
				if (parameter instanceof CallLimitRequest) {
					processed = true;
					CallLimitRequest ind = (CallLimitRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onCallLimitRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing callLimitRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.continueWithArgument:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ContinueWithArgumentRequest) {
					processed = true;
					ContinueWithArgumentRequest ind = (ContinueWithArgumentRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onContinueWithArgumentRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing continueWithArgumentRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.dialogueUserInformation:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_IP_to_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_service_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_to_SCP_service_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_data_management_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_traffic_limitation_AC_REV_B) {
				if (parameter instanceof DialogueUserInformationRequest) {
					processed = true;
					DialogueUserInformationRequest ind = (DialogueUserInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onDialogueUserInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing dialogueUserInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.handOver:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof HandOverRequest) {
					processed = true;
					HandOverRequest ind = (HandOverRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onHandOverRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing handOverRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.holdCallPartyConnection:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof HoldCallPartyConnectionRequest) {
					processed = true;
					HoldCallPartyConnectionRequest ind = (HoldCallPartyConnectionRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onHoldCallPartyConnectionRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing holdCallPartyConnectionRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.reconnect:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (parameter instanceof ReconnectRequest) {
					processed = true;
					ReconnectRequest ind = (ReconnectRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onReconnectRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing reconnectRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		case INAPOperationCode.releaseCallPartyConnection:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B) {
				if (compType == ComponentType.Invoke && parameter == null) {
					if (parameter instanceof ReleaseCallPartyConnectionRequest) {
						processed = true;
						ReleaseCallPartyConnectionRequest ind = (ReleaseCallPartyConnectionRequest) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallPartyConnectionRequest(ind);
							} catch (Exception e) {
								loger.error("Error processing releaseCallPartyConnectionRequest: " + e.getMessage(), e);
							}
						}
					}
				} else if (compType == ComponentType.ReturnResultLast) {
					if(parameter==null) {
						processed = true;
						ReleaseCallPartyConnectionResponse ind = new ReleaseCallPartyConnectionParameterlessResponseImpl();
						ind.setInvokeId(invokeId);
						ind.setINAPDialog(inapDialog);
						inapProviderImpl.getStack().newMessageReceived(ind.getMessageType().name());
						
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallPartyConnectionResponse(ind);
							} catch (Exception e) {
								loger.error("Error processing ReleaseCallPartyConnectionResponse: " + e.getMessage(), e);
							}
						}
					}
					else if (parameter instanceof ReleaseCallPartyConnectionResponse) {
						processed = true;
						ReleaseCallPartyConnectionResponse ind = (ReleaseCallPartyConnectionResponse) parameter;
						for (INAPServiceListener serLis : this.serviceListeners) {
							try {
								serLis.onINAPMessage(ind);
								((INAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallPartyConnectionResponse(ind);
							} catch (Exception e) {
								loger.error("Error processing ReleaseCallPartyConnectionResponse: " + e.getMessage(), e);
							}
						}
					}
				}								
			}
			break;
		case INAPOperationCode.signallingInformation:
			if (acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SSP_TO_SCP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_SCP_to_SSP_AC_REV_B
					|| acn == INAPApplicationContext.Ericcson_cs1plus_assist_handoff_SSP_to_SCP_AC_REV_B) {
				if (parameter instanceof SignallingInformationRequest) {
					processed = true;
					SignallingInformationRequest ind = (SignallingInformationRequest) parameter;
					for (INAPServiceListener serLis : this.serviceListeners) {
						try {
							serLis.onINAPMessage(ind);
							((INAPServiceCircuitSwitchedCallListener) serLis).onSignallingInformationRequest(ind);
						} catch (Exception e) {
							loger.error("Error processing signallingInformationRequest: " + e.getMessage(), e);
						}
					}
				}
			}
			break;
		}

		if (!processed) {
			if (parameter == null)
				throw new INAPParsingComponentException("Error while decoding , Parameter is mandatory but not found",
						INAPParsingComponentExceptionReason.MistypedParameter);

			throw new INAPParsingComponentException("", INAPParsingComponentExceptionReason.UnrecognizedOperation);
		}
	}
}