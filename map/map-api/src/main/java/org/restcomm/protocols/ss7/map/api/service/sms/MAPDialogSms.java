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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TeleserviceCodeImpl;

/**
 *
 * @author sergey vetyutnev
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
    Long addForwardShortMessageRequest(SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA, SmsSignalInfoImpl sm_RP_UI,
            boolean moreMessagesToSend) throws MAPException;

    Long addForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA,
            SmsSignalInfoImpl sm_RP_UI, boolean moreMessagesToSend) throws MAPException;

    /**
     * Sending MAP-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addForwardShortMessageResponse(long invokeId) throws MAPException;

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
    Long addMoForwardShortMessageRequest(SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA, SmsSignalInfoImpl sm_RP_UI,
    		MAPExtensionContainerImpl extensionContainer, IMSIImpl imsi) throws MAPException;

    Long addMoForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA,
            SmsSignalInfoImpl sm_RP_UI, MAPExtensionContainerImpl extensionContainer, IMSIImpl imsi) throws MAPException;

    /**
     * Sending MAP-MO-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMoForwardShortMessageResponse(long invokeId, SmsSignalInfoImpl sm_RP_UI, MAPExtensionContainerImpl extensionContainer)
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
    Long addMtForwardShortMessageRequest(SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA, SmsSignalInfoImpl sm_RP_UI,
            boolean moreMessagesToSend, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addMtForwardShortMessageRequest(int customInvokeTimeout, SM_RP_DAImpl sm_RP_DA, SM_RP_OAImpl sm_RP_OA,
            SmsSignalInfoImpl sm_RP_UI, boolean moreMessagesToSend, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    /**
     * Sending MAP-MT-FORWARD-SHORT-MESSAGE response
     *
     * @param invokeId
     * @param sm_RP_UI optional
     * @param extensionContainer optional
     * @throws MAPException
     */
    void addMtForwardShortMessageResponse(long invokeId, SmsSignalInfoImpl sm_RP_UI, MAPExtensionContainerImpl extensionContainer)
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
    Long addSendRoutingInfoForSMRequest(ISDNAddressStringImpl msisdn, boolean sm_RP_PRI, AddressStringImpl serviceCentreAddress,
    		MAPExtensionContainerImpl extensionContainer, boolean gprsSupportIndicator, SM_RP_MTI sM_RP_MTI, SM_RP_SMEAImpl sM_RP_SMEA,
            SMDeliveryNotIntended smDeliveryNotIntended, boolean ipSmGwGuidanceIndicator, IMSIImpl imsi, boolean t4TriggerIndicator,
            boolean singleAttemptDelivery, TeleserviceCodeImpl teleservice, CorrelationIDImpl correlationID) throws MAPException;

    Long addSendRoutingInfoForSMRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn, boolean sm_RP_PRI,
            AddressStringImpl serviceCentreAddress, MAPExtensionContainerImpl extensionContainer, boolean gprsSupportIndicator,
            SM_RP_MTI sM_RP_MTI, SM_RP_SMEAImpl sM_RP_SMEA, SMDeliveryNotIntended smDeliveryNotIntended,
            boolean ipSmGwGuidanceIndicator, IMSIImpl imsi, boolean t4TriggerIndicator, boolean singleAttemptDelivery,
            TeleserviceCodeImpl teleservice, CorrelationIDImpl correlationID) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param imsi mandatory
     * @param locationInfoWithLMSI mandatory
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addSendRoutingInfoForSMResponse(long invokeId, IMSIImpl imsi, LocationInfoWithLMSIImpl locationInfoWithLMSI,
    		MAPExtensionContainerImpl extensionContainer, Boolean mwdSet, IpSmGwGuidanceImpl ipSmGwGuidance) throws MAPException;

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
    Long addReportSMDeliveryStatusRequest(ISDNAddressStringImpl msisdn, AddressStringImpl serviceCentreAddress,
            SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainerImpl extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addReportSMDeliveryStatusRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn,
            AddressStringImpl serviceCentreAddress, SMDeliveryOutcome sMDeliveryOutcome, Integer absentSubscriberDiagnosticSM,
            MAPExtensionContainerImpl extensionContainer, boolean gprsSupportIndicator, boolean deliveryOutcomeIndicator,
            SMDeliveryOutcome additionalSMDeliveryOutcome, Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @param storedMSISDN optional
     * @return
     * @throws MAPException
     */
    void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressStringImpl storedMSISDN) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @param storedMSISDN optional
     * @param extensionContainer optional
     * @return
     * @throws MAPException
     */
    void addReportSMDeliveryStatusResponse(long invokeId, ISDNAddressStringImpl storedMSISDN,
    		MAPExtensionContainerImpl extensionContainer) throws MAPException;

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
    Long addInformServiceCentreRequest(ISDNAddressStringImpl storedMSISDN, MWStatusImpl mwStatus,
    		MAPExtensionContainerImpl extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    Long addInformServiceCentreRequest(int customInvokeTimeout, ISDNAddressStringImpl storedMSISDN, MWStatusImpl mwStatus,
    		MAPExtensionContainerImpl extensionContainer, Integer absentSubscriberDiagnosticSM,
            Integer additionalAbsentSubscriberDiagnosticSM) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM request
     *
     * @param msisdn mandatory
     * @param serviceCentreAddress mandatory
     * @return
     * @throws MAPException
     */
    Long addAlertServiceCentreRequest(ISDNAddressStringImpl msisdn, AddressStringImpl serviceCentreAddress) throws MAPException;

    Long addAlertServiceCentreRequest(int customInvokeTimeout, ISDNAddressStringImpl msisdn,
            AddressStringImpl serviceCentreAddress) throws MAPException;

    /**
     * Sending MAP-SEND-ROUTING-INFO-FOR-SM response
     *
     * @param invokeId
     * @throws MAPException
     */
    void addAlertServiceCentreResponse(long invokeId) throws MAPException;


    Long addReadyForSMRequest(IMSIImpl imsi, AlertReason alertReason, boolean alertReasonIndicator, MAPExtensionContainerImpl extensionContainer,
            boolean additionalAlertReasonIndicator) throws MAPException;

    Long addReadyForSMRequest(int customInvokeTimeout, IMSIImpl imsi, AlertReason alertReason, boolean alertReasonIndicator,
    		MAPExtensionContainerImpl extensionContainer, boolean additionalAlertReasonIndicator) throws MAPException;

    void addReadyForSMResponse(long invokeId, MAPExtensionContainerImpl extensionContainer) throws MAPException;

    Long addNoteSubscriberPresentRequest(IMSIImpl imsi) throws MAPException;

    Long addNoteSubscriberPresentRequest(int customInvokeTimeout, IMSIImpl imsi) throws MAPException;

}
