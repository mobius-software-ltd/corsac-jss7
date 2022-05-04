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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.FQDN;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDNGWIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.PDPAddress;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class PDNGWIdentityImpl implements PDNGWIdentity {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = PDPAddressImpl.class)
    private PDPAddress pdnGwIpv4Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = PDPAddressImpl.class)
    private PDPAddress pdnGwIpv6Address;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = FQDNImpl.class)
    private FQDN pdnGwName;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public PDNGWIdentityImpl() {
    }

    public PDNGWIdentityImpl(PDPAddress pdnGwIpv4Address, PDPAddress pdnGwIpv6Address, FQDN pdnGwName,
            MAPExtensionContainer extensionContainer) {
        this.pdnGwIpv4Address = pdnGwIpv4Address;
        this.pdnGwIpv6Address = pdnGwIpv6Address;
        this.pdnGwName = pdnGwName;
        this.extensionContainer = extensionContainer;
    }

    public PDPAddress getPdnGwIpv4Address() {
        return this.pdnGwIpv4Address;
    }

    public PDPAddress getPdnGwIpv6Address() {
        return this.pdnGwIpv6Address;
    }

    public FQDN getPdnGwName() {
        return this.pdnGwName;
    }

    public MAPExtensionContainer getExtensionContainer() {
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
