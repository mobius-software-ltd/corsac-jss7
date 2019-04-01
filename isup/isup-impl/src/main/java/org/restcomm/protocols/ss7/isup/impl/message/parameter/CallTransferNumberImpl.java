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
 * Start time:12:34:12 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallTransferNumber;

/**
 * Start time:12:34:12 2009-09-07<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 */
public class CallTransferNumberImpl extends AbstractNAINumber implements CallTransferNumber {
	protected int numberingPlanIndicator;

    protected int addressRepresentationREstrictedIndicator;

    protected int screeningIndicator;

    /**
     * @param representation
     * @throws ParameterException
     */
    public CallTransferNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public CallTransferNumberImpl() {
        super();
    }
    
    public CallTransferNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int addressRepresentationREstrictedIndicator, int screeningIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
        this.screeningIndicator = screeningIndicator;
    }

    public void encodeHeader(ByteBuf buffer) {
        doAddressPresentationRestricted();
        super.encodeHeader(buffer);
    }

    /**
     * makes checks on APRI - see NOTE to APRI in Q.763, p 23
     */
    protected void doAddressPresentationRestricted() {
        //FIXME XXX
        //
        // if (this.addressRepresentationREstrictedIndicator == _)
        // return;
        //
        // // NOTE 1 � If the parameter is included and the address presentation
        // // restricted indicator indicates
        // // address not available, octets 3 to n( this are digits.) are omitted,
        // // the subfields in items a - odd/evem, b -nai , c - ni and d -npi, are
        // // coded with
        // // 0's, and the subfield f - filler, is coded with 11.
        // this.oddFlag = 0;
        // this.natureOfAddresIndicator = 0;
        // this.numberingPlanIndicator = 0;
        // // 11
        // this.screeningIndicator = 3;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.AbstractNumber#decodeBody(java.io.ByteArrayInputStream)
     */

    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException, ParameterException {
    	if(buffer.readableBytes()==0) {
            throw new ParameterException("byte[] must  not be null and length must  be greater than 0");
        }
    		
        int b = buffer.readByte() & 0xff;

        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationREstrictedIndicator = (b & 0x0c) >> 2;
        this.screeningIndicator = (b & 0x03);        
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.AbstractNumber#encodeBody(java.io.ByteArrayOutputStream)
     */
    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.isup.parameters.AbstractNumber#encodeBody(java.io. ByteArrayOutputStream)
     */

    public void encodeBody(ByteBuf buffer) {
        int c = this.numberingPlanIndicator << 4;

        c |= (this.addressRepresentationREstrictedIndicator << 2);
        c |= (this.screeningIndicator);
        buffer.writeByte(c & 0x7F);        
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationREstrictedIndicator;
    }

    public void setAddressRepresentationRestrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationREstrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public int getScreeningIndicator() {
        return screeningIndicator;
    }

    public void setScreeningIndicator(int screeningIndicator) {
        this.screeningIndicator = screeningIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.message.parameter.ISUPParameter#getCode()
     */
    public int getCode() {
        return _PARAMETER_CODE;
    }

}
