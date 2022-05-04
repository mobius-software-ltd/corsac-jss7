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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNMonitorMode;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventNotificationChargingRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
  * @author yulian.oifa
*
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventNotificationChargingRequestImpl extends CircuitSwitchedCallMessageImpl implements EventNotificationChargingRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNOctetString eventTypeCharging;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNOctetString eventSpecificInformationCharging;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private LegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 30,constructed = false,index = -1)
    private ASNMonitorMode monitorMode;
    
	public EventNotificationChargingRequestImpl() {
    }

    public EventNotificationChargingRequestImpl(ByteBuf eventTypeCharging, ByteBuf eventSpecificInformationCharging, LegID legID, 
    		CAPINAPExtensions extensions, MonitorMode monitorMode) {
    	    	
    	if(eventTypeCharging!=null)
    		this.eventTypeCharging = new ASNOctetString(eventTypeCharging,"EventTypeCharging",null,null,false);
    	
    	if(eventSpecificInformationCharging!=null)
    		this.eventSpecificInformationCharging = new ASNOctetString(eventSpecificInformationCharging,"EventSpecificInformationCharging",null,null,false);
    	
    	if(legID!=null)
        	this.legID = new LegIDWrapperImpl(legID);
        
        this.extensions = extensions;
    	
        if(monitorMode!=null)
        	this.monitorMode = new ASNMonitorMode(monitorMode);        	
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.eventNotificationCharging_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.eventNotificationCharging;
    }

    @Override
    public ByteBuf getEventTypeCharging() {
    	if(eventTypeCharging==null)
    		return null;
    	
    	return eventTypeCharging.getValue();
    }

    @Override
    public ByteBuf getEventSpecificInformationCharging() {
    	if(eventSpecificInformationCharging==null)
    		return null;
    	
    	return eventSpecificInformationCharging.getValue();
    }

	@Override
    public LegID getLegID() {
    	if(legID==null || legID.getLegID()==null)
    		return null;
    	
        return legID.getLegID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public MonitorMode getMonitorMode() {
    	if(monitorMode==null)
    		return MonitorMode.notifyAndContinue;
    	
        return monitorMode.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventNotificationChargingRequest [");
        this.addInvokeIdInfo(sb);

        if (this.eventTypeCharging != null && this.eventTypeCharging.getValue()!=null) {
            sb.append(", eventTypeCharging=");
            sb.append(eventTypeCharging.printDataArr());
        }
        if (this.eventSpecificInformationCharging != null && this.eventSpecificInformationCharging.getValue()!=null) {
            sb.append(", eventSpecificInformationCharging=");
            sb.append(eventSpecificInformationCharging.printDataArr());
        }
        if (this.legID != null && this.legID.getLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getLegID());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.monitorMode != null && this.monitorMode.getType()!=null) {
            sb.append(", monitorMode=");
            sb.append(monitorMode.getType());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(eventTypeCharging==null)
			throw new ASNParsingComponentException("event type charging should be set for event notification charging request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
