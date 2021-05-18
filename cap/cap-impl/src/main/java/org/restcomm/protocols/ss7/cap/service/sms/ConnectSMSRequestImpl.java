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
import org.restcomm.protocols.ss7.cap.api.primitives.CAPExtensionsImpl;
import org.restcomm.protocols.ss7.cap.api.primitives.CalledPartyBCDNumberImpl;
import org.restcomm.protocols.ss7.cap.api.service.sms.ConnectSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.SMSAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ConnectSMSRequestImpl extends SmsMessageImpl implements ConnectSMSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private SMSAddressStringImpl callingPartysNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private CalledPartyBCDNumberImpl destinationSubscriberNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private ISDNAddressStringImpl smscAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 10,constructed = true,index = -1)
    private CAPExtensionsImpl extensions;

    public ConnectSMSRequestImpl() {
        super();
    }

    public ConnectSMSRequestImpl(SMSAddressStringImpl callingPartysNumber,
            CalledPartyBCDNumberImpl destinationSubscriberNumber, ISDNAddressStringImpl smscAddress, CAPExtensionsImpl extensions) {
        super();
        this.callingPartysNumber = callingPartysNumber;
        this.destinationSubscriberNumber = destinationSubscriberNumber;
        this.smscAddress = smscAddress;
        this.extensions = extensions;
    }

    @Override
    public SMSAddressStringImpl getCallingPartysNumber() {
        return this.callingPartysNumber;
    }

    @Override
    public CalledPartyBCDNumberImpl getDestinationSubscriberNumber() {
        return this.destinationSubscriberNumber;
    }

    @Override
    public ISDNAddressStringImpl getSMSCAddress() {
        return this.smscAddress;
    }

    @Override
    public CAPExtensionsImpl getExtensions() {
        return this.extensions;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connectSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connectSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ConnectSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.callingPartysNumber != null) {
            sb.append(", callingPartysNumber=");
            sb.append(callingPartysNumber.toString());
        }
        if (this.destinationSubscriberNumber != null) {
            sb.append(", destinationSubscriberNumber=");
            sb.append(destinationSubscriberNumber.toString());
        }
        if (this.smscAddress != null) {
            sb.append(", smscAddress=");
            sb.append(smscAddress.toString());
        }
        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
