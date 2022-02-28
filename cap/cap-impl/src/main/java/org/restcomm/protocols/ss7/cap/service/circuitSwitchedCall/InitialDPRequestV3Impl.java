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

package org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.InitialDPRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.InitialDPArgExtension;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.InitialDPArgExtensionV3Impl;
import org.restcomm.protocols.ss7.commonapp.api.callhandling.CallReferenceNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CalledPartyBCDNumber;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.Carrier;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.ServiceInteractionIndicatorsTwo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.TimeAndTimezone;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.LocationInformation;
import org.restcomm.protocols.ss7.commonapp.api.subscriberInformation.SubscriberState;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGIndex;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.CUGInterlock;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL, tag = 16, constructed = true, lengthIndefinite = false)
public class InitialDPRequestV3Impl extends InitialDPRequestBaseImpl implements InitialDPRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC, tag = 59, constructed = true, index = -1, defaultImplementation = InitialDPArgExtensionV3Impl.class)
	private InitialDPArgExtension initialDPArgExtension;

	/**
	 * This constructor is only for deserialisation purpose
	 */
	public InitialDPRequestV3Impl() {
	}

	public InitialDPRequestV3Impl(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
			CallingPartyNumberIsup callingPartyNumber, CallingPartysCategoryIsup callingPartysCategory,
			CGEncountered cgEncountered, IPSSPCapabilities IPSSPCapabilities, LocationNumberIsup locationNumber,
			OriginalCalledNumberIsup originalCalledPartyID, CAPINAPExtensions extensions,
			HighLayerCompatibilityIsup highLayerCompatibility, DigitsIsup additionalCallingPartyNumber,
			BearerCapability bearerCapability, EventTypeBCSM eventTypeBCSM, RedirectingPartyIDIsup redirectingPartyID,
			RedirectionInformationIsup redirectionInformation, CauseIsup cause,
			ServiceInteractionIndicatorsTwo serviceInteractionIndicatorsTwo, Carrier carrier, CUGIndex cugIndex,
			CUGInterlock cugInterlock, boolean cugOutgoingAccess, IMSI imsi, SubscriberState subscriberState,
			LocationInformation locationInformation, ExtBasicServiceCode extBasicServiceCode,
			CallReferenceNumber callReferenceNumber, ISDNAddressString mscAddress,
			CalledPartyBCDNumber calledPartyBCDNumber, TimeAndTimezone timeAndTimezone, boolean callForwardingSSPending,
			InitialDPArgExtension initialDPArgExtension) {

		super(serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory, cgEncountered,
				IPSSPCapabilities, locationNumber, originalCalledPartyID, extensions, highLayerCompatibility,
				additionalCallingPartyNumber, bearerCapability, eventTypeBCSM, redirectingPartyID,
				redirectionInformation, cause, serviceInteractionIndicatorsTwo, carrier, cugIndex, cugInterlock,
				cugOutgoingAccess, imsi, subscriberState, locationInformation, extBasicServiceCode, callReferenceNumber,
				mscAddress, calledPartyBCDNumber, timeAndTimezone, callForwardingSSPending);

		this.initialDPArgExtension = initialDPArgExtension;
	}

	@Override
	public InitialDPArgExtension getInitialDPArgExtension() {
		return initialDPArgExtension;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("InitialDPRequestIndication [");
		this.addInvokeIdInfo(sb);

		sb.append(super.toString());
		
		if (this.initialDPArgExtension != null) {
			sb.append(", initialDPArgExtension=");
			sb.append(initialDPArgExtension.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}