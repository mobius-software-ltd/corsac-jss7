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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * DeferredLocationEventType ::= BIT STRING { msAvailable (0) , enteringIntoArea (1), leavingFromArea (2), beingInsideArea (3) }
 * (SIZE (1..16)) -- beingInsideArea is always treated as oneTimeEvent regardless of the possible value -- of occurrenceInfo
 * inside areaEventInfo. -- exception handling: -- a ProvideSubscriberLocation-Arg containing other values than listed above in
 * -- DeferredLocationEventType shall be rejected by the receiver with a return error cause of -- unexpected data value.
 *
 * @author amit bhayani
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=3,constructed=false,lengthIndefinite=false)
public interface DeferredLocationEventType {

    boolean getMsAvailable();

    boolean getEnteringIntoArea();

    boolean getLeavingFromArea();

    boolean getBeingInsideArea();

}