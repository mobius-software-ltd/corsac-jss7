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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * ForwardingOptions ::= OCTET STRING (SIZE (1)) -- bit 8: notification to forwarding party -- 0 no notification -- 1
 * notification -- bit 7: redirecting presentation -- 0 no presentation -- 1 presentation -- bit 6: notification to calling
 * party -- 0 no notification -- 1 notification -- bit 5: 0 (unused) -- bits 43: forwarding reason -- 00 ms not reachable -- 01
 * ms busy -- 10 no reply -- 11 unconditional when used in a SRI Result, -- or call deflection when used in a RCH Argument --
 * bits 21: 00 (unused)
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface ForwardingOptions {
    int getData();

    boolean isNotificationToForwardingParty();

    boolean isRedirectingPresentation();

    boolean isNotificationToCallingParty();

    ForwardingReason getForwardingReason();
}