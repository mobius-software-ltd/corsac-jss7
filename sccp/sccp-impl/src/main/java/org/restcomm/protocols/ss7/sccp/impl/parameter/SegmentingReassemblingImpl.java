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
package org.restcomm.protocols.ss7.sccp.impl.parameter;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;
import org.restcomm.protocols.ss7.sccp.parameter.SegmentingReassembling;

import io.netty.buffer.ByteBuf;
/**
 * 
 * @author yulianoifa
 *
 */
public class SegmentingReassemblingImpl extends AbstractParameter implements SegmentingReassembling {
	private static final long serialVersionUID = 1L;

	private byte value;

    public SegmentingReassemblingImpl() {
    }

    public SegmentingReassemblingImpl(boolean value) {
        this.value = (value) ? (byte)1 : 0;
    }

    public boolean isMoreData() {
        return value == 1;
    }

    public void setValue(boolean value) {
        this.value = (value) ? (byte)1 : 0;
    }
    
    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        if (buffer.readableBytes() < 1) {
            throw new ParseException();
        }
        this.value = (byte)(buffer.readByte() & 0x01);

    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
    	buffer.writeByte((byte) (this.value & 0x01));
    }
}
