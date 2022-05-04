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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCode;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MAPDialogSms extends MAPDialog {

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional, default: false
     * @return invokeId
     * @throws MAPException
     */
    Integer addForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend) throws MAPException;

    Integer addForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend) throws MAPException;

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addForwardShortMessageResponse(int invokeId) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param extensionContainer optional
     * @param imsi optional
     * @return invokeId
     * @throws MAPException
     */
    Integer addMoForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
    		MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    Integer addMoForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer, IMSI imsi) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMoForwardShortMessageResponse(int invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE request
     *
     * @param sm_RP_DA mandatory
     * @param sm_RP_OA mandatory
     * @param sm_RP_UI mandatory
     * @param moreMessagesToSend optional
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    Integer addMtForwardShortMessageRequest(SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA, SmsSignalInfo sm_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addMtForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DA sm_RP_DA, SM_RP_OA sm_RP_OA,
            SmsSignalInfo sm_RP_UI, boolean moreMessagesToSend, MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMtForwardShortMessageResponse(int invokeId, SmsSignalInfo sm_RP_UI, MAPExtensionContainer extensionContainer)
            throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param sm_RP_PRI mandatory
     * @param serviceCentreAddress mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param sM_RP_MTI optional
     * @param sM_RP_SMEA optional
     * @return
     * @throws MAPException
     */
    Integer addSendRoutingInfoForSMRequest(ISDNAddressString msisdn, boolean sm_RP_PRI, AddressString serviceCentreAddress,
    		MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA,
            SMDeliveryNotIntended smDeliveryNotIntended, boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator,
            boolean singleAttemptDelivery, TeleserviceCode teleservice, CorrelationID correlationID) throws MAPException;

    Integer addSendRoutingInfoForSMRequest(int customInvokeTimeout, ISDNAddressString msisdn, boolean sm_RP_PRI,
    		AddressString serviceCentreAddress, MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator,
            SM_RP_MTI sM_RP_MTI, SM_RP_SMEA sM_RP_SMEA, SMDeliveryNotIntended smDeliveryNotIntended,
            boolean ipSmGwGuidanceIndicator, IMSI imsi, boolean t4TriggerIndicator, boolean singleAttemptDelivery,
            TeleserviceCode teleservice, CorrelationID correlationID) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param imsi mandatory
     * @param locationInfoWithLMSI mandatory
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addSendRoutingInfoForSMResponse(int invokeId, IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI,
    		MAPExtensionContainer extensionContainer, Boolean mwdSet, IpSmGwGuidance ipSmGwGuidance) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @param sMDeliveryOutcome mandatory
     * @param absentSubscriberDiagnosticSM mandatory
     * @param extensionContainer optional
     * @param gprsSupportIndicator optional
     * @param deliveryOutcomeIndicator optional
     * @param additionalSMDeliveryOutcome optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @return
     * @throws MAPException
     */
    Integer addReportSMDeliveryStatusRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress,
            SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Integer addReportSMDeliveryStatusRequest(int customInvokeTimeout, ISDNAddressString msisdn,
    		AddressString serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainer extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @param storedMSISDN optional
     * @return
     * @throws MAPException
     */
    void addReportSMDeliveryStatusResponse(int invokeId, ISDNAddressString storedMSISDN) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @param storedMSISDN optional
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addReportSMDeliveryStatusResponse(int invokeId, ISDNAddressString storedMSISDN,
    		MAPExtensionContainer extensionContainer) throws MAPException;

    /**
     * Sending MAP-INFORM-SERVICE-CENTRE request
     *
     * @param storedMSISDN optional
     * @param mwStatus optional
     * @param extensionContainer optional
     * @param absentSubscriberDiagnosticSM optional
     * @param additionalAbsentSubscriberDiagnosticSM optional
     * @return
     * @throws MAPException
     */
    Integer addInformServiceCentreRequest(ISDNAddressString storedMSISDN, MWStatus mwStatus,
    		MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Integer addInformServiceCentreRequest(int customInvokeTimeout, ISDNAddressString storedMSISDN, MWStatus mwStatus,
    		MAPExtensionContainer extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @return
     * @throws MAPException
     */
    Integer addAlertServiceCentreRequest(ISDNAddressString msisdn, AddressString serviceCentreAddress) throws MAPException;

    Integer addAlertServiceCentreRequest(int customInvokeTimeout, ISDNAddressString msisdn,
    		AddressString serviceCentreAddress) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addAlertServiceCentreResponse(int invokeId) throws MAPException;


    Integer addReadyForSMRequest(IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainer extensionContainer,
            boolean additionalAlertReasonIndicator) throws MAPException;

    Integer addReadyForSMRequest(int customInvokeTimeout, IMSI imsi, AlertReason alertReason, boolean alertReasonIndicator,
    		MAPExtensionContainer extensionContainer, boolean additionalAlertReasonIndicator) throws MAPException;

    void addReadyForSMResponse(int invokeId, MAPExtensionContainer extensionContainer) throws MAPException;

    Integer addNoteSubscriberPresentRequest(IMSI imsi) throws MAPException;

    Integer addNoteSubscriberPresentRequest(int customInvokeTimeout, IMSI imsi) throws MAPException;

}
