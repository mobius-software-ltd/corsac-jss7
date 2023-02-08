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

package org.restcomm.protocols.ss7.map.api.errors;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;

/**
 *
 ss-Incompatibility ERROR ::= { PARAMETER SS-IncompatibilityCause -- optional CODE local:20 }
 *
 * SS-IncompatibilityCause ::= SEQUENCE { ss-Code [1] SS-Code OPTIONAL, basicService BasicServiceCode OPTIONAL, ss-Status [4]
 * SS-Status OPTIONAL, ...}
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface MAPErrorMessageSsIncompatibility extends MAPErrorMessage {

    SSCode getSSCode();

    BasicServiceCode getBasicService();

    SSStatus getSSStatus();

    void setSSCode(SSCode val);

    void setBasicService(BasicServiceCode val);

    void setSSStatus(SSStatus val);

}
