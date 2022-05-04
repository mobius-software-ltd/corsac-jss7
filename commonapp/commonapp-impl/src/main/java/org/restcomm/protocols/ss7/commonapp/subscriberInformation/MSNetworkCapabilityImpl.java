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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSNetworkCapability;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class MSNetworkCapabilityImpl extends ASNOctetString implements MSNetworkCapability {
	public MSNetworkCapabilityImpl() {
		super("MSNetworkCapability",1,8,false);
    }

	public MSNetworkCapabilityImpl(ByteBuf value) {
		super(value,"MSNetworkCapability",1,8,false);
	}
	
    @Override
    public String toString() {
    	StringBuilder sb=new StringBuilder();
        sb.append("MSNetworkCapabilityImpl");
        sb.append(" [");

        if (getValue() != null) {
            sb.append(", data=[");
            sb.append(printDataArr());
            sb.append("]");
        }
        
        sb.append("]");
        return sb.toString();
    }
}
