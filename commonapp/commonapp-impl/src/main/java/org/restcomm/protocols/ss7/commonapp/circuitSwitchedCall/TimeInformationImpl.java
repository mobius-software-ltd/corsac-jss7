/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeIfTariffSwitch;
import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.TimeInformation;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeInformationImpl implements TimeInformation {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger timeIfNoTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = TimeIfTariffSwitchImpl.class)
    private TimeIfTariffSwitch timeIfTariffSwitch;

    public TimeInformationImpl() {
    }

    public TimeInformationImpl(int timeIfNoTariffSwitch) {
        this.timeIfNoTariffSwitch = new ASNInteger(timeIfNoTariffSwitch,"TimeIfNoTariffSwitch",0,864000,false);        
    }

    public TimeInformationImpl(TimeIfTariffSwitch timeIfTariffSwitch) {
        this.timeIfTariffSwitch = timeIfTariffSwitch;
    }

    public Integer getTimeIfNoTariffSwitch() {
    	if(timeIfNoTariffSwitch==null)
    		return null;
    	
        return timeIfNoTariffSwitch.getIntValue();
    }

    public TimeIfTariffSwitch getTimeIfTariffSwitch() {
        return timeIfTariffSwitch;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TimeInformation [");

        if (this.timeIfNoTariffSwitch != null && this.timeIfNoTariffSwitch.getValue()!=null) {
            sb.append("timeIfNoTariffSwitch=");
            sb.append(timeIfNoTariffSwitch.getValue());
        }
        
        if (this.timeIfTariffSwitch != null) {
            sb.append("timeIfTariffSwitch=");
            sb.append(timeIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(timeIfNoTariffSwitch==null && timeIfTariffSwitch==null)
			throw new ASNParsingComponentException("either time if tariff switch or time if no tariff switch should be set for time information", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
