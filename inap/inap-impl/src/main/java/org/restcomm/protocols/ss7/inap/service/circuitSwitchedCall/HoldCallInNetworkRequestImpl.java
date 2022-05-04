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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HoldCallInNetworkRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.HoldCause;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.HoldCallInNetworkRequestChoisempl;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class HoldCallInNetworkRequestImpl extends CircuitSwitchedCallMessageImpl implements HoldCallInNetworkRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private HoldCallInNetworkRequestChoisempl holdCallInNetworkRequestChose;

    public HoldCallInNetworkRequestImpl() {
    }

    public HoldCallInNetworkRequestImpl(HoldCause holdCause) {
        this.holdCallInNetworkRequestChose = new HoldCallInNetworkRequestChoisempl(holdCause);
    }

    public HoldCallInNetworkRequestImpl(boolean none) {
        this.holdCallInNetworkRequestChose = new HoldCallInNetworkRequestChoisempl(none);
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.cancel_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.cancelCode;
    }

    @Override
    public HoldCause getHoldCause() {
    	if(holdCallInNetworkRequestChose==null)
        	return null;
    	
    	return holdCallInNetworkRequestChose.getHoldCause();
    }

    @Override
    public boolean getEmpty() {
    	if(holdCallInNetworkRequestChose==null)
    		return false;
    	
        return holdCallInNetworkRequestChose.getEmpty();
    }

    @Override
    public String toString() {
        if (this.holdCallInNetworkRequestChose != null)
            return this.holdCallInNetworkRequestChose.toString();
        
        return "";
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(holdCallInNetworkRequestChose==null)
			throw new ASNParsingComponentException("one of childs should be set for hold call in network request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
