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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.APNOIReplacement;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.GPRSSubscriptionData;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation.PDPContextListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author daniel bichara
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class GPRSSubscriptionDataImpl implements GPRSSubscriptionData {
	private ASNNull completeDataListIncluded = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1)
    private PDPContextListWrapperImpl gprsDataList = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1,defaultImplementation = APNOIReplacementImpl.class)
    private APNOIReplacement apnOiReplacement = null;

    public GPRSSubscriptionDataImpl() {
    }

    public GPRSSubscriptionDataImpl(boolean completeDataListIncluded, List<PDPContext> gprsDataList,
            MAPExtensionContainer extensionContainer, APNOIReplacement apnOiReplacement) {
        if(completeDataListIncluded)
        	this.completeDataListIncluded = new ASNNull();
        
        if(gprsDataList!=null)
        	this.gprsDataList = new PDPContextListWrapperImpl(gprsDataList);
        
        this.extensionContainer = extensionContainer;
        this.apnOiReplacement = apnOiReplacement;
    }

    public boolean getCompleteDataListIncluded() {
        return this.completeDataListIncluded!=null;
    }

    public List<PDPContext> getGPRSDataList() {
    	if(this.gprsDataList==null)
    		return null;
    	
        return this.gprsDataList.getPDPContextList();
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public APNOIReplacement getApnOiReplacement() {
        return this.apnOiReplacement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GPRSSubscriptionData [");

        if (this.getCompleteDataListIncluded()) {
            sb.append("completeDataListIncluded, ");
        }

        if (this.gprsDataList != null && this.gprsDataList.getPDPContextList()!=null) {
            sb.append("gprsDataList=[");
            boolean firstItem = true;
            for (PDPContext be : this.gprsDataList.getPDPContextList()) {
                if (firstItem)
                    firstItem = false;
                else
                    sb.append(", ");
                sb.append(be.toString());
            }
            sb.append("], ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.apnOiReplacement != null) {
            sb.append("apnOiReplacement=");
            sb.append(this.apnOiReplacement.toString());
            sb.append(", ");
        }

        sb.append("]");

        return sb.toString();
    }
}
