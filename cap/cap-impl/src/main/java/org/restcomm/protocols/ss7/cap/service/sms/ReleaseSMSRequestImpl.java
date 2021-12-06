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
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.RPCauseImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNWrappedTag
public class ReleaseSMSRequestImpl extends SmsMessageImpl implements ReleaseSMSRequest {
	private static final long serialVersionUID = 1L;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=-1,defaultImplementation = RPCauseImpl.class)
	private RPCause rpCause;

    public ReleaseSMSRequestImpl(RPCause rpCause) {
        super();
        this.rpCause = rpCause;
    }

    public ReleaseSMSRequestImpl() {
        super();
    }

    @Override
    public RPCause getRPCause() {
        return this.rpCause;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.releaseSMS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.releaseSMS;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ReleaseSMSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.rpCause != null) {
            sb.append(", rpCause=");
            sb.append(rpCause.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
