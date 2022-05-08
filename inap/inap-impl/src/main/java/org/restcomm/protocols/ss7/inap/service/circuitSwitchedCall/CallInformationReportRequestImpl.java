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

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.RequestedInformationWrapperImpl;

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
public class CallInformationReportRequestImpl extends CircuitSwitchedCallMessageImpl implements CallInformationReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.PRIVATE,tag = 1,constructed = true,index = -1)
    private ReceivingLegIDWrapperImpl legID;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private RequestedInformationWrapperImpl requestedInformationList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    public CallInformationReportRequestImpl() {
    }

    public CallInformationReportRequestImpl(LegType legID, List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions) {
    	this(requestedInformationList,null,extensions);
    	if(legID!=null)
        	this.legID = new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));
    }
    
    public CallInformationReportRequestImpl(List<RequestedInformation> requestedInformationList, DigitsIsup correlationID, CAPINAPExtensions extensions) {
    	
    	if(requestedInformationList!=null)
    		this.requestedInformationList = new RequestedInformationWrapperImpl(requestedInformationList);
    	
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
    public List<RequestedInformation> getRequestedInformationList() {
    	if(requestedInformationList==null)
    		return null;
    	
        return requestedInformationList.getRequestedInformation();
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
        sb.append("CallInformationReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legID != null && this.legID.getReceivingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getReceivingLegID());
        }
        
        if (this.correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID);
        }
        
        if (this.requestedInformationList != null && this.requestedInformationList.getRequestedInformation()!=null) {
            sb.append(", requestedInformationList=[");
            boolean firstItem = true;
            for (RequestedInformation ri : this.requestedInformationList.getRequestedInformation()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append("requestedInformation=[");
                sb.append(ri.toString());
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
			throw new ASNParsingComponentException("requested information list should be set for call information report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}