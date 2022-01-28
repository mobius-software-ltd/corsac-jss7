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

package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import java.util.List;

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.CallBarringData;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarringFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.Password;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarringFeatureListWrapperImpl;
import org.restcomm.protocols.ss7.map.service.supplementary.PasswordImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 25/05/16.
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class CallBarringDataImpl implements CallBarringData {
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=0)
	private ExtCallBarringFeatureListWrapperImpl callBarringFeatureList;
	
	@ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=18,constructed=false,index=-1,defaultImplementation = PasswordImpl.class)
	private Password password;
    
	private ASNInteger wrongPasswordAttemptsCounter;
    private ASNNull notificationToCSE;
    
    @ASNProperty(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;

    public CallBarringDataImpl() {
    }

    public CallBarringDataImpl(List<ExtCallBarringFeature> callBarringFeatureList, Password password,
            Integer wrongPasswordAttemptsCounter, boolean notificationToCSE, MAPExtensionContainer extensionContainer) {
        if(callBarringFeatureList!=null)
        	this.callBarringFeatureList = new ExtCallBarringFeatureListWrapperImpl(callBarringFeatureList);
        
        this.password = password;
        
        if(wrongPasswordAttemptsCounter!=null)
        	this.wrongPasswordAttemptsCounter = new ASNInteger(wrongPasswordAttemptsCounter);
        	
        if(notificationToCSE)
        	this.notificationToCSE = new ASNNull();
        
        this.extensionContainer = extensionContainer;
    }

    public List<ExtCallBarringFeature> getCallBarringFeatureList() {
    	if(this.callBarringFeatureList==null)
    		return null;
    	
        return this.callBarringFeatureList.getExtCallBarringFeature();
    }

    public Password getPassword() {
        return this.password;
    }

    public Integer getWrongPasswordAttemptsCounter() {
    	if(this.wrongPasswordAttemptsCounter==null)
    		return null;
    	
        return this.wrongPasswordAttemptsCounter.getIntValue();
    }

    public boolean getNotificationToCSE() {
        return this.notificationToCSE!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CallBarringData [");

        if (this.callBarringFeatureList != null && this.callBarringFeatureList.getExtCallBarringFeature()!=null) {
            sb.append("callBarringFeatureList=[");
            boolean firstItem = true;
            for (ExtCallBarringFeature extCallBarringFeature: callBarringFeatureList.getExtCallBarringFeature()) {
                if (firstItem) {
                    firstItem = false;
                } else {
                    sb.append(", ");
                }
                sb.append(extCallBarringFeature);
            }
            sb.append("]");
        }
        if (this.password != null) {
            sb.append(", password=");
            sb.append(this.password);
        }
        if (this.wrongPasswordAttemptsCounter != null) {
            sb.append(", wrongPasswordAttemptsCounter=");
            sb.append(this.wrongPasswordAttemptsCounter.getValue());
        }
        if (this.notificationToCSE!=null) {
            sb.append(", notificationToCSE");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }

        sb.append("]");
        return sb.toString();
    }
}
