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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InbandInfo;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.MessageID;

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
        
        if(numberOfRepetitions!=null) {
        	this.numberOfRepetitions = new ASNInteger();
        	this.numberOfRepetitions.setValue(numberOfRepetitions.longValue());
        }
        
        if(duration!=null) {
        	this.duration = new ASNInteger();
        	this.duration.setValue(duration.longValue());
        }
        
        if(interval!=null) {
        	this.interval = new ASNInteger();
        	this.interval.setValue(interval.longValue());
        }
    }

    public MessageID getMessageID() {
    	if(messageID==null)
    		return null;
    	
        return messageID.getMessageID();
    }

    public Integer getNumberOfRepetitions() {
    	if(numberOfRepetitions==null || numberOfRepetitions.getValue()==null)
    		return null;
    	
        return numberOfRepetitions.getValue().intValue();           
    }

    public Integer getDuration() {
    	if(duration==null || duration.getValue()==null)
    		return null;
    	
        return duration.getValue().intValue();     
    }

    public Integer getInterval() {
    	if(interval==null || interval.getValue()==null)
    		return null;
    	
        return interval.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("InbandInfo [");

        if (this.messageID != null) {
            sb.append("messageID=");
            sb.append(messageID.toString());
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
}
