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

package org.restcomm.protocols.ss7.inap.api.EsiBcsm;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
oMidCallSpecificInfo [6] SEQUENCE {
	connectTime [0] Integer4 OPTIONAL
	-- ... --
},
tMidCallSpecificInfo [11] SEQUENCE {
	connectTime [0] Integer4 OPTIONAL
	-- ... --
},
oMidCallSpecificInfo [06] SEQUENCE {
	‐‐ ...
	midCallEvents [PRIVATE 01] CHOICE {
		flash [00] NULL,
		userCallSuspend [01] NULL,
		userCallResume [02] NULL,
		dTMFDigitsCompleted [03] GenericDigits,
		dTMFDigitsTimeOut [04] GenericDigits
	} OPTIONAL
	‐‐ when empty sequence 'flash' is assumed
}
tMidCallSpecificInfo [11] SEQUENCE {
	‐‐ ...
	midCallEvents [PRIVATE 01] CHOICE {
		flash [00] NULL,
		userCallSuspend [01] NULL,
		userCallResume [02] NULL,
		dTMFDigitsCompleted [03] GenericDigits,
		dTMFDigitsTimeOut [04] GenericDigits
	} OPTIONAL
	‐‐ when empty sequence 'flash' is assumed
},
</code>
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface MidCallSpecificInfo {

	Integer getConnectTime();
	
    MidCallEvents getMidCallEvents();
}