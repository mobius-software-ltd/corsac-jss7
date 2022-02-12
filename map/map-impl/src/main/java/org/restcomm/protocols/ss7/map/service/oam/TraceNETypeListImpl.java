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

import org.restcomm.protocols.ss7.map.api.service.oam.TraceNETypeList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
*
*/
public class TraceNETypeListImpl extends ASNBitString implements TraceNETypeList {
	static final int _ID_mscS = 0;
    static final int _ID_mgw = 1;
    static final int _ID_sgsn = 2;
    static final int _ID_ggsn = 3;
    static final int _ID_rnc = 4;
    static final int _ID_bmSc = 5;
    static final int _ID_mme = 6;
    static final int _ID_sgw = 7;
    static final int _ID_pgw = 8;
    static final int _ID_eNB = 9;

    public TraceNETypeListImpl() {   
    	super("TraceNETypeList",5,15,false);
    }

    public TraceNETypeListImpl(boolean mscS, boolean mgw, boolean sgsn, boolean ggsn, boolean rnc, boolean bmSc, boolean mme, boolean sgw, boolean pgw,
            boolean enb) {
    	super("TraceNETypeList",5,15,false);
    	if (mscS)
            this.setBit(_ID_mscS);
        if (mgw)
            this.setBit(_ID_mgw);
        if (sgsn)
            this.setBit(_ID_sgsn);
        if (ggsn)
            this.setBit(_ID_ggsn);
        if (rnc)
            this.setBit(_ID_rnc);
        if (bmSc)
            this.setBit(_ID_bmSc);
        if (mme)
            this.setBit(_ID_mme);
        if (sgw)
            this.setBit(_ID_sgw);
        if (pgw)
            this.setBit(_ID_pgw);
        if (enb)
            this.setBit(_ID_eNB);
    }

    public boolean getMscS() {
        return this.isBitSet(_ID_mscS);
    }

    public boolean getMgw() {
        return this.isBitSet(_ID_mgw);
    }

    public boolean getSgsn() {
        return this.isBitSet(_ID_sgsn);
    }

    public boolean getGgsn() {
        return this.isBitSet(_ID_ggsn);
    }

    public boolean getRnc() {
        return this.isBitSet(_ID_rnc);
    }

    public boolean getBmSc() {
        return this.isBitSet(_ID_bmSc);
    }

    public boolean getMme() {
        return this.isBitSet(_ID_mme);
    }

    public boolean getSgw() {
        return this.isBitSet(_ID_sgw);
    }

    public boolean getPgw() {
        return this.isBitSet(_ID_pgw);
    }

    public boolean getEnb() {
        return this.isBitSet(_ID_eNB);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TraceNETypeList [");

        if (this.getMscS()) {
            sb.append("mscS, ");
        }
        if (this.getMgw()) {
            sb.append("mgw, ");
        }
        if (this.getSgsn()) {
            sb.append("sgsn, ");
        }
        if (this.getGgsn()) {
            sb.append("ggsn, ");
        }
        if (this.getRnc()) {
            sb.append("rnc, ");
        }
        if (this.getBmSc()) {
            sb.append("bmSc, ");
        }
        if (this.getMme()) {
            sb.append("mme, ");
        }
        if (this.getSgw()) {
            sb.append("sgw, ");
        }
        if (this.getPgw()) {
            sb.append("pgw, ");
        }
        if (this.getEnb()) {
            sb.append("enb, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
