/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CounterAndValue;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FilteringCriteria;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ResponseCondition;

/**
 *
<code>
<p>
ServiceFilteringResponse ::= OPERATION
ARGUMENT ServiceFilteringResponseArg
-- Direction: SSF -> SCF, Timer: Tsfr
-- This operation is used to send back to the SCF the values of counters specified in a previous
-- ActivateServiceFiltering operation.

ServiceFilteringResponseArg ::= SEQUENCE {
	countersValue [0] CountersValue,
	filteringCriteria [1] FilteringCriteria ,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

--- From Q.1218 CS1
ServiceFilteringResponseArg ::= SEQUENCE {
	countersValue [0] CountersValue,
	filteringCriteria [1] FilteringCriteria,
	extensions [2] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	responseCondition [3] ResponseCondition OPTIONAL
-- ...
}

--- from CS1+ Spec
ServiceFilteringResponseArg ::= SEQUENCE {
	countersValue [00] CountersValue,
	filteringCriteria [01] FilteringCriteria,
	‐‐ ...
	responseCondition [03] ResponseCondition DEFAULT intermediateResponse,
	‐‐ See ITU‐T Rec. Q.1218 Revised.
	sCFCorrelationInfo [PRIVATE 01] OCTET STRING (SIZE(16)) OPTIONAL
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface ServiceFilteringResponseRequest extends CircuitSwitchedCallMessage {
	
	List<CounterAndValue> getCounterAndValue();
	
	FilteringCriteria getFilteringCriteria();
	
	CAPINAPExtensions getExtensions();
	
	ResponseCondition getResponseCondition();
	
	byte[] getSCFCorrelationInfo();
}
