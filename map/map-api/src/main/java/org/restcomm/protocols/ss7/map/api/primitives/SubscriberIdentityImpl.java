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

package org.restcomm.protocols.ss7.map.api.primitives;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SubscriberIdentityImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl imsi = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private ISDNAddressStringImpl msisdn = null;

    /**
     *
     */
    public SubscriberIdentityImpl() {
        super();
    }

    /**
     * @param imsi
     */
    public SubscriberIdentityImpl(IMSIImpl imsi) {
        super();
        this.imsi = imsi;
        this.msisdn = null;
    }

    /**
     * @param msisdn
     */
    public SubscriberIdentityImpl(ISDNAddressStringImpl msisdn) {
        super();
        this.msisdn = msisdn;
        this.imsi = null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getIMSI ()
     */
    public IMSIImpl getIMSI() {
        return this.imsi;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.SubscriberIdentity#getMSISDN ()
     */
    public ISDNAddressStringImpl getMSISDN() {
        return this.msisdn;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
        result = prime * result + ((msisdn == null) ? 0 : msisdn.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubscriberIdentityImpl other = (SubscriberIdentityImpl) obj;
        if (imsi == null) {
            if (other.imsi != null)
                return false;
        } else if (!imsi.equals(other.imsi))
            return false;
        if (msisdn == null) {
            if (other.msisdn != null)
                return false;
        } else if (!msisdn.equals(other.msisdn))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SubscriberIdentity [");

        if (this.imsi != null) {
            sb.append(" imsi=");
            sb.append(this.imsi);
        }
        if (this.msisdn != null) {
            sb.append(" msisdn=");
            sb.append(this.msisdn);
        }

        sb.append("]");

        return sb.toString();
    }
}
