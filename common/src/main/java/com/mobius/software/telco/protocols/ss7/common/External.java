package com.mobius.software.telco.protocols.ss7.common;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

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

public interface External
{
	public void setIdentifier(Long value);
	
	public void setIdentifier(String value);
	
	public void setIdentifier(List<Long> ids);
	
	public void setChild(byte[] value);
	
	public void setChild(ASNBitString bitString);
	
	public void setChildAsObject(Object value);
	
	public Boolean isIDIndirect();
	
	public Boolean isIDObjectIdentifier();
	
	public Boolean isIDDescriptor();
	
	public Boolean isValueObject();
	
	public Boolean isValueString();
	
	public Boolean isValueBitString();
	
	public Long getIndirectReference();
	
	public List<Long> getObjectIdentifier();
	
	public String getDescriptor();
	
	public Object getChild();
	
	public byte[] getChildString();
	
	public ASNBitString getBitString();		
}