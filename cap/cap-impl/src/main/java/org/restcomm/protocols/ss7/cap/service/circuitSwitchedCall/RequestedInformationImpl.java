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

import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall.ASNRequestedInformationTypeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class RequestedInformationImpl implements RequestedInformation {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNRequestedInformationTypeImpl requestedInformationType;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private RequestedInformationValueWrapperImpl requestedInformationValue;
    
    public RequestedInformationImpl() {
    }

    public RequestedInformationImpl(RequestedInformationType requestedInformationType, int intValue) {
        if (requestedInformationType == RequestedInformationType.callAttemptElapsedTime)
            this.requestedInformationType = new ASNRequestedInformationTypeImpl(RequestedInformationType.callAttemptElapsedTime);
        else
        	this.requestedInformationType = new ASNRequestedInformationTypeImpl(RequestedInformationType.callConnectedElapsedTime);
        	
        this.requestedInformationValue = new RequestedInformationValueWrapperImpl(new RequestedInformationValueImpl(requestedInformationType, intValue));
    }

    public RequestedInformationImpl(DateAndTime callStopTimeValue) {
    	this.requestedInformationType = new ASNRequestedInformationTypeImpl(RequestedInformationType.callStopTime);
        this.requestedInformationValue = new RequestedInformationValueWrapperImpl(new RequestedInformationValueImpl(callStopTimeValue));
    }

    public RequestedInformationImpl(CauseIsup releaseCauseValue) {
    	this.requestedInformationType = new ASNRequestedInformationTypeImpl(RequestedInformationType.releaseCause);
        this.requestedInformationValue = new RequestedInformationValueWrapperImpl(new RequestedInformationValueImpl(releaseCauseValue));        
    }

    public RequestedInformationType getRequestedInformationType() {
    	if(requestedInformationType==null)
    		return null;
    	
        return requestedInformationType.getType();
    }

    public Integer getCallAttemptElapsedTimeValue() {
    	if(requestedInformationValue==null || requestedInformationValue.getRequestedInformationValue()==null)
    		return null;
    	
        return requestedInformationValue.getRequestedInformationValue().getCallAttemptElapsedTimeValue();
    }

    public DateAndTime getCallStopTimeValue() {
    	if(requestedInformationValue==null || requestedInformationValue.getRequestedInformationValue()==null)
    		return null;
    	
        return requestedInformationValue.getRequestedInformationValue().getCallStopTimeValue();
    }

    public Integer getCallConnectedElapsedTimeValue() {
    	if(requestedInformationValue==null || requestedInformationValue.getRequestedInformationValue()==null)
    		return null;
    	
        return requestedInformationValue.getRequestedInformationValue().getCallConnectedElapsedTimeValue();
    }

    public CauseIsup getReleaseCauseValue() {
    	if(requestedInformationValue==null || requestedInformationValue.getRequestedInformationValue()==null)
    		return null;
    	
        return requestedInformationValue.getRequestedInformationValue().getReleaseCauseValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestedInformation [");
        if (this.requestedInformationType != null) {
            sb.append("requestedInformationType=");
            sb.append(requestedInformationType);
        }
        if (this.requestedInformationValue != null && this.requestedInformationValue.getRequestedInformationValue()!=null) {
            sb.append(", requestedInformationValue=");
            sb.append(this.requestedInformationValue.getRequestedInformationValue());
        }        
        sb.append("]");

        return sb.toString();
    }
}
