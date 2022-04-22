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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.Number;

import io.netty.buffer.ByteBuf;

/**
 * Start time:18:44:10 2009-03-27<br>
 * Project: restcomm-isup-stack<br>
 * This class is super classes for all message parameters with form of:
 *
 * <pre>
 *    ________
 *   | Header |
 *   |________|
 *   |  Body  |
 *   |________|
 *   | Digits |
 *   |________|
 *
 *
 * </pre>
 *
 * Where Header has 1+ bytes, body 1+ byte, and digits part contains pairs of digits encoded from right to left from most
 * significant digits in number. Examples parameters are(from Q763): 3.16 Connected Number,3.9 Called party number, 3.10 Calling
 * party number, 3.26 Generic number, 3.30 Location number. Also (3.39,) Implemetnation class must fill tag variable with proper
 * information. Length part of information component is computed from length of this element. See section (B1, B2 and B3 of
 * Q.763)
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public abstract class AbstractNumber extends AbstractISUPParameter implements Number {
	protected Logger logger = LogManager.getLogger(this.getClass());
    /**
     * Holds odd flag, it can have either value: 10000000(x80) or 00000000. For each it takes value 1 and 0;
     */
    protected int oddFlag;

    /**
     * indicates odd flag value (0x80) as integer (1). This is achieved when odd flag in parameter is moved to the right by
     * seven possitions ( >> 7)
     */
    public static final int _FLAG_ODD = 1;

    /**
     * Holds digits(in specs: "signal"). digits[0] holds most siginificant digit. Also length of this table contains information
     * about Odd/even flag. However there is distinct flag used in process of decoding from buffer. This is becuse in case of
     * decoding we dont know if last digit is filler or digit.
     */
    protected String address;

    public AbstractNumber() {
        super();

    }

    public AbstractNumber(ByteBuf buffer) throws ParameterException {
        super();

        this.decode(buffer);
    }

    public AbstractNumber(String address) {
        super();
        this.setAddress(address);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding header");
        }

        decodeHeader(b);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding header");
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding body");
        }
        decodeBody(b);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding body");
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits");
        }
        decodeDigits(b);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits");
        }        
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header");
        }
        encodeHeader(buffer);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding header");
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body");
        }
        encodeBody(buffer);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding body");
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits");
        }
        encodeDigits(buffer);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits");
        }        
    }    

    /**
     * This method is used in constructor that takes ByteBuf as parameter. Decodes header part (its 1 or
     * 2 bytes usually.) Default implemetnation decodes header of one byte - where most significant bit is O/E indicator and
     * bits 7-1 are NAI. This method should be over
     *
     * @param bis
     * @return - number of bytes reads
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public void decodeHeader(ByteBuf buffer) throws ParameterException {
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = buffer.readByte() & 0xff;
        this.oddFlag = (b & 0x80) >> 7;
    }

    /**
     * This method is used in constructor that takes ByteBuf as parameter. Decodes body part (its 1 byte
     * usually.) However in different "numbers" it has different meaning. Each subclass should provide implementation
     *
     * @param bis
     * @return - number of bytes reads throws IllegalArgumentException - thrown if read error is encountered.
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public abstract void decodeBody(ByteBuf buffer) throws ParameterException;

    /**
     * This method is used in constructor that takes ByteBuf as parameter. Decodes digits part.
     *
     * @param bis
     * @return - number of bytes reads
     * @throws IllegalArgumentException - thrown if read error is encountered.
     */
    public void decodeDigits(ByteBuf buffer) throws ParameterException {
        if(skipDigits()){
            return;
        }
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }

        // FIXME: we could spare time by passing length arg - or getting it from
        try {
            address = "";
            int b = 0;
            while (buffer.readableBytes() - 1 > 0) {
                b = (byte) buffer.readByte();

                int d1 = b & 0x0f;
                int d2 = (b & 0xf0) >> 4;

                address += Integer.toHexString(d1) + Integer.toHexString(d2);

            }

            b = buffer.readByte() & 0xff;
            address += Integer.toHexString((b & 0x0f));

            if (oddFlag != _FLAG_ODD) {
                address += Integer.toHexString((b & 0xf0) >> 4);
            }
            this.setAddress(address);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    /**
     * Decodes digits part. It reads exactly octetsCount number of octets.
     *
     * @param bis
     * @param octetsCount - number iof octets to read from ByteBuf
     * @return
     * @throws IllegalArgumentException
     */
    public void decodeDigits(ByteBuf buffer, int octetsCount) throws ParameterException {
        if(skipDigits()){
            return;
        }
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int count = 0;
        try {
            address = "";
            int b = 0;
            // while (octetsCount != count - 1 && bis.available() - 1 > 0) {
            while (octetsCount - 1 != count && buffer.readableBytes() - 1 > 0) {
                b = (byte) buffer.readByte();
                count++;

                int d1 = b & 0x0f;
                int d2 = (b & 0xf0) >> 4;

                address += Integer.toHexString(d1) + Integer.toHexString(d2);

            }

            b = buffer.readByte() & 0xff;
            count++;
            address += Integer.toHexString((b & 0x0f));

            if (oddFlag != _FLAG_ODD) {
                address += Integer.toHexString((b & 0xf0) >> 4);
            }
            this.setAddress(address);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        if (octetsCount != count) {
            throw new ParameterException("Failed to read [" + octetsCount + "], encountered only [" + count + "]");
        }
    }

    /**
     * This method is used in encode method. It encodes header part (1 or 2 bytes usually.)
     *
     * @param bis
     * @return - number of bytes encoded.
     */
    public void encodeHeader(ByteBuf buffer) {
        int b = 0;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        buffer.writeByte(b);
    }

    /**
     * This methods is used in encode method. It encodes body. Each subclass shoudl provide implementetaion.
     *
     * @param bis
     * @return - number of bytes reads
     *
     */
    public abstract void encodeBody(ByteBuf buffer);

    /**
     * This method is used in encode. Encodes digits part. This is because
     *
     * @param bos - where digits will be encoded
     * @return - number of bytes encoded
     *
     */
    public void encodeDigits(ByteBuf buffer) {
        if(skipDigits()){
            return;
        }
        boolean isOdd = this.oddFlag == _FLAG_ODD;

        byte b = 0;
        int count = (!isOdd) ? address.length() : address.length() - 1;
        for (int i = 0; i < count - 1; i += 2) {
            String ds1 = address.substring(i, i + 1);
            String ds2 = address.substring(i + 1, i + 2);

            int d1 = Integer.parseInt(ds1, 16);
            int d2 = Integer.parseInt(ds2, 16);

            b = (byte) (d2 << 4 | d1);
            buffer.writeByte(b);
        }

        if (isOdd) {
            String ds1 = address.substring(count, count + 1);
            int d = Integer.parseInt(ds1);

            b = (byte) (d & 0x0f);
            buffer.writeByte(b);
        }
    }

    public boolean isOddFlag() {
        return oddFlag == _FLAG_ODD;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        if(address!=null){
            // lets compute odd flag
            oddFlag = this.address.length() % 2;
        } else {
            oddFlag = 0;
        }
    }

    protected boolean skipDigits(){
        return false;
    }
}
