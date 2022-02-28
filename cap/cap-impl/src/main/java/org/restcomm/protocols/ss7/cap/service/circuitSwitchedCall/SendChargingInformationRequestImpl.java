/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristicsImpl;
import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SendChargingInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements
        SendChargingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1,defaultImplementation = SCIBillingChargingCharacteristicsImpl.class)
    private SCIBillingChargingCharacteristics sciBillingChargingCharacteristics;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private SendingLegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public SendChargingInformationRequestImpl() {
    }

    public SendChargingInformationRequestImpl(SCIBillingChargingCharacteristics sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) {
        this.sciBillingChargingCharacteristics = sciBillingChargingCharacteristics;
        
        if(partyToCharge!=null)
        	this.partyToCharge = new SendingLegIDWrapperImpl(new SendingLegIDImpl(partyToCharge));
        
        this.extensions = extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.sendChargingInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.sendChargingInformation;
    }

    @Override
    public SCIBillingChargingCharacteristics getSCIBillingChargingCharacteristics() {
        return sciBillingChargingCharacteristics;
    }

    @Override
    public LegType getPartyToCharge() {
    	if(partyToCharge==null || partyToCharge.getSendingLegID()==null)
    		return null;
    	
        return partyToCharge.getSendingLegID().getSendingSideID();
    }

    @Override
    public CAPINAPExtensions getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("SendChargingInformationRequestIndication [");
        this.addInvokeIdInfo(sb);

        if (this.sciBillingChargingCharacteristics != null) {
            sb.append(", sciBillingChargingCharacteristics=");
            sb.append(sciBillingChargingCharacteristics.toString());
        }
        if (this.partyToCharge != null && this.partyToCharge.getSendingLegID()!=null) {
            sb.append(", partyToCharge=");
            sb.append(partyToCharge.getSendingLegID());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(sciBillingChargingCharacteristics==null)
			throw new ASNParsingComponentException("sci billing charging characteristics should be set for send charging information request", ASNParsingComponentExceptionReason.MistypedRootParameter);		

		if(partyToCharge==null)
			throw new ASNParsingComponentException("party to charge should be set for send charging information request", ASNParsingComponentExceptionReason.MistypedRootParameter);		
	}
}
