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
