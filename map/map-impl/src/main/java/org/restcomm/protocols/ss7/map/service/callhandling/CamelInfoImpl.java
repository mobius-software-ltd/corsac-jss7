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
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.SupportedCamelPhasesImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=true,lengthIndefinite=false)
public class CamelInfoImpl implements CamelInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,index=-1,defaultImplementation = SupportedCamelPhasesImpl.class)
	public SupportedCamelPhases supportedCamelPhases;
    
	public ASNNull suppressTCSI;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    public MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = OfferedCamel4CSIsImpl.class)
    public OfferedCamel4CSIs offeredCamel4CSIs;

    public CamelInfoImpl() {        
    }

    public CamelInfoImpl(SupportedCamelPhases supportedCamelPhases, boolean suppressTCSI,
            MAPExtensionContainer extensionContainer, OfferedCamel4CSIs offeredCamel4CSIs) {        

        this.supportedCamelPhases = supportedCamelPhases;
        if(suppressTCSI)
        	this.suppressTCSI = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        this.offeredCamel4CSIs = offeredCamel4CSIs;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    public boolean getSuppressTCSI() {
        return suppressTCSI!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public OfferedCamel4CSIs getOfferedCamel4CSIs() {
        return offeredCamel4CSIs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((supportedCamelPhases == null) ? 0 : supportedCamelPhases.hashCode());
        result = prime * result + ((suppressTCSI != null) ? 0 : 1);
        result = prime * result + ((extensionContainer == null) ? 0 : extensionContainer.hashCode());
        result = prime * result + ((offeredCamel4CSIs == null) ? 0 : offeredCamel4CSIs.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CamelInfoImpl other = (CamelInfoImpl) obj;

        if (this.supportedCamelPhases == null) {
            if (other.supportedCamelPhases != null)
                return false;
        } else {
            if (!this.supportedCamelPhases.equals(other.supportedCamelPhases))
                return false;
        }

        if (this.suppressTCSI != other.suppressTCSI)
            return false;

        if (this.extensionContainer == null) {
            if (other.extensionContainer != null)
                return false;
        } else {
            if (!this.extensionContainer.equals(other.extensionContainer))
                return false;
        }

        if (this.offeredCamel4CSIs == null) {
            if (other.offeredCamel4CSIs != null)
                return false;
        } else {
            if (!this.offeredCamel4CSIs.equals(other.offeredCamel4CSIs))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CamelInfo");
        sb.append(" [");

        if (this.supportedCamelPhases != null) {
            sb.append("supportedCamelPhases=");
            sb.append(this.supportedCamelPhases);
        }
        if (this.suppressTCSI!=null) {
            sb.append(", suppressTCSI");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.offeredCamel4CSIs != null) {
            sb.append(", offeredCamel4CSIs=");
            sb.append(this.offeredCamel4CSIs);
        }

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(supportedCamelPhases==null)
			throw new ASNParsingComponentException("supported camel phases should be set for camel info", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
