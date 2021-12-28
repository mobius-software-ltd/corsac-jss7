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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequest;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.RequestedInformationWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ReceivingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallInformationReportRequestImpl extends CircuitSwitchedCallMessageImpl implements CallInformationReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private RequestedInformationWrapperImpl requestedInformationList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private ReceivingLegIDWrapperImpl legID;

    public CallInformationReportRequestImpl() {
    }

    public CallInformationReportRequestImpl(List<RequestedInformation> requestedInformationList, CAPINAPExtensions extensions,
            LegType legID) {
    	
    	if(requestedInformationList!=null)
    		this.requestedInformationList = new RequestedInformationWrapperImpl(requestedInformationList);
    	
        this.extensions = extensions;
        
        if(legID!=null)
        	this.legID = new ReceivingLegIDWrapperImpl(new ReceivingLegIDImpl(legID));
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.callInformationReport_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.callInformationReport;
    }

    @Override
    public List<RequestedInformation> getRequestedInformationList() {
    	if(requestedInformationList==null)
    		return null;
    	
        return requestedInformationList.getRequestedInformation();
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
        
        if (this.legID != null && this.legID.getReceivingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getReceivingLegID());
        }

        sb.append("]");

        return sb.toString();
    }
}
