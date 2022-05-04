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

package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.ResetTimerGPRSRequest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNTimerID;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResetTimerGPRSRequestImpl extends GprsMessageImpl implements ResetTimerGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerID timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;

    public ResetTimerGPRSRequestImpl() {        
    }

    public ResetTimerGPRSRequestImpl(TimerID timerID, int timerValue) {
        super();
        
        if(timerID!=null)
        	this.timerID = new ASNTimerID(timerID);
        	
        this.timerValue = new ASNInteger(timerValue,"TimerValue",0,Integer.MAX_VALUE,false);        
    }

    @Override
    public TimerID getTimerID() {
    	if(this.timerID==null)
    		return TimerID.tssf;
    	
        return this.timerID.getType();
    }

    @Override
    public int getTimerValue() {
    	if(this.timerValue==null || this.timerValue.getValue()==null)
    		return -1;
    	
        return this.timerValue.getIntValue();
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timerValue==null)
			throw new ASNParsingComponentException("timer value should be set for reset timer gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}