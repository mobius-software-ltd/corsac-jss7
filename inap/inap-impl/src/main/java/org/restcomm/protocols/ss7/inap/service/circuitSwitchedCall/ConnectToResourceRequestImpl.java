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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ConnectToResourceRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceAddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceAddressImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ServiceInteractionIndicatorsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectToResourceRequestImpl extends CircuitSwitchedCallMessageImpl implements ConnectToResourceRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = ResourceAddressImpl.class)
    private ResourceAddress resourceAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = ResourceAddressImpl.class)
    private ResourceAddress bothAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = true,index = -1,defaultImplementation = ServiceInteractionIndicatorsImpl.class)
    private ServiceInteractionIndicators serviceInteractionIndicators;
    
    public ConnectToResourceRequestImpl() {
    }

    public ConnectToResourceRequestImpl(CalledPartyNumberIsup ipRoutingAddress, CAPINAPExtensions extensions,ServiceInteractionIndicators serviceInteractionIndicators) {
    	if(ipRoutingAddress!=null) {
    		this.resourceAddress=new ResourceAddressImpl(ipRoutingAddress);
    	}
    	this.extensions=extensions;
    	this.serviceInteractionIndicators=serviceInteractionIndicators;
    } 
    
    public ConnectToResourceRequestImpl(LegType legID, CAPINAPExtensions extensions,ServiceInteractionIndicators serviceInteractionIndicators) {
    	if(legID!=null) {
    		this.resourceAddress=new ResourceAddressImpl(legID);
    	}
    	this.extensions=extensions;
    	this.serviceInteractionIndicators=serviceInteractionIndicators;
    } 
    
    public ConnectToResourceRequestImpl(boolean none, CAPINAPExtensions extensions,ServiceInteractionIndicators serviceInteractionIndicators) {
    	if(none) {
    		this.resourceAddress=new ResourceAddressImpl(none);
    	}
    	this.extensions=extensions;
    	this.serviceInteractionIndicators=serviceInteractionIndicators;
    } 
    
    public ConnectToResourceRequestImpl(ResourceAddress resourceAddress, CAPINAPExtensions extensions,ServiceInteractionIndicators serviceInteractionIndicators) {
    	this.bothAddress=resourceAddress;
    	this.extensions=extensions;
    	this.serviceInteractionIndicators=serviceInteractionIndicators;
    } 

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.connectToResource_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.connectToResource;
    }

    @Override
    public ResourceAddress getResourceAddress() {
    	if(resourceAddress==null)
    		return bothAddress;
    	
        return resourceAddress;
    }

    public boolean getResourceAddressNull() {
    	if(resourceAddress==null)
    		return false;
    	
    	return resourceAddress.getNone();
    }
    
    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public ServiceInteractionIndicators getServiceInteractionIndicators() {
        return serviceInteractionIndicators;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ConnectToResourceIndication [");
        this.addInvokeIdInfo(sb);

        if (this.resourceAddress != null) {
            sb.append(", resourceAddress=");
            sb.append(resourceAddress.toString());
        } else if(bothAddress!=null) {
        	sb.append(", resourceAddress=");
            sb.append(bothAddress.toString());
        }
        
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.serviceInteractionIndicators != null) {
            sb.append(", serviceInteractionIndicators=");
            sb.append(serviceInteractionIndicators.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(resourceAddress==null)
			throw new ASNParsingComponentException("resource address should be set for connect to resource request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
