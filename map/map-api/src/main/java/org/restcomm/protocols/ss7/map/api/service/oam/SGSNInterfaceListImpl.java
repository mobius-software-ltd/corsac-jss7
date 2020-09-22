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
public class SGSNInterfaceListImpl extends ASNBitString {
	static final int _ID_gb = 0;
    static final int _ID_iu = 1;
    static final int _ID_gn = 2;
    static final int _ID_mapGr = 3;
    static final int _ID_mapGd = 4;
    static final int _ID_mapGf = 5;
    static final int _ID_gs = 6;
    static final int _ID_ge = 7;
    static final int _ID_s3 = 8;
    static final int _ID_s4 = 9;
    static final int _ID_s6d = 10;

    public SGSNInterfaceListImpl() {
    }

    public SGSNInterfaceListImpl(boolean gb, boolean iu, boolean gn, boolean mapGr, boolean mapGd, boolean mapGf, boolean gs, boolean ge, boolean s3,
            boolean s4, boolean s6d) {
        if (gb)
            this.setBit(_ID_gb);
        if (iu)
            this.setBit(_ID_iu);
        if (gn)
            this.setBit(_ID_gn);
        if (mapGr)
            this.setBit(_ID_mapGr);
        if (mapGd)
            this.setBit(_ID_mapGd);
        if (mapGf)
            this.setBit(_ID_mapGf);
        if (gs)
            this.setBit(_ID_gs);
        if (ge)
            this.setBit(_ID_ge);
        if (s3)
            this.setBit(_ID_s3);
        if (s4)
            this.setBit(_ID_s4);
        if (s6d)
            this.setBit(_ID_s6d);
    }

    public boolean getGb() {
        return this.isBitSet(_ID_gb);
    }

    public boolean getIu() {
        return this.isBitSet(_ID_iu);
    }

    public boolean getGn() {
        return this.isBitSet(_ID_gn);
    }

    public boolean getMapGr() {
        return this.isBitSet(_ID_mapGr);
    }

    public boolean getMapGd() {
        return this.isBitSet(_ID_mapGd);
    }

    public boolean getMapGf() {
        return this.isBitSet(_ID_mapGf);
    }

    public boolean getGs() {
        return this.isBitSet(_ID_gs);
    }

    public boolean getGe() {
        return this.isBitSet(_ID_ge);
    }

    public boolean getS3() {
        return this.isBitSet(_ID_s3);
    }

    public boolean getS4() {
        return this.isBitSet(_ID_s4);
    }

    public boolean getS6d() {
        return this.isBitSet(_ID_s6d);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGSNInterfaceList [");

        if (this.getGb()) {
            sb.append("gb, ");
        }
        if (this.getIu()) {
            sb.append("iu, ");
        }
        if (this.getGn()) {
            sb.append("gn, ");
        }
        if (this.getMapGr()) {
            sb.append("mapGr, ");
        }
        if (this.getMapGd()) {
            sb.append("mapGd, ");
        }
        if (this.getMapGf()) {
            sb.append("mapGf, ");
        }
        if (this.getGs()) {
            sb.append("gs, ");
        }
        if (this.getGe()) {
            sb.append("ge, ");
        }
        if (this.getS3()) {
            sb.append("s3, ");
        }
        if (this.getS4()) {
            sb.append("s4, ");
        }
        if (this.getS6d()) {
            sb.append("s6d, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
