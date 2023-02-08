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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingCharacteristics;

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
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingCharacteristicsImpl implements ChargingCharacteristics {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private ASNInteger maxTransferredVolume;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ASNInteger maxElapsedTime;

    public ChargingCharacteristicsImpl() {
    }

    public ChargingCharacteristicsImpl(long maxTransferredVolume) {
		this.maxTransferredVolume = new ASNInteger(maxTransferredVolume,"MaxTransferredVolume",0L,4294967295L,false);		
    }

    public ChargingCharacteristicsImpl(int maxElapsedTime) {
        this.maxElapsedTime = new ASNInteger(maxElapsedTime,"MaxElapsedTime",0,86400,false);
    }

    public long getMaxTransferredVolume() {
    	if(maxTransferredVolume==null || this.maxTransferredVolume.getValue()==null)
    		return -1;
    	
        return this.maxTransferredVolume.getValue();
    }

    public int getMaxElapsedTime() {
    	if(this.maxElapsedTime==null || this.maxElapsedTime.getValue()==null)
    		return -1;
    	
        return this.maxElapsedTime.getIntValue();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChargingCharacteristics [");

        if (this.maxTransferredVolume != null && this.maxTransferredVolume.getValue()!=null) {
            sb.append("maxTransferredVolume=");
            sb.append(this.maxTransferredVolume.getValue());
        }

        if (this.maxElapsedTime != null && this.maxElapsedTime.getValue()!=null) {
            sb.append("maxElapsedTime=");
            sb.append(this.maxElapsedTime.getValue());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(maxTransferredVolume==null && maxElapsedTime==null)
			throw new ASNParsingComponentException("either max transferred volume or max elapsed time should be set for charging characteristics", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}