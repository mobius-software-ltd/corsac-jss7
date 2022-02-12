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

package org.restcomm.protocols.ss7.inap.charging;

import org.restcomm.protocols.ss7.inap.api.charging.ChargingControlIndicators;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 *
 *
 * @author yulian.oifa
 *
 */
public class ChargingControlIndicatorsImpl extends ASNBitString implements ChargingControlIndicators {
	private static final int _ID_subscriberCharge = 0;
    private static final int _ID_immediateChangeOfActuallyAppliedTariff = 1;
    private static final int _ID_delayUntilStart = 2;
    
    public ChargingControlIndicatorsImpl() {
    	super("ChargingControlIndicators",0,7,false);    	
    }
    
    public ChargingControlIndicatorsImpl(boolean subscriberCharge, boolean immediateChangeOfActuallyAppliedTariff, 
    		boolean delayUntilStart) {
    	super("ChargingControlIndicators",1,8,false);
        if (subscriberCharge)
            this.setBit(_ID_subscriberCharge);
        if (immediateChangeOfActuallyAppliedTariff)
            this.setBit(_ID_immediateChangeOfActuallyAppliedTariff);
        if (delayUntilStart)
            this.setBit(_ID_delayUntilStart);
    }
    
	@Override
	public boolean getSubscriberCharge() 
	{
		return this.isBitSet(_ID_subscriberCharge);
	}

	@Override
	public boolean getImmediateChangeOfActuallyAppliedTariff() {
		return this.isBitSet(_ID_immediateChangeOfActuallyAppliedTariff);
	}

	@Override
	public boolean getDelayUntilStart() {
		return this.isBitSet(_ID_delayUntilStart);
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChargingControlIndicators [");

        if (getSubscriberCharge())
            sb.append("subscriberCharge, ");
        if (getImmediateChangeOfActuallyAppliedTariff())
            sb.append("immediateChangeOfActuallyAppliedTariff, ");
        if (getDelayUntilStart())
            sb.append("delayUntilStart, ");
        
        sb.append("]");

        return sb.toString();
    }
}
