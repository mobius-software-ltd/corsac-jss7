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

import org.restcomm.protocols.ss7.map.api.service.oam.RNCInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
*
*/
public class RNCInterfaceListImpl extends ASNBitString implements RNCInterfaceList {
	static final int _ID_iu = 0;
    static final int _ID_iur = 1;
    static final int _ID_iub = 2;
    static final int _ID_uu = 3;

    public RNCInterfaceListImpl() {
    }

    public RNCInterfaceListImpl(boolean iu, boolean iur, boolean iub, boolean uu) {
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
