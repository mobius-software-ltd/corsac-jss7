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

package org.restcomm.protocols.ss7.map.api.service.mobility.locationManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
SGSN-Capability ::= SEQUENCE {
  solsaSupportIndicator                          NULL OPTIONAL,
  extensionContainer                             [1] ExtensionContainer OPTIONAL,
  ...,
  superChargerSupportedInServingNetworkEntity    [2] SuperChargerInfo OPTIONAL,
  gprsEnhancementsSupportIndicator               [3] NULL OPTIONAL,
  supportedCamelPhases                           [4] SupportedCamelPhases OPTIONAL,
  supportedLCS-CapabilitySets                    [5] SupportedLCS-CapabilitySets OPTIONAL,
  offeredCamel4CSIs                              [6] OfferedCamel4CSIs OPTIONAL,
  smsCallBarringSupportIndicator                 [7] NULL OPTIONAL,
  supportedRAT-TypesIndicator                    [8] SupportedRAT-Types OPTIONAL,
  supportedFeatures                              [9] SupportedFeatures OPTIONAL,
  t-adsDataRetrieval                             [10] NULL OPTIONAL,
  homogeneousSupportOfIMSVoiceOverPSSessions     [11] BOOLEAN OPTIONAL
  -- "true" indicates homogeneous support, "false" indicates homogeneous non-support
  -- in the complete SGSN area
}
</code>
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface SGSNCapability {

    boolean getSolsaSupportIndicator();

    MAPExtensionContainer getExtensionContainer();

    SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity();

    boolean getGprsEnhancementsSupportIndicator();

    SupportedCamelPhases getSupportedCamelPhases();

    SupportedLCSCapabilitySets getSupportedLCSCapabilitySets();

    OfferedCamel4CSIs getOfferedCamel4CSIs();

    boolean getSmsCallBarringSupportIndicator();

    SupportedRATTypes getSupportedRATTypesIndicator();

    SupportedFeatures getSupportedFeatures();

    boolean getTAdsDataRetrieval();

    Boolean getHomogeneousSupportOfIMSVoiceOverPSSessions();

}