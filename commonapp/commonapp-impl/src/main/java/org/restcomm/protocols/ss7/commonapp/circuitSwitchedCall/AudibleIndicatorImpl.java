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

package org.restcomm.protocols.ss7.commonapp.circuitSwitchedCall;

import org.restcomm.protocols.ss7.commonapp.api.circuitSwitchedCall.AudibleIndicator;
import org.restcomm.protocols.ss7.commonapp.api.primitives.BurstList;
import org.restcomm.protocols.ss7.commonapp.primitives.BurstListImpl;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;
import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBoolean;

/**
*
* @author sergey vetyutnev
* @author yulianoifa
*
*/
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class AudibleIndicatorImpl implements AudibleIndicator {
	@ASNProperty(asnClass = ASNClass.UNIVERSAL,tag = 1,constructed = false,index = -1)
    private ASNBoolean tone;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1,defaultImplementation = BurstListImpl.class)
    private BurstList burstList;

    public AudibleIndicatorImpl() {
    }

    public AudibleIndicatorImpl(Boolean tone) {
    	if(tone!=null)
    		this.tone = new ASNBoolean(tone,"Tone",true,false);    		
    }

    public AudibleIndicatorImpl(BurstList burstList) {
        this.burstList = burstList;
    }

    public Boolean getTone() {
    	if(tone==null)
    		return null;
    	
        return tone.getValue();
    }

    public BurstList getBurstList() {
        return burstList;
    }
    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("AudibleIndicator [");

        if (tone != null) {
            sb.append("tone=[");
            sb.append(tone.getValue());
            sb.append("]");
        } else if (burstList != null) {
            sb.append("burstList=[");
            sb.append(burstList);
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(tone==null && burstList==null)
			throw new ASNParsingComponentException("either tone or burst list is required for Audible indicator", ASNParsingComponentExceptionReason.MistypedParameter);
	}
}
