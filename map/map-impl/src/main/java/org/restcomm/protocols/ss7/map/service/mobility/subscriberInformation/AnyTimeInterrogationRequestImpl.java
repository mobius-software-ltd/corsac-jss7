/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AnyTimeInterrogationRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class AnyTimeInterrogationRequestImpl extends MobilityMessageImpl implements AnyTimeInterrogationRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private SubscriberIdentityWrapperImpl subscriberIdentity;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private RequestedInfoImpl requestedInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private ISDNAddressStringImpl gsmSCFAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public AnyTimeInterrogationRequestImpl() {

    }

    public AnyTimeInterrogationRequestImpl(SubscriberIdentityImpl subscriberIdentity, RequestedInfoImpl requestedInfo,
            ISDNAddressStringImpl gsmSCFAddress, MAPExtensionContainerImpl extensionContainer) {
    	
    	if(subscriberIdentity!=null)
    		this.subscriberIdentity = new SubscriberIdentityWrapperImpl(subscriberIdentity);
    	
        this.requestedInfo = requestedInfo;
        this.gsmSCFAddress = gsmSCFAddress;
        this.extensionContainer = extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeInterrogationRequestIndication#getSubscriberIdentity()
     */
    public SubscriberIdentityImpl getSubscriberIdentity() {
    	if(this.subscriberIdentity==null)
    		return null;
        
    	return this.subscriberIdentity.getSubscriberIdentity();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeInterrogationRequestIndication#getRequestedInfo()
     */
    public RequestedInfoImpl getRequestedInfo() {
        return this.requestedInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeInterrogationRequestIndication#getGsmSCFAddress()
     */
    public ISDNAddressStringImpl getGsmSCFAddress() {
        return this.gsmSCFAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.subscriberInformation.
     * AnyTimeInterrogationRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPMessage#getMessageType()
     */
    public MAPMessageType getMessageType() {
        return MAPMessageType.anyTimeInterrogation_Request;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.MAPMessage#getOperationCode()
     */
    public int getOperationCode() {
        return MAPOperationCode.anyTimeInterrogation;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnyTimeInterrogationRequest [");

        if (this.subscriberIdentity != null && this.subscriberIdentity.getSubscriberIdentity()!=null) {
            sb.append("subscriberIdentity=");
            sb.append(this.subscriberIdentity);
        }
        if (this.requestedInfo != null) {
            sb.append(", requestedInfo=");
            sb.append(this.requestedInfo);
        }
        if (this.gsmSCFAddress != null) {
            sb.append(", gsmSCFAddress=");
            sb.append(this.gsmSCFAddress);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
