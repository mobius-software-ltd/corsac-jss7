/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
 * Copyright 2019, Mobius Software LTD and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.restcomm.protocols.ss7.isup.impl.message.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitStateIndicator;

/**
 * Start time:17:10:53 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CircuitStateIndicatorImpl extends AbstractISUPParameter implements CircuitStateIndicator {
	private ByteBuf circuitState = null;

    public CircuitStateIndicatorImpl(ByteBuf circuitState) throws ParameterException {
        super();
        this.decode(circuitState);
    }

    public CircuitStateIndicatorImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
        try {
            setCircuitState(b);
        } catch (Exception e) {
            throw new ParameterException(e);
        }
    }

    public void encode(ByteBuf buffer) throws ParameterException {
        buffer.writeBytes(getCircuitState());
    }

    public ByteBuf getCircuitState() {
    	if(circuitState==null)
    		return null;
    	
        return Unpooled.wrappedBuffer(circuitState);
    }

    public void setCircuitState(ByteBuf circuitState) throws IllegalArgumentException {
        if (circuitState == null || circuitState.readableBytes() == 0) {
            throw new IllegalArgumentException("buffer must nto be null and length must be greater than 0");
        }
        this.circuitState = circuitState;
    }

    public byte createCircuitState(int MBS, int CPS, int HBS) {
        int v = 0;
        // FIXME: Shoudl we here enforce proper coding? according to note or
        // move it to encode??
        if (HBS != _HBS_NO_BLOCKING) {
            // NOTE If bits F E are not coded 0 0, bits D C must be coded 1 1.
            // - this means CPS == 3
            CPS = _CPS_IDLE;
        }

        v = MBS & 0x03;
        v |= (CPS & 0x03) << 2;
        v |= (HBS & 0x03) << 4;
        return (byte) v;
    }

    public int getCallProcessingState(byte b) {
        return (b >> 2) & 0x03;
    }

    public int getHardwareBlockingState(byte b) {
        return (b >> 4) & 0x03;
    }

    public int getMaintenanceBlockingState(byte b) {
        return b & 0x03;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

}
