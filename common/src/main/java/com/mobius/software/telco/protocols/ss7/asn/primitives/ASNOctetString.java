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
import io.netty.buffer.Unpooled;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class ASNOctetString {
	private ByteBuf value;
	
	public ByteBuf getValue() {
		if(value==null)
			return Unpooled.EMPTY_BUFFER;
		
		return Unpooled.wrappedBuffer(value);
	}

	public void setValue(ByteBuf value) {
		this.value = Unpooled.wrappedBuffer(value);				
	}

	@ASNLength
	public Integer getLength() {
		return getLength(getValue());
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) {
		buffer.writeBytes(getValue());
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) {
		if(buffer.readableBytes()>0)
			value=Unpooled.wrappedBuffer(buffer);
		else
			value=Unpooled.EMPTY_BUFFER;
		
		return false;
	}
	
	public static int getLength(ByteBuf value)
	{		
		return value.readableBytes();
	}
}