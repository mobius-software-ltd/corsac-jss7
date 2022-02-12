package com.mobius.software.telco.protocols.ss7.asn.primitives;

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

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class ASNSingleByte {
	private Integer value;
	private String name;
	private Integer minValue;
	private Integer maxValue;
	private Boolean isRoot;
	
	//required for parser
	public ASNSingleByte() {
		
	}
	
	public ASNSingleByte(String name,Integer minValue,Integer maxValue,Boolean isRoot) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isRoot = isRoot;
	}
	
	public ASNSingleByte(Integer value,String name,Integer minValue,Integer maxValue,Boolean isRoot) {
		this.value=value;
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.isRoot = isRoot;
	}
	
	public Integer getValue() {
		return value;
	}

	@ASNLength
	public Integer getLength(ASNParser parser) {
		if(value==null)
			return 0;
		
		return 1;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) {
		if(value==null)
			return;
		
		buffer.writeByte(value);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		if(buffer.readableBytes()==0)
			return false;
		
		value=buffer.readByte() & 0x0FF;
		return false;
	}
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if((minValue!=null || maxValue!=null) && value==null) {
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedRootParameter);
		} else if(minValue!=null && value!=null && value<minValue){
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " should be at least " + minValue,ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " should be at least " + minValue,ASNParsingComponentExceptionReason.MistypedRootParameter);
		} else if(maxValue!=null && value!=null && value>maxValue){
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " should be at most " + maxValue,ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " should be at most " + maxValue,ASNParsingComponentExceptionReason.MistypedRootParameter);
		} 
			
	}
}