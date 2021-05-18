/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.cap.api.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.isup.CalledPartyNumberCapImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeImpl;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtBasicServiceCodeWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TAnswerSpecificInfoImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1)
    private CalledPartyNumberCapImpl destinationAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNNull orCall;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1)
    private ASNNull forwardedCall;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = false,index = -1)
    private ChargeIndicatorImpl chargeIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 54,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode2;

    public TAnswerSpecificInfoImpl() {
    }

    public TAnswerSpecificInfoImpl(CalledPartyNumberCapImpl destinationAddress, boolean orCall, boolean forwardedCall,
            ChargeIndicatorImpl chargeIndicator, ExtBasicServiceCodeImpl extBasicServiceCode, ExtBasicServiceCodeImpl extBasicServiceCode2) {
        this.destinationAddress = destinationAddress;
        
        if(orCall)
        	this.orCall = new ASNNull();
        
        if(forwardedCall)
        	this.forwardedCall = new ASNNull();
        
        this.chargeIndicator = chargeIndicator;
        
        if(extBasicServiceCode!=null)
        	this.extBasicServiceCode = new ExtBasicServiceCodeWrapperImpl(extBasicServiceCode);
        
        if(extBasicServiceCode2!=null)
        	this.extBasicServiceCode2 = new ExtBasicServiceCodeWrapperImpl(extBasicServiceCode2);
    }

    public CalledPartyNumberCapImpl getDestinationAddress() {
        return destinationAddress;
    }

    public boolean getOrCall() {
        return orCall!=null;
    }

    public boolean getForwardedCall() {
        return forwardedCall!=null;
    }

    public ChargeIndicatorImpl getChargeIndicator() {
        return chargeIndicator;
    }

    public ExtBasicServiceCodeImpl getExtBasicServiceCode() {
    	if(extBasicServiceCode==null)
    		return null;
    	
        return extBasicServiceCode.getExtBasicServiceCode();
    }

    public ExtBasicServiceCodeImpl getExtBasicServiceCode2() {
    	if(extBasicServiceCode2==null)
    		return null;
    	
        return extBasicServiceCode2.getExtBasicServiceCode();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TAnswerSpecificInfo [");

        if (this.destinationAddress != null) {
            sb.append("destinationAddress= [");
            sb.append(destinationAddress.toString());
            sb.append("]");
        }
        if (this.orCall!=null) {
            sb.append(", orCall");
        }
        if (this.forwardedCall!=null) {
            sb.append(", forwardedCall");
        }
        if (this.chargeIndicator != null) {
            sb.append(", chargeIndicator= [");
            sb.append(chargeIndicator.toString());
            sb.append("]");
        }
        if (this.extBasicServiceCode != null) {
            sb.append(", extBasicServiceCode= [");
            sb.append(extBasicServiceCode.toString());
            sb.append("]");
        }
        if (this.extBasicServiceCode2 != null) {
            sb.append(", extBasicServiceCode2= [");
            sb.append(extBasicServiceCode2.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
}
