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

package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.EventReportGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.ASNGPRSEventTypeImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.GPRSEventSpecificInformationWrapperImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
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
public class EventReportGPRSRequestImpl extends GprsMessageImpl implements EventReportGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNGPRSEventTypeImpl gprsEventType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscGPRSInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private GPRSEventSpecificInformationWrapperImpl gprsEventSpecificInformation;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1, defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;

    public EventReportGPRSRequestImpl() {
    }

    public EventReportGPRSRequestImpl(GPRSEventType gprsEventType, MiscCallInfo miscGPRSInfo,
            GPRSEventSpecificInformation gprsEventSpecificInformation, PDPID pdpID) {
        super();
        
        if(gprsEventType!=null)
        	this.gprsEventType = new ASNGPRSEventTypeImpl(gprsEventType);
        	
        this.miscGPRSInfo = miscGPRSInfo;
        
        if(gprsEventSpecificInformation!=null)
        	this.gprsEventSpecificInformation = new GPRSEventSpecificInformationWrapperImpl(gprsEventSpecificInformation);
        
        this.pdpID = pdpID;
    }

    @Override
    public GPRSEventType getGPRSEventType() {
    	if(this.gprsEventType==null)
    		return null;
    	
        return this.gprsEventType.getType();
    }

    @Override
    public MiscCallInfo getMiscGPRSInfo() {
    	if(this.miscGPRSInfo==null)
    		return new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);
    	
        return this.miscGPRSInfo;
    }

    @Override
    public GPRSEventSpecificInformation getGPRSEventSpecificInformation() {
    	if(this.gprsEventSpecificInformation==null)
    		return null;
    	
        return this.gprsEventSpecificInformation.getGPRSEventSpecificInformation();
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.eventReportGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.eventReportGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventReportGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.gprsEventType != null && this.gprsEventType.getType()!=null) {
            sb.append(", gprsEventType=");
            sb.append(this.gprsEventType.getType());
        }

        if (this.miscGPRSInfo != null) {
            sb.append(", miscGPRSInfo=");
            sb.append(this.miscGPRSInfo.toString());
        }

        if (this.gprsEventSpecificInformation != null && this.gprsEventSpecificInformation.getGPRSEventSpecificInformation()!=null) {
            sb.append(", gprsEventSpecificInformation=");
            sb.append(this.gprsEventSpecificInformation.getGPRSEventSpecificInformation());
        }

        if (this.pdpID != null) {
            sb.append("pdpID=");
            sb.append(this.pdpID.toString());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(gprsEventType==null)
			throw new ASNParsingComponentException("gprs event type should be set for event report gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
