/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class IPAvailableImpl extends ASNOctetString implements IPAvailable {
	public IPAvailableImpl() {
		super("IPAvailable",null,null,false);
    }

    public IPAvailableImpl(ByteBuf value) {
    	super(value,"IPAvailable",null,null,false);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("IPAvailable [");
        if (getValue() != null) {
            sb.append("data=");
            sb.append(printDataArr());            
        }
        sb.append("]");

        return sb.toString();
    }
}
