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

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class AdditionalSubscriptionsImpl extends ASNBitString implements AdditionalSubscriptions {
	private static final int _INDEX_PrivilegedUplinkRequest = 0;
    private static final int _INDEX_EmergencyUplinkRequest = 1;
    private static final int _INDEX_EmergencyReset = 2;

    public AdditionalSubscriptionsImpl() { 
    	super("AdditionalSubscriptions",2,7,false);
    }

    public AdditionalSubscriptionsImpl(boolean privilegedUplinkRequest, boolean emergencyUplinkRequest, boolean emergencyReset) {
    	super("AdditionalSubscriptions",2,7,false);
        if (privilegedUplinkRequest)
            this.setBit(_INDEX_PrivilegedUplinkRequest);
        if (emergencyUplinkRequest)
            this.setBit(_INDEX_EmergencyUplinkRequest);
        if (emergencyReset)
            this.setBit(_INDEX_EmergencyReset);
    }

    public boolean getPrivilegedUplinkRequest() {
        return this.isBitSet(_INDEX_PrivilegedUplinkRequest);
    }

    public boolean getEmergencyUplinkRequest() {
        return this.isBitSet(_INDEX_EmergencyUplinkRequest);
    }

    public boolean getEmergencyReset() {
        return this.isBitSet(_INDEX_EmergencyReset);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AdditionalSubscriptions [");
        if (this.getPrivilegedUplinkRequest())
            sb.append("PrivilegedUplinkRequest, ");
        if (this.getEmergencyUplinkRequest())
            sb.append("EmergencyUplinkRequest, ");
        if (this.getEmergencyReset())
            sb.append("EmergencyReset ");
        sb.append("]");
        return sb.toString();
    }

}
