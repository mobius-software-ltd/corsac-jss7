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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressString;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.ForwardingOptionsImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author cristian veliscu
 * @author yulianoifa
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ForwardingDataImpl implements ForwardingData {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1,defaultImplementation = ISDNAddressStringImpl.class)
	private ISDNAddressString forwardedToNumber;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = ISDNSubaddressStringImpl.class)
	private ISDNSubaddressString forwardedToSubaddress;
	
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = ForwardingOptionsImpl.class)
	private ForwardingOptions forwardingOptions;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
	private MAPExtensionContainer extensionContainer;
    
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = FTNAddressStringImpl.class)
	private FTNAddressString longForwardedToNumber;

    public ForwardingDataImpl() {
    }

    public ForwardingDataImpl(ISDNAddressString forwardedToNumber, ISDNSubaddressString forwardedToSubaddress,
    		ForwardingOptions forwardingOptions, MAPExtensionContainer extensionContainer,
            FTNAddressString longForwardedToNumber) {
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        this.extensionContainer = extensionContainer;
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public ISDNAddressString getForwardedToNumber() {
        return this.forwardedToNumber;
    }

    public ISDNSubaddressString getForwardedToSubaddress() {
        return this.forwardedToSubaddress;
    }

    public ForwardingOptions getForwardingOptions() {
        return this.forwardingOptions;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public FTNAddressString getLongForwardedToNumber() {
        return this.longForwardedToNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardingData [");

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