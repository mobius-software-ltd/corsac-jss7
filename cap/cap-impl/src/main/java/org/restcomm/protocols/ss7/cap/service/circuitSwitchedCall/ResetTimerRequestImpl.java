/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.primitives.ASNTimerIDImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.TimerID;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.ResetTimerRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ResetTimerRequestImpl extends CircuitSwitchedCallMessageImpl implements ResetTimerRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNTimerIDImpl timerID;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger timerValue;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false,index = -1)
    private ASNInteger callSegmentID;

    public ResetTimerRequestImpl() {
    }

    public ResetTimerRequestImpl(TimerID timerID, int timerValue, CAPExtensionsImpl extensions, Integer callSegmentID) {
    	if(timerID!=null) {
    		this.timerID = new ASNTimerIDImpl();
    		this.timerID.setType(timerID);
    	}
    	    
        this.timerValue = new ASNInteger();
        this.timerValue.setValue(Long.valueOf(timerValue));
        
        this.extensions = extensions;
        
        if(callSegmentID!=null) {
        	this.callSegmentID = new ASNInteger();
        	this.callSegmentID.setValue(callSegmentID.longValue());
        }
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.resetTimer_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.resetTimer;
    }

    @Override
    public TimerID getTimerID() {
    	if(timerID==null)
    		return null;
    	
        return timerID.getType();
    }

    @Override
    public int getTimerValue() {
    	if(timerID==null || timerID.getValue()==null)
    		return 0;
    	
        return timerValue.getValue().intValue();
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return extensions;
    }

    @Override
    public Integer getCallSegmentID() {
    	if(callSegmentID==null || callSegmentID.getValue()==null)
    		return null;
    	
        return callSegmentID.getValue().intValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ResetTimerRequest [");
        this.addInvokeIdInfo(sb);

        if (this.timerID != null) {
            sb.append(", timerID=");
            sb.append(timerID.toString());
        }
        sb.append(", timerValue=");
        sb.append(timerValue);
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }
        if (this.callSegmentID != null) {
            sb.append(", callSegmentID=");
            sb.append(callSegmentID.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
