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

package org.restcomm.protocols.ss7.map.api.primitives;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingSchemeImpl;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSNationalLanguage;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.Gsm7EncodingStyle;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class USSDStringImpl extends ASNOctetString {
	private CBSDataCodingScheme dataCodingScheme;

    private static GSMCharset gsm7Charset = new GSMCharset();
    private static GSMCharset gsm7Charset_Urdu = new GSMCharset(GSMCharset.urduMap,
            GSMCharset.urduExtentionMap);
    private static Charset ucs2Charset = Charset.forName("UTF-16BE");

    public USSDStringImpl(CBSDataCodingScheme dataCodingScheme) {
        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;
    }

    public USSDStringImpl(byte[] data, CBSDataCodingScheme dataCodingScheme) {
    	setValue(Unpooled.wrappedBuffer(data));
        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;
    }

    public USSDStringImpl(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) throws MAPException {
        if (ussdString == null) {
            ussdString = "";
        }
        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;

        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed message
            throw new MAPException("Error encoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    GSMCharset cSet = gsm7Charset;
                    if (dataCodingScheme.getNationalLanguageShiftTable() == CBSNationalLanguage.Arabic) {
                        cSet = gsm7Charset_Urdu;
                    }
                    GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
                    encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_ussd_style, null));
                    ByteBuf bb = null;
                    try {
                        bb = encoder.encode(ussdString);
                    } catch (Exception e) {
                        // This can not occur
                    }
                    
                    setValue(bb);
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                    	ByteBuffer javaBuffer = gsm8Charset.encode(ussdString);
                    	byte[] data = new byte[javaBuffer.limit()];
                    	javaBuffer.get(data);
                    	setValue(Unpooled.wrappedBuffer(data));                    	
                    } else {
                        throw new MAPException(
                                "Error encoding a text in USSDStringImpl: gsm8Charset is not defined for GSM8 dataCodingScheme");
                    }
                    break;

                case UCS2:
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        if (ussdString.length() < 1)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 2)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 3)
                            ussdString = ussdString + "\n";
                        cSet = gsm7Charset;
                        encoder = (GSMCharsetEncoder) cSet.newEncoder();
                        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_ussd_style, null));
                        bb = null;
                        try {
                            String sb = ussdString.substring(0, 3);
                            bb = encoder.encode(sb);
                        } catch (Exception e) {
                            // This can not occur
                        }
                        byte[] buf1;
                        if (bb != null) {
                            buf1 = new byte[bb.readableBytes()];
                            bb.readBytes(buf1);
                        } else
                            buf1 = new byte[0];

                        String sb2 = ussdString.substring(3);
                        ByteBuffer javaBuffer = ucs2Charset.encode(sb2);
                        byte[] data = new byte[buf1.length + javaBuffer.limit()];
                        System.arraycopy(buf1, 0, data, 0, buf1.length);
                        javaBuffer.get(data, buf1.length, data.length - buf1.length);
                        setValue(Unpooled.wrappedBuffer(data));
                    } else {
                        ByteBuffer javaBuffer = ucs2Charset.encode(ussdString);
                        byte[] data = new byte[javaBuffer.limit()];
                        javaBuffer.get(data);
                        setValue(Unpooled.wrappedBuffer(data));
                    }
                    break;
				default:
					break;
            }
        }
    }

    public byte[] getEncodedString() {
    	ByteBuf buf=getValue();
    	byte[] data=new byte[buf.readableBytes()];
    	buf.readBytes(data);
        return data;
    }

    public String getString(Charset gsm8Charset) throws MAPException {

        String res = "";

        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        
        byte[] data=getEncodedString();
        if (data == null) {
            throw new MAPException("Error decoding a text in USSDStringImpl: encoded data can not be null");
        }

        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed sms message
            throw new MAPException("Error decoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    GSMCharset cSet = gsm7Charset;
                    if (dataCodingScheme.getNationalLanguageShiftTable() == CBSNationalLanguage.Arabic) {
                        cSet = gsm7Charset_Urdu;
                    }
                    GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
                    decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_ussd_style,
                            Integer.MAX_VALUE, 0));
                    ByteBuf bb = Unpooled.wrappedBuffer(data);
                    String bf = null;
                    try {
                        bf = decoder.decode(bb);
                    } catch (CharacterCodingException e) {
                        // This can not occur
                    }
                    if (bf != null)
                        res = bf;
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                        byte[] buf = data;
                        ByteBuffer javaBuffer = ByteBuffer.wrap(buf);
                        CharBuffer cb = gsm8Charset.decode(javaBuffer);
                        res = cb.toString();
                    }
                    break;

                case UCS2:
                    String pref = "";
                    byte[] buf = data;
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        cSet = gsm7Charset;
                        decoder = (GSMCharsetDecoder) cSet.newDecoder();
                        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_ussd_style,
                                Integer.MAX_VALUE, 0));
                        byte[] buf2 = new byte[3];
                        if (data.length < 3)
                            buf2 = new byte[data.length];
                        System.arraycopy(data, 0, buf2, 0, buf2.length);
                        bb = Unpooled.wrappedBuffer(buf2);
                        bf = null;
                        try {
                            bf = decoder.decode(bb);
                        } catch (CharacterCodingException e) {
                            // This can not occur
                        }
                        if (bf != null)
                            pref = bf;

                        if (data.length <= 3) {
                            buf = new byte[0];
                        } else {
                            buf = new byte[data.length - 3];
                            System.arraycopy(data, 3, buf, 0, buf.length);
                        }

                        ByteBuffer javaBuffer = ByteBuffer.wrap(buf);
                        CharBuffer cb = ucs2Charset.decode(javaBuffer);
                        res = pref + cb.toString();
                    } else {
                    	ByteBuffer javaBuffer = ByteBuffer.wrap(buf);
                        CharBuffer cb = ucs2Charset.decode(javaBuffer);
                        res = cb.toString();
                    }
                    break;
				default:
					break;
            }
        }

        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("USSD String");
        sb.append(" [");

        try {
            String s1 = this.getString(null);
            sb.append(s1);
        } catch (MAPException e) {
        }

        if (this.dataCodingScheme != null) {
            sb.append(", dcs=");
            sb.append(dataCodingScheme);
        }

        sb.append("]");

        return sb.toString();
    }
}
