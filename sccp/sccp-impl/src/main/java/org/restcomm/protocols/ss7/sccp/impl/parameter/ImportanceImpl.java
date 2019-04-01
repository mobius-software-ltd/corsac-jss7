/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
 *
 */
package org.restcomm.protocols.ss7.sccp.impl.parameter;

import io.netty.buffer.ByteBuf;

import org.restcomm.protocols.ss7.sccp.SccpProtocolVersion;
import org.restcomm.protocols.ss7.sccp.message.ParseException;
import org.restcomm.protocols.ss7.sccp.parameter.Importance;
import org.restcomm.protocols.ss7.sccp.parameter.ParameterFactory;

/**
 * @author baranowb
 *
 */
public class ImportanceImpl extends AbstractParameter implements Importance {
	private static final long serialVersionUID = 1L;

	// default is lowest priority :)
    private byte importance = 0;

    public ImportanceImpl() {
        // TODO Auto-generated constructor stub
    }

    public ImportanceImpl(byte importance) {
        super();
        this.importance = (byte) (importance & 0x07);
    }

    public int getValue() {
        return importance;
    }

    @Override
    public void decode(ByteBuf buffer, final ParameterFactory factory, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        this.importance = (byte) (buffer.readByte() & 0x07);

    }

    @Override
    public void encode(ByteBuf buffer, final boolean removeSpc, final SccpProtocolVersion sccpProtocolVersion) throws ParseException {
        // TODO Auto-generated method stub
    	buffer.writeByte((byte) (importance & 0x07));
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + importance;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImportanceImpl other = (ImportanceImpl) obj;
        if (importance != other.importance)
            return false;
        return true;
    }

}
