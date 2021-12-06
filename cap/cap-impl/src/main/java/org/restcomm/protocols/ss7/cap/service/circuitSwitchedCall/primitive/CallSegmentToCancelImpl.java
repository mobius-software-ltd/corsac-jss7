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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.CallSegmentToCancel;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class CallSegmentToCancelImpl implements CallSegmentToCancel {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger invokeID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger callSegmentID;

    public CallSegmentToCancelImpl() {
    }

    public CallSegmentToCancelImpl(Integer invokeID, Integer callSegmentID) {
    	if(invokeID!=null) {
    		this.invokeID = new ASNInteger();
    		this.invokeID.setValue(invokeID.longValue());
    	}
    	
    	if(callSegmentID!=null) {
    		this.callSegmentID = new ASNInteger();
    		this.callSegmentID.setValue(callSegmentID.longValue());
    	}
    }

    public Integer getInvokeID() {
    	if(invokeID==null || invokeID.getValue()==null)
    		return null;
    	
        return invokeID.getValue().intValue();
    }

    public Integer getCallSegmentID() {
    	if(callSegmentID==null || callSegmentID.getValue()==null)
    		return null;
    	
        return callSegmentID.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CallSegmentToCancel [");

        if (this.invokeID != null && this.invokeID.getValue()!=null) {
            sb.append("invokeID=");
            sb.append(this.invokeID.getValue());
        }
        if (this.callSegmentID != null && this.callSegmentID.getValue()!=null) {
            sb.append(", callSegmentID=");
            sb.append(this.callSegmentID.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
