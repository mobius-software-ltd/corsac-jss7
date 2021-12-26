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

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargeCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.CurrencyFactorScale;
import org.restcomm.protocols.ss7.inap.api.charging.TariffControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrencyFormat;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author yulian.oifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TariffCurrencyFormatImpl implements TariffCurrencyFormat {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1)
    private CommunicationChargeCurrencyListWrapperImpl communicationChargeSequenceCurrency;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffControlIndicatorsImpl.class)
    private TariffControlIndicators tariffControlIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = true,index = -1, defaultImplementation = CurrencyFactorScaleImpl.class)
    private CurrencyFactorScale callAttemptChargeCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = true, index=-1, defaultImplementation = CurrencyFactorScaleImpl.class)
    private CurrencyFactorScale callSetupChargeCurrency;

    public TariffCurrencyFormatImpl() {
    }

    public TariffCurrencyFormatImpl(List<CommunicationChargeCurrency> communicationChargeSequenceCurrency,
    		TariffControlIndicators tariffControlIndicators,CurrencyFactorScale callAttemptChargeCurrency,
    		CurrencyFactorScale callSetupChargeCurrency) {
    	
    	if(communicationChargeSequenceCurrency!=null)
    		this.communicationChargeSequenceCurrency = new CommunicationChargeCurrencyListWrapperImpl(communicationChargeSequenceCurrency);
    	
    	this.tariffControlIndicators=tariffControlIndicators;
    	this.callAttemptChargeCurrency=callAttemptChargeCurrency;
    	this.callSetupChargeCurrency=callSetupChargeCurrency;
    }

    public List<CommunicationChargeCurrency> getCommunicationChargeSequenceCurrency() {
    	if(communicationChargeSequenceCurrency==null)
    		return null;
    	
        return communicationChargeSequenceCurrency.getCommuninucationChargeCurrency();
    }

    public TariffControlIndicators getTariffControlIndicators() {
    	return tariffControlIndicators;
    }

    public CurrencyFactorScale getCallAttemptChargeCurrency() {
    	return callAttemptChargeCurrency;
    }

    public CurrencyFactorScale getCallSetupChargeCurrency() {
    	return callSetupChargeCurrency;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffCurrencyFormat [");

        if (this.communicationChargeSequenceCurrency != null && this.communicationChargeSequenceCurrency.getCommuninucationChargeCurrency()!=null) {
            sb.append(", communicationChargeSequenceCurrency= [");
            boolean isFirst=true;
            for(CommunicationChargeCurrency curr : this.communicationChargeSequenceCurrency.getCommuninucationChargeCurrency()) {
            	if(!isFirst)
            		sb.append(",");
            	
            	sb.append(curr);
            	isFirst=false;
            }
            sb.append("]");
        }

        if (this.tariffControlIndicators != null) {
            sb.append(", tariffControlIndicators=");
            sb.append(tariffControlIndicators);
        }
        
        if (this.callAttemptChargeCurrency != null) {
            sb.append(", callAttemptChargeCurrency=");
            sb.append(callAttemptChargeCurrency);
        }

        if (this.callSetupChargeCurrency != null) {
            sb.append(", callSetupChargeCurrency=");
            sb.append(callSetupChargeCurrency);
        }
        
        sb.append("]");

        return sb.toString();
    }
}