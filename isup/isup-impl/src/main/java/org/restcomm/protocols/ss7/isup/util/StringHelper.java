package org.restcomm.protocols.ss7.isup.util;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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

public class StringHelper {
	public static char[] TELCO_DIGITS= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', '*', '#', 'd','e','f','*','#' };
	public static char[] REGULAR_DIGITS= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e','f','*','#' };
	public static ConcurrentHashMap<Integer, Integer> TELCO_MAP=new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentHashMap<Integer, Integer> TELCO_INDEX_MAP=new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentHashMap<Integer, Integer> REGULAR_MAP=new ConcurrentHashMap<Integer, Integer>();
	
	static {
		for(int i=0;i<TELCO_DIGITS.length;i++) {
			TELCO_MAP.put((int)TELCO_DIGITS[i], (int)REGULAR_DIGITS[i]);
			if(i<16)
				TELCO_INDEX_MAP.put((int)REGULAR_DIGITS[i], i);
			else
				TELCO_INDEX_MAP.put((int)REGULAR_DIGITS[i], i-5);
			
			REGULAR_MAP.put((int)TELCO_DIGITS[i], (int)REGULAR_DIGITS[i]);
		}
	}
	
	public static String fromTelco(String digits) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<digits.length();i++) {
			Integer value=TELCO_MAP.get((int)digits.charAt(i));
			if(value!=null)
				sb.append((char)value.intValue());
			else
				sb.append(digits.charAt(i));
		}
		
		return sb.toString();
	}
	
	public static String fromBinary(ByteBuf buffer, Boolean odd) {
		StringBuilder sb = new StringBuilder();
		while(buffer.readableBytes()>0) {
			byte current=buffer.readByte();
			sb.append(TELCO_DIGITS[(current & 0x0F)]);
            sb.append(TELCO_DIGITS[(((current & 0xF0)>>4) & 0x0F)]);
		}
		
		if(odd)
			 sb.deleteCharAt(sb.length()-1);
		
		return sb.toString();
	}
	
	public static ByteBuf toBinary(String digits) {
		int bytes=(digits.length()/2);
		if(digits.length()%2==1)
			bytes=(digits.length()/2) + 1;
		
		ByteBuf result=Unpooled.buffer(bytes);
		for(int i=0;i<bytes;i++) {
			Integer curr =  TELCO_INDEX_MAP.get((int)digits.charAt(i*2));
			if(curr==null)
				curr=0;
			
			if(digits.length()>i*2 + 1) {
				Integer second =  TELCO_INDEX_MAP.get((int)digits.charAt(i*2 + 1));
				if(second==null)
					second=0;
				
				curr=curr + second*16;
			}
				
			result.writeByte(curr);
		}
		
		return result;
	}
}