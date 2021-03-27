/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.api.primitives;

/**
*
<code>
ISDN-AddressString ::= AddressString (SIZE (1..maxISDN-AddressLength))
-- This type is used to represent ISDN numbers.
</code>
*
*
* @author sergey vetyutnev
*
*/
public class ISDNAddressStringImpl extends AddressStringImpl {
	private static final long serialVersionUID = 1L;

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
