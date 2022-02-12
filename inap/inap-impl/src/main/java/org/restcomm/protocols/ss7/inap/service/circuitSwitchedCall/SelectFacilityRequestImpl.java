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

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.AlertingPatternWrapperImpl;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SelectFacilityRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FacilityGroupWrapperImpl;

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
public class SelectFacilityRequestImpl extends CircuitSwitchedCallMessageImpl implements SelectFacilityRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
	private AlertingPatternWrapperImpl alertingPattern;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CalledPartyNumberIsupImpl.class)
	private CalledPartyNumberIsup destinationNumberRoutingAddress;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CallingPartyBusinessGroupIDImpl.class)
	private ISDNAccessRelatedInformation isdnAccessRelatedInformation;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
	private FacilityGroupWrapperImpl calledFacilityGroup;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
	private ASNInteger calledFacilityGroupMember;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
	private OriginalCalledNumberIsup originalCalledPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
    public SelectFacilityRequestImpl() {
    }

    public SelectFacilityRequestImpl(AlertingPattern alertingPattern,CalledPartyNumberIsup destinationNumberRoutingAddress,
    		ISDNAccessRelatedInformation isdnAccessRelatedInformation,FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions) {
    	if(alertingPattern!=null)
    		this.alertingPattern = new AlertingPatternWrapperImpl(alertingPattern);
    	
        this.destinationNumberRoutingAddress = destinationNumberRoutingAddress;
        this.isdnAccessRelatedInformation = isdnAccessRelatedInformation;
        
        if(calledFacilityGroup!=null)
        	this.calledFacilityGroup=new FacilityGroupWrapperImpl(calledFacilityGroup);
        
        if(calledFacilityGroupMember!=null)
        	this.calledFacilityGroupMember=new ASNInteger(calledFacilityGroupMember,"CalledFacilityGroupMember",0,Integer.MAX_VALUE,false);
        	
        this.originalCalledPartyID=originalCalledPartyID;
        this.extensions = extensions;        
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.selectFacility_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.selectFacility;
    }

    @Override
    public AlertingPattern getAlertingPattern() {
    	if(alertingPattern==null)
    		return null;
    	
		return alertingPattern.getAlertingPattern();
	}

    @Override
    public CalledPartyNumberIsup getDestinationNumberRoutingAddress() {
		return destinationNumberRoutingAddress;
	}

    @Override
    public ISDNAccessRelatedInformation getISDNAccessRelatedInformation() {
		return isdnAccessRelatedInformation;
	}

    @Override
    public FacilityGroup getCalledFacilityGroup() {
    	if(calledFacilityGroup==null)
    		return null;
    	
		return calledFacilityGroup.getFacilityGroup();
	}

    @Override
    public Integer getCalledFacilityGroupMember() {
    	if(calledFacilityGroupMember==null)
    		return null;
    	
		return calledFacilityGroupMember.getIntValue();
	}

    @Override
    public OriginalCalledNumberIsup getOriginalCalledPartyID() {
		return originalCalledPartyID;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SelectFacilityRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.alertingPattern != null && this.alertingPattern.getAlertingPattern()!=null) {
            sb.append(", alertingPattern=");
            sb.append(alertingPattern.toString());
        }
        if (this.destinationNumberRoutingAddress != null) {
            sb.append(", destinationNumberRoutingAddress=");
            sb.append(destinationNumberRoutingAddress.toString());
        }
        if (this.isdnAccessRelatedInformation != null) {
            sb.append(", isdnAccessRelatedInformation=");
            sb.append(isdnAccessRelatedInformation.toString());
        }
        if (this.calledFacilityGroup != null && this.calledFacilityGroup.getFacilityGroup()!=null) {
            sb.append(", calledFacilityGroup=");
            sb.append(calledFacilityGroup.getFacilityGroup().toString());
        }
        if (this.calledFacilityGroupMember != null && this.calledFacilityGroupMember.getValue()!=null) {
            sb.append(", calledFacilityGroupMember=");
            sb.append(calledFacilityGroupMember.getValue().toString());
        }
        if (this.originalCalledPartyID != null) {
            sb.append(", originalCalledPartyID=");
            sb.append(originalCalledPartyID.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
