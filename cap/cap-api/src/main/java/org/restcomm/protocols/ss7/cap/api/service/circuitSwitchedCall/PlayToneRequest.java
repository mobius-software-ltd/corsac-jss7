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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LegOrCallSegment;
import org.restcomm.protocols.ss7.commonapp.api.primitives.Burst;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

/**
 *
 playTone {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT PlayToneArg {bound} RETURN RESULT FALSE ERRORS {missingParameter
 * | parameterOutOfRange | systemFailure | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter |
 * unknownLegID | unknownCSID} CODE opcode-playTone} -- Direction: gsmSCF -> gsmSSF, Timer: Tpt -- This operation is used to
 * play tones to either a leg or a call segment using -- the MSC's tone generator.
 *
 * PlayToneArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { legOrCallSegment [0] LegOrCallSegment {bound}, bursts [1] Burst,
 * extensions [2] Extensions {bound} OPTIONAL, ... }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface PlayToneRequest extends CircuitSwitchedCallMessage {

    LegOrCallSegment getLegOrCallSegment();

    Burst getBursts();

    CAPINAPExtensions getExtensions();

}