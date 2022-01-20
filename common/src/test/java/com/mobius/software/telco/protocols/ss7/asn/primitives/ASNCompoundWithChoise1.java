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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=true,lengthIndefinite=false)
public class ASNCompoundWithChoise1 {
	
	@ASNChoise
	private ASNCompoundPrimitive1 field1;
	
	private ASNNull field2;
	
	public ASNCompoundWithChoise1() {
		
	}
	
	public ASNCompoundWithChoise1(ASNCompoundPrimitive1 field1,Boolean field2Set) {
		this.field1=field1;
				
		if(field2Set!=null && field2Set)
			this.field2=new ASNNull();		
	}

	public ASNCompoundPrimitive1 getField1() {
		return field1;
	}

	public void setField1(ASNCompoundPrimitive1 field1) {
		this.field1 = field1;
	}

	public ASNNull getField2() {
		return field2;
	}

	public void setField2(ASNNull field2) {
		this.field2 = field2;
	}		
}