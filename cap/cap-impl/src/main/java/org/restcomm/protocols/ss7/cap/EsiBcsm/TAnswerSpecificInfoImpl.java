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

package org.restcomm.protocols.ss7.cap.EsiBcsm;

import org.restcomm.protocols.ss7.cap.api.EsiBcsm.ChargeIndicator;
import org.restcomm.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.restcomm.protocols.ss7.commonapp.api.isup.CalledPartyNumberIsup;
import org.restcomm.protocols.ss7.commonapp.api.subscriberManagement.ExtBasicServiceCode;
import org.restcomm.protocols.ss7.commonapp.isup.CalledPartyNumberIsupImpl;
import org.restcomm.protocols.ss7.commonapp.subscriberManagement.ExtBasicServiceCodeWrapperImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author sergey vetyutnev
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TAnswerSpecificInfoImpl implements TAnswerSpecificInfo {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 50,constructed = false,index = -1, defaultImplementation = CalledPartyNumberIsupImpl.class)
    private CalledPartyNumberIsup destinationAddress;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 51,constructed = false,index = -1)
    private ASNNull orCall;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 52,constructed = false,index = -1)
    private ASNNull forwardedCall;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 53,constructed = false,index = -1, defaultImplementation = ChargeIndicatorImpl.class)
    private ChargeIndicator chargeIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 54,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 55,constructed = true,index = -1)
    private ExtBasicServiceCodeWrapperImpl extBasicServiceCode2;

    public TAnswerSpecificInfoImpl() {
    }

    public TAnswerSpecificInfoImpl(CalledPartyNumberIsup destinationAddress, boolean orCall, boolean forwardedCall,
            ChargeIndicator chargeIndicator, ExtBasicServiceCode extBasicServiceCode, ExtBasicServiceCode extBasicServiceCode2) {
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

    public CalledPartyNumberIsup getDestinationAddress() {
        return destinationAddress;
    }

    public boolean getOrCall() {
        return orCall!=null;
    }

    public boolean getForwardedCall() {
        return forwardedCall!=null;
    }

    public ChargeIndicator getChargeIndicator() {
        return chargeIndicator;
    }

    public ExtBasicServiceCode getExtBasicServiceCode() {
    	if(extBasicServiceCode==null)
    		return null;
    	
        return extBasicServiceCode.getExtBasicServiceCode();
    }

    public ExtBasicServiceCode getExtBasicServiceCode2() {
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
