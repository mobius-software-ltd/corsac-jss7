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

import org.restcomm.protocols.ss7.map.api.service.oam.ENBInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ENBInterfaceListImpl extends ASNBitString implements ENBInterfaceList {
	static final int _ID_s1Mme = 0;
    static final int _ID_x2 = 1;
    static final int _ID_uu = 2;

    public ENBInterfaceListImpl() {
    	super("ENBInterfaceList",2,7,false);
    }

    public ENBInterfaceListImpl(boolean s1Mme, boolean x2, boolean uu) {
    	super("ENBInterfaceList",2,7,false);
    	if (s1Mme)
            this.setBit(_ID_s1Mme);
        if (x2)
            this.setBit(_ID_x2);
        if (uu)
            this.setBit(_ID_uu);
    }

    public boolean getS1Mme() {
        return this.isBitSet(_ID_s1Mme);
    }

    public boolean getX2() {
        return this.isBitSet(_ID_x2);
    }

    public boolean getUu() {
        return this.isBitSet(_ID_uu);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ENBInterfaceList [");

        if (this.getS1Mme()) {
            sb.append("s1Mme, ");
        }
        if (this.getX2()) {
            sb.append("x2, ");
        }
        if (this.getUu()) {
            sb.append("uu, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
