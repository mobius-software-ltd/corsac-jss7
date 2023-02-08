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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.VolumeIfTariffSwitch;

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
@ASNTag(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,lengthIndefinite = false)
public class VolumeIfTariffSwitchImpl implements VolumeIfTariffSwitch {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger volumeSinceLastTariffSwitch;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger volumeTariffSwitchInterval;

    public VolumeIfTariffSwitchImpl() {
    }

    public VolumeIfTariffSwitchImpl(long volumeSinceLastTariffSwitch, Long volumeTariffSwitchInterval) {
        this.volumeSinceLastTariffSwitch = new ASNInteger(volumeSinceLastTariffSwitch,"VolumeSinceLastTariffSwitch",0L,4294967295L,false);
        
        if(volumeTariffSwitchInterval!=null)
        	this.volumeTariffSwitchInterval = new ASNInteger(volumeTariffSwitchInterval,"VolumeTariffSwitchInterval",0L,4294967295L,false);        	
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

	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(volumeSinceLastTariffSwitch==null)
			throw new ASNParsingComponentException("volume since last tariff switch should be set for volume if tariff switch", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
