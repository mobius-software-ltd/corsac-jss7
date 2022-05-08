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

import org.restcomm.protocols.ss7.inap.api.charging.ChargingTariff;
import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulse;

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
public class ChargingTariffImpl implements ChargingTariff {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffCurrencyImpl.class)
    private TariffCurrency tariffCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffPulseImpl.class)
    private TariffPulse tariffPulse;

    public ChargingTariffImpl() {
    }

    public ChargingTariffImpl(TariffCurrency tariffCurrency) {
    	this.tariffCurrency=tariffCurrency;        
    }
    
    public ChargingTariffImpl(TariffPulse tariffPulse) {
    	this.tariffPulse=tariffPulse;
    }

    public TariffCurrency getTariffCurrency() {
    	return tariffCurrency;
    }

    public TariffPulse getTariffPulse() {
    	return tariffPulse;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("ChargingTariff [");

        if (this.tariffCurrency != null) {
            sb.append(", tariffCurrency=");
            sb.append(tariffCurrency);
        }

        if (this.tariffPulse != null) {
            sb.append(", tariffPulse=");
            sb.append(tariffPulse);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tariffCurrency==null)
			throw new ASNParsingComponentException("tariff currency should be set for charging tariff", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tariffPulse==null)
			throw new ASNParsingComponentException("tariff pulse should be set for charging tariff", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}