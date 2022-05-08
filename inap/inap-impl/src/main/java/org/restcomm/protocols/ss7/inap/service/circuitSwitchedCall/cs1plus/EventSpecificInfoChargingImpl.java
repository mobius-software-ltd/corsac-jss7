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

package org.restcomm.protocols.ss7.inap.service.circuitSwitchedCall.cs1plus;

import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.ChargeNoChargeIndication;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.EventSpecificInfoCharging;
import org.restcomm.protocols.ss7.inap.api.service.circuitSwitchedCall.cs1plus.TariffInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNOctetString;

import io.netty.buffer.ByteBuf;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class EventSpecificInfoChargingImpl implements EventSpecificInfoCharging {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffInformationImpl.class)
    private TariffInformation tariffInformation;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1)
    private ASNOctetString tariffIndicator;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1)
    private ASNChargeNoChargeIndication chargeNoChargeIndication;
    
	public EventSpecificInfoChargingImpl() {
    }

    public EventSpecificInfoChargingImpl(TariffInformation tariffInformation) {
    	this.tariffInformation=tariffInformation;
    }
    
    public EventSpecificInfoChargingImpl(ByteBuf tariffIndicator) {
    	if(tariffIndicator!=null)
    		this.tariffIndicator=new ASNOctetString(tariffIndicator,"TariffIndicator",2,2,false);    	
    }
    
    public EventSpecificInfoChargingImpl(ChargeNoChargeIndication chargeNoChargeIndication) {
    	if(chargeNoChargeIndication!=null)
    		this.chargeNoChargeIndication=new ASNChargeNoChargeIndication(chargeNoChargeIndication);    		
    }

    public TariffInformation getTariffInformation() {
    	return tariffInformation;
    }

    public ByteBuf getTariffIndicator() {
    	if(tariffIndicator==null)
    		return null;
    	
    	return tariffIndicator.getValue();
    }

    public ChargeNoChargeIndication getChargeNoChargeIndication() {
    	if(chargeNoChargeIndication==null)
    		return null;
    	
    	return chargeNoChargeIndication.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("EventSpecificInfoCharging [");

        if (this.tariffInformation != null) {
            sb.append(", tariffInformation=");
            sb.append(tariffInformation);
        }
        
        if (this.tariffIndicator != null && this.tariffIndicator.getValue()!=null) {
            sb.append(", tariffIndicator=");
            sb.append(tariffIndicator.printDataArr());
        }
        
        if (this.chargeNoChargeIndication != null && this.chargeNoChargeIndication.getType()!=null) {
            sb.append(", chargeNoChargeIndication=");
            sb.append(chargeNoChargeIndication.getType());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tariffIndicator==null && tariffIndicator==null && chargeNoChargeIndication==null)
			throw new ASNParsingComponentException("one of child items should be set for event specific info charging", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}