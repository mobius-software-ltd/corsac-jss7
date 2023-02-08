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

package org.restcomm.protocols.ss7.map.primitives;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.Gsm7EncodingStyle;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingGroup;
import org.restcomm.protocols.ss7.map.api.datacoding.CBSDataCodingScheme;
import org.restcomm.protocols.ss7.map.api.primitives.USSDString;
import org.restcomm.protocols.ss7.map.datacoding.CBSDataCodingSchemeImpl;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class USSDStringImpl extends ASNOctetString implements USSDString {
	private CBSDataCodingScheme dataCodingScheme;

    private static Charset ucs2Charset = Charset.forName("UTF-16BE");

    public USSDStringImpl() {
    	super("USSDString",1,160,false);
    	
    }

    public USSDStringImpl(ByteBuf data, CBSDataCodingScheme dataCodingScheme) {
    	super(data,"USSDString",1,160,false);
        if (dataCodingScheme == null)
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);

        this.dataCodingScheme = dataCodingScheme;
    }

    public USSDStringImpl(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) throws MAPException {
    	super(translate(ussdString, dataCodingScheme, gsm8Charset),"USSDString",1,160,false);
    	
    	if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        this.dataCodingScheme = dataCodingScheme;        
    }
    
    public static ByteBuf translate(String ussdString, CBSDataCodingScheme dataCodingScheme, Charset gsm8Charset) throws MAPException {
        if (ussdString == null) {
            ussdString = "";
        }
        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        
        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed message
            throw new MAPException("Error encoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                	GSMCharset cSet = GSMCharset.gsm7CharsetDefault;
                    if(dataCodingScheme.getNationalLanguageShiftTable()!=null)
                    {
                    	switch(dataCodingScheme.getNationalLanguageShiftTable())
                    	{
							case Arabic:
								cSet=GSMCharset.gsm7CharsetUrdu;
								break;
							case Turkish:
								cSet=GSMCharset.gsm7CharsetTurkish;
								break;
							case Portuguese:
								cSet=GSMCharset.gsm7CharsetPortugese;
								break;
							case Spanish:
								cSet=GSMCharset.gsm7CharsetSpanish;
								break;
							default:
								break;                    		
                    	}
                    }
                    GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
                    encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_ussd_style, null));
                    ByteBuf bb = null;
                    try {
                        bb = encoder.encode(ussdString);
                    } catch (Exception e) {
                        // This can not occur
                    }
                    
                    return bb;                    
                case GSM8:
                    if (gsm8Charset != null) {
                    	return Unpooled.wrappedBuffer(ussdString.getBytes(gsm8Charset));                    	
                    } else {
                        throw new MAPException(
                                "Error encoding a text in USSDStringImpl: gsm8Charset is not defined for GSM8 dataCodingScheme");
                    }
                case UCS2:
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        if (ussdString.length() < 1)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 2)
                            ussdString = ussdString + " ";
                        if (ussdString.length() < 3)
                            ussdString = ussdString + "\n";
                        cSet = GSMCharset.gsm7CharsetDefault;
                        encoder = (GSMCharsetEncoder) cSet.newEncoder();
                        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_ussd_style, null));
                        bb = null;
                        try {
                            String sb = ussdString.substring(0, 3);
                            bb = encoder.encode(sb);
                        } catch (Exception e) {
                            // This can not occur
                        }
                        ByteBuf buf1=null;
                        if (bb != null) {
                        	buf1=Unpooled.wrappedBuffer(bb);
                        	buf1.resetReaderIndex();
                        }
                        
                        String sb2 = ussdString.substring(3);
                        ByteBuf buf2=Unpooled.wrappedBuffer(sb2.getBytes(ucs2Charset));
                        buf2.resetReaderIndex();
                        
                        if(buf1==null)
                        	return buf2;
                        else
                        	return Unpooled.wrappedBuffer(buf1,buf2);                        
                    } else {
                        return Unpooled.wrappedBuffer(ussdString.getBytes(ucs2Charset));
                    }
				default:
					break;
            }
        }
        
        return null;
    }

    public void setDataCoding(CBSDataCodingSchemeImpl dataCodingScheme) {
    	this.dataCodingScheme=dataCodingScheme;
    }
    
    public String getString(Charset gsm8Charset) throws MAPException {

        String res = "";

        if (dataCodingScheme == null) {
            dataCodingScheme = new CBSDataCodingSchemeImpl(15);
        }
        
        ByteBuf value=getValue();
        if (value == null) {
            throw new MAPException("Error decoding a text in USSDStringImpl: encoded data can not be null");
        }

        if (dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed sms message
            throw new MAPException("Error decoding a text in USSDStringImpl: compressed message is not supported yet");
        } else {

            switch (dataCodingScheme.getCharacterSet()) {
                case GSM7:
                	GSMCharset cSet = GSMCharset.gsm7CharsetDefault;
                    if(dataCodingScheme.getNationalLanguageShiftTable()!=null)
                    {
                    	switch(dataCodingScheme.getNationalLanguageShiftTable())
                    	{
							case Arabic:
								cSet=GSMCharset.gsm7CharsetUrdu;
								break;
							case Turkish:
								cSet=GSMCharset.gsm7CharsetTurkish;
								break;
							case Portuguese:
								cSet=GSMCharset.gsm7CharsetPortugese;
								break;
							case Spanish:
								cSet=GSMCharset.gsm7CharsetSpanish;
								break;
							default:
								break;                    		
                    	}
                    }
                    GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
                    decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_ussd_style,
                            Integer.MAX_VALUE, 0));
                    String bf = null;
                    try {
                        bf = decoder.decode(value);
                    } catch (CharacterCodingException e) {
                        // This can not occur
                    }
                    if (bf != null)
                        res = bf;
                    break;

                case GSM8:
                    if (gsm8Charset != null)                    	
                        res = value.toString(gsm8Charset);
                    break;

                case UCS2:
                    String pref = "";
                    if (dataCodingScheme.getDataCodingGroup() == CBSDataCodingGroup.GeneralWithLanguageIndication) {
                        cSet = GSMCharset.gsm7CharsetDefault;
                        decoder = (GSMCharsetDecoder) cSet.newDecoder();
                        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_ussd_style,
                                Integer.MAX_VALUE, 0));
                        ByteBuf buf2;
                        if (value.readableBytes() < 3)
                            buf2 = value;
                        else
                        	buf2 = value.readSlice(3);
                        
                        bf = null;
                        try {
                            bf = decoder.decode(buf2);
                        } catch (CharacterCodingException e) {
                            // This can not occur
                        }
                        if (bf != null)
                            pref = bf;

                        res = pref + value.toString(ucs2Charset);                          		
                    } else {
                    	res = value.toString(ucs2Charset);                    			
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
