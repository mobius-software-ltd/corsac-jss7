/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.subscriberInformation;

import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MSClassmark2Impl extends ASNOctetString implements MSClassmark2 {
	public MSClassmark2Impl() {
		super("MSClassmark2",3,3,false);
    }

    public MSClassmark2Impl(ByteBuf value) {
    	super(value,"MSClassmark2",3,3,false);
    }
	
    @Override
    public String toString() {
    	StringBuilder sb=new StringBuilder();
        sb.append("MSClassmark2Impl");
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
