package org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp;

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

/**
*
* @author yulian oifa
*
*/

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0c,constructed=true,lengthIndefinite=false)
public class ComponentImpl {
	InvokeNotLastImpl invoke;
    InvokeLastImpl invokeLast;
    ReturnResultNotLastImpl returnResult;
	ReturnResultLastImpl returnResultLast;
	RejectImpl reject;
	ReturnErrorImpl returnError;
	
	public InvokeNotLastImpl getInvoke() {
		return invoke;
	}

	public InvokeLastImpl getInvokeLast() {
		return invokeLast;
	}

	public ReturnResultNotLastImpl getReturnResult() {
		return returnResult;
	}

	public ReturnResultLastImpl getReturnResultLast() {
		return returnResultLast;
	}

	public RejectImpl getReject() {
		return reject;
	}

	public ReturnErrorImpl getReturnError() {
		return returnError;
	}

	public BaseComponent getExistingComponent() {
		if(invoke!=null)
			return invoke;
		
		if(invokeLast!=null)
			return invokeLast;
		
		if(returnResult!=null)
			return returnResult;
		
		if(returnResultLast!=null)
			return returnResultLast;
		
		if(reject!=null)
			return reject;
		
		if(returnError!=null)
			return returnError;
		
		return null;		
	}

	public void setInvoke(InvokeNotLastImpl value) {
		this.invoke=value;
	}

	public void setInvokeLast(InvokeLastImpl value) {
		this.invokeLast=value;
	}

	public void setReturnResult(ReturnResultNotLastImpl value) {
		this.returnResult=value;
	}

	public void setReturnResultLast(ReturnResultLastImpl value) {
		this.returnResultLast=value;
	}

	public void setReject(RejectImpl value) {
		this.reject=value;
	}

	public void setReturnError(ReturnErrorImpl value) {
		this.returnError=value;
	}

	public ComponentType getType() {
		if(invoke!=null)
			return ComponentType.InvokeNotLast;
		
		if(invokeLast!=null)
			return ComponentType.InvokeLast;
		
		if(returnResult!=null)
			return ComponentType.ReturnResultNotLast;
		
		if(returnResultLast!=null)
			return ComponentType.ReturnResultLast;
		
		if(reject!=null)
			return ComponentType.Reject;
		
		if(returnError!=null)
			return ComponentType.ReturnError;
		
		return null;
	}
}