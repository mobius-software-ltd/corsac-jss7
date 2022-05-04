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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.IpSmGwGuidance;
import org.restcomm.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.SendRoutingInfoForSMResponse;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForSMResponseImpl extends SmsMessageImpl implements SendRoutingInfoForSMResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=1, defaultImplementation = LocationInfoWithLMSIImpl.class)
    private LocationInfoWithLMSI locationInfoWithLMSI;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNBoolean mwdSet;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=true,index=-1, defaultImplementation = IpSmGwGuidanceImpl.class)
    private IpSmGwGuidance ipSmGwGuidance;

    public SendRoutingInfoForSMResponseImpl() {
    }

    public SendRoutingInfoForSMResponseImpl(IMSI imsi, LocationInfoWithLMSI locationInfoWithLMSI,
    		MAPExtensionContainer extensionContainer, Boolean mwdSet, IpSmGwGuidance ipSmGwGuidance) {
        this.imsi = imsi;
        this.locationInfoWithLMSI = locationInfoWithLMSI;
        this.extensionContainer = extensionContainer;
        
        if(mwdSet!=null)
        	this.mwdSet = new ASNBoolean(mwdSet,"MWDSet",false,false);        	
        
        this.ipSmGwGuidance = ipSmGwGuidance;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForSM_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForSM;
    }

    public IMSI getIMSI() {
        return this.imsi;
    }

    public LocationInfoWithLMSI getLocationInfoWithLMSI() {
        return this.locationInfoWithLMSI;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public Boolean getMwdSet() {
    	if(mwdSet==null)
    		return null;
    	
        return mwdSet.getValue();
    }

    public IpSmGwGuidance getIpSmGwGuidance() {
        return ipSmGwGuidance;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForSMResponse [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.imsi != null) {
            sb.append(", imsi=");
            sb.append(this.imsi.toString());
        }
        if (this.locationInfoWithLMSI != null) {
            sb.append(", locationInfoWithLMSI=");
            sb.append(this.locationInfoWithLMSI.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }
        if (this.mwdSet != null) {
            sb.append(", mwdSet=");
            sb.append(this.mwdSet.getValue());
        }
        if (this.ipSmGwGuidance != null) {
            sb.append(", ipSmGwGuidance=");
            sb.append(this.ipSmGwGuidance.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(imsi==null)
			throw new ASNParsingComponentException("imsi should be set for send routing info for SM response", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(locationInfoWithLMSI==null)
			throw new ASNParsingComponentException("location info with LMSI should be set for send routing info for SM response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
