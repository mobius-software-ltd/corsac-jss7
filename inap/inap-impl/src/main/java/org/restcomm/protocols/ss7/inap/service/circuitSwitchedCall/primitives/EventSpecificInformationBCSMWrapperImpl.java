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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
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
    		else if(eventSpecificInformationBCSM.getCollectedInfoSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getCollectedInfoSpecificInfo());
    		else if(eventSpecificInformationBCSM.getAnalyzedInfoSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getAnalyzedInfoSpecificInfo());
    		else if(eventSpecificInformationBCSM.getOAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOAnswerSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getOCalledPartyBusySpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOCalledPartyBusySpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getODisconnectSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getODisconnectSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getOMidCallSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOMidCallSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getONoAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getONoAnswerSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getRouteSelectFailureSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getRouteSelectFailureSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getOCalledPartyNotReachableSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOCalledPartyNotReachableSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getOAlertingSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getOAlertingSpecificInfo(),false);
    		else if(eventSpecificInformationBCSM.getTAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTAnswerSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTBusySpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTBusySpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTDisconnectSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTDisconnectSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTMidCallSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTMidCallSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTNoAnswerSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTNoAnswerSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTRouteSelectFailureSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getRouteSelectFailureSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTCalledPartyNotReachableSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTCalledPartyNotReachableSpecificInfo(),true);
    		else if(eventSpecificInformationBCSM.getTAlertingSpecificInfo()!=null)
    			this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMImpl(eventSpecificInformationBCSM.getTAlertingSpecificInfo(),true);
    		
    	}
    }

    public EventSpecificInformationBCSM getEventSpecificInformationBCSM() {
    	return eventSpecificInformationBCSM;
    }
}
