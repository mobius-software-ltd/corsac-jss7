/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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
