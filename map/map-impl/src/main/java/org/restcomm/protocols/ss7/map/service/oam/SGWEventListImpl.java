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

package org.restcomm.protocols.ss7.map.service.oam;

import org.restcomm.protocols.ss7.map.api.service.oam.SGWEventList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class SGWEventListImpl extends ASNBitString implements SGWEventList {
	static final int _ID_pdnConnectionCreation = 0;
    static final int _ID_pdnConnectionTermination = 1;
    static final int _ID_bearerActivationModificationDeletion = 2;

    public SGWEventListImpl() {
    	super("SGWEventList",2,7,false);
    }

    public SGWEventListImpl(boolean pdnConnectionCreation, boolean pdnConnectionTermination, boolean bearerActivationModificationDeletion) {
    	super("SGWEventList",2,7,false);
    	if (pdnConnectionCreation)
            this.setBit(_ID_pdnConnectionCreation);
        if (pdnConnectionTermination)
            this.setBit(_ID_pdnConnectionTermination);
        if (bearerActivationModificationDeletion)
            this.setBit(_ID_bearerActivationModificationDeletion);
    }

    public boolean getPdnConnectionCreation() {
        return this.isBitSet(_ID_pdnConnectionCreation);
    }

    public boolean getPdnConnectionTermination() {
        return this.isBitSet(_ID_pdnConnectionTermination);
    }

    public boolean getBearerActivationModificationDeletion() {
        return this.isBitSet(_ID_bearerActivationModificationDeletion);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MMEEventList [");

        if (this.getPdnConnectionCreation()) {
            sb.append("pdnConnectionCreation, ");
        }
        if (this.getPdnConnectionTermination()) {
            sb.append("pdnConnectionTermination, ");
        }
        if (this.getBearerActivationModificationDeletion()) {
            sb.append("bearerActivationModificationDeletion, ");
        }

        sb.append("]");
        return sb.toString();
    }

}
