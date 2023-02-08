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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTime;
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
public class ElapsedTimeImpl implements ElapsedTime {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger timeGPRSIfNoTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = TimeGPRSIfTariffSwitchImpl.class)
    private TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch;

    public ElapsedTimeImpl() {

    }

    public ElapsedTimeImpl(Integer timeGPRSIfNoTariffSwitch) {
    	if(timeGPRSIfNoTariffSwitch!=null)
    		this.timeGPRSIfNoTariffSwitch = new ASNInteger(timeGPRSIfNoTariffSwitch,"TimeGPRSIfNoTariffSwitch",0,86400,false);    		
    }

    public ElapsedTimeImpl(TimeGPRSIfTariffSwitch timeGPRSIfTariffSwitch) {
        this.timeGPRSIfTariffSwitch = timeGPRSIfTariffSwitch;
    }

    public Integer getTimeGPRSIfNoTariffSwitch() {
    	if(this.timeGPRSIfNoTariffSwitch==null)
    		return null;
    	
        return this.timeGPRSIfNoTariffSwitch.getIntValue();
    }

    public TimeGPRSIfTariffSwitch getTimeGPRSIfTariffSwitch() {
        return this.timeGPRSIfTariffSwitch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ElapsedTime [");

        if (this.timeGPRSIfNoTariffSwitch != null && this.timeGPRSIfNoTariffSwitch.getValue()==null) {
            sb.append("timeGPRSIfNoTariffSwitch=");
            sb.append(this.timeGPRSIfNoTariffSwitch.getValue());
        }

        if (this.timeGPRSIfTariffSwitch != null) {
            sb.append(", timeGPRSIfTariffSwitch=");
            sb.append(this.timeGPRSIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timeGPRSIfNoTariffSwitch==null && timeGPRSIfTariffSwitch==null)
			throw new ASNParsingComponentException("one of child items should be set for elapsed time", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}