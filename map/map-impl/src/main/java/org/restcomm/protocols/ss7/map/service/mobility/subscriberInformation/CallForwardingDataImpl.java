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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallForwardingData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwFeature;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwFeatureListWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 24/05/16.
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallForwardingDataImpl implements CallForwardingData {
	private ExtForwFeatureListWrapperImpl forwardingFeatureList;
    private ASNNull isNotificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=0,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public CallForwardingDataImpl() {
    }

    public CallForwardingDataImpl(List<ExtForwFeature> forwardingFeatureList, boolean isNotificationToCSE,
            MAPExtensionContainer extensionContainer) {
    	
    	if(forwardingFeatureList!=null)
    		this.forwardingFeatureList = new ExtForwFeatureListWrapperImpl(forwardingFeatureList);
        
        if(isNotificationToCSE)
        	this.isNotificationToCSE = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public List<ExtForwFeature> getForwardingFeatureList() {
    	if(this.forwardingFeatureList==null)
    		return null;
    	
        return this.forwardingFeatureList.getExtForwFeature();
    }

    public boolean getNotificationToCSE() {
        return this.isNotificationToCSE!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallForwardingData [");

        if (this.forwardingFeatureList != null && this.forwardingFeatureList.getExtForwFeature()!=null) {
            sb.append("forwardingFeatureList=[");
            boolean firstItem = true;
            for (ExtForwFeature extForwFeature: forwardingFeatureList.getExtForwFeature()) {
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
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(forwardingFeatureList==null)
			throw new ASNParsingComponentException("forwarding feature list should be set for call forwarding data", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
