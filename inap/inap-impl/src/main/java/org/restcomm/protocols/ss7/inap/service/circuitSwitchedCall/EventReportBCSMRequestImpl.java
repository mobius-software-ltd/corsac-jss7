/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.EventSpecificInformationBCSM;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.EventSpecificInformationBCSMWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ASNEventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.EventReportBCSMRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

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
    private ReceivingLegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = MiscCallInfoImpl.class)
    private MiscCallInfo miscCallInfo;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public EventReportBCSMRequestImpl() {
    }

    public EventReportBCSMRequestImpl(EventTypeBCSM eventTypeBCSM, DigitsIsup bcsmEventCorrelationID, EventSpecificInformationBCSM eventSpecificInformationBCSM,
            LegType legID, MiscCallInfo miscCallInfo, CAPINAPExtensions extensions) {
    	    	
    	if(eventTypeBCSM!=null) {
    		this.eventTypeBCSM = new ASNEventTypeBCSM();
    		this.eventTypeBCSM.setType(eventTypeBCSM);
    	}
    	
        this.bcsmEventCorrelationID=bcsmEventCorrelationID;
    	if(eventSpecificInformationBCSM!=null)
        	this.eventSpecificInformationBCSM = new EventSpecificInformationBCSMWrapperImpl(eventSpecificInformationBCSM);
        
        if(legID!=null)
        	this.legID = new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));
        
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
    public LegType getLegID() {
    	if(legID==null || legID.getReceivingLegID()==null)
    		return null;
    	
        return legID.getReceivingLegID().getReceivingSideID();
    }

    @Override
    public MiscCallInfo getMiscCallInfo() {
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
        if (this.legID != null && this.legID.getReceivingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getReceivingLegID());
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
