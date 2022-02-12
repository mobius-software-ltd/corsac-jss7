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
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public class ASNBitString 
{	
	private static final int masks[] = {128, 64, 32, 16, 8, 4, 2, 1};
	private ConcurrentHashMap<Integer,AtomicInteger> value=new ConcurrentHashMap<Integer,AtomicInteger>();
	private Integer maxByteUsed=0;
	private Integer maxBitUsed=0;
	private Boolean isRoot;
	private String name;
	private Integer minBytesIndex;
	private Integer minBitsIndex;
	private Integer maxBitsIndex;
	
	public ASNBitString() {
		
	}
	
	public ASNBitString(String name,Integer minBitsIndex,Integer maxBitsIndex,Boolean isRoot) {
		this.name=name;
		this.minBitsIndex=minBitsIndex;
		if(minBitsIndex!=null)
			this.minBytesIndex=minBitsIndex/8;
		
		this.maxBitsIndex=maxBitsIndex;
		this.isRoot=isRoot;
	}
	
	@ASNLength
	public Integer getLength(ASNParser parser) {
		if(minBytesIndex==null || minBytesIndex<maxByteUsed)
			return maxByteUsed+2;
		
		return minBytesIndex+2;
	}
	
	public void setBit(int index) {
		if(maxBitUsed!=null && maxBitUsed<index)
			maxBitUsed=index;
		
		int byteIndex=index/8;
		int bitIndex=index%8;
		if(maxByteUsed<byteIndex)
			maxByteUsed=byteIndex;
		
		AtomicInteger existingByte=value.get(byteIndex);
		if(existingByte==null) {
			existingByte=new AtomicInteger((byte)0);
			AtomicInteger oldByte=value.putIfAbsent(byteIndex, existingByte);
			if(oldByte!=null)
				existingByte=oldByte;
		}
		
		existingByte.addAndGet(masks[bitIndex]);
	}
	
	public Boolean isBitSet(int index) {
		int byteIndex=index/8;
		int bitIndex=index%8;
		AtomicInteger existingByte=value.get(byteIndex);
		if(existingByte==null)
			return false;
				
		return (existingByte.get() & masks[bitIndex]) !=0;	
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) {
		Integer lastByte=0;
		Integer lastByteIndex=maxByteUsed;
		if(minBytesIndex!=null && minBytesIndex>maxByteUsed)
			lastByteIndex=minBytesIndex;
		
		AtomicInteger lastValue=value.get(lastByteIndex);
		if(lastValue!=null)
			lastByte=lastValue.get();
		Integer remainingBits=0;
		if(minBitsIndex!=null && minBitsIndex>maxBitUsed)
			remainingBits=8-((minBitsIndex+1)%8);
		else {
			for(int i=masks.length-1;i>=0;i--) {
				if((lastByte & masks[i])!=0)
					break;
				else
					remainingBits++;
			}
		}
		
		if(remainingBits==8)
			buffer.writeByte(0);
		else
			buffer.writeByte(remainingBits);
			
		for(int i=0;i<=lastByteIndex;i++) {
			AtomicInteger current=value.get(i);
			if(current==null)
				buffer.writeByte(0);
			else
				buffer.writeByte(current.get());
		}
	}
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(maxBitsIndex!=null && maxBitUsed!=null && maxBitUsed>maxBitsIndex) {
			if(isRoot==null || !isRoot)
				throw new ASNParsingComponentException(name + " max bit allowed is " + maxBitsIndex + ",while max bit set is " + maxBitUsed,ASNParsingComponentExceptionReason.MistypedParameter);
			else
				throw new ASNParsingComponentException(name + " max bit allowed is " + maxBitsIndex + ",while max bit set is " + maxBitUsed,ASNParsingComponentExceptionReason.MistypedRootParameter);
		}	
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) {
		//ignoring first byte
		buffer.readByte();
		int count=0;
		while(buffer.readableBytes()>0) {
			value.put(count++, new AtomicInteger(buffer.readByte() & 0x0FF));
		}
		
		maxByteUsed=count-1;
		return false;
	}
}