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

package org.restcomm.protocols.ss7.map.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.SupportedRATTypes;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public class SupportedRATTypesImpl extends ASNBitString implements SupportedRATTypes {
	private static final int _INDEX_utran = 0;
    private static final int _INDEX_geran = 1;
    private static final int _INDEX_gan = 2;
    private static final int _INDEX_i_hspa_evolution = 3;
    private static final int _INDEX_e_utran = 4;

    public SupportedRATTypesImpl() {
    	super("SupportedRATTypes",1,7,false);
    }

    public SupportedRATTypesImpl(boolean utran, boolean geran, boolean gan, boolean i_hspa_evolution, boolean e_utran) {
    	super("SupportedRATTypes",1,7,false);
        if (utran)
            this.setBit(_INDEX_utran);
        if (geran)
            this.setBit(_INDEX_geran);
        if (gan)
            this.setBit(_INDEX_gan);
        if (i_hspa_evolution)
            this.setBit(_INDEX_i_hspa_evolution);
        if (e_utran)
            this.setBit(_INDEX_e_utran);
    }

    public boolean getUtran() {
        return this.isBitSet(_INDEX_utran);
    }

    public boolean getGeran() {
        return this.isBitSet(_INDEX_geran);
    }

    public boolean getGan() {
        return this.isBitSet(_INDEX_gan);
    }

    public boolean getIHspaEvolution() {
        return this.isBitSet(_INDEX_i_hspa_evolution);
    }

    public boolean getEUtran() {
        return this.isBitSet(_INDEX_e_utran);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SupportedRATTypes [");

        if (getUtran())
            sb.append("utran, ");
        if (getGeran())
            sb.append("geran, ");
        if (getGan())
            sb.append("gan, ");
        if (getIHspaEvolution())
            sb.append("i_hspa_evolution, ");
        if (getEUtran())
            sb.append("e_utran, ");

        sb.append("]");

        return sb.toString();
    }
}
