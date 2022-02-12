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

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author sergey vetyutnev
 * @author Amit Bhayani
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class TimeIfTariffSwitchImpl implements TimeIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger timeSinceTariffSwitch;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger tariffSwitchInterval;

    public TimeIfTariffSwitchImpl() {
    }

    public TimeIfTariffSwitchImpl(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
        this.timeSinceTariffSwitch = new ASNInteger(timeSinceTariffSwitch,"TimeSinceTariffSwitch",0,864000,false);
        
        if(tariffSwitchInterval!=null)
        	this.tariffSwitchInterval = new ASNInteger(tariffSwitchInterval,"TariffSwitchInterval",0,864000,false);        	
    }

    public int getTimeSinceTariffSwitch() {
    	if(timeSinceTariffSwitch==null || timeSinceTariffSwitch.getValue()==null)
    		return 0;
    	
        return timeSinceTariffSwitch.getIntValue();
    }

    public Integer getTariffSwitchInterval() {
    	if(tariffSwitchInterval==null)
    		return null;
    	
        return tariffSwitchInterval.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("TimeIfTariffSwitch [");

        sb.append("timeSinceTariffSwitch=");
        sb.append(timeSinceTariffSwitch);
        if (this.tariffSwitchInterval != null) {
            sb.append(", tariffSwitchInterval=");
            sb.append(tariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }
}
