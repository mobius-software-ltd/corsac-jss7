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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNMonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.charging.EventTypeCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
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
public class ChargingEventImpl implements ChargingEvent {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNEventTypeCharging eventTypeCharging;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNMonitorMode monitorMode;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1)
    private LegIDWrapperImpl legID;

    public ChargingEventImpl() {
    }

    public ChargingEventImpl(EventTypeCharging eventTypeCharging,MonitorMode monitorMode,LegID legID) {
    	if(eventTypeCharging!=null)
    		this.eventTypeCharging=new ASNEventTypeCharging(eventTypeCharging);
    		
    	if(monitorMode!=null)
    		this.monitorMode=new ASNMonitorMode(monitorMode);
    		
    	if(legID!=null)
    		this.legID=new LegIDWrapperImpl(legID);    	
    }

    public EventTypeCharging getEventTypeCharging() {
    	if(eventTypeCharging==null || eventTypeCharging.getType()==null)
    		return null;
    	
    	return eventTypeCharging.getType();
    }

    public MonitorMode getMonitorMode() {
    	if(monitorMode==null)
    		return null;
    	
    	return monitorMode.getType();
    }

    public LegID getLegID() {
    	if(legID==null)
    		return null;
    	
    	return legID.getLegID();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingEvent [");

        if (this.eventTypeCharging != null && this.eventTypeCharging.getType()!=null) {
            sb.append(", eventTypeCharging=");
            sb.append(eventTypeCharging.getType());
        }

        if (this.monitorMode != null || this.monitorMode.getType()!=null) {
            sb.append(", monitorMode=");
            sb.append(this.monitorMode.getType());
        }

        if (this.legID != null || this.legID.getLegID()!=null) {
            sb.append(", legID=");
            sb.append(this.legID.getLegID());
        }
                
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(eventTypeCharging==null)
			throw new ASNParsingComponentException("event type charging should be set for charging event", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(monitorMode==null)
			throw new ASNParsingComponentException("monitor mode should be set for charging event", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}