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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.MGWInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class MGWInterfaceListImpl extends ASNBitString implements MGWInterfaceList {
	static final int _ID_mc = 0;
    static final int _ID_nbUp = 1;
    static final int _ID_iuUp = 2;

    public MGWInterfaceListImpl() {
    	super("MGWInterfaceList",2,7,false);
    }

    public MGWInterfaceListImpl(boolean mc, boolean nbUp, boolean iuUp) {
    	super("MGWInterfaceList",2,7,false);
    	if (mc)
            this.setBit(_ID_mc);
        if (nbUp)
            this.setBit(_ID_nbUp);
        if (iuUp)
            this.setBit(_ID_iuUp);
    }

    public boolean getMc() {
        return this.isBitSet(_ID_mc);
    }

    public boolean getNbUp() {
        return this.isBitSet(_ID_nbUp);
    }

    public boolean getIuUp() {
        return this.isBitSet(_ID_iuUp);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MGWInterfaceList [");

        if (this.getMc()) {
            sb.append("mc, ");
        }
        if (this.getNbUp()) {
            sb.append("nbUp, ");
        }
        if (this.getIuUp()) {
            sb.append("iuUp, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
