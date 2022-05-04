/*
 * TeleStax, Open Source Cloud Communications
 * Mobius Software LTD
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.restcomm.protocols.ss7.commonapp.gap;

import org.restcomm.protocols.ss7.commonapp.api.gap.GapIndicators;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNInteger;

/**
 *
 * @author <a href="mailto:bartosz.krok@pro-ids.com"> Bartosz Krok (ProIDS sp. z o.o.)</a>
 * @author yulianoifa
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class GapIndicatorsImpl implements GapIndicators {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
	private ASNInteger duration;
    
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
	private ASNInteger gapInterval;

    public GapIndicatorsImpl() {
    }

    public GapIndicatorsImpl(int duration, int gapInterval) {
        this.duration = new ASNInteger(duration,"Duration",-2,86400,false);
        this.gapInterval = new ASNInteger(gapInterval,"GapInterval",-1,60000,false);        
    }

    public int getDuration() {
    	if(duration==null || duration.getValue()==null)
    		return 0;
    	
        return duration.getIntValue();
    }

    public int getGapInterval() {
    	if(gapInterval==null || gapInterval.getValue()==null)
    		return 0;
    	
        return gapInterval.getIntValue();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("GapIndicators [");

        sb.append("duration=");
        sb.append(duration);
        sb.append(", gapInterval=");
        sb.append(gapInterval);

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(duration==null)
			throw new ASNParsingComponentException("duration should be set for gap indicators", ASNParsingComponentExceptionReason.MistypedParameter);		

		if(gapInterval==null)
			throw new ASNParsingComponentException("gap interval should be set for gap indicators", ASNParsingComponentExceptionReason.MistypedParameter);		
	}
}