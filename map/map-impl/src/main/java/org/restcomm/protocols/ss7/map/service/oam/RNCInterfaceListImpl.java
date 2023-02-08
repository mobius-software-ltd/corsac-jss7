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

import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class RNCInterfaceListImpl extends ASNBitString implements RNCInterfaceList {
	static final int _ID_iu = 0;
    static final int _ID_iur = 1;
    static final int _ID_iub = 2;
    static final int _ID_uu = 3;

    public RNCInterfaceListImpl() {
    	super("RNCInterfaceList",3,7,false);
    }

    public RNCInterfaceListImpl(boolean iu, boolean iur, boolean iub, boolean uu) {
    	super("RNCInterfaceList",3,7,false);
    	if (iu)
            this.setBit(_ID_iu);
        if (iur)
            this.setBit(_ID_iur);
        if (iub)
            this.setBit(_ID_iub);
        if (uu)
            this.setBit(_ID_uu);
    }

    public boolean getIu() {
        return this.isBitSet(_ID_iu);
    }

    public boolean getIur() {
        return this.isBitSet(_ID_iur);
    }

    public boolean getIub() {
        return this.isBitSet(_ID_iub);
    }

    public boolean getUu() {
        return this.isBitSet(_ID_uu);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RNCInterfaceList [");

        if (this.getIu()) {
            sb.append("iu, ");
        }
        if (this.getIur()) {
            sb.append("iur, ");
        }
        if (this.getIub()) {
            sb.append("iub, ");
        }
        if (this.getUu()) {
            sb.append("uu, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
