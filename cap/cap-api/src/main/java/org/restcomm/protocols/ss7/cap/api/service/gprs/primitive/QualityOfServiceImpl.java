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
public class QualityOfServiceImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private GPRSQoSWrapperImpl requestedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private GPRSQoSWrapperImpl subscribedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private GPRSQoSWrapperImpl negotiatedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1)
    private GPRSQoSExtensionImpl requestedQoSExtension;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1)
    private GPRSQoSExtensionImpl subscribedQoSExtension;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1)
    private GPRSQoSExtensionImpl negotiatedQoSExtension;

    public QualityOfServiceImpl() {
    }

    public QualityOfServiceImpl(GPRSQoSImpl requestedQoS, GPRSQoSImpl subscribedQoS, GPRSQoSImpl negotiatedQoS,
    		GPRSQoSExtensionImpl requestedQoSExtension, GPRSQoSExtensionImpl subscribedQoSExtension,
    		GPRSQoSExtensionImpl negotiatedQoSExtension) {
    	if(requestedQoS!=null)
    		this.requestedQoS = new GPRSQoSWrapperImpl(requestedQoS);
    	
    	if(subscribedQoS!=null)
    		this.subscribedQoS = new GPRSQoSWrapperImpl(subscribedQoS);
    	
    	if(negotiatedQoS!=null)
    		this.negotiatedQoS = new GPRSQoSWrapperImpl(negotiatedQoS);
    	
        this.requestedQoSExtension = requestedQoSExtension;
        this.subscribedQoSExtension = subscribedQoSExtension;
        this.negotiatedQoSExtension = negotiatedQoSExtension;
    }

    public GPRSQoSImpl getRequestedQoS() {
    	if(this.requestedQoS==null)
    		return null;
    	
        return this.requestedQoS.getGPRSQoS();
    }

    public GPRSQoSImpl getSubscribedQoS() {
    	if(this.subscribedQoS==null)
    		return null;
    	
        return this.subscribedQoS.getGPRSQoS();
    }

    public GPRSQoSImpl getNegotiatedQoS() {
    	if(this.negotiatedQoS==null)
    		return null;
    	
        return this.negotiatedQoS.getGPRSQoS();
    }

    public GPRSQoSExtensionImpl getRequestedQoSExtension() {
        return this.requestedQoSExtension;
    }

    public GPRSQoSExtensionImpl getSubscribedQoSExtension() {
        return this.subscribedQoSExtension;
    }

    public GPRSQoSExtensionImpl getNegotiatedQoSExtension() {
        return this.negotiatedQoSExtension;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("QualityOfService [");

        if (this.requestedQoS != null) {
            sb.append("requestedQoS=");
            sb.append(this.requestedQoS.toString());
            sb.append(", ");
        }

        if (this.subscribedQoS != null) {
            sb.append("subscribedQoS=");
            sb.append(this.subscribedQoS.toString());
            sb.append(", ");
        }

        if (this.negotiatedQoS != null) {
            sb.append("negotiatedQoS=");
            sb.append(this.negotiatedQoS.toString());
            sb.append(", ");
        }

        if (this.requestedQoSExtension != null) {
            sb.append("requestedQoSExtension=");
            sb.append(this.requestedQoSExtension.toString());
            sb.append(", ");
        }

        if (this.subscribedQoSExtension != null) {
            sb.append("subscribedQoSExtension=");
            sb.append(this.subscribedQoSExtension.toString());
            sb.append(", ");
        }

        if (this.negotiatedQoSExtension != null) {
            sb.append("negotiatedQoSExtension=");
            sb.append(this.negotiatedQoSExtension.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
