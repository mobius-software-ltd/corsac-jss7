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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.FurnishChargingInformationSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.FCIBCCCAMELSequence1SMSImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 * @author alerant appngin
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 4,constructed = false,lengthIndefinite = false)
public class FurnishChargingInformationSMSRequestImpl extends SmsMessageImpl implements FurnishChargingInformationSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index=-1)
	private FCIBCCCAMELSequence1SMSImpl FCIBCCCAMELsequence1;

    public FurnishChargingInformationSMSRequestImpl() {
    }

    public FurnishChargingInformationSMSRequestImpl(FCIBCCCAMELSequence1SMSImpl fciBCCCAMELsequence1) {
        this.FCIBCCCAMELsequence1 = fciBCCCAMELsequence1;
    }

    @Override
    public FCIBCCCAMELSequence1SMSImpl getFCIBCCCAMELsequence1() {
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
}
