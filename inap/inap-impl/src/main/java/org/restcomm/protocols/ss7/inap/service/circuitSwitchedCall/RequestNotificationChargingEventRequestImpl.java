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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestNotificationChargingEventRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ChargingEvent;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ChargingEventListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class RequestNotificationChargingEventRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestNotificationChargingEventRequest {
	private static final long serialVersionUID = 1L;

	private ChargingEventListWrapperImpl chargingEvents;
    
    public RequestNotificationChargingEventRequestImpl() {
    }

    public RequestNotificationChargingEventRequestImpl(List<ChargingEvent> chargingEvents) {
    	if(chargingEvents!=null)
    		this.chargingEvents = new ChargingEventListWrapperImpl(chargingEvents);    	
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.requestNotificationChargingEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.requestNotificationChargingEvent;
    }

    @Override
    public List<ChargingEvent> getChargingEventList() {
    	if(chargingEvents==null)
    		return null;
    	
        return chargingEvents.getChargingEvents();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestNotificationChargingEventRequest [");
        this.addInvokeIdInfo(sb);

        if (this.chargingEvents != null && this.chargingEvents.getChargingEvents()!=null) {
            sb.append(", chargingEvents=[");
            boolean firstItem = true;
            for (ChargingEvent be : this.chargingEvents.getChargingEvents()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}