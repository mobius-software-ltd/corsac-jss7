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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAAttributes;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.LSAIdentificationPriorityValue;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class LSAAttributesImpl extends ASNSingleByte implements LSAAttributes {
	private static int preferentialAccess_mask = 0x10;
    private static int activeModeSupport_mask = 0x20;
    private static int lsaIdentificationPriority_mask = 0x0F;

    public LSAAttributesImpl() {
    	super("LSAAttributes",0,64,false);
    }
    
    public LSAAttributesImpl(int data) {
    	super(data,"LSAAttributes",0,64,false);
    }

    public LSAAttributesImpl(LSAIdentificationPriorityValue value, boolean preferentialAccessAvailable,
            boolean activeModeSupportAvailable) {
    	super(value.getCode() | (preferentialAccessAvailable ? preferentialAccess_mask : 0)
                | (activeModeSupportAvailable ? activeModeSupport_mask : 0),"LSAAttributes",0,64,false);
    }

    public int getData() {
        return getValue();
    }

    public LSAIdentificationPriorityValue getLSAIdentificationPriority() {
        return LSAIdentificationPriorityValue.getInstance(getData() & lsaIdentificationPriority_mask);
    }

    public boolean isPreferentialAccessAvailable() {
        return ((getData() & preferentialAccess_mask) == preferentialAccess_mask);
    }

    public boolean isActiveModeSupportAvailable() {
        return ((getData() & activeModeSupport_mask) == activeModeSupport_mask);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LSAAttributes");
        sb.append(" [");

        sb.append("  LSAIdentificationPriorityValue=");
        sb.append(this.getLSAIdentificationPriority());

        if (this.isPreferentialAccessAvailable()) {
            sb.append(" , PreferentialAccessAvailable ");
        }

        if (this.isActiveModeSupportAvailable()) {
            sb.append(" , ActiveModeSupportAvailable ");
        }

        sb.append(", Data=");
        sb.append(this.getData());

        sb.append("]");

        return sb.toString();
    }

}
