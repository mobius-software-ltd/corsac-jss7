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

package org.restcomm.protocols.ss7.map.primitives;

import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;


/**
 *
 * @author daniel bichara
 * @author yulianoifa
 *
 */
public class ISDNSubaddressStringImpl extends ASNOctetString implements ISDNSubaddressString {
	// TODO: implement SubAddress octets and check address format
    /*
     * -- It is composed of -- a) one octet for type of subaddress and odd/even indicator. -- b) 20 octets for subaddress
     * information.
     *
     * -- a) The first octet includes a one bit extension indicator, a -- 3 bits type of subaddress and a one bit odd/even
     * indicator, -- encoded as follows:
     *
     * -- bit 8: 1 (no extension)
     *
     * -- bits 765: type of subaddress -- 000 NSAP (X.213/ISO 8348 AD2) -- 010 User Specified -- All other values are reserved
     *
     * -- bit 4: odd/even indicator -- 0 even number of address signals -- 1 odd number of address signals -- The odd/even
     * indicator is used when the type of subaddress -- is "user specified" and the coding is BCD.
     *
     * -- bits 321: 000 (unused)
     *
     * -- b) Subaddress information. -- The NSAP X.213/ISO8348AD2 address shall be formatted as specified -- by octet 4 which
     * contains the Authority and Format Identifier -- (AFI). The encoding is made according to the "preferred binary --
     * encoding" as defined in X.213/ISO834AD2. For the definition -- of this type of subaddress, see ITU-T Rec I.334.
     *
     * -- For User-specific subaddress, this field is encoded according -- to the user specification, subject to a maximum
     * length of 20 -- octets. When interworking with X.25 networks BCD coding should -- be applied.
     */
    public ISDNSubaddressStringImpl() {
    	super("ISDNSubaddressString",1,21,false);
    }

    public ISDNSubaddressStringImpl(ByteBuf value) {
        super(value,"ISDNSubaddressString",1,21,false);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ISDNSubaddressStringImpl");
        sb.append(" [");
        if (getValue()!=null) {
            sb.append("data=");
            sb.append(printDataArr());            
        }
        sb.append("]");

        return sb.toString();
    }
}
