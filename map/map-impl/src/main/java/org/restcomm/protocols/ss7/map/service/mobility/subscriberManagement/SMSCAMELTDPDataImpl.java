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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultSMSHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSCAMELTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.SMSTriggerDetectionPoint;

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
public class SMSCAMELTDPDataImpl implements SMSCAMELTDPData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNSMSTriggerDetectionPoint smsTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNDefaultSMSHandling defaultSMSHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public SMSCAMELTDPDataImpl() {
    }

    public SMSCAMELTDPDataImpl(SMSTriggerDetectionPoint smsTriggerDetectionPoint, int serviceKey,
            ISDNAddressString gsmSCFAddress, DefaultSMSHandling defaultSMSHandling, MAPExtensionContainer extensionContainer) {
        if(smsTriggerDetectionPoint!=null)
        	this.smsTriggerDetectionPoint = new ASNSMSTriggerDetectionPoint(smsTriggerDetectionPoint);
        	
        this.serviceKey = new ASNInteger(serviceKey,"ServiceKey",0,Integer.MAX_VALUE,false);
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultSMSHandling!=null)
        	this.defaultSMSHandling = new ASNDefaultSMSHandling(defaultSMSHandling);
        	
        this.extensionContainer = extensionContainer;
    }

    public SMSTriggerDetectionPoint getSMSTriggerDetectionPoint() {
    	if(this.smsTriggerDetectionPoint==null)
    		return null;
    	
        return this.smsTriggerDetectionPoint.getType();
    }

    public int getServiceKey() {
    	if(this.serviceKey==null)
    		return 0;
    	
        return this.serviceKey.getIntValue();
    }

    public ISDNAddressString getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public DefaultSMSHandling getDefaultSMSHandling() {
    	if(this.defaultSMSHandling==null)
    		return null;
    	
        return this.defaultSMSHandling.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMSCAMELTDPData [");

        if (this.smsTriggerDetectionPoint != null) {
            sb.append("smsTriggerDetectionPoint=");
            sb.append(this.smsTriggerDetectionPoint.getType());
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

        if (this.defaultSMSHandling != null) {
            sb.append("defaultSMSHandling=");
            sb.append(this.defaultSMSHandling.getType());
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
		if(smsTriggerDetectionPoint==null)
			throw new ASNParsingComponentException("sms trigger detection point should be set for smsc camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(serviceKey==null)
			throw new ASNParsingComponentException("service key should be set for smsc camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(gsmSCFAddress==null)
			throw new ASNParsingComponentException("gsm scf address should be set for smsc camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(defaultSMSHandling==null)
			throw new ASNParsingComponentException("default sms handling should be set for smsc camel tdp data", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
