/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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
import org.restcomm.protocols.ss7.cap.api.service.gprs.FurnishChargingInformationGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristics;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;

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
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class FurnishChargingInformationGPRSRequestImpl extends GprsMessageImpl implements FurnishChargingInformationGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,index = -1, defaultImplementation = CAMELFCIGPRSBillingChargingCharacteristicsImpl.class)
    private CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics;

    public FurnishChargingInformationGPRSRequestImpl() {
    }

    public FurnishChargingInformationGPRSRequestImpl(
    		CAMELFCIGPRSBillingChargingCharacteristics fciGPRSBillingChargingCharacteristics) {
        super();
        this.fciGPRSBillingChargingCharacteristics = fciGPRSBillingChargingCharacteristics;
    }

    public CAMELFCIGPRSBillingChargingCharacteristics getFCIGPRSBillingChargingCharacteristics() {
        return this.fciGPRSBillingChargingCharacteristics;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.furnishChargingInformationGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.furnishChargingInformationGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FurnishChargingInformationGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.fciGPRSBillingChargingCharacteristics != null) {
            sb.append(", fciGPRSBillingChargingCharacteristics=");
            sb.append(this.fciGPRSBillingChargingCharacteristics.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(fciGPRSBillingChargingCharacteristics==null)
			throw new ASNParsingComponentException("fci gprs billing charging characteristics should be set for furnish charging information gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}