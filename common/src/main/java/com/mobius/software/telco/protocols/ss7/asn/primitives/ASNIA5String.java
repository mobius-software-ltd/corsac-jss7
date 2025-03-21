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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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

import io.netty.buffer.ByteBuf;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=22,constructed=false,lengthIndefinite=false)
public class ASNIA5String {
	public static final Charset ENCODING=Charset.forName("US-ASCII");
	private String value;
	private String name;
	private Integer minLength;
	private Integer maxLength;
	private Boolean isRoot;
	
	//required for parser
	public ASNIA5String() {
			
	}
		
	public ASNIA5String(String name,Integer minLength,Integer maxLength,Boolean isRoot) {
		this.name = name;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.isRoot = isRoot;
	}
	
	public ASNIA5String(String value,String name,Integer minLength,Integer maxLength,Boolean isRoot) {
		this.value=value;
		this.name = name;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.isRoot = isRoot;
	}
	
	public String getValue() {
		return value;
	}

	@ASNLength
	public Integer getLength(ASNParser parser) throws UnsupportedEncodingException {
		return getLength(value);
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws UnsupportedEncodingException {
		buffer.writeBytes(value.getBytes(ENCODING));
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors, Integer level) throws UnsupportedEncodingException {
		value=buffer.toString(ENCODING);
		return false;
	}
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(minLength!=null && value==null) {
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " is required",ASNParsingComponentExceptionReason.MistypedRootParameter);
		} else if(minLength!=null && value!=null && value.length()<minLength){
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " length should be at least " + minLength,ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " length should be at least " + minLength,ASNParsingComponentExceptionReason.MistypedRootParameter);
		} else if(maxLength!=null && value!=null && value.length()>maxLength){
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " length should be at most " + maxLength,ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " length should be at most " + maxLength,ASNParsingComponentExceptionReason.MistypedRootParameter);
		} 
			
	}
	
	public static int getLength(String value) throws UnsupportedEncodingException
	{
		return value.getBytes(ENCODING).length;
	}
}