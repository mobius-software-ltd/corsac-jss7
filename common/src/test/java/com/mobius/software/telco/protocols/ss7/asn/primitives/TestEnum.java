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

import java.util.HashMap;
import java.util.Map;

public enum TestEnum 
{
	ZERO_VALUE(0),VALUE_1(1),VALUE_2(2),UNKNOWN(-1);
	
	private static final Map<Integer, TestEnum> intToTypeMap = new HashMap<Integer, TestEnum>();
	static 
	{
	    for (TestEnum type : TestEnum.values()) 
	    {
	        intToTypeMap.put(type.value, type);
	    }
	}

	public static TestEnum fromInt(int i) 
	{
		TestEnum type = intToTypeMap.get(Integer.valueOf(i));
	    if (type == null) 
	        return TestEnum.UNKNOWN;
	    
	    return type;
	}
	
	int value;
	
	private TestEnum(int value)
	{
		this.value=value;
	}
	
	public int getValue()
	{
		return value;
	}
}
