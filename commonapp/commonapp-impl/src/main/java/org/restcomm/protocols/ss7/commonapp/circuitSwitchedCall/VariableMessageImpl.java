/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariableMessage;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariablePart;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class VariableMessageImpl implements VariableMessage {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger elementaryMessageID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private VariablePartWrapperImpl variableParts;

    public VariableMessageImpl() {
    }

    public VariableMessageImpl(int elementaryMessageID, List<VariablePart> variableParts) {
        this.elementaryMessageID = new ASNInteger(elementaryMessageID,"ElementaryMessageID",0,255,false);

        if(variableParts!=null) {
        	this.variableParts = new VariablePartWrapperImpl(variableParts);
        }
    }

    public int getElementaryMessageID() {
    	if(elementaryMessageID==null || elementaryMessageID.getValue()==null)
    		return 0;
    	
        return elementaryMessageID.getIntValue();
    }

    public List<VariablePart> getVariableParts() {
    	if(variableParts==null)
    		return null;
    	
        return variableParts.getVariablePart();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("VariableMessage [");

        sb.append("elementaryMessageID=");
        sb.append(elementaryMessageID);
        if (this.variableParts != null && this.variableParts.getVariablePart()!=null) {
            sb.append(", variableParts=[");
            for (VariablePart val : this.variableParts.getVariablePart()) {
                if (val != null) {
                    sb.append("variablePart=[");
                    sb.append(val.toString());
                    sb.append("], ");
                }
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(elementaryMessageID==null)
			throw new ASNParsingComponentException("elementary message ID should be set for variable message", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(variableParts==null || variableParts.getVariablePart()==null || variableParts.getVariablePart().size()==0)
			throw new ASNParsingComponentException("variable parts should be set for variable message", ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(variableParts.getVariablePart().size()>5)
			throw new ASNParsingComponentException("variable parts should have between 1 and 5 items for variable message", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}