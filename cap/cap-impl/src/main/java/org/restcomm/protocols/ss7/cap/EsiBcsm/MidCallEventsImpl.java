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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.MidCallEvents;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 16, constructed = true,lengthIndefinite = false)
public class MidCallEventsImpl implements MidCallEvents {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup dtmfDigitsCompleted;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false, index = -1, defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup dtmfDigitsTimeOut;

    public MidCallEventsImpl() {
    }

    public MidCallEventsImpl(DigitsIsup dtmfDigits, boolean isDtmfDigitsCompleted) {
        if (isDtmfDigitsCompleted)
            dtmfDigitsCompleted = dtmfDigits;
        else
            dtmfDigitsTimeOut = dtmfDigits;
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
		if(dtmfDigitsCompleted==null && dtmfDigitsTimeOut==null)
			throw new ASNParsingComponentException("one of child items should be set for mid call events", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
