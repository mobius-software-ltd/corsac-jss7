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

package org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall;

import java.util.List;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
MessageID {PARAMETERS-BOUND : bound} ::= CHOICE {
  elementaryMessageID   [0] Integer4,
  text                  [1] SEQUENCE {
    messageContent [0] IA5String (SIZE(bound.&minMessageContentLength .. bound.&maxMessageContentLength)),
    attributes     [1] OCTET STRING (SIZE(bound.&minAttributesLength .. bound.&maxAttributesLength)) OPTIONAL
  },
  elementaryMessageIDs  [29] SEQUENCE SIZE (1..bound.&numOfMessageIDs) OF Integer4,
  variableMessage       [30] SEQUENCE {
    elementaryMessageID  [0] Integer4,
    variableParts        [1] SEQUENCE SIZE (1..5) OF VariablePart {bound}
  }
}
-- Use of the text parameter is network operator/equipment vendor specific.
numOfMessageIDs ::= 16
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface MessageID {

    Integer getElementaryMessageID();

    MessageIDText getText();

    List<Integer> getElementaryMessageIDs();

    VariableMessage getVariableMessage();
}