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
package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDNGWIdentityImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private PDPAddressImpl pdnGwIpv4Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private PDPAddressImpl pdnGwIpv6Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private FQDNImpl pdnGwName;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public PDNGWIdentityImpl() {
    }

    public PDNGWIdentityImpl(PDPAddressImpl pdnGwIpv4Address, PDPAddressImpl pdnGwIpv6Address, FQDNImpl pdnGwName,
            MAPExtensionContainerImpl extensionContainer) {
        this.pdnGwIpv4Address = pdnGwIpv4Address;
        this.pdnGwIpv6Address = pdnGwIpv6Address;
        this.pdnGwName = pdnGwName;
        this.extensionContainer = extensionContainer;
    }

    public PDPAddressImpl getPdnGwIpv4Address() {
        return this.pdnGwIpv4Address;
    }

    public PDPAddressImpl getPdnGwIpv6Address() {
        return this.pdnGwIpv6Address;
    }

    public FQDNImpl getPdnGwName() {
        return this.pdnGwName;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PDNGWIdentity [");

        if (this.pdnGwIpv4Address != null) {
            sb.append("pdnGwIpv4Address=");
            sb.append(this.pdnGwIpv4Address.toString());
            sb.append(", ");
        }

        if (this.pdnGwIpv6Address != null) {
            sb.append("pdnGwIpv6Address=");
            sb.append(this.pdnGwIpv6Address.toString());
            sb.append(", ");
        }

        if (this.pdnGwName != null) {
            sb.append("pdnGwName=");
            sb.append(this.pdnGwName.toString());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());

        }

        sb.append("]");

        return sb.toString();
    }
}
