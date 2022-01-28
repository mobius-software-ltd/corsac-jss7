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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CalledNumber;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:05:28 2009-04-05<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 *
 */
public abstract class CalledNumberImpl extends AbstractNAINumber implements CalledNumber {
	protected int numberingPlanIndicator;
    protected int addressRepresentationRestrictedIndicator;

    public CalledNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public CalledNumberImpl() {
        super();
    }
    
    public CalledNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int addressRepresentationREstrictedIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public void encodeHeader(ByteBuf buffer) {
        doAddressPresentationRestricted();
        super.encodeHeader(buffer);
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.isup.parameters.AbstractNumber#decodeBody(java.io. ByteBuf)
     */
    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException, ParameterException {
    	if(buffer.readableBytes()==0) {
    		throw new ParameterException("buffer must  not be null and length must  be greater than 0");
    	}
    	
        int b = buffer.readByte() & 0xff;

        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
    }

    protected void doAddressPresentationRestricted() {
        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.oddFlag = 0;
            this.natureOfAddresIndicator = 0;
            this.numberingPlanIndicator = 0;
            this.setAddress("");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.isup.parameters.AbstractNumber#encodeBody(io.netty.buffer.ByteBuf)
     */

    public void encodeBody(ByteBuf buffer) {
        int c = (this.numberingPlanIndicator & 0x07) << 4;
        c |= ((this.addressRepresentationRestrictedIndicator & 0x03) << 2);

        buffer.writeByte(c);        
    }

    public void decodeDigits(ByteBuf buffer) throws ParameterException {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.setAddress("");
        } else {
            super.decodeDigits(buffer);
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    protected abstract String getPrimitiveName();

    public String toString() {
        return getPrimitiveName() + " [numberingPlanIndicator=" + numberingPlanIndicator
                + ", addressRepresentationREstrictedIndicator=" + addressRepresentationRestrictedIndicator
                + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag=" + oddFlag + ", address=" + address
                + "]";
    }
}
