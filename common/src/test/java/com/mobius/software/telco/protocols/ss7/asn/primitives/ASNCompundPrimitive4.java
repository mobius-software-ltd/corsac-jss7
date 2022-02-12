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

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ASNCompundPrimitive4 {
	
	@ASNProperty(tag=23,asnClass=ASNClass.PRIVATE,constructed=false,index=-1)
	private ASNInteger field1;
	
	private ASNInteger field2;
	
	private ASNBoolean field3;
	
	public ASNCompundPrimitive4() {
		
	}
	
	public ASNCompundPrimitive4(Long field1,Long field2,Boolean field3) {
		this.field1=new ASNInteger(field1,null,null,null,false);
		this.field2=new ASNInteger(field2,null,null,null,false);
		this.field3=new ASNBoolean(field3,null,false,false);
	}

	public Long getField1() {
		if(field1==null)
			return null;
		
		return field1.getValue();
	}

	public void setField1(Long field1) {
		if(field1==null) {
			this.field1=null;
			return;
		}
		
		this.field1=new ASNInteger(field1,null,null,null,false);		
	}

	public Long getField2() {
		if(field2==null)
			return null;
		
		return field2.getValue();
	}

	public void setField2(Long field2) {
		if(field2==null) {
			this.field2=null;
			return;
		}
		
		this.field2=new ASNInteger(field2,null,null,null,false);		
	}

	public Boolean getField3() {
		if(field3==null)
			return null;
		
		return field3.getValue();
	}

	public void setField3(Boolean field3) {
		if(field3==null) {
			this.field3=null;
			return;
		}
		
		this.field3=new ASNBoolean(field3,null,false,false);		
	}
}