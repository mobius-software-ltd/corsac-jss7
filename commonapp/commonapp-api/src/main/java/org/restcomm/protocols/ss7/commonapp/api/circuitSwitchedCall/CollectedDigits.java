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

import org.restcomm.protocols.ss7.commonapp.api.primitives.ErrorTreatment;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

import io.netty.buffer.ByteBuf;

/**
 *
<code>
CollectedDigits ::= SEQUENCE {
  minimumNbOfDigits   [0] INTEGER (1..30) DEFAULT 1,
  maximumNbOfDigits   [1] INTEGER (1..30),
  endOfReplyDigit     [2] OCTET STRING (SIZE (1..2)) OPTIONAL,
  cancelDigit         [3] OCTET STRING (SIZE (1..2)) OPTIONAL,
  startDigit          [4] OCTET STRING (SIZE (1..2)) OPTIONAL,
  firstDigitTimeOut   [5] INTEGER (1..127) OPTIONAL,
  interDigitTimeOut   [6] INTEGER (1..127) OPTIONAL,
  errorTreatment      [7] ErrorTreatment DEFAULT stdErrorAndInfo,
  interruptableAnnInd [8] BOOLEAN DEFAULT TRUE,
  voiceInformation    [9] BOOLEAN DEFAULT FALSE,
  voiceBack           [10] BOOLEAN DEFAULT FALSE
}
-- The use of voiceBack and the support of voice recognition via voiceInformation
-- is network operator specific.
-- The endOfReplyDigit, cancelDigit, and startDigit parameters have been
-- designated as OCTET STRING, and are to be encoded as BCD, one digit per octet
-- only, contained in the four least significant bits of each OCTET. The following encoding shall
-- be applied for the non-decimal characters:
-- 1011 (*), 1100 (#).
-- The usage is service dependent.
-- firstDigitTimeOut and interDigitTimeOut are measured in seconds.
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface CollectedDigits {

    Integer getMinimumNbOfDigits();

    int getMaximumNbOfDigits();

    ByteBuf getEndOfReplyDigit();

    ByteBuf getCancelDigit();

    ByteBuf getStartDigit();

    Integer getFirstDigitTimeOut();

    Integer getInterDigitTimeOut();

    ErrorTreatment getErrorTreatment();

    Boolean getInterruptableAnnInd();

    Boolean getVoiceInformation();

    Boolean getVoiceBack();
}