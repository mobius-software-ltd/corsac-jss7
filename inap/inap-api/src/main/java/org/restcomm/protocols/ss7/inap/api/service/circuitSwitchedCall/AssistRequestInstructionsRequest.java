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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.IPAvailable;

/**
 *
AssistRequestInstructions ::= OPERATION
	ARGUMENT  AssistRequestInstructionsArg
	ERRORS {
		MissingCustomerRecord,
		MissingParameter,
		TaskRefused,
		UnexpectedComponentSequence,
		UnexpectedDataValue,
		UnexpectedParameter
}

-- Direction: SSF -> SCF or SRF -> SCF, Timer: Tari
-- This operation is used when there is an assist or a hand-off procedure and may be sent by the
-- SSF or SRF to the SCF. This operation is sent by the SSF or SRF to the SCF, when the initiating
-- SSF has set up a connection to the SRF or to the assisting SSF as a result of receiving an
-- EstablishTemporaryConnection or Connect (in case of hand-off) operation from the SCF.

AssistRequestInstructionsArg ::= SEQUENCE {
	correlationID [0] CorrelationID,
	iPAvailable [1] IPAvailable OPTIONAL,
	iPSSPCapabilities [2] IPSSPCapabilities OPTIONAL,
	extensions [3] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- OPTIONAL denotes network operator specific use. The value of the correlationID may be the
-- Called Party Number supplied by the initiating SSF.
 *
 * @author yulian.oifa
 *
 */
public interface AssistRequestInstructionsRequest extends CircuitSwitchedCallMessage {

    /**
     * Use Digits.getGenericNumber() for CorrelationID
     *
     * @return
     */
    DigitsIsup getCorrelationID();

    IPAvailable getIPAvailable();
    
    IPSSPCapabilities getIPSSPCapabilities();

    CAPINAPExtensions getExtensions();
}