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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
InbandInfo {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  messageID            [0] MessageID {bound},
  numberOfRepetitions  [1] INTEGER (1..127) OPTIONAL,
  duration             [2] INTEGER (0..32767) OPTIONAL,
  interval             [3] INTEGER (0..32767) OPTIONAL,
  ...
}
-- Interval is the time in seconds between each repeated announcement. Duration is the total
-- amount of time in seconds, including repetitions and intervals.
-- The end of announcement is either the end of duration or numberOfRepetitions,
-- whatever comes first.
-- duration with value 0 indicates infinite duration
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface InbandInfo {

    MessageID getMessageID();

    Integer getNumberOfRepetitions();

    Integer getDuration();

    Integer getInterval();

}