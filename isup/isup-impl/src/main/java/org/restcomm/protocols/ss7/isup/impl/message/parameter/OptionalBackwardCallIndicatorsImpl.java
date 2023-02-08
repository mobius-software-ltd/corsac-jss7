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
import org.restcomm.protocols.ss7.isup.message.parameter.OptionalBackwardCallIndicators;

/**
 * Start time:11:38:33 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class OptionalBackwardCallIndicatorsImpl extends AbstractISUPParameter implements OptionalBackwardCallIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private boolean inbandInformationIndicator;
    private boolean callDiversionMayOccurIndicator;
    private boolean simpleSegmentationIndicator;
    private boolean mllpUserIndicator;

    public OptionalBackwardCallIndicatorsImpl() {
        super();

    }

    public OptionalBackwardCallIndicatorsImpl(boolean inbandInformationIndicator, boolean callDiversionMayOccurIndicator,boolean simpleSegmentationIndicator, boolean mllpUserIndicator) {
        super();
        this.inbandInformationIndicator = inbandInformationIndicator;
        this.callDiversionMayOccurIndicator = callDiversionMayOccurIndicator;
        this.simpleSegmentationIndicator = simpleSegmentationIndicator;
        this.mllpUserIndicator = mllpUserIndicator;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must  not be null and length must  be 1");
        }
        
        byte curr=b.readByte();
        this.inbandInformationIndicator = (curr & 0x01) == _TURN_ON;
        this.callDiversionMayOccurIndicator = ((curr >> 1) & 0x01) == _TURN_ON;
        this.simpleSegmentationIndicator = ((curr >> 2) & 0x01) == _TURN_ON;
        this.mllpUserIndicator = ((curr >> 3) & 0x01) == _TURN_ON;        
    }

    public void encode(ByteBuf buffef) throws ParameterException {
        int b0 = 0;
        b0 = this.inbandInformationIndicator ? _TURN_ON : _TURN_OFF;
        b0 |= (this.callDiversionMayOccurIndicator ? _TURN_ON : _TURN_OFF) << 1;
        b0 |= (this.simpleSegmentationIndicator ? _TURN_ON : _TURN_OFF) << 2;
        b0 |= (this.mllpUserIndicator ? _TURN_ON : _TURN_OFF) << 3;
        buffef.writeByte((byte) b0);
    }

    public boolean isInbandInformationIndicator() {
        return this.inbandInformationIndicator;
    }

    public void setInbandInformationIndicator(boolean inbandInformationIndicator) {
        this.inbandInformationIndicator = inbandInformationIndicator;
    }

    public boolean isCallDiversionMayOccurIndicator() {
        return callDiversionMayOccurIndicator;
    }

    public void setCallDiversionMayOccurIndicator(boolean callDiversionMayOccurIndicator) {
        this.callDiversionMayOccurIndicator = callDiversionMayOccurIndicator;
    }

    public boolean isSimpleSegmentationIndicator() {
        return simpleSegmentationIndicator;
    }

    public void setSimpleSegmentationIndicator(boolean simpleSegmentationIndicator) {
        this.simpleSegmentationIndicator = simpleSegmentationIndicator;
    }

    public boolean isMllpUserIndicator() {
        return mllpUserIndicator;
    }

    public void setMllpUserIndicator(boolean mllpUserIndicator) {
        this.mllpUserIndicator = mllpUserIndicator;
    }

    public int getCode() {
        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("OptionalBackwardCallIndicators [");

        sb.append("inbandInformationIndicator=");
        sb.append(inbandInformationIndicator);
        sb.append(", ");
        sb.append("callDiversionMayOccurIndicator=");
        sb.append(callDiversionMayOccurIndicator);
        sb.append(", ");
        sb.append("simpleSegmentationIndicator=");
        sb.append(simpleSegmentationIndicator);
        sb.append(", ");
        sb.append("mllpUserIndicator=");
        sb.append(mllpUserIndicator);

        sb.append("]");
        return sb.toString();
    }
}
