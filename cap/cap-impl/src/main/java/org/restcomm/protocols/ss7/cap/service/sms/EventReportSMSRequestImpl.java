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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.EventReportSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventSpecificInformationSMS;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.EventTypeSMS;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.ASNEventTypeSMSImpl;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.EventSpecificInformationSMSWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;

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
public class EventReportSMSRequestImpl extends SmsMessageImpl implements EventReportSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNEventTypeSMSImpl eventTypeSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private EventSpecificInformationSMSWrapperImpl eventSpecificInformationSMS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public EventReportSMSRequestImpl(EventTypeSMS eventTypeSMS,
            EventSpecificInformationSMS eventSpecificInformationSMS, MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) {
        super();
        
        if(eventTypeSMS!=null)
        	this.eventTypeSMS = new ASNEventTypeSMSImpl(eventTypeSMS);
        	
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

    public EventSpecificInformationSMS getEventSpecificInformationSMS() {
    	if(this.eventSpecificInformationSMS==null)
    		return null;
    	
        return this.eventSpecificInformationSMS.getEventSpecificInformationSMS();
    }

    public MiscCallInfo getMiscCallInfo() {
    	if(this.miscCallInfo==null)
    		return new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);
    	
        return this.miscCallInfo;
    }

    public CAPINAPExtensions getExtensions() {
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(eventTypeSMS==null)
			throw new ASNParsingComponentException("event type sms should be set for event report sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
