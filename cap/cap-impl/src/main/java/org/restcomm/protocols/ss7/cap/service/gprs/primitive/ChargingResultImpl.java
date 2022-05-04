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

package org.restcomm.protocols.ss7.cap.service.gprs.primitive;

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingResult;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTime;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolume;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNValidate;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentException;
import com.mobius.software.telco.protocols.ss7.asn.exceptions.ASNParsingComponentExceptionReason;

/**
 *
 * @author Lasith Waruna Perera
 * @author yulianoifa
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingResultImpl implements ChargingResult {
	@ASNProperty(asnClass =ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private TransferredVolumeWrapperImpl transferredVolume;
    
    @ASNProperty(asnClass =ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private ElapsedTimeWrapperImpl elapsedTime;

    public ChargingResultImpl() {

    }

    public ChargingResultImpl(TransferredVolume transferredVolume) {
    	if(transferredVolume!=null)
    		this.transferredVolume = new TransferredVolumeWrapperImpl(transferredVolume);
    }

    public ChargingResultImpl(ElapsedTime elapsedTime) {
    	if(elapsedTime!=null)
    		this.elapsedTime = new ElapsedTimeWrapperImpl(elapsedTime);
    }

    public TransferredVolume getTransferredVolume() {
    	if(transferredVolume==null)
    		return null;
    	
        return this.transferredVolume.getTransferredVolume();
    }

    public ElapsedTime getElapsedTime() {
    	if(elapsedTime==null)
    		return null;
    	
        return this.elapsedTime.getElapsedTime();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChargingResult [");

        if (this.transferredVolume != null) {
            sb.append("transferredVolume=");
            sb.append(this.transferredVolume.toString());
        }

        if (this.elapsedTime != null) {
            sb.append("elapsedTime=");
            sb.append(this.elapsedTime.toString());
        }

        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(transferredVolume==null && elapsedTime==null)
			throw new ASNParsingComponentException("either transferred volume or elapsed time should be set for charging result", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}
