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
import org.restcomm.protocols.ss7.map.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.SendRoutingInfoForLCSResponse;
import org.restcomm.protocols.ss7.map.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.primitives.SubscriberIdentityWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 * @author amit bhayani
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForLCSResponseImpl extends LsmMessageImpl implements SendRoutingInfoForLCSResponse {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=0)
    private SubscriberIdentityWrapperImpl targetMS;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=1, defaultImplementation = LCSLocationInfoImpl.class)
    private LCSLocationInfo lcsLocationInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress vgmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress hGmlcAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress pprAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress additionalVGmlcAddress;

    /**
     *
     */
    public SendRoutingInfoForLCSResponseImpl() {
        super();
    }

    /**
     * @param targetMS
     * @param lcsLocationInfo
     * @param extensionContainer
     * @param vgmlcAddress
     * @param hGmlcAddress
     * @param pprAddress
     * @param additionalVGmlcAddress
     */
    public SendRoutingInfoForLCSResponseImpl(SubscriberIdentity targetMS, LCSLocationInfo lcsLocationInfo,
    		MAPExtensionContainer extensionContainer, GSNAddress vgmlcAddress, GSNAddress hGmlcAddress, GSNAddress pprAddress,
    		GSNAddress additionalVGmlcAddress) {
        super();
        
        if(targetMS!=null)
        	this.targetMS = new SubscriberIdentityWrapperImpl(targetMS);
        
        this.lcsLocationInfo = lcsLocationInfo;
        this.extensionContainer = extensionContainer;
        this.vgmlcAddress = vgmlcAddress;
        this.hGmlcAddress = hGmlcAddress;
        this.pprAddress = pprAddress;
        this.additionalVGmlcAddress = additionalVGmlcAddress;
    }

    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForLCS_Response;
    }

    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForLCS;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getTargetMS()
     */
    public SubscriberIdentity getTargetMS() {
    	if(targetMS==null)
    		return null;
    	
        return this.targetMS.getSubscriberIdentity();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getLCSLocationInfo()
     */
    public LCSLocationInfo getLCSLocationInfo() {
        return this.lcsLocationInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getExtensionContainer()
     */
    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getVgmlcAddress()
     */
    public GSNAddress getVgmlcAddress() {
        return this.vgmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getHGmlcAddress()
     */
    public GSNAddress getHGmlcAddress() {
        return this.hGmlcAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm. SendRoutingInforForLCSResponseIndication#getPprAddress()
     */
    public GSNAddress getPprAddress() {
        return this.pprAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.restcomm.protocols.ss7.map.api.service.lsm.
     * SendRoutingInforForLCSResponseIndication#getAdditionalVGmlcAddress()
     */
    public GSNAddress getAdditionalVGmlcAddress() {
        return this.additionalVGmlcAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForLCSResponse [");

        if (this.targetMS != null && this.targetMS.getSubscriberIdentity()!=null) {
            sb.append("targetMS");
            sb.append(this.targetMS.getSubscriberIdentity());
        }
        if (this.lcsLocationInfo != null) {
            sb.append(", lcsLocationInfo=");
            sb.append(this.lcsLocationInfo);
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.vgmlcAddress != null) {
            sb.append(", vgmlcAddress=");
            sb.append(this.vgmlcAddress);
        }
        if (this.hGmlcAddress != null) {
            sb.append(", hGmlcAddress=");
            sb.append(this.hGmlcAddress);
        }
        if (this.pprAddress != null) {
            sb.append(", pprAddress=");
            sb.append(this.pprAddress);
        }
        if (this.additionalVGmlcAddress != null) {
            sb.append(", additionalVGmlcAddress=");
            sb.append(this.additionalVGmlcAddress);
        }

        sb.append("]");

        return sb.toString();
    }
}
