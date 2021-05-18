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
package org.restcomm.protocols.ss7.cap.service.sms;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.RequestReportSMSEventRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEventImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSEventWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestReportSMSEventRequestImpl extends SmsMessageImpl implements RequestReportSMSEventRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private SMSEventWrapperImpl smsEvents;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;

    public RequestReportSMSEventRequestImpl(List<SMSEventImpl> smsEvents, CAPExtensionsImpl extensions) {
        super();
        
        if(smsEvents!=null)
        	this.smsEvents = new SMSEventWrapperImpl(smsEvents);
        
        this.extensions = extensions;
    }

    public RequestReportSMSEventRequestImpl() {
        super();
    }

    @Override
    public List<SMSEventImpl> getSMSEvents() {
    	if(this.smsEvents==null)
    		return null;
    	
        return this.smsEvents.getSMSEvents();
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.requestReportSMSEvent_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.requestReportSMSEvent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestReportSMSEventRequest [");
        this.addInvokeIdInfo(sb);

        if (this.smsEvents != null && this.smsEvents.getSMSEvents()!=null) {
            sb.append(", smsEvents=[");
            int i1 = 0;
            for (SMSEventImpl evt : this.smsEvents.getSMSEvents()) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append("smsEvent=");
                sb.append(evt.toString());
            }
            sb.append("]");
        }

        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(this.extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
