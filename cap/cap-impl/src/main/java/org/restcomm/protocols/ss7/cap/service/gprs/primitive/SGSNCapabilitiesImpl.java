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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.SGSNCapabilities;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNSingleByte;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
public class SGSNCapabilitiesImpl extends ASNSingleByte implements SGSNCapabilities {
	public SGSNCapabilitiesImpl() {
		super("SGSNCapabilities",0,1,false);
    }

    public SGSNCapabilitiesImpl(int data) {
    	super(data,"SGSNCapabilities",0,1,false);
    }

    public SGSNCapabilitiesImpl(boolean aoCSupportedBySGSN) {
    	super(aoCSupportedBySGSN ? 0x01 : 0x00,"SGSNCapabilities",0,1,false);
    }

    public int getData() {
    	if(getValue()==null)
    		return 0;
    	
        return getValue();
    }

    public boolean getAoCSupportedBySGSN() {
        return ((getData() & 0x01) == 0x01);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SGSNCapabilities [");

        if (this.getAoCSupportedBySGSN()) {
            sb.append("AoCSupportedBySGSN ");
        }

        sb.append("]");

        return sb.toString();
    }

}
