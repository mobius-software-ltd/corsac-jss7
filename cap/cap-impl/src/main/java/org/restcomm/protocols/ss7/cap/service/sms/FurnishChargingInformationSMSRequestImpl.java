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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMS;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class FurnishChargingInformationSMSRequestImpl extends SmsMessageImpl implements FurnishChargingInformationSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index=-1,defaultImplementation = FCIBCCCAMELSequence1SMSImpl.class)
	private FCIBCCCAMELSequence1SMS FCIBCCCAMELsequence1;

    public FurnishChargingInformationSMSRequestImpl() {
    }

    public FurnishChargingInformationSMSRequestImpl(FCIBCCCAMELSequence1SMS fciBCCCAMELsequence1) {
        this.FCIBCCCAMELsequence1 = fciBCCCAMELsequence1;
    }

    @Override
    public FCIBCCCAMELSequence1SMS getFCIBCCCAMELsequence1() {
        return this.FCIBCCCAMELsequence1;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.furnishChargingInformationSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.furnishChargingInformationSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("FurnishChargingInformationSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.FCIBCCCAMELsequence1 != null) {
            sb.append(", FCIBCCCAMELsequence1=");
            sb.append(FCIBCCCAMELsequence1.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(FCIBCCCAMELsequence1==null)
			throw new ASNParsingComponentException("fcibcc CAMEL sequence1 should be set for furnish charging information sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
