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

import org.restcomm.protocols.ss7.inap.api.charging.TariffControlIndicators;

import com.mobius.software.telco.protocols.ss7.asn.primitives.ASNBitString;

/**
 *
 *
 *
 * @author yulian.oifa
 *
 */
public class TariffControlIndicatorsImpl extends ASNBitString implements TariffControlIndicators {
	private static final int _ID_noncyclicTariff = 0;
    
    public TariffControlIndicatorsImpl() {
    	super("TariffControlIndicators",0,7,false);
    	
    }
    
    public TariffControlIndicatorsImpl(boolean noncyclicTariff) {
    	super("TariffControlIndicators",0,7,false);
        if (noncyclicTariff)
            this.setBit(_ID_noncyclicTariff);
    }
    
	@Override
	public boolean getNonCyclicTariff() 
	{
		return this.isBitSet(_ID_noncyclicTariff);
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TariffControlIndicators [");

        if (getNonCyclicTariff())
            sb.append("nonCyclicTariff ");
        
        sb.append("]");

        return sb.toString();
    }
}
