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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.cap.api.primitives.DateAndTime;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.RequestedInformationType;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 RequestedInformationTypeList ::= SEQUENCE SIZE (1.. numOfInfoItems) OF RequestedInformationType
 *
 * RequestedInformation {PARAMETERS-BOUND : bound} ::= SEQUENCE { requestedInformationType [0] RequestedInformationType,
 * requestedInformationValue [1] RequestedInformationValue {bound}, ... }
 *
 * RequestedInformationValue {PARAMETERS-BOUND : bound} ::= CHOICE { callAttemptElapsedTimeValue [0] INTEGER (0..255),
 * callStopTimeValue [1] DateAndTime, callConnectedElapsedTimeValue [2] Integer4, releaseCauseValue [30] Cause {bound} } -- The
 * callAttemptElapsedTimeValue is specified in seconds. The unit for the -- callConnectedElapsedTimeValue is 100 milliseconds
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface RequestedInformation {

    RequestedInformationType getRequestedInformationType();

    Integer getCallAttemptElapsedTimeValue();

    DateAndTime getCallStopTimeValue();

    Integer getCallConnectedElapsedTimeValue();

    CauseIsup getReleaseCauseValue();

}
