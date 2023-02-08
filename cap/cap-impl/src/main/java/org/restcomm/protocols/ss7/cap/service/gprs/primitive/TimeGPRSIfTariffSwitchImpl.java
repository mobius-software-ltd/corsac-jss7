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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TimeGPRSIfTariffSwitch;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeGPRSIfTariffSwitchImpl implements TimeGPRSIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    public ASNInteger timeGPRSSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    public ASNInteger timeGPRSTariffSwitchInterval;

    public TimeGPRSIfTariffSwitchImpl() {
    }

    public TimeGPRSIfTariffSwitchImpl(int timeGPRSSinceLastTariffSwitch, Integer timeGPRSTariffSwitchInterval) {
        this.timeGPRSSinceLastTariffSwitch = new ASNInteger(timeGPRSSinceLastTariffSwitch,"TimeGPRSSinceLastTariffSwitch",0,86400,false);        
        
        if(timeGPRSTariffSwitchInterval!=null)
        	this.timeGPRSTariffSwitchInterval = new ASNInteger(timeGPRSTariffSwitchInterval,"TimeGPRSTariffSwitchInterval",0,86400,false);        
    }

    public int getTimeGPRSSinceLastTariffSwitch() {
    	if(this.timeGPRSSinceLastTariffSwitch==null || this.timeGPRSSinceLastTariffSwitch.getValue()==null)
    		return 0;
    	
        return this.timeGPRSSinceLastTariffSwitch.getIntValue();
    }

    public Integer getTimeGPRSTariffSwitchInterval() {
    	if(this.timeGPRSTariffSwitchInterval==null)
    		return null;
    	
        return this.timeGPRSTariffSwitchInterval.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TimeGPRSIfTariffSwitch [");

        if(this.timeGPRSSinceLastTariffSwitch!=null && this.timeGPRSSinceLastTariffSwitch.getValue()!=null)
        sb.append("timeGPRSSinceLastTariffSwitch=");
        sb.append(this.timeGPRSSinceLastTariffSwitch.getValue());
        sb.append(", ");

        if (this.timeGPRSTariffSwitchInterval != null && this.timeGPRSTariffSwitchInterval.getValue()!=null) {
            sb.append("timeGPRSTariffSwitchInterval=");
            sb.append(this.timeGPRSTariffSwitchInterval.getValue());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timeGPRSSinceLastTariffSwitch==null)
			throw new ASNParsingComponentException("time gprs since last tariff switch should be set for time gprs if tariff switch", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
