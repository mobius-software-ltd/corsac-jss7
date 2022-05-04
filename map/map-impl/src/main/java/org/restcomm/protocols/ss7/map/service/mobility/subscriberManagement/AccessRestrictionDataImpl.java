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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AccessRestrictionData;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class AccessRestrictionDataImpl extends ASNBitString implements AccessRestrictionData {
	private static final int _INDEX_UtranNotAllowed = 0;
    private static final int _INDEX_GeranNotAllowed = 1;
    private static final int _INDEX_GanNotAllowed = 2;
    private static final int _INDEX_IHspaEvolutionNotAllowed = 3;
    private static final int _INDEX_EUtranNotAllowed = 4;
    private static final int _INDEX_HoToNon3GPPAccessNotAllowed = 5;

    public AccessRestrictionDataImpl() {
    	super("AccessRestrictionData",1,7,false);
    }

    public AccessRestrictionDataImpl(boolean utranNotAllowed, boolean geranNotAllowed, boolean ganNotAllowed,
            boolean iHspaEvolutionNotAllowed, boolean eUtranNotAllowed, boolean hoToNon3GPPAccessNotAllowed) {
    	super("AccessRestrictionData",1,7,false);
        if (utranNotAllowed)
            this.setBit(_INDEX_UtranNotAllowed);
        if (geranNotAllowed)
            this.setBit(_INDEX_GeranNotAllowed);
        if (ganNotAllowed)
            this.setBit(_INDEX_GanNotAllowed);
        if (iHspaEvolutionNotAllowed)
            this.setBit(_INDEX_IHspaEvolutionNotAllowed);
        if (eUtranNotAllowed)
            this.setBit(_INDEX_EUtranNotAllowed);
        if (hoToNon3GPPAccessNotAllowed)
            this.setBit(_INDEX_HoToNon3GPPAccessNotAllowed);

    }

    public boolean getUtranNotAllowed() {
        return this.isBitSet(_INDEX_UtranNotAllowed);
    }

    public boolean getGeranNotAllowed() {
        return this.isBitSet(_INDEX_GeranNotAllowed);
    }

    public boolean getGanNotAllowed() {
        return this.isBitSet(_INDEX_GanNotAllowed);
    }

    public boolean getIHspaEvolutionNotAllowed() {
        return this.isBitSet(_INDEX_IHspaEvolutionNotAllowed);
    }

    public boolean getEUtranNotAllowed() {
        return this.isBitSet(_INDEX_EUtranNotAllowed);
    }

    public boolean getHoToNon3GPPAccessNotAllowed() {
        return this.isBitSet(_INDEX_HoToNon3GPPAccessNotAllowed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessRestrictionData [");
        if (this.getUtranNotAllowed())
            sb.append("UtranNotAllowed, ");
        if (this.getGeranNotAllowed())
            sb.append("GeranNotAllowed, ");
        if (this.getGanNotAllowed())
            sb.append("GanNotAllowed, ");
        if (this.getIHspaEvolutionNotAllowed())
            sb.append("IHspaEvolutionNotAllowed, ");
        if (this.getEUtranNotAllowed())
            sb.append("EUtranNotAllowed, ");
        if (this.getHoToNon3GPPAccessNotAllowed())
            sb.append("HoToNon3GPPAccessNotAllowed");
        sb.append("]");
        return sb.toString();
    }

}
