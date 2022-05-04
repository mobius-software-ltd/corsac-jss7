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
import org.restcomm.protocols.ss7.isup.message.parameter.AutomaticCongestionLevel;

import io.netty.buffer.ByteBuf;

/**
 * Start time:13:42:13 2009-03-30<br>
 * Project: restcomm-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author yulianoifa
 */
public class AutomaticCongestionLevelImpl extends AbstractISUPParameter implements AutomaticCongestionLevel {
	private int automaticCongestionLevel = 0;

    public AutomaticCongestionLevelImpl() {
        super();

    }

    public AutomaticCongestionLevelImpl(ByteBuf b) throws ParameterException {
        super();
        decode(b);
    }

    public void decode(ByteBuf b) throws ParameterException {
        if (b == null || b.readableBytes() != 1) {
            throw new ParameterException("buffer must not be null or have different size than 1");
        }
        this.automaticCongestionLevel = b.readByte();        
    }

    public void encode(ByteBuf b) throws ParameterException {
    	b.writeByte((byte) this.automaticCongestionLevel);
    }

    public int getAutomaticCongestionLevel() {
        return automaticCongestionLevel;
    }

    public void setAutomaticCongestionLevel(int automaticCongestionLevel) {
        this.automaticCongestionLevel = automaticCongestionLevel & 0x01;
    }

    public int getCode() {

        return _PARAMETER_CODE;
    }
}
