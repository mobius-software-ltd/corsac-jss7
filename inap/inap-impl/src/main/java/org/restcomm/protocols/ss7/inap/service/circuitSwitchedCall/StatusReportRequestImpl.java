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
import org.restcomm.protocols.ss7.commonapp.isup.DigitsIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.StatusReportRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ReportCondition;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResourceStatus;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNReportCondition;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNResourceStatus;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ResourceIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class StatusReportRequestImpl extends CircuitSwitchedCallMessageImpl implements StatusReportRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNResourceStatus resourceStatus;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = DigitsIsupImpl.class)
    private DigitsIsup correlationID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private ResourceIDWrapperImpl resourceID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private ASNReportCondition reportCondition;
   
	public StatusReportRequestImpl() {
    }

    public StatusReportRequestImpl(ResourceStatus resourceStatus,DigitsIsup correlationID, ResourceID resourceID, 
    		CAPINAPExtensions extensions,ReportCondition reportCondition) {
    	if(resourceStatus!=null)
    		this.resourceStatus=new ASNResourceStatus(resourceStatus);
    		
        this.correlationID=correlationID;
        
        if(resourceID!=null)
    		this.resourceID=new ResourceIDWrapperImpl(resourceID);
    	
    	this.extensions=extensions;
    	if(reportCondition!=null)
        	this.reportCondition=new ASNReportCondition(reportCondition);        	            
    }

    public INAPMessageType getMessageType() {
        return INAPMessageType.statusReport_Request;
    }

    public int getOperationCode() {
        return INAPOperationCode.statusReport;
    }
    
    @Override
    public ResourceStatus getResourceStatus() {
    	if(resourceStatus==null)
    		return null;
    	
		return resourceStatus.getType();
	}

    @Override
    public DigitsIsup getCorrelationID() {
    	if(correlationID!=null)
    		correlationID.setIsGenericNumber();
    	
        return correlationID;
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
    
    @Override
    public ReportCondition getReportCondition() {
    	if(reportCondition==null)
    		return null;
    	
		return reportCondition.getType();
	}
    
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("StatusReportRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (resourceStatus != null && resourceStatus.getType()!=null) {
            sb.append(", resourceStatus=");
            sb.append(resourceStatus.getType());
        }
        if (correlationID != null) {
            sb.append(", correlationID=");
            sb.append(correlationID);
        }
        if (resourceID != null && resourceID.getResourceID()!=null) {
            sb.append(", resourceID=");
            sb.append(resourceID.getResourceID());
        }
        if (extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }
        if (reportCondition != null && reportCondition.getType()!=null) {
            sb.append(", reportCondition=");
            sb.append(reportCondition.getType());
        }
        
        sb.append("]");

        return sb.toString();
    }
}
