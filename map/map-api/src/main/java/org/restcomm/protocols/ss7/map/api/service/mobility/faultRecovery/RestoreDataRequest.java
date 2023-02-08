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

package org.restcomm.protocols.ss7.map.api.service.mobility.faultRecovery;

import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement.VLRCapability;

/**
 * <p>
 * The VLR uses MAP RD when it needs to reload its subscription data. The RD
 * procedure is not triggered by subscriber action, such as registration in MSC.
 * The handling of the RD procedure is similar to the handling of the location
 * update procedure. That is, the VLR indicates to HLR which CAMEL capability is
 * supported in the VLR, so the HLR can decide whether it can send CAMEL data to
 * the VLR for that subscriber
 * </p>
 * <code>

MAP V2-3:

MAP V3: restoreData OPERATION ::= {
--Timer m ARGUMENT RestoreDataArg
RESULT RestoreDataRes
ERRORS { systemFailure | dataMissing | unexpectedDataValue | unknownSubscriber}
CODE local:57 }

MAP V2: RestoreData ::= OPERATION
--Timer m
ARGUMENT restoreDataArg RestoreDataArg
RESULT restoreDataRes RestoreDataRes
ERRORS { SystemFailure, DataMissing, UnexpectedDataValue, UnknownSubscriber}

MAP V3: RestoreDataArg ::= SEQUENCE {
  imsi                     IMSI,
  lmsi                     LMSI OPTIONAL,
  extensionContainer       ExtensionContainer OPTIONAL,
  ... ,
  vlr-Capability           [6] VLR-Capability OPTIONAL,
  restorationIndicator     [7] NULL OPTIONAL
}

MAP V2: RestoreDataArg ::= SEQUENCE {
  imsi IMSI,
  lmsi LMSI OPTIONAL,
  ...
}
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface RestoreDataRequest extends MobilityMessage {

	IMSI getImsi();

	LMSI getLmsi();

    MAPExtensionContainer getExtensionContainer();

    VLRCapability getVLRCapability();

    boolean getRestorationIndicator();

}
