/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
<code>
connectToResource {PARAMETERS-BOUND : bound} OPERATION ::= {
  ARGUMENT ConnectToResourceArg {bound}
  RETURN RESULT FALSE
  ERRORS {missingParameter | systemFailure | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownCSID}
  CODE opcode-connectToResource
}
-- Direction: gsmSCF -> gsmSSF, Timer: T ctr
-- This operation is used to connect a call segment from the gsmSSF to the
-- gsmSRF.

ConnectToResourceArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  resourceAddress CHOICE {
    ipRoutingAddress  [0] IPRoutingAddress {bound},
    none              [3] NULL
  },
  extensions                        [4] Extensions {bound} OPTIONAL,
  serviceInteractionIndicatorsTwo   [7] ServiceInteractionIndicatorsTwo OPTIONAL,
  callSegmentID                     [50] CallSegmentID {bound} OPTIONAL,
  ...
}

IPRoutingAddress {PARAMETERS-BOUND : bound} ::= CalledPartyNumber {bound}
-- Indicates the routing address for the IP.

CallSegmentID {PARAMETERS-BOUND : bound} ::= INTEGER (1..bound.&numOfCSs)
numOfCSs ::= 127
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ConnectToResourceRequest extends CircuitSwitchedCallMessage {

    CalledPartyNumberIsup getResourceAddress_IPRoutingAddress();

    boolean getResourceAddress_Null();

    CAPINAPExtensions getExtensions();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    Integer getCallSegmentID();

}