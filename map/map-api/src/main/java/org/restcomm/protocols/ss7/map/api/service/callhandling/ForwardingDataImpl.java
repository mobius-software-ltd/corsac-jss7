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

package org.restcomm.protocols.ss7.map.api.service.callhandling;

import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/*
 *
 * @author cristian veliscu
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ForwardingDataImpl {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
	private ISDNAddressStringImpl forwardedToNumber;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
	private ISDNSubaddressStringImpl forwardedToSubaddress;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
	private ForwardingOptionsImpl forwardingOptions;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1)
	private MAPExtensionContainerImpl extensionContainer;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
	private FTNAddressStringImpl longForwardedToNumber;

    private static final String _PrimitiveName = "ForwardingData";

    public ForwardingDataImpl() {
    }

    public ForwardingDataImpl(ISDNAddressStringImpl forwardedToNumber, ISDNSubaddressStringImpl forwardedToSubaddress,
            ForwardingOptionsImpl forwardingOptions, MAPExtensionContainerImpl extensionContainer,
            FTNAddressStringImpl longForwardedToNumber) {
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        this.extensionContainer = extensionContainer;
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public ISDNAddressStringImpl getForwardedToNumber() {
        return this.forwardedToNumber;
    }

    public ISDNSubaddressStringImpl getForwardedToSubaddress() {
        return this.forwardedToSubaddress;
    }

    public ForwardingOptionsImpl getForwardingOptions() {
        return this.forwardingOptions;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public FTNAddressStringImpl getLongForwardedToNumber() {
        return this.longForwardedToNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (this.forwardedToNumber != null) {
            sb.append("forwardedToNumber=[");
            sb.append(this.forwardedToNumber);
            sb.append("], ");
        }

        if (this.forwardedToSubaddress != null) {
            sb.append("forwardedToSubaddress=[");
            sb.append(this.forwardedToSubaddress);
            sb.append("], ");
        }

        if (this.forwardingOptions != null) {
            sb.append("forwardingOptions=[");
            sb.append(this.forwardingOptions);
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=[");
            sb.append(this.extensionContainer);
            sb.append("], ");
        }

        if (this.longForwardedToNumber != null) {
            sb.append("longForwardedToNumber=[");
            sb.append(this.longForwardedToNumber);
            sb.append("]");
        }

        sb.append("]");
        return sb.toString();
    }
}