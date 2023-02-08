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

package org.restcomm.protocols.ss7.map.service.mobility.imei;

import org.restcomm.protocols.ss7.map.api.service.mobility.imei.RequestedEquipmentInfo;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * @author normandes
 * @author yulianoifa
 *
 */
public class RequestedEquipmentInfoImpl extends ASNBitString implements RequestedEquipmentInfo {
	private static final int _INDEX_EQUIPMENT_STATUS = 0;
    private static final int _INDEX_BMUEF = 1;

    public RequestedEquipmentInfoImpl() {  
    	super("RequestedEquipmentInfo",1,7,false);
    }

    public RequestedEquipmentInfoImpl(boolean equipmentStatus, boolean bmuef) {
    	super("RequestedEquipmentInfo",1,7,false);
        if (equipmentStatus)
            this.setBit(_INDEX_EQUIPMENT_STATUS);

        if (bmuef)
            this.setBit(_INDEX_BMUEF);
    }

    public boolean getEquipmentStatus() {
        return this.isBitSet(_INDEX_EQUIPMENT_STATUS);
    }

    public boolean getBmuef() {
        return this.isBitSet(_INDEX_BMUEF);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestedEquipmentInfo [");
        if (getEquipmentStatus()) {
            sb.append("EquipmentStatus, ");
        }
        if (getBmuef()) {
            sb.append("bmuef, ");
        }
        sb.append("]");
        return sb.toString();
    }
}
