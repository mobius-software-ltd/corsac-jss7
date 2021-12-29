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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class RequestCurrentStatusReportRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestCurrentStatusReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
	private ResourceIDImpl resourceID;
    
    public RequestCurrentStatusReportRequestImpl() {
    }

    public RequestCurrentStatusReportRequestImpl(ResourceID resourceID) {
    	if(resourceID instanceof ResourceIDImpl)
    		this.resourceID=(ResourceIDImpl)resourceID;
    	else if(resourceID!=null) {
    		if(resourceID.getFacilityGroup()!=null)
    			this.resourceID = new ResourceIDImpl(resourceID.getFacilityGroup());
    		else if(resourceID.getLineID()!=null)
    			this.resourceID = new ResourceIDImpl(resourceID.getLineID());
    		else if(resourceID.getTrunkGroupID()!=null)
    			this.resourceID = new ResourceIDImpl(resourceID.getTrunkGroupID(),true);
    		else if(resourceID.getFacilityGroupMemberID()!=null)
    			this.resourceID = new ResourceIDImpl(resourceID.getFacilityGroupMemberID(),false);
    	}
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.requestCurrentStatusReport_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.requestCurrentStatusReport;
    }

    @Override
    public ResourceID getResourceID() {
    	return resourceID;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("RequestCurrentStatusReportRequest [");
        this.addInvokeIdInfo(sb);

        if (this.resourceID != null) {
            sb.append(", resourceID=[");
            sb.append(resourceID.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}