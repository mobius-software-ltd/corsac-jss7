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

package org.restcomm.protocols.ss7.map.service.oam;

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
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.ActivateTraceModeResponse_Oam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOam;
import org.restcomm.protocols.ss7.map.api.service.oam.MAPServiceOamListener;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiRequest;
import org.restcomm.protocols.ss7.map.api.service.oam.SendImsiResponse;
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
public class MAPServiceOamImpl extends MAPServiceBaseImpl implements MAPServiceOam {

    protected Logger loger = Logger.getLogger(MAPServiceOamImpl.class);

    public MAPServiceOamImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP OAM dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogOam createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogOam createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
    		AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogOam because MAPServiceOam is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogOamImpl dialog = new MAPDialogOamImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference,
                destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogOamImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceOamListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceOamListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {

        case imsiRetrievalContext:
            if (vers >= 2 && vers <= 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
                List<Long> altOid = dialogApplicationContext.getOID();
                altOid.set(7,3L);
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }

        case tracingContext:
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

        case MAPOperationCode.activateTraceMode:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.tracingContext, MAPApplicationContextVersion.version1);

        }

        return null;
    }

    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId) throws MAPParsingComponentException {
        Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        
        int ocValueInt = (int) (long) ocValue;
        Boolean processed=false;
        switch (ocValueInt) {
            case MAPOperationCode.activateTraceMode:
            	if(parameter instanceof ActivateTraceModeRequest_Oam)
                {
            		processed=true;
            		ActivateTraceModeRequest_Oam ind=(ActivateTraceModeRequest_Oam)parameter;
                	
                    for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                        	 serLis.onMAPMessage(ind);
                             ((MAPServiceOamListener) serLis).onActivateTraceModeRequest_Oam(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInformationRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof ActivateTraceModeResponse_Oam || (parameter == null && compType!= ComponentType.Invoke))
                {
                	processed=true;
                	ActivateTraceModeResponse_Oam ind=null;
                	if(parameter!=null)
                		ind=(ActivateTraceModeResponse_Oam)parameter;
                	else {
                		ind=new ActivateTraceModeResponseImpl();
                		ind.setInvokeId(invokeId);
                        ind.setMAPDialog(mapDialog);
                        ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                	}
                	
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceOamListener) serLis).onActivateTraceModeResponse_Oam(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInformationResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;

            case MAPOperationCode.sendIMSI:
            	if(parameter instanceof SendImsiRequest)
                {
            		processed=true;
                	SendImsiRequest ind=(SendImsiRequest)parameter;
                	
                    for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                        	serLis.onMAPMessage(ind);
                            ((MAPServiceOamListener) serLis).onSendImsiRequest(ind);
                        } catch (Exception e) {
                            loger.error("Error processing ProvideRoamingNumberRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof SendImsiResponse)
                {
                	processed=true;
                	SendImsiResponse ind=(SendImsiResponse)parameter;
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                            serLis.onMAPMessage(ind);
                            ((MAPServiceOamListener) serLis).onSendImsiResponse(ind);
                        } catch (Exception e) {
                            loger.error("Error processing ProvideRoamingNumberResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;
            default:
                throw new MAPParsingComponentException("MAPServiceOam: unknown incoming operation code: " + ocValueInt,
                        MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServiceOam: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}