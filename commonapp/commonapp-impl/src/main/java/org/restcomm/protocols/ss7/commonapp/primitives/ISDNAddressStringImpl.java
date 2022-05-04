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

package org.restcomm.protocols.ss7.commonapp.primitives;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;

/**
*
<code>
ISDN-AddressString ::= AddressString (SIZE (1..maxISDN-AddressLength))
-- This type is used to represent ISDN numbers.
</code>
*
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
public class ISDNAddressStringImpl extends AddressStringImpl implements ISDNAddressString {
	public ISDNAddressStringImpl() {
		super(8);
    }

	public ISDNAddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(8,addressNature, numberingPlan, address);
    }

    public ISDNAddressStringImpl(boolean extension, AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(8,extension, addressNature, numberingPlan, address);
    }

    @Override
    public String toString() {
        return "ISDNAddressString[AddressNature=" + this.addressNature + ", NumberingPlan=" + this.numberingPlan + ", Address="
                + this.address + "]";
    }
}
