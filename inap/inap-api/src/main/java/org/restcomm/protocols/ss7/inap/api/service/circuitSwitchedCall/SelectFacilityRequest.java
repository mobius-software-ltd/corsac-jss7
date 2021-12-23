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

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AlertingPatternWrapper;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.FacilityGroup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ISDNAccessRelatedInformation;

/**
 *
<code>
<p>
SelectFacility ::= OPERATION
ARGUMENT SelectFacilityArg
ERRORS {
	MissingParameter,
	ParameterOutOfRange,
	SystemFailure,
	TaskRefused,
	UnexpectedComponentSequence,
	UnexpectedDataValue,
	UnexpectedParameter
}
-- Direction: SCF –> SSF, Timer: Tsf
-- This operation is used to request the SSF to perform the terminating basic call processing
-- actions to select the terminating line if it is idle, or selects an idle line from a multi-line hunt
-- group, or selects an idle trunk from a trunk group, as appropriate. If no idle line or trunk is
-- available, the SSF determines that the terminating facility is busy

SelectFacilityArg ::= SEQUENCE {
	alertingPattern [0] AlertingPattern OPTIONAL,
	destinationNumberRoutingAddress [1] CalledPartyNumber OPTIONAL,
	iSDNAccessRelatedInformation [2] ISDNAccessRelatedInformation OPTIONAL,
	calledFacilityGroup [3] FacilityGroup OPTIONAL,
	calledFacilityGroupMember [4] FacilityGroupMember OPTIONAL,
	originalCalledPartyID [5] OriginalCalledPartyID OPTIONAL,
	extensions [6] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
-- ...
}
-- OPTIONAL parameters are only provided if modifications desired to basic call processing values.
</code>
 *
 * @author yulian.oifa
 *
 */
public interface SelectFacilityRequest extends CircuitSwitchedCallMessage {
	AlertingPatternWrapper getAlertingPattern();
	
	CalledPartyNumberIsup getDestinationNumberRoutingAddress();
	
	ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
	
	FacilityGroup getCalledFacilityGroup();
	
	Integer getCalledFacilityGroupMember();
	
	OriginalCalledNumberIsup getOriginalCalledPartyID();
	
	CAPINAPExtensions getExtensions();						       
}