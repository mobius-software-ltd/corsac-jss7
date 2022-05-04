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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AMBR;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AMBRImpl implements AMBR {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNInteger maxRequestedBandwidthUL;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=1)
    private ASNInteger maxRequestedBandwidthDL;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;

    public AMBRImpl() {
    }

    public AMBRImpl(int maxRequestedBandwidthUL, int maxRequestedBandwidthDL, MAPExtensionContainer extensionContainer) {
        this.maxRequestedBandwidthUL = new ASNInteger(maxRequestedBandwidthUL,"MaxRequestedBandwidthUL",Integer.MIN_VALUE,Integer.MAX_VALUE,false);
        this.maxRequestedBandwidthDL = new ASNInteger(maxRequestedBandwidthDL,"MaxRequestedBandwidthDL",Integer.MIN_VALUE,Integer.MAX_VALUE,false);
        this.extensionContainer = extensionContainer;
    }

    public int getMaxRequestedBandwidthUL() {
    	if(this.maxRequestedBandwidthUL==null || this.maxRequestedBandwidthUL.getValue()==null)
    		return 0;
    	
        return this.maxRequestedBandwidthUL.getIntValue();
    }

    public int getMaxRequestedBandwidthDL() {
    	if(this.maxRequestedBandwidthDL==null || this.maxRequestedBandwidthDL.getValue()==null)
    		return 0;
    	
        return this.maxRequestedBandwidthDL.getIntValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AMBR [");

        if(this.maxRequestedBandwidthUL!=null) {
	        sb.append("maxRequestedBandwidthUL=");
	        sb.append(this.maxRequestedBandwidthUL.getValue());
	        sb.append(", ");
        }
        
        if(this.maxRequestedBandwidthDL!=null) {
	        sb.append("maxRequestedBandwidthDL=");
	        sb.append(this.maxRequestedBandwidthDL.getValue());
	        sb.append(", ");
        }
        
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(maxRequestedBandwidthDL==null)
			throw new ASNParsingComponentException("max requested bandwidth DL should be set for AMBR", ASNParsingComponentExceptionReason.MistypedParameter);

		if(maxRequestedBandwidthUL==null)
			throw new ASNParsingComponentException("max requested bandwidth UL should be set for AMBR", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
