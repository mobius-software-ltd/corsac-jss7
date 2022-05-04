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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.primitives;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DisplayInformation;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNIA5String;

/**
 *
 *
 * @author yulian.oifa
 *
 */
public class DisplayInformationImpl extends ASNIA5String implements DisplayInformation {
	public DisplayInformationImpl() {
		super("DisplayInformation",null,null,false);
    }

    public DisplayInformationImpl(String value) {
    	super(value,"DisplayInformation",null,null,false);
    }

    public String getString() {
    	return getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("DisplayInformation [");
        String data=getString();
        if (data != null) {
            sb.append(data);            
        }
        sb.append("]");

        return sb.toString();
    }
}
