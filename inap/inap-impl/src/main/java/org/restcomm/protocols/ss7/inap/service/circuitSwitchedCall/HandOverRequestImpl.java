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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.BearerCapability;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.CGEncountered;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.IPSSPCapabilities;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CallingPartysCategoryIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.CauseIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.DigitsIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.ForwardGVNSIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.HighLayerCompatibilityIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.LocationNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.OriginalCalledNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectingPartyIDIsup;
import org.restcomm.protocols.ss7.commonapp.api.isup.RedirectionInformationIsup;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.EventTypeBCSM;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.HandOverRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.BackwardGVNS;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGCallIndicator;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.CUGInterLockCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericDigitsSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.GenericNumbersSet;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.HandOverInfo;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.LegIDs;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.RouteOrigin;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.ServiceInteractionIndicators;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.TriggerType;
import org.restcomm.protocols.ss7.isup.message.parameter.ForwardCallIndicators;

/**
 *
 * @author yulian.oifa
 *
 */
public class HandOverRequestImpl extends InitialDPRequestImpl implements
	HandOverRequest {
	private static final long serialVersionUID = 1L;

	@Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.handOver_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.handOver;
    }

    public HandOverRequestImpl() {
    	
    }
    public HandOverRequestImpl(int serviceKey, CalledPartyNumberIsup calledPartyNumber,
            CallingPartyNumberIsup callingPartyNumber,CallingPartysCategoryIsup callingPartysCategory,
            CGEncountered cgEncountered, IPSSPCapabilities ipsspCapabilities,LocationNumberIsup locationNumber,
            OriginalCalledNumberIsup originalCalledPartyID,CAPINAPExtensions extensions,TriggerType triggerType,
            HighLayerCompatibilityIsup highLayerCompatibility,ServiceInteractionIndicators serviceInteractionIndicators, 
            DigitsIsup additionalCallingPartyNumber,ForwardCallIndicators forwardCallIndicators,BearerCapability bearerCapability, 
            EventTypeBCSM eventTypeBCSM,  RedirectingPartyIDIsup redirectingPartyID,RedirectionInformationIsup redirectionInformation,
            LegIDs legIDs,RouteOrigin routeOrigin,boolean testIndication,CUGCallIndicator cugCallIndicator,
            CUGInterLockCode cugInterLockCode,GenericDigitsSet genericDigitsSet,GenericNumbersSet genericNumberSet,
            CauseIsup cause,HandOverInfo handOverInfo,ForwardGVNSIsup forwardGVNSIndicator,BackwardGVNS backwardGVNSIndicator) {
    	super(serviceKey, calledPartyNumber, callingPartyNumber, callingPartysCategory, cgEncountered, ipsspCapabilities, locationNumber, originalCalledPartyID, extensions, triggerType, highLayerCompatibility, serviceInteractionIndicators, additionalCallingPartyNumber, forwardCallIndicators, bearerCapability, eventTypeBCSM, redirectingPartyID, redirectionInformation, legIDs, routeOrigin, testIndication, cugCallIndicator, cugInterLockCode, genericDigitsSet, genericNumberSet, cause, handOverInfo, forwardGVNSIndicator, backwardGVNSIndicator);
    }
    
    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("HandoverRequestIndication [");
        this.addInvokeIdInfo(sb);

        toStringInternal(sb);

        sb.append("]");

        return sb.toString();
	}
}
