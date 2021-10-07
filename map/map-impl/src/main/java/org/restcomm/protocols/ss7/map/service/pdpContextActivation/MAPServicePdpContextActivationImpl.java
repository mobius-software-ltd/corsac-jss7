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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import java.util.List;

import org.apache.log4j.Logger;
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
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPDialogPdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivation;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.MAPServicePdpContextActivationListener;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsRequest;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;
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
public class MAPServicePdpContextActivationImpl extends MAPServiceBaseImpl implements MAPServicePdpContextActivation {

    protected Logger loger = Logger.getLogger(MAPServicePdpContextActivationImpl.class);

    public MAPServicePdpContextActivationImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP PdpContextActivation dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogPdpContextActivation createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressStringImpl origReference,
            SccpAddress destAddress, AddressStringImpl destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogPdpContextActivation createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressStringImpl origReference,
            SccpAddress destAddress, AddressStringImpl destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException(
                    "Cannot create MAPDialogPdpContextActivation because MAPServicePdpContextActivation is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogPdpContextActivationImpl dialog = new MAPDialogPdpContextActivationImpl(appCntx, tcapDialog,
                this.mapProviderImpl, this, origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogPdpContextActivationImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServicePdpContextActivationListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServicePdpContextActivationListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {
        case gprsLocationInfoRetrievalContext:
            if (vers >= 3 && vers <= 4) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 4) {
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

    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
            Long invokeId, Long linkedId) throws MAPParsingComponentException {

    	Long ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        
        int ocValueInt = (int) (long) ocValue;
        Boolean processed=false;
        switch (ocValueInt) {
            case MAPOperationCode.sendRoutingInfoForGprs:
            	if(parameter instanceof SendRoutingInfoForGprsRequest)
                {
            		processed=true;
            		SendRoutingInfoForGprsRequest ind=(SendRoutingInfoForGprsRequest)parameter;
                	
                    for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                        	serLis.onMAPMessage(ind);
                            ((MAPServicePdpContextActivationListener) serLis).onSendRoutingInfoForGprsRequest(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInfoForGprsRequestIndication: " + e.getMessage(), e);
                        }
                    }
                }
                else if(parameter instanceof SendRoutingInfoForGprsResponse)
                {
                	processed=true;
                	SendRoutingInfoForGprsResponse ind=(SendRoutingInfoForGprsResponse)parameter;
                	for (MAPServiceListener serLis : this.serviceListeners) {
                        try {
                        	serLis.onMAPMessage(ind);
                            ((MAPServicePdpContextActivationListener) serLis).onSendRoutingInfoForGprsResponse(ind);
                        } catch (Exception e) {
                            loger.error("Error processing SendRoutingInformationResponseIndication: " + e.getMessage(), e);
                        }
                    }
                }
                break;
            default:
                throw new MAPParsingComponentException("MAPServicePdpContextActivation: unknown incoming operation code: "
                        + ocValueInt, MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServicePdpContextActivation: unknown incoming operation code: "
                    + ocValueInt, MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}