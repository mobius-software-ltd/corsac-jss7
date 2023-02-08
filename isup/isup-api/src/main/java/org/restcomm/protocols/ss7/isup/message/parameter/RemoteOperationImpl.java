package org.restcomm.protocols.ss7.isup.message.parameter;

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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

/**
*
* @author yulian oifa
*
*/

import org.restcomm.protocols.ss7.isup.message.parameter.RemoteOperation.OperationType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0c,constructed=true,lengthIndefinite=false)
public class RemoteOperationImpl {
	InvokeImpl invoke;
	ReturnResultImpl returnResult;
	RejectImpl reject;
	ReturnErrorImpl returnError;
	
	public InvokeImpl getInvoke() {
		return invoke;
	}

	public ReturnResultImpl getReturnResult() {
		return returnResult;
	}

	public RejectImpl getReject() {
		return reject;
	}

	public ReturnErrorImpl getReturnError() {
		return returnError;
	}

	public RemoteOperation getExistingComponent() {
		if(invoke!=null)
			return invoke;
		
		if(returnResult!=null)
			return returnResult;
		
		if(reject!=null)
			return reject;
		
		if(returnError!=null)
			return returnError;
		
		return null;		
	}

	public void setInvoke(InvokeImpl value) {
		this.invoke=value;
	}

	public void setReturnResult(ReturnResultImpl value) {
		this.returnResult=value;
	}

	public void setReject(RejectImpl value) {
		this.reject=value;
	}

	public void setReturnError(ReturnErrorImpl value) {
		this.returnError=value;
	}

	public OperationType getType() {
		if(invoke!=null)
			return OperationType.Invoke;
		
		if(returnResult!=null)
			return OperationType.ReturnResult;
		
		if(reject!=null)
			return OperationType.Reject;
		
		if(returnError!=null)
			return OperationType.ReturnError;
		
		return null;
	}
}