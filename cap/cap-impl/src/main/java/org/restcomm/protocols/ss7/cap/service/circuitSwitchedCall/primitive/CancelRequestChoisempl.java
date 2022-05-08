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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.CallSegmentToCancelImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,lengthIndefinite = false)
public class CancelRequestChoisempl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
	private ASNInteger invokeID;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
	private ASNNull allRequests;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CallSegmentToCancelImpl.class)
	private CallSegmentToCancel callSegmentToCancel;

    public CancelRequestChoisempl() {
    }

    public CancelRequestChoisempl(Integer invokeID) {
    	if(invokeID!=null)
    		this.invokeID = new ASNInteger(invokeID,"InvokeID",-128,128,false);    		
    }

    public CancelRequestChoisempl(boolean allRequests) {
    	if(allRequests)
    		this.allRequests = new ASNNull();     	
    }
    
    public CancelRequestChoisempl(CallSegmentToCancel callSegmentToCancel) {
    	this.callSegmentToCancel = callSegmentToCancel;     	
    }

    public Integer getInvokeID() {
    	if(invokeID==null)
    		return null;
    	
        return invokeID.getIntValue();
    }

    public boolean getAllRequests() {
    	return allRequests!=null;
    }

    public CallSegmentToCancel getCallSegmentToCancel() {
    	return callSegmentToCancel;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CancelRequest [");
        if (this.invokeID != null && this.invokeID.getValue()!=null) {
            sb.append("invokeID=");
            sb.append(invokeID.getValue());
        }
        
        if (this.allRequests!=null) {
            sb.append(", allRequests");            
        }
        
        if (this.callSegmentToCancel != null) {
            sb.append(", callSegmentToCancel=");
            sb.append(callSegmentToCancel);
        }
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(invokeID==null && allRequests==null)
			throw new ASNParsingComponentException("one of child items should be set for cancel request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}