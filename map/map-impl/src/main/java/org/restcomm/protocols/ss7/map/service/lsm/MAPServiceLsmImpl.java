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

package org.restcomm.protocols.ss7.map.service.lsm;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.MAPServiceBaseImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessage;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.restcomm.protocols.ss7.map.api.MAPServiceListener;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckData;
import org.restcomm.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm;
import org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.ProvideSubscriberLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportRequest;
import org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberLocationReportResponse;
import org.restcomm.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.TcapFactory;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class MAPServiceLsmImpl extends MAPServiceBaseImpl implements MAPServiceLsm {
	private static final Logger loger = LogManager.getLogger(MAPServiceLsmImpl.class);

	public static final String NAME="LocationServices";
    
    /**
     * @param mapProviderImpl
     */
    public MAPServiceLsmImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm#createNewDialog
     * (org.restcomm.protocols.ss7.map.api.MAPApplicationContext, org.restcomm.protocols.ss7.sccp.parameter.SccpAddress,
     * org.restcomm.protocols.ss7.map.api.dialog.AddressString, org.restcomm.protocols.ss7.sccp.parameter.SccpAddress,
     * org.restcomm.protocols.ss7.map.api.dialog.AddressString)
     */
    public MAPDialogLsm createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference, int networkId) throws MAPException {
    	mapProviderImpl.getMAPStack().newDialogSent(NAME, networkId);
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null, networkId);
    }

    public MAPDialogLsm createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference,
            SccpAddress destAddress, AddressString destReference, Long localTrId, int networkId) throws MAPException {
        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogLsm because MAPServiceLsm is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId, networkId);

        MAPDialogLsmImpl dialog = new MAPDialogLsmImpl(appCntx, tcapDialog, this.mapProviderImpl, this, origReference,
                destReference);

        return dialog;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm# addMAPServiceListener
     * (org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
     */
    public void addMAPServiceListener(MAPServiceLsmListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsm# removeMAPServiceListener
     * (org.restcomm.protocols.ss7.map.api.service.lsm.MAPServiceLsmListener)
     */
    public void removeMAPServiceListener(MAPServiceLsmListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.MAPServiceBaseImpl#createNewDialogIncoming
     * (org.restcomm.protocols.ss7.map.api.MAPApplicationContext, org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog)
     */
    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog, Boolean logStats) {
    	if(logStats)
    		mapProviderImpl.getMAPStack().newDialogReceived(NAME, tcapDialog.getNetworkId());
    	
        return new MAPDialogLsmImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceBase#isServingService(org
     * .restcomm.protocols.ss7.map.api.MAPApplicationContext)
     */
    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {

        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (dialogApplicationContext.getApplicationContextName()) {
            case locationSvcEnquiryContext:
            case locationSvcGatewayContext:
                if (vers == 3) {
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

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPServiceBase#processComponent(org
     * .restcomm.protocols.ss7.tcap.asn.comp.ComponentType, org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode,
     * org.restcomm.protocols.ss7.tcap.asn.comp.Parameter, org.restcomm.protocols.ss7.map.api.MAPDialog, java.lang.Long,
     * java.lang.Long)
     */
    @Override
    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
    		Integer invokeId, Integer linkedId) throws MAPParsingComponentException {

    	Integer ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);

        long ocValueInt = ocValue;
        int ocValueInt2 = (int) ocValueInt;
        Boolean processed=false;
        switch (ocValueInt2) {
            case MAPOperationCode.provideSubscriberLocation:
            	if(parameter instanceof ProvideSubscriberLocationRequest) {
            		processed=true;
            		ProvideSubscriberLocationRequest ind=(ProvideSubscriberLocationRequest)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(ind);
        	                ((MAPServiceLsmListener) serLis).onProvideSubscriberLocationRequest(ind);
            			} catch (Exception e) {
                            loger.error("Error processing ProvideSubscriberLocationRequestIndication: " + e.getMessage(), e);
                        }
                    }
            	} else if(parameter instanceof ProvideSubscriberLocationResponse) {
            		processed=true;
            		ProvideSubscriberLocationResponse provideSubsLoctResInd=(ProvideSubscriberLocationResponse)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(provideSubsLoctResInd);
        	                ((MAPServiceLsmListener) serLis).onProvideSubscriberLocationResponse(provideSubsLoctResInd);
            			} catch (Exception e) {
                            loger.error("Error processing ProvideSubscriberLocationResponseIndication: " + e.getMessage(), e);
                        }
                    }
            	}
                break;
            case MAPOperationCode.subscriberLocationReport:
            	if(parameter instanceof SubscriberLocationReportRequest) {
            		processed=true;
            		SubscriberLocationReportRequest ind=(SubscriberLocationReportRequest)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(ind);
        	                ((MAPServiceLsmListener) serLis).onSubscriberLocationReportRequest(ind);
            			} catch (Exception e) {
                            loger.error("Error processing SubscriberLocationReportRequestIndication: " + e.getMessage(), e);
                        }
                    }    		
            	} else if(parameter instanceof SubscriberLocationReportResponse) {
            		processed=true;
            		SubscriberLocationReportResponse resInd=(SubscriberLocationReportResponse)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(resInd);
        	                ((MAPServiceLsmListener) serLis).onSubscriberLocationReportResponse(resInd);
            			} catch (Exception e) {
                            loger.error("Error processing SubscriberLocationReportResponseIndication: " + e.getMessage(), e);
                        }
                    }
            	}
                break;
            case MAPOperationCode.sendRoutingInfoForLCS:
            	if(parameter instanceof SendRoutingInfoForLCSRequest) {
            		processed=true;
            		SendRoutingInfoForLCSRequest ind=(SendRoutingInfoForLCSRequest)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(ind);
        	                ((MAPServiceLsmListener) serLis).onSendRoutingInfoForLCSRequest(ind);
            			} catch (Exception e) {
                            loger.error("Error processing SendRoutingInfoForLCSRequestIndication: " + e.getMessage(), e);
                        }
                    }    		
            	} else if(parameter instanceof SendRoutingInfoForLCSResponse) {
            		processed=true;
            		SendRoutingInfoForLCSResponse resInd=(SendRoutingInfoForLCSResponse)parameter;
            		
            		for (MAPServiceListener serLis : this.serviceListeners) {
            			try {
        	                serLis.onMAPMessage(resInd);
        	                ((MAPServiceLsmListener) serLis).onSendRoutingInfoForLCSResponse(resInd);
            			} catch (Exception e) {
                            loger.error("Error processing SendRoutingInfoForLCSResponseIndication: " + e.getMessage(), e);
                        }
                    }
            	}
                break;
            default:
                throw new MAPParsingComponentException("MAPServiceLsm: unknown incoming operation code: " + ocValueInt,
                        MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }  	        
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServiceLsm: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}