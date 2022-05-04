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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.RequestCurrentStatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDImpl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class RequestCurrentStatusReportRequestImpl extends CircuitSwitchedCallMessageImpl implements RequestCurrentStatusReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise(defaultImplementation = ResourceIDImpl.class)
	private ResourceID resourceID;
    
    public RequestCurrentStatusReportRequestImpl() {
    }

    public RequestCurrentStatusReportRequestImpl(ResourceID resourceID) {
    	this.resourceID=resourceID;    	
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(resourceID==null)
			throw new ASNParsingComponentException("resource ID should be set for request current status report request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}