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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
FilteringCriteria ::= CHOICE {
	serviceKey [2] ServiceKey,
	addressAndService [30] SEQUENCE {
		calledAddressValue [0] Digits,
		serviceKey [1] ServiceKey,
		callingAddressValue [2] Digits OPTIONAL,
		locationNumber [3] LocationNumber OPTIONAL
	}
}

-- In case calledAddressValue is specified, the numbers to be filtered are from calledAddressValue up to
-- and including calledAddressValue +maximumNumberOfCounters-1. The last two digits of calledAddressValue
-- cannot exceed 100-maximumNumberOfCounters.
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface FilteringCriteria {
	Integer getServiceKey();

	AddressAndService getAddressAndService();
}