/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.gprs;

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
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ActivityTestGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ApplyChargingReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPDialogGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprs;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CAPServiceGprsListener;
import org.restcomm.protocols.ss7.cap.api.service.gprs.CancelGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ContinueGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EntityReleasedGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSResponse;
import org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.InitialDpGprsRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ReleaseGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCodeImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPServiceGprsImpl extends CAPServiceBaseImpl implements CAPServiceGprs {

    protected Logger loger = Logger.getLogger(CAPServiceGprsImpl.class);

    public CAPServiceGprsImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogGprs createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress) throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogGprs createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress, Long localTrId) throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException("Cannot create CAPDialogGprs because CAPServiceGprsl is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogGprsImpl dialog = new CAPDialogGprsImpl(appCntx, tcapDialog, this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceGprsListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceGprsListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogGprsImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
        switch (dialogApplicationContext) {
            case CapV3_gprsSSF_gsmSCF:
            case CapV3_gsmSCF_gprsSSF:
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			default:
				break;
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public void processComponent(ComponentType compType, OperationCodeImpl oc, CAPMessage parameter, CAPDialog capDialog,
            Long invokeId, Long linkedId) throws CAPParsingComponentException {

        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new CAPParsingComponentException("", CAPParsingComponentExceptionReason.UnrecognizedOperation);
        
        CAPApplicationContext acn = capDialog.getApplicationContext();
        int ocValueInt = (int) (long) ocValue;
        boolean processed = false;
        
        switch (ocValueInt) {
            case CAPOperationCode.initialDPGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF) {
                	if(parameter instanceof InitialDpGprsRequest) {
        				processed = true;
        				InitialDpGprsRequest ind = (InitialDpGprsRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onInitialDpGprsRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.requestReportGPRSEvent:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof RequestReportGPRSEventRequest) {
        				processed = true;
        				RequestReportGPRSEventRequest ind = (RequestReportGPRSEventRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onRequestReportGPRSEventRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.applyChargingGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ApplyChargingGPRSRequest) {
        				processed = true;
        				ApplyChargingGPRSRequest ind = (ApplyChargingGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onApplyChargingGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.entityReleasedGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof EntityReleasedGPRSRequest) {
        				processed = true;
        				EntityReleasedGPRSRequest ind = (EntityReleasedGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onEntityReleasedGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(compType == ComponentType.ReturnResultLast && parameter == null) {
                		processed = true;
        				EntityReleasedGPRSResponse ind = new EntityReleasedGPRSResponseImpl();
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onEntityReleasedGPRSResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}
                }
                break;
            case CAPOperationCode.connectGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ConnectGPRSRequest) {
        				processed = true;
        				ConnectGPRSRequest ind = (ConnectGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onConnectGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.continueGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ContinueGPRSRequest) {
        				processed = true;
        				ContinueGPRSRequest ind = (ContinueGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onContinueGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.releaseGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ReleaseGPRSRequest) {
        				processed = true;
        				ReleaseGPRSRequest ind = (ReleaseGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onReleaseGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.resetTimerGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ResetTimerGPRSRequest) {
        				processed = true;
        				ResetTimerGPRSRequest ind = (ResetTimerGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onResetTimerGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.furnishChargingInformationGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof FurnishChargingInformationGPRSRequest) {
        				processed = true;
        				FurnishChargingInformationGPRSRequest ind = (FurnishChargingInformationGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onFurnishChargingInformationGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.cancelGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof CancelGPRSRequest) {
        				processed = true;
        				CancelGPRSRequest ind = (CancelGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onCancelGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.sendChargingInformationGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof SendChargingInformationGPRSRequest) {
        				processed = true;
        				SendChargingInformationGPRSRequest ind = (SendChargingInformationGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onSendChargingInformationGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                }
                break;
            case CAPOperationCode.applyChargingReportGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof ApplyChargingReportGPRSRequest) {
        				processed = true;
        				ApplyChargingReportGPRSRequest ind = (ApplyChargingReportGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onApplyChargingReportGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(compType == ComponentType.ReturnResultLast && parameter == null) {
                		processed = true;
                		ApplyChargingReportGPRSResponse ind = new ApplyChargingReportGPRSResponseImpl();
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onApplyChargingReportGPRSResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}                    
                }
                break;
            case CAPOperationCode.eventReportGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(parameter instanceof EventReportGPRSRequest) {
        				processed = true;
        				EventReportGPRSRequest ind = (EventReportGPRSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onEventReportGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
                	else if(compType == ComponentType.ReturnResultLast && parameter == null) {
                		processed = true;
                		EventReportGPRSResponse ind = new EventReportGPRSResponseImpl();
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onEventReportGPRSResponse(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}   
                }
                break;
            case CAPOperationCode.activityTestGPRS:
                if (acn == CAPApplicationContext.CapV3_gprsSSF_gsmSCF || acn == CAPApplicationContext.CapV3_gsmSCF_gprsSSF) {
                	if(compType == ComponentType.Invoke && parameter == null) {
                		processed = true;
                		ActivityTestGPRSRequest ind = new ActivityTestGPRSRequestImpl();
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onActivityTestGPRSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
                	}
                	else if(compType == ComponentType.ReturnResultLast && parameter == null) {
                		processed = true;
                		ActivityTestGPRSResponse ind = new ActivityTestGPRSResponseImpl();
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceGprsListener) serLis).onActivityTestGPRSResponse(ind);
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