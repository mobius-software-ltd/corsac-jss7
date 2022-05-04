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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
<code>
BackwardGVNSIndicator ::= OCTET STRING (SIZE(1))
‐‐ Bit Assignment
‐‐ H G F E D C B A
‐‐ 1 x x x x x 0 0 Term.Acc.Ind.: no information
‐‐ 1 x x x x x 0 1 Term.Acc.Ind.: dedicated terminating access
‐‐ 1 x x x x x 1 0 Term.Acc.Ind.: switched terminating access
‐‐ 1 x x x x x 1 1 Term.Acc.Ind.: spare
</code>

*
* @author yulian.oifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface BackwardGVNSIndicator {
	BackwardGVNS getBackwardGVNS();
}