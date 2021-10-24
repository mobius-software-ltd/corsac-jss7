package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentType;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Return;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;

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
public class WrappedComponentImpl implements WrappedComponent {
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=13,constructed=true,index=-1,defaultImplementation = InvokeNotLastImpl.class)
	Invoke invoke;
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=9,constructed=true,index=-1,defaultImplementation = InvokeLastImpl.class)
	Invoke invokeLast;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=14,constructed=true,index=-1,defaultImplementation = ReturnResultNotLastImpl.class)
	Return returnResult;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=10,constructed=true,index=-1,defaultImplementation = ReturnResultNotLastImpl.class)
	Return returnResultLast;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=12,constructed=true,index=-1,defaultImplementation = RejectImpl.class)
	Reject reject;
	
	@ASNProperty(asnClass=ASNClass.PRIVATE,tag=11,constructed=true,index=-1,defaultImplementation = ReturnErrorImpl.class)
	ReturnError returnError;
	
	public Invoke getInvoke() {
		return invoke;
	}

	public Invoke getInvokeLast() {
		return invokeLast;
	}

	public Return getReturnResult() {
		return returnResult;
	}

	public Return getReturnResultLast() {
		return returnResultLast;
	}

	public Reject getReject() {
		return reject;
	}

	public ReturnError getReturnError() {
		return returnError;
	}

	public Component getExistingComponent() {
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

	public void setInvoke(Invoke value) {
		this.invoke=value;
	}

	public void setInvokeLast(Invoke value) {
		this.invokeLast=value;
	}

	public void setReturnResult(Return value) {
		this.returnResult=value;
	}

	public void setReturnResultLast(Return value) {
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