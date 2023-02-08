/*
 * Mobius Software LTD
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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoS;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.GPRSQoSExtension;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.QualityOfService;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class QualityOfServiceImpl implements QualityOfService {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private GPRSQoSWrapperImpl requestedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private GPRSQoSWrapperImpl subscribedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1)
    private GPRSQoSWrapperImpl negotiatedQoS;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true,index = -1, defaultImplementation = GPRSQoSExtensionImpl.class)
    private GPRSQoSExtension requestedQoSExtension;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true,index = -1, defaultImplementation = GPRSQoSExtensionImpl.class)
    private GPRSQoSExtension subscribedQoSExtension;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = true,index = -1, defaultImplementation = GPRSQoSExtensionImpl.class)
    private GPRSQoSExtension negotiatedQoSExtension;

    public QualityOfServiceImpl() {
    }

    public QualityOfServiceImpl(GPRSQoS requestedQoS, GPRSQoS subscribedQoS, GPRSQoS negotiatedQoS,
    		GPRSQoSExtension requestedQoSExtension, GPRSQoSExtension subscribedQoSExtension,
    		GPRSQoSExtension negotiatedQoSExtension) {
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

    public GPRSQoS getRequestedQoS() {
    	if(this.requestedQoS==null)
    		return null;
    	
        return this.requestedQoS.getGPRSQoS();
    }

    public GPRSQoS getSubscribedQoS() {
    	if(this.subscribedQoS==null)
    		return null;
    	
        return this.subscribedQoS.getGPRSQoS();
    }

    public GPRSQoS getNegotiatedQoS() {
    	if(this.negotiatedQoS==null)
    		return null;
    	
        return this.negotiatedQoS.getGPRSQoS();
    }

    public GPRSQoSExtension getRequestedQoSExtension() {
        return this.requestedQoSExtension;
    }

    public GPRSQoSExtension getSubscribedQoSExtension() {
        return this.subscribedQoSExtension;
    }

    public GPRSQoSExtension getNegotiatedQoSExtension() {
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
