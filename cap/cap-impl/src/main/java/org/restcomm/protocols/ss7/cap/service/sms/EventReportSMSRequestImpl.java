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

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.ASNEventTypeSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMSImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMSWrapperImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.inap.primitives.MiscCallInfoImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventReportSMSRequestImpl extends SmsMessageImpl implements EventReportSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNEventTypeSMSImpl eventTypeSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private EventSpecificInformationSMSWrapperImpl eventSpecificInformationSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;

    public EventReportSMSRequestImpl(EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMSImpl eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPExtensionsImpl extensions) {
        super();
        
        if(eventTypeSMS!=null) {
        	this.eventTypeSMS = new ASNEventTypeSMSImpl();
        	this.eventTypeSMS.setType(eventTypeSMS);
        }
        
        if(eventSpecificInformationSMS!=null)
        	this.eventSpecificInformationSMS = new EventSpecificInformationSMSWrapperImpl(eventSpecificInformationSMS);
        
        this.miscCallInfo = miscCallInfo;
        this.extensions = extensions;
    }

    public EventReportSMSRequestImpl() {
        super();
    }

    public EventTypeSMS getEventTypeSMS() {
    	if(eventTypeSMS==null)
    		return null;
    	
        return this.eventTypeSMS.getType();
    }

    public EventSpecificInformationSMSImpl getEventSpecificInformationSMS() {
    	if(this.eventSpecificInformationSMS==null)
    		return null;
    	
        return this.eventSpecificInformationSMS.getEventSpecificInformationSMS();
    }

    public MiscCallInfo getMiscCallInfo() {
        return this.miscCallInfo;
    }

    public CAPExtensionsImpl getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.eventReportSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.eventReportSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventReportSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.eventTypeSMS != null && this.eventTypeSMS.getType()!=null) {
            sb.append(", eventTypeSMS=");
            sb.append(eventTypeSMS.getType());
        }
        if (this.eventSpecificInformationSMS != null && this.eventSpecificInformationSMS.getEventSpecificInformationSMS()!=null) {
            sb.append(", eventSpecificInformationSMS=");
            sb.append(eventSpecificInformationSMS.getEventSpecificInformationSMS());
        }
        if (this.miscCallInfo != null) {
            sb.append(", miscCallInfo=");
            sb.append(miscCallInfo.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
