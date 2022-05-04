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

import java.util.List;

import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargePulse;
import org.restcomm.protocols.ss7.inap.api.charging.PulseUnits;
import org.restcomm.protocols.ss7.inap.api.charging.TariffControlIndicators;
import org.restcomm.protocols.ss7.inap.api.charging.TariffPulseFormat;

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
public class TariffPulseFormatImpl implements TariffPulseFormat {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true, index=-1)
    private CommunicationChargePulseListWrapperImpl communicationChargeSequencePulse;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = TariffControlIndicatorsImpl.class)
    private TariffControlIndicators tariffControlIndicators;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = PulseUnitsImpl.class)
    private PulseUnits callAttemptChargePulse;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 3,constructed = false, index=-1, defaultImplementation = PulseUnitsImpl.class)
    private PulseUnits callSetupChargePulse;

    public TariffPulseFormatImpl() {
    }

    public TariffPulseFormatImpl(List<CommunicationChargePulse> communicationChargeSequencePulse,
    		TariffControlIndicators tariffControlIndicators,PulseUnits callAttemptChargePulse,
    		PulseUnits callSetupChargePulse) {
    	
    	if(communicationChargeSequencePulse!=null)
    		this.communicationChargeSequencePulse = new CommunicationChargePulseListWrapperImpl(communicationChargeSequencePulse);
    	
    	this.tariffControlIndicators=tariffControlIndicators;
    	this.callAttemptChargePulse=callAttemptChargePulse;
    	this.callSetupChargePulse=callSetupChargePulse;
    }

    public List<CommunicationChargePulse> getCommunicationChargeSequencePulse() {
    	if(communicationChargeSequencePulse==null)
    		return null;
    	
        return communicationChargeSequencePulse.getCommuninucationChargePulse();
    }

    public TariffControlIndicators getTariffControlIndicators() {
    	return tariffControlIndicators;
    }

    public PulseUnits getCallAttemptChargePulse() {
    	return callAttemptChargePulse;
    }

    public PulseUnits getCallSetupChargePulse() {
    	return callSetupChargePulse;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TariffPulseFormat [");

        if (this.communicationChargeSequencePulse != null && this.communicationChargeSequencePulse.getCommuninucationChargePulse()!=null) {
            sb.append(", communicationChargeSequencePulse= [");
            boolean isFirst=true;
            for(CommunicationChargePulse curr : this.communicationChargeSequencePulse.getCommuninucationChargePulse()) {
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
        
        if (this.callAttemptChargePulse != null) {
            sb.append(", callAttemptChargePulse=");
            sb.append(callAttemptChargePulse);
        }

        if (this.callSetupChargePulse != null) {
            sb.append(", callSetupChargePulse=");
            sb.append(callSetupChargePulse);
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tariffControlIndicators==null)
			throw new ASNParsingComponentException("tariff control indicators should be set for tariff pulse format", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}