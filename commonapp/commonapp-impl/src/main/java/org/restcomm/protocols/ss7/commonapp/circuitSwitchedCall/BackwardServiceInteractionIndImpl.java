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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BackwardServiceInteractionInd;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallCompletionTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class BackwardServiceInteractionIndImpl implements BackwardServiceInteractionInd {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNConferenceTreatmentIndicatorImpl conferenceTreatmentIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNCallCompletionTreatmentIndicatorImpl callCompletionTreatmentIndicator;

    public BackwardServiceInteractionIndImpl() {
    }

    public BackwardServiceInteractionIndImpl(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallCompletionTreatmentIndicator callCompletionTreatmentIndicator) {
    	if(conferenceTreatmentIndicator!=null)
    		this.conferenceTreatmentIndicator = new ASNConferenceTreatmentIndicatorImpl(conferenceTreatmentIndicator);
    		
    	if(callCompletionTreatmentIndicator!=null)
    		this.callCompletionTreatmentIndicator = new ASNCallCompletionTreatmentIndicatorImpl(callCompletionTreatmentIndicator);    		
    }

    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
    	if(conferenceTreatmentIndicator==null)
    		return null;
    	
        return conferenceTreatmentIndicator.getType();
    }

    public CallCompletionTreatmentIndicator getCallCompletionTreatmentIndicator() {
    	if(callCompletionTreatmentIndicator==null)
    		return null;
    	
        return callCompletionTreatmentIndicator.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("BackwardServiceInteractionInd [");

        if (this.conferenceTreatmentIndicator != null && this.conferenceTreatmentIndicator.getType() != null) {
            sb.append("conferenceTreatmentIndicator=");
            sb.append(conferenceTreatmentIndicator.getType().toString());
            sb.append(", ");
        }
        if (this.callCompletionTreatmentIndicator != null && this.callCompletionTreatmentIndicator.getType() != null) {
            sb.append("callCompletionTreatmentIndicator=");
            sb.append(callCompletionTreatmentIndicator.getType().toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
