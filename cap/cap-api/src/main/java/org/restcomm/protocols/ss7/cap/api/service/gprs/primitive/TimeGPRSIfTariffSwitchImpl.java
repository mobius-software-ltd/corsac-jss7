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
package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeGPRSIfTariffSwitchImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    public ASNInteger timeGPRSSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    public ASNInteger timeGPRSTariffSwitchInterval;

    public TimeGPRSIfTariffSwitchImpl() {
    }

    public TimeGPRSIfTariffSwitchImpl(int timeGPRSSinceLastTariffSwitch, Integer timeGPRSTariffSwitchInterval) {
        this.timeGPRSSinceLastTariffSwitch = new ASNInteger();
        this.timeGPRSSinceLastTariffSwitch.setValue(Long.valueOf(timeGPRSSinceLastTariffSwitch));
        
        if(timeGPRSTariffSwitchInterval!=null) {
        	this.timeGPRSTariffSwitchInterval = new ASNInteger();
        	this.timeGPRSTariffSwitchInterval.setValue(timeGPRSTariffSwitchInterval.longValue());
        }
    }

    public int getTimeGPRSSinceLastTariffSwitch() {
    	if(this.timeGPRSSinceLastTariffSwitch==null || this.timeGPRSSinceLastTariffSwitch.getValue()==null)
    		return 0;
    	
        return this.timeGPRSSinceLastTariffSwitch.getValue().intValue();
    }

    public Integer getTimeGPRSTariffSwitchInterval() {
    	if(this.timeGPRSTariffSwitchInterval==null || timeGPRSTariffSwitchInterval.getValue()==null)
    		return null;
    	
        return this.timeGPRSTariffSwitchInterval.getValue().intValue();
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
}
