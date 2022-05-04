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

import org.restcomm.protocols.ss7.inap.api.charging.AddOnCharge;
import org.restcomm.protocols.ss7.inap.api.charging.CurrencyFactorScale;
import org.restcomm.protocols.ss7.inap.api.charging.PulseUnits;

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
public class AddOnChargeImpl implements AddOnCharge {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = CurrencyFactorScaleImpl.class)
    private CurrencyFactorScale addOnChargeCurrency;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = PulseUnitsImpl.class)
    private PulseUnits addOnChargePulse;

    public AddOnChargeImpl() {
    }

    public AddOnChargeImpl(CurrencyFactorScale addOnChargeCurrency) {
    	this.addOnChargeCurrency=addOnChargeCurrency;        
    }
    
    public AddOnChargeImpl(PulseUnits addOnChargePulse) {
    	this.addOnChargePulse=addOnChargePulse;
    }

    public CurrencyFactorScale getAddOnChargeCurrency() {
    	return addOnChargeCurrency;
    }

    public PulseUnits getAddOnChargePulse() {
    	return addOnChargePulse;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AddOnCharge [");

        if (this.addOnChargeCurrency != null) {
            sb.append(", addOnChargeCurrency=");
            sb.append(addOnChargeCurrency);
        }

        if (this.addOnChargePulse != null) {
            sb.append(", addOnChargePulse=");
            sb.append(addOnChargePulse);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(addOnChargeCurrency==null && addOnChargePulse==null)
			throw new ASNParsingComponentException("ext bearer service or ext teleservice should be set for ext basic service code", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
