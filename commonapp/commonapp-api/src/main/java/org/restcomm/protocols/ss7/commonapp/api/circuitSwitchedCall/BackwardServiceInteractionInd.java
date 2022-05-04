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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
BackwardServiceInteractionInd ::= SEQUENCE {
  conferenceTreatmentIndicator      [1] OCTET STRING (SIZE(1)) OPTIONAL,
  -- acceptConferenceRequest 'xxxx xx01'B
  -- rejectConferenceRequest 'xxxx xx10'B
  -- if absent from Connect or ContinueWithArgument,
  -- then CAMEL service does not affect conference treatement
  callCompletionTreatmentIndicator  [2] OCTET STRING (SIZE(1)) OPTIONAL,
  -- acceptCallCompletionServiceRequest 'xxxx xx01'B,
  -- rejectCallCompletionServiceRequest 'xxxx xx10'B
  -- if absent from Connect or ContinueWithArgument,
  -- then CAMEL service does not affect call completion treatment
  ...
}
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface BackwardServiceInteractionInd {

    ConferenceTreatmentIndicator getConferenceTreatmentIndicator();

    CallCompletionTreatmentIndicator getCallCompletionTreatmentIndicator();

}