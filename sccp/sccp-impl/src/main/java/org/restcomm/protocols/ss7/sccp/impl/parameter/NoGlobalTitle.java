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
