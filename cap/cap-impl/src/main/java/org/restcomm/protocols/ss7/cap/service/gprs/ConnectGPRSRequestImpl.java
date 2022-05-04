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
import org.restcomm.protocols.ss7.cap.api.service.gprs.ConnectGPRSRequest;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.AccessPointName;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.AccessPointNameImpl;
import org.restcomm.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;

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
public class ConnectGPRSRequestImpl extends GprsMessageImpl implements ConnectGPRSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC ,tag = 0,constructed = false,index = -1,defaultImplementation = AccessPointNameImpl.class)
    private AccessPointName accessPointName;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC ,tag = 1,constructed = false,index = -1,defaultImplementation = PDPIDImpl.class)
    private PDPID pdpID;

    public ConnectGPRSRequestImpl() {
    }

    public ConnectGPRSRequestImpl(AccessPointName accessPointName, PDPID pdpID) {
        super();
        this.accessPointName = accessPointName;
        this.pdpID = pdpID;
    }

    @Override
    public AccessPointName getAccessPointName() {
        return this.accessPointName;
    }

    @Override
    public PDPID getPDPID() {
        return this.pdpID;
    }

    @Override
    public CAPMessageType getMessageType() {
        return CAPMessageType.connectGPRS_Request;
    }

    @Override
    public int getOperationCode() {
        return CAPOperationCode.connectGPRS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ConnectGPRSRequest [");
        this.addInvokeIdInfo(sb);

        if (this.accessPointName != null) {
            sb.append(", accessPointName=");
            sb.append(this.accessPointName.toString());
            sb.append(", ");
        }

        if (this.pdpID != null) {
            sb.append(", pdpID=");
            sb.append(this.pdpID.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(accessPointName==null)
			throw new ASNParsingComponentException("access point name should be set for connect gprs request", ASNParsingComponentExceptionReason.MistypedRootParameter);			
	}
}