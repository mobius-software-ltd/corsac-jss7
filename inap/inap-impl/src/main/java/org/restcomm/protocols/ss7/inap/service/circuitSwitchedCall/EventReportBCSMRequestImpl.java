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

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNEventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.LegIDWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.EventSpecificInformationBCSMWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
  * @author yulian.oifa
*
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventReportBCSMRequestImpl extends CircuitSwitchedCallMessageImpl implements EventReportBCSMRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNEventTypeBCSM eventTypeBCSM;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup bcsmEventCorrelationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private EventSpecificInformationBCSMWrapperImpl eventSpecificInformationBCSM;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private LegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public EventReportBCSMRequestImpl() {
    }

    public EventReportBCSMRequestImpl(EventTypeBCSM eventTypeBCSM, DigitsIsup bcsmEventCorrelationID, EventSpecificInformationBCSM eventSpecificInformationBCSM,
            LegID legID, MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) {
    	    	
    	if(eventTypeBCSM!=null)
    		this.eventTypeBCSM = new ASNEventTypeBCSM(eventTypeBCSM);
    		
        this.bcsmEventCorrelationID=bcsmEventCorrelationID;
    	if(eventSpecificInformationBCSM!=null)
        	this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMWrapperImpl(eventSpecificInformationBCSM);
        
        if(legID!=null)
        	this.legID = new LegIDWrapperImpl(legID);
        
        this.miscCallInfo = miscCallInfo;
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.eventReportBCSM_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.eventReportBCSM;
    }

    @Override
    public EventTypeBCSM getEventTypeBCSM() {
    	if(eventTypeBCSM==null)
    		return null;
    	
        return eventTypeBCSM.getType();
    }

    @Override
    public EventSpecificInformationBCSM getEventSpecificInformationBCSM() {
    	if(eventSpecificInformationBCSM==null)
    		return null;
    	
        return eventSpecificInformationBCSM.getEventSpecificInformationBCSM();
    }

    @Override
    public DigitsIsup getBCSMEventCorrelationID() {
    	if(bcsmEventCorrelationID!=null)
    		bcsmEventCorrelationID.setIsGenericNumber();
    	
		return bcsmEventCorrelationID;
	}

	@Override
    public LegID getLegID() {
    	if(legID==null || legID.getLegID()==null)
    		return null;
    	
        return legID.getLegID();
    }

    @Override
    public MiscCallInfo getMiscCallInfo() {
    	if(miscCallInfo==null)
    		return new MiscCallInfoImpl(MiscCallInfoMessageType.request, null);
    	
        return miscCallInfo;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventReportBCSMRequest [");
        this.addInvokeIdInfo(sb);

        if (this.eventTypeBCSM != null && this.eventTypeBCSM.getType()!=null) {
            sb.append(", eventTypeBCSM=");
            sb.append(eventTypeBCSM.getType());
        }
        if (this.bcsmEventCorrelationID != null) {
            sb.append(", bcsmEventCorrelationID=");
            sb.append(bcsmEventCorrelationID.toString());
        }
        if (this.eventSpecificInformationBCSM != null && this.eventSpecificInformationBCSM.getEventSpecificInformationBCSM()!=null) {
            sb.append(", eventSpecificInformationBCSM=");
            sb.append(eventSpecificInformationBCSM.getEventSpecificInformationBCSM());
        }
        if (this.legID != null && this.legID.getLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getLegID());
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
		if(eventTypeBCSM==null)
			throw new ASNParsingComponentException("event type BCSM should be set for event type BCSM request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}