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

import org.restcomm.protocols.ss7.map.api.service.oam.MMEInterfaceList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class MMEInterfaceListImpl extends ASNBitString implements MMEInterfaceList {
	static final int _ID_s1Mme = 0;
    static final int _ID_s3 = 1;
    static final int _ID_s6a = 2;
    static final int _ID_s10 = 3;
    static final int _ID_s11 = 4;

    public MMEInterfaceListImpl() {
    	super("MMEInterfaceList",4,7,false);
    }

    public MMEInterfaceListImpl(boolean s1Mme, boolean s3, boolean s6a, boolean s10, boolean s11) {
    	super("MMEInterfaceList",4,7,false);
    	if (s1Mme)
            this.setBit(_ID_s1Mme);
        if (s3)
            this.setBit(_ID_s3);
        if (s6a)
            this.setBit(_ID_s6a);
        if (s10)
            this.setBit(_ID_s10);
        if (s11)
            this.setBit(_ID_s11);
    }

    public boolean getS1Mme() {
        return this.isBitSet(_ID_s1Mme);
    }

    public boolean getS3() {
        return this.isBitSet(_ID_s3);
    }

    public boolean getS6a() {
        return this.isBitSet(_ID_s6a);
    }

    public boolean getS10() {
        return this.isBitSet(_ID_s10);
    }

    public boolean getS11() {
        return this.isBitSet(_ID_s11);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MMEInterfaceList [");

        if (this.getS1Mme()) {
            sb.append("s1Mme, ");
        }
        if (this.getS3()) {
            sb.append("s3, ");
        }
        if (this.getS6a()) {
            sb.append("s6a, ");
        }
        if (this.getS10()) {
            sb.append("s10, ");
        }
        if (this.getS11()) {
            sb.append("s11, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
