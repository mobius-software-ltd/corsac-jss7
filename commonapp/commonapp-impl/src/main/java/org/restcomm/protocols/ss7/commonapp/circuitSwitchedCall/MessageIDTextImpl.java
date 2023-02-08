/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageIDText;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNIA5String;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class MessageIDTextImpl implements MessageIDText {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNIA5String messageContent;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNOctetString attributes;

    public MessageIDTextImpl() {
    }

    public MessageIDTextImpl(String messageContent, ByteBuf attributes) {
        if(messageContent!=null)
        	this.messageContent = new ASNIA5String(messageContent,"MessageContent",1,127,false);
        	
        if(attributes!=null)
        	this.attributes = new ASNOctetString(attributes,"Attributes",2,10,false);        
    }

    public String getMessageContent() {
    	if(messageContent==null)
    		return null;
    	
        return messageContent.getValue();
    }

    public ByteBuf getAttributes() {
    	if(attributes==null)
    		return null;
    	
    	return attributes.getValue();    	
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MessageIDText [");

        if (this.messageContent != null) {
            sb.append("messageContent=[");
            sb.append(messageContent);
            sb.append("]");
        }
        
        if (getAttributes() != null) {
            sb.append(", attributes=");
            sb.append(attributes.printDataArr());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(messageContent==null)
			throw new ASNParsingComponentException("message content should be set for text message ID", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
