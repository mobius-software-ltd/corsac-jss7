/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.RequestedInformationTypeWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationRequest;

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
public class CallInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements CallInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = true,index = -1)
    private ReceivingLegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private RequestedInformationTypeWrapperImpl requestedInformationList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    public CallInformationRequestImpl() {
    }

    public CallInformationRequestImpl(LegType legID, List<RequestedInformationType> requestedInformationList, CAPINAPExtensions extensions) {
    	this(requestedInformationList,null,extensions);
    	if(legID!=null)
        	this.legID = new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));
    }
    
    public CallInformationRequestImpl(List<RequestedInformationType> requestedInformationList, DigitsIsup correlationID, CAPINAPExtensions extensions) {
    	
    	if(requestedInformationList!=null)
    		this.requestedInformationList = new RequestedInformationTypeWrapperImpl(requestedInformationList);
    	
    	this.correlationID=correlationID;
        this.extensions = extensions;                
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.callInformationReport_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.callInformationReport;
    }

    @Override
    public List<RequestedInformationType> getRequestedInformationTypeList() {
    	if(requestedInformationList==null)
    		return null;
    	
        return requestedInformationList.getRequestedInformationTypes();
    }

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public LegType getLegID() {
    	if(legID==null || legID.getReceivingLegID()==null)
    		return null;
    	
        return legID.getReceivingLegID().getReceivingSideID();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legID != null && this.legID.getReceivingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getReceivingLegID());
        }
        
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID);
        }
        
        if (this.requestedInformationList != null && this.requestedInformationList.getRequestedInformationTypes()!=null) {
            sb.append(", requestedInformationTypeList=[");
            boolean firstItem = true;
            for (RequestedInformationType ri : this.requestedInformationList.getRequestedInformationTypes()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append("requestedInformationType=[");
                sb.append(ri);
                sb.append("]");
            }
            sb.append("]");
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
		if(requestedInformationList==null)
			throw new ASNParsingComponentException("requested information type list should be set for call information request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
