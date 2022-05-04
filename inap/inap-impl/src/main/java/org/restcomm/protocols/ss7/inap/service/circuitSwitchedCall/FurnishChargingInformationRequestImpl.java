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

import org.restcomm.protocols.ss7.inap.api.INAPMessageType;
import org.restcomm.protocols.ss7.inap.api.INAPOperationCode;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.FurnishChargingInformationRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNWrappedTag
public class FurnishChargingInformationRequestImpl extends CircuitSwitchedCallMessageImpl implements
        FurnishChargingInformationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,index = -1)
    private ASNOctetString fciBillingChargingCharacteristics;

    public FurnishChargingInformationRequestImpl() {
    }

    public FurnishChargingInformationRequestImpl(ByteBuf fciBillingChargingCharacteristics) {
    	if(fciBillingChargingCharacteristics!=null)
    		this.fciBillingChargingCharacteristics = new ASNOctetString(fciBillingChargingCharacteristics,"FurnishChargingInformationRequest",null,null,true);    
    }

    @Override
    public INAPMessageType getMessageType() {
        return INAPMessageType.furnishChargingInformation_Request;
    }

    @Override
    public int getOperationCode() {
        return INAPOperationCode.furnishChargingInformation;
    }

    @Override
    public ByteBuf getFCIBillingChargingCharacteristics() {
    	if(fciBillingChargingCharacteristics==null)
    		return null;
    	
    	return fciBillingChargingCharacteristics.getValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FurnishChargingInformationIndication [");
        this.addInvokeIdInfo(sb);

        if (this.fciBillingChargingCharacteristics != null && fciBillingChargingCharacteristics.getValue()!=null) {
            sb.append(", FCIBillingChargingCharacteristics=");
            sb.append(fciBillingChargingCharacteristics.printDataArr());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(fciBillingChargingCharacteristics==null)
			throw new ASNParsingComponentException("fci billing charging characteristics should be set for furnish charging information request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
