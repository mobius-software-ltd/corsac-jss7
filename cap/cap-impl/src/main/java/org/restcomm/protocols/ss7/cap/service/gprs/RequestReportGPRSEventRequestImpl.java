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

package org.restcomm.protocols.ss7.cap.service.gprs;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.RequestReportGPRSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEvent;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GprsEventWrapperImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;

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
public class RequestReportGPRSEventRequestImpl extends GprsMessageImpl implements RequestReportGPRSEventRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private GprsEventWrapperImpl gprsEvent;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;

    public RequestReportGPRSEventRequestImpl() {
        super();
    }

    public RequestReportGPRSEventRequestImpl(List<GPRSEvent> gprsEvent, PDPID pdpID) {
        super();
        
        if(gprsEvent!=null)
        	this.gprsEvent = new GprsEventWrapperImpl(gprsEvent);
        
        this.pdpID = pdpID;
    }

    @Override
    public List<GPRSEvent> getGPRSEvent() {
    	if(this.gprsEvent==null)
    		return null;
    	
        return this.gprsEvent.getGPRSEvents();
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.requestReportGPRSEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.requestReportGPRSEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestReportGPRSEventRequest [");
        this.addInvokeIdInfo(sb);

        if (this.gprsEvent != null && this.gprsEvent.getGPRSEvents()!=null) {
            sb.append(", gprsEvent=[");
            boolean firstItem = true;
            for (GPRSEvent be : this.gprsEvent.getGPRSEvents()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("]");
        }

        if (this.pdpID != null) {
            sb.append(", pdpID=");
            sb.append(this.pdpID.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gprsEvent==null || gprsEvent.getGPRSEvents()==null || gprsEvent.getGPRSEvents().size()==0)
			throw new ASNParsingComponentException("gprs events should be set for request report gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);
		
		if(gprsEvent.getGPRSEvents().size()>10)
			throw new ASNParsingComponentException("gprs events size should be between 1 and 10 for request report gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
