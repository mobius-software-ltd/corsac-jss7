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

import org.restcomm.protocols.ss7.inap.api.charging.TariffPulseFormat;
import org.restcomm.protocols.ss7.inap.api.charging.TariffSwitchPulse;
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
public class TariffSwitchPulseImpl implements TariffSwitchPulse {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1, defaultImplementation = TariffPulseFormatImpl.class)
    private TariffPulseFormat nextTariffPulse;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffSwitchoverTimeImpl.class)
    private TariffSwitchoverTime tariffSwitchoverTime;

    public TariffSwitchPulseImpl() {
    }

    public TariffSwitchPulseImpl(TariffPulseFormat nextTariffPulse,TariffSwitchoverTime tariffSwitchoverTime) {
    	this.nextTariffPulse=nextTariffPulse; 
    	this.tariffSwitchoverTime=tariffSwitchoverTime;
    }

    public TariffPulseFormat getNextTariffPulse() {
    	return nextTariffPulse;
    }

    public TariffSwitchoverTime getTariffSwitchoverTime() {
    	return tariffSwitchoverTime;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffSwitchPulse [");

        if (this.nextTariffPulse != null) {
            sb.append(", nextTariffPulse=");
            sb.append(nextTariffPulse);
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
		if(nextTariffPulse==null)
			throw new ASNParsingComponentException("next tariff pulse should be set for tariff switch pulse", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tariffSwitchoverTime==null)
			throw new ASNParsingComponentException("tariff switchover time should be set for tariff switch pulse", ASNParsingComponentExceptionReason.MistypedParameter);						
	}
}