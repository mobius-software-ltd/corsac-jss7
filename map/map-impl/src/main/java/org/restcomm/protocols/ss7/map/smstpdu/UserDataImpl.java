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
import org.restcomm.protocols.ss7.map.api.smstpdu.CharacterSet;
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
    private int userDataLength;
    private boolean encodedUserDataHeaderIndicator;
    private UserDataHeader decodedUserDataHeader;
    private ByteBuf messageWithSkipBits;
    private String decodedMessage;

    private boolean isDecoded;
    private boolean isEncoded;

    public UserDataImpl(ByteBuf encodedData, DataCodingScheme dataCodingScheme, int userDataLength,
            boolean encodedUserDataHeaderIndicator, Charset gsm8Charset) {
        this.encodedData = encodedData;
        this.userDataLength = userDataLength;
        this.encodedUserDataHeaderIndicator = encodedUserDataHeaderIndicator;
        if (dataCodingScheme != null)
            this.dataCodingScheme = dataCodingScheme;
        else
            this.dataCodingScheme = new DataCodingSchemeImpl(0);
        this.gsm8Charset = gsm8Charset;

        this.isEncoded = true;
    }

    public UserDataImpl(ByteBuf messageWithSkipBits, DataCodingScheme dataCodingScheme, UserDataHeader decodedUserDataHeader,
            Charset gsm8Charset) {
        this.messageWithSkipBits = messageWithSkipBits;
        this.decodedUserDataHeader = decodedUserDataHeader;
        if (dataCodingScheme != null)
            this.dataCodingScheme = dataCodingScheme;
        else
            this.dataCodingScheme = new DataCodingSchemeImpl(0);
        this.gsm8Charset = gsm8Charset;
        
        this.isDecoded = true;
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

        encodeMessage();  
        this.isDecoded = true;
    }

    public DataCodingScheme getDataCodingScheme() {
        return this.dataCodingScheme;
    }

    public ByteBuf getEncodedData() {
        return this.encodedData;
    }

    public int getUserDataLength() {
        return userDataLength;
    }

    public boolean getEncodedUserDataHeaderIndicator() {
        return encodedUserDataHeaderIndicator;
    }

    public UserDataHeader getDecodedUserDataHeader() {
        return decodedUserDataHeader;
    }

    public ByteBuf getMessageWithSkipBits() {
    	return messageWithSkipBits;
    }
    
    public String getDecodedMessage() {
    	if(decodedMessage==null && messageWithSkipBits!=null)
    		decodeMessage();
    	
        return decodedMessage;
    }

    private void encodeMessage() {
    	if(messageWithSkipBits!=null)
    		return;
    	
    	if (!this.dataCodingScheme.getIsCompressed()) {            
            switch (this.dataCodingScheme.getCharacterSet()) {
            	case GSM7:
            		Integer udhLength=null;
            		if(decodedUserDataHeader!=null)
            			udhLength=decodedUserDataHeader.getLength();
            		
                    GSMCharset cSet = obtainGsmCharacterSet(this.decodedUserDataHeader);
                    GSMCharsetEncoder encoder = (GSMCharsetEncoder) cSet.newEncoder();
                    encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit7_sms_style, udhLength));
                    try {
                    	messageWithSkipBits = encoder.encode(this.decodedMessage);
                    } catch (Exception e) {
                        // This can not occur
                    }
                    
                    if (messageWithSkipBits == null)
                    	messageWithSkipBits = Unpooled.EMPTY_BUFFER;   
            		this.userDataLength = encoder.getGSMCharsetEncodingData().getTotalSeptetCount();
                    break;
            	case GSM8:
            		messageWithSkipBits = Unpooled.wrappedBuffer(this.decodedMessage.getBytes(gsm8Charset));
            		break;
            	case UCS2:
            		messageWithSkipBits = Unpooled.wrappedBuffer(this.decodedMessage.getBytes(ucs2Charset));
            		break;
            	default:
            		messageWithSkipBits = Unpooled.EMPTY_BUFFER;
            		break;
            }
    	}
    }   
    
    private void decodeMessage() {
    	switch (this.dataCodingScheme.getCharacterSet()) {
	        case GSM7:
	            GSMCharset cSet = obtainGsmCharacterSet(this.decodedUserDataHeader);
	            GSMCharsetDecoder decoder = (GSMCharsetDecoder) cSet.newDecoder();
	            
	            int septetOffset = 0;
	            int bitPos=0;
	            if(decodedUserDataHeader!=null)
	            {
	            	bitPos = decodedUserDataHeader.getLength() * 8;
	            	bitPos = 7- (bitPos - ((int)(bitPos/7))*7);
	            }
	            
	            decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_sms_style, this.userDataLength - septetOffset, bitPos)); 
	            String bf = null;
	            try {
	                bf = decoder.decode(Unpooled.wrappedBuffer(this.messageWithSkipBits));
	            } catch (CharacterCodingException e) {
	                // This can not occur
	            }
	            if (bf != null) {
	                this.decodedMessage = bf;
	                if(this.decodedMessage.endsWith("@"))
	                	this.decodedMessage=this.decodedMessage.substring(0,this.decodedMessage.length()-1);
	            }
	            else
	                this.decodedMessage = "";
	            
	            break;
	        case GSM8:
	            if (gsm8Charset != null)
	                this.decodedMessage = messageWithSkipBits.toString(gsm8Charset);
	            break;
	        case UCS2:
	            this.decodedMessage = messageWithSkipBits.toString(ucs2Charset);
	            break;
			default:
				break;
	    }
    }
    
    public void encode() throws MAPException {

        if (this.isEncoded)
            return;
        this.isEncoded = true;

        this.encodedData = null;
        this.encodedUserDataHeaderIndicator = false;

        if (this.messageWithSkipBits == null)
            this.messageWithSkipBits = Unpooled.EMPTY_BUFFER;

        // encoding UserDataHeader if it exists
        ByteBuf buf2 = Unpooled.buffer();
        if (this.decodedUserDataHeader != null) {
            this.decodedUserDataHeader.getEncodedData(buf2);
            if (buf2 != null && buf2.readableBytes() > 0)
                this.encodedUserDataHeaderIndicator = true;
            else
                buf2 = null;
        }
        else
        	buf2 = null;
   
        if (buf2 != null)
            this.encodedData = Unpooled.wrappedBuffer(buf2,messageWithSkipBits);                            
        else
        	this.encodedData = messageWithSkipBits;
        
        this.userDataLength = this.encodedData.readableBytes();
        if(this.dataCodingScheme.getCharacterSet()==CharacterSet.GSM7) {
        	this.userDataLength = GSMCharset.octetsToSeptets(this.userDataLength);
        	
        	if(this.userDataLength!=0 && (this.userDataLength%8)==0)
        	{
	        	byte lastByte = this.encodedData.getByte(this.encodedData.readableBytes()-1);
	        	//we have 7 empty bits in the end
	        	if((lastByte & 0xFE)==0)
	        		this.userDataLength--;
        	}
        	else
        		this.userDataLength--;
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

            this.decodedUserDataHeader = new UserDataHeaderImpl(this.encodedData);                       
        }

        this.messageWithSkipBits=encodedData.readSlice(encodedData.readableBytes());
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
                sb.append(ASNOctetString.printDataArr(this.encodedData.slice()));
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
