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
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.APPLICATION,tag=0x0c,constructed=true,lengthIndefinite=false)
public class ComponentImpl {
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = InvokeImpl.class)
	Invoke invoke;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1,defaultImplementation = ReturnResultImpl.class)
	ReturnResult returnResult;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = ReturnResultLastImpl.class)
	ReturnResultLast returnResultLast;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=true,index=-1,defaultImplementation = RejectImpl.class)
	Reject reject;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = ReturnErrorImpl.class)
	ReturnError returnError;
	
	public Invoke getInvoke() {
		return invoke;
	}

	public ReturnResult getReturnResult() {
		return returnResult;
	}

	public ReturnResultLast getReturnResultLast() {
		return returnResultLast;
	}

	public Reject getReject() {
		return reject;
	}

	public ReturnError getReturnError() {
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

	public void setInvoke(Invoke value) {
		this.invoke=value;
	}

	public void setReturnResult(ReturnResult value) {
		this.returnResult=value;
	}

	public void setReturnResultLast(ReturnResultLast value) {
		this.returnResultLast=value;
	}

	public void setReject(Reject value) {
		this.reject=value;
	}

	public void setReturnError(ReturnError value) {
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