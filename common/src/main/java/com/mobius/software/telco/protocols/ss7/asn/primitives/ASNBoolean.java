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

import io.netty.buffer.ByteBuf;

import java.util.concurrent.ConcurrentHashMap;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=1,constructed=false,lengthIndefinite=false)
public class ASNBoolean 
{	
	private Boolean value;
	
	private String name;
	private Boolean required;
	private Boolean isRoot;
	
	//required for parser
	public ASNBoolean() {
		
	}
	
	public ASNBoolean(String name,Boolean required,Boolean isRoot) {
		this.name=name;
		this.required=required;
		this.isRoot=isRoot;
	}
	
	public ASNBoolean(Boolean value,String name,Boolean required,Boolean isRoot) {		
		this.value=value;
		this.name=name;
		this.required=required;
		this.isRoot=isRoot;
	}
	
	public Boolean getValue() {
		return value;
	}

	@ASNLength
	public Integer getLength(ASNParser parser) {
		return 1;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) {
		if(value)
			buffer.writeByte(0xFF);
		else
			buffer.writeByte(0);
	}
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(required!=null && required && value==null) {
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedRootParameter);
		}	
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		if(buffer.readByte()==0x00)
			value=false;
		else
			value=true;
		
		return false;
	}
}