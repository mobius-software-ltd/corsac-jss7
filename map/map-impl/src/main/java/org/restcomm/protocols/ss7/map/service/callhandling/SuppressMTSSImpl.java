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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.SuppressMTSS;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class SuppressMTSSImpl extends ASNBitString implements SuppressMTSS {
	/**
     *
     */
    public SuppressMTSSImpl() {
    	super("SuppressMTSS",1,15,false);
    }

    public SuppressMTSSImpl(boolean suppressCUG, boolean suppressCCBS) {
    	super("SuppressMTSS",1,15,false);
        if (suppressCUG)
            this.setBit(0);
        
        if (suppressCCBS)
            this.setBit(1);
    }

    public boolean getSuppressCUG() {
        return this.isBitSet(0);
    }

    public boolean getSuppressCCBS() {
        return this.isBitSet(1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SuppressMTSS [");

        if (getSuppressCUG())
            sb.append("SuppressCUG, ");
        if (getSuppressCCBS())
            sb.append("SuppressCCBS, ");

        sb.append("]");
        return sb.toString();
    }
}
