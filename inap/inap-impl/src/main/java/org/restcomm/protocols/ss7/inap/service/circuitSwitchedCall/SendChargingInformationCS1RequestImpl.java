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

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.api.primitives.LegType;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.SendingLegIDWrapperImpl;
import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.SendChargingInformationRequest;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.primitive.SCIBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus.SCIBillingChargingCharacteristicsCS1WrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SendChargingInformationCS1RequestImpl extends CircuitSwitchedCallMessageImpl implements
        SendChargingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private SCIBillingChargingCharacteristicsCS1WrapperImpl sciBillingChargingCharacteristics;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private SendingLegIDWrapperImpl partyToCharge;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1,defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    public SendChargingInformationCS1RequestImpl() {
    }

    public SendChargingInformationCS1RequestImpl(SCIBillingChargingCharacteristicsCS1 sciBillingChargingCharacteristics,
    		LegType partyToCharge, CAPINAPExtensions extensions) {
    	if(sciBillingChargingCharacteristics!=null)
    		this.sciBillingChargingCharacteristics = new SCIBillingChargingCharacteristicsCS1WrapperImpl(sciBillingChargingCharacteristics);
        
        if(partyToCharge!=null)
        	this.partyToCharge = new SendingLegIDWrapperImpl(new SendingLegIDImpl(partyToCharge));
        
        this.extensions = extensions;
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.sendChargingInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.sendChargingInformation;
    }

    @Override
    public SCIBillingChargingCharacteristics getSCIBillingChargingCharacteristics() {
    	if(sciBillingChargingCharacteristics==null)
    		return null;
    	
        return sciBillingChargingCharacteristics.getSCIBillingChargingCharacteristics();
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

        if (this.sciBillingChargingCharacteristics != null && this.sciBillingChargingCharacteristics.getSCIBillingChargingCharacteristics()!=null) {
            sb.append(", sciBillingChargingCharacteristics=");
            sb.append(sciBillingChargingCharacteristics.getSCIBillingChargingCharacteristics().toString());
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
			throw new ASNParsingComponentException("sci billingCharging characteristics should be set for send charging information request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(partyToCharge==null)
			throw new ASNParsingComponentException("party to charge should be set for send charging information request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
