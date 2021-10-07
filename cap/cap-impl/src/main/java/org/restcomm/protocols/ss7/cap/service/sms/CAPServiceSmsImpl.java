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

package org.restcomm.protocols.ss7.cap.service.sms;

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
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPDialogSms;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSms;
import org.restcomm.protocols.ss7.cap.api.service.sms.CAPServiceSmsListener;
import org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ContinueSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.InitialDPSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.cap.dialog.ServingCheckDataImpl;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;
import org.restcomm.protocols.ss7.tcap.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcap.asn.comp.OperationCode;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CAPServiceSmsImpl extends CAPServiceBaseImpl implements CAPServiceSms {

    protected Logger loger = Logger.getLogger(CAPServiceSmsImpl.class);

    public CAPServiceSmsImpl(CAPProviderImpl capProviderImpl) {
        super(capProviderImpl);
    }

    @Override
    public CAPDialogSms createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress, SccpAddress destAddress)
            throws CAPException {
        return this.createNewDialog(appCntx, origAddress, destAddress, null);
    }

    @Override
    public CAPDialogSms createNewDialog(CAPApplicationContext appCntx, SccpAddress origAddress,
            SccpAddress destAddress, Long localTrId) throws CAPException {

        // We cannot create a dialog if the service is not activated
        if (!this.isActivated())
            throw new CAPException("Cannot create CAPDialogSms because CAPServiceSms is not activated");

        Dialog tcapDialog = this.createNewTCAPDialog(origAddress, destAddress, localTrId);
        CAPDialogSmsImpl dialog = new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);

        this.putCAPDialogIntoCollection(dialog);

        return dialog;
    }

    @Override
    public void addCAPServiceListener(CAPServiceSmsListener capServiceListener) {
        super.addCAPServiceListener(capServiceListener);
    }

    @Override
    public void removeCAPServiceListener(CAPServiceSmsListener capServiceListener) {
        super.removeCAPServiceListener(capServiceListener);
    }

    @Override
    protected CAPDialogImpl createNewDialogIncoming(CAPApplicationContext appCntx, Dialog tcapDialog) {
        return new CAPDialogSmsImpl(appCntx, tcapDialog, this.capProviderImpl, this);
    }

    @Override
    public ServingCheckData isServingService(CAPApplicationContext dialogApplicationContext) {
        switch (dialogApplicationContext) {
	        case CapV3_cap3_sms:
	        case CapV4_cap4_sms:
	            return new ServingCheckDataImpl(ServingCheckResult.AC_Serving);
			default:
				break;
        }

        return new ServingCheckDataImpl(ServingCheckResult.AC_NotServing);
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
        	case CAPOperationCode.connectSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof ConnectSMSRequest) {
        				processed = true;
        				ConnectSMSRequest ind = (ConnectSMSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onConnectSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.eventReportSMS:
	            if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
	            	if(parameter instanceof EventReportSMSRequest) {
	    				processed = true;
	    				EventReportSMSRequest ind = (EventReportSMSRequest)parameter;
	    	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onEventReportSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
	    	        }
	            }
	            break;
        	case CAPOperationCode.furnishChargingInformationSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof FurnishChargingInformationSMSRequest) {
        				processed = true;
        				FurnishChargingInformationSMSRequest ind = (FurnishChargingInformationSMSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onFurnishChargingInformationSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.initialDPSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof InitialDPSMSRequest) {
        				processed = true;
        				InitialDPSMSRequest ind = (InitialDPSMSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onInitialDPSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.releaseSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof ReleaseSMSRequest) {
        				processed = true;
        				ReleaseSMSRequest ind = (ReleaseSMSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onReleaseSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.requestReportSMSEvent:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof RequestReportSMSEventRequest) {
        				processed = true;
        				RequestReportSMSEventRequest ind = (RequestReportSMSEventRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onRequestReportSMSEventRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.resetTimerSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if(parameter instanceof ResetTimerSMSRequest) {
        				processed = true;
        				ResetTimerSMSRequest ind = (ResetTimerSMSRequest)parameter;
        	        	
	        	        for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onResetTimerSMSRequest(ind);
	        	            } catch (Exception e) {
	        	                loger.error("Error processing connectSMSRequest: " + e.getMessage(), e);
	        	            }
	        	        }
        	        }
        		}
        		break;
        	case CAPOperationCode.continueSMS:
        		if (acn == CAPApplicationContext.CapV3_cap3_sms || acn == CAPApplicationContext.CapV4_cap4_sms) {
        			if (compType == ComponentType.Invoke && parameter == null) {
        				processed = true;
        				ContinueSMSRequest ind=new ContinueSMSRequestImpl();
        				
        				for (CAPServiceListener serLis : this.serviceListeners) {
	        	            try {
	        	                serLis.onCAPMessage(ind);
	        	                ((CAPServiceSmsListener) serLis).onContinueSMSRequest(ind);
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
