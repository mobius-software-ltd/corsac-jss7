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

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CallTypeCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.CauseValue;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.DestinationNumberCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmCamelTdpCriteria;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OBcsmTriggerDetectionPoint;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class OBcsmCamelTdpCriteriaImpl implements OBcsmCamelTdpCriteria {
	private ASNOBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = DestinationNumberCriteriaImpl.class)
    private DestinationNumberCriteria destinationNumberCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private ExtBasicServiceCodeListWrapperImpl basicServiceCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNCallTypeCriteria callTypeCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private CauseValueWrapperImpl oCauseValueCriteria;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public OBcsmCamelTdpCriteriaImpl() {
    }

    public OBcsmCamelTdpCriteriaImpl(OBcsmTriggerDetectionPoint oBcsmTriggerDetectionPoint,
            DestinationNumberCriteria destinationNumberCriteria, List<ExtBasicServiceCode> basicServiceCriteria,
            CallTypeCriteria callTypeCriteria, List<CauseValue> oCauseValueCriteria,
            MAPExtensionContainer extensionContainer) {
        if(oBcsmTriggerDetectionPoint!=null) {
        	this.oBcsmTriggerDetectionPoint = new ASNOBcsmTriggerDetectionPoint();
        	this.oBcsmTriggerDetectionPoint.setType(oBcsmTriggerDetectionPoint);
        }
        
        this.destinationNumberCriteria = destinationNumberCriteria;
        
        if(basicServiceCriteria!=null)
        this.basicServiceCriteria = new ExtBasicServiceCodeListWrapperImpl(basicServiceCriteria);
        
        if(callTypeCriteria!=null) {
        	this.callTypeCriteria = new ASNCallTypeCriteria();
        	this.callTypeCriteria.setType(callTypeCriteria);
        }
        
        if(oCauseValueCriteria!=null)
        	this.oCauseValueCriteria = new CauseValueWrapperImpl(oCauseValueCriteria);
        
        this.extensionContainer = extensionContainer;
    }

    public OBcsmTriggerDetectionPoint getOBcsmTriggerDetectionPoint() {
    	if(this.oBcsmTriggerDetectionPoint==null)
    		return null;
    	
        return this.oBcsmTriggerDetectionPoint.getType();
    }

    public DestinationNumberCriteria getDestinationNumberCriteria() {
        return this.destinationNumberCriteria;
    }

    public List<ExtBasicServiceCode> getBasicServiceCriteria() {
    	if(this.basicServiceCriteria==null)
    		return null;
    	
        return this.basicServiceCriteria.getExtBasicServiceCode();
    }

    public CallTypeCriteria getCallTypeCriteria() {
    	if(this.callTypeCriteria==null)
    		return null;
    	
        return this.callTypeCriteria.getType();
    }

    public List<CauseValue> getOCauseValueCriteria() {
    	if(this.oCauseValueCriteria==null)
    		return null;
    	
        return this.oCauseValueCriteria.getCauseValueImpl();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OBcsmCamelTdpCriteria [");

        if (this.oBcsmTriggerDetectionPoint != null) {
            sb.append("oBcsmTriggerDetectionPoint=");
            sb.append(this.oBcsmTriggerDetectionPoint.toString());
            sb.append(", ");
        }

        if (this.destinationNumberCriteria != null) {
            sb.append("destinationNumberCriteria=");
            sb.append(this.destinationNumberCriteria.toString());
            sb.append(", ");
        }

        if (this.basicServiceCriteria != null && this.basicServiceCriteria.getExtBasicServiceCode()!=null) {
            sb.append("basicServiceCriteria=[");
            boolean firstItem = true;
            for (ExtBasicServiceCode be : this.basicServiceCriteria.getExtBasicServiceCode()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.callTypeCriteria != null) {
            sb.append("callTypeCriteria=");
            sb.append(this.callTypeCriteria.toString());
            sb.append(", ");
        }

        if (this.oCauseValueCriteria != null && this.oCauseValueCriteria.getCauseValueImpl()!=null) {
            sb.append("oCauseValueCriteria=[");
            boolean firstItem = true;
            for (CauseValue be : this.oCauseValueCriteria.getCauseValueImpl()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

}
