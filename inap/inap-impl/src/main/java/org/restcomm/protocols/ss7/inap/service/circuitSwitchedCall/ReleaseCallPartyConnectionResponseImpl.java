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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.ReleaseCallPartyConnectionResponse;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.LegInformation;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.LegInformationImpl;

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
public class ReleaseCallPartyConnectionResponseImpl extends CircuitSwitchedCallMessageImpl implements
ReleaseCallPartyConnectionResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1,defaultImplementation = LegInformationImpl.class)
    private List<LegInformation> legInformation;


    public ReleaseCallPartyConnectionResponseImpl() {
    }

    public ReleaseCallPartyConnectionResponseImpl(List<LegInformation> legInformation) {
    	this.legInformation=legInformation;    	
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.releaseCallPartyConnection_Response;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.releaseCallPartyConnection;
    }

    @Override
    public List<LegInformation> getLegInformation() {
    	return legInformation;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
        sb.append("ReleaseCallPartyConnectionResponseIndication [");
        this.addInvokeIdInfo(sb);

        if (this.legInformation != null) {
            sb.append(", legInformation=[");
            
            Boolean isFirst=true;
            for(LegInformation curr:legInformation) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr.toString());
                isFirst=false;
            }
            
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(legInformation==null)
			throw new ASNParsingComponentException("leg information should be set for release call party connection response", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}