/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;

/**
*
* FTN-AddressString ::= AddressString (SIZE (1..maxFTN-AddressLength)) -- This type is used to represent forwarded-to numbers.
* -- If NAI = international the first digits represent the country code (CC) -- and the network destination code (NDC) as for
* E.164.
*
*
* @author sergey vetyutnev
*
*/

/**
 *
 * @author sergey vetyutnev
 *
 */

public class CalledPartyBCDNumberImpl extends AddressStringImpl {
	public CalledPartyBCDNumberImpl() {
		super(41);
    }

    public CalledPartyBCDNumberImpl(AddressNature addressNature, NumberingPlan numberingPlan, String address) {
        super(41,addressNature, numberingPlan, address);
    }

    @Override
    public String toString() {
        return "CalledPartyBCDNumberImpl[AddressNature=" + this.addressNature.toString() + ", NumberingPlan="
                + this.numberingPlan.toString() + ", Address=" + this.address + "]";
    }

}
