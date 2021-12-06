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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.cap.CAPDialogImpl;
import org.restcomm.protocols.ss7.cap.CAPProviderImpl;
import org.restcomm.protocols.ss7.cap.CAPServiceBaseImpl;
import org.restcomm.protocols.ss7.cap.api.CAPApplicationContext;
import org.restcomm.protocols.ss7.cap.api.CAPDialog;
import org.restcomm.protocols.ss7.cap.api.CAPException;
import org.restcomm.protocols.ss7.cap.api.CAPMessage;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentException;
import org.restcomm.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.cap.api.CAPServiceListener;
import org.restcomm.protocols.ss7.cap.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.cap.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ActivityTestResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ApplyChargingRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.AssistRequestInstructionsRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPDialogCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCall;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CAPServiceCircuitSwitchedCallListener;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallGapRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CollectInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ContinueWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectForwardConnectionWithArgumentRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.DisconnectLegResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EstablishTemporaryConnectionRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitiateCallAttemptResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.MoveLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PlayAnnouncementRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.PromptAndCollectUserInformationResponse;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ReleaseCallRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.RequestReportBCSMEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SpecializedResourceReportRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SplitLegResponse;
import org.restcomm.protocols.ss7.cap.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPServiceCircuitSwitchedCallImpl extends CAPServiceBaseImpl implements CAPServiceCircuitSwitchedCall {

    protected Logger loger = Logger.getLogger(CAPServiceCircuitSwitchedCallImpl.class);

    public CAPServiceCircuitSwitchedCallImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogCircuitSwitchedCall createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long localTrId)
            throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException(
                    "Cannot create CAPDialogCircuitSwitchedCall because CAPServiceCircuitSwitchedCall is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogCircuitSwitchedCallImpl dialog = new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog,
                this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceCircuitSwitchedCallListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogCircuitSwitchedCallImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {

        switch (dialogApplicationContext) {
            case CapV1_gsmSSF_to_gsmSCF:
            case CapV2_gsmSSF_to_gsmSCF:
            case CapV2_assistGsmSSF_to_gsmSCF:
            case CapV2_gsmSRF_to_gsmSCF:
            case CapV3_gsmSSF_scfGeneric:
            case CapV3_gsmSSF_scfAssistHandoff:
            case CapV3_gsmSRF_gsmSCF:
            case CapV4_gsmSSF_scfGeneric:
            case CapV4_gsmSSF_scfAssistHandoff:
            case CapV4_scf_gsmSSFGeneric:
            case CapV4_gsmSRF_gsmSCF:
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			default:
				break;
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    public long[] getLinkedOperationList(long operCode) {
        if (operCode == CAPOperationCode.playAnnouncement || operCode == CAPOperationCode.promptAndCollectUserInformation) {
            return new long[] { CAPOperationCode.specializedResourceReport };
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, CAPMessage parameter, CAPDialog capDialog,
            Long invokeId, Long linkedId) throws CAPParsingComponentException {

    	Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        CAPApplicationContext acn = capDialog.getApplicationContext();
        int ocValueInt = (int) (long) ocValue;
        boolean processed = false;
        
        switch (ocValueInt) {
            case CAPOperationCode.initialDP:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
                	if(parameter instanceof InitialDPRequest) {
        				processed = true;
        				InitialDPRequest ind = (InitialDPRequest)parameter;
        	        	if(ind.getInitialDPArgExtension()!=null && ind.getInitialDPArgExtension() instanceof InitialDPArgExtensionImpl) {
        	        		int version=2;
        	        		if (acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric)
        	        			version=4;
        	        		
        	        		((InitialDPArgExtensionImpl)ind.getInitialDPArgExtension()).patchVersion(version);
        	        	}
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitialDPRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.requestReportBCSMEvent:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof RequestReportBCSMEventRequest) {
        				processed = true;
        				RequestReportBCSMEventRequest ind = (RequestReportBCSMEventRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onRequestReportBCSMEventRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.applyCharging:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ApplyChargingRequest) {
        				processed = true;
        				ApplyChargingRequest ind = (ApplyChargingRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.eventReportBCSM:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof EventReportBCSMRequest) {
        				processed = true;
        				EventReportBCSMRequest ind = (EventReportBCSMRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onEventReportBCSMRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.continueCode:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                    if (compType == ComponentType.Invoke && parameter == null) {
                    	processed = true;
                    	ContinueRequestImpl ind = new ContinueRequestImpl();
                    	ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onContinueRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                    }
                }
                break;

            case CAPOperationCode.continueWithArgument:
                if (acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ContinueWithArgumentRequest) {
        				processed = true;
        				ContinueWithArgumentRequest ind = (ContinueWithArgumentRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onContinueWithArgumentRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.applyChargingReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ApplyChargingReportRequest) {
        				processed = true;
        				ApplyChargingReportRequest ind = (ApplyChargingReportRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onApplyChargingReportRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.releaseCall:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ReleaseCallRequest) {
        				processed = true;
        				ReleaseCallRequest ind = (ReleaseCallRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onReleaseCallRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.connect:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ConnectRequest) {
        				processed = true;
        				ConnectRequest ind = (ConnectRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onConnectRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.callGap:
                if (acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
                	if(parameter instanceof CallGapRequest) {
        				processed = true;
        				CallGapRequest ind = (CallGapRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onCallGapRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.callInformationRequest:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof CallInformationRequestRequest) {
        				processed = true;
        				CallInformationRequestRequest ind = (CallInformationRequestRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onCallInformationRequestRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.callInformationReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof CallInformationReportRequest) {
        				processed = true;
        				CallInformationReportRequest ind = (CallInformationReportRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onCallInformationReportRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.activityTest:
                if (acn == CAPApplicationContext.CapV1_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(compType == ComponentType.Invoke && parameter == null) {
                		processed = true;
                		ActivityTestRequest ind = new ActivityTestRequestImpl();
                		ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onActivityTestRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}
                	else if(compType == ComponentType.ReturnResultLast && parameter == null) {
                		processed = true;
                		ActivityTestResponse ind = new ActivityTestResponseImpl();
                		ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onActivityTestResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}
                }
                break;

            case CAPOperationCode.assistRequestInstructions:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                	if(parameter instanceof AssistRequestInstructionsRequest) {
        				processed = true;
        				AssistRequestInstructionsRequest ind = (AssistRequestInstructionsRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onAssistRequestInstructionsRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.establishTemporaryConnection:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof EstablishTemporaryConnectionRequest) {
        				processed = true;
        				EstablishTemporaryConnectionRequest ind = (EstablishTemporaryConnectionRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onEstablishTemporaryConnectionRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.disconnectForwardConnection:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(compType == ComponentType.Invoke && parameter == null) {
                		processed = true;
                		DisconnectForwardConnectionRequest ind = new DisconnectForwardConnectionRequestImpl();
                		ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}
                }
                break;

            case CAPOperationCode.disconnectLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                	if(parameter instanceof DisconnectLegRequest) {
        				processed = true;
        				DisconnectLegRequest ind = (DisconnectLegRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectLegRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        } 
                	else if(parameter == null && compType == ComponentType.ReturnResultLast) {
        				processed = true;
        				DisconnectLegResponse ind = new DisconnectLegResponseImpl();
        				ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectLegResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.dFCWithArgument:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                	if(parameter instanceof DisconnectForwardConnectionWithArgumentRequest) {
        				processed = true;
        				DisconnectForwardConnectionWithArgumentRequest ind = (DisconnectForwardConnectionWithArgumentRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onDisconnectForwardConnectionWithArgumentRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        } 
                }
                break;

            case CAPOperationCode.initiateCallAttempt:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                	if(parameter instanceof InitiateCallAttemptRequest) {
        				processed = true;
        				InitiateCallAttemptRequest ind = (InitiateCallAttemptRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitiateCallAttemptRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        } 
                	else if(parameter instanceof InitiateCallAttemptResponse) {
        				processed = true;
        				InitiateCallAttemptResponse ind = (InitiateCallAttemptResponse)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onInitiateCallAttemptResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.connectToResource:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ConnectToResourceRequest) {
        				processed = true;
        				ConnectToResourceRequest ind = (ConnectToResourceRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onConnectToResourceRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        } 
                }
                break;

            case CAPOperationCode.resetTimer:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof ResetTimerRequest) {
        				processed = true;
        				ResetTimerRequest ind = (ResetTimerRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onResetTimerRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.furnishChargingInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter instanceof FurnishChargingInformationRequest) {
        				processed = true;
        				FurnishChargingInformationRequest ind = (FurnishChargingInformationRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onFurnishChargingInformationRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.sendChargingInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric) {
                	if(parameter instanceof SendChargingInformationRequest) {
        				processed = true;
        				SendChargingInformationRequest ind = (SendChargingInformationRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onSendChargingInformationRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.specializedResourceReport:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                	if(parameter instanceof SpecializedResourceReportRequest) {
        				processed = true;
        				SpecializedResourceReportRequest ind = (SpecializedResourceReportRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onSpecializedResourceReportRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.playAnnouncement:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                	if(parameter instanceof PlayAnnouncementRequest) {
        				processed = true;
        				PlayAnnouncementRequest ind = (PlayAnnouncementRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onPlayAnnouncementRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.promptAndCollectUserInformation:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                	if(parameter instanceof PromptAndCollectUserInformationRequest) {
        				processed = true;
        				PromptAndCollectUserInformationRequest ind = (PromptAndCollectUserInformationRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onPromptAndCollectUserInformationRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(parameter instanceof PromptAndCollectUserInformationResponse) {
        				processed = true;
        				PromptAndCollectUserInformationResponse ind = (PromptAndCollectUserInformationResponse)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onPromptAndCollectUserInformationResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.cancelCode:
                if (acn == CAPApplicationContext.CapV2_gsmSSF_to_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV2_assistGsmSSF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_gsmSSF_scfAssistHandoff
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric
                        || acn == CAPApplicationContext.CapV2_gsmSRF_to_gsmSCF
                        || acn == CAPApplicationContext.CapV3_gsmSRF_gsmSCF || acn == CAPApplicationContext.CapV4_gsmSRF_gsmSCF) {
                	if(parameter instanceof CancelRequest) {
        				processed = true;
        				CancelRequest ind = (CancelRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onCancelRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.moveLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                	if(parameter instanceof MoveLegRequest) {
        				processed = true;
        				MoveLegRequest ind = (MoveLegRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onMoveLegRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(parameter == null && compType == ComponentType.ReturnResultLast) {
                		processed = true;
                		MoveLegResponseImpl ind = new MoveLegResponseImpl();
        				ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onMoveLegResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.splitLeg:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {

                	if(parameter instanceof SplitLegRequest) {
        				processed = true;
        				SplitLegRequest ind = (SplitLegRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onSplitLegRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(parameter == null && compType == ComponentType.ReturnResultLast) {
        				processed = true;
        				SplitLegResponse ind = new SplitLegResponseImpl();
        				ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onSplitLegResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            case CAPOperationCode.collectInformation:
                if (acn == CAPApplicationContext.CapV4_gsmSSF_scfGeneric
                        || acn == CAPApplicationContext.CapV4_scf_gsmSSFGeneric) {
                	if(parameter == null && compType == ComponentType.Invoke) {
        				processed = true;
        				CollectInformationRequest ind = new CollectInformationRequestImpl();
        				ind.setInvokeId(invokeId);
                    	ind.setCAPDialog(capDialog);
                		
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceCircuitSwitchedCallListener) serLis).onCollectInformationRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;

            default:
                throw new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
        
        if(!processed) {
        	if(parameter == null)
        		 throw new CAPParsingComponentException("Error while decoding , Parameter is mandatory but not found",CAPParsingComponentExceptionReason.MistypedParameter);
        	
        	throw new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
    }
}