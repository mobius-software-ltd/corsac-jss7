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
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 GPRSSubscriptionData ::= SEQUENCE {
   completeDataListIncluded NULL OPTIONAL,
   -- If segmentation is used, completeDataListIncluded may only be present in the
   -- first segment of GPRSSubscriptionData.
   gprsDataList [1] GPRSDataList,
   extensionContainer [2] ExtensionContainer OPTIONAL,
   ...,
   apn-oi-Replacement [3] APN-OI-Replacement OPTIONAL
   -- this apn-oi-Replacement refers to the UE level apn-oi-Replacement.
}
 *
 * GPRSDataList ::= SEQUENCE SIZE (1..50) OF PDP-Context
 *
 *
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public interface GPRSSubscriptionData {

    boolean getCompleteDataListIncluded();

    List<PDPContext> getGPRSDataList();

    MAPExtensionContainer getExtensionContainer();

    APNOIReplacement getApnOiReplacement();

}