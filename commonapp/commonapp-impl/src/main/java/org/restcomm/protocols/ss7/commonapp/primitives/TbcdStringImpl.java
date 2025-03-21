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

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.concurrent.ConcurrentHashMap;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public abstract class TbcdStringImpl {
	protected static int DIGIT_1_MASK = 0x0F;
    protected static int DIGIT_2_MASK = 0xF0;

    protected static int DIGIT_MASK = 0xFF;

    protected String data;

    protected int minLength;
    protected int maxLength;
    protected String _PrimitiveName;
    protected Boolean hasFiller;    
    
    public TbcdStringImpl(int minLength, int maxLength, String _PrimitiveName,Boolean hasFiller) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this._PrimitiveName = _PrimitiveName;
        this.hasFiller=hasFiller;
    }

    public TbcdStringImpl(int minLength, int maxLength, String _PrimitiveName, String data,Boolean hasFiller) {
        this(minLength, maxLength, _PrimitiveName,hasFiller);
        this.data = data;
    }

    @ASNLength
	public Integer getLength(ASNParser parser) {
    	return getLength(hasFiller, maxLength, data);
	}
	
    public static Integer getLength(Boolean hasFiller,Integer maxLength,String data) {
    	if(hasFiller)
    		return maxLength;
    	else if(data.length()%2==0)
    		return data.length()/2;
    	else
    		return data.length()/2+1;
    }
    
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws ASNParsingException {
		encodeString(buffer, data);
		if(hasFiller) {
        	for (int i = data.length() + 1; i < this.maxLength * 2; i = i + 2) {
        		buffer.writeByte(DIGIT_MASK);
            }
        }		
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors,Integer level) throws ASNParsingComponentException {
		data = decodeString(buffer);
		return false;
	}

    public static String decodeString(ByteBuf buffer) throws ASNParsingComponentException {
        StringBuilder s = new StringBuilder();
        while (buffer.readableBytes()>0) {
            int b = buffer.readByte();

            int digit1 = (b & DIGIT_1_MASK);
            if (digit1 == 15) {
                // this is mask
            } else {
                s.append(decodeNumber(digit1));
            }

            int digit2 = ((b & DIGIT_2_MASK) >> 4);
            if (digit2 == 15) {
                // this is mask
            } else {
                s.append(decodeNumber(digit2));
            }
        }

        return s.toString();
    }

    public static void encodeString(ByteBuf buffer, String data) throws ASNParsingException {
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i = i + 2) {
            char a = chars[i];

            int digit1 = encodeNumber(a);
            int digit2;
            if ((i + 1) == chars.length) {
                // add the filler instead
                digit2 = 15;
            } else {
                char b = chars[i + 1];
                digit2 = encodeNumber(b);
            }

            int digit = (digit2 << 4) | digit1;
            buffer.writeByte(digit);
        }
    }

    protected static int encodeNumber(char c) throws ASNParsingException {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case '*':
                return 10;
            case '#':
                return 11;
            case 'a':
            case 'A':
                return 12;
            case 'b':
            case 'B':
                return 13;
            case 'c':
            case 'C':
                return 14;
            default:
                throw new ASNParsingException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received " + c);

        }
    }

    protected static char decodeNumber(int i) throws ASNParsingComponentException {
        switch (i) {
            case 0:
                return '0';
            case 1:
                return '1';
            case 2:
                return '2';
            case 3:
                return '3';
            case 4:
                return '4';
            case 5:
                return '5';
            case 6:
                return '6';
            case 7:
                return '7';
            case 8:
                return '8';
            case 9:
                return '9';
            case 10:
                return '*';
            case 11:
                return '#';
            case 12:
                return 'a';
            case 13:
                return 'b';
            case 14:
                return 'c';
                // case 15:
                // return 'd';
            default:
                throw new ASNParsingComponentException(
                        "Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received " + i,
                        ASNParsingComponentExceptionReason.MistypedParameter);

        }
    }

    @Override
    public String toString() {
        return _PrimitiveName + " [" + this.data + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
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
        TbcdStringImpl other = (TbcdStringImpl) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(data==null)
			throw new ASNParsingComponentException("data should be set for tbcd string", ASNParsingComponentExceptionReason.MistypedParameter);
		
		Integer length=getLength(hasFiller,maxLength,data);
		if(length<minLength)
			throw new ASNParsingComponentException("data length should not be less then " + (minLength*2-1), ASNParsingComponentExceptionReason.MistypedParameter);
		
		if(length>maxLength)
			throw new ASNParsingComponentException("data length should not be more then " + (maxLength*2), ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}
