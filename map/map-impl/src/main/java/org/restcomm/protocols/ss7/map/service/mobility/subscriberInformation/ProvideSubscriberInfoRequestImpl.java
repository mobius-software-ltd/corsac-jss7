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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.primitives.ASNEMLPPPriorityImpl;
import org.restcomm.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.restcomm.protocols.ss7.map.api.primitives.IMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.LMSIImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.ProvideSubscriberInfoRequest;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedInfoImpl;
import org.restcomm.protocols.ss7.map.service.mobility.MobilityMessageImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ProvideSubscriberInfoRequestImpl extends MobilityMessageImpl implements ProvideSubscriberInfoRequest {
	private static final long serialVersionUID = 1L;

	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1)
    private IMSIImpl imsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1)
    private LMSIImpl lmsi;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1)
    private RequestedInfoImpl requestedInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;    
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNEMLPPPriorityImpl callPriority;

    public ProvideSubscriberInfoRequestImpl() {
    }

    public ProvideSubscriberInfoRequestImpl(IMSIImpl imsi, LMSIImpl lmsi, RequestedInfoImpl requestedInfo, MAPExtensionContainerImpl extensionContainer,
            EMLPPPriority callPriority) {
        this.imsi = imsi;
        this.lmsi = lmsi;
        this.requestedInfo = requestedInfo;
        this.extensionContainer = extensionContainer;
        
        if(callPriority!=null) {
        	this.callPriority = new ASNEMLPPPriorityImpl();
        	this.callPriority.setType(callPriority);
        }
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.provideSubscriberInfo_Request;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.provideSubscriberInfo;
    }

    @Override
    public IMSIImpl getImsi() {
        return imsi;
    }

    @Override
    public LMSIImpl getLmsi() {
        return lmsi;
    }

    @Override
    public RequestedInfoImpl getRequestedInfo() {
        return requestedInfo;
    }

    @Override
    public MAPExtensionContainerImpl getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public EMLPPPriority getCallPriority() {
    	if(callPriority==null)
    		return null;
    	
    	return callPriority.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProvideSubscriberInfoRequest [");

        if (this.imsi != null) {
            sb.append("imsi=");
            sb.append(imsi);
            sb.append(", ");
        }
        if (this.lmsi != null) {
            sb.append("lmsi=");
            sb.append(lmsi);
            sb.append(", ");
        }
        if (this.requestedInfo != null) {
            sb.append("requestedInfo=");
            sb.append(requestedInfo);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }
        if (this.callPriority != null) {
            sb.append("callPriority=");
            sb.append(callPriority.getType());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
