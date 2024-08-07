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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CellGlobalIdOrServiceAreaIdFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
ChangeOfPositionControlInfo {PARAMETERS-BOUND : bound} ::= SEQUENCE SIZE (1..bound.&numOfChangeOfPositionControlInfo) OF ChangeOfLocation {bound}
ChangeOfLocation {PARAMETERS-BOUND : bound} ::= CHOICE {
  cellGlobalId     [0] CellGlobalIdOrServiceAreaIdFixedLength,
  serviceAreaId    [1] CellGlobalIdOrServiceAreaIdFixedLength,
  locationAreaId   [2] LAIFixedLength,
  inter-SystemHandOver  [3] NULL,
  inter-PLMNHandOver    [4] NULL,
  inter-MSCHandOver     [5] NULL,
  changeOfLocationAlt   [6] ChangeOfLocationAlt {bound}
}
-- The cellGlobalId shall contain a Cell Global Identification.
-- The serviceAreaId shall contain a Service Area Identification.
ChangeOfLocationAlt {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  ...
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface ChangeOfLocation {

    CellGlobalIdOrServiceAreaIdFixedLength getCellGlobalId();

    CellGlobalIdOrServiceAreaIdFixedLength getServiceAreaId();

    LAIFixedLength getLocationAreaId();

    boolean isInterSystemHandOver();

    boolean isInterPLMNHandOver();

    boolean isInterMSCHandOver();

    ChangeOfLocationAlt getChangeOfLocationAlt();

    public enum CellGlobalIdOrServiceAreaIdFixedLength_Option {
        cellGlobalId, serviceAreaId;
    }

    public enum Boolean_Option {
        interSystemHandOver, interPLMNHandOver, interMSCHandOver;
    }

}