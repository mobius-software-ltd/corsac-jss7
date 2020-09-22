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

package org.restcomm.protocols.ss7.map.service.lsm;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentityWrapperImpl;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSRequest;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForLCSRequestImpl extends LsmMessageImpl implements SendRoutingInfoForLCSRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=0)
    private ISDNAddressStringImpl mlcNumber;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=1)
    private SubscriberIdentityWrapperImpl targetMS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;
    
    /**
     *
     */
    public SendRoutingInfoForLCSRequestImpl() {
        super();
    }

    /**
     * @param targetMS
     * @param mlcNumber
     */
    public SendRoutingInfoForLCSRequestImpl(ISDNAddressStringImpl mlcNumber, SubscriberIdentityImpl targetMS) {
        super();
        if(targetMS!=null)
        	this.targetMS = new SubscriberIdentityWrapperImpl(targetMS);
        this.mlcNumber = mlcNumber;
    }

    /**
     * @param extensionContainer
     * @param targetMS
     * @param mlcNumber
     */
    public SendRoutingInfoForLCSRequestImpl(ISDNAddressStringImpl mlcNumber, SubscriberIdentityImpl targetMS,
            MAPExtensionContainerImpl extensionContainer) {
        this(mlcNumber, targetMS);
        this.extensionContainer = extensionContainer;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForLCS_Request;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForLCS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getMLCNumber()
     */
    public ISDNAddressStringImpl getMLCNumber() {
        return this.mlcNumber;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getTargetMS()
     */
    public SubscriberIdentityImpl getTargetMS() {
    	if(this.targetMS==null)
    		return null;
    	
        return this.targetMS.getSubscriberIdentity();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSRequestIndication#getExtensionContainer()
     */
    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForLCSRequest [");

        if (this.mlcNumber != null) {
            sb.append("mlcNumber");
            sb.append(this.mlcNumber);
        }
        if (this.targetMS != null && this.targetMS.getSubscriberIdentity()!=null) {
            sb.append(", targetMS=");
            sb.append(this.targetMS.getSubscriberIdentity());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");

        return sb.toString();
    }
}
