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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeMessage;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargingInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNNull;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingInformationImpl implements ChargingInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1)
    private ASNNull orderStartOfCharging;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = ChargeMessageImpl.class)
    private ChargeMessage chargeMessage;

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNInteger pulseBurst;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1)
    private ASNNull createDefaultBillingRecord;
    
	public ChargingInformationImpl() {
    }

    public ChargingInformationImpl(boolean orderStartOfCharging,ChargeMessage chargeMessage,
    		Integer pulseBurst,boolean createDefaultBillingRecord) {
    	if(orderStartOfCharging)
    		this.orderStartOfCharging=new ASNNull();
    	
    	this.chargeMessage=chargeMessage;
    	
    	if(pulseBurst!=null)
    		this.pulseBurst=new ASNInteger(pulseBurst);    		
    	
    	if(createDefaultBillingRecord)
    		this.createDefaultBillingRecord=new ASNNull();
    }

    public boolean getOrderStartOfCharging() {
    	return orderStartOfCharging!=null;
    }
    
    public ChargeMessage getChargeMessage() {
    	return chargeMessage;
    }

    public Integer getPulseBurst() {
    	if(pulseBurst==null || pulseBurst.getValue()==null)
    		return null;
    	
    	return pulseBurst.getValue().intValue();
    }

    public boolean getCreateDefaultBillingRecord() {
    	return createDefaultBillingRecord!=null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingInformation [");

        if (this.orderStartOfCharging != null) {
            sb.append(", orderStartOfCharging");         	
        }
        
        if (this.chargeMessage != null) {
            sb.append(", chargeMessage=");
            sb.append(chargeMessage);
        }
        
        if (this.pulseBurst != null && this.pulseBurst.getValue()!=null) {
            sb.append(", pulseBurst=");
            sb.append(pulseBurst.getValue());
        }
        
        if (this.createDefaultBillingRecord != null) {
            sb.append(", createDefaultBillingRecord");         	
        }
        
        sb.append("]");

        return sb.toString();
    }
}