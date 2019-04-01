package org.restcomm.protocols.ss7.tcap.asn.comp;

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
	InvokeImpl invoke;
	ReturnResultImpl returnResult;
	ReturnResultLastImpl returnResultLast;
	RejectImpl reject;
	ReturnErrorImpl returnError;
	
	public InvokeImpl getInvoke() {
		return invoke;
	}

	public ReturnResultImpl getReturnResult() {
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

	public void setInvoke(InvokeImpl value) {
		this.invoke=value;
	}

	public void setReturnResult(ReturnResultImpl value) {
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
			return ComponentType.Invoke;
		
		if(returnResult!=null)
			return ComponentType.ReturnResult;
		
		if(returnResultLast!=null)
			return ComponentType.ReturnResultLast;
		
		if(reject!=null)
			return ComponentType.Reject;
		
		if(returnError!=null)
			return ComponentType.ReturnError;
		
		return null;
	}
}