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

package org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.inap.api.primitives.TerminalType;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
<code>
DpSpecificCommonParameters ::= SEQUENCE {
	serviceAddressInformation [0] ServiceAddressInformation,
	bearerCapability [1] BearerCapability OPTIONAL,
	calledPartyNumber [2] CalledPartyNumber OPTIONAL,
	callingPartyNumber [3] CallingPartyNumber OPTIONAL,
	callingPartysCategory [4] CallingPartysCategory OPTIONAL,
	iPSSPCapabilities [5] IPSSPCapabilities OPTIONAL,
	iPAvailable [6] IPAvailable OPTIONAL,
	iSDNAccessRelatedInformation [7] ISDNAccessRelatedInformation OPTIONAL,
	cGEncountered [8] CGEncountered OPTIONAL,
	locationNumber [9] LocationNumber OPTIONAL,
	serviceProfileIdentifier [10] ServiceProfileIdentifier OPTIONAL,
	terminalType [11] TerminalType OPTIONAL,
	extensions [12] SEQUENCE SIZE(1..numOfExtensions) OF ExtensionField OPTIONAL,
	chargeNumber [13] ChargeNumber OPTIONAL,
	servingAreaID [14] ServingAreaID OPTIONAL
-- ...
}
-- OPTIONAL for iPSSPCapabilities, iPAvailable, and cGEncountered denotes network operator
-- specific use. OPTIONAL for callingPartyNumber, and callingPartysCategory refer to clause 3 for
-- the trigger detection point processing rules to specify when these parameters are included in the
-- message. bearerCapability should be appropriately coded as speech.

ChargeNumber ::= LocationNumber

ServingAreaID ::= LocationNumber
</code>
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public interface DpSpecificCommonParameters {
	ServiceAddressInformation getServiceAddressInformation();

	BearerCapability getBearerCapability();
	
	CalledPartyNumberIsup getCalledPartyNumber();
	
	CallingPartyNumberIsup getCallingPartyNumber();
	
	CallingPartysCategoryIsup getCallingPartysCategory();
	
	IPSSPCapabilities getIPSSPCapabilities();
	
	IPAvailable getIPAvailable();
	
	ISDNAccessRelatedInformation getISDNAccessRelatedInformation();
	
	CGEncountered getCGEncountered();
	
	LocationNumberIsup getLocationNumber();
	
	ServiceProfileIdentifier getServiceProfileIdentifier();
	
	TerminalType getTerminalType();
	
    CAPINAPExtensions getExtensions();  
    
    LocationNumberIsup getChargeNumber();
	
    LocationNumberIsup getServingAreaID();
}