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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AllocationRetentionPriority;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.EPSQoSSubscribed;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.QoSClassIdentifier;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EPSQoSSubscribedImpl implements EPSQoSSubscribed {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ASNQoSClassIdentifier qoSClassIdentifier;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=1,defaultImplementation = AllocationRetentionPriorityImpl.class)
    private AllocationRetentionPriority allocationRetentionPriority;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public EPSQoSSubscribedImpl() {
    }

    public EPSQoSSubscribedImpl(QoSClassIdentifier qoSClassIdentifier, AllocationRetentionPriority allocationRetentionPriority,
            MAPExtensionContainer extensionContainer) {
        if(qoSClassIdentifier!=null)
        	this.qoSClassIdentifier = new ASNQoSClassIdentifier(qoSClassIdentifier);
        	
        this.allocationRetentionPriority = allocationRetentionPriority;
        this.extensionContainer = extensionContainer;
    }

    public QoSClassIdentifier getQoSClassIdentifier() {
    	if(this.qoSClassIdentifier==null)
    		return null;
    	
        return this.qoSClassIdentifier.getType();
    }

    public AllocationRetentionPriority getAllocationRetentionPriority() {
        return this.allocationRetentionPriority;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EPSQoSSubscribed [");

        if (this.qoSClassIdentifier != null) {
            sb.append("qoSClassIdentifier=");
            sb.append(this.qoSClassIdentifier.getType());
            sb.append(", ");
        }

        if (this.allocationRetentionPriority != null) {
            sb.append("allocationRetentionPriority=");
            sb.append(this.allocationRetentionPriority.toString());
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
		if(qoSClassIdentifier==null)
			throw new ASNParsingComponentException("qos class identifier should be set for eps qos subscriber", ASNParsingComponentExceptionReason.MistypedParameter);

		if(allocationRetentionPriority==null)
			throw new ASNParsingComponentException("allocation retention priority should be set for eps qos subscriber", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
