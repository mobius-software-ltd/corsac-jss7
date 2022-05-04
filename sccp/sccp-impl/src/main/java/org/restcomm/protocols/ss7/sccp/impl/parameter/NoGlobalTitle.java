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

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.indicator.GlobalTitleIndicator;
import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author amit bhayani
 * @author yulianoifa
 *
 */
public class NoGlobalTitle extends AbstractGlobalTitle {
	private static final long serialVersionUID = 1L;

	public NoGlobalTitle() {

    }

    public NoGlobalTitle(String digits) {
        super.digits = digits;
    }

    @Override
    public GlobalTitleIndicator getGlobalTitleIndicator() {
        return GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED;
    }

    @Override
    public String getDigits() {
        return super.digits;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion)
            throws ParseException {
        this.digits = this.encodingScheme.decode(buffer);
    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion)
            throws ParseException {
        if (this.digits == null) {
            throw new IllegalStateException();
        }
        this.encodingScheme.encode(this.digits, buffer);
    }
}
