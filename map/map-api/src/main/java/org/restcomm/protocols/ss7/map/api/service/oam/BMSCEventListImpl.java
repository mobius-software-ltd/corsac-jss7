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

package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
*
*/
public class BMSCEventListImpl extends ASNBitString {
	static final int _ID_mbmsMulticastServiceActivation = 0;

    public BMSCEventListImpl() {
    }

    public BMSCEventListImpl(boolean mbmsMulticastServiceActivation) {
        if (mbmsMulticastServiceActivation)
            this.setBit(_ID_mbmsMulticastServiceActivation);
    }

    public boolean getMbmsMulticastServiceActivation() {
        return this.isBitSet(_ID_mbmsMulticastServiceActivation);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BMSCEventList [");

        if (this.getMbmsMulticastServiceActivation()) {
            sb.append("mbmsMulticastServiceActivation, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
