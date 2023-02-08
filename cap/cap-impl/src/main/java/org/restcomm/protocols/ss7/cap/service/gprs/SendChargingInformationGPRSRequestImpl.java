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

package org.restcomm.protocols.ss7.cap.service.gprs;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.gprs.SendChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELSCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CamelSCIGPRSBillingCharacteristicsWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class SendChargingInformationGPRSRequestImpl extends GprsMessageImpl implements SendChargingInformationGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private CamelSCIGPRSBillingCharacteristicsWrapperImpl sciGPRSBillingChargingCharacteristics;

    public SendChargingInformationGPRSRequestImpl() {
    }

    public SendChargingInformationGPRSRequestImpl(
            CAMELSCIGPRSBillingChargingCharacteristics sciGPRSBillingChargingCharacteristics) {
        super();
        
        if(sciGPRSBillingChargingCharacteristics!=null)
        	this.sciGPRSBillingChargingCharacteristics = new CamelSCIGPRSBillingCharacteristicsWrapperImpl(sciGPRSBillingChargingCharacteristics);
    }

    @Override
    public CAMELSCIGPRSBillingChargingCharacteristics getSCIGPRSBillingChargingCharacteristics() {
    	if(sciGPRSBillingChargingCharacteristics==null)
    		return null;
    	
        return this.sciGPRSBillingChargingCharacteristics.getCAMELSCIGPRSBillingChargingCharacteristics();
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.sendChargingInformationGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.sendChargingInformationGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendChargingInformationGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.sciGPRSBillingChargingCharacteristics != null && this.sciGPRSBillingChargingCharacteristics.getCAMELSCIGPRSBillingChargingCharacteristics()!=null) {
            sb.append(", sciGPRSBillingChargingCharacteristics=");
            sb.append(this.sciGPRSBillingChargingCharacteristics.getCAMELSCIGPRSBillingChargingCharacteristics());
            sb.append(" ");
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(sciGPRSBillingChargingCharacteristics==null)
			throw new ASNParsingComponentException("sci gprs billing charging characteristics should be set for send charging information gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}

}
