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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
AChBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE (bound.&minAChBillingChargingLength .. bound.&maxAChBillingChargingLength))
(CONSTRAINED BY {
-- shall be the result of the BER-encoded value of the type
-- CAMEL-AChBillingChargingCharacteristics {bound}})
-- The AChBillingChargingCharacteristics parameter specifies the charging related information
-- to be provided by the gsmSSF and the conditions on which this information has to be reported
-- back to the gsmSCF with the ApplyChargingReport operation. The value of the
-- AChBillingChargingCharacteristics of type OCTET STRING carries a value of the ASN.1 data type:
-- CAMEL-AChBillingChargingCharacteristics. The normal encoding rules are used to encode this
-- value.
-- The violation of the UserDefinedConstraint shall be handled as an ASN.1 syntax error.
CAMEL V2:
CAMEL-AChBillingChargingCharacteristics ::= CHOICE {
  timeDurationCharging [0] SEQUENCE {
    maxCallPeriodDuration      [0] INTEGER (1..864000),
    releaseIfdurationExceeded  [1] ReleaseIfDurationExceeded OPTIONAL,
    tariffSwitchInterval       [2] INTEGER (1..86400) OPTIONAL
  }
}
ReleaseIfDurationExceeded ::= SEQUENCE {
  tone BOOLEAN DEFAULT FALSE,
  ...,
  extensions [10] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL
}
CAMEL V3:
CAMEL-AChBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= CHOICE {
  timeDurationCharging [0] SEQUENCE {
    maxCallPeriodDuration      [0] INTEGER (1..864000),
    releaseIfdurationExceeded  [1] BOOLEAN DEFAULT FALSE,
    tariffSwitchInterval       [2] INTEGER (1..86400) OPTIONAL,
    tone                       [3] BOOLEAN DEFAULT FALSE,
    extensions                 [4] Extensions {bound} OPTIONAL,
    ...
  }
}
CAMEL V4:
CAMEL-AChBillingChargingCharacteristics {PARAMETERS-BOUND : bound} ::= CHOICE {
  timeDurationCharging [0] SEQUENCE {
    maxCallPeriodDuration      [0] INTEGER (1..864000),
    releaseIfdurationExceeded  [1] BOOLEAN DEFAULT FALSE,
    tariffSwitchInterval       [2] INTEGER (1..86400) OPTIONAL,
    audibleIndicator           [3] AudibleIndicator DEFAULT tone: FALSE,
    extensions                 [4] Extensions {bound} OPTIONAL,
    ...
  }
}
-- tariffSwitchInterval is measured in 1 second units.
-- maxCallPeriodDuration is measured in 100 millisecond units
</code>
 *
 *
 * @author sergey vetyutnev
 * @author alerant appngin
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public interface CAMELAChBillingChargingCharacteristics {

    long getMaxCallPeriodDuration();

    boolean getReleaseIfdurationExceeded();

    Long getTariffSwitchInterval();

    AudibleIndicator getAudibleIndicator();

    CAPINAPExtensions getExtensions();
}