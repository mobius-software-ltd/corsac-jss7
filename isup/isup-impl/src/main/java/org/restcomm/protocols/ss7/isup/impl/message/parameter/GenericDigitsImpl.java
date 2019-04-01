/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.GenericDigits;
import org.restcomm.protocols.ss7.isup.util.BcdHelper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.xml.bind.DatatypeConverter;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Start time:12:24:47 2009-03-31<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:grzegorz.figiel@pro-ids.com"> Grzegorz Figiel </a>
 */
public class GenericDigitsImpl extends AbstractISUPParameter implements GenericDigits {
	private static final Charset asciiCharset = Charset.forName("ASCII");

    private int encodingScheme;
    private int typeOfDigits;
    private ByteBuf digits;

    public GenericDigitsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public GenericDigitsImpl(int encodingScheme, int typeOfDigits, ByteBuf digits) {
        super();
        this.encodingScheme = encodingScheme;
        this.typeOfDigits = typeOfDigits;
        this.setEncodedDigits(digits);
    }

    public GenericDigitsImpl(int encodingScheme, int typeOfDigits, String digits) throws UnsupportedEncodingException {
        super();
        this.typeOfDigits = typeOfDigits;
        setDecodedDigits(encodingScheme, digits);
    }

    public GenericDigitsImpl() {
        super();

    }

    public String getDecodedDigits() throws UnsupportedEncodingException {
    	ByteBuf buffer=getEncodedDigits();
    	byte[] data=new byte[buffer.readableBytes()];
    	buffer.readBytes(data);
    	
        switch (encodingScheme) {
            case GenericDigits._ENCODING_SCHEME_BCD_EVEN:
            case GenericDigits._ENCODING_SCHEME_BCD_ODD:
                return BcdHelper.bcdDecodeToHexString(encodingScheme, data);
            case GenericDigits._ENCODING_SCHEME_IA5:
                return new String(data, asciiCharset);
            default:
                //TODO: add other encoding schemas support
                throw new UnsupportedEncodingException("Specified GenericDigits encoding: " + encodingScheme + " is unsupported");
        }
    }

    public void setDecodedDigits(int encodingScheme, String digits) throws UnsupportedEncodingException {
        if (digits == null || digits.length() < 1) {
            throw new IllegalArgumentException("Digits must not be null or zero length");
        }
        switch (encodingScheme) {
            case GenericDigits._ENCODING_SCHEME_BCD_EVEN:
            case GenericDigits._ENCODING_SCHEME_BCD_ODD:
                if ((digits.length() % 2) == 0) {
                    if (encodingScheme == GenericDigits._ENCODING_SCHEME_BCD_ODD)
                        throw new UnsupportedEncodingException("SCHEME_BCD_ODD is possible only for odd digits count");
                } else {
                    if (encodingScheme == GenericDigits._ENCODING_SCHEME_BCD_EVEN)
                        throw new UnsupportedEncodingException("SCHEME_BCD_EVEN is possible only for odd digits count");
                }
                this.encodingScheme = encodingScheme;
                this.setEncodedDigits(Unpooled.wrappedBuffer(BcdHelper.encodeHexStringToBCD(digits)));
                break;
            case GenericDigits._ENCODING_SCHEME_IA5:
                this.encodingScheme = encodingScheme;
                this.setEncodedDigits(Unpooled.wrappedBuffer(digits.getBytes(asciiCharset)));
                break;
            default:
                //TODO: add other encoding schemas support
                throw new UnsupportedEncodingException("Specified GenericDigits encoding: " + encodingScheme + " is unsupported");
        }
    }

    public void decode(ByteBuf buffer) throws ParameterException {
        if (buffer == null || buffer.readableBytes() < 2) {
            throw new ParameterException("byte[] must not be null or has size less than 2");
        }
        
        byte b=buffer.readByte();
        this.typeOfDigits = b & 0x1F;
        this.encodingScheme = (b >> 5) & 0x07;
        this.digits = buffer.slice(buffer.readerIndex(),buffer.readableBytes());
    }

    public void encode(ByteBuf buffer) throws ParameterException {
    	byte b=0;
        b |= this.typeOfDigits & 0x1F;
        b |= ((this.encodingScheme & 0x07) << 5);
        buffer.writeByte(b);
        buffer.writeBytes(getEncodedDigits());
    }

    public int getEncodingScheme() {
        return encodingScheme;
    }

    public void setEncodingScheme(int encodingScheme) {
        this.encodingScheme = encodingScheme;
    }

    public int getTypeOfDigits() {
        return typeOfDigits;
    }

    public void setTypeOfDigits(int typeOfDigits) {
        this.typeOfDigits = typeOfDigits;
    }

    public ByteBuf getEncodedDigits() {
    	if(digits==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(digits);
    }

    public void setEncodedDigits(ByteBuf digits) {
        if (digits == null)
            throw new IllegalArgumentException("Digits must not be null");
        this.digits = digits;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GenericDigits [encodingScheme=");
        sb.append(encodingScheme);
        sb.append(", typeOfDigits=");
        sb.append(typeOfDigits);
        if (digits != null) {
        	ByteBuf buffer=getEncodedDigits();
        	byte[] data=new byte[buffer.readableBytes()];
        	buffer.readBytes(data);
        	
            sb.append(", encodedDigits=[");
            sb.append(DatatypeConverter.printHexBinary(data));
            sb.append("]");

            try {
                String s = getDecodedDigits();
                sb.append(", decodedDigits=[");
                sb.append(s);
                sb.append("]");
            } catch (Exception e) {
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
