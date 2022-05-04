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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
<code>
PlayAnnouncement ::= OPERATION
ARGUMENT PlayAnnouncementArg
ERRORS {
	Cancelled,
	MissingParameter,
	SystemFailure,
	UnavailableResource,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
LINKED {
	SpecializedResourceReport
}
-- Direction: SCF -> SRF, Timer: Tpa -- This operation is to be used after Establish Temporary Connection (assist procedure with a
-- second SSP) or a Connect to Resource (no assist) operation. It may be used for inband
-- interaction with an analogue user, or for interaction with an ISDN user. In the former case, the SRF
-- is usually collocated with the SSF for standard tones (congestion tone etc.) or standard
-- announcements. In the latter case, the SRF is always collocated with the SSF in the switch. Any
-- error is returned to the SCF. The timer associated with this operation must be of a sufficient
-- duration to allow its linked operation to be correctly correlated.

PlayAnnouncementArg ::= SEQUENCE {
	informationToSend [0] InformationToSend,
	disconnectFromIPForbidden [1] BOOLEAN DEFAULT TRUE,
	requestAnnouncementComplete [2] BOOLEAN DEFAULT TRUE,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField
OPTIONAL
-- ...
}

--- from CS1+ Spec
PlayAnnouncementArg ::= SEQUENCE {
	legID [PRIVATE 01] SendingSideID OPTIONAL,
	‐‐ legID absent indicates CP
	requestAnnouncementStarted [PRIVATE 02] BOOLEAN DEFAULT FALSE,
	informationToSend [00] InformationToSend,
	disconnectFromIPForbidden [01] BOOLEAN DEFAULT TRUE,
	requestAnnouncementComplete [02] BOOLEAN DEFAULT TRUE,
	extensions [03] SEQUENCE SIZE (1..16) OF ExtensionField1 OPTIONAL
‐‐ ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface PlayAnnouncementRequest extends CircuitSwitchedCallMessage {

	LegType getLegID();
	
	Boolean getRequestAnnouncementStarted();
	
    InformationToSend getInformationToSend();

    Boolean getDisconnectFromIPForbidden();

    Boolean getRequestAnnouncementCompleteNotification();

    CAPINAPExtensions getExtensions();
}