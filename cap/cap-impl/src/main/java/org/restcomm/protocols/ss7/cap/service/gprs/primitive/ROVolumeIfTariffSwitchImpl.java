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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitch;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true, lengthIndefinite = false)
public class ROVolumeIfTariffSwitchImpl implements ROVolumeIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger roVolumeSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger roVolumeTariffSwitchInterval;

    public ROVolumeIfTariffSwitchImpl() {
    }

    public ROVolumeIfTariffSwitchImpl(Integer roVolumeSinceLastTariffSwitch, Integer roVolumeTariffSwitchInterval) {
    	if(roVolumeSinceLastTariffSwitch!=null)
    		this.roVolumeSinceLastTariffSwitch = new ASNInteger(roVolumeSinceLastTariffSwitch);
    		
    	if(roVolumeTariffSwitchInterval!=null)
    		this.roVolumeTariffSwitchInterval = new ASNInteger(roVolumeTariffSwitchInterval);    		
    }

    public Integer getROVolumeSinceLastTariffSwitch() {
    	if(this.roVolumeSinceLastTariffSwitch==null)
    		return null;
    	
        return this.roVolumeSinceLastTariffSwitch.getIntValue();
    }

    public Integer getROVolumeTariffSwitchInterval() {
    	if(this.roVolumeTariffSwitchInterval==null)
    		return null;
    	
        return this.roVolumeTariffSwitchInterval.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ROVolumeIfTariffSwitch [");

        if (this.roVolumeSinceLastTariffSwitch != null && this.roVolumeSinceLastTariffSwitch.getValue()!=null) {
            sb.append("roVolumeSinceLastTariffSwitch=");
            sb.append(this.roVolumeSinceLastTariffSwitch.getValue().toString());
            sb.append(", ");
        }

        if (this.roVolumeTariffSwitchInterval != null && this.roVolumeTariffSwitchInterval.getValue()!=null) {
            sb.append("roVolumeTariffSwitchInterval=");
            sb.append(this.roVolumeTariffSwitchInterval.getValue().toString());
        }

        sb.append("]");

        return sb.toString();
    }

}
