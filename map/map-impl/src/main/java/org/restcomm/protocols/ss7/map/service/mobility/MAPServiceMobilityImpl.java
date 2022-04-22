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

package org.restcomm.protocols.ss7.map.service.mobility;

import java.util.List;

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
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.MAPServiceMobilityListener;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.AuthenticationFailureReportResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.authentication.SendAuthenticationInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.ResetRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery.RestoreDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.CheckImeiResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.PurgeMSResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SendIdentificationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateGprsLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.UpdateLocationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeRequest_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.oam.ActivateTraceModeResponse_Mobility;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DeleteSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.InsertSubscriberDataResponse;
import org.restcomm.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.AuthenticationFailureReportResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.authentication.SendAuthenticationInfoResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.faultRecovery.ForwardCheckSSIndicationRequestImpl;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.imei.CheckImeiResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.CancelLocationResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.PurgeMSResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationRequestImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.SendIdentificationResponseImplV3;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.locationManagement.UpdateLocationResponseImplV2;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.DeleteSubscriberDataResponseImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImplV1;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.InsertSubscriberDataResponseImplV3;
import org.restcomm.protocols.ss7.map.service.oam.ActivateTraceModeResponseImpl;
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
public class MAPServiceMobilityImpl extends MAPServiceBaseImpl implements MAPServiceMobility {

    protected Logger loger = LogManager.getLogger(MAPServiceMobilityImpl.class);

    public MAPServiceMobilityImpl(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    /*
     * Creating a new outgoing MAP Mobility dialog and adding it to the MAPProvider.dialog collection
     */
    public MAPDialogMobility createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference) throws MAPException {
        return this.createNewDialog(appCntx, origAddress, origReference, destAddress, destReference, null);
    }

    public MAPDialogMobility createNewDialog(MAPApplicationContext appCntx, SccpAddress origAddress, AddressString origReference, SccpAddress destAddress,
            AddressString destReference, Long localTrId) throws MAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new MAPException("Cannot create MAPDialogMobility because MAPServiceMobility is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        MAPDialogMobilityImpl dialog = new MAPDialogMobilityImpl(appCntx, tcapDialog, this.mapProviderImpl, this,
                origReference, destReference);

        this.putMAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    protected MAPDialogImpl createNewDialogIncoming(MAPApplicationContext appCntx, Dialog tcapDialog) {
        return new MAPDialogMobilityImpl(appCntx, tcapDialog, this.mapProviderImpl, this, null, null);
    }

    public void addMAPServiceListener(MAPServiceMobilityListener mapServiceListener) {
        super.addMAPServiceListener(mapServiceListener);
    }

    public void removeMAPServiceListener(MAPServiceMobilityListener mapServiceListener) {
        super.removeMAPServiceListener(mapServiceListener);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        MAPApplicationContextName ctx = dialogApplicationContext.getApplicationContextName();
        int vers = dialogApplicationContext.getApplicationContextVersion().getVersion();

        switch (ctx) {

        // -- Authentication management services
        case infoRetrievalContext:
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
        case authenticationFailureReportContext:
            if (vers >= 3 && vers <= 3) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 3) {
            	List<Long> altOid = dialogApplicationContext.getOID();
            	altOid.set(7,3L);
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }

            // -- Location management services
        case networkLocUpContext:
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
        case locationCancellationContext:
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
        case interVlrInfoRetrievalContext:
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
        case gprsLocationUpdateContext:
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
        case msPurgingContext:
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

            // -- Fault recovery
        case resetContext:
            if (vers >= 1 && vers <= 2) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 2) {
            	List<Long> altOid = dialogApplicationContext.getOID();
            	altOid.set(7,3L);
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }

            // -- International mobile equipment identities management services
        case equipmentMngtContext:
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

            // -- Subscriber Information services
        case anyTimeEnquiryContext:
        case anyTimeInfoHandlingContext:
        case subscriberInfoEnquiryContext:
            if (vers >= 3 && vers <= 3) {
                return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
            } else if (vers > 3) {
            	List<Long> altOid = dialogApplicationContext.getOID();
            	altOid.set(7,3L);
                ApplicationContextName alt = TcapFactory.createApplicationContextName(altOid);
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, alt);
            } else {
                return new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect);
            }

            // -- Subscriber Management services
        case subscriberDataMngtContext:
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

        // -- Location management services
        case MAPOperationCode.updateLocation:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.networkLocUpContext, MAPApplicationContextVersion.version1);

        case MAPOperationCode.cancelLocation:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.locationCancellationContext, MAPApplicationContextVersion.version1);

            // -- Authentication management services
        case MAPOperationCode.sendParameters:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.infoRetrievalContext, MAPApplicationContextVersion.version1);

            // -- Fault recovery services
        case MAPOperationCode.reset:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.resetContext, MAPApplicationContextVersion.version1);

            // -- IMEI services
        case MAPOperationCode.checkIMEI:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.equipmentMngtContext, MAPApplicationContextVersion.version1);

            // -- Subscriber Management services
        case MAPOperationCode.insertSubscriberData:
        case MAPOperationCode.deleteSubscriberData:
            return MAPApplicationContext.getInstance(MAPApplicationContextName.subscriberDataMngtContext, MAPApplicationContextVersion.version1);

        }

        return null;
    }

    @Override
    public void processComponent(ComponentType compType, OperationCode oc, MAPMessage parameter, MAPDialog mapDialog,
            Integer invokeId, Integer linkedId) throws MAPParsingComponentException {

    	Integer ocValue = oc.getLocalOperationCode();
        if (ocValue == null)
            new MAPParsingComponentException("", MAPParsingComponentExceptionReason.UnrecognizedOperation);
        MAPApplicationContextName acn = mapDialog.getApplicationContext().getApplicationContextName();
        int vers = mapDialog.getApplicationContext().getApplicationContextVersion().getVersion();
        int ocValueInt = (int) (long) ocValue;
        Boolean processed=false;
        switch (ocValueInt) {

        // -- Location management services
            case MAPOperationCode.updateLocation:
                if (acn == MAPApplicationContextName.networkLocUpContext) {
                	if(parameter instanceof UpdateLocationRequest) {
                		processed=true;
                		UpdateLocationRequest ind=(UpdateLocationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onUpdateLocationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing UpdateLocationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof UpdateLocationResponse) {
                		processed=true;
                		
                		if (vers >= 2 && parameter instanceof UpdateLocationResponseImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 UpdateLocationResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<2 && parameter instanceof UpdateLocationResponseImplV2)
                    		throw new MAPParsingComponentException(
                                    "Received V3 UpdateLocationResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	UpdateLocationResponse ind=(UpdateLocationResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onUpdateLocationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing RegisterSSRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.cancelLocation:
                if (acn == MAPApplicationContextName.locationCancellationContext) {
                	if(parameter instanceof CancelLocationRequest) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof CancelLocationRequestImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 CancelLocationRequest while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof CancelLocationRequestImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 CancelLocationRequest while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	CancelLocationRequest ind=(CancelLocationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onCancelLocationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing CancelLocationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof CancelLocationResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		CancelLocationResponse ind=null;
                		if(parameter!=null)
                			ind=(CancelLocationResponse)parameter;
                		else {
                			ind=new CancelLocationResponseImpl();
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onCancelLocationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing CancelLocationResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.sendIdentification:
                if (acn == MAPApplicationContextName.interVlrInfoRetrievalContext) {
                	if(parameter instanceof SendIdentificationRequest) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof SendIdentificationRequestImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 SendIdentificationRequest while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof SendIdentificationRequestImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 SendIdentificationRequest while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	SendIdentificationRequest ind=(SendIdentificationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onSendIdentificationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing SendIdentificationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof SendIdentificationResponse) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof SendIdentificationResponseImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 SendIdentificationResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof SendIdentificationResponseImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 SendIdentificationResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	SendIdentificationResponse ind=(SendIdentificationResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onSendIdentificationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing SendIdentificationResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.updateGprsLocation:
                if (acn == MAPApplicationContextName.gprsLocationUpdateContext) {
                	if(parameter instanceof UpdateGprsLocationRequest) {
                		processed=true;
                		UpdateGprsLocationRequest ind=(UpdateGprsLocationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onUpdateGprsLocationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing UpdateGprsLocationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof UpdateGprsLocationResponse) {
                		processed=true;
                		UpdateGprsLocationResponse ind=(UpdateGprsLocationResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onUpdateGprsLocationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing UpdateGprsLocationResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.purgeMS:
                if (acn == MAPApplicationContextName.msPurgingContext) {
                	if(parameter instanceof PurgeMSRequest) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof PurgeMSRequestImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 PurgeMSRequest while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof PurgeMSRequestImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 PurgeMSRequest while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	PurgeMSRequest ind=(PurgeMSRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onPurgeMSRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing PurgeMSRequestRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof PurgeMSResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		PurgeMSResponse ind=null;
                		if(parameter!=null)
                			ind=(PurgeMSResponse)parameter;
                		else {
                			ind=new PurgeMSResponseImpl();
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onPurgeMSResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing PurgeMSResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- Authentication management services
            case MAPOperationCode.sendAuthenticationInfo:
                if (acn == MAPApplicationContextName.infoRetrievalContext && vers >= 2) {
                	if(parameter instanceof SendAuthenticationInfoRequest || (parameter==null && compType == ComponentType.Invoke)) {
                		processed=true;
                		SendAuthenticationInfoRequest ind=null;
                		if(parameter!=null) {
                			if (vers >= 3 && parameter instanceof SendAuthenticationInfoRequestImplV1)
                        		throw new MAPParsingComponentException(
                                        "Received V1 SendAuthenticationInfoRequest while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	else if(vers<3 && parameter instanceof SendAuthenticationInfoRequestImplV3)
                        		throw new MAPParsingComponentException(
                                        "Received V3 SendAuthenticationInfoRequest while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	
                        	ind=(SendAuthenticationInfoRequest)parameter;
                		}
                		else {
                			if(vers<3)
                				 throw new MAPParsingComponentException(
                						 "Error while decoding sendAuthenticationInfoRequest V2: Parameter is mandatory but not found",
                						 MAPParsingComponentExceptionReason.MistypedParameter);
                			else
                				ind=new SendAuthenticationInfoRequestImplV3();
                			
                			ind.setInvokeId(invokeId);
                			ind.setMAPDialog(mapDialog);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onSendAuthenticationInfoRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing SendAuthenticationInfoRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof SendAuthenticationInfoResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		SendAuthenticationInfoResponse ind=null;
                		if(parameter!=null) {
                			if (vers >= 3 && parameter instanceof SendAuthenticationInfoResponseImplV1)
                        		throw new MAPParsingComponentException(
                                        "Received V1 SendAuthenticationInfoResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	else if(vers<3 && parameter instanceof SendAuthenticationInfoResponseImplV3)
                        		throw new MAPParsingComponentException(
                                        "Received V3 SendAuthenticationInfoResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                        	
                        	ind=(SendAuthenticationInfoResponse)parameter;
                		}
                		else {
                			if(vers<3)
                				ind=new SendAuthenticationInfoResponseImplV1();
                			else
                				ind=new SendAuthenticationInfoResponseImplV3();
                			
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onSendAuthenticationInfoResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing SendAuthenticationInfoResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.authenticationFailureReport:
                if (acn == MAPApplicationContextName.authenticationFailureReportContext && vers >= 3) {
                	if(parameter instanceof AuthenticationFailureReportRequest) {
                		processed=true;
                		AuthenticationFailureReportRequest ind=(AuthenticationFailureReportRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onAuthenticationFailureReportRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AuthenticationFailureReportRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof AuthenticationFailureReportResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		AuthenticationFailureReportResponse ind=null;
                		if(parameter!=null)
                			ind=(AuthenticationFailureReportResponse)parameter;
                		else {
                			ind=new AuthenticationFailureReportResponseImpl();
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                			
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onAuthenticationFailureReportResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AuthenticationFailureReportResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- Fault Recovery services
            case MAPOperationCode.reset:
                if (acn == MAPApplicationContextName.resetContext && vers <= 2) {
                	if(parameter instanceof ResetRequest) {
                		processed=true;
                		ResetRequest ind=(ResetRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onResetRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AuthenticationFailureReportRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.forwardCheckSsIndication:
                if (acn == MAPApplicationContextName.networkLocUpContext) {
                    if (parameter == null && compType == ComponentType.Invoke) {
                    	processed=true;
                		ForwardCheckSSIndicationRequestImpl ind = new ForwardCheckSSIndicationRequestImpl();
                		ind.setInvokeId(invokeId);
                		ind.setMAPDialog(mapDialog);
                		
                        for (MAPServiceListener serLis : this.serviceListeners) {
                            try {
                                serLis.onMAPMessage(ind);
                                ((MAPServiceMobilityListener) serLis).onForwardCheckSSIndicationRequest(ind);
                            } catch (Exception e) {
                                loger.error("Error processing forwardCheckSsIndicationRequest: " + e.getMessage(), e);
                            }
                        }    
                    }                    
                }
                break;
            case MAPOperationCode.restoreData:
                if (acn == MAPApplicationContextName.networkLocUpContext && vers >= 2) {
                	if(parameter instanceof RestoreDataRequest) {
                		processed=true;
                		RestoreDataRequest ind=(RestoreDataRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onRestoreDataRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing RestoreDataRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof RestoreDataResponse) {
                		processed=true;
                		RestoreDataResponse ind=(RestoreDataResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onRestoreDataResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing RestoreDataResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- Subscriber Information services
            case MAPOperationCode.anyTimeInterrogation:
                if (acn == MAPApplicationContextName.anyTimeEnquiryContext) {
                	if(parameter instanceof AnyTimeInterrogationRequest) {
                		processed=true;
                		AnyTimeInterrogationRequest ind=(AnyTimeInterrogationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onAnyTimeInterrogationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AnyTimeInterrogationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof AnyTimeInterrogationResponse) {
                		processed=true;
                		AnyTimeInterrogationResponse ind=(AnyTimeInterrogationResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onAnyTimeInterrogationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AnyTimeInterrogationResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.anyTimeSubscriptionInterrogation:
                if (acn == MAPApplicationContextName.anyTimeInfoHandlingContext) {
                	if(parameter instanceof AnyTimeSubscriptionInterrogationRequest) {
                		processed=true;
                		AnyTimeSubscriptionInterrogationRequest ind=(AnyTimeSubscriptionInterrogationRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onAnyTimeSubscriptionInterrogationRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AnyTimeSubscriptionInterrogationRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof AnyTimeSubscriptionInterrogationResponse) {
                		processed=true;
                		AnyTimeSubscriptionInterrogationResponse ind=(AnyTimeSubscriptionInterrogationResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onAnyTimeSubscriptionInterrogationResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing AnyTimeSubscriptionInterrogationResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.provideSubscriberInfo:
                if (acn == MAPApplicationContextName.subscriberInfoEnquiryContext) {
                	if(parameter instanceof ProvideSubscriberInfoRequest) {
                		processed=true;
                		ProvideSubscriberInfoRequest ind=(ProvideSubscriberInfoRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onProvideSubscriberInfoRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing ProvideSubscriberInfoRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof ProvideSubscriberInfoResponse) {
                		processed=true;
                		ProvideSubscriberInfoResponse ind=(ProvideSubscriberInfoResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onProvideSubscriberInfoResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing ProvideSubscriberInfoResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- IMEI services
            case MAPOperationCode.checkIMEI:
                if (acn == MAPApplicationContextName.equipmentMngtContext) {
                	if(parameter instanceof CheckImeiRequest) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof CheckImeiRequestImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 CheckImeiRequest while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof CheckImeiRequestImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 CheckImeiRequest while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	CheckImeiRequest ind=(CheckImeiRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onCheckImeiRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing CheckImeiRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof CheckImeiResponse) {
                		processed=true;
                		if (vers >= 3 && parameter instanceof CheckImeiResponseImplV1)
                    		throw new MAPParsingComponentException(
                                    "Received V1 CheckImeiResponse while version is >=3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	else if(vers<3 && parameter instanceof CheckImeiResponseImplV3)
                    		throw new MAPParsingComponentException(
                                    "Received V3 CheckImeiResponse while version is <3", MAPParsingComponentExceptionReason.MistypedParameter);
                    	
                    	CheckImeiResponse ind=(CheckImeiResponse)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onCheckImeiResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing ProvideSubscriberInfoResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- Subscriber management services
            case MAPOperationCode.insertSubscriberData:
                if (acn == MAPApplicationContextName.subscriberDataMngtContext
                        || acn == MAPApplicationContextName.networkLocUpContext
                        || acn == MAPApplicationContextName.gprsLocationUpdateContext) {
                	if(parameter instanceof InsertSubscriberDataRequest) {
                		processed=true;
                		InsertSubscriberDataRequest ind=(InsertSubscriberDataRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onInsertSubscriberDataRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing InsertSubscriberDataRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof InsertSubscriberDataResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		InsertSubscriberDataResponse ind=null;
                		if(parameter!=null)
                			ind=(InsertSubscriberDataResponse)parameter;
                		else {
                			if(vers<3)
                				ind=new InsertSubscriberDataResponseImplV1();
                			else
                				ind=new InsertSubscriberDataResponseImplV3();
                			
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onInsertSubscriberDataResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing InsertSubscriberDataResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;
            case MAPOperationCode.deleteSubscriberData:
                if (acn == MAPApplicationContextName.subscriberDataMngtContext) {
                	if(parameter instanceof DeleteSubscriberDataRequest) {
                		processed=true;
                		DeleteSubscriberDataRequest ind=(DeleteSubscriberDataRequest)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onDeleteSubscriberDataRequest(ind);
                			} catch (Exception e) {
                                loger.error("Error processing DeleteSubscriberDataRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof DeleteSubscriberDataResponse || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		DeleteSubscriberDataResponse ind=null;
                		if(parameter!=null)
                			ind=(DeleteSubscriberDataResponse)parameter;
                		else {
                			ind=new DeleteSubscriberDataResponseImpl();
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onDeleteSubscriberDataResponse(ind);
                			} catch (Exception e) {
                                loger.error("Error processing DeleteSubscriberDataResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            // -- OAM service: activateTraceMode operation can be present in
            // networkLocUpContext and gprsLocationUpdateContext application
            // contexts
            case MAPOperationCode.activateTraceMode:
                if (acn == MAPApplicationContextName.networkLocUpContext
                        || acn == MAPApplicationContextName.gprsLocationUpdateContext) {
                	if(parameter instanceof ActivateTraceModeRequest_Mobility) {
                		processed=true;
                		ActivateTraceModeRequest_Mobility ind=(ActivateTraceModeRequest_Mobility)parameter;
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
            	                serLis.onMAPMessage(ind);
            	                ((MAPServiceMobilityListener) serLis).onActivateTraceModeRequest_Mobility(ind);
                			} catch (Exception e) {
                                loger.error("Error processing ActivateTraceModeRequestIndication: " + e.getMessage(), e);
                            }
                        }
                	} else if(parameter instanceof ActivateTraceModeResponse_Mobility || (parameter == null && compType!= ComponentType.Invoke)) {
                		processed=true;
                		ActivateTraceModeResponse_Mobility ind=null;
                		if(parameter!=null)
                			ind=(ActivateTraceModeResponse_Mobility)parameter;
                		else {
                			ind=new ActivateTraceModeResponseImpl();
                			ind.setInvokeId(invokeId);
                            ind.setMAPDialog(mapDialog);
                            ind.setReturnResultNotLast(compType==ComponentType.ReturnResultLast);
                		}
                		
                		for (MAPServiceListener serLis : this.serviceListeners) {
                			try {
                				serLis.onMAPMessage(ind);
                				((MAPServiceMobilityListener) serLis).onActivateTraceModeResponse_Mobility(ind);
                			} catch (Exception e) {
                                loger.error("Error processing ActivateTraceModeResponseIndication: " + e.getMessage(), e);
                            }
                        }
                	}
                }
                break;

            default:
                throw new MAPParsingComponentException("MAPServiceMobility: unknown incoming operation code: " + ocValueInt,
                        MAPParsingComponentExceptionReason.UnrecognizedOperation);
        }
        
        if(!processed)
        	throw new MAPParsingComponentException("MAPServiceMobility: unknown incoming operation code: " + ocValueInt,
                    MAPParsingComponentExceptionReason.UnrecognizedOperation);
    }
}