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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 VlrCamelSubscriptionInfo ::= SEQUENCE { o-CSI [0] O-CSI OPTIONAL, extensionContainer [1] ExtensionContainer OPTIONAL, ...,
 * ss-CSI [2] SS-CSI OPTIONAL, o-BcsmCamelTDP-CriteriaList [4] O-BcsmCamelTDPCriteriaList OPTIONAL, tif-CSI [3] NULL OPTIONAL,
 * m-CSI [5] M-CSI OPTIONAL, mo-sms-CSI [6] SMS-CSI OPTIONAL, vt-CSI [7] T-CSI OPTIONAL, t-BCSM-CAMEL-TDP-CriteriaList [8]
 * T-BCSM-CAMEL-TDP-CriteriaList OPTIONAL, d-CSI [9] D-CSI OPTIONAL, mt-sms-CSI [10] SMS-CSI OPTIONAL,
 * mt-smsCAMELTDP-CriteriaList [11] MT-smsCAMELTDP-CriteriaList OPTIONAL }
 *
 * O-BcsmCamelTDPCriteriaList ::= SEQUENCE SIZE (1..10) OF O-BcsmCamelTDP-Criteria
 *
 * T-BCSM-CAMEL-TDP-CriteriaList ::= SEQUENCE SIZE (1..10) OF T-BCSM-CAMEL-TDP-Criteria
 *
 * MT-smsCAMELTDP-CriteriaList ::= SEQUENCE SIZE (1.. 10) OF MT-smsCAMELTDP-Criteria
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface VlrCamelSubscriptionInfo {

    OCSI getOCsi();

    MAPExtensionContainer getExtensionContainer();

    SSCSI getSsCsi();

    List<OBcsmCamelTdpCriteria> getOBcsmCamelTDPCriteriaList();

    boolean getTifCsi();

    MCSI getMCsi();

    SMSCSI getSmsCsi();

    TCSI getVtCsi();

    List<TBcsmCamelTdpCriteria> getTBcsmCamelTdpCriteriaList();

    DCSI getDCsi();

    SMSCSI getMtSmsCSI();

    List<MTsmsCAMELTDPCriteria> getMtSmsCamelTdpCriteriaList();

}
