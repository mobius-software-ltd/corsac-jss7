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
package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalSubscriptions;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
public class AdditionalSubscriptionsImpl extends ASNBitString implements AdditionalSubscriptions {
	private static final int _INDEX_PrivilegedUplinkRequest = 0;
    private static final int _INDEX_EmergencyUplinkRequest = 1;
    private static final int _INDEX_EmergencyReset = 2;

    public AdditionalSubscriptionsImpl() {        
    }

    public AdditionalSubscriptionsImpl(boolean privilegedUplinkRequest, boolean emergencyUplinkRequest, boolean emergencyReset) {
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
