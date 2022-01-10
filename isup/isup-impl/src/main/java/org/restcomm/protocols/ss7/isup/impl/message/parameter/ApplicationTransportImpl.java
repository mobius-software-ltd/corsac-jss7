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
 * Start time:15:10:58 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.ApplicationTransport;

/**
 * Start time:15:10:58 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ApplicationTransportImpl extends AbstractISUPParameter implements ApplicationTransport {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private Byte applicationContextIdentifier, apmSegmentationIndicator, segmentationLocalReference;
    private Boolean sendNotificationIndicator, releaseCallIndicator, segmentationIndicator;
    private ByteBuf encapsulatedApplicationData;

    public ApplicationTransportImpl() {
        super();
    }

    public ApplicationTransportImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        // 4+ lines, depending on "ext" bits...
        if (b == null || b.readableBytes() < 1) {
            throw new ParameterException("byte[] must not be null or have bigger size.");
        }

        // integrity check
        b.markReaderIndex();
        for (int index = 0; index < 4 && b.readableBytes() > 0; index++) {
            if( (b.readByte() & 0x80) == 0){
                //expect more
                if(b.readableBytes() == 0){
                    //but there is nothing more
                    throw new ParameterException();
                }
            } else {
                //this should be last
                if(b.readableBytes() > 0){
                    throw new ParameterException();
                }
            }
        }
        
        b.resetReaderIndex();
        this.applicationContextIdentifier = (byte) (b.readByte() & 0x7F);
        if (b.readableBytes() == 0)
            return;
        
        byte currByte=b.readByte();
        this.releaseCallIndicator = (currByte & 0x01) == _TURN_ON;
        this.sendNotificationIndicator = ((currByte >> 1) & 0x01) == _TURN_ON;
        if (b.readableBytes() == 0)
            return;
        
        currByte=b.readByte();
        this.apmSegmentationIndicator = (byte) (currByte & 0x3F);
        this.segmentationIndicator = ((currByte >> 6) & 0x01) == _TURN_ON;
        if (b.readableBytes() == 0)
            return;
        
        currByte=b.readByte();
        this.segmentationLocalReference = (byte) (currByte & 0x7F);
        if (b.readableBytes() == 0)
            return;
        
        this.encapsulatedApplicationData = b.slice(b.readerIndex(), b.readableBytes());
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        if(this.applicationContextIdentifier == null){
            throw new ParameterException();
        }
        
        boolean end = false;
        int value = 0x80;
        if (this.releaseCallIndicator != null) {
            value = 0;
        } else {
            end = true;
        }
        value |= (this.applicationContextIdentifier & 0x7F);
        buffer.writeByte(value);
        if (end)
            return;
        
        if (this.apmSegmentationIndicator != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        
        value |= ((this.sendNotificationIndicator ? _TURN_ON : _TURN_OFF) << 1);
        value |= (this.releaseCallIndicator ? _TURN_ON : _TURN_OFF);
        buffer.writeByte(value);
        if (end)
            return;

        if (this.segmentationLocalReference != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        value |= ((this.segmentationIndicator ? _TURN_ON : _TURN_OFF) << 6);
        value |= ((this.apmSegmentationIndicator) & 0x3F);
        buffer.writeByte(value);
        if (end)
            return;

        if (this.encapsulatedApplicationData != null) {
            value = 0;
        } else {
            value = 0x80;
            end = true;
        }
        value |= ((this.segmentationLocalReference) & 0x7F);
        buffer.writeByte(value);
        if (end)
            return;

        buffer.writeBytes(this.encapsulatedApplicationData);
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public Byte getApplicationContextIdentifier() {
        return this.applicationContextIdentifier;
    }

    @Override
    public void setApplicationContextIdentifier(Byte v) {
        this.applicationContextIdentifier = v;
    }

    @Override
    public Boolean isSendNotificationIndicator() {
        return this.sendNotificationIndicator;
    }

    @Override
    public void setSendNotificationIndicator(Boolean v) {
        this.sendNotificationIndicator = v;
    }

    @Override
    public Boolean isReleaseCallIndicator() {
        return this.releaseCallIndicator;
    }

    @Override
    public void setReleaseCallIndicator(Boolean v) {
        this.releaseCallIndicator = v;
    }

    @Override
    public Boolean isSegmentationIndicator() {
        return this.segmentationIndicator;
    }

    @Override
    public void setSegmentationIndicator(Boolean v) {
        this.segmentationIndicator = v;
    }

    @Override
    public Byte getAPMSegmentationIndicator() {
        return this.apmSegmentationIndicator;
    }

    @Override
    public void setAPMSegmentationIndicator(Byte v) {
        this.apmSegmentationIndicator = v;
    }

    @Override
    public Byte getSegmentationLocalReference() {
        return this.segmentationLocalReference;
    }

    @Override
    public void setSegmentationLocalReference(Byte v) {
        this.segmentationLocalReference = v;
    }

    @Override
    public ByteBuf getEncapsulatedApplicationInformation() {
    	if(this.encapsulatedApplicationData==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(this.encapsulatedApplicationData);
    }

    @Override
    public void setEncapsulatedApplicationInformation(ByteBuf v) {
        this.encapsulatedApplicationData = v;
    }
}
