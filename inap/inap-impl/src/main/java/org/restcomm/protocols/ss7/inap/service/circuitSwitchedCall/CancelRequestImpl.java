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
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives.CancelRequestChoisempl;

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
public class CancelRequestImpl extends CircuitSwitchedCallMessageImpl implements CancelRequest {
	private static final long serialVersionUID = 1L;

	@ASNChoise
    private CancelRequestChoisempl cancelRequestChoise;

    public CancelRequestImpl() {
    }

    public CancelRequestImpl(Integer invokeID) {
        this.cancelRequestChoise = new CancelRequestChoisempl(invokeID);
    }

    public CancelRequestImpl(boolean allRequests) {
        this.cancelRequestChoise = new CancelRequestChoisempl(allRequests);
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
    public Integer getInvokeID() {
    	if(cancelRequestChoise==null)
        	return null;
    	
    	return cancelRequestChoise.getInvokeID();
    }

    @Override
    public boolean getAllRequests() {
    	if(cancelRequestChoise==null)
    		return false;
    	
        return cancelRequestChoise.getAllRequests();
    }

    @Override
    public String toString() {
        if (this.cancelRequestChoise != null)
            return this.cancelRequestChoise.toString();
        
        return "";
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(cancelRequestChoise==null)
			throw new ASNParsingComponentException("child item should be set for cancel request request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
