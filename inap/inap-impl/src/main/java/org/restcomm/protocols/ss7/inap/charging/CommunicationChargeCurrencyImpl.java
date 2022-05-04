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

import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargeCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.CurrencyFactorScale;
import org.restcomm.protocols.ss7.inap.api.charging.SubTariffControl;
import org.restcomm.protocols.ss7.inap.api.charging.TariffDuration;

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
public class CommunicationChargeCurrencyImpl implements CommunicationChargeCurrency {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = CurrencyFactorScaleImpl.class)
    private CurrencyFactorScale currencyFactorScale;
    
   @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1, defaultImplementation = TariffDurationImpl.class)
    private TariffDuration tariffDuration;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false, index=-1, defaultImplementation = SubTariffControlIndicatorsImpl.class)
    private SubTariffControl subTariffControl;

    public CommunicationChargeCurrencyImpl() {
    }

    public CommunicationChargeCurrencyImpl(CurrencyFactorScale currencyFactorScale,Integer tariffDuration,
    			SubTariffControl subTariffControl) {
    	
    	this.currencyFactorScale=currencyFactorScale;
    	if(tariffDuration!=null)
    		this.tariffDuration=new TariffDurationImpl(tariffDuration);
    	
    	this.subTariffControl=subTariffControl;
    }

    public CurrencyFactorScale getCurrencyFactorScale() {
    	return currencyFactorScale;
    }

    public Integer getTariffDuration() {
    	if(tariffDuration==null)
    		return null;
    	
        return tariffDuration.getData();
    }

    public SubTariffControl getSubTariffControl() {
    	return subTariffControl;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CommunicationChargeCurrency [");

        if (this.currencyFactorScale != null) {
            sb.append(", currencyFactorScale=");
            sb.append(currencyFactorScale);
        }
        
        if (this.tariffDuration != null && this.tariffDuration.getData()!=null) {
            sb.append(", tariffDuration=");
            sb.append(tariffDuration.getData());
        }

        if (this.subTariffControl != null) {
            sb.append(", subTariffControl=");
            sb.append(subTariffControl);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(currencyFactorScale==null)
			throw new ASNParsingComponentException("currency factor scale should be set for communication charge currency", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tariffDuration==null)
			throw new ASNParsingComponentException("tariff duration should be set for communication charge currency", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(subTariffControl==null)
			throw new ASNParsingComponentException("subtariff control should be set for communication charge currency", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}