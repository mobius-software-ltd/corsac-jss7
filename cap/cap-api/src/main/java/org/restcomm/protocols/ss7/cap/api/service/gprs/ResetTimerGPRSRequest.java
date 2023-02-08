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

package org.restcomm.protocols.ss7.cap.api.service.gprs;

import org.restcomm.protocols.ss7.commonapp.api.primitives.TimerID;

/**
 *
 resetTimerGPRS OPERATION ::= { ARGUMENT ResetTimerGPRSArg RETURN RESULT FALSE ERRORS {missingParameter | parameterOutOfRange
 * | taskRefused | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter | unknownPDPID} CODE
 * opcode-resetTimerGPRS} -- Direction: gsmSCF > gprsSSF, Timer: Trtg -- This operation is used to request the gprsSSF to
 * refresh an application timer in the gprsSSF.
 *
 * ResetTimerGPRSArg ::= SEQUENCE { timerID [0] TimerID DEFAULT tssf, timervalue [1] TimerValue, ... }
 *
 * TimerValue ::= Integer4 -- Indicates the timer value (in seconds).
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ResetTimerGPRSRequest extends GprsMessage {

    TimerID getTimerID();

    int getTimerValue();

}