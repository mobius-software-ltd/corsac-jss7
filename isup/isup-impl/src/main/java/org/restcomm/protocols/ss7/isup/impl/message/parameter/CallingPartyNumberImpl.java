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

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyNumber;

/**
 * Start time:16:14:51 2009-03-29<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Oleg Kulikoff
 * @author yulianoifa
 */
public class CallingPartyNumberImpl extends AbstractNAINumber implements CallingPartyNumber {
	protected int numberingPlanIndicator;
    protected int numberIncompleteIndicator;
    protected int addressRepresentationRestrictedIndicator;
    protected int screeningIndicator;

    /**
     *
     * @param representation
     * @throws ParameterException
     */
    public CallingPartyNumberImpl(ByteBuf representation) throws ParameterException {
        super(representation);
    }

    public CallingPartyNumberImpl() {
        super();
    }

    public CallingPartyNumberImpl(int natureOfAddresIndicator, String address, int numberingPlanIndicator,
            int numberIncompleteIndicator, int addressRepresentationREstrictedIndicator, int screeningIndicator) {
        super(natureOfAddresIndicator, address);
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.numberIncompleteIndicator = numberIncompleteIndicator;
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
        this.screeningIndicator = screeningIndicator;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.isup.parameters.AbstractNumber#decodeBody(io.netty.buffer.ByteBuf)
     */

    public void decodeBody(ByteBuf buffer) throws IllegalArgumentException {
        int b = buffer.readByte() & 0xff;

        this.numberIncompleteIndicator = (b & 0x80) >> 7;
        this.numberingPlanIndicator = (b & 0x70) >> 4;
        this.addressRepresentationRestrictedIndicator = (b & 0x0c) >> 2;
        this.screeningIndicator = (b & 0x03);
    }

    public void encodeHeader(ByteBuf buffer) {
        doAddressPresentationRestricted();
        super.encodeHeader(buffer);
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.restcomm.isup.parameters.AbstractNumber#encodeBody(io.netty.buffer.ByteBuf)
     */

    public void encodeBody(ByteBuf buffer) {
        int c = this.numberingPlanIndicator << 4;
        c |= (this.numberIncompleteIndicator << 7);
        c |= (this.addressRepresentationRestrictedIndicator << 2);
        c |= (this.screeningIndicator);
        buffer.writeByte(c);
    }

    /**
     * makes checks on APRI - see NOTE to APRI in Q.763, p 23
     */
    protected void doAddressPresentationRestricted() {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {

            // NOTE 1 If the parameter is included and the address
            // presentation
            // restricted indicator indicates
            // address not available, octets 3 to n( this are digits.) are
            // omitted,
            // the subfields in items a - odd/evem, b -nai , c - ni and d -npi,
            // are
            // coded with
            // FIXME: add this filler
            // 0's, and the subfield f - filler, is coded with 11.
            this.oddFlag = 0;
            this.natureOfAddresIndicator = 0;
            this.numberIncompleteIndicator = 0;
            this.numberingPlanIndicator = 0;
            this.screeningIndicator = 3; // !!!
            this.setAddress("");
        }
    }

    public void decodeDigits(ByteBuf buffer) throws ParameterException {

        if (this.addressRepresentationRestrictedIndicator == _APRI_NOT_AVAILABLE) {
            this.setAddress("");            
        } else {
            super.decodeDigits(buffer);
        }
    }

    public void encodeDigits(ByteBuf buffer) {

        if (this.addressRepresentationRestrictedIndicator != _APRI_NOT_AVAILABLE) {
            super.encodeDigits(buffer);
        }
    }

    public int getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(int numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public int getNumberIncompleteIndicator() {
        return numberIncompleteIndicator;
    }

    public void setNumberIncompleteIndicator(int numberIncompleteIndicator) {
        this.numberIncompleteIndicator = numberIncompleteIndicator;
    }

    public int getAddressRepresentationRestrictedIndicator() {
        return addressRepresentationRestrictedIndicator;
    }

    public void setAddressRepresentationREstrictedIndicator(int addressRepresentationREstrictedIndicator) {
        this.addressRepresentationRestrictedIndicator = addressRepresentationREstrictedIndicator;
    }

    public int getScreeningIndicator() {
        return screeningIndicator;
    }

    public void setScreeningIndicator(int screeningIndicator) {
        this.screeningIndicator = screeningIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    public String toString() {
        return "CallingPartyNumber [numberingPlanIndicator=" + numberingPlanIndicator + ", numberIncompleteIndicator="
                + numberIncompleteIndicator + ", addressRepresentationRestrictedIndicator="
                + addressRepresentationRestrictedIndicator + ", screeningIndicator=" + screeningIndicator
                + ", natureOfAddresIndicator=" + natureOfAddresIndicator + ", oddFlag=" + oddFlag + ", address=" + address
                + "]";
    }
}
