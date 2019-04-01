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
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNDecodeResult;
import com.mobius.software.telco.protocols.ss7.asn.ASNException;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.PRIVATE,tag=0,constructed=false,lengthIndefinite=false)
public abstract class ASNGeneric {
	private static ConcurrentHashMap<String,ASNParser> innerParser=new ConcurrentHashMap<String,ASNParser>();
	
	Object value;
	
	public ASNGeneric() {		
	}
	
	private static ASNParser getParser(Class<?> rootClazz) {
		ASNParser parser=innerParser.get(rootClazz.getCanonicalName());
		if(parser==null) {
			parser=new ASNParser();
			ASNParser oldParser=innerParser.putIfAbsent(rootClazz.getCanonicalName(), parser);
			if(oldParser!=null)
				parser=oldParser;				
		}
		
		return parser;
	}
	
	public static void clear(Class<?> rootClazz) {
		getParser(rootClazz).clear();
	}
	
	public static void registerAlternative(Class<?> rootClazz, Class<?> clazz) {
		getParser(rootClazz).loadClass(clazz);
	}
	
	@ASNLength
	public Integer getLength() throws ASNException {
		return getParser(this.getClass()).getLength(value);
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) throws ASNException {
		getParser(this.getClass()).encode(buffer,value);
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) throws ASNException {
		ASNDecodeResult result=getParser(this.getClass()).decode(buffer,skipErrors);
		this.value=result.getResult();
		return result.getHadErrors();
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}		
}
