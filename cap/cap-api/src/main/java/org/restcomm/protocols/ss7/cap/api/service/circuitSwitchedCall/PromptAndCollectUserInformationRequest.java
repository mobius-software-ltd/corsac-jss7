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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CollectedInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.InformationToSend;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
<code>
promptAndCollectUserInformation {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT PromptAndCollectUserInformationArg {bound}
  RESULT ReceivedInformationArg {bound}
  ERRORS {canceled | improperCallerResponse | missingParameter | parameterOutOfRange | systemFailure | taskRefused |
          unexpectedComponentSequence | unavailableResource | unexpectedDataValue | unexpectedParameter | unknownCSID}
  LINKED {specializedResourceReport} CODE opcode-promptAndCollectUserInformation
}
  -- Direction: gsmSCF -> gsmSRF, Timer: T pc
  -- This operation is used to interact with a user to collect information.

PromptAndCollectUserInformationArg {PARAMETERS-BOUND : bound}::= SEQUENCE {
  collectedInfo              [0] CollectedInfo,
  disconnectFromIPForbidden  [1] BOOLEAN DEFAULT TRUE,
  informationToSend          [2] InformationToSend {bound} OPTIONAL,
  extensions                 [3] Extensions {bound} OPTIONAL,
  callSegmentID              [4] CallSegmentID {bound} OPTIONAL,
  requestAnnouncementStartedNotification [51] BOOLEAN DEFAULT FALSE,
  ...
}

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs)
numOfCSs ::= 127
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PromptAndCollectUserInformationRequest extends CircuitSwitchedCallMessage {

    CollectedInfo getCollectedInfo();

    Boolean getDisconnectFromIPForbidden();

    InformationToSend getInformationToSend();

    CAPINAPExtensions getExtensions();

    Integer getCallSegmentID();

    Boolean getRequestAnnouncementStartedNotification();

}
