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

package org.restcomm.protocols.ss7.map.service.sms;

import org.restcomm.protocols.ss7.commonapp.api.primitives.AddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.sms.AlertServiceCentreRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AlertServiceCentreRequestImpl extends SmsMessageImpl implements AlertServiceCentreRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=0, defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString msisdn;
    
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=4,constructed=false,index=1, defaultImplementation = AddressStringImpl.class)
	private AddressString serviceCentreAddress;
    private int operationCode;

    public AlertServiceCentreRequestImpl() {
    	
    }
    
    public AlertServiceCentreRequestImpl(int operationCode) {
        this.operationCode = operationCode;
    }

    public AlertServiceCentreRequestImpl(ISDNAddressString msisdn, AddressString serviceCentreAddress) {
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

    public ISDNAddressString getMsisdn() {
        return this.msisdn;
    }

    public AddressString getServiceCentreAddress() {
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(msisdn==null)
			throw new ASNParsingComponentException("msisdn should be set for alert service center request", ASNParsingComponentExceptionReason.MistypedRootParameter);

		if(serviceCentreAddress==null)
			throw new ASNParsingComponentException("service centre address should be set for alert service center request", ASNParsingComponentExceptionReason.MistypedRootParameter);
	}
}
