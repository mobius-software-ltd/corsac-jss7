/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.commonapp.api.APPParsingComponentException;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.UUData;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.LowLayerCompatibility;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMEI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.MSClassmark2;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.OfferedCamel4Functionalities;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.SupportedCamelPhases;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
CAP V4:
InitialDPArgExtension {PARAMETERS-BOUND : bound} ::= SEQUENCE {
  gmscAddress                   [0] ISDN-AddressString OPTIONAL,
  forwardingDestinationNumber   [1] CalledPartyNumber {bound} OPTIONAL,
  ms-Classmark2                 [2] MS-Classmark2 OPTIONAL,
  iMEI                          [3] IMEI OPTIONAL,
  supportedCamelPhases          [4] SupportedCamelPhases OPTIONAL,
  offeredCamel4Functionalities  [5] OfferedCamel4Functionalities OPTIONAL,
  bearerCapability2             [6] BearerCapability {bound} OPTIONAL,
  ext-basicServiceCode2         [7] Ext-BasicServiceCode OPTIONAL,
  highLayerCompatibility2       [8] HighLayerCompatibility OPTIONAL,
  lowLayerCompatibility         [9] LowLayerCompatibility {bound} OPTIONAL,
  lowLayerCompatibility2        [10] LowLayerCompatibility {bound} OPTIONAL,
  ...,
  enhancedDialledServicesAllowed [11] NULL OPTIONAL,
  uu-Data                        [12] UU-Data OPTIONAL
  collectInformationAllowed      [13] NULL OPTIONAL,
  releaseCallArgExtensionAllowed [14] NULL OPTIONAL
}
CAP V2: InitialDPArgExtension ::= SEQUENCE {
  naCarrierInformation          [0] NACarrierInformation OPTIONAL,
  gmscAddress                   [1] ISDN-AddressString OPTIONAL,
  ...
}
-- If iPSSPCapabilities is not present then this denotes that a colocated gsmSRF is not
-- supported by the gsmSSF. If present, then the gsmSSF supports a colocated gsmSRF capable
-- of playing announcements via elementaryMessageIDs and variableMessages, the playing of
-- tones and the collection of DTMF digits. Other supported capabilities are explicitly
-- detailed in the IPSSPCapabilities parameter itself.
-- Carrier is included at the discretion of the gsmSSF operator.
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface InitialDPArgExtension {

    ISDNAddressString getGmscAddress();

    CalledPartyNumberIsup getForwardingDestinationNumber();

    MSClassmark2 getMSClassmark2();

    IMEI getIMEI();

    SupportedCamelPhases getSupportedCamelPhases();

    OfferedCamel4Functionalities getOfferedCamel4Functionalities();

    BearerCapability getBearerCapability2();

    ExtBasicServiceCode getExtBasicServiceCode2();

    HighLayerCompatibilityIsup getHighLayerCompatibility2();

    LowLayerCompatibility getLowLayerCompatibility();

    LowLayerCompatibility getLowLayerCompatibility2();

    boolean getEnhancedDialledServicesAllowed();

    UUData getUUData();

    boolean getCollectInformationAllowed();

    boolean getReleaseCallArgExtensionAllowed();
    
    void patchVersion(int version) throws APPParsingComponentException;

}
