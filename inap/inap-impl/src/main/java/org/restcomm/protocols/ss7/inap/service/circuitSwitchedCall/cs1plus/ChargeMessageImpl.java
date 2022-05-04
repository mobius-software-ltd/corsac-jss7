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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.charging.EventTypeCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeMessage;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.EventSpecificInfoCharging;
import org.restcomm.protocols.ss7.inap.charging.ASNEventTypeCharging;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargeMessageImpl implements ChargeMessage {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNEventTypeCharging eventTypeCharging;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1)
    private EventSpecificInfoChargingWrapperImpl eventSpecificInfoCharging;

    public ChargeMessageImpl() {
    }

    public ChargeMessageImpl(EventTypeCharging eventTypeCharging, EventSpecificInfoCharging eventSpecificInfoCharging) {
    	if(eventTypeCharging!=null)
    		this.eventTypeCharging=new ASNEventTypeCharging(eventTypeCharging);
    		
    	if(eventSpecificInfoCharging!=null)
    		this.eventSpecificInfoCharging=new EventSpecificInfoChargingWrapperImpl(eventSpecificInfoCharging);
    }

    public EventTypeCharging getEventTypeCharging() {
    	if(eventTypeCharging==null)
    		return null;
    	
    	return eventTypeCharging.getType();
    }

    public EventSpecificInfoCharging getEventSpecificInfoCharging() {
    	if(eventSpecificInfoCharging==null)
    		return null;
    	
    	return eventSpecificInfoCharging.getEventSpecificInfoCharging();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargeMessage [");

        if (this.eventTypeCharging != null && this.eventTypeCharging.getType()!=null) {
            sb.append(", eventTypeCharging="); 
        	sb.append(eventTypeCharging.getType());
        }
        
        if (this.eventSpecificInfoCharging != null && this.eventSpecificInfoCharging.getEventSpecificInfoCharging()!=null) {
            sb.append(", eventSpecificInfoCharging=");
            sb.append(eventSpecificInfoCharging.getEventSpecificInfoCharging());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(eventTypeCharging==null)
			throw new ASNParsingComponentException("event type charging should be set for charge message", ASNParsingComponentExceptionReason.MistypedParameter);

		if(eventSpecificInfoCharging==null)
			throw new ASNParsingComponentException("event specific info should be set for charge message", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}