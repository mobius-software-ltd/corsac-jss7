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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InbandInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.MessageID;

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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class InbandInfoImpl implements InbandInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private MessageIDWrapperImpl messageID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger numberOfRepetitions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNInteger duration;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNInteger interval;

    public InbandInfoImpl() {
    }

    public InbandInfoImpl(MessageID messageID, Integer numberOfRepetitions, Integer duration, Integer interval) {
    	if(messageID!=null)
    		this.messageID = new MessageIDWrapperImpl(messageID);
        
        if(numberOfRepetitions!=null)
        	this.numberOfRepetitions = new ASNInteger(numberOfRepetitions,"NumberOfRepetitions",1,127,false);        	
        
        if(duration!=null)
        	this.duration = new ASNInteger(duration,"Duration",0,32767,false);
        	
        if(interval!=null)
        	this.interval = new ASNInteger(interval,"Interval",0,32767,false);        	
    }

    public MessageID getMessageID() {
    	if(messageID==null)
    		return null;
    	
        return messageID.getMessageID();
    }

    public Integer getNumberOfRepetitions() {
    	if(numberOfRepetitions==null)
    		return null;
    	
        return numberOfRepetitions.getIntValue();           
    }

    public Integer getDuration() {
    	if(duration==null)
    		return null;
    	
        return duration.getIntValue();     
    }

    public Integer getInterval() {
    	if(interval==null)
    		return null;
    	
        return interval.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InbandInfo [");

        if (this.messageID != null && this.messageID.getMessageID()!=null) {
            sb.append("messageID=");
            sb.append(messageID.getMessageID().toString());
        }
        if (this.numberOfRepetitions != null) {
            sb.append(", numberOfRepetitions=");
            sb.append(numberOfRepetitions.getValue());
        }
        if (this.duration != null) {
            sb.append(", duration=");
            sb.append(duration.getValue());
        }
        if (this.interval != null) {
            sb.append(", interval=");
            sb.append(interval.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(messageID==null)
			throw new ASNParsingComponentException("message ID should be set for inband info", ASNParsingComponentExceptionReason.MistypedParameter);		    				
	}
}
