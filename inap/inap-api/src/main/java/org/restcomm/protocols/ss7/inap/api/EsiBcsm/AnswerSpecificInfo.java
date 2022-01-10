/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.EsiBcsm;

import org.restcomm.protocols.ss7.commonapp.api.isup.BackwardCallIndicatorsIsup;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNSIndicator;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
oAnswerSpecificInfo [5] SEQUENCE {
	-- no specific info defined --
	-- ... --
},
tAnswerSpecificInfo [10] SEQUENCE {
	-- no specific info defined --
	-- ...
}
from cs1+
oAnswerSpecificInfo [05] SEQUENCE {
	‐‐ ...
	timeToAnswer [PRIVATE 01] INTEGER (0..2047) OPTIONAL,
	backwardCallIndicators [PRIVATE 02] BackwardCallIndicators OPTIONAL,
	backwardGVNSIndicator [PRIVATE 03] BackwardGVNSIndicator OPTIONAL
}

tAnswerSpecificInfo [10] SEQUENCE {
	‐‐ ...
	timeToAnswer [PRIVATE 01] INTEGER (0..2047) OPTIONAL,
	backwardCallIndicators [PRIVATE 02] BackwardCallIndicators OPTIONAL,
	backwardGVNSIndicator [PRIVATE 03] BackwardGVNSIndicator OPTIONAL
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface AnswerSpecificInfo {

    Integer getTimeToAnswer();

    BackwardCallIndicatorsIsup getBackwardCallIndicators();

    BackwardGVNSIndicator getBackwardGVNSIndicator();
}
