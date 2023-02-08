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

package org.restcomm.protocols.ss7.map.api.service.oam;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
TraceType ::= INTEGER (0..255)
-- Trace types are fully defined in 3GPP TS 52.008. [61]
MSC/BSS Trace Type:
8 - Priority Indication
7 - For future expansion (Set to 0)
6, 5 - BSS Record Type
  0 - Basic
  1 - Handover
  2 - Radio
  3 - No BSS Trace
4, 3 - MSC Record Type
  0 - Basic
  1 - Detailed (Optional)
  2 - Spare
  3 - No MSC Trace
2, 1 - Invoking Event
  0 - MOC, MTC, SMS MO, SMS MT, PDS MO, PDS MT, SS, Location Updates, IMSI attach, IMSI detach
  1 - MOC, MTC, SMS_MO, SMS_MT, PDS MO, PDS MT, SS only
  2 - Location updates, IMSI attach IMSI detach only
  3 - Operator definable
HLR Trace Type:
8 - Priority Indication
7, 6, 5 - For future expansion (Set to 0)
4, 3 - HLR Record Type
  0 - Basic
  1 - Detailed
  2 - Spare
  3 - No HLR Trace
2, 1 - Invoking Event
  0 - All HLR Interactions
  1 - Spare
  2 - Spare
  3 - Operator definable
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,lengthIndefinite=false)
public interface TraceType {

    int getData();

    BssRecordType getBssRecordType();

    MscRecordType getMscRecordType();

    HlrRecordType getHlrRecordType();

    TraceTypeInvokingEvent getTraceTypeInvokingEvent();

    boolean isPriorityIndication();

}