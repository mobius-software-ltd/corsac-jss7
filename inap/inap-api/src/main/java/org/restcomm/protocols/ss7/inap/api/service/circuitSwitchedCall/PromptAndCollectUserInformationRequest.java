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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

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
</code>
 *
 * @author yulian.oifa
 *
 */
public interface PromptAndCollectUserInformationRequest extends CircuitSwitchedCallMessage {

    CollectedInfo getCollectedInfo();

    Boolean getDisconnectFromIPForbidden();

    InformationToSend getInformationToSend();

    CAPINAPExtensions getExtensions();
}