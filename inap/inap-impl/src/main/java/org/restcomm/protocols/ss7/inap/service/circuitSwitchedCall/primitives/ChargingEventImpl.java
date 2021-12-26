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

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNMonitorModeImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingEventImpl implements ChargingEvent {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNOctetString eventTypeCharging;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNMonitorModeImpl monitorMode;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1)
    private LegIDWrapperImpl legID;

    public ChargingEventImpl() {
    }

    public ChargingEventImpl(byte[] eventTypeCharging,MonitorMode monitorMode,LegID legID) {
    	if(eventTypeCharging!=null) {
    		this.eventTypeCharging=new ASNOctetString();
    		this.eventTypeCharging.setValue(Unpooled.wrappedBuffer(eventTypeCharging));
    	}
    	
    	if(monitorMode!=null) {
    		this.monitorMode=new ASNMonitorModeImpl();
    		this.monitorMode.setType(monitorMode);
    	}
    	
    	if(legID!=null)
    		this.legID=new LegIDWrapperImpl(legID);    	
    }

    public byte[] getEventTypeCharging() {
    	if(eventTypeCharging==null || eventTypeCharging.getValue()==null)
    		return null;
    	
    	ByteBuf value=eventTypeCharging.getValue();
    	byte[] data=new byte[value.readableBytes()];
    	value.readBytes(data);
    	return data;
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

        if (this.eventTypeCharging != null && this.eventTypeCharging.getValue()!=null) {
            sb.append(", eventTypeCharging=");
            sb.append(ASNOctetString.printDataArr(getEventTypeCharging()));
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
}