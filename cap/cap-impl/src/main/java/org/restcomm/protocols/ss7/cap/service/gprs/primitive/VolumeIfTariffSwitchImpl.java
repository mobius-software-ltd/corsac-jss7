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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitch;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,lengthIndefinite = false)
public class VolumeIfTariffSwitchImpl implements VolumeIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger volumeSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger volumeTariffSwitchInterval;

    public VolumeIfTariffSwitchImpl() {
    }

    public VolumeIfTariffSwitchImpl(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval) {
        this.volumeSinceLastTariffSwitch = new ASNInteger(volumeSinceLastTariffSwitch);
        
        if(volumeTariffSwitchInterval!=null)
        	this.volumeTariffSwitchInterval = new ASNInteger(volumeTariffSwitchInterval);        	
    }

    public long getVolumeSinceLastTariffSwitch() {
    	if(this.volumeSinceLastTariffSwitch==null || this.volumeSinceLastTariffSwitch.getValue()==null)
    		return 0;
    	
        return this.volumeSinceLastTariffSwitch.getValue();
    }

    public Long getVolumeTariffSwitchInterval() {
    	if(this.volumeTariffSwitchInterval==null)
    		return null;
    	
        return this.volumeTariffSwitchInterval.getValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VolumeIfTariffSwitch [");

        sb.append("volumeSinceLastTariffSwitch=");
        sb.append(this.volumeSinceLastTariffSwitch);
        sb.append(", ");

        if (this.volumeTariffSwitchInterval != null) {
            sb.append("volumeTariffSwitchInterval=");
            sb.append(this.volumeTariffSwitchInterval);
        }

        sb.append("]");

        return sb.toString();
    }

}
