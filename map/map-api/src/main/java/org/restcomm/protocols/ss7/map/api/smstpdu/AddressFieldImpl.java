/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import java.io.IOException;

import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPParsingComponentException;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.map.api.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.map.api.datacoding.Gsm7EncodingStyle;
import org.restcomm.protocols.ss7.map.api.primitives.TbcdString;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class AddressFieldImpl {
	private TypeOfNumber typeOfNumber;
    private NumberingPlanIdentification numberingPlanIdentification;
    private String addressValue;

    private AddressFieldImpl() {
    }

    public AddressFieldImpl(TypeOfNumber typeOfNumber, NumberingPlanIdentification numberingPlanIdentification,
            String addressValue) {
        this.typeOfNumber = typeOfNumber;
        this.numberingPlanIdentification = numberingPlanIdentification;
        this.addressValue = addressValue;
    }

    public static AddressFieldImpl createMessage(ByteBuf buf) throws MAPException {

        if (buf == null)
            throw new MAPException("Error creating AddressField: stream must not be null");

        AddressFieldImpl res = new AddressFieldImpl();

        try {
            // Address-Length
            int addressLength = buf.readByte()& 0x0ff;
            if (addressLength == -1)
                throw new MAPException("Error creating AddressField: Address-Length field not found");
            if (addressLength < 0 || addressLength > 20)
                throw new MAPException(
                        "Error creating AddressField: Address-Length field must be equal from 0 to 20, found: addressLength");

            // Type-of-Address
            int typeOfAddress = buf.readByte() & 0x0ff;
            if (typeOfAddress == -1)
                throw new MAPException("Error creating AddressField: Type-of-Address field not found");
            res.typeOfNumber = TypeOfNumber.getInstance((typeOfAddress & 0x70) >> 4);
            res.numberingPlanIdentification = NumberingPlanIdentification.getInstance(typeOfAddress & 0x0F);

            int addressArrayLength = (addressLength + 1) / 2;

            // Address-Value
            if (res.typeOfNumber == TypeOfNumber.Alphanumeric) {
                GSMCharset cs = new GSMCharset();
                GSMCharsetDecoder decoder = (GSMCharsetDecoder) cs.newDecoder();
                int totalSeptetCount = (addressLength < 14 ? addressArrayLength : addressArrayLength + 1);
                GSMCharsetDecodingData encodingData = new GSMCharsetDecodingData(Gsm7EncodingStyle.bit7_sms_style,
                        totalSeptetCount, 0);
                decoder.setGSMCharsetDecodingData(encodingData);
                if(buf.readableBytes()>addressArrayLength) {
                	res.addressValue = decoder.decode(buf.slice(buf.readerIndex(), addressArrayLength));
                    buf.skipBytes(addressArrayLength);
                } else {
                	res.addressValue = decoder.decode(buf);                    
                }
            } else {
                // Address-Value            	
                res.addressValue = TbcdString.decodeString(buf.slice(buf.readerIndex(), addressArrayLength));
                buf.skipBytes(addressArrayLength);
            }

        } catch (IOException e) {
            throw new MAPException("IOException when creating AddressField: " + e.getMessage(), e);
        } catch (MAPParsingComponentException e) {
            throw new MAPException("MAPParsingComponentException when creating AddressField: " + e.getMessage(), e);
        }

        return res;
    }

    public TypeOfNumber getTypeOfNumber() {
        return this.typeOfNumber;
    }

    public NumberingPlanIdentification getNumberingPlanIdentification() {
        return this.numberingPlanIdentification;
    }

    public String getAddressValue() {
        return this.addressValue;
    }

    public void encodeData(ByteBuf buf) throws MAPException {

        if (typeOfNumber == null || numberingPlanIdentification == null || addressValue == null)
            throw new MAPException(
                    "Error encoding AddressFieldImpl: typeOfNumber, addressValue and numberingPlanIdentification fields must not be null");

        try {
            int addrLen = addressValue.length();
            int tpOfAddr = 0x80 + (this.typeOfNumber.getCode() << 4) + this.numberingPlanIdentification.getCode();

            if (this.typeOfNumber == TypeOfNumber.Alphanumeric) {
                GSMCharset cs = new GSMCharset();
                GSMCharsetEncoder encoder = (GSMCharsetEncoder) cs.newEncoder();
                
                int semiOct = addrLen * 2 - (int) (addrLen / 4);

                buf.writeByte(semiOct);
                buf.writeByte(tpOfAddr);
                encoder.encode(this.addressValue,buf);
                
                // As per 3GPP TS 23.040 (23040-3a[1].pdf)
                // The Address-Length field is an integer representation of the
                // number of useful semi-octets within the Address-Value field,
                // i.e. excludes any semi octet containing only fill bits.

                // TODO Here we have added flag 0xF0 as filler check. This needs
                // to verify for correctness
                // if ((data[dataLength - 1] & 0xF0) == 0x00) {
                // dataLength = (dataLength * 2) - 1;
                // } else {
                // dataLength = dataLength * 2;
                // }
            } else {
                buf.writeByte(addrLen);
                buf.writeByte(tpOfAddr);
                TbcdString.encodeString(buf, addressValue);
            }
        } catch (IOException e) {
            // This can not occur
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("AddressField [");

        if (typeOfNumber != null) {
            sb.append("typeOfNumber=");
            sb.append(typeOfNumber);
        }
        if (numberingPlanIdentification != null) {
            sb.append(", numberingPlanIdentification=");
            sb.append(numberingPlanIdentification);
        }
        if (addressValue != null) {
            sb.append(", addressValue=");
            sb.append(addressValue);
        }
        sb.append("]");

        return sb.toString();
    }
}
