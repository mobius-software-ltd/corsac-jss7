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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.MSCSInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
*
*/
public class MSCSInterfaceListImpl extends ASNBitString implements MSCSInterfaceList {
	static final int _ID_a = 0;
    static final int _ID_iu = 1;
    static final int _ID_mc = 2;
    static final int _ID_mapG = 3;
    static final int _ID_mapB = 4;
    static final int _ID_mapE = 5;
    static final int _ID_mapF = 6;
    static final int _ID_cap = 7;
    static final int _ID_mapD = 8;
    static final int _ID_mapC = 9;

    public MSCSInterfaceListImpl() {        
    }

    public MSCSInterfaceListImpl(boolean a, boolean iu, boolean mc, boolean mapG, boolean mapB, boolean mapE, boolean mapF, boolean cap, boolean mapD,
            boolean mapC) {
        if (a)
            this.setBit(_ID_a);
        if (iu)
            this.setBit(_ID_iu);
        if (mc)
            this.setBit(_ID_mc);
        if (mapG)
            this.setBit(_ID_mapG);
        if (mapB)
            this.setBit(_ID_mapB);
        if (mapE)
            this.setBit(_ID_mapE);
        if (mapF)
            this.setBit(_ID_mapF);
        if (cap)
            this.setBit(_ID_cap);
        if (mapD)
            this.setBit(_ID_mapD);
        if (mapC)
            this.setBit(_ID_mapC);
    }

    public boolean getA() {
        return this.isBitSet(_ID_a);
    }

    public boolean getIu() {
        return this.isBitSet(_ID_iu);
    }

    public boolean getMc() {
        return this.isBitSet(_ID_mc);
    }

    public boolean getMapG() {
        return this.isBitSet(_ID_mapG);
    }

    public boolean getMapB() {
        return this.isBitSet(_ID_mapB);
    }

    public boolean getMapE() {
        return this.isBitSet(_ID_mapE);
    }

    public boolean getMapF() {
        return this.isBitSet(_ID_mapF);
    }

    public boolean getCap() {
        return this.isBitSet(_ID_cap);
    }

    public boolean getMapD() {
        return this.isBitSet(_ID_mapD);
    }

    public boolean getMapC() {
        return this.isBitSet(_ID_mapC);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MSCSInterfaceList [");

        if (this.getA()) {
            sb.append("a, ");
        }
        if (this.getIu()) {
            sb.append("iu, ");
        }
        if (this.getMc()) {
            sb.append("mc, ");
        }
        if (this.getMapG()) {
            sb.append("mapG, ");
        }
        if (this.getMapB()) {
            sb.append("mapB, ");
        }
        if (this.getMapE()) {
            sb.append("mapE, ");
        }
        if (this.getMapF()) {
            sb.append("mapF, ");
        }
        if (this.getCap()) {
            sb.append("cap, ");
        }
        if (this.getMapD()) {
            sb.append("mapD, ");
        }
        if (this.getMapC()) {
            sb.append("mapC, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
