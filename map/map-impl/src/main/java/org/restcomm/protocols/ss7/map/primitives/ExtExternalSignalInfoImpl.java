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
import org.restcomm.protocols.ss7.map.api.primitives.ExtExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExtProtocolId;
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
public class ExtExternalSignalInfoImpl implements ExtExternalSignalInfo {
	private ASNExtProtocolIDImpl extProtocolId = null;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = SignalInfoImpl.class)
	private SignalInfo signalInfo = null;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer = null;

    public ExtExternalSignalInfoImpl() {
    }

    public ExtExternalSignalInfoImpl(SignalInfo signalInfo, ExtProtocolId extProtocolId,
            MAPExtensionContainer extensionContainer) {
        this.signalInfo = signalInfo;
        if(extProtocolId!=null)
        	this.extProtocolId = new ASNExtProtocolIDImpl(extProtocolId);
        	     
        this.extensionContainer = extensionContainer;
    }

    public SignalInfo getSignalInfo() {
        return this.signalInfo;
    }

    public ExtProtocolId getExtProtocolId() {
    	if(this.extProtocolId==null)
    		return null;
    	
        return this.extProtocolId.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtExternalSignalInfo [");

        if (this.signalInfo != null) {
            sb.append("signalInfo=[");
            sb.append(this.signalInfo);
            sb.append("], ");
        }

        if (this.extProtocolId != null) {
            sb.append("extProtocolId=[");
            sb.append(this.extProtocolId);
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
		if(extProtocolId==null)
			throw new ASNParsingComponentException("ext protocol ID should be set for external signal info", ASNParsingComponentExceptionReason.MistypedParameter);			
		
		if(signalInfo==null)
			throw new ASNParsingComponentException("signal info should be set for external signal info", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}