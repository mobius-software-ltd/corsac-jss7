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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultGPRSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSTriggerDetectionPoint;

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
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSCamelTDPDataImpl implements GPRSCamelTDPData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNGPRSTriggerDetectionPoint gprsTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNDefaultGPRSHandling defaultSessionHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public GPRSCamelTDPDataImpl() {
    }

    public GPRSCamelTDPDataImpl(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, int serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainer extensionContainer) {
        if(gprsTriggerDetectionPoint!=null)
        	this.gprsTriggerDetectionPoint =  new ASNGPRSTriggerDetectionPoint(gprsTriggerDetectionPoint);
        	
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultSessionHandling!=null)
        	this.defaultSessionHandling = new ASNDefaultGPRSHandling(defaultSessionHandling);
        	
        this.extensionContainer = extensionContainer;
    }

    public GPRSTriggerDetectionPoint getGPRSTriggerDetectionPoint() {
    	if(this.gprsTriggerDetectionPoint==null)
    		return null;
    	
        return this.gprsTriggerDetectionPoint.getType();
    }

    public int getServiceKey() {
    	if(this.serviceKey==null)
    		return 0;
    	
        return this.serviceKey.getIntValue();
    }

    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public DefaultGPRSHandling getDefaultSessionHandling() {
    	if(this.defaultSessionHandling==null)
    		return null;
    	
        return this.defaultSessionHandling.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSCamelTDPData [");

        if (this.gprsTriggerDetectionPoint != null) {
            sb.append("gprsTriggerDetectionPoint=");
            sb.append(this.gprsTriggerDetectionPoint.getType());
            sb.append(", ");
        }

        if(this.serviceKey!=null) {
	        sb.append("serviceKey=");
	        sb.append(this.serviceKey.getValue());
	        sb.append(", ");
        }
        
        if (this.gsmSCFAddress != null) {
            sb.append("gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
            sb.append(", ");
        }

        if (this.defaultSessionHandling != null) {
            sb.append("defaultSessionHandling=");
            sb.append(this.defaultSessionHandling.getType());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());

        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gprsTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("gprs trigger detection point should be set for gprs camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(serviceKey==null)
			throw new ASNParsingComponentException("service key should be set for gprs camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(gsmSCFAddress==null)
			throw new ASNParsingComponentException("gsm scf address should be set for gprs camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);

		if(defaultSessionHandling==null)
			throw new ASNParsingComponentException("default session handling should be set for gprs camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
