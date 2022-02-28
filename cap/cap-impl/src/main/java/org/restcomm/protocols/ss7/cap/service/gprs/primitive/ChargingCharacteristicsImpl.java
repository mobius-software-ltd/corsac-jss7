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