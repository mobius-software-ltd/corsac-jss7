/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.primitives.DateAndTime;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
TariffInformation ::= SEQUENCE {
	numberOfStartPulses [00] INTEGER (0..255),
	startInterval [01] INTEGER (0..3276),
	startIntervalAccuracy [02] ENUMERATED {
		tenMilliSeconds (01),
		oneHundredMilliSeconds (02),
		seconds (03)
	},
	numberOfPeriodicPulses [03] INTEGER (0..255),
	periodicInterval [04] INTEGER (0..3276),
	periodicIntervalAccuracy [05] ENUMERATED {
		tenMilliSeconds (01),
		oneHundredMilliSeconds (02),
		seconds (03)
	},
	activationTime [06] DateAndTime OPTIONAL
‐‐ when not specified the activation is immediate
‐‐ ...
}
</code>
 *
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface TariffInformation {

	Integer getNumberOfStartPulses();

	Integer getStartInterval();

    IntervalAccuracy getStartIntervalAccuracy();

    Integer getNumberOfPeriodicPulses(); 
    
    Integer getPeriodicInterval();

    IntervalAccuracy getPeriodicIntervalAccuracy();

    DateAndTime getActivationTime();
}