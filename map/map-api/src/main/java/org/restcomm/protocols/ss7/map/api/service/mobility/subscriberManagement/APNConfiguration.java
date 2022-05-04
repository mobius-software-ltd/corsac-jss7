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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 APN-Configuration ::= SEQUENCE { contextId [0] ContextId, pdn-Type [1] PDN-Type, servedPartyIP-IPv4-Address [2] PDP-Address
 * OPTIONAL, apn [3] APN, eps-qos-Subscribed [4] EPS-QoS-Subscribed, pdn-gw-Identity [5] PDN-GW-Identity OPTIONAL,
 * pdn-gw-AllocationType [6] PDN-GW-AllocationType OPTIONAL, vplmnAddressAllowed [7] NULL OPTIONAL, chargingCharacteristics [8]
 * ChargingCharacteristics OPTIONAL, ambr [9] AMBR OPTIONAL, specificAPNInfoList [10] SpecificAPNInfoList OPTIONAL,
 * extensionContainer [11] ExtensionContainer OPTIONAL, servedPartyIP-IPv6-Address [12] PDP-Address OPTIONAL, ...,
 * apn-oi-Replacement [13] APN-OI-Replacement OPTIONAL, -- this apn-oi-Replacement refers to the APN level apn-oi-Replacement.
 * sipto-Permission [14] SIPTO-Permission OPTIONAL, lipa-Permission [15] LIPA-Permission OPTIONAL }
 *
 * ContextId ::= INTEGER (1..50)
 *
 * SpecificAPNInfoList ::= SEQUENCE SIZE (1..50) OF SpecificAPNInfo
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface APNConfiguration {

    int getContextId();

    PDNType getPDNType();

    PDPAddress getServedPartyIPIPv4Address();

    APN getApn();

    EPSQoSSubscribed getEPSQoSSubscribed();

    PDNGWIdentity getPdnGwIdentity();

    PDNGWAllocationType getPdnGwAllocationType();

    boolean getVplmnAddressAllowed();

    ChargingCharacteristics getChargingCharacteristics();

    AMBR getAmbr();

    List<SpecificAPNInfo> getSpecificAPNInfoList();

    MAPExtensionContainer getExtensionContainer();

    PDPAddress getServedPartyIPIPv6Address();

    APNOIReplacement getApnOiReplacement();

    SIPTOPermission getSiptoPermission();

    LIPAPermission getLipaPermission();

}
