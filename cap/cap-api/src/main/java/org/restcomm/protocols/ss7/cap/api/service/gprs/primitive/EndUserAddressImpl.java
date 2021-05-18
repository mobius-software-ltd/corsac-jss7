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
package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EndUserAddressImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private PDPTypeOrganizationImpl pdpTypeOrganization;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private PDPTypeNumberImpl pdpTypeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1)
    private PDPAddressImpl pdpAddress;

    public EndUserAddressImpl() {
    }

    public EndUserAddressImpl(PDPTypeOrganizationImpl pdpTypeOrganization, PDPTypeNumberImpl pdpTypeNumber, PDPAddressImpl pdpAddress) {
        this.pdpTypeOrganization = pdpTypeOrganization;
        this.pdpTypeNumber = pdpTypeNumber;
        this.pdpAddress = pdpAddress;
    }

    public PDPTypeOrganizationImpl getPDPTypeOrganization() {
        return this.pdpTypeOrganization;
    }

    public PDPTypeNumberImpl getPDPTypeNumber() {
        return this.pdpTypeNumber;
    }

    public PDPAddressImpl getPDPAddress() {
        return this.pdpAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EndUserAddress [");

        if (this.pdpTypeOrganization != null) {
            sb.append("pdpTypeOrganization=");
            sb.append(this.pdpTypeOrganization.toString());
            sb.append(", ");
        }

        if (this.pdpTypeNumber != null) {
            sb.append("pdpTypeNumber=");
            sb.append(this.pdpTypeNumber.toString());
            sb.append(", ");
        }

        if (this.pdpAddress != null) {
            sb.append("pdpAddress=");
            sb.append(this.pdpAddress.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
