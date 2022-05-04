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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;

/**
 *
<code>
PromptAndCollectUserInformation ::= OPERATION
ARGUMENT PromptAndCollectUserInformationArg
RESULT ReceivedInformationArg
ERRORS {
	Cancelled,
	ImproperCallerResponse,
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnavailableResource,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SRF, Timer: Tpc -- This operation is used to interact with a user to collect information.

PromptAndCollectUserInformationArg ::= SEQUENCE {
	collectedInfo [0] CollectedInfo,
	disconnectFromIPForbidden [1] BOOLEAN DEFAULT TRUE,
	informationToSend [2] InformationToSend OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}

--- from CS1+ Spec
PromptAndCollectUserInformationArg ::= SEQUENCE {
	legID [PRIVATE 01] SendingSideID OPTIONAL,
	‐‐ legID absent indicates CP
	requestAnnouncementStarted [PRIVATE 02] BOOLEAN DEFAULT FALSE,
	requestAnnouncementComplete [PRIVATE 03] BOOLEAN DEFAULT FALSE,
	collectedInfo [00] CollectedInfo,
	disconnectFromIPForbidden [01] BOOLEAN DEFAULT TRUE,
	informationToSend [02] InformationToSend OPTIONAL,
	extensions [03] SEQUENCE SIZE (1..16) OF ExtensionField1 OPTIONAL
‐‐ ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface PromptAndCollectUserInformationRequest extends CircuitSwitchedCallMessage {

	LegType getLegID();
	
	Boolean getRequestAnnouncementStartedNotification();
	
	Boolean getRequestAnnouncementCompleteNotification();
	
    CollectedInfo getCollectedInfo();

    Boolean getDisconnectFromIPForbidden();

    InformationToSend getInformationToSend();

    CAPINAPExtensions getExtensions();
}