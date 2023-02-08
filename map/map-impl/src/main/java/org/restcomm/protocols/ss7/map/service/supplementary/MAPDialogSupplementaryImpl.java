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

package org.restcomm.protocols.ss7.map.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.MAPDialogImpl;
import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContext;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextName;
import org.restcomm.protocols.ss7.map.api.MAPApplicationContextVersion;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GenericServiceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.GuidanceInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.Dialog;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MAPDialogSupplementaryImpl extends MAPDialogImpl implements MAPDialogSupplementary {
	private static final long serialVersionUID = 1L;

	protected MAPDialogSupplementaryImpl(MAPApplicationContext appCntx, Dialog tcapDialog, MAPProviderImpl mapProviderImpl,
            MAPServiceSupplementary mapService, AddressString origReference, AddressString destReference) {
        super(appCntx, tcapDialog, mapProviderImpl, mapService, origReference, destReference);
    }

    public Integer addProcessUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addProcessUnstructuredSSRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }


    @Override
    public Integer addRegisterSSRequest(SSCode ssCode, BasicServiceCode basicService, AddressString forwardedToNumber, ISDNAddressString forwardedToSubaddress,
            Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser, ISDNAddressString longFTNSupported) throws MAPException {
        return this.addRegisterSSRequest(_Timer_Default, ssCode, basicService, forwardedToNumber, forwardedToSubaddress, noReplyConditionTime, defaultPriority,
                nbrUser, longFTNSupported);
    }

    @Override
    public Integer addRegisterSSRequest(int customInvokeTimeout, SSCode ssCode, BasicServiceCode basicService, AddressString forwardedToNumber,
            ISDNAddressString forwardedToSubaddress, Integer noReplyConditionTime, EMLPPPriority defaultPriority, Integer nbrUser,
            ISDNAddressString longFTNSupported) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterSSRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        RegisterSSRequestImpl req = new RegisterSSRequestImpl(ssCode, basicService, forwardedToNumber, forwardedToSubaddress, noReplyConditionTime,
                defaultPriority, nbrUser, longFTNSupported);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.registerSS, req, true, false);
    }

    @Override
    public void addRegisterSSResponse(int invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterSSResponse: must be networkFunctionalSsContext_V2");

        RegisterSSResponseImpl req=null;
        if (ssInfo != null)
            req = new RegisterSSResponseImpl(ssInfo);
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.registerSS_Response.name(), getNetworkId());               
        
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.registerSS, req, false, true);
    }

    @Override
    public Integer addEraseSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addEraseSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Integer addEraseSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addEraseSSRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getMediumTimer();
        else
        	customTimeout = customInvokeTimeout;

        EraseSSRequestImpl req = new EraseSSRequestImpl(ssForBSCode);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.eraseSS, req, true, false);
    }

    @Override
    public void addEraseSSResponse(int invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addEraseSSResponse: must be networkFunctionalSsContext_V2");

        EraseSSResponseImpl req = null;
        if (ssInfo != null)
            req = new EraseSSResponseImpl(ssInfo);
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.eraseSS_Response.name(), getNetworkId());               
            
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.eraseSS, req, false, true);
    }

    @Override
    public Integer addActivateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addActivateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Integer addActivateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addActivateSSRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        ActivateSSRequestImpl req = new ActivateSSRequestImpl(ssForBSCode);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.activateSS, req, true, false);
    }

    @Override
    public void addActivateSSResponse(int invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addActivateSSResponse: must be networkFunctionalSsContext_V2");

        ActivateSSResponseImpl req=null;
        if (ssInfo != null)
            req = new ActivateSSResponseImpl(ssInfo);            
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.activateSS_Response.name(), getNetworkId());               
        
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.activateSS, req, false, true);
    }

    @Override
    public Integer addDeactivateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addDeactivateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Integer addDeactivateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addDeactivateSSRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        DeactivateSSRequestImpl req = new DeactivateSSRequestImpl(ssForBSCode);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.deactivateSS, req, true, false);
    }

    @Override
    public void addDeactivateSSResponse(int invokeId, SSInfo ssInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addDeactivateSSResponse: must be networkFunctionalSsContext_V2");

        DeactivateSSResponseImpl req=null;
        if (ssInfo != null)
            req = new DeactivateSSResponseImpl(ssInfo);           
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.deactivateSS_Response.name(), getNetworkId());               
        
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.deactivateSS, req, false, true);
    }

    @Override
    public Integer addInterrogateSSRequest(SSForBSCode ssForBSCode) throws MAPException {
        return this.addInterrogateSSRequest(_Timer_Default, ssForBSCode);
    }

    @Override
    public Integer addInterrogateSSRequest(int customInvokeTimeout, SSForBSCode ssForBSCode) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        InterrogateSSRequestImpl req = new InterrogateSSRequestImpl(ssForBSCode);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.interrogateSS, req, true, false);
    }

    @Override
    public void addInterrogateSSResponse_SSStatus(int invokeId, SSStatus ssStatus) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(ssStatus);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.interrogateSS, req, false, true);
    }

    @Override
    public void addInterrogateSSResponse_BasicServiceGroupList(int invokeId, List<BasicServiceCode> basicServiceGroupList) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(basicServiceGroupList, false);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.interrogateSS, req, false, true);
    }

    @Override
    public void addInterrogateSSResponse_ForwardingFeatureList(int invokeId, List<ForwardingFeature> forwardingFeatureList) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(forwardingFeatureList);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.interrogateSS, req, false, true);
    }

    @Override
    public void addInterrogateSSResponse_GenericServiceInfo(int invokeId, GenericServiceInfo genericServiceInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addInterrogateSSResponse: must be networkFunctionalSsContext_V2");

        InterrogateSSResponseImpl req = new InterrogateSSResponseImpl(genericServiceInfo);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.interrogateSS, req, false, true);
    }

    @Override
    public Integer addGetPasswordRequest(Integer linkedId, GuidanceInfo guidanceInfo) throws MAPException {
        return this.addGetPasswordRequest(_Timer_Default, linkedId, guidanceInfo);
    }

    @Override
    public Integer addGetPasswordRequest(int customInvokeTimeout, Integer linkedId, GuidanceInfo guidanceInfo) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addGetPasswordRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        GetPasswordRequestImpl req = new GetPasswordRequestImpl(guidanceInfo);
        return this.sendDataComponent(null, linkedId, null, customTimeout.longValue(), MAPOperationCode.getPassword, req, true, false);
    }

    @Override
    public void addGetPasswordResponse(int invokeId, Password password) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addGetPasswordResponse: must be networkFunctionalSsContext_V2");

        GetPasswordResponseImpl req = new GetPasswordResponseImpl(password);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.getPassword, req, false, true);
    }

    @Override
    public Integer addRegisterPasswordRequest(SSCode ssCode) throws MAPException {
        return this.addRegisterPasswordRequest(_Timer_Default, ssCode);
    }

    @Override
    public Integer addRegisterPasswordRequest(int customInvokeTimeout, SSCode ssCode) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterPasswordRequest: must be networkFunctionalSsContext_V2");

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getMediumTimer();
        else
        	customTimeout=customInvokeTimeout;

        RegisterPasswordRequestImpl req = new RegisterPasswordRequestImpl(ssCode);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.registerPassword, req, true, false);
    }

    @Override
    public void addRegisterPasswordResponse(int invokeId, Password password) throws MAPException {
        if ((this.appCntx.getApplicationContextName() != MAPApplicationContextName.networkFunctionalSsContext)
                || this.appCntx.getApplicationContextVersion() != MAPApplicationContextVersion.version2)
            throw new MAPException("Bad application context name for addRegisterPasswordResponse: must be networkFunctionalSsContext_V2");

        RegisterPasswordResponseImpl req = new RegisterPasswordResponseImpl(password);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.registerPassword, req, false, true);
    }


    public Integer addProcessUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=600000; // 10 minutes
        else
        	customTimeout=customInvokeTimeout;

        ProcessUnstructuredSSRequestImpl req = new ProcessUnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString,
                alertingPatter, msisdn);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.processUnstructuredSS_Request, req, true, false);
    }

    public void addProcessUnstructuredSSResponse(int invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString)
            throws MAPException {
        ProcessUnstructuredSSResponseImpl req = new ProcessUnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.processUnstructuredSS_Request, req, false, true);
    }

    public Integer addUnstructuredSSRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addUnstructuredSSRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }

    public Integer addUnstructuredSSRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout=getLongTimer();
        else
        	customTimeout=customInvokeTimeout;

        UnstructuredSSRequestImpl req = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.unstructuredSS_Request, req, true, false);
    }

    public Integer addUnstructuredSSNotifyRequest(CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString,
            AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {
        return this.addUnstructuredSSNotifyRequest(_Timer_Default, ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
    }

    public Integer addUnstructuredSSNotifyRequest(int customInvokeTimeout, CBSDataCodingScheme ussdDataCodingScheme,
            USSDString ussdString, AlertingPattern alertingPatter, ISDNAddressString msisdn) throws MAPException {

        Integer customTimeout;
        if (customInvokeTimeout == _Timer_Default)
        	customTimeout = getLongTimer();
        else
        	customTimeout = customInvokeTimeout;

        UnstructuredSSRequestImpl req = null;
        if (ussdString != null)
            req = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString, alertingPatter, msisdn);
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.unstructuredSSNotify_Request.name(), getNetworkId());               
        
        return this.sendDataComponent(null, null, null, customTimeout.longValue(), MAPOperationCode.unstructuredSS_Notify, req, true, false);
    }

    public void addUnstructuredSSResponse(int invokeId, CBSDataCodingScheme ussdDataCodingScheme, USSDString ussdString)
            throws MAPException {

    	UnstructuredSSResponseImpl req=null;
        if (ussdString != null)
            req = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
        else
        	mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.unstructuredSSRequest_Response.name(), getNetworkId());               
        
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.unstructuredSS_Request, req, false, true);
    }

    public void addUnstructuredSSNotifyResponse(int invokeId) throws MAPException {
        mapProviderImpl.getMAPStack().newMessageSent(MAPMessageType.unstructuredSSNotify_Response.name(), getNetworkId());               
        this.sendDataComponent(invokeId, null, null, null, MAPOperationCode.unstructuredSS_Notify, null, false, true);
    }
}