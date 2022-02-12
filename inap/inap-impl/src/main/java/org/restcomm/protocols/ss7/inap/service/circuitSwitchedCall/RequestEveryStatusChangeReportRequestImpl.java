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

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestEveryStatusChangeReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestEveryStatusChangeReportRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestEveryStatusChangeReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private ResourceIDWrapperImpl resourceID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private ASNInteger duration;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public RequestEveryStatusChangeReportRequestImpl() {
    }

    public RequestEveryStatusChangeReportRequestImpl(ResourceID resourceID, DigitsIsup correlationID, Integer duration, CAPINAPExtensions extensions) {
    	if(resourceID!=null)
    		this.resourceID=new ResourceIDWrapperImpl(resourceID);
    	
        this.correlationID=correlationID;
        if(duration!=null)
        	this.duration=new ASNInteger(duration,"Duration",-2,86400,false);
        	
        this.extensions=extensions;
    }

    public INAPMessageType getMessageType() {
        return INAPMessageType.requestEveryStatusChangeReport_Request;
    }

    public int getOperationCode() {
        return INAPOperationCode.requestEveryStatusChangeReport;
    }
    
    @Override
    public ResourceID getResourceID() {
    	if(resourceID==null)
    		return null;
    	
		return resourceID.getResourceID();
	}

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
    }

    @Override
    public Integer getDuration() {
    	if(duration==null)
    		return null;
    	
		return duration.getIntValue();
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestEveryStatusChangeReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (resourceID != null && resourceID.getResourceID()!=null) {
            sb.append(", resourceID=");
            sb.append(resourceID.getResourceID());
        }
        if (correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID);
        }
        if(duration != null && duration.getValue()!=null) {
            sb.append(", duration=");
            sb.append(duration.getValue());
        }
        if (extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }

        sb.append("]");

        return sb.toString();
    }
}
