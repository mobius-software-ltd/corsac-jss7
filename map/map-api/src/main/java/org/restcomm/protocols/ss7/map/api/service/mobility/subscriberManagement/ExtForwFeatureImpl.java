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

package org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement;

import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNSubaddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainerImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 * @author daniel bichara
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ExtForwFeatureImpl {
	@ASNChoise
    private ExtBasicServiceCodeImpl basicService = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private ExtSSStatusImpl ssStatus = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ISDNAddressStringImpl forwardedToNumber = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ISDNSubaddressStringImpl forwardedToSubaddress = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ExtForwOptionsImpl forwardingOptions = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNInteger noReplyConditionTime = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=true,index=-1)
    private MAPExtensionContainerImpl extensionContainer = null;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=10,constructed=false,index=-1)
    private FTNAddressStringImpl longForwardedToNumber = null;

    public ExtForwFeatureImpl() {
    }

    /**
     *
     */
    public ExtForwFeatureImpl(ExtBasicServiceCodeImpl basicService, ExtSSStatusImpl ssStatus, ISDNAddressStringImpl forwardedToNumber,
    		ISDNSubaddressStringImpl forwardedToSubaddress, ExtForwOptionsImpl forwardingOptions, Integer noReplyConditionTime,
            MAPExtensionContainerImpl extensionContainer, FTNAddressStringImpl longForwardedToNumber) {
        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.forwardedToNumber = forwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        
        if(noReplyConditionTime!=null) {
        	this.noReplyConditionTime = new ASNInteger();
        	this.noReplyConditionTime.setValue(noReplyConditionTime.longValue());
        }
        
        this.extensionContainer = extensionContainer;
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public ExtBasicServiceCodeImpl getBasicService() {
        return this.basicService;
    }

    public ExtSSStatusImpl getSsStatus() {
        return this.ssStatus;
    }

    public ISDNAddressStringImpl getForwardedToNumber() {
        return this.forwardedToNumber;
    }

    public ISDNSubaddressStringImpl getForwardedToSubaddress() {
        return this.forwardedToSubaddress;
    }

    public ExtForwOptionsImpl getForwardingOptions() {
        return this.forwardingOptions;
    }

    public Integer getNoReplyConditionTime() {
    	if(this.noReplyConditionTime==null)
    		return null;
    	
        return this.noReplyConditionTime.getValue().intValue();
    }

    public MAPExtensionContainerImpl getExtensionContainer() {
        return this.extensionContainer;
    }

    public FTNAddressStringImpl getLongForwardedToNumber() {
        return this.longForwardedToNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtForwFeature [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService.toString());
            sb.append(", ");
        }

        if (this.ssStatus != null) {
            sb.append("ssStatus=");
            sb.append(this.ssStatus.toString());
            sb.append(", ");
        }

        if (this.forwardedToNumber != null) {
            sb.append("forwardedToNumber=");
            sb.append(this.forwardedToNumber.toString());
            sb.append(", ");
        }

        if (this.forwardedToSubaddress != null) {
            sb.append("forwardedToSubaddress=");
            sb.append(this.forwardedToSubaddress.toString());
            sb.append(", ");
        }

        if (this.forwardingOptions != null) {
            sb.append("forwardingOptions=");
            sb.append(this.forwardingOptions.toString());
            sb.append(", ");
        }

        if (this.noReplyConditionTime != null) {
            sb.append("noReplyConditionTime=");
            sb.append(this.noReplyConditionTime.getValue());
            sb.append(", ");
        }

        if (this.extensionContainer != null) {
            sb.append("extensionContainer=");
            sb.append(this.extensionContainer.toString());
            sb.append(", ");
        }

        if (this.longForwardedToNumber != null) {
            sb.append("longForwardedToNumber=");
            sb.append(this.longForwardedToNumber.toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
