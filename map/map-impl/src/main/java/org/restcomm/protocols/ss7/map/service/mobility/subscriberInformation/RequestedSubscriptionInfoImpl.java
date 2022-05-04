/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

import org.restcomm.protocols.ss7.commonapp.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.commonapp.primitives.MAPExtensionContainerImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.AdditionalRequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedCAMELSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RequestedSubscriptionInfo;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSForBSCode;
import org.restcomm.protocols.ss7.map.service.supplementary.SSForBSCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 * Created by vsubbotin on 24/05/16.
 * @author yulianoifa
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class RequestedSubscriptionInfoImpl implements RequestedSubscriptionInfo {
	@ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=1,constructed=true,index=-1,defaultImplementation = SSForBSCodeImpl.class)
    private SSForBSCode ssForBSCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=2,constructed=false,index=-1)
    private ASNNull isOdb;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=3,constructed=false,index=-1)
    private ASNRequestedCamelSubscriptionInfoImpl requestedCAMELSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ASNNull isSupportedVlrCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ASNNull isSupportedSgsnCamelPhases;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=true,index=-1,defaultImplementation = MAPExtensionContainerImpl.class)
    private MAPExtensionContainer extensionContainer;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNAdditionalRequestedCamelSubscriptionInfoImpl additionalRequestedCAMELSubscriptionInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ASNNull isMsisdnBsList;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private ASNNull isCsgSubscriptionDataRequested;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private ASNNull isCwInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=11,constructed=false,index=-1)
    private ASNNull isClipInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=12,constructed=false,index=-1)
    private ASNNull isClirInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=13,constructed=false,index=-1)
    private ASNNull isHoldInfo;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=14,constructed=false,index=-1)
    private ASNNull isEctInfo;

    public RequestedSubscriptionInfoImpl() {
    }

    public RequestedSubscriptionInfoImpl(SSForBSCode ssForBSCode, boolean isOdb, RequestedCAMELSubscriptionInfo requestedCAMELSubscriptionInfo,
                                         boolean isSupportedVlrCamelPhases, boolean isSupportedSgsnCamelPhases, MAPExtensionContainer extensionContainer,
                                         AdditionalRequestedCAMELSubscriptionInfo additionalRequestedCAMELSubscriptionInfo, boolean isMsisdnBsList,
                                         boolean isCsgSubscriptionDataRequested, boolean isCwInfo, boolean isClipInfo, boolean isClirInfo, boolean isHoldInfo,
                                         boolean isEctInfo) {
        this.ssForBSCode = ssForBSCode;
        
        if(isOdb)
        	this.isOdb = new ASNNull();
        
        if(requestedCAMELSubscriptionInfo!=null)
        	this.requestedCAMELSubscriptionInfo = new ASNRequestedCamelSubscriptionInfoImpl(requestedCAMELSubscriptionInfo);
        	
        if(isSupportedVlrCamelPhases)
        	this.isSupportedVlrCamelPhases = new ASNNull();
        
        if(isSupportedSgsnCamelPhases)
        	this.isSupportedSgsnCamelPhases = new ASNNull();
        
        this.extensionContainer = extensionContainer;
        
        if(additionalRequestedCAMELSubscriptionInfo!=null)
        	this.additionalRequestedCAMELSubscriptionInfo = new ASNAdditionalRequestedCamelSubscriptionInfoImpl(additionalRequestedCAMELSubscriptionInfo);
        	
        if(isMsisdnBsList)
        	this.isMsisdnBsList = new ASNNull();
        
        if(isCsgSubscriptionDataRequested)
        	this.isCsgSubscriptionDataRequested = new ASNNull();
        
        if(isCwInfo)
        	this.isCwInfo = new ASNNull();
        
        if(isClipInfo)
        	this.isClipInfo = new ASNNull();
        
        if(isClirInfo)
        	this.isClirInfo = new ASNNull();
        
        if(isHoldInfo)
        	this.isHoldInfo = new ASNNull();
        
        if(isEctInfo)
        	this.isEctInfo = new ASNNull();
    }

    public SSForBSCode getRequestedSSInfo() {
        return this.ssForBSCode;
    }

    public boolean getOdb() {
        return this.isOdb!=null;
    }

    public RequestedCAMELSubscriptionInfo getRequestedCAMELSubscriptionInfo() {
    	if(this.requestedCAMELSubscriptionInfo==null)
    		return null;
    	
        return this.requestedCAMELSubscriptionInfo.getType();
    }

    public boolean getSupportedVlrCamelPhases() {
        return this.isSupportedVlrCamelPhases!=null;
    }

    public boolean getSupportedSgsnCamelPhases() {
        return this.isSupportedSgsnCamelPhases!=null;
    }

    public MAPExtensionContainer getExtensionContainer() {
        return this.extensionContainer;
    }

    public AdditionalRequestedCAMELSubscriptionInfo getAdditionalRequestedCamelSubscriptionInfo() {
    	if(this.additionalRequestedCAMELSubscriptionInfo==null)
    		return null;
    	
        return this.additionalRequestedCAMELSubscriptionInfo.getType();
    }

    public boolean getMsisdnBsList() {
        return this.isMsisdnBsList!=null;
    }

    public boolean getCsgSubscriptionDataRequested() {
        return this.isCsgSubscriptionDataRequested!=null;
    }

    public boolean getCwInfo() {
        return this.isCwInfo!=null;
    }

    public boolean getClipInfo() {
        return this.isClipInfo!=null;
    }

    public boolean getClirInfo() {
        return this.isClirInfo!=null;
    }

    public boolean getHoldInfo() {
        return this.isHoldInfo!=null;
    }

    public boolean getEctInfo() {
        return this.isEctInfo!=null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestedSubscriptionInfo [");

        if (this.ssForBSCode != null) {
            sb.append("ssForBSCode=");
            sb.append(this.ssForBSCode);
        }
        if (this.isOdb!=null) {
            sb.append(", isOdb");
        }
        if (this.requestedCAMELSubscriptionInfo != null) {
            sb.append(", requestedCAMELSubscriptionInfo=");
            sb.append(this.requestedCAMELSubscriptionInfo.getType());
        }
        if (this.isSupportedVlrCamelPhases!=null) {
            sb.append(", isSupportedVlrCamelPhases");
        }
        if (this.isSupportedSgsnCamelPhases!=null) {
            sb.append(", isSupportedSgsnCamelPhases");
        }
        if (this.extensionContainer != null) {
            sb.append(", extensionContainer=");
            sb.append(this.extensionContainer);
        }
        if (this.additionalRequestedCAMELSubscriptionInfo != null) {
            sb.append(", additionalRequestedCAMELSubscriptionInfo=");
            sb.append(this.additionalRequestedCAMELSubscriptionInfo.getType());
        }
        if (this.isMsisdnBsList!=null) {
            sb.append(", isMsisdnBsList");
        }
        if (this.isCsgSubscriptionDataRequested!=null) {
            sb.append(", isCsgSubscriptionDataRequested");
        }
        if (this.isCwInfo!=null) {
            sb.append(",isCwInfo");
        }
        if (this.isClipInfo!=null) {
            sb.append(", isClipInfo");
        }
        if (this.isClirInfo!=null) {
            sb.append(", isClirInfo");
        }
        if (this.isHoldInfo!=null) {
            sb.append(", isHoldInfo");
        }
        if (this.isEctInfo!=null) {
            sb.append(", isEctInfo");
        }

        sb.append("]");
        return sb.toString();
    }
}
