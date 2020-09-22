/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.api.service.sms;

import org.restcomm.protocols.ss7.map.api.primitives.AddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=16,constructed=true,lengthIndefinite=false)
public class SM_RP_DAImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=0)
    private LMSIImpl lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=0)
    private AddressStringImpl serviceCentreAddressDA;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=0)
    private ASNNull noSMRPDA=new ASNNull();
    
    public SM_RP_DAImpl() {
    }

    public SM_RP_DAImpl(IMSIImpl imsi) {
        this.imsi = imsi;
    }

    public SM_RP_DAImpl(LMSIImpl lmsi) {
        this.lmsi = lmsi;
    }

    public SM_RP_DAImpl(AddressStringImpl serviceCentreAddressDA) {
        this.serviceCentreAddressDA = serviceCentreAddressDA;
    }

    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    public LMSIImpl getLMSI() {
        return this.lmsi;
    }

    public AddressStringImpl getServiceCentreAddressDA() {
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
}
