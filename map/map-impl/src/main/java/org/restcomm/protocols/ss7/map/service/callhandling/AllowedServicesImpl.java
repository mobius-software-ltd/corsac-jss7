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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.map.api.service.callhandling.AllowedServices;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 * AllowedServices ::= BIT STRING { firstServiceAllowed (0), secondServiceAllowed (1) } (SIZE (2..8)) -- firstService is the
 * service indicated in the networkSignalInfo -- secondService is the service indicated in the networkSignalInfo2 -- Other bits
 * than listed above shall be discarded
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
public class AllowedServicesImpl extends ASNBitString implements AllowedServices {
	/**
     *
     */
    public AllowedServicesImpl() {
    	super("AllowedServices",1,7,false);
    }

    public AllowedServicesImpl(boolean suppressCUG, boolean suppressCCBS) {
    	super("AllowedServices",1,7,false);
        if (suppressCUG)
        	super.setBit(0);            
        if (suppressCCBS)
        	super.setBit(1);
    }

    public boolean getFirstServiceAllowed() {
    	return super.isBitSet(0);        
    }

    public boolean getSecondServiceAllowed() {
        return super.isBitSet(1);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AllowedServices [");

        if (getFirstServiceAllowed())
            sb.append("FirstServiceAllowed, ");
        if (getSecondServiceAllowed())
            sb.append("SecondServiceAllowed, ");

        sb.append("]");
        return sb.toString();
    }
}
