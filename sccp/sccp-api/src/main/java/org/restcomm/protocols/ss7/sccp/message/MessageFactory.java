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

package org.restcomm.protocols.ss7.sccp.message;

import java.io.Serializable;

import org.restcomm.protocols.ss7.sccp.parameter.Credit;
import org.restcomm.protocols.ss7.sccp.parameter.HopCounter;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.SccpAddress;

import io.netty.buffer.ByteBuf;

/**
 * Factory for creating messages.
 *
 * @author baranowb
 * @author kulikov
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MessageFactory extends Serializable {

    /**
     * Create a SCCP data transfer message (class 0)
     *
     * @param calledParty
     * @param callingParty
     * @param data
     * @param localSsn
     * @param returnMessageOnError
     * @param hopCounter This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpDataMessage createDataMessageClass0(SccpAddress calledParty, SccpAddress callingParty, ByteBuf data,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance);

    /**
     * Create a SCCP data transfer message (class 1)
     *
     * @param calledParty
     * @param callingParty
     * @param data
     * @param sls
     * @param localSsn
     * @param returnMessageOnError
     * @param hopCounter This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpDataMessage createDataMessageClass1(SccpAddress calledParty, SccpAddress callingParty, ByteBuf data, int sls,
            int localSsn, boolean returnMessageOnError, HopCounter hopCounter, Importance importance);

    // SccpNoticeMessage createNoticeMessage(ReturnCause returnCause, int outgoingSls, SccpAddress calledParty,
    // SccpAddress callingParty, ByteBuf data,
    // HopCounter hopCounter, Importance importance);

    /**
     * Create a SCCP connection request message (class 2)
     *
     * @param calledParty
     * @param callingParty This parameter is optional
     * @param data       This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpConnCrMessage createConnectMessageClass2(int localSsn, SccpAddress calledParty, SccpAddress callingParty, ByteBuf data, Importance importance);

    /**
     * Create a SCCP connection request message (class 3)
     *
     * @param calledParty
     * @param callingParty This parameter is optional
     * @param credit
     * @param data       This parameter is optional
     * @param importance This parameter is optional
     * @return
     */
    SccpConnCrMessage createConnectMessageClass3(int localSsn, SccpAddress calledParty, SccpAddress callingParty, Credit credit, ByteBuf data, Importance importance);
}
