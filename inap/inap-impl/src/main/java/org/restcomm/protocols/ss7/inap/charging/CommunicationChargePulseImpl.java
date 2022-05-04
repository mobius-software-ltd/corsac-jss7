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

import org.restcomm.protocols.ss7.inap.api.charging.ChargeUnitTimeInterval;
import org.restcomm.protocols.ss7.inap.api.charging.CommunicationChargePulse;
import org.restcomm.protocols.ss7.inap.api.charging.PulseUnits;
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
public class CommunicationChargePulseImpl implements CommunicationChargePulse {

	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false, index=-1, defaultImplementation = PulseUnitsImpl.class)
    private PulseUnits pulseUnits;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false, index=-1, defaultImplementation = ChargeUnitTimeIntervalImpl.class)
    private ChargeUnitTimeInterval chargeUnitTimeInterval;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 2,constructed = false,index = -1, defaultImplementation = TariffDurationImpl.class)
    private TariffDuration tariffDuration;
    
    public CommunicationChargePulseImpl() {
    }

    public CommunicationChargePulseImpl(PulseUnits pulseUnits,Integer chargeUnitTimeInterval, 
    		Integer tariffDuration) {
    	
    	this.pulseUnits=pulseUnits;
    	if(chargeUnitTimeInterval!=null)
    		this.chargeUnitTimeInterval=new ChargeUnitTimeIntervalImpl(chargeUnitTimeInterval);
    	
    	if(tariffDuration!=null)
    		this.tariffDuration=new TariffDurationImpl(tariffDuration);    	
    }

    public PulseUnits getPulseUnits() {
    	return pulseUnits;
    }

    public Integer getChargeUnitTimeInterval() {
    	if(chargeUnitTimeInterval==null)
    		return null;
    	
        return chargeUnitTimeInterval.getData();
    }

    public Integer getTariffDuration() {
    	if(tariffDuration==null)
    		return null;
    	
        return tariffDuration.getData();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("CommunicationChargePulse [");

        if (this.pulseUnits != null) {
            sb.append(", pulseUnits=");
            sb.append(pulseUnits);
        }
        
        if (this.chargeUnitTimeInterval != null && this.chargeUnitTimeInterval.getData()!=null) {
            sb.append(", chargeUnitTimeInterval=");
            sb.append(chargeUnitTimeInterval.getData());
        }
        
        if (this.tariffDuration != null && this.tariffDuration.getData()!=null) {
            sb.append(", tariffDuration=");
            sb.append(tariffDuration.getData());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(pulseUnits==null)
			throw new ASNParsingComponentException("pulse unit should be set for communication charge pulse", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(chargeUnitTimeInterval==null)
			throw new ASNParsingComponentException("charge unit time interval should be set for communication charge pulse", ASNParsingComponentExceptionReason.MistypedParameter);			

		if(tariffDuration==null)
			throw new ASNParsingComponentException("tariff duration should be set for communication charge pulse", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}