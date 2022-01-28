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

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageID;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageIDText;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.VariableMessage;
import org.restcomm.protocols.ss7.commonapp.primitives.IntegerListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class MessageIDImpl implements MessageID {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger elementaryMessageID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = MessageIDTextImpl.class)
    private MessageIDText text;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 29,constructed = true,index = -1)
    private IntegerListWrapperImpl elementaryMessageIDs;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = true,index = -1, defaultImplementation = VariableMessageImpl.class)
    private VariableMessage variableMessage;

    public MessageIDImpl() {
    }

    public MessageIDImpl(Integer elementaryMessageID) {
    	if(elementaryMessageID!=null)
    		this.elementaryMessageID = new ASNInteger(elementaryMessageID);    		
    }

    public MessageIDImpl(MessageIDText text) {
        this.text = text;
    }

    public MessageIDImpl(List<Integer> elementaryMessageIDs) {
    	if(elementaryMessageIDs!=null) {
    		List<ASNInteger> wrappedList=new ArrayList<ASNInteger>();
    		for(Integer curr:elementaryMessageIDs) {
    			ASNInteger currValue=new ASNInteger(curr);
    			wrappedList.add(currValue);
    		}
    		
    		this.elementaryMessageIDs = new IntegerListWrapperImpl(wrappedList);
    	}
    }

    public MessageIDImpl(VariableMessage variableMessage) {
        this.variableMessage = variableMessage;
    }

    public Integer getElementaryMessageID() {
    	if(elementaryMessageID==null)
    		return null;
    	
        return elementaryMessageID.getIntValue();
    }

    public MessageIDText getText() {
        return text;
    }

    public List<Integer> getElementaryMessageIDs() {
    	if(elementaryMessageIDs==null || elementaryMessageIDs.getValues()==null)
    		return null;
    	
    	List<Integer> result=new ArrayList<Integer>();
    	for(ASNInteger curr:elementaryMessageIDs.getValues())
    		result.add(curr.getIntValue());
    	
        return result;
    }

    public VariableMessage getVariableMessage() {
        return variableMessage;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MessageID [");

        if (this.elementaryMessageID != null && this.elementaryMessageID.getValue()!=null) {
            sb.append("elementaryMessageID=");
            sb.append(elementaryMessageID.getValue());
        }
        if (this.text != null) {
            sb.append(" text=");
            sb.append(text.toString());
        }
        if (this.elementaryMessageIDs != null && this.elementaryMessageIDs.getValues()!=null) {
            sb.append(" elementaryMessageIDs=[");
            for (ASNInteger val : this.elementaryMessageIDs.getValues()) {
                if (val != null) {
                    sb.append(val.getValue());
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        if (this.variableMessage != null) {
            sb.append(" variableMessage=");
            sb.append(variableMessage.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}