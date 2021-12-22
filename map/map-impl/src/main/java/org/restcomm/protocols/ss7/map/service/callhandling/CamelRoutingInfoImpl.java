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

package org.restcomm.protocols.ss7.map.service.callhandling;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.callhandling.CamelRoutingInfo;
import org.restcomm.protocols.ss7.map.api.service.callhandling.ForwardingData;
import org.restcomm.protocols.ss7.map.api.service.callhandling.GmscCamelSubscriptionInfo;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CamelRoutingInfoImpl implements CamelRoutingInfo {
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1, defaultImplementation = ForwardingDataImpl.class)
	private ForwardingData forwardingData;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1, defaultImplementation = GmscCamelSubscriptionInfoImpl.class)
    private GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1, defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public CamelRoutingInfoImpl() {       
    }

    public CamelRoutingInfoImpl(ForwardingData forwardingData, GmscCamelSubscriptionInfo gmscCamelSubscriptionInfo,
    		MAPExtensionContainer extensionContainer) {
        this.forwardingData = forwardingData;
        this.gmscCamelSubscriptionInfo = gmscCamelSubscriptionInfo;
        this.extensionContainer = extensionContainer;
    }

    public ForwardingData getForwardingData() {
        return forwardingData;
    }

    public GmscCamelSubscriptionInfo getGmscCamelSubscriptionInfo() {
        return gmscCamelSubscriptionInfo;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CamelRoutingInfo [");

        if (this.forwardingData != null) {
            sb.append("forwardingData=");
            sb.append(this.forwardingData.toString());
        }
        if (this.gmscCamelSubscriptionInfo != null) {
            sb.append(", gmscCamelSubscriptionInfo=");
            sb.append(this.gmscCamelSubscriptionInfo.toString());
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer.toString());
        }

        sb.append("]");

        return sb.toString();
    }
}
