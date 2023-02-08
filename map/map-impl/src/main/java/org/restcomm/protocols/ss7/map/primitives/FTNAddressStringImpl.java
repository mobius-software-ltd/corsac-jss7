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

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.commonapp.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;

/**
*
* FTN-AddressString ::= AddressString (SIZE (1..maxFTN-AddressLength)) -- This type is used to represent forwarded-to numbers.
* -- If NAI = international the first digits represent the country code (CC) -- and the network destination code (NDC) as for
* E.164.
*
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/

/**
 *
 * @author sergey vetyutnev
 *
 */

public class FTNAddressStringImpl extends AddressStringImpl implements FTNAddressString {
	public FTNAddressStringImpl() {
		super(14);
    }

    public FTNAddressStringImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(14,addressNature, numberingPlan, address);
    }

    @Override
    public String toString() {
        return "FTNAddressString[AddressNature=" + this.addressNature.toString() + ", NumberingPlan="
                + this.numberingPlan.toString() + ", Address=" + this.address + "]";
    }

}
