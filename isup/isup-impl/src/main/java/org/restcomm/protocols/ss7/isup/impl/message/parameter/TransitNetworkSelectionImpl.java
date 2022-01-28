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

/**
 * Start time:17:05:31 2009-04-03<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.TransitNetworkSelection;

/**
 * Start time:17:05:31 2009-04-03<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class TransitNetworkSelectionImpl extends AbstractISUPParameter implements TransitNetworkSelection {
	protected static final Logger logger = Logger.getLogger(TransitNetworkSelectionImpl.class);

    // FIXME: Oleg is this correct?
    private String address;
    private int typeOfNetworkIdentification;
    private int networkIdentificationPlan;
    /**
     * Holds odd flag, it can have either value: 10000000(x80) or 00000000. For each it takes value 1 and 0;
     */
    protected int oddFlag;

    /**
     * indicates odd flag value (0x80) as integer (1). This is achieved when odd flag in parameter is moved to the right by
     * sever possitions ( >> 7)
     */
    public static final int _FLAG_ODD = 1;

    public TransitNetworkSelectionImpl(String address, int typeOfNetworkIdentification, int networkIdentificationPlan) {
        super();
        this.setAddress(address);
        this.typeOfNetworkIdentification = typeOfNetworkIdentification;
        this.networkIdentificationPlan = networkIdentificationPlan;
    }

    public TransitNetworkSelectionImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public TransitNetworkSelectionImpl() {
        super();

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

        encodeDigits(buffer);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Encoding digits");
        }
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

        decodeDigits(b);
        if (logger.isDebugEnabled()) {
            logger.debug("[" + this.getClass().getSimpleName() + "]Decoding digits");
        }        
    }

    /**
     * This method is used in encode. Encodes digits part. This is because
     *
     * @param bos - where digits will be encoded
     * @return - number of bytes encoded
     *
     */
    public void encodeDigits(ByteBuf buffer) {
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

    /**
     * This method is used in constructor that takes ByteBuf as parameter. Decodes digits part. Stores
     * result in digits field, where digits[0] holds most significant digit. This is because
     *
     * @param bis
     * @return - number of bytes reads throws IllegalArgumentException - thrown if read error is encountered.
     * @throws ParameterException - thrown if read error is encountered.
     */
    public void decodeDigits(ByteBuf buffer) throws ParameterException {
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        // FIXME: we could spare time by passing length arg - or getting it from
        // bis??
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

        if (oddFlag != 1) {
            address += Integer.toHexString((b & 0xf0) >> 4);
        }
    }

    /**
     * This method is used in constructor that takes ByteBuf as parameter. Decodes header part (its 1 or
     * 2 bytes usually.) Default implemetnation decodes header of one byte - where most significant bit is O/E indicator and
     * bits 7-1 are NAI. This method should be over
     *
     * @param bis
     * @return - number of bytes reads
     * @throws ParameterException - thrown if read error is encountered.
     */
    public void decodeHeader(ByteBuf buffer) throws ParameterException {
        if (buffer.readableBytes() == 0) {
            throw new ParameterException("No more data to read.");
        }
        int b = buffer.readByte() & 0xff;

        this.oddFlag = (b & 0x80) >> 7;
        this.setTypeOfNetworkIdentification((b >> 4));
        this.setNetworkIdentificationPlan(b);
    }

    /**
     * This method is used in encode method. It encodes header part (1 or 2 bytes usually.)
     *
     * @param bis
     * @return - number of bytes encoded.
     */
    public void encodeHeader(ByteBuf buffer) {
        int b = this.networkIdentificationPlan & 0x0F;
        b |= (this.typeOfNetworkIdentification & 0x07) << 4;
        // Even is 000000000 == 0
        boolean isOdd = this.oddFlag == _FLAG_ODD;
        if (isOdd)
            b |= 0x80;
        buffer.writeByte(b);
    }

    public int getTypeOfNetworkIdentification() {
        return typeOfNetworkIdentification;
    }

    public void setTypeOfNetworkIdentification(int typeOfNetworkIdentification) {
        this.typeOfNetworkIdentification = typeOfNetworkIdentification & 0x07;
    }

    public int getNetworkIdentificationPlan() {
        return networkIdentificationPlan;
    }

    public void setNetworkIdentificationPlan(int networkIdentificationPlan) {
        this.networkIdentificationPlan = networkIdentificationPlan & 0x0F;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        oddFlag = this.address.length() % 2;
    }

    public boolean isOddFlag() {
        return oddFlag == _FLAG_ODD;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
