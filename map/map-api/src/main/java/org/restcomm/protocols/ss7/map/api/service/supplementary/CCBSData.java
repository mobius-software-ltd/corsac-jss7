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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import java.io.Serializable;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;

/**
 *
 CCBS-Data ::= SEQUENCE { ccbs-Feature [0] CCBS-Feature, translatedB-Number [1] ISDN-AddressString, serviceIndicator [2]
 * ServiceIndicator OPTIONAL, callInfo [3] ExternalSignalInfo, networkSignalInfo [4] ExternalSignalInfo, ...}
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
public interface CCBSData extends Serializable {

    CCBSFeature getCcbsFeature();

    ISDNAddressString getTranslatedBNumber();

    ServiceIndicator getServiceIndicator();

    ExternalSignalInfo getCallInfo();

    ExternalSignalInfo getNetworkSignalInfo();

}
