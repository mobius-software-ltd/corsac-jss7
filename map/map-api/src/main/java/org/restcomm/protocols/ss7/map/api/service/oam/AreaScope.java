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

package org.restcomm.protocols.ss7.map.api.service.oam;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.EUtranCgi;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RAIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.TAId;

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