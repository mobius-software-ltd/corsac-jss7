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

package org.restcomm.protocols.ss7.map.smstpdu;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.commonapp.api.datacoding.NationalLanguageIdentifier;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.commonapp.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.commonapp.datacoding.Gsm7EncodingStyle;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.smstpdu.DataCodingScheme;
import org.restcomm.protocols.ss7.map.api.smstpdu.Gsm7NationalLanguageIdentifier;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserData;
import org.restcomm.protocols.ss7.map.api.smstpdu.UserDataHeader;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class UserDataImpl implements UserData {
	private static GSMCharset gsm7Charset = new GSMCharset();
    private static Charset ucs2Charset = Charset.forName("UTF-16BE");

    private DataCodingScheme dataCodingScheme;
    private Charset gsm8Charset;
    private ByteBuf encodedData;
    private int encodedUserDataLength;
    private boolean encodedUserDataHeaderIndicator;
    private UserDataHeader decodedUserDataHeader;
    private String decodedMessage;

    private boolean isDecoded;
    private boolean isEncoded;

    public UserDataImpl(ByteBuf encodedData, DataCodingScheme dataCodingScheme, int encodedUserDataLength,
            boolean encodedUserDataHeaderIndicator, Charset gsm8Charset) {
        this.encodedData = encodedData;
        this.encodedUserDataLength = encodedUserDataLength;
        this.encodedUserDataHeaderIndicator = encodedUserDataHeaderIndicator;
        if (dataCodingScheme != null)
            this.dataCodingScheme = dataCodingScheme;
        else
            this.dataCodingScheme = new DataCodingSchemeImpl(0);
        this.gsm8Charset = gsm8Charset;

        this.isEncoded = true;
    }

    public UserDataImpl(String decodedMessage, DataCodingScheme dataCodingScheme, UserDataHeader decodedUserDataHeader,
            Charset gsm8Charset) {
        this.decodedMessage = decodedMessage;
        this.decodedUserDataHeader = decodedUserDataHeader;
        if (dataCodingScheme != null)
            this.dataCodingScheme = dataCodingScheme;
        else
            this.dataCodingScheme = new DataCodingSchemeImpl(0);
        this.gsm8Charset = gsm8Charset;

        this.isDecoded = true;
    }

    public DataCodingScheme getDataCodingScheme() {
        return this.dataCodingScheme;
    }

    public ByteBuf getEncodedData() {
        return this.encodedData;
    }

    public int getEncodedUserDataLength() {
        return encodedUserDataLength;
    }

    public boolean getEncodedUserDataHeaderIndicator() {
        return encodedUserDataHeaderIndicator;
    }

    public UserDataHeader getDecodedUserDataHeader() {
        return decodedUserDataHeader;
    }

    public String getDecodedMessage() {
        return decodedMessage;
    }

    public void encode() throws MAPException {

        if (this.isEncoded)
            return;
        this.isEncoded = true;

        this.encodedData = null;
        this.encodedUserDataLength = 0;
        this.encodedUserDataHeaderIndicator = false;

        if (this.decodedMessage == null)
            this.decodedMessage = "";

        // encoding UserDataHeader if it exists
        ByteBuf buf2 = Unpooled.buffer();
        if (this.decodedUserDataHeader != null) {
            this.decodedUserDataHeader.getEncodedData(buf2);
            if (buf2 != null && buf2.readableBytes() > 0)
                this.encodedUserDataHeaderIndicator = true;
            else
                buf2 = null;
        }

        if (this.dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed message
            throw new MAPException("Error encoding a text in Sms UserData: compressed message is not supported yet");
        } else {
            switch (this.dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    // selecting a Charset for encoding
//                    Charset cSet = gsm7Charset;
//                    Gsm7NationalLanguageIdentifier nationalLanguageLockingShift = null;
//                    Gsm7NationalLanguageIdentifier nationalLanguageSingleShift = null;
//                    NationalLanguageIdentifier nationalLanguageLockingShiftIdentifier = null;
//                    NationalLanguageIdentifier nationalLanguageSingleShiftIdentifier = null;
//                    if (this.decodedUserDataHeader != null) {
//                        nationalLanguageLockingShift = this.decodedUserDataHeader.getNationalLanguageLockingShift();
//                        nationalLanguageSingleShift = this.decodedUserDataHeader.getNationalLanguageSingleShift();
//                        if (nationalLanguageLockingShift != null)
//                            nationalLanguageLockingShiftIdentifier = nationalLanguageLockingShift
//                                    .getNationalLanguageIdentifier();
//                        if (nationalLanguageSingleShift != null)
//                            nationalLanguageSingleShiftIdentifier = nationalLanguageSingleShift.getNationalLanguageIdentifier();
//                    }
//                    if (nationalLanguageLockingShift != null || nationalLanguageSingleShift != null) {
//                        cSet = new GSMCharset("GSM", new String[] {}, nationalLanguageLockingShiftIdentifier,
//                                nationalLanguageSingleShiftIdentifier);
//                    }

                    GSMCharset cSet = obtainGsmCharacterSet(this.decodedUserDataHeader);
                    GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
                    encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_sms_style, buf2));
                    ByteBuf bb = null;
                    try {
                        bb = encoder.encode(this.decodedMessage);
                    } catch (Exception e) {
                        // This can not occur
                    }
                    this.encodedUserDataLength = encoder.getGSMCharsetEncodingData().getTotalSeptetCount();
                    if (bb != null)
                        this.encodedData = bb.readSlice(bb.readableBytes());                        
                    else
                        this.encodedData = Unpooled.EMPTY_BUFFER;                    
                    break;

                case GSM8:
                    if (gsm8Charset != null) {                    	
                        this.encodedData = Unpooled.wrappedBuffer(this.decodedMessage.getBytes(gsm8Charset));
                        if (buf2 != null)
                            this.encodedData = Unpooled.wrappedBuffer(buf2,encodedData);                            
                        
                        this.encodedUserDataLength = this.encodedData.readableBytes();
                    } else {
                        throw new MAPException(
                                "Error encoding a text in Sms UserData: gsm8Charset is not defined for GSM8 dataCodingScheme");
                    }
                    break;

                case UCS2:
                    this.encodedData = Unpooled.wrappedBuffer(this.decodedMessage.getBytes(ucs2Charset));
                    if (buf2 != null)
                        this.encodedData = Unpooled.wrappedBuffer(buf2,encodedData);                            
                    
                    this.encodedUserDataLength = this.encodedData.readableBytes();
                    break;
				default:
					break;
            }
        }
    }

    public void decode() throws MAPException {

        if (this.isDecoded)
            return;
        this.isDecoded = true;

        this.decodedUserDataHeader = null;
        this.decodedMessage = null;

        if (this.encodedData == null)
            throw new MAPException("Error decoding a text from Sms UserData: encodedData field is null");

        int offset = 0;
        if (this.encodedUserDataHeaderIndicator) {
            // decode userDataHeader
            if (this.encodedData.readableBytes() < 1)
                return;
            
            this.encodedData.markReaderIndex();
            offset = (this.encodedData.readByte() & 0xFF) + 1;
            this.encodedData.resetReaderIndex();
            if (offset > this.encodedData.readableBytes())
                return;

            this.encodedData.markReaderIndex();
            this.decodedUserDataHeader = new UserDataHeaderImpl(this.encodedData);
            this.encodedData.resetReaderIndex();            
        }

        if (this.dataCodingScheme.getIsCompressed()) {
            // TODO: implement the case with compressed sms message
            // If this is a signal message - this.decodedMessage can be decoded
            // If thus is a segment of concatenated message this.decodedMessage should stay null and this case should be
            // processed by a special static method            
        } else {
            switch (this.dataCodingScheme.getCharacterSet()) {
                case GSM7:
                    GSMCharset cSet = obtainGsmCharacterSet(this.decodedUserDataHeader);
                    GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
                    if (offset > 0) {
                        int bitOffset = offset * 8;
                        int septetOffset = (bitOffset - 1) / 7 + 1;
                        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_sms_style,
                                this.encodedUserDataLength, septetOffset));
                    } else
                        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_sms_style,
                                this.encodedUserDataLength, 0));
                    ByteBuf bb = Unpooled.wrappedBuffer(this.encodedData);
                    String bf = null;
                    try {
                        bf = decoder.decode(bb);
                    } catch (CharacterCodingException e) {
                        // This can not occur
                    }
                    if (bf != null)
                        this.decodedMessage = bf;
                    else
                        this.decodedMessage = "";
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                        ByteBuf buf = this.encodedData;
                        int len = this.encodedUserDataLength;
                        if (len > buf.readableBytes())
                            len = buf.readableBytes();
                        if (offset > 0) {
                            if (offset > len)
                                buf = Unpooled.EMPTY_BUFFER;
                            else
                            	buf.skipBytes(offset);                            
                        }
                        this.decodedMessage = buf.toString(gsm8Charset);
                    }
                    break;

                case UCS2:
                    ByteBuf buf = this.encodedData;
                    int len = this.encodedUserDataLength;
                    if (len > buf.readableBytes())
                        len = buf.readableBytes();
                    if (offset > 0) {
                        if (offset > len)
                        	 buf = Unpooled.EMPTY_BUFFER;
                        else 
                        	buf.skipBytes(offset);
                    }
                    this.decodedMessage = buf.toString(ucs2Charset);
                    break;
				default:
					break;
            }
        }
    }

    private static GSMCharset obtainGsmCharacterSet(UserDataHeader udh) {
        GSMCharset cSet = gsm7Charset;
        Gsm7NationalLanguageIdentifier nationalLanguageLockingShift = null;
        Gsm7NationalLanguageIdentifier nationalLanguageSingleShift = null;
        NationalLanguageIdentifier nationalLanguageLockingShiftIdentifier = null;
        NationalLanguageIdentifier nationalLanguageSingleShiftIdentifier = null;
        if (udh != null) {
            nationalLanguageLockingShift = udh.getNationalLanguageLockingShift();
            nationalLanguageSingleShift = udh.getNationalLanguageSingleShift();
            if (nationalLanguageLockingShift != null)
                nationalLanguageLockingShiftIdentifier = nationalLanguageLockingShift.getNationalLanguageIdentifier();
            if (nationalLanguageSingleShift != null)
                nationalLanguageSingleShiftIdentifier = nationalLanguageSingleShift.getNationalLanguageIdentifier();
        }
        if (nationalLanguageLockingShift != null || nationalLanguageSingleShift != null) {
            cSet = new GSMCharset(nationalLanguageLockingShiftIdentifier,
                    nationalLanguageSingleShiftIdentifier);
        }
        return cSet;
    }

    public static int checkEncodedDataLengthInChars(String msg, UserDataHeader udh) {
        GSMCharset cSet = obtainGsmCharacterSet(udh);
        return cSet.checkEncodedDataLengthInChars(msg);
    }

    public static String[] sliceString(String data, int charCount, UserDataHeader udh) {
        GSMCharset cSet = obtainGsmCharacterSet(udh);
        return cSet.sliceString(data, charCount);
    }

//    public static UserData[] encodeSplittedMessagesSet(String msg, DataCodingScheme dataCodingScheme,
//            Gsm7NationalLanguageIdentifier nationalLanguageLockingShift,
//            Gsm7NationalLanguageIdentifier nationalLanguageSingleShift, boolean referenceIs16bit,
//            UserDataHeaderElement[] extraUserDataHeader) throws MAPException {
//
//        // TODO: implement the splitMessage tool: splitting a sms message into several messages depending on data coding
//        throw new MAPException("Not yet implemented");
//    }
//
//    public static ConcatenatedMessage decodeSplittedMessagesSet(UserData[] encodedArray, DataCodingScheme dataCodingScheme)
//            throws MAPException {
//
//        // TODO: implement the splitMessage tool: decoding splitted sms messages into a solid message
//        throw new MAPException("Not yet implemented");
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("TP-User-Data [");
        if (this.decodedMessage == null) {
            if (this.encodedData != null)
                sb.append(ASNOctetString.printDataArr(Unpooled.wrappedBuffer(this.encodedData)));
        } else {
            sb.append("Msg:[");
            sb.append(this.decodedMessage);
            sb.append("]");
            if (this.decodedUserDataHeader != null) {
                sb.append("\n");
                sb.append(this.decodedUserDataHeader.toString());
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
