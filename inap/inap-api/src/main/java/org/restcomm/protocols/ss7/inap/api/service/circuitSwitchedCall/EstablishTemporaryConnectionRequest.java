/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegID;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ScfID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.RouteList;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;

/**
 *
<code>
EstablishTemporaryConnection ::= OPERATION
ARGUMENT EstablishTemporaryConnectionArg
ERRORS {
	ETCFailed,
	MissingParameter,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF -> SSF, Timer: Tetc
-- This operation is used to create a connection to a resource for a limited period of time (e.g. to
-- play an announcement, to collect user information); it implies the use of the assist procedure.

EstablishTemporaryConnectionArg ::= SEQUENCE {
	assistingSSPIPRoutingAddress [0] AssistingSSPIPRoutingAddress,
	correlationID [1] CorrelationID OPTIONAL,
	scfID [3] ScfID OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	serviceInteractionIndicators [30] ServiceInteractionIndicators OPTIONAL
-- ...
}

--- From Q.1218 CS1
EstablishTemporaryConnectionArg ::= SEQUENCE {
	assistingSSPIPRoutingAddress [0] AssistingSSPIPRoutingAddress,
	correlationID [1] CorrelationID OPTIONAL,
	legID [2] LegID OPTIONAL,
	scfID [3] ScfID OPTIONAL,
	extensions [4] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	carrier [5] Carrier OPTIONAL,
	serviceInteractionIndicators [30] ServiceInteractionIndicators OPTIONAL
-- ...
}

--- From CS1+ Spec
EstablishTemporaryConnectionArg ::= SEQUENCE {
	legID [PRIVATE 01] SendingSideID OPTIONAL,
	‐‐ legID absent indicates CP
	assistingSSPIPRoutingAddress [00] GenericNumber,
	correlationID [01] GenericDigits OPTIONAL,
	sCFID [03] GenericNumber OPTIONAL,
	extensions [04] SEQUENCE SIZE (1..7) OF ExtensionField1 OPTIONAL,
	serviceInteractionIndicators [30] ETCServiceInteractionIndicators OPTIONAL,
‐‐ ...
	routeList [PRIVATE 02] RouteList OPTIONAL
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface EstablishTemporaryConnectionRequest extends CircuitSwitchedCallMessage {

	LegType getPrivateLegID();
    /**
     * Use Digits.getGenericNumber() for AssistingSSPIPRoutingAddress
     *
     * @return
     */
    DigitsIsup getAssistingSSPIPRoutingAddress();

    /**
     * Use Digits.getGenericDigits() for CorrelationID
     *
     * @return
     */
    DigitsIsup getCorrelationID();

    LegID getLegID();
    
    ScfID getScfID();

    CAPINAPExtensions getExtensions();

    Carrier getCarrier();
    
    ServiceInteractionIndicators getServiceInteractionIndicators();
    
    RouteList getRouteList();
}