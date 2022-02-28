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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.EndUserAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPAddress;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeNumber;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.PDPTypeOrganization;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EndUserAddressImpl implements EndUserAddress {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1, defaultImplementation = PDPTypeOrganizationImpl.class)
    private PDPTypeOrganization pdpTypeOrganization;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = PDPTypeNumberImpl.class)
    private PDPTypeNumber pdpTypeNumber;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = PDPAddressImpl.class)
    private PDPAddress pdpAddress;

    public EndUserAddressImpl() {
    }

    public EndUserAddressImpl(PDPTypeOrganization pdpTypeOrganization, PDPTypeNumber pdpTypeNumber, PDPAddress pdpAddress) {
        this.pdpTypeOrganization = pdpTypeOrganization;
        this.pdpTypeNumber = pdpTypeNumber;
        this.pdpAddress = pdpAddress;
    }

    public PDPTypeOrganization getPDPTypeOrganization() {
        return this.pdpTypeOrganization;
    }

    public PDPTypeNumber getPDPTypeNumber() {
        return this.pdpTypeNumber;
    }

    public PDPAddress getPDPAddress() {
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(pdpTypeOrganization==null)
			throw new ASNParsingComponentException("pdp type organization should be set for end user address", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(pdpTypeNumber==null)
			throw new ASNParsingComponentException("pdp type number should be set for end user address", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}