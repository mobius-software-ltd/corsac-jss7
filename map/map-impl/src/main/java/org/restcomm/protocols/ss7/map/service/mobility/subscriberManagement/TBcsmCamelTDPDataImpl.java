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

import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DefaultCallHandling;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmCamelTDPData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.TBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class TBcsmCamelTDPDataImpl implements TBcsmCamelTDPData {
	private ASNTBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint;
    private ASNInteger serviceKey;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNDefaultCallHandling defaultCallHandling;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public TBcsmCamelTDPDataImpl() {
    }

    public TBcsmCamelTDPDataImpl(TBcsmTriggerDetectionPoint tBcsmTriggerDetectionPoint, long serviceKey,
    		ISDNAddressString gsmSCFAddress, DefaultCallHandling defaultCallHandling, MAPExtensionContainer extensionContainer) {
        if(tBcsmTriggerDetectionPoint!=null) {
        	this.tBcsmTriggerDetectionPoint = new ASNTBcsmTriggerDetectionPoint();
        	this.tBcsmTriggerDetectionPoint.setType(tBcsmTriggerDetectionPoint);
        }
                
        this.serviceKey = new ASNInteger();
        this.serviceKey.setValue(serviceKey);
        
        this.gsmSCFAddress = gsmSCFAddress;
        
        if(defaultCallHandling!=null) {
        	this.defaultCallHandling = new ASNDefaultCallHandling();
        	this.defaultCallHandling.setType(defaultCallHandling);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public TBcsmTriggerDetectionPoint getTBcsmTriggerDetectionPoint() {
    	if(tBcsmTriggerDetectionPoint==null)
    		return null;
    	
        return tBcsmTriggerDetectionPoint.getType();
    }

    public long getServiceKey() {
    	return serviceKey.getValue();
    }

    public ISDNAddressString getGsmSCFAddress() {
        return gsmSCFAddress;
    }

    public DefaultCallHandling getDefaultCallHandling() {
    	if(defaultCallHandling==null)
    		return null;
    	
        return defaultCallHandling.getType();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TBcsmCamelTDPData");
        sb.append(" [");

        if (this.tBcsmTriggerDetectionPoint != null) {
            sb.append("tBcsmTriggerDetectionPoint=");
            sb.append(this.tBcsmTriggerDetectionPoint.toString());
        }
        sb.append(", serviceKey=");
        sb.append(this.serviceKey);
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress.toString());
        }
        if (this.defaultCallHandling != null) {
            sb.append(", defaultCallHandling=");
            sb.append(this.defaultCallHandling.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
