package com.mobius.software.telco.protocols.ss7.asn;

import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

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

public class ASNHeaderWithLength extends ASNHeader {
	
	private Integer length;
	public ASNHeaderWithLength(ASNClass asnClass,Boolean isConstructed,Integer asnTag,Boolean indefiniteLength,Integer index,Integer length) {
		super(asnClass,isConstructed,asnTag,indefiniteLength,index);
		this.length=length;
	}
	
	public ASNHeaderWithLength(ASNTag asnTag,ASNClass realClass, Integer realTag,Boolean realConstructed,Integer index,Integer length) {
		super(asnTag,realClass,realTag,realConstructed,index);
		this.length=length;
	}

	public void setLength(int newLength) {
		this.length=newLength;
	}
	
	public Integer getLength() {
		return length;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}			
}