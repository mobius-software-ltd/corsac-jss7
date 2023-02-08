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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CUGCheckInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.InterrogationType;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SendRoutingInformationRequest;
import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSS;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.ISTSupportIndicator;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingReason;
import org.restcomm.protocols.ss7.map.primitives.ExternalSignalInfoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInformationRequestImplV1 extends CallHandlingMessageImpl implements SendRoutingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger numberOfForwarding;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=true,index=-1, defaultImplementation = ExternalSignalInfoImpl.class)
    private ExternalSignalInfo networkSignalInfo;
    
    public SendRoutingInformationRequestImplV1() {
    }

    public SendRoutingInformationRequestImplV1(ISDNAddressString msisdn,
            Integer numberOfForwarding, ExternalSignalInfo networkSignalInfo) {
        this.msisdn = msisdn;
        
        if(numberOfForwarding!=null)
        	this.numberOfForwarding = new ASNInteger(numberOfForwarding,"NumberOfForwarding",1,5,false);
        	
        this.networkSignalInfo=networkSignalInfo;
    }

    @Override
    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    @Override
    public CUGCheckInfo getCUGCheckInfo() {
        return null;
    }

    @Override
    public Integer getNumberOfForwarding() {
    	if(this.numberOfForwarding==null)
    		return null;
    	
        return this.numberOfForwarding.getIntValue();
    }

    @Override
    public InterrogationType getInterogationType() {
    	return null;
    }

    @Override
    public boolean getORInterrogation() {    	
        return false;
    }

    @Override
    public Integer getORCapability() {
    	return null;
    }

    @Override
    public ISDNAddressString getGmscOrGsmSCFAddress() {
    	return null;
    }

    @Override
    public CallReferenceNumber getCallReferenceNumber() {
    	return null;
    }

    @Override
    public ForwardingReason getForwardingReason() {
    	return null;
    }

    @Override
    public ExtBasicServiceCode getBasicServiceGroup() {
    	return null;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo() {
        return this.networkSignalInfo;
    }

    @Override
    public CamelInfo getCamelInfo() {
    	return null;
    }

    @Override
    public boolean getSuppressionOfAnnouncement() {
        return false;
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
    	return null;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
    	return null;
    }

    @Override
    public boolean getCCBSCall() {
    	return false;
    }

    @Override
    public Integer getSupportedCCBSPhase() {
    	return null;
    }

    @Override
    public ExtExternalSignalInfo getAdditionalSignalInfo() {
    	return null;
    }

    @Override
    public ISTSupportIndicator getIstSupportIndicator() {
    	return null;
    }

    @Override
    public boolean getPrePagingSupported() {
    	return false;
    }

    @Override
    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
    	return null;
    }

    @Override
    public boolean getLongFTNSupported() {
        return false;
    }

    @Override
    public boolean getSuppressVtCSI() {
        return false;
    }

    @Override
    public boolean getSuppressIncomingCallBarring() {
        return false;
    }

    @Override
    public boolean getGsmSCFInitiatedCall() {
        return false;
    }

    @Override
    public ExtBasicServiceCode getBasicServiceGroup2() {
    	return null;
    }

    @Override
    public ExternalSignalInfo getNetworkSignalInfo2() {
    	return null;
    }

    @Override
    public SuppressMTSS getSuppressMTSS() {
    	return null;
    }

    @Override
    public boolean getMTRoamingRetrySupported() {
    	return false;
    }

    @Override
    public EMLPPPriority getCallPriority() {
    	return null;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfo_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfo;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInformationRequest [");

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn);
        }

        if (this.numberOfForwarding != null) {
            sb.append(", numberOfForwarding=");
            sb.append(this.numberOfForwarding.getValue());
        }

        if (this.networkSignalInfo != null) {
            sb.append(", networkSignalInfo=");
            sb.append(this.networkSignalInfo);
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(msisdn==null)
			throw new ASNParsingComponentException("msisdn should be set for send routing information request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}