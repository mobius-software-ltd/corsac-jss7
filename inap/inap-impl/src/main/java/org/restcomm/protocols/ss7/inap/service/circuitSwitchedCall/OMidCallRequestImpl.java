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
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.OMidCallRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CallingPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FeatureRequestIndicator;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
public class OMidCallRequestImpl extends MidCallRequestImpl implements
        OMidCallRequest {
	private static final long serialVersionUID = 1L;

	@Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.oMidCall_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.oMidCall;
    }

    public OMidCallRequestImpl() {
    	
    }
    
    public OMidCallRequestImpl(DpSpecificCommonParameters dpSpecificCommonParameters,CalledPartyBusinessGroupID calledPartyBusinessGroupID,
    		CalledPartySubaddress calledPartySubaddress,CallingPartyBusinessGroupID callingPartyBusinessGroupID,
    		CallingPartySubaddress callingPartySubaddress,FeatureRequestIndicator featureRequestIndicator,
    		CAPINAPExtensions extensions,Carrier carrier) {
    	super(dpSpecificCommonParameters,calledPartyBusinessGroupID,calledPartySubaddress,callingPartyBusinessGroupID,
    		callingPartySubaddress,featureRequestIndicator,extensions,carrier);
    }
    
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("OMidCallRequestIndication [");
        this.addInvokeIdInfo(sb);

        toStringInternal(sb);
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(getDpSpecificCommonParameters()==null)
			throw new ASNParsingComponentException("dp specific common parameters should be set for omid call request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
