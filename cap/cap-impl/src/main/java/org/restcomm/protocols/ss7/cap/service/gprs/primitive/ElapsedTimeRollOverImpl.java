/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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
package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROTimeGPRSIfTariffSwitch;

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
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ElapsedTimeRollOverImpl implements ElapsedTimeRollOver {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    public ASNInteger roTimeGPRSIfNoTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1, defaultImplementation = ROTimeGPRSIfTariffSwitchImpl.class)
    public ROTimeGPRSIfTariffSwitch roTimeGPRSIfTariffSwitch;

    public ElapsedTimeRollOverImpl() {
    }

    public ElapsedTimeRollOverImpl(Integer roTimeGPRSIfNoTariffSwitch) {
    	if(roTimeGPRSIfNoTariffSwitch!=null)
    		this.roTimeGPRSIfNoTariffSwitch = new ASNInteger(roTimeGPRSIfNoTariffSwitch,"ROTimeGPRSIfNoTariffSwitch",0,255,false);    		
    }

    public ElapsedTimeRollOverImpl(ROTimeGPRSIfTariffSwitch roTimeGPRSIfTariffSwitch) {
        this.roTimeGPRSIfTariffSwitch = roTimeGPRSIfTariffSwitch;
    }

    public Integer getROTimeGPRSIfNoTariffSwitch() {
    	if(this.roTimeGPRSIfNoTariffSwitch==null )
    		return null;
    	
        return this.roTimeGPRSIfNoTariffSwitch.getIntValue();
    }

    public ROTimeGPRSIfTariffSwitch getROTimeGPRSIfTariffSwitch() {
        return this.roTimeGPRSIfTariffSwitch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ElapsedTimeRollOver [");

        if (this.roTimeGPRSIfNoTariffSwitch != null && this.roTimeGPRSIfNoTariffSwitch.getValue() != null) {
            sb.append("roTimeGPRSIfNoTariffSwitch=");
            sb.append(this.roTimeGPRSIfNoTariffSwitch.getValue());
        }

        if (this.roTimeGPRSIfTariffSwitch != null) {
            sb.append("roTimeGPRSIfTariffSwitch=");
            sb.append(this.roTimeGPRSIfTariffSwitch.toString());
        }

        sb.append("]");

        return sb.toString();
    }

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(roTimeGPRSIfNoTariffSwitch==null && roTimeGPRSIfTariffSwitch==null)
			throw new ASNParsingComponentException("one of child items should be set for elapsed time rollover", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}