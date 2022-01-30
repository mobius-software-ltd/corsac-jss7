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

package org.restcomm.protocols.ss7.map.service.sms;

import java.util.List;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.MAPServiceBaseImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.MAPServiceListener;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.InformServiceCentreRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSmsListener;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MoForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.MtForwardShortMessageResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.NoteSubscriberPresentRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReadyForSMResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMRequest;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;
import org.restcomm.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPServiceSmsImpl extends MAPServiceBaseImpl implements MAPServiceSms {

    protected Logger loger = Logger.getLogger(MAPServiceSmsImpl.class);

    public MAPServiceSmsImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP SMS dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogSms createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogSms createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogSms because MAPServiceSms is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogSmsImpl dialog = new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference,
                destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogSmsImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceSmsListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
            case shortMsgAlertContext:
                if (vers >= 1 && vers <= 2) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 2) {
                    List<Long> altOid = dialogApplicationContext.getOID();
                    altOid.set(7,2L);
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case shortMsgMORelayContext:
            case shortMsgGatewayContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                	List<Long> altOid = dialogApplicationContext.getOID();
                    altOid.set(7,3L);
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case shortMsgMTRelayContext:
                if (vers >= 2 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                	List<Long> altOid = dialogApplicationContext.getOID();
                    altOid.set(7,3L);
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }

            case mwdMngtContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else if (vers > 3) {
                	List<Long> altOid = dialogApplicationContext.getOID();
                    altOid.set(7,3L);
                    ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
			default:
				break;
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
    }

    @Override
    public MAPApplicationContext getMAPv1ApplicationContext(int operationCode) {

        switch (operationCode) {
            case MAPOperationCode.mo_forwardSM:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgMORelayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.alertServiceCentreWithoutResult:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgAlertContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.sendRoutingInfoForSM:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.reportSM_DeliveryStatus:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.shortMsgGatewayContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.noteSubscriberPresent:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.mwdMngtContext,
                        MAPApplicationContextVersion.version1);
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId) throws MAPParsingComponentException {

    	 Long ocValue = oc.getLocalOperationCode();
         if (ocValue == null)
             new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
         MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
         int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
         int ocValueInt = (int) (long) ocValue;

         Boolean processed=false;
         switch (ocValueInt) {
             case MAPOperationCode.mo_forwardSM:
                 if (acn == MAPApplicationContextName.shortMsgMORelayContext
                         || acn == MAPApplicationContextName.shortMsgMTRelayContext && vers == 2) {
                     if (vers >= 3) {
                    	 if(parameter instanceof MoForwardShortMessageRequest)
                         {
                    		 processed=true;
                         	 MoForwardShortMessageRequest ind=(MoForwardShortMessageRequest)parameter;
                         	
                             for (MAPServiceListener serLis : this.serviceListeners) {
                                 try {
                                     serLis.onMAPMessage(ind);
                                     ((MAPServiceSmsListener) serLis).onMoForwardShortMessageRequest(ind);
                                 } catch (Exception e) {
                                     loger.error("Error processing MoForwardShortMessageRequestIndication: " + e.getMessage(), e);
                                 }
                             }
                         }
                         else if(parameter instanceof MoForwardShortMessageResponse || (parameter==null && compType!= ComponentType.Invoke))
                         {
                        	processed=true;
                         	MoForwardShortMessageResponse ind;
                         	if(parameter!=null)
                         		ind=(MoForwardShortMessageResponse)parameter;
                         	else {
                         		ind=new MoForwardShortMessageResponseImpl();
                         		ind.setInvokeId(invokeId);
                         		ind.setMAPDialog(mapDialog);
                         		ind.setReturnResultNotLast(compType == ComponentType.ReturnResult);
                         	}
                         	
                         	for (MAPServiceListener serLis : this.serviceListeners) {
                                 try {
                                     serLis.onMAPMessage(ind);
                                     ((MAPServiceSmsListener) serLis).onMoForwardShortMessageResponse(ind);
                                 } catch (Exception e) {
                                     loger.error("Error processing MoForwardShortMessageResponseIndication: " + e.getMessage(), e);
                                 }
                             }
                         }
                     } else {
                    	 if(parameter instanceof ForwardShortMessageRequest)
                         {
                    		 processed=true;
                          	 ForwardShortMessageRequest ind=(ForwardShortMessageRequest)parameter;
                         	
                             for (MAPServiceListener serLis : this.serviceListeners) {
                                 try {
                                     serLis.onMAPMessage(ind);
                                     ((MAPServiceSmsListener) serLis).onForwardShortMessageRequest(ind);
                                 } catch (Exception e) {
                                     loger.error("Error processing ForwardShortMessageRequestIndication: " + e.getMessage(), e);
                                 }
                             }
                         }
                         else if(parameter==null && compType!= ComponentType.Invoke)
                         {
                        	 processed=true;
                        	 MoForwardShortMessageResponseImpl ind=new MoForwardShortMessageResponseImpl();
                         	 ind.setInvokeId(invokeId);
                             ind.setMAPDialog(mapDialog);
                             ind.setReturnResultNotLast(compType!=ComponentType.ReturnResultLast);
                            
                         	 for (MAPServiceListener serLis : this.serviceListeners) {
                                 try {
                                     serLis.onMAPMessage(ind);
                                     ((MAPServiceSmsListener) serLis).onForwardShortMessageResponse(ind);
                                 } catch (Exception e) {
                                     loger.error("Error processing ForwardShortMessageResponseIndication: " + e.getMessage(), e);
                                 }
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.mt_forwardSM:
                 if (acn == MAPApplicationContextName.shortMsgMTRelayContext && vers >= 3) {
                	 if(parameter instanceof MtForwardShortMessageRequest)
                     {
                		 processed=true;
                      	 MtForwardShortMessageRequest ind=(MtForwardShortMessageRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onMtForwardShortMessageRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing MtForwardShortMessageRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                     else if(parameter instanceof MtForwardShortMessageResponse || (parameter==null && compType!= ComponentType.Invoke))
                     {
                    	processed=true;
                      	MtForwardShortMessageResponse ind;
                     	if(parameter!=null)
                     		ind=(MtForwardShortMessageResponse)parameter;
                     	else {
                     		ind=new MtForwardShortMessageResponseImpl();
                     		ind.setInvokeId(invokeId);
                     		ind.setMAPDialog(mapDialog);
                     		ind.setReturnResultNotLast(compType == ComponentType.ReturnResult);
                     	}
                     	
                     	for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onMtForwardShortMessageResponse(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing MtForwardShortMessageResponseIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.sendRoutingInfoForSM:
                 if (acn == MAPApplicationContextName.shortMsgGatewayContext) {
                	 if(parameter instanceof SendRoutingInfoForSMRequest)
                     {
                		 processed=true;
                      	 SendRoutingInfoForSMRequest ind=(SendRoutingInfoForSMRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onSendRoutingInfoForSMRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing SendRoutingInfoForSMRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                     else if(parameter instanceof SendRoutingInfoForSMResponse)
                     {
                    	 processed=true;
                      	 SendRoutingInfoForSMResponse ind=(SendRoutingInfoForSMResponse)parameter;
                     	 for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onSendRoutingInfoForSMResponse(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing SendRoutingInfoForSMResponseIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.reportSM_DeliveryStatus:
                 if (acn == MAPApplicationContextName.shortMsgGatewayContext) {
                	 if(parameter instanceof ReportSMDeliveryStatusRequest)
                     {
                		 processed=true;
                      	 ReportSMDeliveryStatusRequest ind=(ReportSMDeliveryStatusRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onReportSMDeliveryStatusRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing ReportSMDeliveryStatusRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                     else if(parameter instanceof ReportSMDeliveryStatusResponse || (parameter==null && compType!= ComponentType.Invoke))
                     {
                    	 processed=true;
                      	 ReportSMDeliveryStatusResponse ind;
                     	 if(parameter!=null) {
                     		if (vers >= 3 && parameter instanceof ReportSMDeliveryStatusResponseImplV1)
                        		throw new MAPParsingComponentException(
                                        "Received V1 ReportSMDeliveryStatusResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	else if(vers<3 && parameter instanceof ReportSMDeliveryStatusResponseImplV3)
                        		throw new MAPParsingComponentException(
                                        "Received V3 ReportSMDeliveryStatusResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	
                        	ind = (ReportSMDeliveryStatusResponse)parameter;
                     	 }
                     	 else {
                     		ind = new ReportSMDeliveryStatusResponseImplV1();
                     		ind.setInvokeId(invokeId);
                     		ind.setMAPDialog(mapDialog);
                     		ind.setReturnResultNotLast(compType == ComponentType.ReturnResult);
                     	 }
                     	
                     	 for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onReportSMDeliveryStatusResponse(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing ReportSMDeliveryStatusResponseIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.informServiceCentre:
                 if (acn == MAPApplicationContextName.shortMsgGatewayContext && vers >= 2) {
                	 if(parameter instanceof InformServiceCentreRequest)
                     {
                		 processed=true;
                      	 InformServiceCentreRequest ind=(InformServiceCentreRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onInformServiceCentreRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing InformServiceCentreRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.alertServiceCentre:
                 if (acn == MAPApplicationContextName.shortMsgAlertContext && vers >= 2) {
                	 if(parameter instanceof AlertServiceCentreRequest)
                     {
                		 processed=true;
                      	 AlertServiceCentreRequest ind=(AlertServiceCentreRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onAlertServiceCentreRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing AlertServiceCentreRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                     else if(parameter==null && compType!= ComponentType.Invoke)
                     {
                    	 processed=true;
                      	 AlertServiceCentreResponse ind=new AlertServiceCentreResponseImpl();
                     	 ind.setInvokeId(invokeId);
                         ind.setMAPDialog(mapDialog);
                         ind.setReturnResultNotLast(compType!=ComponentType.ReturnResultLast);
                        
                         for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSmsListener) serLis).onAlertServiceCentreResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing AlertServiceCentreResponseIndication: " + e.getMessage(), e);
                            }
                        }
                     }
                 }
                 break;

             case MAPOperationCode.alertServiceCentreWithoutResult:
                 if (acn == MAPApplicationContextName.shortMsgAlertContext && vers == 1) {
                	 if(parameter instanceof AlertServiceCentreRequest)
                     {
                		 processed=true;
                      	 AlertServiceCentreRequest ind=(AlertServiceCentreRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onAlertServiceCentreRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing AlertServiceCentreRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.readyForSM:
                 if (acn == MAPApplicationContextName.mwdMngtContext && vers >= 2) {
                	 if(parameter instanceof ReadyForSMRequest)
                     {
                		 processed=true;
                      	 ReadyForSMRequest ind=(ReadyForSMRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onReadyForSMRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing ReadyForSMRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                     else if(parameter instanceof ReadyForSMResponse || (parameter==null && compType!= ComponentType.Invoke))
                     {
                    	 processed=true;
                      	 ReadyForSMResponse ind;
                     	 if(parameter!=null)
                     		ind=(ReadyForSMResponse)parameter;
                     	 else {
                     		ind=new ReadyForSMResponseImpl();
                     		ind.setInvokeId(invokeId);
                     		ind.setMAPDialog(mapDialog);
                     		ind.setReturnResultNotLast(compType == ComponentType.ReturnResult);
                     	 }
                     	
                     	 for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onReadyForSMResponse(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing ReadyForSMResponseIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             case MAPOperationCode.noteSubscriberPresent:
                 if (acn == MAPApplicationContextName.mwdMngtContext && vers == 1) {
                	 if(parameter instanceof NoteSubscriberPresentRequest)
                     {
                		 processed=true;
                      	 NoteSubscriberPresentRequest ind=(NoteSubscriberPresentRequest)parameter;
                     	
                         for (MAPServiceListener serLis : this.serviceListeners) {
                             try {
                                 serLis.onMAPMessage(ind);
                                 ((MAPServiceSmsListener) serLis).onNoteSubscriberPresentRequest(ind);
                             } catch (Exception e) {
                                 loger.error("Error processing NoteSubscriberPresentRequestIndication: " + e.getMessage(), e);
                             }
                         }
                     }
                 }
                 break;

             default:
                 throw new MAPParsingComponentException("MAPServiceSms: unknown incoming operation code: " + ocValueInt,
                         MAPParsingComponentExceptionReason.UnrecognizedOperation);
         }
         
         if(!processed)
        	 throw new MAPParsingComponentException("MAPServiceSms: unknown incoming operation code: " + ocValueInt,
                     MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}