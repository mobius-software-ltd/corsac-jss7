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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CallSegmentToCancel;

/**
 *
 cancel {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT CancelArg {bound} RETURN RESULT FALSE ERRORS {cancelFailed |
 * missingParameter | taskRefused | unknownCSID} CODE opcode-cancel} -- Direction: gsmSCF -> gsmSSF, or gsmSCF -> gsmSRF, Timer:
 * T can -- This operation cancels the correlated previous operation or all previous requests. The following -- operations can
 * be canceled: PlayAnnouncement, PromptAndCollectUserInformation.
 *
 * CancelArg {PARAMETERS-BOUND : bound} ::= CHOICE { invokeID [0] InvokeID, allRequests [1] NULL, callSegmentToCancel [2]
 * CallSegmentToCancel {bound} } -- The InvokeID has the same value as that which was used for the operation to be cancelled.
 *
 * InvokeID ::= TCInvokeIdSet (from TCAP)
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CancelRequest extends CircuitSwitchedCallMessage {

    Integer getInvokeID();

    boolean getAllRequests();

    CallSegmentToCancel getCallSegmentToCancel();

}