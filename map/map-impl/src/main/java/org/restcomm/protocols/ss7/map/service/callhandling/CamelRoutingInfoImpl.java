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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CamelRoutingInfoImpl implements CamelRoutingInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = ForwardingDataImpl.class)
	private ForwardingData forwardingData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = GmscCamelSubscriptionInfoImpl.class)
    private GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public CamelRoutingInfoImpl() {       
    }

    public CamelRoutingInfoImpl(ForwardingData forwardingData, GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo,
    		MAPExtensionContainer extensionContainer) {
        this.forwardingData = forwardingData;
        this.gmscCamelSubscriptionInfo = gmscCamelSubscriptionInfo;
        this.extensionContainer = extensionContainer;
    }

    public ForwardingData getForwardingData() {
        return forwardingData;
    }

    public GmscCamelSubscriptionInfo getGmscCamelSubscriptionInfo() {
        return gmscCamelSubscriptionInfo;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CamelRoutingInfo [");

        if (this.forwardingData != null) {
            sb.append("forwardingData=");
            sb.append(this.forwardingData.toString());
        }
        if (this.gmscCamelSubscriptionInfo != null) {
            sb.append(", gmscCamelSubscriptionInfo=");
            sb.append(this.gmscCamelSubscriptionInfo.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gmscCamelSubscriptionInfo==null)
			throw new ASNParsingComponentException("gmsc camel subscription info should be set for camel info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
