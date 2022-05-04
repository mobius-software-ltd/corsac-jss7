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

package org.restcomm.protocols.ss7.isup.impl.stack.timers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.restcomm.protocols.ss7.isup.ISUPTimeoutEvent;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupQueryMessage;
import org.restcomm.protocols.ss7.isup.message.CircuitGroupQueryResponseMessage;
import org.restcomm.protocols.ss7.isup.message.ISUPMessage;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitIdentificationCode;
import org.restcomm.protocols.ss7.isup.message.parameter.CircuitStateIndicator;
import org.restcomm.protocols.ss7.isup.message.parameter.RangeAndStatus;

/**
 * @author baranowb
 * @author yulianoifa
 * 
 *
 */
public class CQMTest extends SingleTimers {

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT()
     */

    protected long getT() {
        return ISUPTimeoutEvent.T28_DEFAULT;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.impl.stack.timers.SingleTimers#getT_ID()
     */

    protected int getT_ID() {
        return ISUPTimeoutEvent.T28;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.impl.stack.timers.EventTestHarness# getRequest()
     */

    protected ISUPMessage getRequest() {
        CircuitGroupQueryMessage msg = super.provider.getMessageFactory().createCQM(1);
        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        msg.setRangeAndStatus(ras);
        return msg;
    }

    protected ISUPMessage getAfterTRequest() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.isup.impl.stack.timers.EventTestHarness#getAnswer ()
     */
    protected ISUPMessage getAnswer() {
        CircuitGroupQueryResponseMessage ans = super.provider.getMessageFactory().createCQR();
        CircuitIdentificationCode cic = super.provider.getParameterFactory().createCircuitIdentificationCode();
        cic.setCIC(1);
        ans.setCircuitIdentificationCode(cic);

        RangeAndStatus ras = super.provider.getParameterFactory().createRangeAndStatus();
        ras.setRange((byte) 7, true);
        ras.setAffected((byte) 1, true);
        ras.setAffected((byte) 0, true);
        ans.setRangeAndStatus(ras);

        CircuitStateIndicator ci = super.provider.getParameterFactory().createCircuitStateIndicator();
        ByteBuf state = Unpooled.buffer(2);
        state.writeByte(ci.createCircuitState(CircuitStateIndicator._MBS_LAR_BLOCKED, CircuitStateIndicator._CPS_CIB, CircuitStateIndicator._HBS_LAR_BLOCKED));
        state.writeByte(ci.createCircuitState(CircuitStateIndicator._MBS_LAR_BLOCKED, CircuitStateIndicator._CPS_COB, CircuitStateIndicator._HBS_LAR_BLOCKED));
        ci.setCircuitState(state);
        ans.setCircuitStateIndicator(ci);
        return ans;
    }
}
