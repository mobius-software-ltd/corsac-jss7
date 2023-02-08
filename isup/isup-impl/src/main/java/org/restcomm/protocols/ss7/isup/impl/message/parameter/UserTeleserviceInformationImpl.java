/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
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
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

import io.netty.buffer.ByteBuf;

/**
 * Start time:12:43:13 2009-04-04<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author sergey vetyutnev
 * @author yulianoifa
 */
public class UserTeleserviceInformationImpl extends AbstractISUPParameter implements UserTeleserviceInformation {
	private int codingStandard;
    private int interpretation;
    private int presentationMethod;
    private int highLayerCharIdentification;

    // I hate this, its soo jsr 17 style...
    private boolean eHighLayerCharIdentificationPresent;
    private boolean eVideoTelephonyCharIdentificationPresent;

    private int eHighLayerCharIdentification;
    private int eVideoTelephonyCharIdentification;

    public UserTeleserviceInformationImpl() {
        super();

    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification, int eVideoTelephonyCharIdentification, boolean notImportantIgnoredParameter) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
        setEVideoTelephonyCharIdentification(eVideoTelephonyCharIdentification);
    }

    public UserTeleserviceInformationImpl(int codingStandard, int interpretation, int presentationMethod,
            int highLayerCharIdentification, int eHighLayerCharIdentification) {
        super();
        this.setCodingStandard(codingStandard);
        this.setInterpretation(interpretation);
        this.setPresentationMethod(presentationMethod);
        this.setHighLayerCharIdentification(highLayerCharIdentification);
        this.setEHighLayerCharIdentification(eHighLayerCharIdentification);
    }

    public UserTeleserviceInformationImpl(ByteBuf b) throws ParameterException {
        super();
        // FIXME: this is only elementID

        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() < 2) {
            throw new ParameterException("buffer must not be null and length must be greater than  1");
        }

        byte b0=b.readByte(),b1=b.readByte();
        try {
        	this.setPresentationMethod(b0);
            this.setInterpretation((b0 >> 2));
            this.setCodingStandard((b0 >> 5));
            this.setHighLayerCharIdentification(b1);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
        
        boolean ext = ((b1 >> 7) & 0x01) == 0;
        if (ext && b.readableBytes()==0) {
            throw new ParameterException(
                    "buffer indicates extension to high layer cahracteristic indicator, however there isnt enough bytes");
        }
        if (!ext) {
            return;
        }

        // FIXME: add check for excesive byte?, it should not happen though?
        if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
            this.setEHighLayerCharIdentification(b.readByte() & 0x7F);
            // } else if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <=
            // _HLCI_AUDIO_VID_HIGH_RANGE)
            // || (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <=
            // _HLCI_AUDIO_VID_HIGH_RANGE2)) {
        } else if ((this.highLayerCharIdentification >= _HLCI_VIDEOTELEPHONY && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
            this.setEVideoTelephonyCharIdentification(b.readByte() & 0x7F);
        } else {
            // logger.warning("HLCI indicates value which does not allow for extension, but its present....");
        }
    }

    public void encode(ByteBuf buffer) {
        int v = 0;
        v = this.presentationMethod & 0x03;
        v |= (this.interpretation & 0x07) << 2;
        v |= (this.codingStandard & 0x03) << 5;
        v |= 0x80;

        buffer.writeByte((byte) v);
        byte b1=(byte) (this.highLayerCharIdentification & 0x7F);        
        if (this.eHighLayerCharIdentificationPresent || this.eVideoTelephonyCharIdentificationPresent) {
        	buffer.writeByte(b1);
            if (this.eHighLayerCharIdentificationPresent) {
            	buffer.writeByte((byte) (0x80 | (this.eHighLayerCharIdentification & 0x7F)));
            } else {
            	buffer.writeByte((byte) (0x80 | (this.eVideoTelephonyCharIdentification & 0x7F)));
            }
        } else {
            b1 |= 0x80;
            buffer.writeByte(b1);
        }
    }

    public int getCodingStandard() {
        return codingStandard;
    }

    public void setCodingStandard(int codingStandard) {
        this.codingStandard = codingStandard & 0x03;
    }

    public int getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(int interpretation) {
        this.interpretation = interpretation & 0x07;
    }

    public int getPresentationMethod() {
        return presentationMethod;
    }

    public void setPresentationMethod(int presentationMethod) {
        this.presentationMethod = presentationMethod & 0x03;
    }

    public int getHighLayerCharIdentification() {
        return highLayerCharIdentification;
    }

    public void setHighLayerCharIdentification(int highLayerCharIdentification) {
        this.highLayerCharIdentification = highLayerCharIdentification & 0x7F;
    }

    public int getEHighLayerCharIdentification() {
        return eHighLayerCharIdentification;
    }

    public void setEHighLayerCharIdentification(int highLayerCharIdentification) {

        if (this.eVideoTelephonyCharIdentificationPresent) {
            throw new IllegalStateException(
                    "Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedVideoTlephony is already present");
        }
        if (this.highLayerCharIdentification == _HLCI_MAINTAINENCE || this.highLayerCharIdentification == _HLCI_MANAGEMENT) {
            this.eHighLayerCharIdentificationPresent = true;
            this.eHighLayerCharIdentification = highLayerCharIdentification & 0x7F;
        } else {
            throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
        }
    }

    public int getEVideoTelephonyCharIdentification() {
        return eVideoTelephonyCharIdentification;
    }

    public void setEVideoTelephonyCharIdentification(int eVidedoTelephonyCharIdentification) {
        if (this.eHighLayerCharIdentificationPresent) {
            throw new IllegalStateException(
                    "Either Extended VideoTlephony or Extended HighLayer octet is set. ExtendedHighLayer is already present");
        }
        // if ((this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE && this.highLayerCharIdentification <=
        // _HLCI_AUDIO_VID_HIGH_RANGE)
        // || (this.highLayerCharIdentification >= _HLCI_AUDIO_VID_LOW_RANGE2 && this.highLayerCharIdentification <=
        // _HLCI_AUDIO_VID_HIGH_RANGE2)) {
        if ((this.highLayerCharIdentification >= _HLCI_VIDEOTELEPHONY && this.highLayerCharIdentification <= _HLCI_AUDIO_VID_HIGH_RANGE2)) {
            this.eVideoTelephonyCharIdentificationPresent = true;
            this.eVideoTelephonyCharIdentification = eVidedoTelephonyCharIdentification & 0x7F;
        } else {
            throw new IllegalArgumentException("Can not set this octet - HLCI is of value: " + this.highLayerCharIdentification);
        }
    }

    public boolean isEHighLayerCharIdentificationPresent() {
        return eHighLayerCharIdentificationPresent;
    }

    public boolean isEVideoTelephonyCharIdentificationPresent() {
        return eVideoTelephonyCharIdentificationPresent;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("UserTeleserviceInformation [codingStandard=");
        sb.append(codingStandard);
        sb.append(", interpretation=");
        sb.append(interpretation);
        sb.append(", presentationMethod=");
        sb.append(presentationMethod);
        sb.append(", highLayerCharIdentification=");
        sb.append(highLayerCharIdentification);

        if (this.eHighLayerCharIdentificationPresent) {
            sb.append(", eHighLayerCharIdentification=");
            sb.append(eHighLayerCharIdentification);
        }

        if (this.eVideoTelephonyCharIdentificationPresent) {
            sb.append(", eVideoTelephonyCharIdentification=");
            sb.append(this.eVideoTelephonyCharIdentification);
        }

        sb.append("]");

        return sb.toString();
    }
}
