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
package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNTimerIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResetTimerGPRSRequestImpl extends GprsMessageImpl implements ResetTimerGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerIDImpl timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;

    public ResetTimerGPRSRequestImpl() {        
    }

    public ResetTimerGPRSRequestImpl(TimerID timerID, int timerValue) {
        super();
        
        if(timerID!=null) {
        	this.timerID = new ASNTimerIDImpl();
        	this.timerID.setType(timerID);
        }
        
        this.timerValue = new ASNInteger();
        this.timerValue.setValue(Long.valueOf(timerValue));
    }

    @Override
    public TimerID getTimerID() {
    	if(this.timerID==null)
    		return null;
    	
        return this.timerID.getType();
    }

    @Override
    public int getTimerValue() {
    	if(this.timerValue==null || this.timerValue.getValue()==null)
    		return -1;
    	
        return this.timerValue.getValue().intValue();
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.resetTimerGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.resetTimerGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ResetTimerGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.timerID != null) {
            sb.append(", timerID=");
            sb.append(this.timerID.toString());
        }

        sb.append(", timerValue=");
        sb.append(this.timerValue);

        sb.append("]");

        return sb.toString();
    }
}