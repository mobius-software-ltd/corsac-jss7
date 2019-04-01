/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
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

package org.restcomm.protocols.ss7.sccp.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.indicator.NatureOfAddress;
import org.restcomm.protocols.ss7.indicator.NumberingPlan;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.EncodingScheme;
import org.restcomm.protocols.ss7.sccp.parameter.GlobalTitle0100;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 */
public class GlobalTitle0100Impl extends AbstractGlobalTitle implements GlobalTitle0100 {
	private static final long serialVersionUID = 1L;

	private NatureOfAddress natureOfAddress;
    private NumberingPlan numberingPlan;
    private int translationType;

    public GlobalTitle0100Impl() {
    }

    /**
     * @param natureOfAddress
     */
    public GlobalTitle0100Impl(final String digits,final int translationType, final EncodingScheme encodingScheme,final NumberingPlan numberingPlan, final NatureOfAddress natureOfAddress) {
        super();

        if(digits == null){
            throw new IllegalArgumentException();
        }
        if(encodingScheme == null){
            throw new IllegalArgumentException();
        }
        if(numberingPlan == null){
            throw new IllegalArgumentException();
        }
        if(natureOfAddress == null){
            throw new IllegalArgumentException();
        }
        super.encodingScheme = encodingScheme;
        this.translationType = translationType;
        this.natureOfAddress = natureOfAddress;
        this.numberingPlan = numberingPlan;
        super.digits = digits;

    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.GLOBAL_TITLE_INCLUDES_TRANSLATION_TYPE_NUMBERING_PLAN_ENCODING_SCHEME_AND_NATURE_OF_ADDRESS;
    }

    @Override
    public EncodingScheme getEncodingScheme() {
        return this.encodingScheme;
    }

    @Override
    public int getTranslationType() {
        return this.translationType;
    }

    @Override
    public NatureOfAddress getNatureOfAddress() {
        return this.natureOfAddress;
    }

    @Override
    public NumberingPlan getNumberingPlan() {
        return this.numberingPlan;
    }

    @Override
    public void decode(ByteBuf buffer,final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	this.translationType = buffer.readByte() & 0xff;

        int b = buffer.readByte() & 0xff;

        this.encodingScheme = factory.createEncodingScheme((byte) (b & 0x0f));
        this.numberingPlan = NumberingPlan.valueOf((b & 0xf0) >> 4);
        b = buffer.readByte() & 0xff;
        this.natureOfAddress = NatureOfAddress.valueOf(b);
        super.digits = this.encodingScheme.decode(buffer);
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	if(super.digits == null){
            throw new IllegalStateException();
        }
        buffer.writeByte(this.translationType);
        buffer.writeByte((this.numberingPlan.getValue() << 4) | this.encodingScheme.getSchemeCode());
        buffer.writeByte(this.natureOfAddress.getValue());
        this.encodingScheme.encode(digits, buffer);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((natureOfAddress == null) ? 0 : natureOfAddress.hashCode());
        result = prime * result + ((numberingPlan == null) ? 0 : numberingPlan.hashCode());
        result = prime * result + translationType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GlobalTitle0100Impl other = (GlobalTitle0100Impl) obj;
        if (natureOfAddress != other.natureOfAddress)
            return false;
        if (numberingPlan != other.numberingPlan)
            return false;
        if (translationType != other.translationType)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GlobalTitle0100Impl [digits=" + digits + ", natureOfAddress=" + natureOfAddress + ", numberingPlan=" + numberingPlan
                + ", translationType=" + translationType + ", encodingScheme=" + encodingScheme + "]";
    }
}