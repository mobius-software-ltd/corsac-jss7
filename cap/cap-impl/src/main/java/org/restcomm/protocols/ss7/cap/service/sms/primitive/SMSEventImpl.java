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
package org.restcomm.protocols.ss7.cap.service.sms.primitive;

import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEvent;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNMonitorMode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16, constructed = true, lengthIndefinite = false)
public class SMSEventImpl implements SMSEvent {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNEventTypeSMSImpl eventTypeSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNMonitorMode monitorMode;

    public EventTypeSMS getEventTypeSMS() {
    	if(this.eventTypeSMS==null)
    		return null;
    	
        return this.eventTypeSMS.getType();
    }

    public MonitorMode getMonitorMode() {
    	if(this.monitorMode==null)
    		return null;
    	
        return this.monitorMode.getType();
    }

    public SMSEventImpl() {
    }

    public SMSEventImpl(EventTypeSMS eventTypeSMS, MonitorMode monitorMode) {
    	if(eventTypeSMS!=null)
    		this.eventTypeSMS = new ASNEventTypeSMSImpl(eventTypeSMS);
    		
    	if(monitorMode!=null)
    		this.monitorMode = new ASNMonitorMode(monitorMode);    		
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMSEvent [");

        if (this.eventTypeSMS != null) {
            sb.append("eventTypeSMS=");
            sb.append(this.eventTypeSMS.toString());
        }

        if (this.monitorMode != null) {
            sb.append(", monitorMode=");
            sb.append(this.monitorMode.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(eventTypeSMS==null)
			throw new ASNParsingComponentException("event type sms should be set for sms event", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(monitorMode==null)
			throw new ASNParsingComponentException("monitor mode should be set for sms event", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
