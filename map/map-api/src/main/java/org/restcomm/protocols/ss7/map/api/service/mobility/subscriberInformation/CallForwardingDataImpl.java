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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeatureImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeatureListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

import java.util.ArrayList;

/**
 * Created by vsubbotin on 24/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallForwardingDataImpl {
	private ExtForwFeatureListWrapperImpl forwardingFeatureList;
    private ASNNull isNotificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer;

    public CallForwardingDataImpl() {
    }

    public CallForwardingDataImpl(ArrayList<ExtForwFeatureImpl> forwardingFeatureList, boolean isNotificationToCSE,
            MAPExtensionContainerImpl extensionContainer) {
    	
    	if(forwardingFeatureList!=null)
    		this.forwardingFeatureList = new ExtForwFeatureListWrapperImpl(forwardingFeatureList);
        
        if(isNotificationToCSE)
        	this.isNotificationToCSE = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public ArrayList<ExtForwFeatureImpl> getForwardingFeatureList() {
    	if(this.forwardingFeatureList==null)
    		return null;
    	
        return this.forwardingFeatureList.getExtForwFeature();
    }

    public boolean getNotificationToCSE() {
        return this.isNotificationToCSE!=null;
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallForwardingData [");

        if (this.forwardingFeatureList != null && this.forwardingFeatureList.getExtForwFeature()!=null) {
            sb.append("forwardingFeatureList=[");
            boolean firstItem = true;
            for (ExtForwFeatureImpl extForwFeature: forwardingFeatureList.getExtForwFeature()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extForwFeature);
            }
            sb.append("], ");
        }
        if (isNotificationToCSE!=null) {
            sb.append("isNotificationToCSE, ");
        }
        
        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer);
            sb.append(", ");
        }

        sb.append("]");
        return sb.toString();
    }
}
