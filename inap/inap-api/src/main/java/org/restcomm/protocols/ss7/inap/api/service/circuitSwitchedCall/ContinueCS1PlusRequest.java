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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
<code>
Continue ::= OPERATION
-- Direction: SCF -> SSF, Timer: Tcue -- This operation is used to request the SSF to proceed with call processing at the DP at which it
-- previously suspended call processing to await SCF instructions (i.e., proceed to the next point
-- in call in the BCSM). The SSF continues call processing without substituting new data from SCF.

ContinueArg ::= [PRIVATE 01] SEQUENCE {
	legID [01] SendingSideID
‐‐ ...
}
‐‐ The argument is OPTIONAL.
‐‐ No argument indicates CP.
<code>
*
 * @author yulian.oifa
 *
 */
public interface ContinueCS1PlusRequest extends ContinueRequest {
	LegType getLegID();
}