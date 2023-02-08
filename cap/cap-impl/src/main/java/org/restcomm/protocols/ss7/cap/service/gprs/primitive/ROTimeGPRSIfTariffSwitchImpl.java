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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROTimeGPRSIfTariffSwitch;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,lengthIndefinite = false)
public class ROTimeGPRSIfTariffSwitchImpl implements ROTimeGPRSIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    public ASNInteger roTimeGPRSSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    public ASNInteger roTimeGPRSTariffSwitchInterval;

    public ROTimeGPRSIfTariffSwitchImpl() {
    }

    public ROTimeGPRSIfTariffSwitchImpl(Integer roTimeGPRSSinceLastTariffSwitch, Integer roTimeGPRSTariffSwitchInterval) {
    	if(roTimeGPRSSinceLastTariffSwitch!=null)
    		this.roTimeGPRSSinceLastTariffSwitch = new ASNInteger(roTimeGPRSSinceLastTariffSwitch,"ROTimeGPRSSinceLastTariffSwitch",0,255,false);
    		
    	if(roTimeGPRSTariffSwitchInterval!=null)
    		this.roTimeGPRSTariffSwitchInterval = new ASNInteger(roTimeGPRSTariffSwitchInterval,"ROTimeGPRSTariffSwitchInterval",0,255,false);
    }

    public Integer getROTimeGPRSSinceLastTariffSwitch() {
    	if(roTimeGPRSSinceLastTariffSwitch==null)
    		return null;
    	
        return this.roTimeGPRSSinceLastTariffSwitch.getIntValue();
    }

    public Integer getROTimeGPRSTariffSwitchInterval() {
    	if(roTimeGPRSTariffSwitchInterval==null)
    		return null;
    	
        return this.roTimeGPRSTariffSwitchInterval.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROTimeGPRSIfTariffSwitch [");

        if (this.roTimeGPRSSinceLastTariffSwitch != null && this.roTimeGPRSSinceLastTariffSwitch.getValue() != null) {
            sb.append("roTimeGPRSSinceLastTariffSwitch=");
            sb.append(this.roTimeGPRSSinceLastTariffSwitch.getValue());
            sb.append(", ");
        }

        if (this.roTimeGPRSTariffSwitchInterval != null && this.roTimeGPRSTariffSwitchInterval.getValue() != null) {
            sb.append("roTimeGPRSTariffSwitchInterval=");
            sb.append(this.roTimeGPRSTariffSwitchInterval.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
}
