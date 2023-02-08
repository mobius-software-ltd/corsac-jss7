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

import org.restcomm.protocols.ss7.map.api.service.oam.GGSNInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class GGSNInterfaceListImpl extends ASNBitString implements GGSNInterfaceList {
	static final int _ID_gn = 0;
    static final int _ID_gi = 1;
    static final int _ID_gmb = 2;

    public GGSNInterfaceListImpl() {
    	super("GGSNInterfaceList",2,7,false);
    }

    public GGSNInterfaceListImpl(boolean gn, boolean gi, boolean gmb) {
    	super("GGSNInterfaceList",2,7,false);
    	if (gn)
            this.setBit(_ID_gn);
        if (gi)
            this.setBit(_ID_gi);
        if (gmb)
            this.setBit(_ID_gmb);
    }

    public boolean getGn() {
        return this.isBitSet(_ID_gn);
    }

    public boolean getGi() {
        return this.isBitSet(_ID_gi);
    }

    public boolean getGmb() {
        return this.isBitSet(_ID_gmb);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GGSNInterfaceList [");

        if (this.getGn()) {
            sb.append("gn, ");
        }
        if (this.getGi()) {
            sb.append("gi, ");
        }
        if (this.getGmb()) {
            sb.append("gmb, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
