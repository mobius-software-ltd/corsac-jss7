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
 * Start time:09:12:26 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.NatureOfConnectionIndicators;

/**
 * Start time:09:12:26 2009-04-02<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NatureOfConnectionIndicatorsImpl extends AbstractISUPParameter implements NatureOfConnectionIndicators {
	private static final int _TURN_ON = 1;
    private static final int _TURN_OFF = 0;

    private int satelliteIndicator = 0;
    private int continuityCheckIndicator = 0;
    private boolean echoControlDeviceIndicator = false;

    public NatureOfConnectionIndicatorsImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public NatureOfConnectionIndicatorsImpl() {
        super();

    }

    public NatureOfConnectionIndicatorsImpl(byte satelliteIndicator, byte continuityCheckIndicator,
            boolean echoControlDeviceIndicator) {
        super();
        this.satelliteIndicator = satelliteIndicator;
        this.continuityCheckIndicator = continuityCheckIndicator;
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must not be null and must have length of 1");
        }
        
        byte curr=b.readByte();
        this.satelliteIndicator = (byte) (curr & 0x03);
        this.continuityCheckIndicator = (byte) ((curr >> 2) & 0x03);
        this.echoControlDeviceIndicator = ((curr >> 4) == _TURN_ON);
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        int b0 = 0;
        b0 = this.satelliteIndicator & 0x03;
        b0 |= (this.continuityCheckIndicator & 0x03) << 2;
        b0 |= (this.echoControlDeviceIndicator ? _TURN_ON : _TURN_OFF) << 4;
        buffer.writeByte((byte) b0);
    }

    public int getSatelliteIndicator() {
        return satelliteIndicator;
    }

    public void setSatelliteIndicator(int satelliteIndicator) {
        this.satelliteIndicator = satelliteIndicator & 0x03;
    }

    public int getContinuityCheckIndicator() {
        return continuityCheckIndicator;
    }

    public void setContinuityCheckIndicator(int continuityCheckIndicator) {
        this.continuityCheckIndicator = continuityCheckIndicator & 0x03;
    }

    public boolean isEchoControlDeviceIndicator() {
        return echoControlDeviceIndicator;
    }

    public void setEchoControlDeviceIndicator(boolean echoControlDeviceIndicator) {
        this.echoControlDeviceIndicator = echoControlDeviceIndicator;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("NatureOfConnectionIndicators [");

        sb.append("satelliteIndicator=");
        sb.append(satelliteIndicator);
        sb.append(", ");
        sb.append("continuityCheckIndicator=");
        sb.append(continuityCheckIndicator);
        sb.append(", ");
        sb.append("echoControlDeviceIndicator=");
        sb.append(echoControlDeviceIndicator);

        sb.append("]");
        return sb.toString();
    }
}
