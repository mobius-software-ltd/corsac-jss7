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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.ResetTimerSMSRequest;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNTimerID;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;

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
public class ResetTimerSMSRequestImpl extends SmsMessageImpl implements ResetTimerSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerID timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public ResetTimerSMSRequestImpl(TimerID timerID, int timerValue, CAPINAPExtensions extensions) {
        super();
        
        if(timerID!=null)
        	this.timerID = new ASNTimerID(timerID);
        	
        this.timerValue = new ASNInteger(timerValue,"TimerValue",0,Integer.MAX_VALUE,false);
        this.extensions = extensions;
    }

    public ResetTimerSMSRequestImpl() {
        super();
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
    		return 0;
    	
        return this.timerValue.getIntValue();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.resetTimerSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.resetTimerSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResetTimerSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.timerID != null && this.timerID.getType()!=null) {
            sb.append(", timerID=");
            sb.append(timerID.getType());
        }
        
        if(timerValue!=null && timerValue.getValue()!=null) {
        	sb.append(", timerValue=");
        	sb.append(timerValue);
        } else
        	sb.append(", timerValue=0");
        
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timerValue==null)
			throw new ASNParsingComponentException("timer value should be set for reset timer sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
