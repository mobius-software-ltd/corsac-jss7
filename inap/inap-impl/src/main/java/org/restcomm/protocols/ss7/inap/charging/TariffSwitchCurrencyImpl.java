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

import org.restcomm.protocols.ss7.inap.api.charging.TariffCurrencyFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchCurrency;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchoverTime;

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
public class TariffSwitchCurrencyImpl implements TariffSwitchCurrency {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffCurrencyFormatImpl.class)
    private TariffCurrencyFormat nextTariffCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffSwitchoverTimeImpl.class)
    private TariffSwitchoverTime tariffSwitchoverTime;

    public TariffSwitchCurrencyImpl() {
    }

    public TariffSwitchCurrencyImpl(TariffCurrencyFormat nextTariffCurrency,TariffSwitchoverTime tariffSwitchoverTime) {
    	this.nextTariffCurrency=nextTariffCurrency; 
    	this.tariffSwitchoverTime=tariffSwitchoverTime;
    }

    public TariffCurrencyFormat getNextTariffCurrency() {
    	return nextTariffCurrency;
    }

    public TariffSwitchoverTime getTariffSwitchoverTime() {
    	return tariffSwitchoverTime;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffSwitchCurrency [");

        if (this.nextTariffCurrency != null) {
            sb.append(", nextTariffCurrency=");
            sb.append(nextTariffCurrency);
        }

        if (this.tariffSwitchoverTime != null) {
            sb.append(", tariffSwitchoverTime=");
            sb.append(tariffSwitchoverTime);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(nextTariffCurrency==null)
			throw new ASNParsingComponentException("next tariff currency should be set for tariff switch currency", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tariffSwitchoverTime==null)
			throw new ASNParsingComponentException("tariff switchover time should be set for tariff switch currency", ASNParsingComponentExceptionReason.MistypedParameter);						
	}
}