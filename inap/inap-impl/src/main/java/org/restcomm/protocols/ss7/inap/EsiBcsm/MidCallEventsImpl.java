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

package org.restcomm.protocols.ss7.inap.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.MidCallEvents;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 16, constructed = true,lengthIndefinite = false)
public class MidCallEventsImpl implements MidCallEvents {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index = -1)
	private ASNNull flash;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index = -1)
	private ASNNull userCallSuspend;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index = -1)
	private ASNNull userCallResume;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup dtmfDigitsCompleted;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup dtmfDigitsTimeOut;

    public MidCallEventsImpl() {
    }

    public MidCallEventsImpl(boolean userCallSuspend,boolean userCallResume) {
    	if(userCallResume)
    		this.userCallResume=new ASNNull();
    	else if(userCallSuspend)
    		this.userCallSuspend=new ASNNull();
    	else
    		this.flash=new ASNNull();
    }
    
    public MidCallEventsImpl(DigitsIsup dtmfDigits, boolean isDtmfDigitsCompleted) {
        if (isDtmfDigitsCompleted)
            dtmfDigitsCompleted = dtmfDigits;
        else
            dtmfDigitsTimeOut = dtmfDigits;
    }

    public boolean getFlash() {
    	return flash!=null;
    }
    
    public boolean getUserCallSuspend() {
    	return userCallSuspend!=null;
    }
    
    public boolean getUserCallResume() {
    	return userCallResume!=null;
    }
    
    public DigitsIsup getDTMFDigitsCompleted() {
    	if(dtmfDigitsCompleted!=null)
    		dtmfDigitsCompleted.setIsGenericDigits();
    	
        return dtmfDigitsCompleted;
    }

    public DigitsIsup getDTMFDigitsTimeOut() {
    	if(dtmfDigitsTimeOut!=null)
    		dtmfDigitsTimeOut.setIsGenericDigits();
    	
        return dtmfDigitsTimeOut;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("MidCallEvents [");

        if(flash!=null) {
        	sb.append("flash");            
        }
        if(userCallResume!=null) {
        	sb.append("userCallResume");            
        }
        if(userCallSuspend!=null) {
        	sb.append("userCallSuspend");            
        }
        if (dtmfDigitsCompleted != null) {
            sb.append("dtmfDigitsCompleted=[");
            sb.append(dtmfDigitsCompleted.toString());
            sb.append("]");
        } else if (dtmfDigitsTimeOut != null) {
            sb.append("dtmfDigitsTimeOut=[");
            sb.append(dtmfDigitsTimeOut.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(flash==null && userCallSuspend==null && userCallResume==null && dtmfDigitsCompleted==null && dtmfDigitsTimeOut==null)
			throw new ASNParsingComponentException("one if child properties should be set for mid call events", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
