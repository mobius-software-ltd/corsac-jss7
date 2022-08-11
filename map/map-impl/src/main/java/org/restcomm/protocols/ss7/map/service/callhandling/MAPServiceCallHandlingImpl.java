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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.IstCommandResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPDialogCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandling;
import org.restcomm.protocols.ss7.map.api.service.callhandling.MAPServiceCallHandlingListener;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ProvideRoamingNumberResponse;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationResponse;
import org.restcomm.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author cristian veliscu
 * @author eva ogallar
 * @author yulianoifa
 */
public class MAPServiceCallHandlingImpl extends MAPServiceBaseImpl implements MAPServiceCallHandling {
    private static final Logger loger = LogManager.getLogger(MAPServiceCallHandlingImpl.class);

    public static final String NAME="CallHandling";
    
    // Include these constants in MAPApplicationContextName and MAPOperationCode
    // sendRoutingInfo_Request: add constant to MAPMessageType
    // sendRoutingInfo_Response: add constant to MAPMessageType
    protected static final int version = 3;

    public MAPServiceCallHandlingImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    @Override
    public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, int networkId) throws MAPException {
    	mapProviderImpl.getMAPStack().newDialogSent(NAME, networkId);
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null, networkId);
    }

    @Override
    public MAPDialogCallHandling createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId, int networkId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException(
                    "Cannot create MAPDialogRoutingInformation because MAPServiceRoutingInformation is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId, networkId);
        MAPDialogCallHandlingImpl dialog = new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);
        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
    	mapProviderImpl.getMAPStack().newDialogReceived(NAME, tcapDialog.getNetworkId());
        return new MAPDialogCallHandlingImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    @Override
    public void addMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    @Override
    public void removeMAPServiceListener(MAPServiceCallHandlingListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    @Override
    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
            case locationInfoRetrievalContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
            case roamingNumberEnquiryContext:
                if (vers >= 1 && vers <= 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
                } else {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
                }
            case ServiceTerminationContext:
                if (vers == 3) {
                    return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
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
            case MAPOperationCode.sendRoutingInfo:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.locationInfoRetrievalContext,
                        MAPApplicationContextVersion.version1);
            case MAPOperationCode.provideRoamingNumber:
                return MAPApplicationContext.getInstance(MAPApplicationContextName.roamingNumberEnquiryContext,
                        MAPApplicationContextVersion.version1);
        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
    		Integer invokeId, Integer linkedId) throws MAPParsingComponentException {

    	Integer ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        
        int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
        int ocValueInt = (int) (long) ocValue;
        Boolean processed=false;
        switch (ocValueInt) {
            case MAPOperationCode.sendRoutingInfo:
            	if(parameter instanceof SendRoutingInformationRequest)
                {
            		processed=true;
                	SendRoutingInformationRequest ind=(SendRoutingInformationRequest)parameter;
                	
                    for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationRequest(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInformationRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof SendRoutingInformationResponse)
                {
                	if (vers >= 3 && parameter instanceof SendRoutingInformationResponseImplV1)
                		throw new MAPParsingComponentException(
                                "Received V1 SendRoutingInformationResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                	else if(vers<3 && parameter instanceof SendRoutingInformationResponseImplV3)
                		throw new MAPParsingComponentException(
                                "Received V3 SendRoutingInformationResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                	
                	processed=true;
                	SendRoutingInformationResponse ind=(SendRoutingInformationResponse)parameter;
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onSendRoutingInformationResponse(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInformationResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;
            case MAPOperationCode.provideRoamingNumber:
            	if(parameter instanceof ProvideRoamingNumberRequest)
                {
            		processed=true;
                	ProvideRoamingNumberRequest ind=(ProvideRoamingNumberRequest)parameter;
                	
                    for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onProvideRoamingNumberRequest(ind);
                        } catch (Exception e) {
                            loger.error("Error processing ProvideRoamingNumberRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof ProvideRoamingNumberResponse)
                {
                	if (vers >= 3 && parameter instanceof ProvideRoamingNumberResponseImplV1)
                		throw new MAPParsingComponentException(
                                "Received V1 ProvideRoamingNumberResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                	else if(vers<3 && parameter instanceof ProvideRoamingNumberResponseImplV3)
                		throw new MAPParsingComponentException(
                                "Received V3 ProvideRoamingNumberResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                	
                	processed=true;
                	ProvideRoamingNumberResponse ind=(ProvideRoamingNumberResponse)parameter;
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onProvideRoamingNumberResponse(ind);
                        } catch (Exception e) {
                            loger.error("Error processing ProvideRoamingNumberResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;

            case MAPOperationCode.istCommand:
            	if(parameter instanceof IstCommandRequest)
                {
            		processed=true;
                	IstCommandRequest ind=(IstCommandRequest)parameter;
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onIstCommandRequest(ind);
                        } catch (Exception e) {
                            loger.error("Error processing IstCommandRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof IstCommandResponse || (parameter == null && compType!= ComponentType.Invoke))
                {
                	processed=true;
                	IstCommandResponse ind=null;
                	if(parameter!=null)
                		ind=(IstCommandResponse)parameter;
                	else {
                		ind=new IstCommandResponseImpl();
                		ind.setInvokeId(invokeId);
                        ind.setMAPDialog(mapDialog);
                        ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                        mapProviderImpl.getMAPStack().newMessageReceived(ind.getMessageType().name(), mapDialog.getNetworkId());
                	}
                	
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceCallHandlingListener) serLis).onIstCommandResponse(ind);
                        } catch (Exception e) {
                            loger.error("Error processing IstCommandResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;
            default:
            	throw new MAPParsingComponentException("MAPServiceCallHandling: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }       
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServiceCallHandling: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}