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
import org.restcomm.protocols.ss7.commonapp.api.primitives.IMSI;
import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSI;
import org.restcomm.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.restcomm.protocols.ss7.map.primitives.LMSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SM_RP_DAImpl implements SM_RP_DA {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0, defaultImplementation = IMSIImpl.class)
    private IMSI imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=0, defaultImplementation = LMSIImpl.class)
    private LMSI lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=0, defaultImplementation = AddressStringImpl.class)
    private AddressString serviceCentreAddressDA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=0)
    private ASNNull noSMRPDA=new ASNNull();
    
    public SM_RP_DAImpl() {
    }

    public SM_RP_DAImpl(IMSI imsi) {
        this.imsi = imsi;
        this.noSMRPDA = null;
    }

    public SM_RP_DAImpl(LMSI lmsi) {
        this.lmsi = lmsi;
        this.noSMRPDA = null;
    }

    public SM_RP_DAImpl(AddressString serviceCentreAddressDA) {
        this.serviceCentreAddressDA = serviceCentreAddressDA;
        this.noSMRPDA = null;
    }

    public IMSI getIMSI() {
        return this.imsi;
    }

    public LMSI getLMSI() {
        return this.lmsi;
    }

    public AddressString getServiceCentreAddressDA() {
        return serviceCentreAddressDA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SM_RP_DA [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(this.imsi.toString());
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(this.lmsi.toString());
        }
        if (this.serviceCentreAddressDA != null) {
            sb.append("serviceCentreAddressDA=");
            sb.append(this.serviceCentreAddressDA.toString());
        }

        if(this.noSMRPDA!=null) {
            sb.append("noSMRPDA");            
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(imsi==null && lmsi==null && serviceCentreAddressDA==null && noSMRPDA==null)
			throw new ASNParsingComponentException("one of child items should be set for send SM RP DA", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
