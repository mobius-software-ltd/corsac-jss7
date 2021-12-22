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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallDiversionTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallingPartyRestrictionIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ConferenceTreatmentIndicator;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ForwardServiceInteractionInd;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ForwardServiceInteractionIndImpl implements ForwardServiceInteractionInd {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNConferenceTreatmentIndicatorImpl conferenceTreatmentIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ASNCallDiversionTreatmentIndicatorImpl callDiversionTreatmentIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
    private ASNCallingPartyRestrictionTreatmentIndicatorImpl callingPartyRestrictionIndicator;

    public ForwardServiceInteractionIndImpl() {
    }

    public ForwardServiceInteractionIndImpl(ConferenceTreatmentIndicator conferenceTreatmentIndicator,
            CallDiversionTreatmentIndicator callDiversionTreatmentIndicator, CallingPartyRestrictionIndicator callingPartyRestrictionIndicator) {
        if(conferenceTreatmentIndicator!=null) {
        	this.conferenceTreatmentIndicator = new ASNConferenceTreatmentIndicatorImpl();
        	this.conferenceTreatmentIndicator.setType(conferenceTreatmentIndicator);
        }
        
        if(callDiversionTreatmentIndicator!=null) {
        	this.callDiversionTreatmentIndicator = new ASNCallDiversionTreatmentIndicatorImpl();
        	this.callDiversionTreatmentIndicator.setType(callDiversionTreatmentIndicator);
        }
        
        if(callingPartyRestrictionIndicator!=null) {
        	this.callingPartyRestrictionIndicator = new ASNCallingPartyRestrictionTreatmentIndicatorImpl();
        	this.callingPartyRestrictionIndicator.setType(callingPartyRestrictionIndicator);
        }
    }

    public ConferenceTreatmentIndicator getConferenceTreatmentIndicator() {
    	if(conferenceTreatmentIndicator==null)
    		return null;
    	
        return conferenceTreatmentIndicator.getType();
    }

    public CallDiversionTreatmentIndicator getCallDiversionTreatmentIndicator() {
    	if(callDiversionTreatmentIndicator==null)
    		return null;
    	
        return callDiversionTreatmentIndicator.getType();
    }

    public CallingPartyRestrictionIndicator getCallingPartyRestrictionIndicator() {
    	if(callingPartyRestrictionIndicator==null)
    		return null;
    	
        return callingPartyRestrictionIndicator.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ForwardServiceInteractionInd [");

        if (this.conferenceTreatmentIndicator != null && this.conferenceTreatmentIndicator.getType()!=null) {
            sb.append("conferenceTreatmentIndicator=");
            sb.append(conferenceTreatmentIndicator.getType());
            sb.append(", ");
        }
        if (this.callDiversionTreatmentIndicator != null && this.callDiversionTreatmentIndicator.getType()!=null) {
            sb.append("callDiversionTreatmentIndicator=");
            sb.append(callDiversionTreatmentIndicator.getType());
            sb.append(", ");
        }
        if (this.callingPartyRestrictionIndicator != null) {
            sb.append("callingPartyRestrictionIndicator=");
            sb.append(callingPartyRestrictionIndicator.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
