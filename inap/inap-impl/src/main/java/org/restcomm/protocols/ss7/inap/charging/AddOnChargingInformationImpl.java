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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.charging.AddOnCharge;
import org.restcomm.protocols.ss7.inap.api.charging.AddOnChargingInformation;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingReferenceIdentification;
import org.restcomm.protocols.ss7.inap.api.charging.Currency;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AddOnChargingInformationImpl implements AddOnChargingInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1, defaultImplementation = ChargingControlIndicatorsImpl.class)
    private ChargingControlIndicators chargingControlIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1)
    private AddOnChargeWrapperImpl addOnCharge;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true, index=-1, defaultImplementation = ChargingReferenceIdentificationImpl.class)
    private ChargingReferenceIdentification originationIdentification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true, index=-1, defaultImplementation = ChargingReferenceIdentificationImpl.class)
    private ChargingReferenceIdentification destinationIdentification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1)
    private ASNCurrency currency;

    public AddOnChargingInformationImpl() {
    }

    public AddOnChargingInformationImpl(ChargingControlIndicators chargingControlIndicators,AddOnCharge addOnCharge,
    		CAPINAPExtensions extensions,ChargingReferenceIdentification originationIdentification,
    		ChargingReferenceIdentification destinationIdentification,Currency currency) {
    	
    	this.chargingControlIndicators=chargingControlIndicators;
    	
    	if(addOnCharge!=null)
    		this.addOnCharge = new AddOnChargeWrapperImpl(addOnCharge);    		
    	
    	this.extensions=extensions;
    	this.originationIdentification=originationIdentification;
    	this.destinationIdentification=destinationIdentification;
    	
    	if(currency!=null)
    		this.currency=new ASNCurrency(currency);    		
    }

    public ChargingControlIndicators getChargingControlIndicators() {
    	return chargingControlIndicators;
    }

    public AddOnCharge getAddOnCharge() {
    	if(addOnCharge==null)
    		return null;
    	
        return addOnCharge.getAddOnCharge();
    }

    public CAPINAPExtensions getExtensions() {
    	return extensions;
    }

    public ChargingReferenceIdentification getOriginationIdentification() {
    	return originationIdentification;
    }

    public ChargingReferenceIdentification getDestinationIdentification() {
    	return destinationIdentification;
    }

    public Currency getCurrency() {
    	if(currency==null || currency.getType()==null)
    		return null;
    	
    	return currency.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AddOnChargingInformation [");

        if (this.chargingControlIndicators != null) {
            sb.append(", chargingControlIndicators=");
            sb.append(chargingControlIndicators);
        }
        
        if (this.addOnCharge != null && this.addOnCharge.getAddOnCharge()!=null) {
            sb.append(", addOnCharge=");
            sb.append(addOnCharge.getAddOnCharge());
        }

        if (this.extensions != null) {
            sb.append(", extensions=");
            sb.append(extensions);
        }

        if (this.originationIdentification != null) {
            sb.append(", originationIdentification=");
            sb.append(originationIdentification);
        }

        if (this.destinationIdentification != null) {
            sb.append(", destinationIdentification=");
            sb.append(destinationIdentification);
        }
        
        if (this.currency != null && this.currency.getType()!=null) {
            sb.append(", currency=");
            sb.append(currency.getType());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(chargingControlIndicators==null)
			throw new ASNParsingComponentException("charging control indicators should be set for add on charging information", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(addOnCharge==null)
			throw new ASNParsingComponentException("add on charge should be set for add on charging information", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(originationIdentification==null)
			throw new ASNParsingComponentException("origination identification should be set for add on charging information", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(currency==null)
			throw new ASNParsingComponentException("currency should be set for add on charging information", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}