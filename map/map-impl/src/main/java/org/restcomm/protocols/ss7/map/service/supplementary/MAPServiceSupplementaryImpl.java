/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.MAPServiceBaseImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.MAPServiceListener;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ActivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.DeactivateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.EraseSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GetPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.InterrogateSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterPasswordResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.RegisterSSResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.restcomm.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.restcomm.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.map.service.sms.MAPServiceSmsImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPServiceSupplementaryImpl extends MAPServiceBaseImpl implements MAPServiceSupplementary {

    private static final Logger loger = LogManager.getLogger(MAPServiceSmsImpl.class);
    public static final String NAME="USSD";
    
    public MAPServiceSupplementaryImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /**
     * Creating a new outgoing MAP Supplementary dialog and adding it to the MAPProvider.dialog collection
     *
     */
    public MAPDialogSupplementary createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference) throws MAPException {
    	mapProviderImpl.getMAPStack().newDialogSent(NAME);
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogSupplementary createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogSupplementary because MAPServiceSupplementary is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogSupplementaryImpl dialog = new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.MAPServiceBaseImpl#createNewDialogIncoming
     * (org.restcomm.protocols.ss7.map.api.MAPApplicationContext, org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
    	mapProviderImpl.getMAPStack().newDialogReceived(NAME);
        return new MAPDialogSupplementaryImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceSupplementaryListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
        case networkUnstructuredSsContext:
            if (vers == 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
                List<Long> altOid = dialogApplicationContext.getOID();
                altOid.set(7,2L);
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }
        case networkFunctionalSsContext:
            if (vers == 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
            	List<Long> altOid = dialogApplicationContext.getOID();
            	altOid.set(7,2L);
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

    public long[] getLinkedOperationList(long operCode) {
        if (operCode == MAPOperationCode.registerPassword) {
            return new long[] { MAPOperationCode.getPassword };
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
    		Integer invokeId, Integer linkedId) throws MAPParsingComponentException {

    	Integer ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

        long ocValueInt = ocValue;
        int ocValueInt2 = (int) ocValueInt;
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        Boolean processed=false;
        switch (ocValueInt2) {
            case MAPOperationCode.registerSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof RegisterSSRequest)
                    {
                		processed=true;
                		RegisterSSRequest ind=(RegisterSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onRegisterSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing RegisterSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof RegisterSSResponse || (parameter == null && compType!= ComponentType.Invoke))
                    {
                    	processed=true;
                    	RegisterSSResponse ind=null;
                    	if(parameter!=null)
                    		ind=(RegisterSSResponse)parameter;
                    	else {
                    		ind=new RegisterSSResponseImpl();
                    		ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                            mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());
                    	}
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onRegisterSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing RegisterSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.eraseSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof EraseSSRequest)
                    {
                		processed=true;
                    	EraseSSRequest ind=(EraseSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onEraseSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing EraseSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof EraseSSResponse || (parameter == null && compType!= ComponentType.Invoke))
                    {
                    	processed=true;
                    	EraseSSResponse ind=null;
                    	if(parameter!=null)
                    		ind=(EraseSSResponse)parameter;
                    	else {
                    		ind=new EraseSSResponseImpl();
                    		ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                            mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());
                    	}
                    	
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onEraseSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing EraseSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.activateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof ActivateSSRequest)
                    {
                		processed=true;
                    	ActivateSSRequest ind=(ActivateSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onActivateSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing ActivateSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof ActivateSSResponse || (parameter == null && compType!= ComponentType.Invoke))
                    {
                    	processed=true;
                    	ActivateSSResponse ind=null;
                    	if(parameter!=null)
                    		ind=(ActivateSSResponse)parameter;
                    	else {
                    		ind=new ActivateSSResponseImpl();
                    		ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                            mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());
                    	}
                    	
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onActivateSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing ActivateSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.deactivateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof DeactivateSSRequest)
                    {
                		processed=true;
                    	DeactivateSSRequest ind=(DeactivateSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onDeactivateSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing DeactivateSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof DeactivateSSResponse || (parameter == null && compType!= ComponentType.Invoke))
                    {
                    	processed=true;
                    	DeactivateSSResponse ind=null;
                    	if(parameter!=null)
                    		ind=(DeactivateSSResponse)parameter;
                    	else {
                    		ind=new DeactivateSSResponseImpl();
                    		ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                            mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());
                    	}
                    	
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onDeactivateSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing DeactivateSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.interrogateSS:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof InterrogateSSRequest)
                    {
                		processed=true;
                    	InterrogateSSRequest ind=(InterrogateSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onInterrogateSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing InterrogateSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof InterrogateSSResponse)
                    {
                    	processed=true;
                    	InterrogateSSResponse ind=(InterrogateSSResponse)parameter;
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onInterrogateSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing InterrogateSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.registerPassword:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof RegisterPasswordRequest)
                    {
                		processed=true;
                    	RegisterPasswordRequest ind=(RegisterPasswordRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onRegisterPasswordRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing RegisterPasswordRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof RegisterPasswordResponse)
                    {
                    	processed=true;
                    	RegisterPasswordResponse ind=(RegisterPasswordResponse)parameter;
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onRegisterPasswordResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing RegisterPasswordResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.getPassword:
                if (acn == MAPApplicationContextName.networkFunctionalSsContext) {
                	if(parameter instanceof GetPasswordRequest)
                    {
                		processed=true;
                    	GetPasswordRequest ind=(GetPasswordRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onGetPasswordRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing GetPasswordRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof GetPasswordResponse)
                    {
                    	processed=true;
                    	GetPasswordResponse ind=(GetPasswordResponse)parameter;
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onGetPasswordResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing GetPasswordResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;

            case MAPOperationCode.processUnstructuredSS_Request:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                	if(parameter instanceof ProcessUnstructuredSSRequest)
                    {
                		processed=true;
                    	ProcessUnstructuredSSRequest ind=(ProcessUnstructuredSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onProcessUnstructuredSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing ProcessUnstructuredSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof ProcessUnstructuredSSResponse)
                    {
                    	processed=true;
                    	ProcessUnstructuredSSResponse ind=(ProcessUnstructuredSSResponse)parameter;
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onProcessUnstructuredSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing ProcessUnstructuredSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.unstructuredSS_Request:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                	if(parameter instanceof UnstructuredSSRequest)
                    {
                		processed=true;
                    	UnstructuredSSRequest ind=(UnstructuredSSRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing UnstructuredSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter instanceof UnstructuredSSResponse || (parameter == null && compType!= ComponentType.Invoke))
                    {
                    	processed=true;
                    	UnstructuredSSResponse ind=null;
                    	if(parameter!=null)
                    		ind=(UnstructuredSSResponse)parameter;
                    	else {
                    		ind=new UnstructuredSSResponseImpl();
                    		ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                            mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());
                    	}
                    	
                    	for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing UnstructuredSSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            case MAPOperationCode.unstructuredSS_Notify:
                if (acn == MAPApplicationContextName.networkUnstructuredSsContext) {
                	if(parameter instanceof UnstructuredSSNotifyRequest)
                    {
                		processed=true;
                    	UnstructuredSSNotifyRequest ind=(UnstructuredSSNotifyRequest)parameter;
                    	
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSNotifyRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing UnstructuredSSNotifyRequestIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                    else if(parameter == null && compType!= ComponentType.Invoke)
                    {
                    	processed=true;
                    	UnstructuredSSNotifyResponse ind=new UnstructuredSSNotifyResponseImpl();
                    	ind.setInvokeId(invokeId);
                        ind.setMAPDialog(mapDialog);
                        ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                        mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name());

                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceSupplementaryListener) serLis).onUnstructuredSSNotifyResponse(ind);
                            } catch (Exception e) {
                                loger.error("Error processing UnstructuredSSNotifyResponseIndication: " + e.getMessage(), e);
                            }
                        }
                    }
                }
                break;
            default:
                throw new MAPParsingComponentException("MAPServiceSupplementary: unknown incoming operation code: "
                        + ocValueInt2, MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }    	
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServiceSupplementary: unknown incoming operation code: "
                    + ocValueInt2, MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}