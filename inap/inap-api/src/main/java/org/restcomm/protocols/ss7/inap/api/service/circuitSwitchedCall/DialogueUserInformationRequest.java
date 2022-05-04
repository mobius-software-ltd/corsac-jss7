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

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.DialogueUserInformation;

/**
 *
DialogueUserInformation ::= OPERATION
ARGUMENT DialogueUserInformationArg
ERRORS {
	UnexpectedDataValue
}
‐‐ Direction SSF ‐> SCF, SCF ‐> SSF, SCF ‐> SRF, SCF ‐> SDF,
‐‐ Timer: Tdui
‐‐ Used to indicate Test and Trace function to be applied
‐‐ on the dialogue. When send it shall be the first
‐‐ operation in a blue TCAP BEGIN message.
‐‐ With white TCAP, this information is transferred in the
‐‐ TCAP Dialogue Portion Field.

DialogueUserInformationArg ::= SEQUENCE {
	sendingFunctionsActive [00] ENUMERATED {
		normal (1),
		simulation (2),
		messageLogging (3),
		simulationPrettyPrint (4),
		messageLoggingPrettyPrint (5),
		serviceLogicTrace (6), ‐‐ from SCF only
		logAndTrace (7), ‐‐ from SCF only
		loggingPrettyPrintAndTrace (8) ‐‐ from SCF only
	} DEFAULT normal,
	receivingFunctionsRequested [01] ENUMERATED {
		normal (1),
		simulation (2),
		messageLogging (3),
		simulationPrettyPrint (4),
		messageLoggingPrettyPrint (5),
		serviceLogicTrace (6), ‐‐ towards SCF only
		logAndTrace (7), ‐‐ towards SCF only
		loggingPrettyPrintAndTrace (8) ‐‐ towards SCF only
	} DEFAULT normal,
	trafficSimulationSessionID [02] INTEGER (0..65535) OPTIONAL
	‐‐ ...
}
 *
 * @author yulian.oifa
 *
 */
public interface DialogueUserInformationRequest extends CircuitSwitchedCallMessage,DialogueUserInformation {
}