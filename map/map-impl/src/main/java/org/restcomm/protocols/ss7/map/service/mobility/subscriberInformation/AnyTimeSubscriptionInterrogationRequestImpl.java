/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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


package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeSubscriptionInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityWrapperImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 24/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AnyTimeSubscriptionInterrogationRequestImpl extends MobilityMessageImpl implements AnyTimeSubscriptionInterrogationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private SubscriberIdentityWrapperImpl subscriberIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = RequestedSubscriptionInfoImpl.class)
    private RequestedSubscriptionInfo requestedSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer mapExtensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull isLongFTNSupported;

    public AnyTimeSubscriptionInterrogationRequestImpl() {

    }

    public AnyTimeSubscriptionInterrogationRequestImpl(SubscriberIdentity subscriberIdentity, RequestedSubscriptionInfo requestedSubscriptionInfo,
            ISDNAddressString gsmSCFAddress, MAPExtensionContainer mapExtensionContainer, boolean isLongFTNSupported) {
       
    	if(subscriberIdentity!=null)
    		this.subscriberIdentity = new SubscriberIdentityWrapperImpl(subscriberIdentity);
    	
        this.requestedSubscriptionInfo = requestedSubscriptionInfo;
        this.gsmSCFAddress = gsmSCFAddress;
        this.mapExtensionContainer = mapExtensionContainer;
        
        if(isLongFTNSupported)
        	this.isLongFTNSupported = new ASNNull();
    }

    public SubscriberIdentity getSubscriberIdentity() {
    	if(this.subscriberIdentity==null)
    		return null;
    	
        return this.subscriberIdentity.getSubscriberIdentity();
    }

    public RequestedSubscriptionInfo getRequestedSubscriptionInfo() {
        return this.requestedSubscriptionInfo;
    }

    public ISDNAddressString getGsmScfAddress() {
        return this.gsmSCFAddress;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.mapExtensionContainer;
    }

    public boolean getLongFTNSupported() {
        return this.isLongFTNSupported!=null;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeSubscriptionInterrogation_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.anyTimeSubscriptionInterrogation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnyTimeSubscriptionInterrogationRequest [");

        if (this.subscriberIdentity != null && this.subscriberIdentity.getSubscriberIdentity()!=null) {
            sb.append("subscriberIdentity=");
            sb.append(this.subscriberIdentity.getSubscriberIdentity());
        }
        if (this.requestedSubscriptionInfo != null) {
            sb.append(", requestedSubscriptionInfo=");
            sb.append(this.requestedSubscriptionInfo);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress);
        }
        if (this.mapExtensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.mapExtensionContainer);
        }

        if (this.isLongFTNSupported!=null) {
            sb.append(", isLongFTNSupported");
        }

        sb.append("]");
        return sb.toString();
    }
}
