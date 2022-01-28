package com.mobius.software.telco.protocols.ss7.asn.primitives;

import java.nio.charset.Charset;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

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

@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=9,constructed=false,lengthIndefinite=false)
public class ASNReal {
	public static final Charset CHARSET=Charset.forName("US-ASCII");
	
	public static final int REAL_BB_BASE_MASK = 0x30;	
	public static final int REAL_BB_SIGN_MASK = 0x40;
	public static final int REAL_BB_SCALE_MASK = 0xC;
	public static final int REAL_BB_EE_MASK = 0x3;
	
	private Double value;
	
	public ASNReal() {
		
	}
	
	public ASNReal(Double value) {
		this.value=value;
	}
		
	public Double getValue() {
		return value;
	}

	@ASNLength
	public Integer getLength(ASNParser parser) {
		if(value==null)
			return 0;
		
		if (value == 0) 
			return 0;
		
		if (value == Double.POSITIVE_INFINITY) 
			return 1;
		
		if (value == Double.NEGATIVE_INFINITY) 
			return 1;
		
		return 10;
	}
	
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) {
		if(value==null || value==0)
			return;
		
		if (value == Double.POSITIVE_INFINITY) {
			buffer.writeByte(0x40);
			return;
		}			

		if (value == Double.NEGATIVE_INFINITY) {
			buffer.writeByte(0x41);
			return;
		}
		
		long bits = Double.doubleToLongBits(value);
		int info = ((int) (bits >> 57)) & 0x40;
		info |= 0x81;

		buffer.writeByte(info);

		buffer.writeByte((byte) (((int) (bits >> 60)) & 0x07));
		buffer.writeByte((byte) (bits >> 52));
		
		buffer.writeByte((byte)((bits >> 48) & 0x0F));
		buffer.writeByte((byte)(bits >> 40));
		buffer.writeByte((byte)(bits >> 32));
		buffer.writeByte((byte)(bits >> 24));
		buffer.writeByte((byte)(bits >> 16));
		buffer.writeByte((byte)(bits >> 8));
		buffer.writeByte((byte)bits);		
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,Boolean skipErrors) {
		if(buffer.readableBytes()==0)
			return false;
		
		if (buffer.readableBytes() == 1) {
			// +INF/-INF
			int b = buffer.readByte() & 0xFF;
			if (b == 0x40)
				value = Double.POSITIVE_INFINITY;
			else if (b == 0x41)
				value = Double.NEGATIVE_INFINITY;
			else
				return true;

			return false;
		}
		
		int infoBits = buffer.readByte();
		if ((infoBits & 0xC0) == 0) {
			try {
				String nrRep = buffer.toString(CHARSET);
				value = Double.parseDouble(nrRep);
			}
			catch(Exception ex) {
				return true;
			}
			return false;
		} else if((infoBits & 0x80) == 0x80) {
			int tmp = 0;
			int signBit = (infoBits & REAL_BB_SIGN_MASK) << 1;
			long e = 0;
			int s = (infoBits & REAL_BB_SCALE_MASK) >> 2;
			tmp = infoBits & REAL_BB_EE_MASK;
			if (tmp == 0x0) {
				e = buffer.readByte() & 0xFF;				
			} else if (tmp == 0x01) {
				e = (buffer.readByte() & 0xFF) << 8;
				e |= buffer.readByte() & 0xFF;
				if (e > 0x7FF) 
					return true;

				e &= 0x7FF;
			} else 
				return true;


			if (buffer.readableBytes() > 7)
				return true;

			long n = 0;
			while (buffer.readableBytes() > 0) {
				long readV = (((long) buffer.readByte() << 32) >>> 32) & 0xFF;
				readV = readV << (buffer.readableBytes() * 8);
				n |= readV;
			}

			if ((n & 0x0FFFFFFF) > 4503599627370495L) 
				return true;
			
			int shift = (int) Math.pow(2, s) - 1;
			n = n << (shift);
			int base = (infoBits & REAL_BB_BASE_MASK) >> 4;
			if (base == 0x01)
				e = e * 3;
			else if (base == 0x10)
				e = e * 4;
			
			if (e > 0x7FF) 
				return true;

			ByteBuf doubleRep = Unpooled.buffer(8);
			doubleRep.writeByte((byte)(signBit | ((e >> 4) & 0xFF)));
			doubleRep.writeByte((byte)(((e & 0x0F) << 4) | ((n >> 48) & 0x0F)));
			doubleRep.writeByte((byte) (n >> 40));
			doubleRep.writeByte((byte) (n >> 32));
			doubleRep.writeByte((byte) (n >> 24));
			doubleRep.writeByte((byte) (n >> 16));
			doubleRep.writeByte((byte) (n >> 8));
			doubleRep.writeByte((byte) n);
			value=doubleRep.readDouble();
			return false;
		} else 
			return true;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		ASNReal other = (ASNReal) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		
		return true;
	}
}