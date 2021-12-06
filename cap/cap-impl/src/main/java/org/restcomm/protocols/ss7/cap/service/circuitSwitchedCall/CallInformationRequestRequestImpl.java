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

import java.util.ArrayList;
import java.util.List;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationRequestRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformationType;
import org.restcomm.protocols.ss7.cap.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.ASNRequestedInformationTypeImpl;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.RequestedInformationTypeWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.inap.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallInformationRequestRequestImpl extends CircuitSwitchedCallMessageImpl implements CallInformationRequestRequest {
	private static final long serialVersionUID = 1L;

	public static final String _PrimitiveName = "CallInformationRequestRequestIndication";

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private RequestedInformationTypeWrapperImpl requestedInformationTypeList;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPExtensionsImpl.class)
    private CAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private SendingLegIDWrapperImpl legID;

    public CallInformationRequestRequestImpl() {
    }

    public CallInformationRequestRequestImpl(List<RequestedInformationType> requestedInformationTypeList,
            CAPExtensions extensions, LegType legID) {
    	
    	if(requestedInformationTypeList!=null) {
    		List<ASNRequestedInformationTypeImpl> typesList=new ArrayList<ASNRequestedInformationTypeImpl>();
    		for(RequestedInformationType currType:requestedInformationTypeList) {
    			ASNRequestedInformationTypeImpl currValue=new ASNRequestedInformationTypeImpl();
    			currValue.setType(currType);
    			typesList.add(currValue);
    		}    		
    		this.requestedInformationTypeList = new RequestedInformationTypeWrapperImpl(typesList);
    	}
    	
        this.extensions = extensions;
        
        if(legID!=null)
        	this.legID = new SendingLegIDWrapperImpl(new SendingLegIDImpl(legID));
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.callInformationRequest_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.callInformationRequest;
    }

    @Override
    public List<RequestedInformationType> getRequestedInformationTypeList() {
    	if(requestedInformationTypeList==null || requestedInformationTypeList.getRequestedInformationTypes()==null)
    		return null;
    	
    	List<RequestedInformationType> result=new ArrayList<RequestedInformationType>();
    	for(ASNRequestedInformationTypeImpl curr:requestedInformationTypeList.getRequestedInformationTypes())
    		result.add(curr.getType());
    	
        return result;
    }

    @Override
    public CAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public LegType getLegID() {
    	if(legID==null || legID.getSendingLegID()==null)
    		return null;
    	
        return legID.getSendingLegID().getSendingSideID();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallInformationRequestRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.requestedInformationTypeList != null && this.requestedInformationTypeList.getRequestedInformationTypes()!=null) {
            sb.append(", requestedInformationTypeList=[");
            boolean firstItem = true;
            for (ASNRequestedInformationTypeImpl ri : this.requestedInformationTypeList.getRequestedInformationTypes()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(ri.toString());
            }
            sb.append("]");
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        
        if (this.legID != null && this.legID.getSendingLegID()!=null) {
            sb.append(", legID=");
            sb.append(legID.getSendingLegID());
        }

        sb.append("]");

        return sb.toString();
    }
}
