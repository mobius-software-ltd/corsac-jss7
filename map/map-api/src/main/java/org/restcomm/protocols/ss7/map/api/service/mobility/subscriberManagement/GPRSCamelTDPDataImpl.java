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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSCamelTDPDataImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ASNGPRSTriggerDetectionPoint gprsTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ISDNAddressStringImpl gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNDefaultGPRSHandling defaultSessionHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public GPRSCamelTDPDataImpl() {
    }

    public GPRSCamelTDPDataImpl(GPRSTriggerDetectionPoint gprsTriggerDetectionPoint, long serviceKey,
            ISDNAddressStringImpl gsmSCFAddress, DefaultGPRSHandling defaultSessionHandling,
            MAPExtensionContainerImpl extensionContainer) {
        if(gprsTriggerDetectionPoint!=null) {
        	this.gprsTriggerDetectionPoint =  new ASNGPRSTriggerDetectionPoint();
        	this.gprsTriggerDetectionPoint.setType(gprsTriggerDetectionPoint);
        }
        
        this.serviceKey = new ASNInteger();
        this.serviceKey.setValue(serviceKey);
        
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultSessionHandling!=null) {
        	this.defaultSessionHandling = new ASNDefaultGPRSHandling();
        	this.defaultSessionHandling.setType(defaultSessionHandling);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public GPRSTriggerDetectionPoint getGPRSTriggerDetectionPoint() {
    	if(this.gprsTriggerDetectionPoint==null)
    		return null;
    	
        return this.gprsTriggerDetectionPoint.getType();
    }

    public long getServiceKey() {
    	if(this.serviceKey==null)
    		return 0;
    	
        return this.serviceKey.getValue();
    }

    public ISDNAddressStringImpl getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    public DefaultGPRSHandling getDefaultSessionHandling() {
    	if(this.defaultSessionHandling==null)
    		return null;
    	
        return this.defaultSessionHandling.getType();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
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
}
