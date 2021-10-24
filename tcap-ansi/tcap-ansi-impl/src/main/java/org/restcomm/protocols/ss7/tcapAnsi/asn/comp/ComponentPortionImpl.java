package org.restcomm.protocols.ss7.tcapAnsi.asn.comp;

import java.util.ArrayList;

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

import java.util.List;

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.WrappedComponent;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNExclude;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x08,constructed=true,lengthIndefinite=false)
public class ComponentPortionImpl implements ComponentPortion {

	@ASNChoise
	List<WrappedComponentImpl> components;

	@ASNExclude
	List<WrappedComponent> realComponents;
	
	public List<WrappedComponent> getComponents() {
		if(realComponents==null && components!=null) {
			realComponents=new ArrayList<WrappedComponent>();
			for(WrappedComponentImpl curr:components)
				realComponents.add(curr);
		}
		
		return realComponents;		
	}

	public void setComponents(List<WrappedComponent> components) {
		this.realComponents=components;
		if(components==null)
			this.components=null;
		else {
			this.components=new ArrayList<WrappedComponentImpl>();
			for(WrappedComponent curr:components) {
				if(curr instanceof WrappedComponentImpl)
					this.components.add((WrappedComponentImpl)curr);
				else {
					WrappedComponentImpl currComp=new WrappedComponentImpl();
					switch(curr.getType()) {
						case InvokeLast:
							currComp.setInvokeLast(curr.getInvokeLast());
							break;
						case InvokeNotLast:
							currComp.setInvoke(curr.getInvoke());
							break;
						case Reject:
							currComp.setReject(curr.getReject());
							break;
						case ReturnError:
							currComp.setReturnError(curr.getReturnError());
							break;
						case ReturnResultLast:
							currComp.setReturnResultLast(curr.getReturnResultLast());
							break;
						case ReturnResultNotLast:
							currComp.setReturnResult(curr.getReturnResult());
							break;
						default:
							break;					
					}
					
					this.components.add(currComp);
				}					
			}		
		}	
	}
	
	public String toString() {
		return "ComponentPortion[" + components + "]";
	}
}