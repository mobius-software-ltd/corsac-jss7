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
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=10,constructed=false,lengthIndefinite=false)
public class ASNEnumerated extends ASNInteger {
	//required for parser
	public ASNEnumerated() {
	}
	
	public ASNEnumerated(String name,Integer minValue,Integer maxValue,Boolean isRoot) {
		super(null,name,minValue,maxValue,isRoot);
	}
	
	public ASNEnumerated(Integer value,String name,Integer minValue,Integer maxValue,Boolean isRoot) {
		super(value,name,minValue,maxValue,isRoot);
	}
}