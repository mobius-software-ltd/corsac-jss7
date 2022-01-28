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

package org.restcomm.protocols.ss7.map.service.pdpContextActivation;

import org.restcomm.protocols.ss7.commonapp.api.primitives.GSNAddress;
import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.GSNAddressImpl;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.MAPMessageType;
import org.restcomm.protocols.ss7.map.api.MAPOperationCode;
import org.restcomm.protocols.ss7.map.api.service.pdpContextActivation.SendRoutingInfoForGprsResponse;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
*
* @author sergey vetyutnev
*
*/
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class SendRoutingInfoForGprsResponseImpl extends PdpContextActivationMessageImpl implements SendRoutingInfoForGprsResponse {
	private static final long serialVersionUID = 1L;

    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress sgsnAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=false,index=-1, defaultImplementation = GSNAddressImpl.class)
    private GSNAddress ggsnAddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNInteger mobileNotReachableReason;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public SendRoutingInfoForGprsResponseImpl() {
    }

    public SendRoutingInfoForGprsResponseImpl(GSNAddress sgsnAddress, GSNAddress ggsnAddress, Integer mobileNotReachableReason,
    		MAPExtensionContainer extensionContainer) {
        this.sgsnAddress = sgsnAddress;
        this.ggsnAddress = ggsnAddress;
        
        if(mobileNotReachableReason!=null)
        	this.mobileNotReachableReason = new ASNInteger(mobileNotReachableReason);
        	
        this.extensionContainer = extensionContainer;
    }

    @Override
    public MAPMessageType getMessageType() {
        return MAPMessageType.sendRoutingInfoForGprs_Response;
    }

    @Override
    public int getOperationCode() {
        return MAPOperationCode.sendRoutingInfoForGprs;
    }

    @Override
    public GSNAddress getSgsnAddress() {
        return sgsnAddress;
    }

    @Override
    public GSNAddress getGgsnAddress() {
        return ggsnAddress;
    }

    @Override
    public Integer getMobileNotReachableReason() {
    	if(mobileNotReachableReason==null)
    		return null;
    	
        return mobileNotReachableReason.getIntValue();
    }

    @Override
    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRoutingInfoForGprsResponse [");

        if (this.sgsnAddress != null) {
            sb.append("sgsnAddress=");
            sb.append(sgsnAddress);
            sb.append(", ");
        }
        if (this.ggsnAddress != null) {
            sb.append("ggsnAddress=");
            sb.append(ggsnAddress);
            sb.append(", ");
        }
        if (this.mobileNotReachableReason != null) {
            sb.append("mobileNotReachableReason=");
            sb.append(mobileNotReachableReason);
            sb.append(", ");
        }
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(extensionContainer);
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }

}
