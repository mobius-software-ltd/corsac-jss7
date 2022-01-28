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
 * Start time:13:49:00 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.BackwardCallIndicators;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:49:00 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class BackwardCallIndicatorsImpl extends AbstractISUPParameter implements BackwardCallIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private int chargeIndicator = 0;

    private int calledPartysStatusIndicator = 0;

    private int calledPartysCategoryIndicator = 0;

    private int endToEndMethodIndicator = 0;

    private boolean interworkingIndicator = false;

    private boolean endToEndInformationIndicator = false;

    private boolean isdnUserPartIndicator = false;

    private boolean isdnAccessIndicator = false;

    private boolean echoControlDeviceIndicator = false;

    private boolean holdingIndicator = false;

    private int sccpMethodIndicator = 0;

    public BackwardCallIndicatorsImpl() {

    }

    public BackwardCallIndicatorsImpl(int chargeIndicator, int calledPartysStatusIndicator, int calledPartysCategoryIndicator,
            int endToEndMethodIndicator, boolean interworkingIndicator, boolean endToEndInformationIndicator,
            boolean isdnUserPartIndicator, boolean isdnAccessIndicator, boolean echoControlDeviceIndicator,
            boolean holdingIndicator, int sccpMethodIndicator) {
        super();
        this.chargeIndicator = chargeIndicator;
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
        this.endToEndMethodIndicator = endToEndMethodIndicator;
        this.interworkingIndicator = interworkingIndicator;
        this.endToEndInformationIndicator = endToEndInformationIndicator;
        this.isdnUserPartIndicator = isdnUserPartIndicator;
        this.isdnAccessIndicator = isdnAccessIndicator;
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
        this.holdingIndicator = holdingIndicator;
        this.sccpMethodIndicator = sccpMethodIndicator;
    }

    /**
     * requres length 2 array.
     *
     * @param b
     */
    public BackwardCallIndicatorsImpl(ByteBuf b) throws ParameterException {
        this.decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 2) {
            throw new ParameterException("buffer must not be null or have different size than 2");
        }

        int v = b.readByte();
        this.chargeIndicator = v & 0x03;
        this.calledPartysStatusIndicator = (v >> 2) & 0x03;
        this.calledPartysCategoryIndicator = (v >> 4) & 0x03;
        this.endToEndMethodIndicator = (v >> 6) & 0x03;

        v = b.readByte();
        this.interworkingIndicator = (v & 0x01) == _TURN_ON;
        this.endToEndInformationIndicator = ((v >> 1) & 0x01) == _TURN_ON;
        this.isdnUserPartIndicator = ((v >> 2) & 0x01) == _TURN_ON;
        this.holdingIndicator = ((v >> 3) & 0x01) == _TURN_ON;
        this.isdnAccessIndicator = ((v >> 4) & 0x01) == _TURN_ON;
        this.echoControlDeviceIndicator = ((v >> 5) & 0x01) == _TURN_ON;
        this.sccpMethodIndicator = (v >> 6) & 0x03;        
    }

    public void encode(ByteBuf b) throws ParameterException {
        int v = 0;
        v |= this.chargeIndicator & 0x03;
        v |= (this.calledPartysStatusIndicator & 0x03) << 2;
        v |= (this.calledPartysCategoryIndicator & 0x03) << 4;
        v |= (this.endToEndMethodIndicator & 0x03) << 6;
        b.writeByte((byte) v);
        v = 0;

        v |= (this.interworkingIndicator ? _TURN_ON : _TURN_OFF);
        v |= (this.endToEndInformationIndicator ? _TURN_ON : _TURN_OFF) << 1;
        v |= (this.isdnUserPartIndicator ? _TURN_ON : _TURN_OFF) << 2;
        v |= (this.holdingIndicator ? _TURN_ON : _TURN_OFF) << 3;
        v |= (this.isdnAccessIndicator ? _TURN_ON : _TURN_OFF) << 4;
        v |= (this.echoControlDeviceIndicator ? _TURN_ON : _TURN_OFF) << 5;
        v |= (this.sccpMethodIndicator & 0x03) << 6;

        b.writeByte((byte) v);
    }

    public int getChargeIndicator() {
        return chargeIndicator;
    }

    public void setChargeIndicator(int chargeIndicator) {
        this.chargeIndicator = chargeIndicator;
    }

    public int getCalledPartysStatusIndicator() {
        return calledPartysStatusIndicator;
    }

    public void setCalledPartysStatusIndicator(int calledPartysStatusIndicator) {
        this.calledPartysStatusIndicator = calledPartysStatusIndicator;
    }

    public int getCalledPartysCategoryIndicator() {
        return calledPartysCategoryIndicator;
    }

    public void setCalledPartysCategoryIndicator(int calledPartysCategoryIndicator) {
        this.calledPartysCategoryIndicator = calledPartysCategoryIndicator;
    }

    public int getEndToEndMethodIndicator() {
        return endToEndMethodIndicator;
    }

    public void setEndToEndMethodIndicator(int endToEndMethodIndicator) {
        this.endToEndMethodIndicator = endToEndMethodIndicator;
    }

    public boolean isInterworkingIndicator() {
        return interworkingIndicator;
    }

    public void setInterworkingIndicator(boolean interworkingIndicator) {
        this.interworkingIndicator = interworkingIndicator;
    }

    public boolean isEndToEndInformationIndicator() {
        return endToEndInformationIndicator;
    }

    public void setEndToEndInformationIndicator(boolean endToEndInformationIndicator) {
        this.endToEndInformationIndicator = endToEndInformationIndicator;
    }

    public boolean isIsdnUserPartIndicator() {
        return isdnUserPartIndicator;
    }

    public void setIsdnUserPartIndicator(boolean isdnUserPartIndicator) {
        this.isdnUserPartIndicator = isdnUserPartIndicator;
    }

    public boolean isIsdnAccessIndicator() {
        return isdnAccessIndicator;
    }

    public void setIsdnAccessIndicator(boolean isdnAccessIndicator) {
        this.isdnAccessIndicator = isdnAccessIndicator;
    }

    public boolean isEchoControlDeviceIndicator() {
        return echoControlDeviceIndicator;
    }

    public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator) {
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public boolean isHoldingIndicator() {
        return holdingIndicator;
    }

    public void setHoldingIndicator(boolean holdingIndicator) {
        this.holdingIndicator = holdingIndicator;
    }

    public int getSccpMethodIndicator() {
        return sccpMethodIndicator;
    }

    public void setSccpMethodIndicator(int sccpMethodIndicator) {
        this.sccpMethodIndicator = sccpMethodIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("BackwardCallIndicators [");

        sb.append("chargeIndicator=");
        sb.append(chargeIndicator);
        sb.append(", ");
        sb.append("calledPartysStatusIndicator=");
        sb.append(calledPartysStatusIndicator);
        sb.append(", ");
        sb.append("calledPartysCategoryIndicator=");
        sb.append(calledPartysCategoryIndicator);
        sb.append(", ");
        sb.append("endToEndMethodIndicator=");
        sb.append(endToEndMethodIndicator);
        sb.append(", ");
        sb.append("interworkingIndicator=");
        sb.append(interworkingIndicator);
        sb.append(", ");
        sb.append("endToEndInformationIndicator=");
        sb.append(endToEndInformationIndicator);
        sb.append(", ");
        sb.append("isdnUserPartIndicator=");
        sb.append(isdnUserPartIndicator);
        sb.append(", ");
        sb.append("isdnAccessIndicator=");
        sb.append(isdnAccessIndicator);
        sb.append(", ");
        sb.append("echoControlDeviceIndicator=");
        sb.append(echoControlDeviceIndicator);
        sb.append(", ");
        sb.append("holdingIndicator=");
        sb.append(holdingIndicator);
        sb.append(", ");
        sb.append("sccpMethodIndicator=");
        sb.append(sccpMethodIndicator);

        sb.append("]");
        return sb.toString();
    }

}
