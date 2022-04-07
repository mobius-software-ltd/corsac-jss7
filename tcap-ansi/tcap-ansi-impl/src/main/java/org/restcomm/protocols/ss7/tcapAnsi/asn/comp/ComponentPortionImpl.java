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

import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.restcomm.protocols.ss7.tcapAnsi.api.asn.comp.ComponentPortion;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWildcard;

@ASNTag(asnClass=ASNClass.PRIVATE,tag=0x08,constructed=true,lengthIndefinite=false)
public class ComponentPortionImpl implements ComponentPortion {

	@ASNWildcard
	List<ASNComponentPortionObjectImpl> components;

	public List<Component> getComponents() {
		if(components==null)
			return null;
		else {
			List<Component> output=new ArrayList<Component>();
			for(ASNComponentPortionObjectImpl curr:components)
				if(curr.getValue() instanceof Component)
					output.add((Component)curr.getValue());
			
			return output;
		}
	}

	public void setComponents(List<Component> components) {
		if(components==null)
			this.components=null;
		else {
			this.components = new ArrayList<ASNComponentPortionObjectImpl>();
			for(Component curr:components)
				this.components.add(new ASNComponentPortionObjectImpl(curr));
		}	
	}
	
	public String toString() {
		return "ComponentPortion[" + components + "]";
	}
}