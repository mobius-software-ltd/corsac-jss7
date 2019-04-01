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

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=6,constructed=false,lengthIndefinite=false)
public class ASNObjectIdentifier {
	private List<Long> value=new ArrayList<Long>();
	
	@ASNLength
	public Integer getLength() {
		return getLength(value);
	}
	
	public void setValue(List<Long> value) {
		this.value=value;
	}
	
	public void addOid(Long value) {
		this.value.add(value);
	}
	
	public List<Long> getValue() {
		return this.value;
	}
	
	@ASNEncode
	public void encode(ByteBuf buffer) {
		buffer.writeByte((int)(0x00FF & value.get(0) * 40 + value.get(1)));

		for (int i = 2; i < value.size(); ++i) {
			long currValue=value.get(i);
			int len = getOIDLength(currValue);

			for (int j = len - 1; j > 0; --j) {
				long m = 0x0080 | (0x007F & (currValue >> (j * 7)));
				buffer.writeByte((int) m);
			}
			
			buffer.writeByte((int) (0x007F & currValue));
		}
	}
	
	@ASNDecode
	public Boolean decode(ByteBuf buffer,Boolean skipErrors) {
		int b = 0x00FF & buffer.readByte();

		long currValue = b / 40;
		if (currValue == 0 || currValue == 1) {
			value.add(currValue);
			value.add((long)(b%40));
		} else {
			value.add(2L);
			value.add(b - 80L);
		}
		
		currValue = 0;
		while(buffer.readableBytes()>0) {
			byte b1 = buffer.readByte();
			currValue = (currValue << 7) | ((b1 & 0x7F));
			if ((b1 & 0x80) == 0x0) {
				value.add(currValue);
				currValue = 0;
			}
		}
		
		return false;
	}
	
	public static int getLength(List<Long> value)
	{
		int length=1;
		for(int i=2;i<value.size();i++) {
			long current=value.get(i);
			if (current < 0) {
				length+=10;
			}
			else {
				int l=1;
				for (int j = 1; j < 9; j++) {
					length++;
					l <<= 7;
					if (current < l)
						break;
				}							
			}
		}
		
		return length;
	}
	
	private static int getOIDLength(long value) {
		if (value < 0) {
			return 10;
		}
		else {
			int l=1;
			for (int j = 1; j < 9; j++) {
				l <<= 7;
				if (value < l)
					return j;
			}

			return 9;
		}
	}
}