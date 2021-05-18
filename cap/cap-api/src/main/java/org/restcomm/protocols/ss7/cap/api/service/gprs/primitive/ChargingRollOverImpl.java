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
package org.restcomm.protocols.ss7.cap.api.service.gprs.primitive;

import com.mobius.software.telco.protocols.ss7.asn.ASNClass;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNProperty;
import com.mobius.software.telco.protocols.ss7.asn.annotations.ASNTag;

/**
 *
 * @author Lasith Waruna Perera
 *
 */
@ASNTag(asnClass = ASNClass.UNIVERSAL,tag = 16,constructed = true,lengthIndefinite = false)
public class ChargingRollOverImpl {
	@ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = true,index = -1)
    private TransferVolumeRollOverWrapperImpl transferredVolumeRollOver1;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 0,constructed = false,index = -1)
    private TransferVolumeRollOverWrapperImpl transferredVolumeRollOver2;
    
    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = true,index = -1)
    private ElapsedTimeRollOverWrapperImpl elapsedTimeRollOver1;

    @ASNProperty(asnClass = ASNClass.CONTEXT_SPECIFIC,tag = 1,constructed = false,index = -1)
    private ElapsedTimeRollOverWrapperImpl elapsedTimeRollOver2;

    public ChargingRollOverImpl() {
    }

    public ChargingRollOverImpl(TransferredVolumeRollOverImpl transferredVolumeRollOver) {
    	if(transferredVolumeRollOver!=null) {
    		if(transferredVolumeRollOver.getROVolumeIfTariffSwitch()!=null)
    			this.transferredVolumeRollOver1 = new TransferVolumeRollOverWrapperImpl(transferredVolumeRollOver);
    		else
    			this.transferredVolumeRollOver2 = new TransferVolumeRollOverWrapperImpl(transferredVolumeRollOver);
    	}
    }

    public ChargingRollOverImpl(ElapsedTimeRollOverImpl elapsedTimeRollOver) {
    	if(elapsedTimeRollOver!=null) {
    		if(elapsedTimeRollOver.getROTimeGPRSIfTariffSwitch()!=null)
    			this.elapsedTimeRollOver1 = new ElapsedTimeRollOverWrapperImpl(elapsedTimeRollOver);
    		else
    			this.elapsedTimeRollOver2 = new ElapsedTimeRollOverWrapperImpl(elapsedTimeRollOver);
    	}
    }

    public TransferredVolumeRollOverImpl getTransferredVolumeRollOver() {
    	if(this.transferredVolumeRollOver1!=null)
    		return this.transferredVolumeRollOver1.getTransferredVolumeRollOver();
    	else if(this.transferredVolumeRollOver2!=null)
    		return this.transferredVolumeRollOver2.getTransferredVolumeRollOver();
    	
    	return null;
    }

    public ElapsedTimeRollOverImpl getElapsedTimeRollOver() {
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
}