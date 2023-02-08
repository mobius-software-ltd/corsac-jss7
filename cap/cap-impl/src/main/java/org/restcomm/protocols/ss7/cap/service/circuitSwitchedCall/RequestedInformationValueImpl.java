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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.cap.primitives.DateAndTimeImpl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.isup.CauseIsupImpl;

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
public class RequestedInformationValueImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=0, constructed = false,index = -1)
    private ASNInteger callAttemptElapsedTimeValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=1, constructed = false,index = -1, defaultImplementation = DateAndTimeImpl.class)
    private DateAndTime callStopTimeValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=2, constructed = false,index = -1)
    private ASNInteger callConnectedElapsedTimeValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag=30, constructed = false,index = -1, defaultImplementation = CauseIsupImpl.class)
    private CauseIsup releaseCauseValue;

    public RequestedInformationValueImpl() {
    }

    public RequestedInformationValueImpl(RequestedInformationType requestedInformationType, int intValue) {
        if (requestedInformationType == RequestedInformationType.callAttemptElapsedTime)
            this.callAttemptElapsedTimeValue = new ASNInteger(intValue,"CallAttemptElapsedTimeValue",0,255,false);
        else if(requestedInformationType == RequestedInformationType.callConnectedElapsedTime)
        	this.callConnectedElapsedTimeValue = new ASNInteger(intValue,"CallConnectedElapsedTimeValue",0,Integer.MAX_VALUE,false);        
    }

    public RequestedInformationValueImpl(DateAndTime callStopTimeValue) {
        this.callStopTimeValue = callStopTimeValue;
    }

    public RequestedInformationValueImpl(CauseIsup releaseCauseValue) {
        this.releaseCauseValue = releaseCauseValue;
    }

    public Integer getCallAttemptElapsedTimeValue() {
    	if(callAttemptElapsedTimeValue==null)
    		return null;
    	
        return callAttemptElapsedTimeValue.getIntValue();
    }

    public DateAndTime getCallStopTimeValue() {
        return callStopTimeValue;
    }

    public Integer getCallConnectedElapsedTimeValue() {
    	if(callConnectedElapsedTimeValue==null)
    		return null;
    	
        return callConnectedElapsedTimeValue.getIntValue();
    }

    public CauseIsup getReleaseCauseValue() {
        return releaseCauseValue;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestedInformationValue [");
       if (this.callAttemptElapsedTimeValue != null && this.callAttemptElapsedTimeValue.getValue()!=null) {
            sb.append(", callAttemptElapsedTimeValue=");
            sb.append(callAttemptElapsedTimeValue.getValue());
        }
        if (this.callStopTimeValue != null) {
            sb.append(", callStopTimeValue=");
            sb.append(callStopTimeValue.toString());
        }
        if (this.callConnectedElapsedTimeValue != null && this.callConnectedElapsedTimeValue.getValue()!=null) {
            sb.append(", callConnectedElapsedTimeValue=");
            sb.append(callConnectedElapsedTimeValue.getValue());
        }
        if (this.releaseCauseValue != null) {
            sb.append(", releaseCauseValue=");
            sb.append(releaseCauseValue.toString());
        }
        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(callAttemptElapsedTimeValue==null && callStopTimeValue==null && callConnectedElapsedTimeValue==null && releaseCauseValue==null)
			throw new ASNParsingComponentException("one of child items should be set for requested information value", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
