package com.mobius.software.telco.protocols.ss7.asn;

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

public enum FieldType 
{
	STANDARD(0),CHOISE(1),WILDCARD(2);

	private static final Map<Integer, FieldType> intToTypeMap = new HashMap<Integer, FieldType>();
	static 
	{
	    for (FieldType type : FieldType.values()) 
	    {
	        intToTypeMap.put(type.value, type);
	    }
	}

	public static FieldType fromInt(int i) 
	{
		FieldType type = intToTypeMap.get(Integer.valueOf(i));
	    if (type == null) 
	        return FieldType.STANDARD;
	    
	    return type;
	}
	
	private int value;
	
	private FieldType(int value)
	{
		this.value=value;
	}
	
	public int getValue()
	{
		return value;
	}
}
