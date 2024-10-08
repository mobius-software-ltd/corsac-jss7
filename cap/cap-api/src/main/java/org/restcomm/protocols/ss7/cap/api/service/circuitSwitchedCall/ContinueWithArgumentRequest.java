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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ContinueWithArgumentArgExtension;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.NAOliInfo;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.GenericNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.AlertingPattern;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;

/**
 *
 continueWithArgument {PARAMETERS-BOUND : bound} OPERATION ::= {
   ARGUMENT ContinueWithArgumentArg {bound}
   RETURN RESULT FALSE
   ERRORS {missingParameter | parameterOutOfRange | unexpectedComponentSequence | unexpectedDataValue | unexpectedParameter |
           unknownLegID | unknownCSID} CODE opcode-continueWithArgument}
-- Direction: gsmSCF -> gsmSSF, Timer: T cwa
-- This operation is used to request the gsmSSF to proceed with call processing at the
-- DP at which it previously suspended call processing to await gsmSCF instructions
-- (i.e. proceed to the next point in call in the BCSM). The gsmSSF continues call
-- processing with the modified call setup information as received from the gsmSCF.

ContinueWithArgumentArg {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  alertingPattern                    [1] AlertingPattern OPTIONAL,
  extensions                         [6] Extensions {bound} OPTIONAL,
  serviceInteractionIndicatorsTwo    [7] ServiceInteractionIndicatorsTwo OPTIONAL,
  callingPartysCategory              [12] CallingPartysCategory OPTIONAL,
  genericNumbers                     [16] GenericNumbers {bound} OPTIONAL,
  cug-Interlock                      [17] CUG-Interlock OPTIONAL,
  cug-OutgoingAccess                 [18] NULL OPTIONAL,
  chargeNumber                       [50] ChargeNumber {bound} OPTIONAL,
  carrier                            [52] Carrier {bound} OPTIONAL,
  suppressionOfAnnouncement          [55] SuppressionOfAnnouncement OPTIONAL,
  naOliInfo                          [56] NAOliInfo OPTIONAL,
  bor-InterrogationRequested         [57] NULL OPTIONAL,
  suppress-O-CSI                     [58] NULL OPTIONAL,
  continueWithArgumentArgExtension   [59] ContinueWithArgumentArgExtension {bound} OPTIONAL,
  ...
}

GenericNumbers {PARAMETERS-BOUND : bound} ::= SET SIZE(1..bound.&numOfGenericNumbers) OF GenericNumber {bound}

SuppressionOfAnnouncement ::= NULL

 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface ContinueWithArgumentRequest extends CircuitSwitchedCallMessage {

    AlertingPattern getAlertingPattern();

    CAPINAPExtensions getExtensions();

    ServiceInteractionIndicatorsTwo getServiceInteractionIndicatorsTwo();

    CallingPartysCategoryIsup getCallingPartysCategory();

    List<GenericNumberIsup> getGenericNumbers();

    CUGInterlock getCugInterlock();

    boolean getCugOutgoingAccess();

    LocationNumberIsup getChargeNumber();

    Carrier getCarrier();

    boolean getSuppressionOfAnnouncement();

    NAOliInfo getNaOliInfo();

    boolean getBorInterrogationRequested();

    boolean getSuppressOCsi();

    ContinueWithArgumentArgExtension getContinueWithArgumentArgExtension();

}
