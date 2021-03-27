/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AlertServiceCentreRequestImpl extends SmsMessageImpl implements AlertServiceCentreRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0)
	private ISDNAddressStringImpl msisdn;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1)
	private AddressStringImpl serviceCentreAddress;
    private int operationCode;

    public AlertServiceCentreRequestImpl() {
    	
    }
    
    public AlertServiceCentreRequestImpl(int operationCode) {
        this.operationCode = operationCode;
    }

    public AlertServiceCentreRequestImpl(ISDNAddressStringImpl msisdn, AddressStringImpl serviceCentreAddress) {
        this.msisdn = msisdn;
        this.serviceCentreAddress = serviceCentreAddress;
    }

    public MAPMessageType getMessageType() {
        if (this.operationCode == MAPOperationCode.alertServiceCentre)
            return MAPMessageType.alertServiceCentre_Request;
        else
            return MAPMessageType.alertServiceCentreWithoutResult_Request;
    }

    public int getOperationCode() {
        return this.operationCode;
    }

    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    public AddressStringImpl getServiceCentreAddress() {
        return this.serviceCentreAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlertServiceCentreRequest [");

        if (this.getMAPDialog() != null) {
            sb.append("DialogId=").append(this.getMAPDialog().getLocalDialogId());
        }

        if (this.msisdn != null) {
            sb.append(", msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.serviceCentreAddress != null) {
            sb.append(", serviceCentreAddress=");
            sb.append(this.serviceCentreAddress.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
