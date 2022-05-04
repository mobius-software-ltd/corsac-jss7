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

import org.restcomm.protocols.ss7.commonapp.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SM_RP_OAForTestImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private AddressStringImpl serviceCentreAddressOA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull noSMRPUA=new ASNNull();
    
    public SM_RP_OAForTestImpl() {
    }

    public void setMsisdn(ISDNAddressStringImpl msisdn) {
        this.msisdn = msisdn;
        this.noSMRPUA=null;
    }

    public void setServiceCentreAddressOA(AddressStringImpl serviceCentreAddressOA) {
        this.serviceCentreAddressOA = serviceCentreAddressOA;
        this.noSMRPUA=null;
    }

    public ISDNAddressStringImpl getMsisdn() {
        return this.msisdn;
    }

    public AddressStringImpl getServiceCentreAddressOA() {
        return this.serviceCentreAddressOA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SM_RP_OA [");

        if (this.msisdn != null) {
            sb.append("msisdn=");
            sb.append(this.msisdn.toString());
        }
        if (this.serviceCentreAddressOA != null) {
            sb.append("serviceCentreAddressOA=");
            sb.append(this.serviceCentreAddressOA.toString());
        }

        if(this.noSMRPUA!=null) {
            sb.append("noSMRPUA");            
        }
        
        sb.append("]");

        return sb.toString();
    }
}
