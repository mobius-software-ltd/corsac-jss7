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

package org.restcomm.protocols.ss7.cap.service.sms;

import org.restcomm.protocols.ss7.cap.api.CAPMessageType;
import org.restcomm.protocols.ss7.cap.api.CAPOperationCode;
import org.restcomm.protocols.ss7.cap.api.service.sms.ReleaseSMSRequest;
import org.restcomm.protocols.ss7.cap.api.service.sms.primitive.RPCause;
import org.restcomm.protocols.ss7.cap.service.sms.primitive.RPCauseImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNWrappedTag;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(rpCause==null)
			throw new ASNParsingComponentException("rp cause should be set for release sms request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}
