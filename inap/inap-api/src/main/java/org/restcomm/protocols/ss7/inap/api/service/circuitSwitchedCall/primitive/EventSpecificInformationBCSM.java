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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AlertingSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnalyzedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.AnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.BusySpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.CollectedInfoSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.DisconnectSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.MidCallSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NoAnswerSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.NotReachableSpecificInfo;
import org.restcomm.protocols.ss7.inap.api.EsiBcsm.RouteSelectFailureSpecificInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
EventSpecificInformationBCSM ::= CHOICE {
	collectedInfoSpecificInfo [0] SEQUENCE {
		calledPartynumber [0] CalledPartyNumber
		-- ... --
	},
	analyzedInfoSpecificInfo [1] SEQUENCE {
		calledPartynumber [0] CalledPartyNumber
		-- ... --
	},
	routeSelectFailureSpecificInfo [2] SEQUENCE {
		failureCause [0] Cause OPTIONAL
		-- ... --
	},
	oCalledPartyBusySpecificInfo [3] SEQUENCE {
		busyCause [0] Cause OPTIONAL
		-- ... --
	},
	oNoAnswerSpecificInfo [4] SEQUENCE {
		-- no specific info defined --
		-- ... --
	},
	oAnswerSpecificInfo [5] SEQUENCE {
		-- no specific info defined --
		-- ... --
	},
	oMidCallSpecificInfo [6] SEQUENCE {
		connectTime [0] Integer4 OPTIONAL
		-- ... --
	},
	oDisconnectSpecificInfo [7] SEQUENCE {
		releaseCause [0] Cause OPTIONAL,
		connectTime [1] Integer4 OPTIONAL
		-- ... --
	},
	tBusySpecificInfo [8] SEQUENCE {
		busyCause [0] Cause OPTIONAL
		-- ... --
	},
	tNoAnswerSpecificInfo [9] SEQUENCE {
		-- no specific info defined --
		-- ... --
	},
	tAnswerSpecificInfo [10] SEQUENCE {
		-- no specific info defined --
		-- ...
	}
	tMidCallSpecificInfo [11] SEQUENCE {
		connectTime [0] Integer4 OPTIONAL
		-- ... --
	},
	tDisconnectSpecificInfo [12] SEQUENCE {
		releaseCause [0] Cause OPTIONAL,
		connectTime [1] Integer4 OPTIONAL
		-- ... --
	}
}
  -- Indicates the call related information specific to the event.
  
--- from cs1+
EventSpecificInformationBCSM ::= CHOICE {
	collectedInfoSpecificInfo [00] SEQUENCE {
		calledPartyNumber [00] Number
		‐‐ ...
	},
	analyzedInfoSpecificInfo [01] SEQUENCE {
		calledPartyNumber [00] Number
		‐‐ ...
	},
	routeSelectFailureSpecificInfo [02] SEQUENCE {
		failureCause [00] Cause OPTIONAL
		‐‐ ...
	},
	oCalledPartyBusySpecificInfo [03] SEQUENCE {
		busyCause [00] Cause OPTIONAL
		‐‐ ...
	},
	oCalledPartyNotReachableSpecificInfo [PRIVATE 01] SEQUENCE {
		notReachableCause [00] Cause OPTIONAL
		‐‐ ...
	},
	oAlertingSpecificInfo [PRIVATE 02] SEQUENCE {
		backwardCallIndicators [00] BackwardCallIndicators
		‐‐ ...
	},
	oNoAnswerSpecificInfo [04] SEQUENCE {
		‐‐ ...
	},
	oAnswerSpecificInfo [05] SEQUENCE {
		‐‐ ...
		timeToAnswer [PRIVATE 01] INTEGER (0..2047) OPTIONAL,
		backwardCallIndicators [PRIVATE 02] BackwardCallIndicators OPTIONAL,
		backwardGVNSIndicator [PRIVATE 03] BackwardGVNSIndicator OPTIONAL
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
	},
	oDisconnectSpecificInfo [07] SEQUENCE {
		releaseCause [00] Cause OPTIONAL
		‐‐ ...
	},
	tRouteSelectFailureSpecificInfo [PRIVATE 06] SEQUENCE {
		failureCause [00] Cause OPTIONAL
		‐‐ ...
	},
	tCalledPartyBusySpecificInfo [08] SEQUENCE {
		busyCause [00] Cause OPTIONAL
		‐‐ ...
	},
	tCalledPartyNotReachableSpecificInfo [PRIVATE 03] SEQUENCE {
		notReachableCause [00] Cause OPTIONAL
		‐‐ ...
	}
	tAlertingSpecificInfo [PRIVATE 07] SEQUENCE {
		backwardCallIndicators [00] BackwardCallIndicators
		‐‐ ...
	},
	tNoAnswerSpecificInfo [09] SEQUENCE {
		‐‐ ...
	},
	tAnswerSpecificInfo [10] SEQUENCE {
		‐‐ ...
		timeToAnswer [PRIVATE 01] INTEGER (0..2047) OPTIONAL,
		backwardCallIndicators [PRIVATE 02] BackwardCallIndicators OPTIONAL,
		backwardGVNSIndicator [PRIVATE 03] BackwardGVNSIndicator OPTIONAL
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
	tDisconnectSpecificInfo [12] SEQUENCE {
		releaseCause [00] Cause OPTIONAL
		‐‐ ...
	}
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface EventSpecificInformationBCSM {

	CollectedInfoSpecificInfo getCollectedInfoSpecificInfo();
	
	AnalyzedInfoSpecificInfo getAnalyzedInfoSpecificInfo();
	
    RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo();

    BusySpecificInfo getOCalledPartyBusySpecificInfo();

    NoAnswerSpecificInfo getONoAnswerSpecificInfo();

    AnswerSpecificInfo getOAnswerSpecificInfo();

    MidCallSpecificInfo getOMidCallSpecificInfo();

    DisconnectSpecificInfo getODisconnectSpecificInfo();

    BusySpecificInfo getTBusySpecificInfo();

    NoAnswerSpecificInfo getTNoAnswerSpecificInfo();

    AnswerSpecificInfo getTAnswerSpecificInfo();

    MidCallSpecificInfo getTMidCallSpecificInfo();

    DisconnectSpecificInfo getTDisconnectSpecificInfo();
    
    NotReachableSpecificInfo getOCalledPartyNotReachableSpecificInfo();
    
    AlertingSpecificInfo getOAlertingSpecificInfo();
    
    RouteSelectFailureSpecificInfo getTRouteSelectFailureSpecificInfo();
    
    NotReachableSpecificInfo getTCalledPartyNotReachableSpecificInfo();
    
    AlertingSpecificInfo getTAlertingSpecificInfo();
}