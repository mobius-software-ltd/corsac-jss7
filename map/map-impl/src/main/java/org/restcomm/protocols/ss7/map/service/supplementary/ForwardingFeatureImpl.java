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

package org.restcomm.protocols.ss7.map.service.supplementary;

import org.restcomm.protocols.ss7.commonapp.api.primitives.ISDNAddressString;
import org.restcomm.protocols.ss7.commonapp.primitives.ISDNAddressStringImpl;
import org.restcomm.protocols.ss7.map.api.primitives.FTNAddressString;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.BasicServiceCode;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingFeature;
import org.restcomm.protocols.ss7.map.api.service.supplementary.ForwardingOptions;
import org.restcomm.protocols.ss7.map.api.service.supplementary.SSStatus;
import org.restcomm.protocols.ss7.map.primitives.FTNAddressStringImpl;
import org.restcomm.protocols.ss7.map.service.mobility.subscriberManagement.BasicServiceCodeImpl;

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
public class ForwardingFeatureImpl implements ForwardingFeature {
	@ASNChoise(defaultImplementation = BasicServiceCodeImpl.class)
    private BasicServiceCode basicServiceCode;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=4,constructed=false,index=-1, defaultImplementation = SSStatusImpl.class)
    private SSStatus ssStatus;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=5,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString torwardedToNumber;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=8,constructed=false,index=-1, defaultImplementation = ISDNAddressStringImpl.class)
    private ISDNAddressString forwardedToSubaddress;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=6,constructed=false,index=-1, defaultImplementation = ForwardingOptionsImpl.class)
    private ForwardingOptions forwardingOptions;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=7,constructed=false,index=-1)
    private ASNInteger noReplyConditionTime;
    
    @ASNProperty(asnClass=ASNClass.CONTEXT_SPECIFIC,tag=9,constructed=false,index=-1, defaultImplementation = FTNAddressStringImpl.class)
    private FTNAddressString longForwardedToNumber;

    public ForwardingFeatureImpl() {
    }

    public ForwardingFeatureImpl(BasicServiceCode basicServiceCode, SSStatus ssStatus, ISDNAddressString torwardedToNumber,
            ISDNAddressString forwardedToSubaddress, ForwardingOptions forwardingOptions, Integer noReplyConditionTime, FTNAddressString longForwardedToNumber) {
    	this.basicServiceCode=basicServiceCode;    	
        this.ssStatus = ssStatus;
        this.torwardedToNumber = torwardedToNumber;
        this.forwardedToSubaddress = forwardedToSubaddress;
        this.forwardingOptions = forwardingOptions;
        
        if(noReplyConditionTime!=null)
        	this.noReplyConditionTime = new ASNInteger(noReplyConditionTime,"NoReplyConditionTime",5,30,false);
        	
        this.longForwardedToNumber = longForwardedToNumber;
    }

    public BasicServiceCode getBasicService() {
        return basicServiceCode;
    }

    public SSStatus getSsStatus() {
        return ssStatus;
    }

    public ISDNAddressString getForwardedToNumber() {
        return torwardedToNumber;
    }

    public ISDNAddressString getForwardedToSubaddress() {
        return forwardedToSubaddress;
    }

    public ForwardingOptions getForwardingOptions() {
        return forwardingOptions;
    }

    public Integer getNoReplyConditionTime() {
    	if(noReplyConditionTime==null)
    		return null;
    	
        return noReplyConditionTime.getIntValue();
    }

    public FTNAddressString getLongForwardedToNumber() {
        return longForwardedToNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ForwardingFeature [");

        if (this.basicServiceCode != null) {
            sb.append("basicService=");
            sb.append(this.basicServiceCode);
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
