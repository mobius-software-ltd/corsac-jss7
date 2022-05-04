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

import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardSuppressionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ForwardSuppressionIndicators;

/**
<code>
SignallingInformation ::= OPERATION
ARGUMENT SignallingInformationArg
ERRORS {
	MissingParameter,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
‐‐ Direction SCF ‐> SSF, Timer: Tsgi
‐‐ Used to send additional signalling related information
‐‐ for the call.

SignallingInformationArg ::= SEQUENCE {
	backwardSuppressIndicators [01] BackwardSuppressionIndicators OPTIONAL,
	connectedNumber [02] Number OPTIONAL,
	forwardSuppressionIndicators [03] ForwardSuppressionIndicators OPTIONAL,
	backwardGVNSIndicator [04] BackwardGVNSIndicator OPTIONAL,
	extensions [05] SEQUENCE SIZE (1..7) OF ExtensionField OPTIONAL
‐‐ ...
</code>
*
 * @author yulian.oifa
 *
 */
public interface SignallingInformationRequest extends CircuitSwitchedCallMessage {

	BackwardSuppressionIndicators getBackwardSuppressionIndicators();

	CalledPartyNumberIsup getConnectedNumber();

    ForwardSuppressionIndicators getForwardSuppressionIndicators();
    
    BackwardGVNS getBackwardGVNSIndicator();
    
    CAPINAPExtensions getExtensions();
}
