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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AccessRestrictionDataImpl extends ASNBitString {
	private static final int _INDEX_UtranNotAllowed = 0;
    private static final int _INDEX_GeranNotAllowed = 1;
    private static final int _INDEX_GanNotAllowed = 2;
    private static final int _INDEX_IHspaEvolutionNotAllowed = 3;
    private static final int _INDEX_EUtranNotAllowed = 4;
    private static final int _INDEX_HoToNon3GPPAccessNotAllowed = 5;

    public AccessRestrictionDataImpl() {
    }

    public AccessRestrictionDataImpl(boolean utranNotAllowed, boolean geranNotAllowed, boolean ganNotAllowed,
            boolean iHspaEvolutionNotAllowed, boolean eUtranNotAllowed, boolean hoToNon3GPPAccessNotAllowed) {
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
