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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AllocationRetentionPriorityImpl implements AllocationRetentionPriority {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNInteger priorityLevel = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ASNBoolean preEmptionCapability = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNBoolean preEmptionVulnerability = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;

    public AllocationRetentionPriorityImpl() {
    }

    public AllocationRetentionPriorityImpl(int priorityLevel, Boolean preEmptionCapability, Boolean preEmptionVulnerability,
            MAPExtensionContainer extensionContainer) {    	
        this.priorityLevel = new ASNInteger();
        this.priorityLevel.setValue((long)priorityLevel & 0x0FFFFFFFFL);
        
        if(preEmptionCapability!=null) {
        	this.preEmptionCapability = new ASNBoolean();
        	this.preEmptionCapability.setValue(preEmptionCapability);
        }
        
        if(preEmptionVulnerability!=null) {
        	this.preEmptionVulnerability = new ASNBoolean();
        	this.preEmptionVulnerability.setValue(preEmptionVulnerability);
        }
        
        this.extensionContainer = extensionContainer;
    }

    public int getPriorityLevel() {
    	if(this.priorityLevel==null)
    		return -1;
    	
        return this.priorityLevel.getValue().intValue();
    }

    public Boolean getPreEmptionCapability() {
    	if(this.preEmptionCapability==null)
    		return null;
    	
        return this.preEmptionCapability.getValue();
    }

    public Boolean getPreEmptionVulnerability() {
    	if(this.preEmptionVulnerability==null)
    		return null;
    	
        return this.preEmptionVulnerability.getValue();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AllocationRetentionPriority [");

        if(this.priorityLevel!=null) {
	        sb.append("priorityLevel=");
	        sb.append(this.priorityLevel.getValue());
	        sb.append(", ");
        }
        
        if (this.preEmptionCapability != null) {
            sb.append("preEmptionCapability=");
            sb.append(this.preEmptionCapability.getValue());
            sb.append(", ");
        }

        if (this.preEmptionVulnerability != null) {
            sb.append("preEmptionVulnerability=");
            sb.append(this.preEmptionVulnerability.getValue());
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
}
