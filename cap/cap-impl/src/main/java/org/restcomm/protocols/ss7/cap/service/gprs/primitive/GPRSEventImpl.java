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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.primitives.ASNMonitorModeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GPRSEventImpl implements GPRSEvent {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNGPRSEventTypeImpl gprsEventType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNMonitorModeImpl monitorMode;

    public GPRSEventImpl() {
    }

    public GPRSEventImpl(GPRSEventType gprsEventType, MonitorMode monitorMode) {
        if(gprsEventType!=null) {
        	this.gprsEventType = new ASNGPRSEventTypeImpl();
        	this.gprsEventType.setType(gprsEventType);
        }
        
        if(monitorMode!=null) {
        	this.monitorMode = new ASNMonitorModeImpl();
        	this.monitorMode.setType(monitorMode);
        }
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

}
