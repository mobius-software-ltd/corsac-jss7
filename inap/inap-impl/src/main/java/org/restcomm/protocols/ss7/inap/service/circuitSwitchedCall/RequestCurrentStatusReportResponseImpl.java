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
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNResourceStatus;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDWrapperImpl;

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
public class RequestCurrentStatusReportResponseImpl extends CircuitSwitchedCallMessageImpl implements RequestCurrentStatusReportResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private ASNResourceStatus resourceStatus;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private ResourceIDWrapperImpl resourceID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public RequestCurrentStatusReportResponseImpl() {
    }

    public RequestCurrentStatusReportResponseImpl(ResourceStatus resourceStatus, ResourceID resourceID, CAPINAPExtensions extensions) {
        if(resourceStatus!=null)
        	this.resourceStatus=new ASNResourceStatus(resourceStatus);
        	
        if(resourceID!=null)
        	this.resourceID=new ResourceIDWrapperImpl(resourceID);
        
        this.extensions=extensions;
    }

    public INAPMessageType getMessageType() {
        return INAPMessageType.requestCurrentStatusReport_Response;
    }

    public int getOperationCode() {
        return INAPOperationCode.requestCurrentStatusReport;
    }
    
    @Override
    public ResourceStatus getResourceStatus() {
    	if(resourceStatus==null)
    		return null;
    	
		return resourceStatus.getType();
	}

    @Override
    public ResourceID getResourceID() {
    	if(resourceID==null)
    		return null;
    	
		return resourceID.getResourceID();
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestCurrentStatusReportResponseIndication [");
        this.addInvokeIdInfo(sb);

        if(resourceStatus != null && resourceStatus.getType()!=null) {
            sb.append(", resourceStatus=");
            sb.append(resourceStatus.getType());
        }
        if (resourceID != null) {
            sb.append(", resourceID=");
            sb.append(resourceID);
        }
        if (extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(resourceStatus==null)
			throw new ASNParsingComponentException("resource status should be set for request current status report response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
