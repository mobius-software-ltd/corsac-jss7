package com.mobius.software.telco.protocols.ss7.asn.primitives;

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

import java.util.ArrayList;
import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,lengthIndefinite=false)
public class ASNCompundPrimitiveWithList1 {
	
	@ASNProperty(tag=23,asnClass=ASNClass.PRIVATE,constructed=false,index=-1)
	private List<ASNInteger> field1;
	
	public ASNCompundPrimitiveWithList1() {
		
	}
	
	public ASNCompundPrimitiveWithList1(List<Long> value) {
		setField1(value);
	}

	public List<Long> getField1() {
		if(field1==null)
			return null;
		
		List<Long> output=new ArrayList<Long>();
		for(ASNInteger curr:field1)
			output.add(curr.getValue());
		
		return output;
	}

	public void setField1(List<Long> field1) {
		this.field1=new ArrayList<ASNInteger>();
		for(Long currInteger:field1) {
			ASNInteger currValue=new ASNInteger(currInteger,null,null,null,false);
			this.field1.add(currValue);
		}
	}
}