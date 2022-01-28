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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.commonapp.api.primitives.CAPINAPExtensions;
import org.restcomm.protocols.ss7.commonapp.primitives.CAPINAPExtensionsImpl;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingReferenceIdentification;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariff;
import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariffInformation;
import org.restcomm.protocols.ss7.inap.api.charging.Currency;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingTariffInformationImpl implements ChargingTariffInformation {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1, defaultImplementation = ChargingControlIndicatorsImpl.class)
    private ChargingControlIndicators chargingControlIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, index=-1, defaultImplementation = TariffSwitchoverTimeImpl.class)
    private ChargingTariffWrapperImpl chargingTariff;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true, index=-1, defaultImplementation = CAPINAPExtensionsImpl.class)
    private CAPINAPExtensions extensions;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true, index=-1, defaultImplementation = ChargingReferenceIdentificationImpl.class)
    private ChargingReferenceIdentification originationIdentification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 4,constructed = true, index=-1, defaultImplementation = ChargingReferenceIdentificationImpl.class)
    private ChargingReferenceIdentification destinationIdentification;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 5,constructed = false, index=-1)
    private ASNCurrency currency;

    public ChargingTariffInformationImpl() {
    }

    public ChargingTariffInformationImpl(ChargingControlIndicators chargingControlIndicators,ChargingTariff chargingTariff,
    		CAPINAPExtensions extensions,ChargingReferenceIdentification originationIdentification,
    		ChargingReferenceIdentification destinationIdentification,Currency currency) {
    	this.chargingControlIndicators=chargingControlIndicators; 
    	
    	if(chargingTariff!=null)
    		this.chargingTariff=new ChargingTariffWrapperImpl(chargingTariff);
    	
    	this.extensions=extensions;
    	this.originationIdentification=originationIdentification;
    	this.destinationIdentification=destinationIdentification;
    	
    	if(currency!=null)
    		this.currency=new ASNCurrency(currency);    		
    }

    public ChargingControlIndicators getChargingControlIndicators() {
    	return chargingControlIndicators;
    }

    public ChargingTariff getChargingTariff() {
    	if(chargingTariff==null)
    		return null;
    	
    	return chargingTariff.getChargingTariff();
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
    	if(currency==null)
    		return null;
    	
    	return currency.getType();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingTariffInformation [");

        if (this.chargingControlIndicators != null) {
            sb.append(", chargingControlIndicators=");
            sb.append(chargingControlIndicators);
        }

        if (this.chargingTariff != null && this.chargingTariff.getChargingTariff()!=null) {
            sb.append(", chargingTariff=");
            sb.append(chargingTariff.getChargingTariff());
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
}