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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import java.util.List;

import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
GenericServiceInfo ::= SEQUENCE {
  ss-Status               SS-Status,
  cliRestrictionOption    CliRestrictionOption OPTIONAL,
  ...,
  maximumEntitledPriority    [0] EMLPP-Priority OPTIONAL,
  defaultPriority            [1] EMLPP-Priority OPTIONAL,
  ccbs-FeatureList           [2] CCBS-FeatureList OPTIONAL,
  nbrSB                      [3] MaxMC-Bearers OPTIONAL,
  nbrUser                    [4] MC-Bearers OPTIONAL,
  nbrSN                      [5] MC-Bearers OPTIONAL
}
CCBS-FeatureList ::= SEQUENCE SIZE (1..5) OF CCBS-Feature
MC-Bearers ::= INTEGER (1..7)
MaxMC-Bearers ::= INTEGER (2..7)
</code>
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface GenericServiceInfo {

    SSStatus getSsStatus();

    CliRestrictionOption getCliRestrictionOption();

    EMLPPPriority getMaximumEntitledPriority();

    EMLPPPriority getDefaultPriority();

    List<CCBSFeature> getCcbsFeatureList();

    Integer getNbrSB();

    Integer getNbrUser();

    Integer getNbrSN();
}