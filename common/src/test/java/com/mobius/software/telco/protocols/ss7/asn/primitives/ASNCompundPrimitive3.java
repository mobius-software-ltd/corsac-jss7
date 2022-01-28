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
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=121,constructed=true,lengthIndefinite=true)
public class ASNCompundPrimitive3 {
	
	@ASNProperty(tag=102,asnClass=ASNClass.PRIVATE,constructed=false,index=-1)
	private ASNUTF8String field1;
	
	private ASNUTF8String field2;
	
	public ASNCompundPrimitive3() {
		
	}
	
	public ASNCompundPrimitive3(String field1,String field2) {
		if(field1!=null)
			this.field1=new ASNUTF8String(field1);
		
		if(field2!=null)
			this.field2=new ASNUTF8String(field2);			
	}

	public String getField1() {
		if(field1==null)
			return null;
		
		return field1.getValue();
	}

	public void setField1(String field1) {
		if(field1==null) {
			this.field1=null;
			return;
		}
		
		this.field1=new ASNUTF8String(field1);		
	}

	public String getField2() {
		if(field2==null)
			return null;
		
		return field2.getValue();
	}

	public void setField2(String field2) {
		if(field2==null) {
			this.field2=null;
			return;
		}
		
		this.field2=new ASNUTF8String(field2);		
	}
}