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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CarrierImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.MidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FeatureRequestIndicator;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.ASNFeatureRequestIndicator;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CalledPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CallingPartyBusinessGroupIDImpl;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.DpSpecificCommonParametersImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public abstract class MidCallRequestImpl extends CircuitSwitchedCallMessageImpl implements MidCallRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1,defaultImplementation = DpSpecificCommonParametersImpl.class)
	private DpSpecificCommonParameters dpSpecificCommonParameters;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1,defaultImplementation = CalledPartyBusinessGroupIDImpl.class)
	private CalledPartyBusinessGroupID calledPartyBusinessGroupID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1,defaultImplementation = CalledPartySubaddress.class)
	private CalledPartySubaddress calledPartySubaddress;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1,defaultImplementation = CallingPartyBusinessGroupIDImpl.class)
	private CallingPartyBusinessGroupID callingPartyBusinessGroupID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = false,index = -1,defaultImplementation = CallingPartySubaddress.class)
	private CallingPartySubaddress callingPartySubaddress;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false,index = -1)
	private ASNFeatureRequestIndicator featureRequestIndicator;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 6,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
	private CAPINAPExtensions extensions;
	
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 7,constructed = false,index = -1, defaultImplementation = CarrierImpl.class)
	private Carrier carrier;

    public MidCallRequestImpl() {
    }

    public MidCallRequestImpl(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) {
        this.dpSpecificCommonParameters = dpSpecificCommonParameters;
        this.calledPartyBusinessGroupID = calledPartyBusinessGroupID;
        this.calledPartySubaddress=calledPartySubaddress;
        this.callingPartyBusinessGroupID = callingPartyBusinessGroupID;
        this.callingPartySubaddress=callingPartySubaddress;
        
        if(featureRequestIndicator!=null)
        	this.featureRequestIndicator=new ASNFeatureRequestIndicator(featureRequestIndicator);
        	
        this.extensions = extensions;
        this.carrier=carrier;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.analyzedInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.analysedInformation;
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
    public CallingPartyBusinessGroupID getCallingPartyBusinessGroupID() {
		return callingPartyBusinessGroupID;
	}

    @Override
    public CallingPartySubaddress getCallingPartySubaddress() {
		return callingPartySubaddress;
	}

    @Override
    public FeatureRequestIndicator getFeatureRequestIndicator() {
    	if(featureRequestIndicator==null) 
    		return null;
    	
		return featureRequestIndicator.getType();
	}

    @Override
    public CAPINAPExtensions getExtensions() {
		return extensions;
	}

    @Override
    public Carrier getCarrier() {
		return carrier;
	}

	public void toStringInternal(StringBuilder sb) {

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
        if (this.callingPartyBusinessGroupID != null) {
            sb.append(", callingPartyBusinessGroupID=");
            sb.append(callingPartyBusinessGroupID.toString());
        }
        if (this.callingPartySubaddress != null) {
            sb.append(", callingPartySubaddress=");
            sb.append(callingPartySubaddress.toString());
        }
        if (this.featureRequestIndicator != null && this.featureRequestIndicator.getType()!=null) {
            sb.append(", featureRequestIndicator=");
            sb.append(featureRequestIndicator.getType());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.carrier != null) {
            sb.append(", carrier=");
            sb.append(carrier.toString());
        }
    }
}
