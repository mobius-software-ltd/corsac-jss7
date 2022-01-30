/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.protocols.ss7.commonapp.primitives;

import java.util.concurrent.ConcurrentHashMap;

import org.restcomm.protocols.ss7.commonapp.api.APPException;
import org.restcomm.protocols.ss7.commonapp.api.APPParsingComponentException;
import org.restcomm.protocols.ss7.commonapp.api.APPParsingComponentExceptionReason;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.ASNParser;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNDecode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNEncode;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNLength;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public class SingleTbcdStringImpl {
	protected static int DIGIT_1_MASK = 0x0F;
    protected static int DIGIT_MASK = 0x0F;

    protected String data;
    protected String _PrimitiveName;
    
    public SingleTbcdStringImpl() {
    	
    }
    
    public SingleTbcdStringImpl(String _PrimitiveName) {
        this._PrimitiveName = _PrimitiveName;
    }

    public SingleTbcdStringImpl(String _PrimitiveName, String data) {
    	this._PrimitiveName = _PrimitiveName;
        this.data = data;
    }

    @ASNLength
	public Integer getLength(ASNParser parser) {
    	return data.length();
	}
    
	@ASNEncode
	public void encode(ASNParser parser,ByteBuf buffer) throws APPException {
		encodeString(buffer, data);				
	}
	
	@ASNDecode
	public Boolean decode(ASNParser parser,Object parent,ByteBuf buffer,ConcurrentHashMap<Integer,Object> mappedData,Boolean skipErrors) throws APPParsingComponentException {
		data = decodeString(buffer);
		return false;
	}

	public String getData() {
		return data;
	}
	
    public static String decodeString(ByteBuf buffer) throws APPParsingComponentException {
        StringBuilder s = new StringBuilder();
        while (buffer.readableBytes()>0) {
            int b = buffer.readByte();

            int digit = (b & DIGIT_1_MASK);
            s.append(decodeNumber(digit));            
        }

        return s.toString();
    }

    public static void encodeString(ByteBuf buffer, String data) throws APPException {
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char a = chars[i];
            int digit = encodeNumber(a) & DIGIT_1_MASK;
            buffer.writeByte(digit);
        }
    }

    protected static int encodeNumber(char c) throws APPException {
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
                throw new APPException(
                        "char should be between 0 - 9, *, #, a, b, c for Telephony Binary Coded Decimal String. Received " + c);

        }
    }

    protected static char decodeNumber(int i) throws APPParsingComponentException {
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
                throw new APPParsingComponentException(
                        "Integer should be between 0 - 15 for Telephony Binary Coded Decimal String. Received " + i,
                        APPParsingComponentExceptionReason.MistypedParameter);

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
        SingleTbcdStringImpl other = (SingleTbcdStringImpl) obj;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }
}
