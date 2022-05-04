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

import org.restcomm.protocols.ss7.isup.ParameterException;
import org.restcomm.protocols.ss7.isup.message.parameter.CallHistoryInformation;

import io.netty.buffer.ByteBuf;

/**
 * Start time:15:04:29 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class CallHistoryInformationImpl extends AbstractISUPParameter implements CallHistoryInformation {
	// XXX: again this goes aganist usuall way.
    private int callHistory;

    public CallHistoryInformationImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public CallHistoryInformationImpl(int callHistory) {
        super();
        this.callHistory = callHistory;
    }

    public CallHistoryInformationImpl() {
        super();

    }

    public void decode(ByteBuf b) throws ParameterException {
        // This one is other way around, as Eduardo might say.
        if (b == null || b.readableBytes() != 2) {
            throw new IllegalArgumentException("buffer must  not be null and length must be 2");
        }

        // this.callHistory = b[0] << 8;
        // this.callHistory |= b[1];
        // //We need this, cause otherwise we get corrupted number
        // this.callHistory &=0xFFFF;
        this.callHistory = ((b.readByte() << 8) | b.readByte()) & 0xFFFF;
    }

    public void encode(ByteBuf buffer) throws ParameterException {

        buffer.writeByte((byte) (this.callHistory >> 8));
        buffer.writeByte((byte) this.callHistory);        
    }

    public int getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(int callHistory) {
        this.callHistory = callHistory;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("CallHistoryInformation [");

        sb.append("callHistory=");
        sb.append(callHistory);

        sb.append("]");
        return sb.toString();
    }
}
