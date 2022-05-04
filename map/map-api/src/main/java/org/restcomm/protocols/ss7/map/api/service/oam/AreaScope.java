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

package org.restcomm.protocols.ss7.map.api.service.oam;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.TAId;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
AreaScope ::= SEQUENCE {
  cgi-List              [0] CGI-List OPTIONAL,
  e-utran-cgi-List      [1] E-UTRAN-CGI-List OPTIONAL,
  routingAreaId-List    [2] RoutingAreaId-List OPTIONAL,
  locationAreaId-List   [3] LocationAreaId-List OPTIONAL,
  trackingAreaId-List   [4] TrackingAreaId-List OPTIONAL,
  extensionContainer    [5] ExtensionContainer OPTIONAL,
  ...
}
CGI-List ::= SEQUENCE SIZE (1..32) OF GlobalCellId
E-UTRAN-CGI-List ::= SEQUENCE SIZE (1..32) OF E-UTRAN-CGI
RoutingAreaId-List ::= SEQUENCE SIZE (1..8) OF RAIdentity
LocationAreaId-List ::= SEQUENCE SIZE (1..8) OF LAIFixedLength
TrackingAreaId-List ::= SEQUENCE SIZE (1..8) OF TA-Id
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface AreaScope {

    List<GlobalCellId> getCgiList();

    List<EUtranCgi> getEUutranCgiList();

    List<RAIdentity> getRoutingAreaIdList();

    List<LAIFixedLength> getLocationAreaIdList();

    List<TAId> getTrackingAreaIdList();

    MAPExtensionContainer getExtensionContainer();

}