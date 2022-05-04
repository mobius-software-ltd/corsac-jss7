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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.BMSCInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class BMSCInterfaceListImpl extends ASNBitString implements BMSCInterfaceList {
	static final int _ID_gmb = 0;

    public BMSCInterfaceListImpl() { 
    	super("BMSCInterfaceList",0,7,false);
    }

    public BMSCInterfaceListImpl(boolean gmb) {
    	super("BMSCInterfaceList",0,7,false);
    	if (gmb)
            this.setBit(_ID_gmb);
    }

    public boolean getGmb() {
        return this.isBitSet(_ID_gmb);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BMSCInterfaceList [");

        if (this.getGmb()) {
            sb.append("gmb, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
