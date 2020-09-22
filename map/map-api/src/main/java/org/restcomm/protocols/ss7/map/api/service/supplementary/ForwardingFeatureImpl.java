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

package org.restcomm.protocols.ss7.map.api.service.supplementary;

import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCodeImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNChoise;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass=ASNClass.UNIVERSAL,tag=16,constructed=true,lengthIndefinite=false)
public class ForwardingFeatureImpl {
	@ASNChoise
    private BasicServiceCodeImpl basicService;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1)
    private SSStatusImpl ssStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1)
    private ISDNAddressStringImpl torwardedToNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1)
    private ISDNAddressStringImpl forwardedToSubaddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1)
    private ForwardingOptionsImpl forwardingOptions;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNInteger noReplyConditionTime;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1)
    private FTNAddressStringImpl longForwardedToNumber;

    public ForwardingFeatureImpl() {
    }

    public ForwardingFeatureImpl(BasicServiceCodeImpl basicService, SSStatusImpl ssStatus, ISDNAddressStringImpl torwardedToNumber,
            ISDNAddressStringImpl forwardedToSubaddress, ForwardingOptionsImpl forwardingOptions, Integer noReplyConditionTime, FTNAddressStringImpl longForwardedToNumber) {
        this.basicService = basicService;
        this.ssStatus = ssStatus;
        this.torwardedToNumber = torwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        
        if(noReplyConditionTime!=null) {
        	this.noReplyConditionTime = new ASNInteger();
        	this.noReplyConditionTime.setValue(noReplyConditionTime.longValue());
        }
        
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public BasicServiceCodeImpl getBasicService() {
        return basicService;
    }

    public SSStatusImpl getSsStatus() {
        return ssStatus;
    }

    public ISDNAddressStringImpl getForwardedToNumber() {
        return torwardedToNumber;
    }

    public ISDNAddressStringImpl getForwardedToSubaddress() {
        return forwardedToSubaddress;
    }

    public ForwardingOptionsImpl getForwardingOptions() {
        return forwardingOptions;
    }

    public Integer getNoReplyConditionTime() {
    	if(noReplyConditionTime==null)
    		return null;
    	
        return noReplyConditionTime.getValue().intValue();
    }

    public FTNAddressStringImpl getLongForwardedToNumber() {
        return longForwardedToNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardingFeature [");

        if (this.basicService != null) {
            sb.append("basicService=");
            sb.append(this.basicService);
        }

        if (this.ssStatus != null) {
            sb.append(", ssStatus=");
            sb.append(this.ssStatus);
        }

        if (this.torwardedToNumber != null) {
            sb.append(", torwardedToNumber=");
            sb.append(this.torwardedToNumber);
        }

        if (this.forwardedToSubaddress != null) {
            sb.append(", forwardedToSubaddress=");
            sb.append(this.forwardedToSubaddress);
        }

        if (this.forwardingOptions != null) {
            sb.append(", forwardingOptions=");
            sb.append(this.forwardingOptions);
        }

        if (this.noReplyConditionTime != null) {
            sb.append(", noReplyConditionTime=");
            sb.append(this.noReplyConditionTime);
        }

        if (this.longForwardedToNumber != null) {
            sb.append(", longForwardedToNumber=");
            sb.append(this.longForwardedToNumber);
        }

        sb.append("]");
        return sb.toString();
    }

}
