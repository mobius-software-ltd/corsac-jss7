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

package org.restcomm.protocols.ss7.map.api.smstpdu;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.Gsm7EncodingStyle;
import org.restcomm.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class UserDataImpl {
	private static GSMCharset gsm7Charset = new GSMCharset();
    private static Charset ucs2Charset = Charset.forName("UTF-16BE");

    private DataCodingSchemeImpl dataCodingScheme;
    private Charset gsm8Charset;
    private byte[] encodedData;
    private int encodedUserDataLength;
    private boolean encodedUserDataHeaderIndicator;
    private UserDataHeaderImpl decodedUserDataHeader;
    private String decodedMessage;

    private boolean isDecoded;
    private boolean isEncoded;

    public UserDataImpl(byte[] encodedData, DataCodingSchemeImpl dataCodingScheme, int encodedUserDataLength,
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

    public UserDataImpl(String decodedMessage, DataCodingSchemeImpl dataCodingScheme, UserDataHeaderImpl decodedUserDataHeader,
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

    public DataCodingSchemeImpl getDataCodingScheme() {
        return this.dataCodingScheme;
    }

    public byte[] getEncodedData() {
        return this.encodedData;
    }

    public int getEncodedUserDataLength() {
        return encodedUserDataLength;
    }

    public boolean getEncodedUserDataHeaderIndicator() {
        return encodedUserDataHeaderIndicator;
    }

    public UserDataHeaderImpl getDecodedUserDataHeader() {
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
                    if (bb != null) {
                        this.encodedData = new byte[bb.readableBytes()];
                        bb.readBytes(this.encodedData);
                    } else {
                        this.encodedData = new byte[0];
                    }
                    break;

                case GSM8:
                    if (gsm8Charset != null) {
                        ByteBuffer javaBuffer = gsm8Charset.encode(this.decodedMessage);
                        this.encodedData = new byte[javaBuffer.limit()];
                        javaBuffer.get(this.encodedData);
                        if (buf2 != null) {
                            byte[] tempBuf = this.encodedData;
                            int buf2Length=buf2.readableBytes();
                            this.encodedData = new byte[buf2Length + tempBuf.length];
                            buf2.readBytes(this.encodedData);
                            System.arraycopy(tempBuf, 0, this.encodedData, buf2Length, tempBuf.length);
                        }
                        this.encodedUserDataLength = this.encodedData.length;
                    } else {
                        throw new MAPException(
                                "Error encoding a text in Sms UserData: gsm8Charset is not defined for GSM8 dataCodingScheme");
                    }
                    break;

                case UCS2:
                    ByteBuffer javaBuffer = ucs2Charset.encode(this.decodedMessage);
                    this.encodedData = new byte[javaBuffer.limit()];
                    javaBuffer.get(this.encodedData);
                    if (buf2 != null) {
                        byte[] tempBuf = this.encodedData;
                        int buf2Length=buf2.readableBytes();
                        this.encodedData = new byte[buf2Length + tempBuf.length];
                        buf2.readBytes(this.encodedData);
                        System.arraycopy(tempBuf, 0, this.encodedData, buf2Length, tempBuf.length);
                    }
                    this.encodedUserDataLength = this.encodedData.length;
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
            if (this.encodedData.length < 1)
                return;
            offset = (this.encodedData[0] & 0xFF) + 1;
            if (offset > this.encodedData.length)
                return;

            this.decodedUserDataHeader = new UserDataHeaderImpl(this.encodedData);
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
                        byte[] buf = this.encodedData;
                        int len = this.encodedUserDataLength;
                        if (len > buf.length)
                            len = buf.length;
                        if (offset > 0) {
                            if (offset > len)
                                buf = new byte[0];
                            else {
                                buf = new byte[len - offset];
                                System.arraycopy(this.encodedData, offset, buf, 0, len - offset);
                            }
                        }
                        ByteBuffer javaBuffer = ByteBuffer.wrap(buf);
                        this.decodedMessage = gsm8Charset.decode(javaBuffer).toString();
                    }
                    break;

                case UCS2:
                    byte[] buf = this.encodedData;
                    int len = this.encodedUserDataLength;
                    if (len > buf.length)
                        len = buf.length;
                    if (offset > 0) {
                        if (offset > len)
                            buf = new byte[0];
                        else {
                            buf = new byte[len - offset];
                            System.arraycopy(this.encodedData, offset, buf, 0, len - offset);
                        }
                    }
                    ByteBuffer javaBuffer = ByteBuffer.wrap(buf);
                    this.decodedMessage = ucs2Charset.decode(javaBuffer).toString();
                    break;
				default:
					break;
            }
        }
    }

    private static GSMCharset obtainGsmCharacterSet(UserDataHeaderImpl udh) {
        GSMCharset cSet = gsm7Charset;
        Gsm7NationalLanguageIdentifierImpl nationalLanguageLockingShift = null;
        Gsm7NationalLanguageIdentifierImpl nationalLanguageSingleShift = null;
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

    public static int checkEncodedDataLengthInChars(String msg, UserDataHeaderImpl udh) {
        GSMCharset cSet = obtainGsmCharacterSet(udh);
        return cSet.checkEncodedDataLengthInChars(msg);
    }

    public static String[] sliceString(String data, int charCount, UserDataHeaderImpl udh) {
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
                sb.append(printDataArr(this.encodedData));
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

    private String printDataArr(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int b : arr) {
            sb.append(b);
            sb.append(", ");
        }

        return sb.toString();
    }
}
