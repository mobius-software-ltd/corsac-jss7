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

import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressString;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertReason;
import org.restcomm.protocols.ss7.map.api.service.sms.CorrelationID;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MAPServiceSms;
import org.restcomm.protocols.ss7.map.api.service.sms.MWStatus;
import org.restcomm.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryNotIntended;
import org.restcomm.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.restcomm.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.restcomm.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MAPDialogSmsImpl extends MAPDialogImpl implements MAPDialogSms {
	private static final long serialVersionUID = 1L;

	protected MAPDialogSmsImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceSms mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Long addForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend) throws MAPException {
        return addForwardShortMessageRequest(_Timer_Default, sm_RP_DA, sm_RP_OA, sm_RP_UI, moreMessagesToSend);
    }

    public Long addForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMORelayContext && this.appCntx
                .getApplicationContextName() != MAPApplicationContextName.shortMsgMTRelayContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addForwardShortMessageRequest: must be shortMsgMORelayContext_V1 or V2 or shortMsgMTRelayContext_V1 or V2");

        if (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            moreMessagesToSend = false;

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        MoForwardShortMessageRequestImpl req = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                moreMessagesToSend);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.mo_forwardSM, req, true, false);
    }

    public void addForwardShortMessageResponse(long invokeId) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMORelayContext && this.appCntx
                .getApplicationContextName() != MAPApplicationContextName.shortMsgMTRelayContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addForwardShortMessageResponse: must be shortMsgMORelayContext_V1 or V2 or shortMsgMTRelayContext_V1 or V2");

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.mo_forwardSM, null, false, true);
    }

    public Long addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException {
        return addMoForwardShortMessageRequest(_Timer_Default, sm_RP_DA, sm_RP_OA, sm_RP_UI, extensionContainer, imsi);
    }

    public Long addMoForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMORelayContext
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addMoForwardShortMessageRequest: must be shortMsgMORelayContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        MoForwardShortMessageRequestImpl req = new MoForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                extensionContainer, imsi);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.mo_forwardSM, req, true, false);
    }

    public void addMoForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMORelayContext
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addMoForwardShortMessageResponse: must be shortMsgMORelayContext_V3");

        MoForwardShortMessageResponseImpl req = null;
        if (sm_RP_UI != null || extensionContainer != null)
            req = new MoForwardShortMessageResponseImpl(sm_RP_UI, extensionContainer);

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.mo_forwardSM, req, false, true);
    }

    public Long addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException {
        return this.addMtForwardShortMessageRequest(_Timer_Default, sm_RP_DA, sm_RP_OA, sm_RP_UI, moreMessagesToSend,
                extensionContainer);
    }

    public Long addMtForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMTRelayContext
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addMtForwardShortMessageRequest: must be shortMsgMTRelayContext_V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        MtForwardShortMessageRequestImpl req = new MtForwardShortMessageRequestImpl(sm_RP_DA, sm_RP_OA, sm_RP_UI,
                moreMessagesToSend, extensionContainer);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.mt_forwardSM, req, true, false);
    }

    public void addMtForwardShortMessageResponse(long invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgMTRelayContext
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addMtForwardShortMessageResponse: must be shortMsgMTRelayContext_V3");

        MtForwardShortMessageResponseImpl resp=null;
        if (sm_RP_UI != null || extensionContainer != null)
            resp = new MtForwardShortMessageResponseImpl(sm_RP_UI, extensionContainer);
        
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.mt_forwardSM, resp, false, true);
    }

    public Long addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, boolean sm_RP_PRI, AddressString serviceCentreAddress,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA,
            SMDeliveryNotIntended smDeliveryNotIntended, boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator,
            boolean singleAttemptDelivery, TeleserviceCode teleservice, CorrelationID correlationId) throws MAPException {
        return this.addSendRoutingInfoForSMRequest(_Timer_Default, msisdn, sm_RP_PRI, serviceCentreAddress, extensionContainer,
                gprsSupportIndicator, sM_RP_MTI, sM_RP_SMEA, smDeliveryNotIntended, ipSmGwGuidanceIndicator, imsi,
                t4TriggerIndicator, singleAttemptDelivery, teleservice, correlationId);
    }

    public Long addSendRoutingInfoForSMRequest(int customInvokeTimeout, ISDNAddressString msisdn, boolean sm_RP_PRI,
    		AddressString serviceCentreAddress, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator,
            SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA, SMDeliveryNotIntended smDeliveryNotIntended,
            boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator, boolean singleAttemptDelivery,
            TeleserviceCode teleservice, CorrelationID correlationId) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForSMRequest: must be shortMsgGatewayContext_V1, V2 or V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        SendRoutingInfoForSMRequestImpl req = new SendRoutingInfoForSMRequestImpl(msisdn, sm_RP_PRI, serviceCentreAddress,
                extensionContainer, gprsSupportIndicator, sM_RP_MTI, sM_RP_SMEA, smDeliveryNotIntended,
                ipSmGwGuidanceIndicator, imsi, t4TriggerIndicator, singleAttemptDelivery, teleservice, correlationId);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.sendRoutingInfoForSM, req, true, false);
    }

    public void addSendRoutingInfoForSMResponse(long invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI,
            MAPExtensionContainer extensionContainer, Boolean mwdSet, IpSmGwGuidance ipSmGwGuidance) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addSendRoutingInfoForSMResponse: must be shortMsgGatewayContext_V1, V2 or V3");

        SendRoutingInfoForSMResponseImpl resp = new SendRoutingInfoForSMResponseImpl(imsi, locationInfoWithLMSI,
                extensionContainer, mwdSet, ipSmGwGuidance);
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.sendRoutingInfoForSM, resp, false, true);
    }

    public Long addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress,
            SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
        return this.addReportSMDeliveryStatusRequest(_Timer_Default, msisdn, serviceCentreAddress, sMDeliveryOutcome,
                absentSubscriberDiagnosticSM, extensionContainer, gprsSupportIndicator, deliveryOutcomeIndicator,
                additionalSMDeliveryOutcome, additionalAbsentSubscriberDiagnosticSM);
    }

    public Long addReportSMDeliveryStatusRequest(int customInvokeTimeout, ISDNAddressString msisdn,
    		AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addReportSMDeliveryStatusRequest: must be shortMsgGatewayContext_V1, V2 or V3");

        if (msisdn == null || serviceCentreAddress == null
                || (vers != MAPApplicationContextVersion.version1 && sMDeliveryOutcome == null))
            throw new MAPException("msisdn, serviceCentreAddress and sMDeliveryOutcome must not be null");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getShortTimer();
        else
        	customTimeout=customInvokeTimeout;

        ReportSMDeliveryStatusRequestImpl req = new ReportSMDeliveryStatusRequestImpl(msisdn, serviceCentreAddress, sMDeliveryOutcome,
                absentSubscriberDiagnosticSM, extensionContainer, gprsSupportIndicator, deliveryOutcomeIndicator,
                additionalSMDeliveryOutcome, additionalAbsentSubscriberDiagnosticSM);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.reportSM_DeliveryStatus, req, true, false);            
    }

    public void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || (vers != MAPApplicationContextVersion.version1 && vers != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addReportSMDeliveryStatusResponse: must be shortMsgGatewayContext_V1, V2");

        ReportSMDeliveryStatusResponse resp=null;
        if(storedMSISDN != null)
            resp = new ReportSMDeliveryStatusResponseImplV1(vers.getVersion(), storedMSISDN);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.reportSM_DeliveryStatus, resp, false, true);
    }

    public void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressString storedMSISDN,
            MAPExtensionContainer extensionContainer) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || vers != MAPApplicationContextVersion.version3)
            throw new MAPException(
                    "Bad application context name for addReportSMDeliveryStatusResponse: must be shortMsgGatewayContext_V3");

        ReportSMDeliveryStatusResponse resp=null;
        if (storedMSISDN != null || extensionContainer != null)
            resp = new ReportSMDeliveryStatusResponseImplV3(vers.getVersion(), storedMSISDN,extensionContainer);
            
        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.reportSM_DeliveryStatus, resp, false, true);
    }

    public Long addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {
        return this.addInformServiceCentreRequest(_Timer_Default, storedMSISDN, mwStatus, extensionContainer,
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
    }

    public Long addInformServiceCentreRequest(int customInvokeTimeout, ISDNAddressString storedMSISDN, MWStatus mwStatus,
            MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException {

        MAPApplicationContextVersion vers = this.appCntx.getApplicationContextVersion();
        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgGatewayContext
                || (vers != MAPApplicationContextVersion.version2 && vers != MAPApplicationContextVersion.version3))
            throw new MAPException(
                    "Bad application context name for addInformServiceCentreRequest: must be shortMsgGatewayContext_V2 or V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getShortTimer();
        else
        	customTimeout=customInvokeTimeout;

        InformServiceCentreRequestImpl req = new InformServiceCentreRequestImpl(storedMSISDN, mwStatus, extensionContainer,
                absentSubscriberDiagnosticSM, additionalAbsentSubscriberDiagnosticSM);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) MAPOperationCode.informServiceCentre, req, true, false);
    }

    public Long addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress) throws MAPException {
        return this.addAlertServiceCentreRequest(_Timer_Default, msisdn, serviceCentreAddress);
    }

    public Long addAlertServiceCentreRequest(int customInvokeTimeout, ISDNAddressString msisdn,
    		AddressString serviceCentreAddress) throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgAlertContext
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1 && this.appCntx
                        .getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addAlertServiceCentreRequest: must be shortMsgAlertContext_V1 or V2");

        InvokeClass invokeClass=null;
        if (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1)
        	invokeClass=InvokeClass.Class4;

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getShortTimer();
        else
        	customTimeout=customInvokeTimeout;

        Long oc = null;
        if (this.appCntx.getApplicationContextVersion() == MAPApplicationContextVersion.version1)
        	oc=(long) MAPOperationCode.alertServiceCentreWithoutResult;
        else
        	oc=(long) MAPOperationCode.alertServiceCentre;
        
        AlertServiceCentreRequestImpl req = new AlertServiceCentreRequestImpl(msisdn, serviceCentreAddress);
        return this.sendDataComponent(null, null, invokeClass, customTimeout.longValue(), oc, req, true, false);
    }

    public void addAlertServiceCentreResponse(long invokeId) throws MAPException {

        if (this.appCntx.getApplicationContextName() != MAPApplicationContextName.shortMsgAlertContext
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2))
            throw new MAPException(
                    "Bad application context name for addAlertServiceCentreResponse: must be shortMsgAlertContext_V2");

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.alertServiceCentre, null, false, true);
    }

    @Override
    public Long addReadyForSMRequest(IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainer extensionContainer,
            boolean additionalAlertReasonIndicator) throws MAPException {
        return addReadyForSMRequest(_Timer_Default, imsi, alertReason, alertReasonIndicator, extensionContainer, additionalAlertReasonIndicator);
    }

    @Override
    public Long addReadyForSMRequest(int customInvokeTimeout, IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator,
            MAPExtensionContainer extensionContainer, boolean additionalAlertReasonIndicator) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.mwdMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for addReadyForSMRequest: must be mwdMngtContext_V2 or V3");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        ReadyForSMRequestImpl req = new ReadyForSMRequestImpl(imsi, alertReason, alertReasonIndicator, extensionContainer, additionalAlertReasonIndicator);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), (long) MAPOperationCode.readyForSM, req, true, false);      
    }

    @Override
    public void addReadyForSMResponse(long invokeId, MAPExtensionContainer extensionContainer) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.mwdMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2 && this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version3))
            throw new MAPException("Bad application context name for addReadyForSMRequest: must be mwdMngtContext_V2 or V3");

        ReadyForSMResponseImpl req=null;
        if (this.appCntx.getApplicationContextVersion().getVersion() >= 3 || extensionContainer != null)
            req = new ReadyForSMResponseImpl(extensionContainer);

        this.sendDataComponent(invokeId, null, null, null, (long) MAPOperationCode.readyForSM, req, false, true);
    }

    @Override
    public Long addNoteSubscriberPresentRequest(IMSI imsi) throws MAPException {
        return addNoteSubscriberPresentRequest(_Timer_Default, imsi);
    }

    @Override
    public Long addNoteSubscriberPresentRequest(int customInvokeTimeout, IMSI imsi) throws MAPException {

        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.mwdMngtContext)
                || (this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version1))
            throw new MAPException("Bad application context name for addNoteSubscriberPresentRequest: must be mwdMngtContext_V1");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getShortTimer();
        else
        	customTimeout = customInvokeTimeout;

        NoteSubscriberPresentRequestImpl req = new NoteSubscriberPresentRequestImpl(imsi);
        return this.sendDataComponent(null, null, InvokeClass.Class4, customTimeout.longValue(), (long) MAPOperationCode.noteSubscriberPresent, req, true, false);
    }
}
