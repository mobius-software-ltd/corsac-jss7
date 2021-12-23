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

import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartyBusinessGroupID;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.CalledPartySubaddress;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.DpSpecificCommonParameters;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;

/**
 *
<code>
<p>
TDisconnect ::= OPERATION
ARGUMENT TDisconnectArg
ERRORS {
	MissingCustomerRecord,
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SSF –> SCF, Timer: Ttd
-- This operation is used for a disconnect indication (e.g. onhook, Q.931 Disconnect message,
-- SS7 Release message) is received from the terminating party, or received from the originating party
-- via the originating half BCSM. (DP 17 – T_Disconnect.) For additional information on this operation,
-- refer to 4.2.2.2.2-10/Q.1214.

TDisconnectArg ::= SEQUENCE {
	dpSpecificCommonParameters [0] DpSpecificCommonParameters,
	calledPartyBusinessGroupID [1] CalledPartyBusinessGroupID OPTIONAL,
	calledPartySubaddress [2] CalledPartySubaddress OPTIONAL,
	calledFacilityGroup [3] FacilityGroup OPTIONAL,
	calledFacilityGroupMember [4] FacilityGroupMember OPTIONAL,
	releaseCause [5] Cause OPTIONAL,
	extensions [6] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	connectTime [7] Integer4 OPTIONAL
-- ...
}
</code>
 *
 * @author yulian.oifa
 *
 */
public interface TDisconnectRequest extends CircuitSwitchedCallMessage {
	DpSpecificCommonParameters getDpSpecificCommonParameters();
	
	CalledPartyBusinessGroupID getCalledPartyBusinessGroupID();
	
	CalledPartySubaddress getCalledPartySubaddress();
	
	FacilityGroup getCalledFacilityGroup();
	
	Integer getCalledFacilityGroupMember();
	
	CauseIsup getReleaseCause();
	
	CAPINAPExtensions getExtensions();
	
	Integer getConnectTime();
}