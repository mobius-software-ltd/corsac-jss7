/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.DateAndTime;

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
