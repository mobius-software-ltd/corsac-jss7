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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.MAPDialog;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.AlertingPatternImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.USSDStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface MAPDialogSupplementary extends MAPDialog {

    Long addRegisterSSRequest(SSCodeImpl ssCode, BasicServiceCodeImpl basicService, AddressStringImpl forwardedToNumber, ISDNAddressStringImpl forwardedToSubaddress,
            Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser, ISDNAddressStringImpl longFTNSupported) throws MAPException;

    Long addRegisterSSRequest(int customInvokeTimeout, SSCodeImpl ssCode, BasicServiceCodeImpl basicService, AddressStringImpl forwardedToNumber,
            ISDNAddressStringImpl forwardedToSubaddress, Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser,
            ISDNAddressStringImpl longFTNSupported) throws MAPException;

    void addRegisterSSResponse(long invokeId, SSInfoImpl ssInfo) throws MAPException;

    Long addEraseSSRequest(SSForBSCodeImpl ssForBSCode) throws MAPException;

    Long addEraseSSRequest(int customInvokeTimeout, SSForBSCodeImpl ssForBSCode) throws MAPException;

    void addEraseSSResponse(long invokeId, SSInfoImpl ssInfo) throws MAPException;

    Long addActivateSSRequest(SSForBSCodeImpl ssForBSCode) throws MAPException;

    Long addActivateSSRequest(int customInvokeTimeout, SSForBSCodeImpl ssForBSCode) throws MAPException;

    void addActivateSSResponse(long invokeId, SSInfoImpl ssInfo) throws MAPException;

    Long addDeactivateSSRequest(SSForBSCodeImpl ssForBSCode) throws MAPException;

    Long addDeactivateSSRequest(int customInvokeTimeout, SSForBSCodeImpl ssForBSCode) throws MAPException;

    void addDeactivateSSResponse(long invokeId, SSInfoImpl ssInfo) throws MAPException;

    Long addInterrogateSSRequest(SSForBSCodeImpl ssForBSCode) throws MAPException;

    Long addInterrogateSSRequest(int customInvokeTimeout, SSForBSCodeImpl ssForBSCode) throws MAPException;

    void addInterrogateSSResponse_SSStatus(long invokeId, SSStatusImpl ssStatus) throws MAPException;

    void addInterrogateSSResponse_BasicServiceGroupList(long invokeId, ArrayList<BasicServiceCodeImpl> basicServiceGroupList) throws MAPException;

    void addInterrogateSSResponse_ForwardingFeatureList(long invokeId, ArrayList<ForwardingFeatureImpl> forwardingFeatureList) throws MAPException;

    void addInterrogateSSResponse_GenericServiceInfo(long invokeId, GenericServiceInfoImpl genericServiceInfo) throws MAPException;

    Long addGetPasswordRequest(Long linkedId, GuidanceInfo guidanceInfo) throws MAPException;

    Long addGetPasswordRequest(int customInvokeTimeout, Long linkedId, GuidanceInfo guidanceInfo) throws MAPException;

    void addGetPasswordResponse(long invokeId, PasswordImpl password) throws MAPException;

    Long addRegisterPasswordRequest(SSCodeImpl ssCode) throws MAPException;

    Long addRegisterPasswordRequest(int customInvokeTimeout, SSCodeImpl ssCode) throws MAPException;

    void addRegisterPasswordResponse(long invokeId, PasswordImpl password) throws MAPException;


    /**
     * Add's a new Process Unstructured SS Request as Component.
     *
     * @param ussdDataCodingScheme The Data Coding Scheme for this USSD String as defined in GSM 03.38
     * @param ussdString Ussd String
     * @param alertingPatter The optional alerting pattern. See {@link AlertingPattern}
     * @param msisdn The optional MSISDN in {@link ISDNAddressStringImpl} format.
     * @return invokeId
     * @throws MAPException
     */
    Long addProcessUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString,
            AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    Long addProcessUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    /**
     * Add's a new ProcessUnstructured SS Response as Component.
     *
     * @param invokeId The original invoke ID retrieved from {@link ProcessUnstructuredSSResponse}
     * @param ussdDataCodingScheme The Data Coding Scheme for this USSD String as defined in GSM 03.38
     * @param ussdString Ussd String {@link USSDString}
     * @throws MAPException
     */
    void addProcessUnstructuredSSResponse(long invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString)
            throws MAPException;

    /**
     * Add's a new Unstructured SS Request
     *
     * @param ussdDataCodingScheme The Data Coding Scheme for this USSD String as defined in GSM 03.38
     * @param ussdString Ussd String {@link USSDString}
     * @param alertingPatter The optional alerting pattern. See {@link AlertingPattern}
     * @param msisdn The optional MSISDN in {@link ISDNAddressStringImpl} format.
     * @return invokeId
     * @throws MAPException
     */
    Long addUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString,
            AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    Long addUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    /**
     * Add's a new Unstructured SS Response
     *
     * @param invokeId The original invoke ID retrieved from {@link UnstructuredSSResponse}
     * @param ussdDataCodingScheme The Data Coding Scheme for this USSD String as defined in GSM 03.38
     * @param ussdString Ussd String {@link USSDString}
     * @throws MAPException
     */
    void addUnstructuredSSResponse(long invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString)
            throws MAPException;

    /**
     * Add's a new Unstructured SS Notify
     *
     * @param ussdDataCodingScheme The Data Coding Scheme for this USSD String as defined in GSM 03.38
     * @param ussdString Ussd String {@link USSDString}
     * @param alertingPatter The optional alerting pattern. See {@link AlertingPattern}
     * @param msisdn The optional MSISDN in {@link ISDNAddressStringImpl} format.
     * @return invokeId
     * @throws MAPException
     */
    Long addUnstructuredSSNotifyRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDStringImpl ussdString,
            AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    Long addUnstructuredSSNotifyRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
    		USSDStringImpl ussdString, AlertingPatternImpl alertingPatter, ISDNAddressStringImpl msisdn) throws MAPException;

    void addUnstructuredSSNotifyResponse(long invokeId) throws MAPException;

}