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

package org.restcomm.protocols.ss7.map.api.service.lsm;

import org.restcomm.protocols.ss7.map.api.primitives.DiameterIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ServingNodeAddressImpl {
	public static final String _PrimitiveName = "ProvideSubscriberLocationResponse";

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private ISDNAddressStringImpl mscNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ISDNAddressStringImpl sgsnNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private DiameterIdentityImpl mmeNumber;

    public ServingNodeAddressImpl() {
    }

    public ServingNodeAddressImpl(ISDNAddressStringImpl isdnNumber, boolean isMsc) {
        if (isMsc)
            this.mscNumber = isdnNumber;
        else
            this.sgsnNumber = isdnNumber;
    }

    public ServingNodeAddressImpl(DiameterIdentityImpl mmeNumber) {
        this.mmeNumber = mmeNumber;
    }

    public ISDNAddressStringImpl getMscNumber() {
        return mscNumber;
    }

    public ISDNAddressStringImpl getSgsnNumber() {
        return sgsnNumber;
    }

    public DiameterIdentityImpl getMmeNumber() {
        return mmeNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideSubscriberLocationResponse [");

        if (this.mscNumber != null) {
            sb.append(" mscNumber=");
            sb.append(this.mscNumber);
        }
        if (this.sgsnNumber != null) {
            sb.append(" sgsnNumber=");
            sb.append(this.sgsnNumber);
        }
        if (this.mmeNumber != null) {
            sb.append(" mmeNumber=");
            sb.append(this.mmeNumber);
        }

        sb.append("]");

        return sb.toString();
    }
}
