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

import org.restcomm.protocols.ss7.map.api.service.oam.PGWEventList;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class PGWEventListImpl extends ASNBitString implements PGWEventList {
	static final int _ID_connectionCreation = 0;
    static final int _ID_connectionTermination = 1;
    static final int _ID_bearerActivationModificationDeletion = 2;
    
    public PGWEventListImpl() { 
    	super("PGWEventList",2,7,false);
    }

    public PGWEventListImpl(boolean connectionCreation, boolean connectionTermination, boolean bearerActivationModificationDeletion) {
    	super("PGWEventList",2,7,false);
    	if (connectionCreation)
            this.setBit(_ID_connectionCreation);
        if (connectionTermination)
            this.setBit(_ID_connectionTermination);
        if (bearerActivationModificationDeletion)
            this.setBit(_ID_bearerActivationModificationDeletion);        
    }

    public boolean getConnectionCreation() {
        return this.isBitSet(_ID_connectionCreation);
    }

    public boolean getConnectionTermination() {
        return this.isBitSet(_ID_connectionTermination);
    }

    public boolean getBearerActivationModificationDeletion() {
        return this.isBitSet(_ID_bearerActivationModificationDeletion);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PGWInterfaceList [");

        if (this.getConnectionCreation()) {
            sb.append("connectionCreation, ");
        }
        if (this.getConnectionTermination()) {
            sb.append("connectionTermination, ");
        }
        if (this.getBearerActivationModificationDeletion()) {
            sb.append("bearerActivationModificationDeletion, ");
        }

        sb.append("]");
        return sb.toString();
    }

}