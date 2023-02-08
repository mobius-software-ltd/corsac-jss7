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

import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ChargingRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.ElapsedTimeRollOver;
import org.restcomm.protocols.ss7.cap.api.service.gprs.primitive.TransferredVolumeRollOver;

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
public class ChargingRollOverImpl implements ChargingRollOver {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private TransferredVolumeRollOverWrapperImpl transferredVolumeRollOver1;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private TransferredVolumeRollOverWrapperImpl transferredVolumeRollOver2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private ElapsedTimeRollOverWrapperImpl elapsedTimeRollOver1;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ElapsedTimeRollOverWrapperImpl elapsedTimeRollOver2;

    public ChargingRollOverImpl() {
    }

    public ChargingRollOverImpl(TransferredVolumeRollOver transferredVolumeRollOver) {
    	if(transferredVolumeRollOver!=null) {
    		if(transferredVolumeRollOver.getROVolumeIfTariffSwitch()!=null)
    			this.transferredVolumeRollOver1 = new TransferredVolumeRollOverWrapperImpl(transferredVolumeRollOver);
    		else
    			this.transferredVolumeRollOver2 = new TransferredVolumeRollOverWrapperImpl(transferredVolumeRollOver);
    	}
    }

    public ChargingRollOverImpl(ElapsedTimeRollOver elapsedTimeRollOver) {
    	if(elapsedTimeRollOver!=null) {
    		if(elapsedTimeRollOver.getROTimeGPRSIfTariffSwitch()!=null)
    			this.elapsedTimeRollOver1 = new ElapsedTimeRollOverWrapperImpl(elapsedTimeRollOver);
    		else
    			this.elapsedTimeRollOver2 = new ElapsedTimeRollOverWrapperImpl(elapsedTimeRollOver);
    	}
    }

    public TransferredVolumeRollOver getTransferredVolumeRollOver() {
    	if(this.transferredVolumeRollOver1!=null)
    		return this.transferredVolumeRollOver1.getTransferredVolumeRollOver();
    	else if(this.transferredVolumeRollOver2!=null)
    		return this.transferredVolumeRollOver2.getTransferredVolumeRollOver();
    	
    	return null;
    }

    public ElapsedTimeRollOver getElapsedTimeRollOver() {
    	if(this.elapsedTimeRollOver1!=null)
    		return this.elapsedTimeRollOver1.getElapsedTimeRollOver();
    	else if(this.elapsedTimeRollOver2!=null)
    		return this.elapsedTimeRollOver2.getElapsedTimeRollOver();
    	
    	return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChargingRollOver [");

        if (this.transferredVolumeRollOver1 != null && this.transferredVolumeRollOver1.getTransferredVolumeRollOver()!=null) {
            sb.append("transferredVolumeRollOver=");
            sb.append(this.transferredVolumeRollOver1.getTransferredVolumeRollOver().toString());
        }
        
        if (this.transferredVolumeRollOver2 != null && this.transferredVolumeRollOver2.getTransferredVolumeRollOver()!=null) {
            sb.append("transferredVolumeRollOver=");
            sb.append(this.transferredVolumeRollOver1.getTransferredVolumeRollOver().toString());
        }

        if (this.elapsedTimeRollOver1 != null && this.elapsedTimeRollOver1.getElapsedTimeRollOver() != null) {
            sb.append("elapsedTimeRollOver=");
            sb.append(this.elapsedTimeRollOver1.getElapsedTimeRollOver().toString());
        }

        if (this.elapsedTimeRollOver2 != null && this.elapsedTimeRollOver2.getElapsedTimeRollOver() != null) {
            sb.append("elapsedTimeRollOver=");
            sb.append(this.elapsedTimeRollOver2.getElapsedTimeRollOver().toString());
        }
        
        sb.append("]");

        return sb.toString();
    }
	
	@ASNValidate
	public void validateElement() throws ASNParsingComponentException {
		if(transferredVolumeRollOver1==null && transferredVolumeRollOver2==null && elapsedTimeRollOver1==null && elapsedTimeRollOver2==null)
			throw new ASNParsingComponentException("one of child items should be set for charging rollover", ASNParsingComponentExceptionReason.MistypedParameter);			
	}
}