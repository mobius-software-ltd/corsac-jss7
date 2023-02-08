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

import org.restcomm.protocols.ss7.map.api.service.oam.SGWInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class SGWInterfaceListImpl extends ASNBitString implements SGWInterfaceList {
	static final int _ID_s4 = 0;
    static final int _ID_s5 = 1;
    static final int _ID_s8b = 2;
    static final int _ID_s11 = 3;
    static final int _ID_gxc = 4;

    public SGWInterfaceListImpl() {
    	super("SGWInterfaceList",4,7,false);
    }

    public SGWInterfaceListImpl(boolean s4, boolean s5, boolean s8b, boolean s11, boolean gxc) {
    	super("SGWInterfaceList",4,7,false);
    	if (s4)
            this.setBit(_ID_s4);
        if (s5)
            this.setBit(_ID_s5);
        if (s8b)
            this.setBit(_ID_s8b);
        if (s11)
            this.setBit(_ID_s11);
        if (gxc)
            this.setBit(_ID_gxc);
    }

    public boolean getS4() {
        return this.isBitSet(_ID_s4);
    }

    public boolean getS5() {
        return this.isBitSet(_ID_s5);
    }

    public boolean getS8b() {
        return this.isBitSet(_ID_s8b);
    }

    public boolean getS11() {
        return this.isBitSet(_ID_s11);
    }

    public boolean getGxc() {
        return this.isBitSet(_ID_gxc);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGWInterfaceList [");

        if (this.getS4()) {
            sb.append("s4, ");
        }
        if (this.getS5()) {
            sb.append("s5, ");
        }
        if (this.getS8b()) {
            sb.append("s8b, ");
        }
        if (this.getS11()) {
            sb.append("s11, ");
        }
        if (this.getGxc()) {
            sb.append("gxc, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
