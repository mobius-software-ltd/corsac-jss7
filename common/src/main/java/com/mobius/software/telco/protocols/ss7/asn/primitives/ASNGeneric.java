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
import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNGenericMapping;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.PRIVATE,tag=0,constructed=false,lengthIndefinite=false)
public abstract class ASNGeneric {
	public ASNGeneric() {
		
	}
	
	public ASNGeneric(Object value) {
		this.value=value;
	}
	
	Object value;
	
	@ASNLength
	public Integer getLength(ASNParser parser) throws ASNException {
		if(value==null)
			return 0;
		
		return parser.getParser(this.getClass()).getLength(value);
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws ASNException {
		if(value==null)
			return;
		
		parser.getParser(this.getClass()).encode(buffer,value);
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,Boolean skipErrors) throws ASNException {
		if(buffer.readableBytes()==0)
		{
			this.value=null;
			return false;
		}
		
		Class<?> clazz=this.getClass();
		if(parent!=null) {
			Method[] methods=parent.getClass().getMethods();
			for(Method method:methods) {
				ASNGenericMapping mapping=method.getAnnotation(ASNGenericMapping.class);
				if(mapping!=null) {
					Class<?> innerClass=null;
					try
					{
						innerClass=(Class<?>)method.invoke(parent, parser);
					}
					catch(Exception ex) 
					{
					}
					
					if(innerClass!=null)
						clazz=innerClass;
					break;
				}
			}
		}
		
		ASNDecodeResult result;
		if(this.value!=null) {
			result=parser.getParser(clazz).decode(buffer,skipErrors);
			if(!result.getHadErrors() && result.getResult()!=null)
				parser.getParser(clazz).merge(this.value, result.getResult());							
		}
		else {
			result=parser.getParser(clazz).decode(buffer,skipErrors);
			this.value=result.getResult();
		} 
			
		return result.getHadErrors();
	}

	public Object getValue() {
		return value;
	}		
}
