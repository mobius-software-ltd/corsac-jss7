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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
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
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GPRSEventImpl implements GPRSEvent {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNGPRSEventTypeImpl gprsEventType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNMonitorMode monitorMode;

    public GPRSEventImpl() {
    }

    public GPRSEventImpl(GPRSEventType gprsEventType, MonitorMode monitorMode) {
        if(gprsEventType!=null)
        	this.gprsEventType = new ASNGPRSEventTypeImpl(gprsEventType);
        	
        if(monitorMode!=null)
        	this.monitorMode = new ASNMonitorMode(monitorMode);        	
    }

    public GPRSEventType getGPRSEventType() {
    	if(this.gprsEventType==null)
    		return null;
    	
        return this.gprsEventType.getType();
    }

    public MonitorMode getMonitorMode() {
    	if(this.monitorMode==null)
    		return null;
    	
        return this.monitorMode.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSEvent [");

        if (this.gprsEventType != null) {
            sb.append("gprsEventType=");
            sb.append(this.gprsEventType.getType());
            sb.append(", ");
        }

        if (this.monitorMode != null) {
            sb.append("monitorMode=");
            sb.append(this.monitorMode.getType());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gprsEventType==null)
			throw new ASNParsingComponentException("gprs event type should be set for gprs event", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(monitorMode==null)
			throw new ASNParsingComponentException("monitor mode should be set for gprs event", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
