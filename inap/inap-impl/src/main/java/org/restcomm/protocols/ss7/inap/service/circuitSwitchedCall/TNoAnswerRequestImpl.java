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

import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.isup.LocationNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.OriginalCalledNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectingPartyIDIsupImpl;
import org.restcomm.protocols.ss7.commonapp.isup.RedirectionInformationIsupImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.TNoAnswerRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CalledPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DpSpecificCommonParametersImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.FacilityGroupWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TNoAnswerRequestImpl extends CircuitSwitchedCallMessageImpl implements TNoAnswerRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = DpSpecificCommonParametersImpl.class)
	private DpSpecificCommonParameters dpSpecificCommonParameters;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CalledPartyBusinessGroupIDImpl.class)
	private CalledPartyBusinessGroupID calledPartyBusinessGroupID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CalledPartySubaddress.class)
	private CalledPartySubaddress calledPartySubaddress;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
	private FacilityGroupWrapperImpl calledFacilityGroup;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1)
	private ASNInteger calledFacilityGroupMember;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1, defaultImplementation = OriginalCalledNumberIsupImpl.class)
	private OriginalCalledNumberIsup originalCalledPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = false,index = -1, defaultImplementation = RedirectingPartyIDIsupImpl.class)
	private RedirectingPartyIDIsup redirectingPartyID;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1, defaultImplementation = RedirectionInformationIsupImpl.class)
	private RedirectionInformationIsup redirectionInformation;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 8,constructed = false,index = -1, defaultImplementation = LocationNumberIsupImpl.class)
	private LocationNumberIsup travellingClassMark;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 9,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;	

    public TNoAnswerRequestImpl() {
    }

    public TNoAnswerRequestImpl(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,FacilityGroup calledFacilityGroup,Integer calledFacilityGroupMember,
    		OriginalCalledNumberIsup originalCalledPartyID,RedirectingPartyIDIsup redirectingPartyID,
    		RedirectionInformationIsup redirectionInformation,LocationNumberIsup travellingClassMark,CAPINAPExtensions extensions) {
        this.dpSpecificCommonParameters = dpSpecificCommonParameters;
        this.calledPartyBusinessGroupID = calledPartyBusinessGroupID;
        this.calledPartySubaddress=calledPartySubaddress;
        
        if(calledFacilityGroup!=null)
        	this.calledFacilityGroup=new FacilityGroupWrapperImpl(calledFacilityGroup);
        
        if(calledFacilityGroupMember!=null)
        	this.calledFacilityGroupMember=new ASNInteger(calledFacilityGroupMember,"CalledFacilityGroupMember",0,Integer.MAX_VALUE,false);
        	
        this.originalCalledPartyID=originalCalledPartyID;
        this.redirectingPartyID=redirectingPartyID;
        this.redirectionInformation=redirectionInformation;
        this.travellingClassMark=travellingClassMark;
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.tNoAnswer_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.tNoAnswer;
    }

    @Override
    public DpSpecificCommonParameters getDpSpecificCommonParameters() {
		return dpSpecificCommonParameters;
	}

    @Override
    public CalledPartyBusinessGroupID getCalledPartyBusinessGroupID() {
		return calledPartyBusinessGroupID;
	}

    @Override
    public CalledPartySubaddress getCalledPartySubaddress() {
		return calledPartySubaddress;
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
    public RedirectingPartyIDIsup getRedirectingPartyID() {
		return redirectingPartyID;
	}

    @Override
    public RedirectionInformationIsup getRedirectionInformation() {
		return redirectionInformation;
	}

    @Override
    public LocationNumberIsup getTravellingClassMark() {
		return travellingClassMark;
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

	@Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TNoAnswerRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.dpSpecificCommonParameters != null) {
            sb.append(", dpSpecificCommonParameters=");
            sb.append(dpSpecificCommonParameters.toString());
        }
        if (this.calledPartyBusinessGroupID != null) {
            sb.append(", calledPartyBusinessGroupID=");
            sb.append(calledPartyBusinessGroupID.toString());
        }
        if (this.calledPartySubaddress != null) {
            sb.append(", calledPartySubaddress=");
            sb.append(calledPartySubaddress.toString());
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
        if (this.redirectingPartyID != null) {
            sb.append(", redirectingPartyID=");
            sb.append(redirectingPartyID.toString());
        }
        if (this.redirectionInformation != null) {
            sb.append(", redirectionInformation=");
            sb.append(redirectionInformation.toString());
        }
        if (this.travellingClassMark != null) {
            sb.append(", travellingClassMark=");
            sb.append(travellingClassMark.toString());
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
		if(dpSpecificCommonParameters==null)
			throw new ASNParsingComponentException("dp specific common parameters should be set for tnoanswer request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
