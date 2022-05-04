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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ProtocolId;
import org.restcomm.protocols.ss7.map.api.primitives.SignalInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExternalSignalInfoImpl implements ExternalSignalInfo {
	private ASNProtocolIDImpl protocolId = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SignalInfoImpl.class)
	private SignalInfo signalInfo = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer = null;

    public ExternalSignalInfoImpl() {
    }

    public ExternalSignalInfoImpl(SignalInfo signalInfo, ProtocolId protocolId, MAPExtensionContainer extensionContainer) {
        this.signalInfo = signalInfo;
        if(protocolId!=null)
	        this.protocolId = new ASNProtocolIDImpl(protocolId);
	        
        this.extensionContainer = extensionContainer;
    }

    public SignalInfo getSignalInfo() {
        return this.signalInfo;
    }

    public ProtocolId getProtocolId() {
    	if(this.protocolId==null)
    		return null;
    	
        return this.protocolId.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExternalSignalInfo [");

        if (this.signalInfo != null) {
            sb.append("signalInfo=[");
            sb.append(this.signalInfo);
            sb.append("], ");
        }

        if (this.protocolId != null) {
            sb.append("protocolId=[");
            sb.append(this.protocolId.getType());
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(protocolId==null)
			throw new ASNParsingComponentException("protocol ID should be set for external signal info", ASNParsingComponentExceptionReason.MistypedParameter);			
		
		if(signalInfo==null)
			throw new ASNParsingComponentException("signal info should be set for external signal info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}