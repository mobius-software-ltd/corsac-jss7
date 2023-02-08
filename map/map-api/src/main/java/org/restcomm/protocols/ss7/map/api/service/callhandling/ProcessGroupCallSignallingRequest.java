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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.AdditionalInfo;

/**
 *
 MAP V3:
 *
 * processGroupCallSignalling OPERATION ::= { --Timer s ARGUMENT ProcessGroupCallSignallingArg CODE local:41 }
 *
 * ProcessGroupCallSignallingArg ::= SEQUENCE { uplinkRequest [0] NULL OPTIONAL, uplinkReleaseIndication [1] NULL OPTIONAL,
 * releaseGroupCall [2] NULL OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ..., talkerPriority [3] TalkerPriority
 * OPTIONAL, additionalInfo [4] AdditionalInfo OPTIONAL, emergencyModeResetCommandFlag [5] NULL OPTIONAL, an-APDU [6]
 * AccessNetworkSignalInfo OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ProcessGroupCallSignallingRequest extends CallHandlingMessage {

     boolean getUplinkRequest();

     boolean getUplinkReleaseIndication();

     boolean getReleaseGroupCall();

     MAPExtensionContainer getExtensionContainer();

     TalkerPriority getTalkerPriority();

     AdditionalInfo getAdditionalInfo();

     boolean getEmergencyModeResetCommandFlag();

     AccessNetworkSignalInfo getAnAPDU();

}
