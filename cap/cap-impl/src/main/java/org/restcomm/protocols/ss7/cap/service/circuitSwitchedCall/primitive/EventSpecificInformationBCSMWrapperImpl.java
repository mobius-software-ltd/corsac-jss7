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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class EventSpecificInformationBCSMWrapperImpl {
	@ASNChoise
	private EventSpecificInformationBCSMImpl eventSpecificInformationBCSM;

    public EventSpecificInformationBCSMWrapperImpl() {
    }

    public EventSpecificInformationBCSMWrapperImpl(EventSpecificInformationBCSM eventSpecificInformationBCSM) {
    	if(eventSpecificInformationBCSM!=null) {
    		if(eventSpecificInformationBCSM instanceof EventSpecificInformationBCSMImpl)
    			this.eventSpecificInformationBCSM = (EventSpecificInformationBCSMImpl)eventSpecificInformationBCSM;
    		else if(eventSpecificInformationBCSM.getCallAcceptedSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getCallAcceptedSpecificInfo());
    		else if(eventSpecificInformationBCSM.getDpSpecificInfoAlt()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getDpSpecificInfoAlt());
    		else if(eventSpecificInformationBCSM.getOAbandonSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOAbandonSpecificInfo());
    		else if(eventSpecificInformationBCSM.getOAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOAnswerSpecificInfo());
    		else if(eventSpecificInformationBCSM.getOCalledPartyBusySpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOCalledPartyBusySpecificInfo());
    		else if(eventSpecificInformationBCSM.getOChangeOfPositionSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOChangeOfPositionSpecificInfo());
    		else if(eventSpecificInformationBCSM.getODisconnectSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getODisconnectSpecificInfo());
    		else if(eventSpecificInformationBCSM.getOMidCallSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOMidCallSpecificInfo());
    		else if(eventSpecificInformationBCSM.getONoAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getONoAnswerSpecificInfo());
    		else if(eventSpecificInformationBCSM.getOTermSeizedSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOTermSeizedSpecificInfo());
    		else if(eventSpecificInformationBCSM.getRouteSelectFailureSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getRouteSelectFailureSpecificInfo());
    		else if(eventSpecificInformationBCSM.getTAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTAnswerSpecificInfo());
    		else if(eventSpecificInformationBCSM.getTBusySpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTBusySpecificInfo());
    		else if(eventSpecificInformationBCSM.getTChangeOfPositionSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTChangeOfPositionSpecificInfo());
    		else if(eventSpecificInformationBCSM.getTDisconnectSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTDisconnectSpecificInfo());
    		else if(eventSpecificInformationBCSM.getTMidCallSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTMidCallSpecificInfo());
    		else if(eventSpecificInformationBCSM.getTNoAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTNoAnswerSpecificInfo());
    	}
    }

    public EventSpecificInformationBCSM getEventSpecificInformationBCSM() {
    	return eventSpecificInformationBCSM;
    }
}
