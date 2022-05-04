/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.CancelRequest;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.CancelRequestChoisempl;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * @author yulianoifa
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

    public CancelRequestImpl(CallSegmentToCancel callSegmentToCancel) {
        this.cancelRequestChoise = new CancelRequestChoisempl(callSegmentToCancel);
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.cancel_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.cancelCode;
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
    public CallSegmentToCancel getCallSegmentToCancel() {
        if(cancelRequestChoise==null)
        	return null;
        
        return cancelRequestChoise.getCallSegmentToCancel();
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
			throw new ASNParsingComponentException("one of child items should be set for cancel request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
